package tiketkereta.ui.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import tiketkereta.data.DataStore;
import tiketkereta.ui.MainFrame;
import tiketkereta.util.Tema;

public class BackupRestorePanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;

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

        Tema.styleCard(pnlBackupCard, 20, 22);
        Tema.styleCard(pnlRestoreCard, 20, 22);
        Tema.styleCard(pnlStatusCard, 20, 22);

        pnlBackupText.setOpaque(false);
        pnlRestoreText.setOpaque(false);
        pnlInfoGrid.setOpaque(false);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblSubtitle);
        Tema.styleSubTitle(lblBackupTitle);
        Tema.styleMuted(lblBackupDesc);
        Tema.styleSubTitle(lblRestoreTitle);
        Tema.styleMuted(lblRestoreDesc);
        Tema.styleSubTitle(lblStatusTitle);

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
    }

    private void setupEvents() {
        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.lakukanBackupJson(BackupRestorePanel.this);
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
        lblFileVal.setText(ds.getActiveFile().getPath());
        lblStasiunVal.setText(ds.getStasiunList().size() + " stasiun");
        lblKeretaVal.setText(ds.getKeretaList().size() + " kereta");
        lblJadwalVal.setText(ds.getJadwalList().size() + " jadwal");
        lblUserVal.setText(ds.getUserList().size() + " pengguna");
        lblPemesananVal.setText(ds.getPemesananList().size() + " pesanan");
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
        pnlBackupText = new javax.swing.JPanel();
        lblBackupTitle = new javax.swing.JLabel();
        lblBackupDesc = new javax.swing.JLabel();
        btnExport = new javax.swing.JButton();
        pnlRestoreCard = new javax.swing.JPanel();
        pnlRestoreText = new javax.swing.JPanel();
        lblRestoreTitle = new javax.swing.JLabel();
        lblRestoreDesc = new javax.swing.JLabel();
        btnImport = new javax.swing.JButton();
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

        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Backup & Restore Data JSON");
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Cadangkan seluruh data ArrayList ke file JSON atau pulihkan data dari file cadangan");
        pnlHeader.add(lblSubtitle);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlContent.setLayout(new java.awt.BorderLayout(0, 16));

        pnlCards.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        pnlBackupCard.setLayout(new java.awt.BorderLayout(0, 14));

        pnlBackupText.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblBackupTitle.setText("Export / Backup JSON");
        pnlBackupText.add(lblBackupTitle);

        lblBackupDesc.setText("Simpan seluruh stasiun, kereta, jadwal, akun, dan pemesanan ke file .json pilihan Anda.");
        pnlBackupText.add(lblBackupDesc);

        pnlBackupCard.add(pnlBackupText, java.awt.BorderLayout.CENTER);

        btnExport.setText("Pilih Lokasi & Backup JSON");
        pnlBackupCard.add(btnExport, java.awt.BorderLayout.SOUTH);

        pnlCards.add(pnlBackupCard);

        pnlRestoreCard.setLayout(new java.awt.BorderLayout(0, 14));

        pnlRestoreText.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblRestoreTitle.setText("Import / Restore JSON");
        pnlRestoreText.add(lblRestoreTitle);

        lblRestoreDesc.setText("Muat ulang data dari file JSON valid (wajib memiliki minimal 1 akun ADMIN dan data stasiun).");
        pnlRestoreText.add(lblRestoreDesc);

        pnlRestoreCard.add(pnlRestoreText, java.awt.BorderLayout.CENTER);

        btnImport.setText("Pilih File & Restore JSON");
        pnlRestoreCard.add(btnImport, java.awt.BorderLayout.SOUTH);

        pnlCards.add(pnlRestoreCard);

        pnlContent.add(pnlCards, java.awt.BorderLayout.NORTH);

        pnlStatusCard.setLayout(new java.awt.BorderLayout(0, 12));

        lblStatusTitle.setText("Ringkasan Database Aktif");
        pnlStatusCard.add(lblStatusTitle, java.awt.BorderLayout.NORTH);

        pnlInfoGrid.setLayout(new java.awt.GridLayout(0, 2, 16, 10));

        lblFileTag.setText("File Aktif");
        pnlInfoGrid.add(lblFileTag);

        lblFileVal.setText("data/data.json");
        pnlInfoGrid.add(lblFileVal);

        lblStasiunTag.setText("Jumlah Stasiun");
        pnlInfoGrid.add(lblStasiunTag);

        lblStasiunVal.setText("0");
        pnlInfoGrid.add(lblStasiunVal);

        lblKeretaTag.setText("Jumlah Kereta");
        pnlInfoGrid.add(lblKeretaTag);

        lblKeretaVal.setText("0");
        pnlInfoGrid.add(lblKeretaVal);

        lblJadwalTag.setText("Jumlah Jadwal");
        pnlInfoGrid.add(lblJadwalTag);

        lblJadwalVal.setText("0");
        pnlInfoGrid.add(lblJadwalVal);

        lblUserTag.setText("Jumlah Akun Pengguna");
        pnlInfoGrid.add(lblUserTag);

        lblUserVal.setText("0");
        pnlInfoGrid.add(lblUserVal);

        lblPemesananTag.setText("Jumlah Pemesanan");
        pnlInfoGrid.add(lblPemesananTag);

        lblPemesananVal.setText("0");
        pnlInfoGrid.add(lblPemesananVal);

        pnlStatusCard.add(pnlInfoGrid, java.awt.BorderLayout.CENTER);

        pnlContent.add(pnlStatusCard, java.awt.BorderLayout.CENTER);

        add(pnlContent, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnImport;
    private javax.swing.JLabel lblBackupDesc;
    private javax.swing.JLabel lblBackupTitle;
    private javax.swing.JLabel lblFileTag;
    private javax.swing.JLabel lblFileVal;
    private javax.swing.JLabel lblJadwalTag;
    private javax.swing.JLabel lblJadwalVal;
    private javax.swing.JLabel lblKeretaTag;
    private javax.swing.JLabel lblKeretaVal;
    private javax.swing.JLabel lblPemesananTag;
    private javax.swing.JLabel lblPemesananVal;
    private javax.swing.JLabel lblRestoreDesc;
    private javax.swing.JLabel lblRestoreTitle;
    private javax.swing.JLabel lblStasiunTag;
    private javax.swing.JLabel lblStasiunVal;
    private javax.swing.JLabel lblStatusTitle;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserTag;
    private javax.swing.JLabel lblUserVal;
    private javax.swing.JPanel pnlBackupCard;
    private javax.swing.JPanel pnlBackupText;
    private javax.swing.JPanel pnlCards;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlInfoGrid;
    private javax.swing.JPanel pnlRestoreCard;
    private javax.swing.JPanel pnlRestoreText;
    private javax.swing.JPanel pnlStatusCard;
    // End of variables declaration//GEN-END:variables
}
