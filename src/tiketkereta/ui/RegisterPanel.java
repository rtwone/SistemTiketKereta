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
        Tema.centerFlowPanel(pnlWrapper, pnlCard);
        Tema.styleCard(pnlCard, 32, 38);

        pnlHeader.setOpaque(false);
        pnlForm.setOpaque(false);
        pnlFieldNama.setOpaque(false);
        pnlFieldPhone.setOpaque(false);
        pnlFieldJenis.setOpaque(false);
        pnlFieldNoId.setOpaque(false);
        pnlFieldPass.setOpaque(false);
        pnlFieldKonfirmasi.setOpaque(false);
        pnlActions.setOpaque(false);

        lblBrand.setFont(Tema.FONT_SMALL);
        lblBrand.setForeground(Tema.ACCENT);
        lblTitle.setFont(Tema.FONT_HERO);
        lblTitle.setForeground(Tema.PRIMARY);
        Tema.styleMuted(lblSubtitle);

        Tema.styleFieldLabel(lblNama);
        Tema.styleFieldLabel(lblPhone);
        Tema.styleFieldLabel(lblJenisId);
        Tema.styleFieldLabel(lblNoId);
        Tema.styleFieldLabel(lblPassword);
        Tema.styleFieldLabel(lblKonfirmasi);

        cmbJenisId.setModel(new DefaultComboBoxModel<String>(new String[]{"KTP", "PASPOR"}));
        Tema.styleComboBox(cmbJenisId);
        Tema.styleTextField(txtNama, "Masukkan nama sesuai identitas");
        Tema.styleTextField(txtPhone, "Contoh: 081234567890");
        Tema.styleTextField(txtNoId, "Contoh: 3529000000000002");
        Tema.styleTextField(pwdPassword, "Minimal 4 karakter");
        Tema.styleTextField(pwdKonfirmasi, "Ketik ulang password");

        Dimension fieldSize = Tema.dim(290, 42);
        cmbJenisId.setPreferredSize(fieldSize);
        txtNama.setPreferredSize(fieldSize);
        txtPhone.setPreferredSize(fieldSize);
        txtNoId.setPreferredSize(fieldSize);
        pwdPassword.setPreferredSize(fieldSize);
        pwdKonfirmasi.setPreferredSize(fieldSize);

        Tema.styleSecondaryButton(btnKembali);
        Tema.styleAccentButton(btnDaftar);
        btnKembali.setPreferredSize(Tema.dim(220, 44));
        btnDaftar.setPreferredSize(Tema.dim(280, 44));
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
                    "Registrasi berhasil! Silakan masuk menggunakan Nomor Identitas: " + baru.getIdNumber(),
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
        pnlFieldNama = new javax.swing.JPanel();
        lblNama = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        pnlFieldPhone = new javax.swing.JPanel();
        lblPhone = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        pnlFieldJenis = new javax.swing.JPanel();
        lblJenisId = new javax.swing.JLabel();
        cmbJenisId = new javax.swing.JComboBox<>();
        pnlFieldNoId = new javax.swing.JPanel();
        lblNoId = new javax.swing.JLabel();
        txtNoId = new javax.swing.JTextField();
        pnlFieldPass = new javax.swing.JPanel();
        lblPassword = new javax.swing.JLabel();
        pwdPassword = new javax.swing.JPasswordField();
        pnlFieldKonfirmasi = new javax.swing.JPanel();
        lblKonfirmasi = new javax.swing.JLabel();
        pwdKonfirmasi = new javax.swing.JPasswordField();
        pnlActions = new javax.swing.JPanel();
        btnKembali = new javax.swing.JButton();
        btnDaftar = new javax.swing.JButton();

        setBackground(new java.awt.Color(245, 247, 250));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 26, 22, 26));
        setPreferredSize(new java.awt.Dimension(1120, 680));
        setLayout(new java.awt.BorderLayout());

        pnlWrapper.setOpaque(false);
        pnlWrapper.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 105));

        pnlCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(32, 38, 32, 38)));
        pnlCard.setLayout(new java.awt.BorderLayout(0, 22));

        pnlHeader.setOpaque(false);
        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblBrand.setText("ACCESS BY KAI • REGISTRASI PELANGGAN");
        lblBrand.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblBrand.setForeground(new java.awt.Color(243, 112, 33));
        pnlHeader.add(lblBrand);

        lblTitle.setText("Buat Akun Pelanggan Baru");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24));
        lblTitle.setForeground(new java.awt.Color(27, 54, 93));
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Isi identitas resmi (KTP/NIK atau Paspor) untuk memesan tiket kereta api");
        lblSubtitle.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblSubtitle.setForeground(new java.awt.Color(100, 116, 139));
        pnlHeader.add(lblSubtitle);
        pnlCard.add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlForm.setOpaque(false);
        pnlForm.setLayout(new java.awt.GridLayout(3, 2, 18, 14));

        pnlFieldNama.setOpaque(false);
        pnlFieldNama.setLayout(new java.awt.BorderLayout(0, 6));

        lblNama.setText("Nama Lengkap");
        lblNama.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblNama.setForeground(new java.awt.Color(15, 23, 42));
        pnlFieldNama.add(lblNama, java.awt.BorderLayout.NORTH);

        txtNama.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txtNama.setPreferredSize(new java.awt.Dimension(290, 42));
        pnlFieldNama.add(txtNama, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldNama);

        pnlFieldPhone.setOpaque(false);
        pnlFieldPhone.setLayout(new java.awt.BorderLayout(0, 6));

        lblPhone.setText("Nomor HP Aktif");
        lblPhone.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblPhone.setForeground(new java.awt.Color(15, 23, 42));
        pnlFieldPhone.add(lblPhone, java.awt.BorderLayout.NORTH);

        txtPhone.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txtPhone.setPreferredSize(new java.awt.Dimension(290, 42));
        pnlFieldPhone.add(txtPhone, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldPhone);

        pnlFieldJenis.setOpaque(false);
        pnlFieldJenis.setLayout(new java.awt.BorderLayout(0, 6));

        lblJenisId.setText("Jenis Identitas");
        lblJenisId.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblJenisId.setForeground(new java.awt.Color(15, 23, 42));
        pnlFieldJenis.add(lblJenisId, java.awt.BorderLayout.NORTH);

        cmbJenisId.setFont(new java.awt.Font("Segoe UI", 0, 13));
        cmbJenisId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "KTP", "PASPOR" }));
        cmbJenisId.setPreferredSize(new java.awt.Dimension(290, 42));
        pnlFieldJenis.add(cmbJenisId, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldJenis);

        pnlFieldNoId.setOpaque(false);
        pnlFieldNoId.setLayout(new java.awt.BorderLayout(0, 6));

        lblNoId.setText("Nomor KTP / Paspor (Unik)");
        lblNoId.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblNoId.setForeground(new java.awt.Color(15, 23, 42));
        pnlFieldNoId.add(lblNoId, java.awt.BorderLayout.NORTH);

        txtNoId.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txtNoId.setPreferredSize(new java.awt.Dimension(290, 42));
        pnlFieldNoId.add(txtNoId, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldNoId);

        pnlFieldPass.setOpaque(false);
        pnlFieldPass.setLayout(new java.awt.BorderLayout(0, 6));

        lblPassword.setText("Password");
        lblPassword.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblPassword.setForeground(new java.awt.Color(15, 23, 42));
        pnlFieldPass.add(lblPassword, java.awt.BorderLayout.NORTH);

        pwdPassword.setFont(new java.awt.Font("Segoe UI", 0, 13));
        pwdPassword.setPreferredSize(new java.awt.Dimension(290, 42));
        pnlFieldPass.add(pwdPassword, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldPass);

        pnlFieldKonfirmasi.setOpaque(false);
        pnlFieldKonfirmasi.setLayout(new java.awt.BorderLayout(0, 6));

        lblKonfirmasi.setText("Ulangi Password");
        lblKonfirmasi.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblKonfirmasi.setForeground(new java.awt.Color(15, 23, 42));
        pnlFieldKonfirmasi.add(lblKonfirmasi, java.awt.BorderLayout.NORTH);

        pwdKonfirmasi.setFont(new java.awt.Font("Segoe UI", 0, 13));
        pwdKonfirmasi.setPreferredSize(new java.awt.Dimension(290, 42));
        pnlFieldKonfirmasi.add(pwdKonfirmasi, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldKonfirmasi);
        pnlCard.add(pnlForm, java.awt.BorderLayout.CENTER);

        pnlActions.setOpaque(false);
        pnlActions.setLayout(new java.awt.BorderLayout(14, 0));

        btnKembali.setText("← Kembali ke Login");
        btnKembali.setBackground(new java.awt.Color(255, 255, 255));
        btnKembali.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnKembali.setForeground(new java.awt.Color(15, 23, 42));
        btnKembali.setFocusPainted(false);
        btnKembali.setPreferredSize(new java.awt.Dimension(220, 44));
        pnlActions.add(btnKembali, java.awt.BorderLayout.WEST);

        btnDaftar.setText("Daftar Akun Sekarang →");
        btnDaftar.setBackground(new java.awt.Color(243, 112, 33));
        btnDaftar.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnDaftar.setForeground(new java.awt.Color(255, 255, 255));
        btnDaftar.setFocusPainted(false);
        btnDaftar.setPreferredSize(new java.awt.Dimension(280, 44));
        pnlActions.add(btnDaftar, java.awt.BorderLayout.EAST);
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
    private javax.swing.JPanel pnlFieldJenis;
    private javax.swing.JPanel pnlFieldKonfirmasi;
    private javax.swing.JPanel pnlFieldNama;
    private javax.swing.JPanel pnlFieldNoId;
    private javax.swing.JPanel pnlFieldPass;
    private javax.swing.JPanel pnlFieldPhone;
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
