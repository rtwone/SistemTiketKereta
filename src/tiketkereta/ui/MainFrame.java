package tiketkereta.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import tiketkereta.data.DataStore;
import tiketkereta.model.OpsiJadwal;
import tiketkereta.model.Pemesanan;
import tiketkereta.model.Stasiun;
import tiketkereta.model.User;
import tiketkereta.service.AuthService;
import tiketkereta.service.JadwalService;
import tiketkereta.service.PemesananService;
import tiketkereta.service.TarifService;
import tiketkereta.ui.admin.AdminDashboardPanel;
import tiketkereta.ui.admin.BackupRestorePanel;
import tiketkereta.ui.admin.KelolaJadwalPanel;
import tiketkereta.ui.admin.KelolaKeretaPanel;
import tiketkereta.ui.admin.KelolaPemesananPanel;
import tiketkereta.ui.admin.KelolaStasiunPanel;
import tiketkereta.util.Tema;
import tiketkereta.util.ValidasiException;

public class MainFrame extends javax.swing.JFrame {

    private static final String CARD_LOGIN = "LOGIN";
    private static final String CARD_REGISTER = "REGISTER";
    private static final String CARD_CARI = "CARI_TIKET";
    private static final String CARD_PILIH = "PILIH_KERETA";
    private static final String CARD_KURSI = "PESAN_KURSI";
    private static final String CARD_BAYAR = "PEMBAYARAN";
    private static final String CARD_ETIKET = "ETIKET";
    private static final String CARD_TIKET_SAYA = "TIKET_SAYA";
    private static final String CARD_CETAK_TIKET = "CETAK_TIKET";
    private static final String CARD_ADM_DASHBOARD = "ADM_DASHBOARD";
    private static final String CARD_ADM_STASIUN = "ADM_STASIUN";
    private static final String CARD_ADM_KERETA = "ADM_KERETA";
    private static final String CARD_ADM_JADWAL = "ADM_JADWAL";
    private static final String CARD_ADM_PEMESANAN = "ADM_PEMESANAN";
    private static final String CARD_ADM_BACKUP = "ADM_BACKUP";

    private DataStore dataStore;
    private AuthService authService;
    private TarifService tarifService;
    private JadwalService jadwalService;
    private PemesananService pemesananService;

    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private CariTiketPanel cariTiketPanel;
    private PilihKeretaPanel pilihKeretaPanel;
    private PesanKursiPanel pesanKursiPanel;
    private PembayaranPanel pembayaranPanel;
    private ETiketPanel eTiketPanel;
    private TiketSayaPanel tiketSayaPanel;
    private CetakTiketPanel cetakTiketPanel;

    private AdminDashboardPanel adminDashboardPanel;
    private KelolaStasiunPanel kelolaStasiunPanel;
    private KelolaKeretaPanel kelolaKeretaPanel;
    private KelolaJadwalPanel kelolaJadwalPanel;
    private KelolaPemesananPanel kelolaPemesananPanel;
    private BackupRestorePanel backupRestorePanel;

    private final Map<String, JButton> sidebarButtons;
    private final Map<String, String> sidebarLabels;
    private Stasiun lastAsal;
    private Stasiun lastTujuan;
    private int lastJumlahPenumpang;

    public MainFrame() {
        this.sidebarButtons = new LinkedHashMap<String, JButton>();
        this.sidebarLabels = new LinkedHashMap<String, String>();
        this.lastJumlahPenumpang = 1;
        initServices();
        initComponents();
        setupStyle();
        initScreens();
        setupEvents();
        tampilkanLogin();
    }

    private void initServices() {
        dataStore = new DataStore();
        try {
            dataStore.load();
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(null,
                    "Gagal memuat data/data.json:\n" + ex.getMessage(),
                    "Peringatan Data",
                    JOptionPane.ERROR_MESSAGE);
        }
        authService = new AuthService(dataStore);
        tarifService = new TarifService(dataStore);
        jadwalService = new JadwalService(dataStore);
        pemesananService = new PemesananService(dataStore);
    }

    private void setupStyle() {
        setMinimumSize(Tema.dim(1024, 680));
        setSize(Tema.dim(1280, 760));
        setLocationRelativeTo(null);
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        pnlSidebar.setBackground(Tema.PRIMARY);
        pnlSidebar.setPreferredSize(Tema.dim(265, 720));
        pnlSidebar.setBorder(BorderFactory.createEmptyBorder(Tema.s(24), Tema.s(18), Tema.s(22), Tema.s(18)));

        pnlBrand.setOpaque(false);
        pnlBrand.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0x2B4C7E)),
                BorderFactory.createEmptyBorder(0, Tema.s(4), Tema.s(16), Tema.s(4))));
        pnlNavWrap.setOpaque(false);
        pnlNavHolder.setOpaque(false);
        pnlNavMenu.setOpaque(false);

        pnlUserFooter.setBackground(Tema.PRIMARY_DARK);
        pnlUserFooter.setBorder(BorderFactory.createEmptyBorder(Tema.s(14), Tema.s(14), Tema.s(14), Tema.s(14)));
        pnlUserFooter.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        pnlUserMeta.setOpaque(false);

        lblBrandTitle.setFont(Tema.FONT_TITLE);
        lblBrandTitle.setForeground(Color.WHITE);
        lblBrandSub.setFont(Tema.FONT_SMALL);
        lblBrandSub.setForeground(Tema.ACCENT);

        lblNavSection.setFont(Tema.FONT_SMALL);
        lblNavSection.setForeground(new Color(0x94A3B8));
        lblNavSection.setBorder(BorderFactory.createEmptyBorder(0, Tema.s(6), 0, 0));

        lblUserName.setFont(Tema.FONT_BODY_BOLD);
        lblUserName.setForeground(Color.WHITE);
        lblUserSub.setFont(Tema.FONT_SMALL);
        lblUserSub.setForeground(new Color(0xCBD5E1));

        Tema.styleDangerButton(btnLogout);
        pnlContent.setBackground(Tema.CANVAS);
    }

    private void initScreens() {
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        pnlAuthHost.add(loginPanel, CARD_LOGIN);
        pnlAuthHost.add(registerPanel, CARD_REGISTER);

        cariTiketPanel = new CariTiketPanel(this);
        pilihKeretaPanel = new PilihKeretaPanel(this);
        pesanKursiPanel = new PesanKursiPanel(this);
        pembayaranPanel = new PembayaranPanel(this);
        eTiketPanel = new ETiketPanel(this);
        tiketSayaPanel = new TiketSayaPanel(this);
        cetakTiketPanel = new CetakTiketPanel(this);

        adminDashboardPanel = new AdminDashboardPanel(this);
        kelolaStasiunPanel = new KelolaStasiunPanel(this);
        kelolaKeretaPanel = new KelolaKeretaPanel(this);
        kelolaJadwalPanel = new KelolaJadwalPanel(this);
        kelolaPemesananPanel = new KelolaPemesananPanel(this);
        backupRestorePanel = new BackupRestorePanel(this);

        pnlContent.add(cariTiketPanel, CARD_CARI);
        pnlContent.add(pilihKeretaPanel, CARD_PILIH);
        pnlContent.add(pesanKursiPanel, CARD_KURSI);
        pnlContent.add(pembayaranPanel, CARD_BAYAR);
        pnlContent.add(eTiketPanel, CARD_ETIKET);
        pnlContent.add(tiketSayaPanel, CARD_TIKET_SAYA);
        pnlContent.add(cetakTiketPanel, CARD_CETAK_TIKET);

        pnlContent.add(adminDashboardPanel, CARD_ADM_DASHBOARD);
        pnlContent.add(kelolaStasiunPanel, CARD_ADM_STASIUN);
        pnlContent.add(kelolaKeretaPanel, CARD_ADM_KERETA);
        pnlContent.add(kelolaJadwalPanel, CARD_ADM_JADWAL);
        pnlContent.add(kelolaPemesananPanel, CARD_ADM_PEMESANAN);
        pnlContent.add(backupRestorePanel, CARD_ADM_BACKUP);
    }

    private void setupEvents() {
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int konfirmasi = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Keluar dari sesi akun saat ini?",
                        "Konfirmasi Keluar",
                        JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    authService.logout();
                    tampilkanLogin();
                }
            }
        });
    }

    public void tampilkanLogin() {
        showRootCard("AUTH");
        ((CardLayout) pnlAuthHost.getLayout()).show(pnlAuthHost, CARD_LOGIN);
        loginPanel.resetForm();
    }

    public void tampilkanRegister() {
        showRootCard("AUTH");
        ((CardLayout) pnlAuthHost.getLayout()).show(pnlAuthHost, CARD_REGISTER);
        registerPanel.resetForm();
    }

    public void onLoginBerhasil(User user) {
        lblUserName.setText(user.getFullName());
        lblUserSub.setText(user.getDisplaySubtitle());
        bangunSidebar(user.isAdmin());
        showRootCard("APP");
        if (user.isAdmin()) {
            tampilkanAdminPanel(CARD_ADM_DASHBOARD);
        } else {
            tampilkanCariTiket();
        }
    }

    private void bangunSidebar(boolean isAdmin) {
        pnlNavMenu.removeAll();
        sidebarButtons.clear();
        sidebarLabels.clear();
        lblNavSection.setText(isAdmin ? "MENU ADMINISTRATOR" : "MENU PELANGGAN");
        if (isAdmin) {
            tambahTombolNav(CARD_ADM_DASHBOARD, "Dashboard Ringkas");
            tambahTombolNav(CARD_ADM_STASIUN, "Kelola Stasiun");
            tambahTombolNav(CARD_ADM_KERETA, "Kelola Kereta");
            tambahTombolNav(CARD_ADM_JADWAL, "Kelola Jadwal");
            tambahTombolNav(CARD_ADM_PEMESANAN, "Kelola Pemesanan");
            tambahTombolNav(CARD_ADM_BACKUP, "Backup & Restore JSON");
        } else {
            tambahTombolNav(CARD_CARI, "Cari & Pesan Tiket");
            tambahTombolNav(CARD_TIKET_SAYA, "Tiket Saya");
            tambahTombolNav(CARD_CETAK_TIKET, "Cetak Tiket Mandiri");
        }
        pnlNavMenu.revalidate();
        pnlNavMenu.repaint();
    }

    private void tambahTombolNav(final String cardKey, String label) {
        JButton btn = new JButton("   " + label);
        Tema.styleSidebarButton(btn, false);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CARD_CARI.equals(cardKey)) {
                    tampilkanCariTiket();
                } else if (CARD_TIKET_SAYA.equals(cardKey)) {
                    tampilkanTiketSaya();
                } else if (CARD_CETAK_TIKET.equals(cardKey)) {
                    tampilkanCetakTiket();
                } else {
                    tampilkanAdminPanel(cardKey);
                }
            }
        });
        sidebarButtons.put(cardKey, btn);
        sidebarLabels.put(cardKey, label);
        pnlNavMenu.add(btn);
    }

    private void setActiveNav(String activeKey) {
        for (Map.Entry<String, JButton> entry : sidebarButtons.entrySet()) {
            boolean active = entry.getKey().equals(activeKey);
            String base = sidebarLabels.get(entry.getKey());
            entry.getValue().setText((active ? "»  " : "   ") + base);
            Tema.styleSidebarButton(entry.getValue(), active);
        }
    }

    public void tampilkanCariTiket() {
        cariTiketPanel.refreshData();
        showContentCard(CARD_CARI);
        setActiveNav(CARD_CARI);
    }

    public void tampilkanPilihKereta(Stasiun asal, Stasiun tujuan, int jumlahPenumpang, ArrayList<OpsiJadwal> hasil) {
        this.lastAsal = asal;
        this.lastTujuan = tujuan;
        this.lastJumlahPenumpang = jumlahPenumpang;
        pilihKeretaPanel.tampilkanHasil(asal, tujuan, jumlahPenumpang, hasil);
        showContentCard(CARD_PILIH);
        setActiveNav(CARD_CARI);
    }

    public void kembaliKePilihKereta() {
        if (lastAsal == null || lastTujuan == null) {
            tampilkanCariTiket();
            return;
        }
        try {
            ArrayList<OpsiJadwal> segar = jadwalService.cariJadwalCocok(
                    lastAsal.getCode(), lastTujuan.getCode(), lastJumlahPenumpang,
                    pemesananService, tarifService);
            tampilkanPilihKereta(lastAsal, lastTujuan, lastJumlahPenumpang, segar);
        } catch (ValidasiException ex) {
            tampilkanCariTiket();
        }
    }

    public void tampilkanPesanKursi(OpsiJadwal opsi, int jumlahPenumpang) {
        pesanKursiPanel.siapkanPemesanan(opsi, jumlahPenumpang);
        showContentCard(CARD_KURSI);
        setActiveNav(CARD_CARI);
    }

    public void tampilkanPembayaran(Pemesanan pesanan) {
        pembayaranPanel.tampilkanPesanan(pesanan);
        showContentCard(CARD_BAYAR);
        setActiveNav(CARD_TIKET_SAYA);
    }

    public void tampilkanETiket(Pemesanan pesanan) {
        eTiketPanel.tampilkanETiket(pesanan);
        showContentCard(CARD_ETIKET);
        setActiveNav(CARD_TIKET_SAYA);
    }

    public void tampilkanTiketSaya() {
        tiketSayaPanel.refreshData();
        showContentCard(CARD_TIKET_SAYA);
        setActiveNav(CARD_TIKET_SAYA);
    }

    public void tampilkanCetakTiket() {
        cetakTiketPanel.refreshData();
        showContentCard(CARD_CETAK_TIKET);
        setActiveNav(CARD_CETAK_TIKET);
    }

    public void mulaiCetakTiket(Pemesanan pesanan) {
        showContentCard(CARD_CETAK_TIKET);
        setActiveNav(CARD_CETAK_TIKET);
        cetakTiketPanel.mulaiSimulasiCetak(pesanan);
    }

    private void tampilkanAdminPanel(String cardKey) {
        if (CARD_ADM_DASHBOARD.equals(cardKey)) {
            adminDashboardPanel.refreshData();
        } else if (CARD_ADM_STASIUN.equals(cardKey)) {
            kelolaStasiunPanel.refreshData();
        } else if (CARD_ADM_KERETA.equals(cardKey)) {
            kelolaKeretaPanel.refreshData();
        } else if (CARD_ADM_JADWAL.equals(cardKey)) {
            kelolaJadwalPanel.refreshData();
        } else if (CARD_ADM_PEMESANAN.equals(cardKey)) {
            kelolaPemesananPanel.refreshData();
        } else if (CARD_ADM_BACKUP.equals(cardKey)) {
            backupRestorePanel.refreshData();
        }
        showContentCard(cardKey);
        setActiveNav(cardKey);
    }

    public void lakukanBackupJson(Component parent) {
        try {
            File fileBaru = dataStore.buatBackupOtomatis();
            if (backupRestorePanel != null) {
                backupRestorePanel.refreshData();
            }
            JOptionPane.showMessageDialog(parent,
                    "Backup data ArrayList berhasil dibuat!\nFile baru: data/" + fileBaru.getName(),
                    "Backup Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(parent, ex.getMessage(), "Backup Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void lakukanRestoreJson(Component parent) {
        int konfirmasi = JOptionPane.showConfirmDialog(parent,
                "Muat ulang seluruh data sistem dari file utama 'data/data.json'?\n"
                        + "Perubahan sementara di memori ArrayList saat ini akan digantikan.",
                "Konfirmasi Restore Data",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dataStore.restoreDefaultData();
            if (backupRestorePanel != null) {
                backupRestorePanel.refreshData();
            }
            JOptionPane.showMessageDialog(parent,
                    "Restore berhasil! Seluruh data sistem telah dimuat ulang dari data/data.json.",
                    "Restore Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(parent,
                    "Restore dibatalkan (data lama tetap aman):\n" + ex.getMessage(),
                    "Restore Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRootCard(String name) {
        ((CardLayout) pnlRoot.getLayout()).show(pnlRoot, name);
    }

    private void showContentCard(String name) {
        ((CardLayout) pnlContent.getLayout()).show(pnlContent, name);
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public TarifService getTarifService() {
        return tarifService;
    }

    public JadwalService getJadwalService() {
        return jadwalService;
    }

    public PemesananService getPemesananService() {
        return pemesananService;
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        Tema.applyGlobalDefaults();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                frame.setExtendedState(frame.getExtendedState() | javax.swing.JFrame.MAXIMIZED_BOTH);
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlRoot = new javax.swing.JPanel();
        pnlAuthHost = new javax.swing.JPanel();
        pnlAppShell = new javax.swing.JPanel();
        pnlSidebar = new javax.swing.JPanel();
        pnlBrand = new javax.swing.JPanel();
        lblBrandTitle = new javax.swing.JLabel();
        lblBrandSub = new javax.swing.JLabel();
        pnlNavWrap = new javax.swing.JPanel();
        lblNavSection = new javax.swing.JLabel();
        pnlNavHolder = new javax.swing.JPanel();
        pnlNavMenu = new javax.swing.JPanel();
        pnlUserFooter = new javax.swing.JPanel();
        pnlUserMeta = new javax.swing.JPanel();
        lblUserName = new javax.swing.JLabel();
        lblUserSub = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        pnlContent = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Access by KAI - Sistem Tiket Kereta");

        pnlRoot.setLayout(new java.awt.CardLayout());

        pnlAuthHost.setLayout(new java.awt.CardLayout());
        pnlRoot.add(pnlAuthHost, "AUTH");

        pnlAppShell.setLayout(new java.awt.BorderLayout());

        pnlSidebar.setLayout(new java.awt.BorderLayout(0, 20));

        pnlBrand.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblBrandTitle.setText("ACCESS BY KAI");
        pnlBrand.add(lblBrandTitle);

        lblBrandSub.setText("Jalur Kereta Api Madura");
        pnlBrand.add(lblBrandSub);

        pnlSidebar.add(pnlBrand, java.awt.BorderLayout.NORTH);

        pnlNavWrap.setLayout(new java.awt.BorderLayout(0, 10));

        lblNavSection.setText("NAVIGASI UTAMA");
        pnlNavWrap.add(lblNavSection, java.awt.BorderLayout.NORTH);

        pnlNavHolder.setLayout(new java.awt.BorderLayout());

        pnlNavMenu.setLayout(new java.awt.GridLayout(0, 1, 0, 8));
        pnlNavHolder.add(pnlNavMenu, java.awt.BorderLayout.NORTH);

        pnlNavWrap.add(pnlNavHolder, java.awt.BorderLayout.CENTER);

        pnlSidebar.add(pnlNavWrap, java.awt.BorderLayout.CENTER);

        pnlUserFooter.setLayout(new java.awt.BorderLayout(0, 10));

        pnlUserMeta.setLayout(new java.awt.GridLayout(0, 1, 0, 2));

        lblUserName.setText("Pengguna");
        pnlUserMeta.add(lblUserName);

        lblUserSub.setText("Role");
        pnlUserMeta.add(lblUserSub);

        pnlUserFooter.add(pnlUserMeta, java.awt.BorderLayout.CENTER);

        btnLogout.setText("Keluar Akun");
        pnlUserFooter.add(btnLogout, java.awt.BorderLayout.SOUTH);

        pnlSidebar.add(pnlUserFooter, java.awt.BorderLayout.SOUTH);

        pnlAppShell.add(pnlSidebar, java.awt.BorderLayout.WEST);

        pnlContent.setLayout(new java.awt.CardLayout());
        pnlAppShell.add(pnlContent, java.awt.BorderLayout.CENTER);

        pnlRoot.add(pnlAppShell, "APP");

        getContentPane().add(pnlRoot, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogout;
    private javax.swing.JLabel lblBrandSub;
    private javax.swing.JLabel lblBrandTitle;
    private javax.swing.JLabel lblNavSection;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JLabel lblUserSub;
    private javax.swing.JPanel pnlAppShell;
    private javax.swing.JPanel pnlAuthHost;
    private javax.swing.JPanel pnlBrand;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlNavHolder;
    private javax.swing.JPanel pnlNavMenu;
    private javax.swing.JPanel pnlNavWrap;
    private javax.swing.JPanel pnlRoot;
    private javax.swing.JPanel pnlSidebar;
    private javax.swing.JPanel pnlUserFooter;
    private javax.swing.JPanel pnlUserMeta;
    // End of variables declaration//GEN-END:variables
}
