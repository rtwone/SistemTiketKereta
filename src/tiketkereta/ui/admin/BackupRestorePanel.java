package tiketkereta.ui.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import tiketkereta.data.DataStore;
import tiketkereta.ui.MainFrame;
import tiketkereta.util.Tema;

public class BackupRestorePanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;
    private DefaultTableModel backupTableModel;

    public BackupRestorePanel() {
        this(null);
    }

    public BackupRestorePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlHeader.setOpaque(false);
        pnlContent.setOpaque(false);
        pnlCards.setOpaque(false);
        pnlBottomSplit.setOpaque(false);

        Tema.styleCard(pnlBackupCard, 20, 22);
        Tema.styleCard(pnlRestoreCard, 20, 22);
        Tema.styleCard(pnlStatusCard, 20, 22);
        Tema.styleCard(pnlHistoryCard, 20, 22);

        pnlBackupTop.setOpaque(false);
        pnlRestoreTop.setOpaque(false);
        pnlInfoGrid.setOpaque(false);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblSubtitle);
        Tema.styleSubTitle(lblBackupTitle);
        Tema.styleBadge(lblNextBackup, Tema.PRIMARY_SOFT, Tema.PRIMARY);
        Tema.styleBody(lblBackupDesc);

        Tema.styleSubTitle(lblRestoreTitle);
        Tema.styleBadge(lblSourceBadge, Tema.ACCENT_SOFT, Tema.ACCENT);
        Tema.styleBody(lblRestoreDesc);

        Tema.styleSubTitle(lblStatusTitle);
        Tema.styleSubTitle(lblHistoryTitle);

        Tema.styleMuted(lblFileTag);
        Tema.styleMuted(lblStasiunTag);
        Tema.styleMuted(lblKeretaTag);
        Tema.styleMuted(lblJadwalTag);
        Tema.styleMuted(lblUserTag);
        Tema.styleMuted(lblPemesananTag);

        lblFileVal.setFont(Tema.FONT_BODY_BOLD);
        lblStasiunVal.setFont(Tema.FONT_BODY_BOLD);
        lblKeretaVal.setFont(Tema.FONT_BODY_BOLD);
        lblJadwalVal.setFont(Tema.FONT_BODY_BOLD);
        lblUserVal.setFont(Tema.FONT_BODY_BOLD);
        lblPemesananVal.setFont(Tema.FONT_BODY_BOLD);

        Tema.stylePrimaryButton(btnExport);
        Tema.styleAccentButton(btnImport);

        backupTableModel = new DefaultTableModel(
                new Object[]{"No", "Nama File Backup", "Ukuran", "Waktu Dibuat"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblBackupList.setModel(backupTableModel);
        Tema.styleTable(tblBackupList);
        Tema.styleScrollPane(scrBackupList);
    }

    private void setupEvents() {
        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.lakukanBackupJson(BackupRestorePanel.this);
                    refreshData();
                }
            }
        });

        btnImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.lakukanRestoreJson(BackupRestorePanel.this);
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
        File nextFile = ds.getFileBackupBerikutnya();
        lblNextBackup.setText("Berikutnya: data/" + nextFile.getName());
        btnExport.setText("Buat File data/" + nextFile.getName() + " Sekarang");

        lblFileVal.setText("data/data.json");
        lblStasiunVal.setText(ds.getStasiunList().size() + " stasiun");
        lblKeretaVal.setText(ds.getKeretaList().size() + " kereta");
        lblJadwalVal.setText(ds.getJadwalList().size() + " jadwal");
        lblUserVal.setText(ds.getUserList().size() + " akun");
        lblPemesananVal.setText(ds.getPemesananList().size() + " pesanan");

        backupTableModel.setRowCount(0);
        ArrayList<File> backups = ds.getDaftarFileBackup();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int no = 1;
        for (File f : backups) {
            long kb = Math.max(1L, Math.round(f.length() / 1024.0));
            backupTableModel.addRow(new Object[]{
                no++,
                "data/" + f.getName(),
                kb + " KB",
                sdf.format(new Date(f.lastModified()))
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlContent = new javax.swing.JPanel();
        pnlCards = new javax.swing.JPanel();
        pnlBackupCard = new javax.swing.JPanel();
        pnlBackupTop = new javax.swing.JPanel();
        lblBackupTitle = new javax.swing.JLabel();
        lblNextBackup = new javax.swing.JLabel();
        lblBackupDesc = new javax.swing.JLabel();
        btnExport = new javax.swing.JButton();
        pnlRestoreCard = new javax.swing.JPanel();
        pnlRestoreTop = new javax.swing.JPanel();
        lblRestoreTitle = new javax.swing.JLabel();
        lblSourceBadge = new javax.swing.JLabel();
        lblRestoreDesc = new javax.swing.JLabel();
        btnImport = new javax.swing.JButton();
        pnlBottomSplit = new javax.swing.JPanel();
        pnlStatusCard = new javax.swing.JPanel();
        lblStatusTitle = new javax.swing.JLabel();
        pnlInfoGrid = new javax.swing.JPanel();
        lblFileTag = new javax.swing.JLabel();
        lblFileVal = new javax.swing.JLabel();
        lblStasiunTag = new javax.swing.JLabel();
        lblStasiunVal = new javax.swing.JLabel();
        lblKeretaTag = new javax.swing.JLabel();
        lblKeretaVal = new javax.swing.JLabel();
        lblJadwalTag = new javax.swing.JLabel();
        lblJadwalVal = new javax.swing.JLabel();
        lblUserTag = new javax.swing.JLabel();
        lblUserVal = new javax.swing.JLabel();
        lblPemesananTag = new javax.swing.JLabel();
        lblPemesananVal = new javax.swing.JLabel();
        pnlHistoryCard = new javax.swing.JPanel();
        lblHistoryTitle = new javax.swing.JLabel();
        scrBackupList = new javax.swing.JScrollPane();
        tblBackupList = new javax.swing.JTable();

        setBackground(new java.awt.Color(245, 247, 250));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 26, 22, 26));
        setPreferredSize(new java.awt.Dimension(1120, 680));
        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setOpaque(false);
        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Backup & Restore Data JSON");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 20));
        lblTitle.setForeground(new java.awt.Color(27, 54, 93));
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Simpan data sementara ArrayList ke file backup berurutan di folder data/ atau muat ulang dari data/data.json");
        lblSubtitle.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblSubtitle.setForeground(new java.awt.Color(100, 116, 139));
        pnlHeader.add(lblSubtitle);
        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlContent.setOpaque(false);
        pnlContent.setLayout(new java.awt.BorderLayout(0, 16));

        pnlCards.setOpaque(false);
        pnlCards.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        pnlBackupCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlBackupCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(20, 22, 20, 22)));
        pnlBackupCard.setLayout(new java.awt.BorderLayout(0, 14));

        pnlBackupTop.setOpaque(false);
        pnlBackupTop.setLayout(new java.awt.BorderLayout());

        lblBackupTitle.setText("Backup Data Sementara ArrayList");
        lblBackupTitle.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblBackupTitle.setForeground(new java.awt.Color(15, 23, 42));
        pnlBackupTop.add(lblBackupTitle, java.awt.BorderLayout.WEST);

        lblNextBackup.setText("Target: data/backup-1.json");
        lblNextBackup.setBackground(new java.awt.Color(239, 246, 255));
        lblNextBackup.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10));
        lblNextBackup.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblNextBackup.setForeground(new java.awt.Color(27, 54, 93));
        lblNextBackup.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNextBackup.setOpaque(true);
        pnlBackupTop.add(lblNextBackup, java.awt.BorderLayout.EAST);
        pnlBackupCard.add(pnlBackupTop, java.awt.BorderLayout.NORTH);

        lblBackupDesc.setText("Membuat file JSON baru secara otomatis di folder data/ (backup-1.json, backup-2.json, dst.).");
        lblBackupDesc.setFont(new java.awt.Font("Segoe UI", 0, 13));
        lblBackupDesc.setForeground(new java.awt.Color(15, 23, 42));
        pnlBackupCard.add(lblBackupDesc, java.awt.BorderLayout.CENTER);

        btnExport.setText("Buat File Backup Baru Sekarang");
        btnExport.setBackground(new java.awt.Color(27, 54, 93));
        btnExport.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnExport.setForeground(new java.awt.Color(255, 255, 255));
        btnExport.setFocusPainted(false);
        pnlBackupCard.add(btnExport, java.awt.BorderLayout.SOUTH);
        pnlCards.add(pnlBackupCard);

        pnlRestoreCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlRestoreCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(20, 22, 20, 22)));
        pnlRestoreCard.setLayout(new java.awt.BorderLayout(0, 14));

        pnlRestoreTop.setOpaque(false);
        pnlRestoreTop.setLayout(new java.awt.BorderLayout());

        lblRestoreTitle.setText("Restore Data Utama Sistem");
        lblRestoreTitle.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblRestoreTitle.setForeground(new java.awt.Color(15, 23, 42));
        pnlRestoreTop.add(lblRestoreTitle, java.awt.BorderLayout.WEST);

        lblSourceBadge.setText("Sumber: data/data.json");
        lblSourceBadge.setBackground(new java.awt.Color(255, 247, 237));
        lblSourceBadge.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10));
        lblSourceBadge.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblSourceBadge.setForeground(new java.awt.Color(243, 112, 33));
        lblSourceBadge.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSourceBadge.setOpaque(true);
        pnlRestoreTop.add(lblSourceBadge, java.awt.BorderLayout.EAST);
        pnlRestoreCard.add(pnlRestoreTop, java.awt.BorderLayout.NORTH);

        lblRestoreDesc.setText("Memuat ulang data awal langsung dari data/data.json ke ArrayList memori dengan validasi aman.");
        lblRestoreDesc.setFont(new java.awt.Font("Segoe UI", 0, 13));
        lblRestoreDesc.setForeground(new java.awt.Color(15, 23, 42));
        pnlRestoreCard.add(lblRestoreDesc, java.awt.BorderLayout.CENTER);

        btnImport.setText("Restore Data dari data/data.json");
        btnImport.setBackground(new java.awt.Color(243, 112, 33));
        btnImport.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnImport.setForeground(new java.awt.Color(255, 255, 255));
        btnImport.setFocusPainted(false);
        pnlRestoreCard.add(btnImport, java.awt.BorderLayout.SOUTH);
        pnlCards.add(pnlRestoreCard);
        pnlContent.add(pnlCards, java.awt.BorderLayout.NORTH);

        pnlBottomSplit.setOpaque(false);
        pnlBottomSplit.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        pnlStatusCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlStatusCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(20, 22, 20, 22)));
        pnlStatusCard.setLayout(new java.awt.BorderLayout(0, 12));

        lblStatusTitle.setText("Isi Memori ArrayList Saat Ini");
        lblStatusTitle.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblStatusTitle.setForeground(new java.awt.Color(15, 23, 42));
        pnlStatusCard.add(lblStatusTitle, java.awt.BorderLayout.NORTH);

        pnlInfoGrid.setOpaque(false);
        pnlInfoGrid.setLayout(new java.awt.GridLayout(0, 2, 16, 10));

        lblFileTag.setText("Sumber Data Utama");
        lblFileTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblFileTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlInfoGrid.add(lblFileTag);

        lblFileVal.setText("data/data.json");
        lblFileVal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        pnlInfoGrid.add(lblFileVal);

        lblStasiunTag.setText("ArrayList Stasiun");
        lblStasiunTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblStasiunTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlInfoGrid.add(lblStasiunTag);

        lblStasiunVal.setText("0");
        lblStasiunVal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        pnlInfoGrid.add(lblStasiunVal);

        lblKeretaTag.setText("ArrayList Kereta");
        lblKeretaTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblKeretaTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlInfoGrid.add(lblKeretaTag);

        lblKeretaVal.setText("0");
        lblKeretaVal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        pnlInfoGrid.add(lblKeretaVal);

        lblJadwalTag.setText("ArrayList Jadwal");
        lblJadwalTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblJadwalTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlInfoGrid.add(lblJadwalTag);

        lblJadwalVal.setText("0");
        lblJadwalVal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        pnlInfoGrid.add(lblJadwalVal);

        lblUserTag.setText("ArrayList Pengguna");
        lblUserTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblUserTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlInfoGrid.add(lblUserTag);

        lblUserVal.setText("0");
        lblUserVal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        pnlInfoGrid.add(lblUserVal);

        lblPemesananTag.setText("ArrayList Pemesanan");
        lblPemesananTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblPemesananTag.setForeground(new java.awt.Color(100, 116, 139));
        pnlInfoGrid.add(lblPemesananTag);

        lblPemesananVal.setText("0");
        lblPemesananVal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        pnlInfoGrid.add(lblPemesananVal);
        pnlStatusCard.add(pnlInfoGrid, java.awt.BorderLayout.CENTER);
        pnlBottomSplit.add(pnlStatusCard);

        pnlHistoryCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlHistoryCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(20, 22, 20, 22)));
        pnlHistoryCard.setLayout(new java.awt.BorderLayout(0, 12));

        lblHistoryTitle.setText("File Backup Tersimpan di Folder data/");
        lblHistoryTitle.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblHistoryTitle.setForeground(new java.awt.Color(15, 23, 42));
        pnlHistoryCard.add(lblHistoryTitle, java.awt.BorderLayout.NORTH);

        scrBackupList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        tblBackupList.setFont(new java.awt.Font("Segoe UI", 0, 13));
        tblBackupList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No", "Nama File Backup", "Ukuran", "Waktu Dibuat"
            }
        ));
        tblBackupList.setRowHeight(34);
        scrBackupList.setViewportView(tblBackupList);
        pnlHistoryCard.add(scrBackupList, java.awt.BorderLayout.CENTER);
        pnlBottomSplit.add(pnlHistoryCard);
        pnlContent.add(pnlBottomSplit, java.awt.BorderLayout.CENTER);
        add(pnlContent, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnImport;
    private javax.swing.JLabel lblBackupDesc;
    private javax.swing.JLabel lblBackupTitle;
    private javax.swing.JLabel lblFileTag;
    private javax.swing.JLabel lblFileVal;
    private javax.swing.JLabel lblHistoryTitle;
    private javax.swing.JLabel lblJadwalTag;
    private javax.swing.JLabel lblJadwalVal;
    private javax.swing.JLabel lblKeretaTag;
    private javax.swing.JLabel lblKeretaVal;
    private javax.swing.JLabel lblNextBackup;
    private javax.swing.JLabel lblPemesananTag;
    private javax.swing.JLabel lblPemesananVal;
    private javax.swing.JLabel lblRestoreDesc;
    private javax.swing.JLabel lblRestoreTitle;
    private javax.swing.JLabel lblSourceBadge;
    private javax.swing.JLabel lblStasiunTag;
    private javax.swing.JLabel lblStasiunVal;
    private javax.swing.JLabel lblStatusTitle;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserTag;
    private javax.swing.JLabel lblUserVal;
    private javax.swing.JPanel pnlBackupCard;
    private javax.swing.JPanel pnlBackupTop;
    private javax.swing.JPanel pnlBottomSplit;
    private javax.swing.JPanel pnlCards;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlHistoryCard;
    private javax.swing.JPanel pnlInfoGrid;
    private javax.swing.JPanel pnlRestoreCard;
    private javax.swing.JPanel pnlRestoreTop;
    private javax.swing.JPanel pnlStatusCard;
    private javax.swing.JScrollPane scrBackupList;
    private javax.swing.JTable tblBackupList;
    // End of variables declaration//GEN-END:variables
}
