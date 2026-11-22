package tiketkereta.ui.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import tiketkereta.model.Pemesanan;
import tiketkereta.model.Penumpang;
import tiketkereta.ui.MainFrame;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.Tema;
import tiketkereta.util.ValidasiException;

public class KelolaPemesananPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;
    private DefaultTableModel tableModel;
    private ArrayList<Pemesanan> daftarTampil;

    public KelolaPemesananPanel() {
        this(null);
    }

    public KelolaPemesananPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.daftarTampil = new ArrayList<Pemesanan>();
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlHeader.setOpaque(false);
        pnlTitleBox.setOpaque(false);
        pnlFilter.setOpaque(false);
        Tema.styleCard(pnlCard, 16, 18);
        pnlActions.setOpaque(false);
        pnlActionButtons.setOpaque(false);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblSubtitle);
        Tema.styleBody(lblFilter);
        Tema.styleMuted(lblInfo);

        cmbFilter.setModel(new DefaultComboBoxModel<String>(new String[]{"Semua", "Dipesan", "Lunas", "Dibatalkan"}));
        Tema.styleComboBox(cmbFilter);

        Tema.styleSecondaryButton(btnRefresh);
        Tema.stylePrimaryButton(btnDetail);
        Tema.styleSuccessButton(btnTandaiLunas);
        Tema.styleDangerButton(btnBatalkan);

        tableModel = new DefaultTableModel(
                new Object[]{"Kode Booking", "Pemesan", "Kereta", "Kelas", "Rute", "Kursi", "Total", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPemesanan.setModel(tableModel);
        tblPemesanan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Tema.styleTable(tblPemesanan);
        tblPemesanan.getColumnModel().getColumn(7).setCellRenderer(Tema.createStatusRenderer());
        Tema.styleScrollPane(scrPemesanan);
    }

    private void setupEvents() {
        cmbFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        btnDetail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tampilkanDetail();
            }
        });
        btnTandaiLunas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ubahStatus(Pemesanan.STATUS_LUNAS);
            }
        });
        btnBatalkan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ubahStatus(Pemesanan.STATUS_DIBATALKAN);
            }
        });
    }

    public void refreshData() {
        if (mainFrame == null) {
            return;
        }
        String filter = String.valueOf(cmbFilter.getSelectedItem());
        ArrayList<Pemesanan> semua = mainFrame.getPemesananService().getSemuaPemesanan();
        daftarTampil = new ArrayList<Pemesanan>();
        tableModel.setRowCount(0);

        for (Pemesanan p : semua) {
            if (!"Semua".equalsIgnoreCase(filter) && !p.getStatus().equalsIgnoreCase(filter)) {
                continue;
            }
            daftarTampil.add(p);
            tableModel.addRow(new Object[]{
                p.getBookingCode(),
                p.getUserIdentifier(),
                p.getTrainNumber() + " " + p.getTrainName(),
                p.getKelas(),
                p.getOriginCode() + " → " + p.getDestinationCode(),
                p.getDaftarKursiRingkas(),
                FormatUtil.formatRupiah(p.getTotalBayar()),
                p.getStatus()
            });
        }
        lblInfo.setText("Menampilkan " + daftarTampil.size() + " pesanan.");
    }

    private Pemesanan getSelected() {
        int row = tblPemesanan.getSelectedRow();
        if (row < 0 || row >= daftarTampil.size()) {
            JOptionPane.showMessageDialog(this, "Pilih pesanan pada tabel terlebih dahulu.",
                    "Belum Ada Pilihan", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return daftarTampil.get(row);
    }

    private void tampilkanDetail() {
        Pemesanan p = getSelected();
        if (p == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Kode Booking : ").append(p.getBookingCode()).append("\n");
        sb.append("Pemesan      : ").append(p.getUserIdentifier()).append("\n");
        sb.append("Kereta       : ").append(p.getTrainNumber()).append(" - ").append(p.getTrainName())
                .append(" (").append(p.getKelas()).append(")\n");
        sb.append("Rute         : ").append(p.getOriginCode()).append(" → ").append(p.getDestinationCode())
                .append(" (").append(p.getDepartureTime()).append(" - ").append(p.getArrivalTime()).append(")\n");
        sb.append("Total Bayar  : ").append(FormatUtil.formatRupiah(p.getTotalBayar())).append("\n");
        sb.append("Status       : ").append(p.getStatus()).append("\n\n");
        sb.append("Daftar Penumpang:\n");
        int no = 1;
        for (Penumpang pen : p.getPenumpangList()) {
            sb.append(no++).append(". ").append(pen.getNama())
                    .append(" [").append(pen.getIdType()).append(" ").append(pen.getIdNumber()).append("]")
                    .append(" - Kursi ").append(pen.getKodeKursi()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Detail Pesanan " + p.getBookingCode(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void ubahStatus(String statusBaru) {
        Pemesanan p = getSelected();
        if (p == null || mainFrame == null) {
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Ubah status pesanan " + p.getBookingCode() + " menjadi '" + statusBaru + "'?",
                "Konfirmasi Status", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            mainFrame.getPemesananService().ubahStatusAdmin(p.getBookingCode(), statusBaru);
            refreshData();
            JOptionPane.showMessageDialog(this, "Status pesanan " + p.getBookingCode() + " diubah menjadi " + statusBaru + ".",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal Ubah Status", JOptionPane.WARNING_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        pnlTitleBox = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlFilter = new javax.swing.JPanel();
        lblFilter = new javax.swing.JLabel();
        cmbFilter = new javax.swing.JComboBox<>();
        btnRefresh = new javax.swing.JButton();
        pnlCard = new javax.swing.JPanel();
        scrPemesanan = new javax.swing.JScrollPane();
        tblPemesanan = new javax.swing.JTable();
        pnlActions = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();
        pnlActionButtons = new javax.swing.JPanel();
        btnDetail = new javax.swing.JButton();
        btnTandaiLunas = new javax.swing.JButton();
        btnBatalkan = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setLayout(new java.awt.BorderLayout(12, 0));

        pnlTitleBox.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Kelola Pemesanan");
        pnlTitleBox.add(lblTitle);

        lblSubtitle.setText("Pantau seluruh transaksi tiket dan ubah status menjadi Lunas atau Dibatalkan");
        pnlTitleBox.add(lblSubtitle);

        pnlHeader.add(pnlTitleBox, java.awt.BorderLayout.CENTER);

        pnlFilter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 4));

        lblFilter.setText("Status:");
        pnlFilter.add(lblFilter);

        pnlFilter.add(cmbFilter);

        btnRefresh.setText("Muat Ulang");
        pnlFilter.add(btnRefresh);

        pnlHeader.add(pnlFilter, java.awt.BorderLayout.EAST);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlCard.setLayout(new java.awt.BorderLayout(0, 14));

        scrPemesanan.setViewportView(tblPemesanan);

        pnlCard.add(scrPemesanan, java.awt.BorderLayout.CENTER);

        pnlActions.setLayout(new java.awt.BorderLayout());

        lblInfo.setText("0 pesanan ditampilkan");
        pnlActions.add(lblInfo, java.awt.BorderLayout.WEST);

        pnlActionButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));

        btnDetail.setText("Lihat Detail");
        pnlActionButtons.add(btnDetail);

        btnTandaiLunas.setText("Tandai Lunas");
        pnlActionButtons.add(btnTandaiLunas);

        btnBatalkan.setText("Batalkan Pesanan");
        pnlActionButtons.add(btnBatalkan);

        pnlActions.add(pnlActionButtons, java.awt.BorderLayout.EAST);

        pnlCard.add(pnlActions, java.awt.BorderLayout.SOUTH);

        add(pnlCard, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatalkan;
    private javax.swing.JButton btnDetail;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnTandaiLunas;
    private javax.swing.JComboBox<String> cmbFilter;
    private javax.swing.JLabel lblFilter;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JPanel pnlFilter;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlTitleBox;
    private javax.swing.JScrollPane scrPemesanan;
    private javax.swing.JTable tblPemesanan;
    // End of variables declaration//GEN-END:variables
}
