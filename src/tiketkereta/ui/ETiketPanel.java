package tiketkereta.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import tiketkereta.model.Pemesanan;
import tiketkereta.model.Penumpang;
import tiketkereta.model.Stasiun;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.Tema;

public class ETiketPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;
    private DefaultTableModel tableModel;
    private Pemesanan pesananAktif;

    public ETiketPanel() {
        this(null);
    }

    public ETiketPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        Tema.styleHeroCard(pnlTicketHeader);
        pnlHeadLeft.setOpaque(false);
        Tema.styleCard(pnlTicketBody, 20, 24);
        pnlInfoGrid.setOpaque(false);
        Tema.styleSoftCard(pnlTileRute, 14, 16);
        Tema.styleSoftCard(pnlTileKereta, 14, 16);
        Tema.styleAccentCard(pnlTileBayar, 14, 16);
        pnlFooter.setOpaque(false);
        pnlFooterRight.setOpaque(false);

        lblBrand.setFont(Tema.FONT_SMALL);
        lblBrand.setForeground(Tema.ACCENT);
        lblBookingCode.setFont(Tema.FONT_HERO);
        lblBookingCode.setForeground(Color.WHITE);
        lblStatusBadge.setFont(Tema.FONT_TITLE);
        lblStatusBadge.setForeground(Color.WHITE);

        Tema.styleMuted(lblRuteTag);
        Tema.styleMuted(lblKeretaTag);
        Tema.styleMuted(lblTotalTag);

        lblRuteVal.setFont(Tema.FONT_SUBTITLE);
        lblRuteVal.setForeground(Tema.PRIMARY);
        lblJamVal.setFont(Tema.FONT_BODY_BOLD);
        lblJamVal.setForeground(Tema.TEXT_PRIMARY);

        lblKeretaVal.setFont(Tema.FONT_SUBTITLE);
        lblKeretaVal.setForeground(Tema.TEXT_PRIMARY);
        lblKelasVal.setFont(Tema.FONT_BODY_BOLD);
        lblKelasVal.setForeground(Tema.PRIMARY);

        lblTotalVal.setFont(Tema.FONT_TITLE);
        lblTotalVal.setForeground(Tema.ACCENT);
        lblWaktuVal.setFont(Tema.FONT_SMALL);
        lblWaktuVal.setForeground(Tema.TEXT_MUTED);

        Tema.styleSecondaryButton(btnTiketSaya);
        Tema.stylePrimaryButton(btnCetakFisik);
        Tema.styleAccentButton(btnPesanLagi);

        tableModel = new DefaultTableModel(
                new Object[]{"No", "Nama Penumpang", "Jenis Identitas", "Nomor Identitas", "Nomor Kursi"}, 0) {
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
        btnTiketSaya.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.tampilkanTiketSaya();
                }
            }
        });

        btnCetakFisik.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pesananAktif != null && mainFrame != null) {
                    mainFrame.mulaiCetakTiket(pesananAktif);
                }
            }
        });

        btnPesanLagi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.tampilkanCariTiket();
                }
            }
        });
    }

    public void tampilkanETiket(Pemesanan pesanan) {
        if (pesanan == null) {
            return;
        }
        this.pesananAktif = pesanan;
        boolean lunas = Pemesanan.STATUS_LUNAS.equalsIgnoreCase(pesanan.getStatus());
        btnCetakFisik.setEnabled(lunas);
        lblBookingCode.setText("KODE BOOKING: " + pesanan.getBookingCode());
        lblStatusBadge.setText("•  " + pesanan.getStatus().toUpperCase());
        if (Pemesanan.STATUS_LUNAS.equalsIgnoreCase(pesanan.getStatus())) {
            lblStatusBadge.setForeground(new Color(0x86EFAC));
        } else if (Pemesanan.STATUS_DIPESAN.equalsIgnoreCase(pesanan.getStatus())) {
            lblStatusBadge.setForeground(new Color(0xFDE68A));
        } else {
            lblStatusBadge.setForeground(new Color(0xFCA5A5));
        }

        lblKeretaVal.setText(pesanan.getTrainNumber() + " - " + pesanan.getTrainName());
        lblKelasVal.setText("Kelas " + pesanan.getKelas());

        String asal = pesanan.getOriginCode();
        String tujuan = pesanan.getDestinationCode();
        if (mainFrame != null) {
            Stasiun sAsal = mainFrame.getDataStore().cariStasiunByCode(pesanan.getOriginCode());
            Stasiun sTujuan = mainFrame.getDataStore().cariStasiunByCode(pesanan.getDestinationCode());
            if (sAsal != null) {
                asal = sAsal.getLabelSingkat();
            }
            if (sTujuan != null) {
                tujuan = sTujuan.getLabelSingkat();
            }
        }
        lblRuteVal.setText(asal + "  →  " + tujuan);
        lblJamVal.setText("Berangkat " + pesanan.getDepartureTime() + "  •  Tiba " + pesanan.getArrivalTime());
        lblWaktuVal.setText("Dipesan: " + (pesanan.getCreatedAt() == null ? "-" : pesanan.getCreatedAt()));
        lblTotalVal.setText(FormatUtil.formatRupiah(pesanan.getTotalBayar()));

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
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTicketHeader = new javax.swing.JPanel();
        pnlHeadLeft = new javax.swing.JPanel();
        lblBrand = new javax.swing.JLabel();
        lblBookingCode = new javax.swing.JLabel();
        lblStatusBadge = new javax.swing.JLabel();
        pnlTicketBody = new javax.swing.JPanel();
        pnlInfoGrid = new javax.swing.JPanel();
        pnlTileRute = new javax.swing.JPanel();
        lblRuteTag = new javax.swing.JLabel();
        lblRuteVal = new javax.swing.JLabel();
        lblJamVal = new javax.swing.JLabel();
        pnlTileKereta = new javax.swing.JPanel();
        lblKeretaTag = new javax.swing.JLabel();
        lblKeretaVal = new javax.swing.JLabel();
        lblKelasVal = new javax.swing.JLabel();
        pnlTileBayar = new javax.swing.JPanel();
        lblTotalTag = new javax.swing.JLabel();
        lblTotalVal = new javax.swing.JLabel();
        lblWaktuVal = new javax.swing.JLabel();
        scrPenumpang = new javax.swing.JScrollPane();
        tblPenumpang = new javax.swing.JTable();
        pnlFooter = new javax.swing.JPanel();
        btnTiketSaya = new javax.swing.JButton();
        pnlFooterRight = new javax.swing.JPanel();
        btnCetakFisik = new javax.swing.JButton();
        btnPesanLagi = new javax.swing.JButton();

        setBackground(new java.awt.Color(245, 247, 250));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 26, 22, 26));
        setPreferredSize(new java.awt.Dimension(1120, 680));
        setLayout(new java.awt.BorderLayout(0, 16));

        pnlTicketHeader.setBackground(new java.awt.Color(27, 54, 93));
        pnlTicketHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 26, 22, 26));
        pnlTicketHeader.setLayout(new java.awt.BorderLayout(12, 0));

        pnlHeadLeft.setOpaque(false);
        pnlHeadLeft.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblBrand.setText("BOARDING PASS / E-TIKET RESMI • ACCESS BY KAI");
        lblBrand.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblBrand.setForeground(new java.awt.Color(243, 112, 33));
        pnlHeadLeft.add(lblBrand);

        lblBookingCode.setText("TKT-0001");
        lblBookingCode.setFont(new java.awt.Font("Segoe UI", 1, 24));
        lblBookingCode.setForeground(new java.awt.Color(255, 255, 255));
        pnlHeadLeft.add(lblBookingCode);
        pnlTicketHeader.add(pnlHeadLeft, java.awt.BorderLayout.CENTER);

        lblStatusBadge.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatusBadge.setText("LUNAS");
        lblStatusBadge.setFont(new java.awt.Font("Segoe UI", 1, 20));
        lblStatusBadge.setForeground(new java.awt.Color(255, 255, 255));
        pnlTicketHeader.add(lblStatusBadge, java.awt.BorderLayout.EAST);
        add(pnlTicketHeader, java.awt.BorderLayout.NORTH);

        pnlTicketBody.setBackground(new java.awt.Color(255, 255, 255));
        pnlTicketBody.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24)));
        pnlTicketBody.setLayout(new java.awt.BorderLayout(0, 16));

        pnlInfoGrid.setOpaque(false);
        pnlInfoGrid.setLayout(new java.awt.GridLayout(1, 3, 16, 0));

        pnlTileRute.setBackground(new java.awt.Color(248, 250, 252));
        pnlTileRute.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        pnlTileRute.setLayout(new java.awt.GridLayout(3, 1, 0, 4));

        lblRuteTag.setText("Rute & Waktu Perjalanan");
        lblRuteTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblRuteTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlTileRute.add(lblRuteTag);

        lblRuteVal.setText("-");
        lblRuteVal.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblRuteVal.setForeground(new java.awt.Color(27, 54, 93));
        pnlTileRute.add(lblRuteVal);

        lblJamVal.setText("-");
        lblJamVal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblJamVal.setForeground(new java.awt.Color(15, 23, 42));
        pnlTileRute.add(lblJamVal);
        pnlInfoGrid.add(pnlTileRute);

        pnlTileKereta.setBackground(new java.awt.Color(248, 250, 252));
        pnlTileKereta.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        pnlTileKereta.setLayout(new java.awt.GridLayout(3, 1, 0, 4));

        lblKeretaTag.setText("Armada & Kelas Layanan");
        lblKeretaTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblKeretaTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlTileKereta.add(lblKeretaTag);

        lblKeretaVal.setText("-");
        lblKeretaVal.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblKeretaVal.setForeground(new java.awt.Color(15, 23, 42));
        pnlTileKereta.add(lblKeretaVal);

        lblKelasVal.setText("-");
        lblKelasVal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblKelasVal.setForeground(new java.awt.Color(27, 54, 93));
        pnlTileKereta.add(lblKelasVal);
        pnlInfoGrid.add(pnlTileKereta);

        pnlTileBayar.setBackground(new java.awt.Color(255, 247, 237));
        pnlTileBayar.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(254, 215, 170)), javax.swing.BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        pnlTileBayar.setLayout(new java.awt.GridLayout(3, 1, 0, 4));

        lblTotalTag.setText("Total Bayar & Waktu Pesan");
        lblTotalTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblTotalTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlTileBayar.add(lblTotalTag);

        lblTotalVal.setText("Rp 0");
        lblTotalVal.setFont(new java.awt.Font("Segoe UI", 1, 20));
        lblTotalVal.setForeground(new java.awt.Color(243, 112, 33));
        pnlTileBayar.add(lblTotalVal);

        lblWaktuVal.setText("-");
        lblWaktuVal.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblWaktuVal.setForeground(new java.awt.Color(100, 116, 139));
        pnlTileBayar.add(lblWaktuVal);
        pnlInfoGrid.add(pnlTileBayar);
        pnlTicketBody.add(pnlInfoGrid, java.awt.BorderLayout.NORTH);

        scrPenumpang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        tblPenumpang.setFont(new java.awt.Font("Segoe UI", 0, 13));
        tblPenumpang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No", "Nama Penumpang", "Jenis Identitas", "Nomor Identitas", "Nomor Kursi"
            }
        ));
        tblPenumpang.setRowHeight(34);
        scrPenumpang.setViewportView(tblPenumpang);
        pnlTicketBody.add(scrPenumpang, java.awt.BorderLayout.CENTER);

        pnlFooter.setOpaque(false);
        pnlFooter.setLayout(new java.awt.BorderLayout());

        btnTiketSaya.setText("←  Lihat Daftar Tiket Saya");
        btnTiketSaya.setBackground(new java.awt.Color(255, 255, 255));
        btnTiketSaya.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnTiketSaya.setForeground(new java.awt.Color(15, 23, 42));
        btnTiketSaya.setFocusPainted(false);
        pnlFooter.add(btnTiketSaya, java.awt.BorderLayout.WEST);

        pnlFooterRight.setOpaque(false);
        pnlFooterRight.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));

        btnCetakFisik.setText("Cetak Tiket Fisik");
        btnCetakFisik.setBackground(new java.awt.Color(27, 54, 93));
        btnCetakFisik.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnCetakFisik.setForeground(new java.awt.Color(255, 255, 255));
        btnCetakFisik.setFocusPainted(false);
        pnlFooterRight.add(btnCetakFisik);

        btnPesanLagi.setText("Pesan Tiket Baru  →");
        btnPesanLagi.setBackground(new java.awt.Color(243, 112, 33));
        btnPesanLagi.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnPesanLagi.setForeground(new java.awt.Color(255, 255, 255));
        btnPesanLagi.setFocusPainted(false);
        pnlFooterRight.add(btnPesanLagi);
        pnlFooter.add(pnlFooterRight, java.awt.BorderLayout.EAST);
        pnlTicketBody.add(pnlFooter, java.awt.BorderLayout.SOUTH);
        add(pnlTicketBody, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetakFisik;
    private javax.swing.JButton btnPesanLagi;
    private javax.swing.JButton btnTiketSaya;
    private javax.swing.JLabel lblBookingCode;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblJamVal;
    private javax.swing.JLabel lblKelasVal;
    private javax.swing.JLabel lblKeretaTag;
    private javax.swing.JLabel lblKeretaVal;
    private javax.swing.JLabel lblRuteTag;
    private javax.swing.JLabel lblRuteVal;
    private javax.swing.JLabel lblStatusBadge;
    private javax.swing.JLabel lblTotalTag;
    private javax.swing.JLabel lblTotalVal;
    private javax.swing.JLabel lblWaktuVal;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlFooterRight;
    private javax.swing.JPanel pnlHeadLeft;
    private javax.swing.JPanel pnlInfoGrid;
    private javax.swing.JPanel pnlTicketBody;
    private javax.swing.JPanel pnlTicketHeader;
    private javax.swing.JPanel pnlTileBayar;
    private javax.swing.JPanel pnlTileKereta;
    private javax.swing.JPanel pnlTileRute;
    private javax.swing.JScrollPane scrPenumpang;
    private javax.swing.JTable tblPenumpang;
    // End of variables declaration//GEN-END:variables
}
