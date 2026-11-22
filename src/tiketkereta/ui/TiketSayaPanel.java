package tiketkereta.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
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
        Tema.styleCard(pnlCard, 16, 18);
        pnlActions.setOpaque(false);
        pnlActionButtons.setOpaque(false);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblSubtitle);
        Tema.styleMuted(lblInfo);

        Tema.styleSecondaryButton(btnRefresh);
        Tema.stylePrimaryButton(btnLihatETiket);
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
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });

        btnLihatETiket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pemesanan p = getSelectedPesanan();
                if (p != null && mainFrame != null) {
                    mainFrame.tampilkanETiket(p);
                }
            }
        });

        btnBayar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pemesanan p = getSelectedPesanan();
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
        if (mainFrame == null) {
            return;
        }
        User user = mainFrame.getAuthService().getCurrentUser();
        if (user == null) {
            return;
        }
        daftarPesanan = mainFrame.getPemesananService().getPemesananByUser(user.getIdentifier());
        tableModel.setRowCount(0);
        for (Pemesanan p : daftarPesanan) {
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
        lblInfo.setText("Menampilkan " + daftarPesanan.size() + " riwayat tiket Anda.");
    }

    private Pemesanan getSelectedPesanan() {
        int row = tblTiket.getSelectedRow();
        if (row < 0 || row >= daftarPesanan.size()) {
            JOptionPane.showMessageDialog(this, "Pilih salah satu tiket pada tabel terlebih dahulu.",
                    "Belum Ada Pilihan", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return daftarPesanan.get(row);
    }

    private void prosesBatalkan() {
        Pemesanan p = getSelectedPesanan();
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
            refreshData();
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
        btnRefresh = new javax.swing.JButton();
        pnlCard = new javax.swing.JPanel();
        scrTiket = new javax.swing.JScrollPane();
        tblTiket = new javax.swing.JTable();
        pnlActions = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();
        pnlActionButtons = new javax.swing.JPanel();
        btnLihatETiket = new javax.swing.JButton();
        btnBayar = new javax.swing.JButton();
        btnBatalkan = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setLayout(new java.awt.BorderLayout(12, 0));

        pnlTitleBox.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Tiket Saya");
        pnlTitleBox.add(lblTitle);

        lblSubtitle.setText("Kelola riwayat pemesanan, lihat E-Tiket, bayar, atau batalkan pesanan");
        pnlTitleBox.add(lblSubtitle);

        pnlHeader.add(pnlTitleBox, java.awt.BorderLayout.CENTER);

        btnRefresh.setText("Muat Ulang");
        pnlHeader.add(btnRefresh, java.awt.BorderLayout.EAST);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlCard.setLayout(new java.awt.BorderLayout(0, 14));

        scrTiket.setViewportView(tblTiket);

        pnlCard.add(scrTiket, java.awt.BorderLayout.CENTER);

        pnlActions.setLayout(new java.awt.BorderLayout());

        lblInfo.setText("Pilih baris pesanan untuk melakukan aksi");
        pnlActions.add(lblInfo, java.awt.BorderLayout.WEST);

        pnlActionButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));

        btnLihatETiket.setText("Lihat E-Tiket");
        pnlActionButtons.add(btnLihatETiket);

        btnBayar.setText("Bayar Sekarang");
        pnlActionButtons.add(btnBayar);

        btnBatalkan.setText("Batalkan Tiket");
        pnlActionButtons.add(btnBatalkan);

        pnlActions.add(pnlActionButtons, java.awt.BorderLayout.EAST);

        pnlCard.add(pnlActions, java.awt.BorderLayout.SOUTH);

        add(pnlCard, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatalkan;
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnLihatETiket;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlTitleBox;
    private javax.swing.JScrollPane scrTiket;
    private javax.swing.JTable tblTiket;
    // End of variables declaration//GEN-END:variables
}
