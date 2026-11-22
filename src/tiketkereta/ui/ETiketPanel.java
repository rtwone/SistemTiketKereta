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
        pnlFooter.setOpaque(false);

        lblBrand.setFont(Tema.FONT_SMALL);
        lblBrand.setForeground(Tema.ACCENT);
        lblBookingCode.setFont(Tema.FONT_HERO);
        lblBookingCode.setForeground(Color.WHITE);
        lblStatusBadge.setFont(Tema.FONT_SUBTITLE);
        lblStatusBadge.setForeground(Color.WHITE);

        Tema.styleMuted(lblKeretaTag);
        Tema.styleMuted(lblKelasTag);
        Tema.styleMuted(lblRuteTag);
        Tema.styleMuted(lblJamTag);
        Tema.styleMuted(lblWaktuTag);
        Tema.styleMuted(lblTotalTag);

        lblKeretaVal.setFont(Tema.FONT_BODY_BOLD);
        lblKeretaVal.setForeground(Tema.TEXT_PRIMARY);
        lblKelasVal.setFont(Tema.FONT_BODY_BOLD);
        lblKelasVal.setForeground(Tema.PRIMARY);
        lblRuteVal.setFont(Tema.FONT_BODY_BOLD);
        lblRuteVal.setForeground(Tema.TEXT_PRIMARY);
        lblJamVal.setFont(Tema.FONT_BODY_BOLD);
        lblJamVal.setForeground(Tema.TEXT_PRIMARY);
        lblWaktuVal.setFont(Tema.FONT_BODY);
        lblWaktuVal.setForeground(Tema.TEXT_MUTED);
        lblTotalVal.setFont(Tema.FONT_SUBTITLE);
        lblTotalVal.setForeground(Tema.ACCENT);

        Tema.styleSecondaryButton(btnTiketSaya);
        Tema.styleAccentButton(btnPesanLagi);

        tableModel = new DefaultTableModel(
                new Object[]{"No", "Nama Penumpang", "Jenis ID", "No. Identitas", "No. Kursi"}, 0) {
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
        lblBookingCode.setText(pesanan.getBookingCode());
        lblStatusBadge.setText("STATUS: " + pesanan.getStatus().toUpperCase());
        if (Pemesanan.STATUS_LUNAS.equalsIgnoreCase(pesanan.getStatus())) {
            lblStatusBadge.setForeground(new Color(0x86EFAC));
        } else if (Pemesanan.STATUS_DIPESAN.equalsIgnoreCase(pesanan.getStatus())) {
            lblStatusBadge.setForeground(new Color(0xFDE68A));
        } else {
            lblStatusBadge.setForeground(new Color(0xFCA5A5));
        }

        lblKeretaVal.setText(pesanan.getTrainNumber() + " - " + pesanan.getTrainName());
        lblKelasVal.setText(pesanan.getKelas());

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
        lblJamVal.setText(pesanan.getDepartureTime() + "  -  " + pesanan.getArrivalTime());
        lblWaktuVal.setText(pesanan.getCreatedAt() == null ? "-" : pesanan.getCreatedAt());
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
        lblKeretaTag = new javax.swing.JLabel();
        lblKeretaVal = new javax.swing.JLabel();
        lblKelasTag = new javax.swing.JLabel();
        lblKelasVal = new javax.swing.JLabel();
        lblRuteTag = new javax.swing.JLabel();
        lblRuteVal = new javax.swing.JLabel();
        lblJamTag = new javax.swing.JLabel();
        lblJamVal = new javax.swing.JLabel();
        lblWaktuTag = new javax.swing.JLabel();
        lblWaktuVal = new javax.swing.JLabel();
        lblTotalTag = new javax.swing.JLabel();
        lblTotalVal = new javax.swing.JLabel();
        scrPenumpang = new javax.swing.JScrollPane();
        tblPenumpang = new javax.swing.JTable();
        pnlFooter = new javax.swing.JPanel();
        btnTiketSaya = new javax.swing.JButton();
        btnPesanLagi = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(0, 16));

        pnlTicketHeader.setLayout(new java.awt.BorderLayout(12, 0));

        pnlHeadLeft.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblBrand.setText("E-TIKET RESMI • ACCESS BY KAI");
        pnlHeadLeft.add(lblBrand);

        lblBookingCode.setText("TKT-0001");
        pnlHeadLeft.add(lblBookingCode);

        pnlTicketHeader.add(pnlHeadLeft, java.awt.BorderLayout.CENTER);

        lblStatusBadge.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatusBadge.setText("LUNAS");
        pnlTicketHeader.add(lblStatusBadge, java.awt.BorderLayout.EAST);

        add(pnlTicketHeader, java.awt.BorderLayout.NORTH);

        pnlTicketBody.setLayout(new java.awt.BorderLayout(0, 16));

        pnlInfoGrid.setLayout(new java.awt.GridLayout(0, 2, 18, 8));

        lblKeretaTag.setText("Kereta Api");
        pnlInfoGrid.add(lblKeretaTag);

        lblKeretaVal.setText("-");
        pnlInfoGrid.add(lblKeretaVal);

        lblKelasTag.setText("Kelas Layanan");
        pnlInfoGrid.add(lblKelasTag);

        lblKelasVal.setText("-");
        pnlInfoGrid.add(lblKelasVal);

        lblRuteTag.setText("Rute Perjalanan");
        pnlInfoGrid.add(lblRuteTag);

        lblRuteVal.setText("-");
        pnlInfoGrid.add(lblRuteVal);

        lblJamTag.setText("Jadwal Berangkat - Tiba");
        pnlInfoGrid.add(lblJamTag);

        lblJamVal.setText("-");
        pnlInfoGrid.add(lblJamVal);

        lblWaktuTag.setText("Waktu Pemesanan");
        pnlInfoGrid.add(lblWaktuTag);

        lblWaktuVal.setText("-");
        pnlInfoGrid.add(lblWaktuVal);

        lblTotalTag.setText("Total Pembayaran");
        pnlInfoGrid.add(lblTotalTag);

        lblTotalVal.setText("Rp 0");
        pnlInfoGrid.add(lblTotalVal);

        pnlTicketBody.add(pnlInfoGrid, java.awt.BorderLayout.NORTH);

        scrPenumpang.setViewportView(tblPenumpang);

        pnlTicketBody.add(scrPenumpang, java.awt.BorderLayout.CENTER);

        pnlFooter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));

        btnTiketSaya.setText("Ke Tiket Saya");
        pnlFooter.add(btnTiketSaya);

        btnPesanLagi.setText("Pesan Tiket Baru");
        pnlFooter.add(btnPesanLagi);

        pnlTicketBody.add(pnlFooter, java.awt.BorderLayout.SOUTH);

        add(pnlTicketBody, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPesanLagi;
    private javax.swing.JButton btnTiketSaya;
    private javax.swing.JLabel lblBookingCode;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblJamTag;
    private javax.swing.JLabel lblJamVal;
    private javax.swing.JLabel lblKelasTag;
    private javax.swing.JLabel lblKelasVal;
    private javax.swing.JLabel lblKeretaTag;
    private javax.swing.JLabel lblKeretaVal;
    private javax.swing.JLabel lblRuteTag;
    private javax.swing.JLabel lblRuteVal;
    private javax.swing.JLabel lblStatusBadge;
    private javax.swing.JLabel lblTotalTag;
    private javax.swing.JLabel lblTotalVal;
    private javax.swing.JLabel lblWaktuTag;
    private javax.swing.JLabel lblWaktuVal;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlHeadLeft;
    private javax.swing.JPanel pnlInfoGrid;
    private javax.swing.JPanel pnlTicketBody;
    private javax.swing.JPanel pnlTicketHeader;
    private javax.swing.JScrollPane scrPenumpang;
    private javax.swing.JTable tblPenumpang;
    // End of variables declaration//GEN-END:variables
}
