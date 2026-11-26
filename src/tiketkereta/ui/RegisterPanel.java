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

        setLayout(new java.awt.BorderLayout());

        pnlWrapper.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 54));

        pnlCard.setLayout(new java.awt.BorderLayout(0, 22));

        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblBrand.setText("ACCESS BY KAI • REGISTRASI PELANGGAN");
        pnlHeader.add(lblBrand);

        lblTitle.setText("Buat Akun Pelanggan Baru");
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Isi identitas resmi (KTP/NIK atau Paspor) untuk memesan tiket kereta api");
        pnlHeader.add(lblSubtitle);

        pnlCard.add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlForm.setLayout(new java.awt.GridLayout(3, 2, 18, 14));

        pnlFieldNama.setLayout(new java.awt.BorderLayout(0, 6));

        lblNama.setText("Nama Lengkap");
        pnlFieldNama.add(lblNama, java.awt.BorderLayout.NORTH);
        pnlFieldNama.add(txtNama, java.awt.BorderLayout.CENTER);

        pnlForm.add(pnlFieldNama);

        pnlFieldPhone.setLayout(new java.awt.BorderLayout(0, 6));

        lblPhone.setText("Nomor HP Aktif");
        pnlFieldPhone.add(lblPhone, java.awt.BorderLayout.NORTH);
        pnlFieldPhone.add(txtPhone, java.awt.BorderLayout.CENTER);

        pnlForm.add(pnlFieldPhone);

        pnlFieldJenis.setLayout(new java.awt.BorderLayout(0, 6));

        lblJenisId.setText("Jenis Identitas");
        pnlFieldJenis.add(lblJenisId, java.awt.BorderLayout.NORTH);
        pnlFieldJenis.add(cmbJenisId, java.awt.BorderLayout.CENTER);

        pnlForm.add(pnlFieldJenis);

        pnlFieldNoId.setLayout(new java.awt.BorderLayout(0, 6));

        lblNoId.setText("Nomor KTP / Paspor (Unik)");
        pnlFieldNoId.add(lblNoId, java.awt.BorderLayout.NORTH);
        pnlFieldNoId.add(txtNoId, java.awt.BorderLayout.CENTER);

        pnlForm.add(pnlFieldNoId);

        pnlFieldPass.setLayout(new java.awt.BorderLayout(0, 6));

        lblPassword.setText("Password");
        pnlFieldPass.add(lblPassword, java.awt.BorderLayout.NORTH);
        pnlFieldPass.add(pwdPassword, java.awt.BorderLayout.CENTER);

        pnlForm.add(pnlFieldPass);

        pnlFieldKonfirmasi.setLayout(new java.awt.BorderLayout(0, 6));

        lblKonfirmasi.setText("Ulangi Password");
        pnlFieldKonfirmasi.add(lblKonfirmasi, java.awt.BorderLayout.NORTH);
        pnlFieldKonfirmasi.add(pwdKonfirmasi, java.awt.BorderLayout.CENTER);

        pnlForm.add(pnlFieldKonfirmasi);

        pnlCard.add(pnlForm, java.awt.BorderLayout.CENTER);

        pnlActions.setLayout(new java.awt.BorderLayout(14, 0));

        btnKembali.setText("← Kembali ke Login");
        pnlActions.add(btnKembali, java.awt.BorderLayout.WEST);

        btnDaftar.setText("Daftar Akun Sekarang →");
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
