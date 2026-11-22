package tiketkereta.ui;

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
        Tema.styleCard(pnlCard, 36, 42);

        pnlHeader.setOpaque(false);
        pnlForm.setOpaque(false);
        pnlActions.setOpaque(false);

        lblBrand.setFont(Tema.FONT_SUBTITLE);
        lblBrand.setForeground(Tema.ACCENT);
        lblTitle.setFont(Tema.FONT_HERO);
        lblTitle.setForeground(Tema.PRIMARY);
        Tema.styleMuted(lblSubtitle);
        lblIdentifier.setFont(Tema.FONT_BODY_BOLD);
        lblIdentifier.setForeground(Tema.TEXT_PRIMARY);
        lblPassword.setFont(Tema.FONT_BODY_BOLD);
        lblPassword.setForeground(Tema.TEXT_PRIMARY);
        Tema.styleMuted(lblDemoHint);

        Tema.styleTextField(txtIdentifier, "Contoh: admin atau 3529000000000001");
        Tema.styleTextField(pwdPassword, "Masukkan password");
        txtIdentifier.setPreferredSize(Tema.dim(420, 42));
        pwdPassword.setPreferredSize(Tema.dim(420, 42));

        Tema.styleAccentButton(btnLogin);
        Tema.styleSecondaryButton(btnKeRegister);
        btnLogin.setPreferredSize(Tema.dim(420, 44));
        btnKeRegister.setPreferredSize(Tema.dim(420, 42));
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
        pnlCard = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        lblBrand = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlForm = new javax.swing.JPanel();
        lblIdentifier = new javax.swing.JLabel();
        txtIdentifier = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        pwdPassword = new javax.swing.JPasswordField();
        pnlActions = new javax.swing.JPanel();
        btnLogin = new javax.swing.JButton();
        btnKeRegister = new javax.swing.JButton();
        lblDemoHint = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        pnlWrapper.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 80));

        pnlCard.setLayout(new java.awt.BorderLayout(0, 18));

        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblBrand.setText("ACCESS BY KAI");
        pnlHeader.add(lblBrand);

        lblTitle.setText("Masuk ke Akun Anda");
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Gunakan username admin atau nomor KTP/Paspor pelanggan");
        pnlHeader.add(lblSubtitle);

        pnlCard.add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlForm.setLayout(new java.awt.GridLayout(0, 1, 0, 8));

        lblIdentifier.setText("Username / No. Identitas");
        pnlForm.add(lblIdentifier);
        pnlForm.add(txtIdentifier);

        lblPassword.setText("Password");
        pnlForm.add(lblPassword);
        pnlForm.add(pwdPassword);

        pnlCard.add(pnlForm, java.awt.BorderLayout.CENTER);

        pnlActions.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        btnLogin.setText("Masuk");
        pnlActions.add(btnLogin);

        btnKeRegister.setText("Belum punya akun? Daftar Pelanggan");
        pnlActions.add(btnKeRegister);

        lblDemoHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDemoHint.setText("Demo: admin / admin123  |  3529000000000001 / budi123");
        pnlActions.add(lblDemoHint);

        pnlCard.add(pnlActions, java.awt.BorderLayout.SOUTH);

        pnlWrapper.add(pnlCard);

        add(pnlWrapper, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKeRegister;
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblDemoHint;
    private javax.swing.JLabel lblIdentifier;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlWrapper;
    private javax.swing.JPasswordField pwdPassword;
    private javax.swing.JTextField txtIdentifier;
    // End of variables declaration//GEN-END:variables
}
