package tiketkereta.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import tiketkereta.model.Pemesanan;
import tiketkereta.model.Penumpang;
import tiketkereta.model.Stasiun;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.Tema;
import tiketkereta.util.ValidasiException;

public class PembayaranPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;
    private DefaultTableModel tableModel;
    private Pemesanan pemesananAktif;

    public PembayaranPanel() {
        this(null);
    }

    public PembayaranPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlHeader.setOpaque(false);
        Tema.styleCard(pnlCard, 20, 24);
        pnlRingkasan.setOpaque(false);
        pnlTotalBar.setOpaque(false);
        pnlTotalInfo.setOpaque(false);
        pnlButtons.setOpaque(false);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblSubtitle);
        Tema.styleMuted(lblKodeTag);
        Tema.styleMuted(lblKeretaTag);
        Tema.styleMuted(lblRuteTag);
        Tema.styleMuted(lblStatusTag);
        lblKodeVal.setFont(Tema.FONT_SUBTITLE);
        lblKodeVal.setForeground(Tema.PRIMARY);
        lblKeretaVal.setFont(Tema.FONT_BODY_BOLD);
        lblKeretaVal.setForeground(Tema.TEXT_PRIMARY);
        lblRuteVal.setFont(Tema.FONT_BODY_BOLD);
        lblRuteVal.setForeground(Tema.TEXT_PRIMARY);
        lblStatusVal.setFont(Tema.FONT_BODY_BOLD);
        lblStatusVal.setForeground(Tema.STATUS_AMBER);

        Tema.styleMuted(lblRincianHarga);
        lblTotalNominal.setFont(Tema.FONT_KPI);
        lblTotalNominal.setForeground(Tema.ACCENT);

        Tema.styleSecondaryButton(btnBayarNanti);
        Tema.styleAccentButton(btnBayarSekarang);

        tableModel = new DefaultTableModel(
                new Object[]{"No", "Nama Penumpang", "Jenis ID", "No. Identitas", "Kode Kursi"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPenumpang.setModel(tableModel);
        Tema.styleTable(tblPenumpang);
        Tema.styleScrollPane(scrPenumpang);
    }

    private void setupEvents() {
        btnBayarNanti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.tampilkanTiketSaya();
                }
            }
        });

        btnBayarSekarang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesBayar();
            }
        });
    }

    public void tampilkanPesanan(Pemesanan pesanan) {
        this.pemesananAktif = pesanan;
        if (pesanan == null) {
            return;
        }
        lblKodeVal.setText(pesanan.getBookingCode());
        lblKeretaVal.setText(pesanan.getTrainNumber() + " - " + pesanan.getTrainName() + " (" + pesanan.getKelas() + ")");

        String labelAsal = pesanan.getOriginCode();
        String labelTujuan = pesanan.getDestinationCode();
        if (mainFrame != null) {
            Stasiun asal = mainFrame.getDataStore().cariStasiunByCode(pesanan.getOriginCode());
            Stasiun tujuan = mainFrame.getDataStore().cariStasiunByCode(pesanan.getDestinationCode());
            if (asal != null) {
                labelAsal = asal.getLabelSingkat();
            }
            if (tujuan != null) {
                labelTujuan = tujuan.getLabelSingkat();
            }
        }
        lblRuteVal.setText(labelAsal + " (" + pesanan.getDepartureTime() + ")  →  "
                + labelTujuan + " (" + pesanan.getArrivalTime() + ")");
        lblStatusVal.setText(pesanan.getStatus());
        lblStatusVal.setForeground(Tema.warnaStatus(pesanan.getStatus()));

        tableModel.setRowCount(0);
        int no = 1;
        for (Penumpang pen : pesanan.getPenumpangList()) {
            tableModel.addRow(new Object[]{
                no++,
                pen.getNama(),
                pen.getIdType(),
                pen.getIdNumber(),
                pen.getKodeKursi()
            });
        }

        int jml = pesanan.getPenumpangList().size();
        lblRincianHarga.setText("Total Bayar (" + jml + " × " + FormatUtil.formatRupiah(pesanan.getTarifPerPenumpang()) + ")");
        lblTotalNominal.setText(FormatUtil.formatRupiah(pesanan.getTotalBayar()));
    }

    private void prosesBayar() {
        if (mainFrame == null || pemesananAktif == null) {
            return;
        }
        try {
            Pemesanan lunas = mainFrame.getPemesananService().bayarPemesanan(pemesananAktif.getBookingCode());
            JOptionPane.showMessageDialog(this,
                    "Pembayaran berhasil! E-Tiket " + lunas.getBookingCode() + " telah diterbitkan.",
                    "Pembayaran Lunas",
                    JOptionPane.INFORMATION_MESSAGE);
            mainFrame.tampilkanETiket(lunas);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Pembayaran Gagal", JOptionPane.WARNING_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlCard = new javax.swing.JPanel();
        pnlRingkasan = new javax.swing.JPanel();
        lblKodeTag = new javax.swing.JLabel();
        lblKodeVal = new javax.swing.JLabel();
        lblKeretaTag = new javax.swing.JLabel();
        lblKeretaVal = new javax.swing.JLabel();
        lblRuteTag = new javax.swing.JLabel();
        lblRuteVal = new javax.swing.JLabel();
        lblStatusTag = new javax.swing.JLabel();
        lblStatusVal = new javax.swing.JLabel();
        scrPenumpang = new javax.swing.JScrollPane();
        tblPenumpang = new javax.swing.JTable();
        pnlTotalBar = new javax.swing.JPanel();
        pnlTotalInfo = new javax.swing.JPanel();
        lblRincianHarga = new javax.swing.JLabel();
        lblTotalNominal = new javax.swing.JLabel();
        pnlButtons = new javax.swing.JPanel();
        btnBayarNanti = new javax.swing.JButton();
        btnBayarSekarang = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Konfirmasi & Pembayaran");
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Periksa kembali rincian pesanan sebelum menyelesaikan pembayaran");
        pnlHeader.add(lblSubtitle);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlCard.setLayout(new java.awt.BorderLayout(0, 16));

        pnlRingkasan.setLayout(new java.awt.GridLayout(0, 2, 16, 8));

        lblKodeTag.setText("Kode Booking");
        pnlRingkasan.add(lblKodeTag);

        lblKodeVal.setText("-");
        pnlRingkasan.add(lblKodeVal);

        lblKeretaTag.setText("Kereta & Kelas");
        pnlRingkasan.add(lblKeretaTag);

        lblKeretaVal.setText("-");
        pnlRingkasan.add(lblKeretaVal);

        lblRuteTag.setText("Rute & Waktu");
        pnlRingkasan.add(lblRuteTag);

        lblRuteVal.setText("-");
        pnlRingkasan.add(lblRuteVal);

        lblStatusTag.setText("Status Pembayaran");
        pnlRingkasan.add(lblStatusTag);

        lblStatusVal.setText("Dipesan");
        pnlRingkasan.add(lblStatusVal);

        pnlCard.add(pnlRingkasan, java.awt.BorderLayout.NORTH);

        scrPenumpang.setViewportView(tblPenumpang);

        pnlCard.add(scrPenumpang, java.awt.BorderLayout.CENTER);

        pnlTotalBar.setLayout(new java.awt.BorderLayout());

        pnlTotalInfo.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblRincianHarga.setText("Rincian Tarif");
        pnlTotalInfo.add(lblRincianHarga);

        lblTotalNominal.setText("Rp 0");
        pnlTotalInfo.add(lblTotalNominal);

        pnlTotalBar.add(pnlTotalInfo, java.awt.BorderLayout.WEST);

        pnlButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 6));

        btnBayarNanti.setText("Ke Tiket Saya");
        pnlButtons.add(btnBayarNanti);

        btnBayarSekarang.setText("Bayar Sekarang");
        pnlButtons.add(btnBayarSekarang);

        pnlTotalBar.add(pnlButtons, java.awt.BorderLayout.EAST);

        pnlCard.add(pnlTotalBar, java.awt.BorderLayout.SOUTH);

        add(pnlCard, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBayarNanti;
    private javax.swing.JButton btnBayarSekarang;
    private javax.swing.JLabel lblKeretaTag;
    private javax.swing.JLabel lblKeretaVal;
    private javax.swing.JLabel lblKodeTag;
    private javax.swing.JLabel lblKodeVal;
    private javax.swing.JLabel lblRincianHarga;
    private javax.swing.JLabel lblRuteTag;
    private javax.swing.JLabel lblRuteVal;
    private javax.swing.JLabel lblStatusTag;
    private javax.swing.JLabel lblStatusVal;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotalNominal;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlRingkasan;
    private javax.swing.JPanel pnlTotalBar;
    private javax.swing.JPanel pnlTotalInfo;
    private javax.swing.JScrollPane scrPenumpang;
    private javax.swing.JTable tblPenumpang;
    // End of variables declaration//GEN-END:variables
}
