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

        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Backup & Restore Data JSON");
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Simpan data sementara ArrayList ke file backup berurutan di folder data/ atau muat ulang dari data/data.json");
        pnlHeader.add(lblSubtitle);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlContent.setLayout(new java.awt.BorderLayout(0, 16));

        pnlCards.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        pnlBackupCard.setLayout(new java.awt.BorderLayout(0, 14));

        pnlBackupTop.setLayout(new java.awt.BorderLayout());

        lblBackupTitle.setText("Backup Data Sementara ArrayList");
        pnlBackupTop.add(lblBackupTitle, java.awt.BorderLayout.WEST);

        lblNextBackup.setText("Target: data/backup-1.json");
        pnlBackupTop.add(lblNextBackup, java.awt.BorderLayout.EAST);

        pnlBackupCard.add(pnlBackupTop, java.awt.BorderLayout.NORTH);

        lblBackupDesc.setText("Membuat file JSON baru secara otomatis di folder data/ (backup-1.json, backup-2.json, dst.).");
        pnlBackupCard.add(lblBackupDesc, java.awt.BorderLayout.CENTER);

        btnExport.setText("Buat File Backup Baru Sekarang");
        pnlBackupCard.add(btnExport, java.awt.BorderLayout.SOUTH);

        pnlCards.add(pnlBackupCard);

        pnlRestoreCard.setLayout(new java.awt.BorderLayout(0, 14));

        pnlRestoreTop.setLayout(new java.awt.BorderLayout());

        lblRestoreTitle.setText("Restore Data Utama Sistem");
        pnlRestoreTop.add(lblRestoreTitle, java.awt.BorderLayout.WEST);

        lblSourceBadge.setText("Sumber: data/data.json");
        pnlRestoreTop.add(lblSourceBadge, java.awt.BorderLayout.EAST);

        pnlRestoreCard.add(pnlRestoreTop, java.awt.BorderLayout.NORTH);

        lblRestoreDesc.setText("Memuat ulang data awal langsung dari data/data.json ke ArrayList memori dengan validasi aman.");
        pnlRestoreCard.add(lblRestoreDesc, java.awt.BorderLayout.CENTER);

        btnImport.setText("Restore Data dari data/data.json");
        pnlRestoreCard.add(btnImport, java.awt.BorderLayout.SOUTH);

        pnlCards.add(pnlRestoreCard);

        pnlContent.add(pnlCards, java.awt.BorderLayout.NORTH);

        pnlBottomSplit.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        pnlStatusCard.setLayout(new java.awt.BorderLayout(0, 12));

        lblStatusTitle.setText("Isi Memori ArrayList Saat Ini");
        pnlStatusCard.add(lblStatusTitle, java.awt.BorderLayout.NORTH);

        pnlInfoGrid.setLayout(new java.awt.GridLayout(0, 2, 16, 10));

        lblFileTag.setText("Sumber Data Utama");
        pnlInfoGrid.add(lblFileTag);

        lblFileVal.setText("data/data.json");
        pnlInfoGrid.add(lblFileVal);

        lblStasiunTag.setText("ArrayList Stasiun");
        pnlInfoGrid.add(lblStasiunTag);

        lblStasiunVal.setText("0");
        pnlInfoGrid.add(lblStasiunVal);

        lblKeretaTag.setText("ArrayList Kereta");
        pnlInfoGrid.add(lblKeretaTag);

        lblKeretaVal.setText("0");
        pnlInfoGrid.add(lblKeretaVal);

        lblJadwalTag.setText("ArrayList Jadwal");
        pnlInfoGrid.add(lblJadwalTag);

        lblJadwalVal.setText("0");
        pnlInfoGrid.add(lblJadwalVal);

        lblUserTag.setText("ArrayList Pengguna");
        pnlInfoGrid.add(lblUserTag);

        lblUserVal.setText("0");
        pnlInfoGrid.add(lblUserVal);

        lblPemesananTag.setText("ArrayList Pemesanan");
        pnlInfoGrid.add(lblPemesananTag);

        lblPemesananVal.setText("0");
        pnlInfoGrid.add(lblPemesananVal);

        pnlStatusCard.add(pnlInfoGrid, java.awt.BorderLayout.CENTER);

        pnlBottomSplit.add(pnlStatusCard);

        pnlHistoryCard.setLayout(new java.awt.BorderLayout(0, 12));

        lblHistoryTitle.setText("File Backup Tersimpan di Folder data/");
        pnlHistoryCard.add(lblHistoryTitle, java.awt.BorderLayout.NORTH);

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
