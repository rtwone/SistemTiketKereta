package tiketkereta.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import tiketkereta.model.Pelanggan;
import tiketkereta.util.Tema;
import tiketkereta.util.ValidasiException;

public class RegisterPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;

    public RegisterPanel() {
        this(null);
    }

    public RegisterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlWrapper.setOpaque(false);
        Tema.styleCard(pnlCard, 32, 38);

        pnlHeader.setOpaque(false);
        pnlForm.setOpaque(false);
        pnlActions.setOpaque(false);

        lblBrand.setFont(Tema.FONT_SUBTITLE);
        lblBrand.setForeground(Tema.ACCENT);
        lblTitle.setFont(Tema.FONT_HERO);
        lblTitle.setForeground(Tema.PRIMARY);
        Tema.styleMuted(lblSubtitle);
        lblNama.setFont(Tema.FONT_BODY_BOLD);
        lblNama.setForeground(Tema.TEXT_PRIMARY);
        lblJenisId.setFont(Tema.FONT_BODY_BOLD);
        lblJenisId.setForeground(Tema.TEXT_PRIMARY);
        lblNoId.setFont(Tema.FONT_BODY_BOLD);
        lblNoId.setForeground(Tema.TEXT_PRIMARY);
        lblPhone.setFont(Tema.FONT_BODY_BOLD);
        lblPhone.setForeground(Tema.TEXT_PRIMARY);
        lblPassword.setFont(Tema.FONT_BODY_BOLD);
        lblPassword.setForeground(Tema.TEXT_PRIMARY);
        lblKonfirmasi.setFont(Tema.FONT_BODY_BOLD);
        lblKonfirmasi.setForeground(Tema.TEXT_PRIMARY);

        cmbJenisId.setModel(new DefaultComboBoxModel<String>(new String[]{"KTP", "PASPOR"}));
        Tema.styleComboBox(cmbJenisId);
        Tema.styleTextField(txtNama, "Masukkan nama sesuai identitas");
        Tema.styleTextField(txtNoId, "Contoh: 3529000000000002");
        Tema.styleTextField(txtPhone, "Contoh: 081234567890");
        Tema.styleTextField(pwdPassword, "Minimal 4 karakter");
        Tema.styleTextField(pwdKonfirmasi, "Ulangi password");

        Dimension fieldSize = Tema.dim(260, 40);
        cmbJenisId.setPreferredSize(fieldSize);
        txtNama.setPreferredSize(fieldSize);
        txtNoId.setPreferredSize(fieldSize);
        txtPhone.setPreferredSize(fieldSize);
        pwdPassword.setPreferredSize(fieldSize);
        pwdKonfirmasi.setPreferredSize(fieldSize);

        Tema.styleAccentButton(btnDaftar);
        Tema.styleSecondaryButton(btnKembali);
        btnDaftar.setPreferredSize(Tema.dim(480, 44));
        btnKembali.setPreferredSize(Tema.dim(480, 42));
    }

    private void setupEvents() {
        btnDaftar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesDaftar();
            }
        });
        btnKembali.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.tampilkanLogin();
                }
            }
        });
    }

    public void resetForm() {
        txtNama.setText("");
        cmbJenisId.setSelectedIndex(0);
        txtNoId.setText("");
        txtPhone.setText("");
        pwdPassword.setText("");
        pwdKonfirmasi.setText("");
    }

    private void prosesDaftar() {
        if (mainFrame == null) {
            return;
        }
        String nama = txtNama.getText();
        String jenisId = String.valueOf(cmbJenisId.getSelectedItem());
        String noId = txtNoId.getText();
        String phone = txtPhone.getText();
        String pass = new String(pwdPassword.getPassword());
        String konfirmasi = new String(pwdKonfirmasi.getPassword());

        try {
            Pelanggan baru = mainFrame.getAuthService().registerPelanggan(
                    nama, jenisId, noId, phone, pass, konfirmasi);
            JOptionPane.showMessageDialog(this,
                    "Registrasi berhasil! Silakan masuk menggunakan No. Identitas: " + baru.getIdNumber(),
                    "Registrasi Berhasil",
                    JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            mainFrame.tampilkanLogin();
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Registrasi Gagal", JOptionPane.WARNING_MESSAGE);
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
        lblNama = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        lblJenisId = new javax.swing.JLabel();
        cmbJenisId = new javax.swing.JComboBox<>();
        lblNoId = new javax.swing.JLabel();
        txtNoId = new javax.swing.JTextField();
        lblPhone = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        pwdPassword = new javax.swing.JPasswordField();
        lblKonfirmasi = new javax.swing.JLabel();
        pwdKonfirmasi = new javax.swing.JPasswordField();
        pnlActions = new javax.swing.JPanel();
        btnDaftar = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        pnlWrapper.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 48));

        pnlCard.setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblBrand.setText("ACCESS BY KAI");
        pnlHeader.add(lblBrand);

        lblTitle.setText("Registrasi Akun Pelanggan");
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Lengkapi data diri Anda untuk mulai memesan tiket kereta");
        pnlHeader.add(lblSubtitle);

        pnlCard.add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlForm.setLayout(new java.awt.GridLayout(0, 2, 14, 10));

        lblNama.setText("Nama Lengkap");
        pnlForm.add(lblNama);
        pnlForm.add(txtNama);

        lblJenisId.setText("Jenis Identitas");
        pnlForm.add(lblJenisId);

        pnlForm.add(cmbJenisId);

        lblNoId.setText("Nomor KTP / Paspor");
        pnlForm.add(lblNoId);
        pnlForm.add(txtNoId);

        lblPhone.setText("Nomor HP");
        pnlForm.add(lblPhone);
        pnlForm.add(txtPhone);

        lblPassword.setText("Password");
        pnlForm.add(lblPassword);
        pnlForm.add(pwdPassword);

        lblKonfirmasi.setText("Konfirmasi Password");
        pnlForm.add(lblKonfirmasi);
        pnlForm.add(pwdKonfirmasi);

        pnlCard.add(pnlForm, java.awt.BorderLayout.CENTER);

        pnlActions.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        btnDaftar.setText("Daftar Sekarang");
        pnlActions.add(btnDaftar);

        btnKembali.setText("Sudah punya akun? Kembali ke Login");
        pnlActions.add(btnKembali);

        pnlCard.add(pnlActions, java.awt.BorderLayout.SOUTH);

        pnlWrapper.add(pnlCard);

        add(pnlWrapper, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDaftar;
    private javax.swing.JButton btnKembali;
    private javax.swing.JComboBox<String> cmbJenisId;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblJenisId;
    private javax.swing.JLabel lblKonfirmasi;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblNoId;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlWrapper;
    private javax.swing.JPasswordField pwdKonfirmasi;
    private javax.swing.JPasswordField pwdPassword;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNoId;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration//GEN-END:variables
}
