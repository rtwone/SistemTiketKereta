package tiketkereta.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import tiketkereta.model.Pemesanan;
import tiketkereta.model.User;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.Tema;
import tiketkereta.util.ValidasiException;

public class TiketSayaPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;
    private DefaultTableModel tableModel;
    private ArrayList<Pemesanan> daftarPesanan;

    public TiketSayaPanel() {
        this(null);
    }

    public TiketSayaPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.daftarPesanan = new ArrayList<Pemesanan>();
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlHeader.setOpaque(false);
        pnlTitleBox.setOpaque(false);
        pnlFilterBar.setOpaque(false);
        Tema.styleCard(pnlCard, 18, 20);
        Tema.styleSoftCard(pnlActions, 10, 14);
        pnlActionButtons.setOpaque(false);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblSubtitle);
        Tema.styleBody(lblFilterStatus);
        lblInfo.setFont(Tema.FONT_BODY_BOLD);
        lblInfo.setForeground(Tema.PRIMARY);

        cmbFilterStatus.setModel(new DefaultComboBoxModel<String>(
                new String[]{"Semua Status", "Dipesan", "Lunas", "Dibatalkan"}));
        Tema.styleComboBox(cmbFilterStatus);

        Tema.styleSecondaryButton(btnRefresh);
        Tema.stylePrimaryButton(btnLihatETiket);
        Tema.styleSuccessButton(btnCetakTiket);
        Tema.styleAccentButton(btnBayar);
        Tema.styleDangerButton(btnBatalkan);

        tableModel = new DefaultTableModel(
                new Object[]{"Kode Booking", "Kereta", "Kelas", "Rute", "Jam", "Kursi", "Total", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblTiket.setModel(tableModel);
        tblTiket.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Tema.styleTable(tblTiket);
        tblTiket.getColumnModel().getColumn(7).setCellRenderer(Tema.createStatusRenderer());
        Tema.styleScrollPane(scrTiket);
    }

    private void setupEvents() {
        cmbFilterStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                muatTabel();
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });

        tblTiket.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    perbaruiStatusTombol();
                }
            }
        });

        tblTiket.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Pemesanan p = getSelectedPesanan(false);
                    if (p != null && mainFrame != null) {
                        mainFrame.tampilkanETiket(p);
                    }
                }
            }
        });

        btnLihatETiket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pemesanan p = getSelectedPesanan(true);
                if (p != null && mainFrame != null) {
                    mainFrame.tampilkanETiket(p);
                }
            }
        });

        btnCetakTiket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pemesanan p = getSelectedPesanan(true);
                if (p != null && mainFrame != null) {
                    mainFrame.mulaiCetakTiket(p);
                }
            }
        });

        btnBayar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pemesanan p = getSelectedPesanan(true);
                if (p == null || mainFrame == null) {
                    return;
                }
                if (!Pemesanan.STATUS_DIPESAN.equalsIgnoreCase(p.getStatus())) {
                    JOptionPane.showMessageDialog(TiketSayaPanel.this,
                            "Hanya pesanan berstatus Dipesan yang dapat dibayar.",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                mainFrame.tampilkanPembayaran(p);
            }
        });

        btnBatalkan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesBatalkan();
            }
        });
    }

    public void refreshData() {
        cmbFilterStatus.setSelectedIndex(0);
        muatTabel();
    }

    private void muatTabel() {
        if (mainFrame == null) {
            return;
        }
        User user = mainFrame.getAuthService().getCurrentUser();
        if (user == null) {
            return;
        }
        String filter = String.valueOf(cmbFilterStatus.getSelectedItem());
        ArrayList<Pemesanan> semua = mainFrame.getPemesananService().getPemesananByUser(user.getIdentifier());
        daftarPesanan = new ArrayList<Pemesanan>();
        tableModel.setRowCount(0);

        for (Pemesanan p : semua) {
            if (!"Semua Status".equalsIgnoreCase(filter) && !p.getStatus().equalsIgnoreCase(filter)) {
                continue;
            }
            daftarPesanan.add(p);
            tableModel.addRow(new Object[]{
                p.getBookingCode(),
                p.getTrainNumber() + " " + p.getTrainName(),
                p.getKelas(),
                p.getOriginCode() + " → " + p.getDestinationCode(),
                p.getDepartureTime() + " - " + p.getArrivalTime(),
                p.getDaftarKursiRingkas(),
                FormatUtil.formatRupiah(p.getTotalBayar()),
                p.getStatus()
            });
        }
        if (!daftarPesanan.isEmpty()) {
            tblTiket.setRowSelectionInterval(0, 0);
        }
        perbaruiStatusTombol();
    }

    private void perbaruiStatusTombol() {
        int row = tblTiket.getSelectedRow();
        if (row < 0 || row >= daftarPesanan.size()) {
            lblInfo.setText("Menampilkan " + daftarPesanan.size() + " pesanan.");
            btnLihatETiket.setEnabled(false);
            btnCetakTiket.setEnabled(false);
            btnBayar.setEnabled(false);
            btnBatalkan.setEnabled(false);
            return;
        }
        Pemesanan p = daftarPesanan.get(row);
        lblInfo.setText("Terpilih: " + p.getBookingCode() + "  •  Status: " + p.getStatus());
        btnLihatETiket.setEnabled(true);
        btnCetakTiket.setEnabled(Pemesanan.STATUS_LUNAS.equalsIgnoreCase(p.getStatus()));
        btnBayar.setEnabled(Pemesanan.STATUS_DIPESAN.equalsIgnoreCase(p.getStatus()));
        btnBatalkan.setEnabled(!Pemesanan.STATUS_DIBATALKAN.equalsIgnoreCase(p.getStatus()));
    }

    private Pemesanan getSelectedPesanan(boolean tampilkanPeringatan) {
        int row = tblTiket.getSelectedRow();
        if (row < 0 || row >= daftarPesanan.size()) {
            if (tampilkanPeringatan) {
                JOptionPane.showMessageDialog(this, "Pilih salah satu tiket pada tabel terlebih dahulu.",
                        "Belum Ada Pilihan", JOptionPane.WARNING_MESSAGE);
            }
            return null;
        }
        return daftarPesanan.get(row);
    }

    private void prosesBatalkan() {
        Pemesanan p = getSelectedPesanan(true);
        if (p == null || mainFrame == null) {
            return;
        }
        if (Pemesanan.STATUS_DIBATALKAN.equalsIgnoreCase(p.getStatus())) {
            JOptionPane.showMessageDialog(this, "Tiket ini sudah dibatalkan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Batalkan pesanan " + p.getBookingCode() + " (" + p.getOriginCode() + " → " + p.getDestinationCode() + ")?\n"
                + "Kursi akan dilepas kembali.",
                "Konfirmasi Pembatalan",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            mainFrame.getPemesananService().batalkanPemesanan(p.getBookingCode());
            muatTabel();
            JOptionPane.showMessageDialog(this, "Tiket " + p.getBookingCode() + " berhasil dibatalkan.",
                    "Dibatalkan", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal Membatalkan", JOptionPane.WARNING_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        pnlTitleBox = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlFilterBar = new javax.swing.JPanel();
        lblFilterStatus = new javax.swing.JLabel();
        cmbFilterStatus = new javax.swing.JComboBox<>();
        btnRefresh = new javax.swing.JButton();
        pnlCard = new javax.swing.JPanel();
        scrTiket = new javax.swing.JScrollPane();
        tblTiket = new javax.swing.JTable();
        pnlActions = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();
        pnlActionButtons = new javax.swing.JPanel();
        btnLihatETiket = new javax.swing.JButton();
        btnCetakTiket = new javax.swing.JButton();
        btnBayar = new javax.swing.JButton();
        btnBatalkan = new javax.swing.JButton();

        setBackground(new java.awt.Color(245, 247, 250));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 26, 22, 26));
        setPreferredSize(new java.awt.Dimension(1120, 680));
        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setOpaque(false);
        pnlHeader.setLayout(new java.awt.BorderLayout(12, 0));

        pnlTitleBox.setOpaque(false);
        pnlTitleBox.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Tiket Saya");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 20));
        lblTitle.setForeground(new java.awt.Color(27, 54, 93));
        pnlTitleBox.add(lblTitle);

        lblSubtitle.setText("Klik dua kali baris pesanan untuk membuka E-Tiket, atau kelola pembayaran & pembatalan");
        lblSubtitle.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblSubtitle.setForeground(new java.awt.Color(100, 116, 139));
        pnlTitleBox.add(lblSubtitle);
        pnlHeader.add(pnlTitleBox, java.awt.BorderLayout.CENTER);

        pnlFilterBar.setOpaque(false);
        pnlFilterBar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 4));

        lblFilterStatus.setText("Status:");
        lblFilterStatus.setFont(new java.awt.Font("Segoe UI", 0, 13));
        lblFilterStatus.setForeground(new java.awt.Color(15, 23, 42));
        pnlFilterBar.add(lblFilterStatus);

        cmbFilterStatus.setFont(new java.awt.Font("Segoe UI", 0, 13));
        cmbFilterStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Status", "Dipesan", "Lunas", "Dibatalkan" }));
        cmbFilterStatus.setPreferredSize(new java.awt.Dimension(170, 38));
        pnlFilterBar.add(cmbFilterStatus);

        btnRefresh.setText("Muat Ulang");
        btnRefresh.setBackground(new java.awt.Color(255, 255, 255));
        btnRefresh.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnRefresh.setForeground(new java.awt.Color(15, 23, 42));
        btnRefresh.setFocusPainted(false);
        pnlFilterBar.add(btnRefresh);
        pnlHeader.add(pnlFilterBar, java.awt.BorderLayout.EAST);
        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        pnlCard.setLayout(new java.awt.BorderLayout(0, 14));

        scrTiket.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        tblTiket.setFont(new java.awt.Font("Segoe UI", 0, 13));
        tblTiket.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode Booking", "Kereta", "Kelas", "Rute", "Jam", "Kursi", "Total", "Status"
            }
        ));
        tblTiket.setRowHeight(34);
        scrTiket.setViewportView(tblTiket);
        pnlCard.add(scrTiket, java.awt.BorderLayout.CENTER);

        pnlActions.setBackground(new java.awt.Color(248, 250, 252));
        pnlActions.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        pnlActions.setLayout(new java.awt.BorderLayout());

        lblInfo.setText("Pilih baris pesanan untuk melakukan aksi");
        lblInfo.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblInfo.setForeground(new java.awt.Color(27, 54, 93));
        pnlActions.add(lblInfo, java.awt.BorderLayout.WEST);

        pnlActionButtons.setOpaque(false);
        pnlActionButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));

        btnLihatETiket.setText("Lihat E-Tiket");
        btnLihatETiket.setBackground(new java.awt.Color(27, 54, 93));
        btnLihatETiket.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnLihatETiket.setForeground(new java.awt.Color(255, 255, 255));
        btnLihatETiket.setFocusPainted(false);
        pnlActionButtons.add(btnLihatETiket);

        btnCetakTiket.setText("Cetak Tiket Fisik");
        btnCetakTiket.setBackground(new java.awt.Color(22, 163, 74));
        btnCetakTiket.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnCetakTiket.setForeground(new java.awt.Color(255, 255, 255));
        btnCetakTiket.setFocusPainted(false);
        pnlActionButtons.add(btnCetakTiket);

        btnBayar.setText("Bayar Sekarang");
        btnBayar.setBackground(new java.awt.Color(243, 112, 33));
        btnBayar.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnBayar.setForeground(new java.awt.Color(255, 255, 255));
        btnBayar.setFocusPainted(false);
        pnlActionButtons.add(btnBayar);

        btnBatalkan.setText("Batalkan Tiket");
        btnBatalkan.setBackground(new java.awt.Color(220, 38, 38));
        btnBatalkan.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnBatalkan.setForeground(new java.awt.Color(255, 255, 255));
        btnBatalkan.setFocusPainted(false);
        pnlActionButtons.add(btnBatalkan);
        pnlActions.add(pnlActionButtons, java.awt.BorderLayout.EAST);
        pnlCard.add(pnlActions, java.awt.BorderLayout.SOUTH);
        add(pnlCard, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatalkan;
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnCetakTiket;
    private javax.swing.JButton btnLihatETiket;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> cmbFilterStatus;
    private javax.swing.JLabel lblFilterStatus;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JPanel pnlFilterBar;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlTitleBox;
    private javax.swing.JScrollPane scrTiket;
    private javax.swing.JTable tblTiket;
    // End of variables declaration//GEN-END:variables
}
