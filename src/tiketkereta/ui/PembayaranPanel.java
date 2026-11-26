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
        pnlTopArea.setOpaque(false);
        pnlStepper.setOpaque(false);
        Tema.styleStepItem(lblStep1, false, true);
        Tema.styleStepItem(lblStep2, false, true);
        Tema.styleStepItem(lblStep3, false, true);
        Tema.styleStepItem(lblStep4, true, false);

        pnlHeader.setOpaque(false);
        Tema.styleCard(pnlCard, 20, 24);
        pnlRingkasan.setOpaque(false);
        Tema.styleSoftCard(pnlTileKode, 12, 14);
        Tema.styleSoftCard(pnlTileKereta, 12, 14);
        Tema.styleSoftCard(pnlTileRute, 12, 14);
        Tema.styleSoftCard(pnlTileStatus, 12, 14);

        Tema.styleAccentCard(pnlTotalBar, 14, 18);
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
        lblStatusVal.setFont(Tema.FONT_SUBTITLE);
        lblStatusVal.setForeground(Tema.STATUS_AMBER);

        Tema.styleMuted(lblRincianHarga);
        lblTotalNominal.setFont(Tema.FONT_KPI);
        lblTotalNominal.setForeground(Tema.ACCENT);

        Tema.styleSecondaryButton(btnBayarNanti);
        Tema.styleAccentButton(btnBayarSekarang);

        tableModel = new DefaultTableModel(
                new Object[]{"No", "Nama Penumpang", "Jenis ID", "Nomor Identitas", "Nomor Kursi"}, 0) {
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
        lblKeretaVal.setText(pesanan.getTrainNumber() + " " + pesanan.getTrainName() + " (" + pesanan.getKelas() + ")");

        String labelAsal = pesanan.getOriginCode();
        String labelTujuan = pesanan.getDestinationCode();
        if (mainFrame != null) {
            Stasiun asal = mainFrame.getDataStore().cariStasiunByCode(pesanan.getOriginCode());
            Stasiun tujuan = mainFrame.getDataStore().cariStasiunByCode(pesanan.getDestinationCode());
            if (asal != null) {
                labelAsal = asal.getCode();
            }
            if (tujuan != null) {
                labelTujuan = tujuan.getCode();
            }
        }
        lblRuteVal.setText(labelAsal + " (" + pesanan.getDepartureTime() + ") → "
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
        lblRincianHarga.setText("Total Pembayaran (" + jml + " Penumpang × " + FormatUtil.formatRupiah(pesanan.getTarifPerPenumpang()) + ")");
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

        pnlTopArea = new javax.swing.JPanel();
        pnlStepper = new javax.swing.JPanel();
        lblStep1 = new javax.swing.JLabel();
        lblStep2 = new javax.swing.JLabel();
        lblStep3 = new javax.swing.JLabel();
        lblStep4 = new javax.swing.JLabel();
        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlCard = new javax.swing.JPanel();
        pnlRingkasan = new javax.swing.JPanel();
        pnlTileKode = new javax.swing.JPanel();
        lblKodeTag = new javax.swing.JLabel();
        lblKodeVal = new javax.swing.JLabel();
        pnlTileKereta = new javax.swing.JPanel();
        lblKeretaTag = new javax.swing.JLabel();
        lblKeretaVal = new javax.swing.JLabel();
        pnlTileRute = new javax.swing.JPanel();
        lblRuteTag = new javax.swing.JLabel();
        lblRuteVal = new javax.swing.JLabel();
        pnlTileStatus = new javax.swing.JPanel();
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

        setLayout(new java.awt.BorderLayout(0, 14));

        pnlTopArea.setLayout(new java.awt.BorderLayout(0, 12));

        pnlStepper.setLayout(new java.awt.GridLayout(1, 4, 10, 0));

        lblStep1.setText("•  1. Cari Rute (Selesai)");
        pnlStepper.add(lblStep1);

        lblStep2.setText("•  2. Pilih Kereta (Selesai)");
        pnlStepper.add(lblStep2);

        lblStep3.setText("•  3. Penumpang & Kursi (Selesai)");
        pnlStepper.add(lblStep3);

        lblStep4.setText("4. Bayar & E-Tiket");
        pnlStepper.add(lblStep4);

        pnlTopArea.add(pnlStepper, java.awt.BorderLayout.NORTH);

        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Konfirmasi Pesanan & Pembayaran");
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Periksa kembali rincian perjalanan dan daftar penumpang sebelum melunasi tiket");
        pnlHeader.add(lblSubtitle);

        pnlTopArea.add(pnlHeader, java.awt.BorderLayout.CENTER);

        add(pnlTopArea, java.awt.BorderLayout.NORTH);

        pnlCard.setLayout(new java.awt.BorderLayout(0, 16));

        pnlRingkasan.setLayout(new java.awt.GridLayout(1, 4, 14, 0));

        pnlTileKode.setLayout(new java.awt.GridLayout(2, 1, 0, 4));

        lblKodeTag.setText("Kode Booking");
        pnlTileKode.add(lblKodeTag);

        lblKodeVal.setText("-");
        pnlTileKode.add(lblKodeVal);

        pnlRingkasan.add(pnlTileKode);

        pnlTileKereta.setLayout(new java.awt.GridLayout(2, 1, 0, 4));

        lblKeretaTag.setText("Kereta & Kelas");
        pnlTileKereta.add(lblKeretaTag);

        lblKeretaVal.setText("-");
        pnlTileKereta.add(lblKeretaVal);

        pnlRingkasan.add(pnlTileKereta);

        pnlTileRute.setLayout(new java.awt.GridLayout(2, 1, 0, 4));

        lblRuteTag.setText("Rute & Jam");
        pnlTileRute.add(lblRuteTag);

        lblRuteVal.setText("-");
        pnlTileRute.add(lblRuteVal);

        pnlRingkasan.add(pnlTileRute);

        pnlTileStatus.setLayout(new java.awt.GridLayout(2, 1, 0, 4));

        lblStatusTag.setText("Status Pembayaran");
        pnlTileStatus.add(lblStatusTag);

        lblStatusVal.setText("Dipesan");
        pnlTileStatus.add(lblStatusVal);

        pnlRingkasan.add(pnlTileStatus);

        pnlCard.add(pnlRingkasan, java.awt.BorderLayout.NORTH);

        scrPenumpang.setViewportView(tblPenumpang);

        pnlCard.add(scrPenumpang, java.awt.BorderLayout.CENTER);

        pnlTotalBar.setLayout(new java.awt.BorderLayout());

        pnlTotalInfo.setLayout(new java.awt.GridLayout(0, 1, 0, 2));

        lblRincianHarga.setText("Rincian Tarif");
        pnlTotalInfo.add(lblRincianHarga);

        lblTotalNominal.setText("Rp 0");
        pnlTotalInfo.add(lblTotalNominal);

        pnlTotalBar.add(pnlTotalInfo, java.awt.BorderLayout.WEST);

        pnlButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 4));

        btnBayarNanti.setText("Bayar Nanti (Tiket Saya)");
        pnlButtons.add(btnBayarNanti);

        btnBayarSekarang.setText("Bayar Sekarang & Terbitkan E-Tiket  →");
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
    private javax.swing.JLabel lblStep1;
    private javax.swing.JLabel lblStep2;
    private javax.swing.JLabel lblStep3;
    private javax.swing.JLabel lblStep4;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotalNominal;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlRingkasan;
    private javax.swing.JPanel pnlStepper;
    private javax.swing.JPanel pnlTileKereta;
    private javax.swing.JPanel pnlTileKode;
    private javax.swing.JPanel pnlTileRute;
    private javax.swing.JPanel pnlTileStatus;
    private javax.swing.JPanel pnlTopArea;
    private javax.swing.JPanel pnlTotalBar;
    private javax.swing.JPanel pnlTotalInfo;
    private javax.swing.JScrollPane scrPenumpang;
    private javax.swing.JTable tblPenumpang;
    // End of variables declaration//GEN-END:variables
}
