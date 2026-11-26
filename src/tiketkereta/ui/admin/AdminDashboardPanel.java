package tiketkereta.ui.admin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import tiketkereta.data.DataStore;
import tiketkereta.model.Pemesanan;
import tiketkereta.ui.MainFrame;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.Tema;

public class AdminDashboardPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;
    private DefaultTableModel tableModel;

    public AdminDashboardPanel() {
        this(null);
    }

    public AdminDashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlHeader.setOpaque(false);
        pnlTitleBox.setOpaque(false);
        pnlQuickActions.setOpaque(false);
        pnlBody.setOpaque(false);
        pnlKpiGrid.setOpaque(false);

        styleKpiCard(pnlKpiStasiun, Tema.PRIMARY);
        styleKpiCard(pnlKpiKereta, Tema.PRIMARY);
        styleKpiCard(pnlKpiJadwal, Tema.STATUS_BLUE);
        styleKpiCard(pnlKpiPemesanan, Tema.ACCENT);
        styleKpiCard(pnlKpiPendapatan, Tema.STATUS_GREEN);
        Tema.styleCard(pnlRecentCard, 18, 20);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblSubtitle);
        Tema.styleSubTitle(lblRecentTitle);

        Tema.styleMuted(lblKpiStasiunTag);
        Tema.styleMuted(lblKpiKeretaTag);
        Tema.styleMuted(lblKpiJadwalTag);
        Tema.styleMuted(lblKpiPemesananTag);
        Tema.styleMuted(lblKpiPendapatanTag);

        lblKpiStasiunVal.setFont(Tema.FONT_KPI);
        lblKpiStasiunVal.setForeground(Tema.PRIMARY);
        lblKpiKeretaVal.setFont(Tema.FONT_KPI);
        lblKpiKeretaVal.setForeground(Tema.PRIMARY);
        lblKpiJadwalVal.setFont(Tema.FONT_KPI);
        lblKpiJadwalVal.setForeground(Tema.PRIMARY);
        lblKpiPemesananVal.setFont(Tema.FONT_KPI);
        lblKpiPemesananVal.setForeground(Tema.ACCENT);
        lblKpiPendapatanVal.setFont(Tema.FONT_TITLE);
        lblKpiPendapatanVal.setForeground(Tema.STATUS_GREEN);

        Tema.stylePrimaryButton(btnBackupCepat);
        Tema.styleSecondaryButton(btnRestoreCepat);

        tableModel = new DefaultTableModel(
                new Object[] { "Kode Booking", "Pemesan", "Kereta", "Rute", "Penumpang", "Total", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblRecent.setModel(tableModel);
        Tema.styleTable(tblRecent);
        tblRecent.getColumnModel().getColumn(6).setCellRenderer(Tema.createStatusRenderer());
        Tema.styleScrollPane(scrRecent);
    }

    private void setupEvents() {
        btnBackupCepat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.lakukanBackupJson(AdminDashboardPanel.this);
                }
            }
        });

        btnRestoreCepat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.lakukanRestoreJson(AdminDashboardPanel.this);
                    refreshData();
                }
            }
        });
    }

    public void refreshData() {
        if (mainFrame == null) {
            return;
        }
        DataStore ds = mainFrame.getDataStore();
        btnBackupCepat.setText("Backup Baru (" + ds.getFileBackupBerikutnya().getName() + ")");
        btnRestoreCepat.setText("Restore (data.json)");
        lblKpiStasiunVal.setText(String.valueOf(ds.getStasiunList().size()));
        lblKpiKeretaVal.setText(String.valueOf(ds.getKeretaList().size()));
        lblKpiJadwalVal.setText(String.valueOf(ds.getJadwalList().size()));
        lblKpiPemesananVal.setText(String.valueOf(ds.getPemesananList().size()));
        long pendapatan = mainFrame.getPemesananService().getTotalPendapatanLunas();
        lblKpiPendapatanVal.setText(FormatUtil.formatRupiah(pendapatan));

        tableModel.setRowCount(0);
        ArrayList<Pemesanan> semua = mainFrame.getPemesananService().getSemuaPemesanan();
        int batas = Math.min(10, semua.size());
        for (int i = 0; i < batas; i++) {
            Pemesanan p = semua.get(i);
            tableModel.addRow(new Object[] {
                    p.getBookingCode(),
                    p.getUserIdentifier(),
                    p.getTrainNumber() + " (" + p.getKelas() + ")",
                    p.getOriginCode() + " → " + p.getDestinationCode(),
                    p.getPenumpangList().size() + " org",
                    FormatUtil.formatRupiah(p.getTotalBayar()),
                    p.getStatus()
            });
        }
    }

    private void styleKpiCard(JPanel panel, Color accentTop) {
        panel.setBackground(Tema.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDER, 1),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(Tema.s(4), 0, 0, 0, accentTop),
                        BorderFactory.createEmptyBorder(Tema.s(14), Tema.s(16), Tema.s(16), Tema.s(16)))));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        pnlTitleBox = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlQuickActions = new javax.swing.JPanel();
        btnBackupCepat = new javax.swing.JButton();
        btnRestoreCepat = new javax.swing.JButton();
        pnlBody = new javax.swing.JPanel();
        pnlKpiGrid = new javax.swing.JPanel();
        pnlKpiStasiun = new javax.swing.JPanel();
        lblKpiStasiunTag = new javax.swing.JLabel();
        lblKpiStasiunVal = new javax.swing.JLabel();
        pnlKpiKereta = new javax.swing.JPanel();
        lblKpiKeretaTag = new javax.swing.JLabel();
        lblKpiKeretaVal = new javax.swing.JLabel();
        pnlKpiJadwal = new javax.swing.JPanel();
        lblKpiJadwalTag = new javax.swing.JLabel();
        lblKpiJadwalVal = new javax.swing.JLabel();
        pnlKpiPemesanan = new javax.swing.JPanel();
        lblKpiPemesananTag = new javax.swing.JLabel();
        lblKpiPemesananVal = new javax.swing.JLabel();
        pnlKpiPendapatan = new javax.swing.JPanel();
        lblKpiPendapatanTag = new javax.swing.JLabel();
        lblKpiPendapatanVal = new javax.swing.JLabel();
        pnlRecentCard = new javax.swing.JPanel();
        lblRecentTitle = new javax.swing.JLabel();
        scrRecent = new javax.swing.JScrollPane();
        tblRecent = new javax.swing.JTable();

        setBackground(new java.awt.Color(245, 247, 250));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 26, 22, 26));
        setPreferredSize(new java.awt.Dimension(1120, 680));
        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setOpaque(false);
        pnlHeader.setLayout(new java.awt.BorderLayout(12, 0));

        pnlTitleBox.setOpaque(false);
        pnlTitleBox.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Dashboard Administrator");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 20));
        lblTitle.setForeground(new java.awt.Color(27, 54, 93));
        pnlTitleBox.add(lblTitle);

        lblSubtitle.setText("Ringkasan data operasional dan transaksi pemesanan tiket kereta");
        lblSubtitle.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblSubtitle.setForeground(new java.awt.Color(100, 116, 139));
        pnlTitleBox.add(lblSubtitle);
        pnlHeader.add(pnlTitleBox, java.awt.BorderLayout.CENTER);

        pnlQuickActions.setOpaque(false);
        pnlQuickActions.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 4));

        btnBackupCepat.setText("Backup JSON");
        btnBackupCepat.setBackground(new java.awt.Color(27, 54, 93));
        btnBackupCepat.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnBackupCepat.setForeground(new java.awt.Color(255, 255, 255));
        btnBackupCepat.setFocusPainted(false);
        pnlQuickActions.add(btnBackupCepat);

        btnRestoreCepat.setText("Restore JSON");
        btnRestoreCepat.setBackground(new java.awt.Color(255, 255, 255));
        btnRestoreCepat.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnRestoreCepat.setForeground(new java.awt.Color(15, 23, 42));
        btnRestoreCepat.setFocusPainted(false);
        pnlQuickActions.add(btnRestoreCepat);
        pnlHeader.add(pnlQuickActions, java.awt.BorderLayout.EAST);
        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlBody.setOpaque(false);
        pnlBody.setLayout(new java.awt.BorderLayout(0, 16));

        pnlKpiGrid.setOpaque(false);
        pnlKpiGrid.setLayout(new java.awt.GridLayout(1, 5, 12, 0));

        pnlKpiStasiun.setBackground(new java.awt.Color(255, 255, 255));
        pnlKpiStasiun.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(27, 54, 93)), javax.swing.BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        pnlKpiStasiun.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblKpiStasiunTag.setText("Total Stasiun");
        lblKpiStasiunTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblKpiStasiunTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlKpiStasiun.add(lblKpiStasiunTag);

        lblKpiStasiunVal.setText("0");
        lblKpiStasiunVal.setFont(new java.awt.Font("Segoe UI", 1, 24));
        lblKpiStasiunVal.setForeground(new java.awt.Color(27, 54, 93));
        pnlKpiStasiun.add(lblKpiStasiunVal);
        pnlKpiGrid.add(pnlKpiStasiun);

        pnlKpiKereta.setBackground(new java.awt.Color(255, 255, 255));
        pnlKpiKereta.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(27, 54, 93)), javax.swing.BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        pnlKpiKereta.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblKpiKeretaTag.setText("Total Kereta");
        lblKpiKeretaTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblKpiKeretaTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlKpiKereta.add(lblKpiKeretaTag);

        lblKpiKeretaVal.setText("0");
        lblKpiKeretaVal.setFont(new java.awt.Font("Segoe UI", 1, 24));
        lblKpiKeretaVal.setForeground(new java.awt.Color(27, 54, 93));
        pnlKpiKereta.add(lblKpiKeretaVal);
        pnlKpiGrid.add(pnlKpiKereta);

        pnlKpiJadwal.setBackground(new java.awt.Color(255, 255, 255));
        pnlKpiJadwal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(37, 99, 235)), javax.swing.BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        pnlKpiJadwal.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblKpiJadwalTag.setText("Total Jadwal");
        lblKpiJadwalTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblKpiJadwalTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlKpiJadwal.add(lblKpiJadwalTag);

        lblKpiJadwalVal.setText("0");
        lblKpiJadwalVal.setFont(new java.awt.Font("Segoe UI", 1, 24));
        lblKpiJadwalVal.setForeground(new java.awt.Color(27, 54, 93));
        pnlKpiJadwal.add(lblKpiJadwalVal);
        pnlKpiGrid.add(pnlKpiJadwal);

        pnlKpiPemesanan.setBackground(new java.awt.Color(255, 255, 255));
        pnlKpiPemesanan.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(243, 112, 33)), javax.swing.BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        pnlKpiPemesanan.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblKpiPemesananTag.setText("Total Pemesanan");
        lblKpiPemesananTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblKpiPemesananTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlKpiPemesanan.add(lblKpiPemesananTag);

        lblKpiPemesananVal.setText("0");
        lblKpiPemesananVal.setFont(new java.awt.Font("Segoe UI", 1, 24));
        lblKpiPemesananVal.setForeground(new java.awt.Color(243, 112, 33));
        pnlKpiPemesanan.add(lblKpiPemesananVal);
        pnlKpiGrid.add(pnlKpiPemesanan);

        pnlKpiPendapatan.setBackground(new java.awt.Color(255, 255, 255));
        pnlKpiPendapatan.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(22, 163, 74)), javax.swing.BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        pnlKpiPendapatan.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblKpiPendapatanTag.setText("Pendapatan Lunas");
        lblKpiPendapatanTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblKpiPendapatanTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlKpiPendapatan.add(lblKpiPendapatanTag);

        lblKpiPendapatanVal.setText("Rp 0");
        lblKpiPendapatanVal.setFont(new java.awt.Font("Segoe UI", 1, 20));
        lblKpiPendapatanVal.setForeground(new java.awt.Color(22, 163, 74));
        pnlKpiPendapatan.add(lblKpiPendapatanVal);
        pnlKpiGrid.add(pnlKpiPendapatan);
        pnlBody.add(pnlKpiGrid, java.awt.BorderLayout.NORTH);

        pnlRecentCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlRecentCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        pnlRecentCard.setLayout(new java.awt.BorderLayout(0, 12));

        lblRecentTitle.setText("Pemesanan Terbaru");
        lblRecentTitle.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblRecentTitle.setForeground(new java.awt.Color(15, 23, 42));
        pnlRecentCard.add(lblRecentTitle, java.awt.BorderLayout.NORTH);

        scrRecent.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        tblRecent.setFont(new java.awt.Font("Segoe UI", 0, 13));
        tblRecent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode Booking", "Pemesan", "Kereta", "Rute", "Penumpang", "Total", "Status"
            }
        ));
        tblRecent.setRowHeight(34);
        scrRecent.setViewportView(tblRecent);
        pnlRecentCard.add(scrRecent, java.awt.BorderLayout.CENTER);
        pnlBody.add(pnlRecentCard, java.awt.BorderLayout.CENTER);
        add(pnlBody, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackupCepat;
    private javax.swing.JButton btnRestoreCepat;
    private javax.swing.JLabel lblKpiJadwalTag;
    private javax.swing.JLabel lblKpiJadwalVal;
    private javax.swing.JLabel lblKpiKeretaTag;
    private javax.swing.JLabel lblKpiKeretaVal;
    private javax.swing.JLabel lblKpiPemesananTag;
    private javax.swing.JLabel lblKpiPemesananVal;
    private javax.swing.JLabel lblKpiPendapatanTag;
    private javax.swing.JLabel lblKpiPendapatanVal;
    private javax.swing.JLabel lblKpiStasiunTag;
    private javax.swing.JLabel lblKpiStasiunVal;
    private javax.swing.JLabel lblRecentTitle;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlKpiGrid;
    private javax.swing.JPanel pnlKpiJadwal;
    private javax.swing.JPanel pnlKpiKereta;
    private javax.swing.JPanel pnlKpiPemesanan;
    private javax.swing.JPanel pnlKpiPendapatan;
    private javax.swing.JPanel pnlKpiStasiun;
    private javax.swing.JPanel pnlQuickActions;
    private javax.swing.JPanel pnlRecentCard;
    private javax.swing.JPanel pnlTitleBox;
    private javax.swing.JScrollPane scrRecent;
    private javax.swing.JTable tblRecent;
    // End of variables declaration//GEN-END:variables
}
