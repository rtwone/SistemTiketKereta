package tiketkereta.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import tiketkereta.model.User;
import tiketkereta.util.Tema;
import tiketkereta.util.ValidasiException;

public class LoginPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;

    public LoginPanel() {
        this(null);
    }

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlWrapper.setOpaque(false);
        pnlSplitBox.setOpaque(false);
        Tema.centerFlowPanel(pnlWrapper, pnlSplitBox);

        Tema.styleHeroCard(pnlHeroSide);
        pnlHeroSide.setPreferredSize(Tema.dim(370, 380));
        pnlHeroTop.setOpaque(false);
        pnlHeroFeatures.setOpaque(false);
        pnlDemoBox.setOpaque(false);
        pnlDemoButtons.setOpaque(false);

        lblBrand.setFont(Tema.FONT_SMALL);
        lblBrand.setForeground(Tema.ACCENT);
        lblHeroHeadline.setFont(Tema.FONT_TITLE);
        lblHeroHeadline.setForeground(Color.WHITE);
        lblHeroHeadline2.setFont(Tema.FONT_TITLE);
        lblHeroHeadline2.setForeground(Color.WHITE);

        Color featColor = new Color(0xE2E8F0);
        lblFeat1.setFont(Tema.FONT_BODY);
        lblFeat1.setForeground(featColor);
        lblFeat2.setFont(Tema.FONT_BODY);
        lblFeat2.setForeground(featColor);
        lblFeat3.setFont(Tema.FONT_BODY);
        lblFeat3.setForeground(featColor);

        lblDemoHint.setFont(Tema.FONT_SMALL);
        lblDemoHint.setForeground(new Color(0x94A3B8));
        Tema.styleSecondaryButton(btnDemoPelanggan);
        Tema.styleSecondaryButton(btnDemoAdmin);

        Tema.styleCard(pnlCard, 32, 38);
        pnlHeader.setOpaque(false);
        pnlForm.setOpaque(false);
        pnlIdGroup.setOpaque(false);
        pnlPassGroup.setOpaque(false);
        pnlActions.setOpaque(false);

        lblTitle.setFont(Tema.FONT_HERO);
        lblTitle.setForeground(Tema.PRIMARY);
        Tema.styleMuted(lblSubtitle);
        Tema.styleFieldLabel(lblIdentifier);
        Tema.styleFieldLabel(lblPassword);

        Tema.styleTextField(txtIdentifier, "Contoh: admin atau 3529000000000001");
        Tema.styleTextField(pwdPassword, "Masukkan password akun");
        txtIdentifier.setPreferredSize(Tema.dim(410, 42));
        pwdPassword.setPreferredSize(Tema.dim(410, 42));

        Tema.styleAccentButton(btnLogin);
        Tema.styleSecondaryButton(btnKeRegister);
        btnLogin.setPreferredSize(Tema.dim(410, 44));
        btnKeRegister.setPreferredSize(Tema.dim(410, 42));
    }

    private void setupEvents() {
        ActionListener loginAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesLogin();
            }
        };
        btnLogin.addActionListener(loginAction);
        pwdPassword.addActionListener(loginAction);
        txtIdentifier.addActionListener(loginAction);

        btnKeRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.tampilkanRegister();
                }
            }
        });

        btnDemoPelanggan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtIdentifier.setText("3529000000000001");
                pwdPassword.setText("budi123");
                btnLogin.requestFocusInWindow();
            }
        });

        btnDemoAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtIdentifier.setText("admin");
                pwdPassword.setText("admin123");
                btnLogin.requestFocusInWindow();
            }
        });
    }

    public void resetForm() {
        txtIdentifier.setText("");
        pwdPassword.setText("");
        txtIdentifier.requestFocusInWindow();
    }

    private void prosesLogin() {
        if (mainFrame == null) {
            return;
        }
        String identifier = txtIdentifier.getText();
        String password = new String(pwdPassword.getPassword());
        try {
            User user = mainFrame.getAuthService().login(identifier, password);
            resetForm();
            mainFrame.onLoginBerhasil(user);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Gagal", JOptionPane.WARNING_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlWrapper = new javax.swing.JPanel();
        pnlSplitBox = new javax.swing.JPanel();
        pnlHeroSide = new javax.swing.JPanel();
        pnlHeroTop = new javax.swing.JPanel();
        lblBrand = new javax.swing.JLabel();
        lblHeroHeadline = new javax.swing.JLabel();
        lblHeroHeadline2 = new javax.swing.JLabel();
        pnlHeroFeatures = new javax.swing.JPanel();
        lblFeat1 = new javax.swing.JLabel();
        lblFeat2 = new javax.swing.JLabel();
        lblFeat3 = new javax.swing.JLabel();
        pnlDemoBox = new javax.swing.JPanel();
        lblDemoHint = new javax.swing.JLabel();
        pnlDemoButtons = new javax.swing.JPanel();
        btnDemoPelanggan = new javax.swing.JButton();
        btnDemoAdmin = new javax.swing.JButton();
        pnlCard = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlForm = new javax.swing.JPanel();
        pnlIdGroup = new javax.swing.JPanel();
        lblIdentifier = new javax.swing.JLabel();
        txtIdentifier = new javax.swing.JTextField();
        pnlPassGroup = new javax.swing.JPanel();
        lblPassword = new javax.swing.JLabel();
        pwdPassword = new javax.swing.JPasswordField();
        pnlActions = new javax.swing.JPanel();
        btnLogin = new javax.swing.JButton();
        btnKeRegister = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        pnlWrapper.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 64));

        pnlSplitBox.setLayout(new java.awt.BorderLayout(20, 0));

        pnlHeroSide.setLayout(new java.awt.BorderLayout(0, 20));

        pnlHeroTop.setLayout(new java.awt.GridLayout(0, 1, 0, 8));

        lblBrand.setText("ACCESS BY KAI • MADURA");
        pnlHeroTop.add(lblBrand);

        lblHeroHeadline.setText("Perjalanan Kereta Api");
        pnlHeroTop.add(lblHeroHeadline);

        lblHeroHeadline2.setText("Lebih Mudah & Nyaman");
        pnlHeroTop.add(lblHeroHeadline2);

        pnlHeroSide.add(pnlHeroTop, java.awt.BorderLayout.NORTH);

        pnlHeroFeatures.setLayout(new java.awt.GridLayout(0, 1, 0, 8));

        lblFeat1.setText("•  21 Stasiun Jalur Kalianget - Kamal");
        pnlHeroFeatures.add(lblFeat1);

        lblFeat2.setText("•  48 Jadwal Harian Multi-Kelas");
        pnlHeroFeatures.add(lblFeat2);

        lblFeat3.setText("•  Pilih Kursi Gerbong & E-Tiket Instan");
        pnlHeroFeatures.add(lblFeat3);

        pnlHeroSide.add(pnlHeroFeatures, java.awt.BorderLayout.CENTER);

        pnlDemoBox.setLayout(new java.awt.BorderLayout(0, 8));

        lblDemoHint.setText("Pintasan Isi Akun Demo:");
        pnlDemoBox.add(lblDemoHint, java.awt.BorderLayout.NORTH);

        pnlDemoButtons.setLayout(new java.awt.GridLayout(1, 2, 8, 0));

        btnDemoPelanggan.setText("Pelanggan");
        pnlDemoButtons.add(btnDemoPelanggan);

        btnDemoAdmin.setText("Admin");
        pnlDemoButtons.add(btnDemoAdmin);

        pnlDemoBox.add(pnlDemoButtons, java.awt.BorderLayout.CENTER);

        pnlHeroSide.add(pnlDemoBox, java.awt.BorderLayout.SOUTH);

        pnlSplitBox.add(pnlHeroSide, java.awt.BorderLayout.WEST);

        pnlCard.setLayout(new java.awt.BorderLayout(0, 22));

        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblTitle.setText("Masuk ke Akun Anda");
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Gunakan username admin atau nomor KTP/Paspor pelanggan");
        pnlHeader.add(lblSubtitle);

        pnlCard.add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlForm.setLayout(new java.awt.GridLayout(2, 1, 0, 16));

        pnlIdGroup.setLayout(new java.awt.BorderLayout(0, 6));

        lblIdentifier.setText("Username / Nomor Identitas");
        pnlIdGroup.add(lblIdentifier, java.awt.BorderLayout.NORTH);
        pnlIdGroup.add(txtIdentifier, java.awt.BorderLayout.CENTER);

        pnlForm.add(pnlIdGroup);

        pnlPassGroup.setLayout(new java.awt.BorderLayout(0, 6));

        lblPassword.setText("Password");
        pnlPassGroup.add(lblPassword, java.awt.BorderLayout.NORTH);
        pnlPassGroup.add(pwdPassword, java.awt.BorderLayout.CENTER);

        pnlForm.add(pnlPassGroup);

        pnlCard.add(pnlForm, java.awt.BorderLayout.CENTER);

        pnlActions.setLayout(new java.awt.GridLayout(2, 1, 0, 10));

        btnLogin.setText("Masuk ke Aplikasi →");
        pnlActions.add(btnLogin);

        btnKeRegister.setText("Belum punya akun? Daftar Pelanggan Baru");
        pnlActions.add(btnKeRegister);

        pnlCard.add(pnlActions, java.awt.BorderLayout.SOUTH);

        pnlSplitBox.add(pnlCard, java.awt.BorderLayout.CENTER);

        pnlWrapper.add(pnlSplitBox);

        add(pnlWrapper, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDemoAdmin;
    private javax.swing.JButton btnDemoPelanggan;
    private javax.swing.JButton btnKeRegister;
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblDemoHint;
    private javax.swing.JLabel lblFeat1;
    private javax.swing.JLabel lblFeat2;
    private javax.swing.JLabel lblFeat3;
    private javax.swing.JLabel lblHeroHeadline;
    private javax.swing.JLabel lblHeroHeadline2;
    private javax.swing.JLabel lblIdentifier;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JPanel pnlDemoBox;
    private javax.swing.JPanel pnlDemoButtons;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlHeroFeatures;
    private javax.swing.JPanel pnlHeroSide;
    private javax.swing.JPanel pnlHeroTop;
    private javax.swing.JPanel pnlIdGroup;
    private javax.swing.JPanel pnlPassGroup;
    private javax.swing.JPanel pnlSplitBox;
    private javax.swing.JPanel pnlWrapper;
    private javax.swing.JPasswordField pwdPassword;
    private javax.swing.JTextField txtIdentifier;
    // End of variables declaration//GEN-END:variables
}
