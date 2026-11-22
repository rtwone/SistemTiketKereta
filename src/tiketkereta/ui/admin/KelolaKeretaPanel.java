package tiketkereta.ui.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import tiketkereta.model.Kereta;
import tiketkereta.model.Stasiun;
import tiketkereta.ui.MainFrame;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.Tema;
import tiketkereta.util.ValidasiException;

public class KelolaKeretaPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;
    private DefaultTableModel tableModel;
    private String nomorTerpilih;

    public KelolaKeretaPanel() {
        this(null);
    }

    public KelolaKeretaPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlHeader.setOpaque(false);
        pnlBody.setOpaque(false);
        Tema.styleCard(pnlTableCard, 14, 14);
        Tema.styleCard(pnlFormCard, 18, 20);
        pnlFormCard.setPreferredSize(Tema.dim(420, 500));
        pnlFields.setOpaque(false);
        pnlButtons.setOpaque(false);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblSubtitle);
        Tema.styleSubTitle(lblFormTitle);
        Tema.styleMuted(lblNomor);
        Tema.styleMuted(lblNama);
        Tema.styleMuted(lblJenis);
        Tema.styleMuted(lblAsal);
        Tema.styleMuted(lblTujuan);
        Tema.styleMuted(lblTarif);
        Tema.styleMuted(lblGerbong);

        Tema.styleTextField(txtNomor, "Contoh: KA 111");
        Tema.styleTextField(txtNama, "Contoh: KA Madura Jaya");
        cmbJenis.setModel(new DefaultComboBoxModel<String>(new String[]{"Ekspres", "Antarkota", "Lokal", "Komuter"}));
        Tema.styleComboBox(cmbJenis);
        Tema.styleComboBox(cmbAsal);
        Tema.styleComboBox(cmbTujuan);
        Tema.styleTextField(txtTarif, "Contoh: 75000");
        Tema.styleTextField(txtGerbong, "Eksekutif:3:50, Bisnis:3:64");

        Tema.styleAccentButton(btnTambah);
        Tema.stylePrimaryButton(btnUbah);
        Tema.styleDangerButton(btnHapus);
        Tema.styleSecondaryButton(btnReset);

        tableModel = new DefaultTableModel(
                new Object[]{"No. Kereta", "Nama Kereta", "Jenis", "Rute", "Tarif Dasar", "Konfigurasi Gerbong"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblKereta.setModel(tableModel);
        tblKereta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Tema.styleTable(tblKereta);
        Tema.styleScrollPane(scrKereta);
    }

    private void setupEvents() {
        tblKereta.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    isiFormDariTabel();
                }
            }
        });

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesTambah();
            }
        });
        btnUbah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesUbah();
            }
        });
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesHapus();
            }
        });
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });
    }

    public void refreshData() {
        if (mainFrame == null) {
            return;
        }
        ArrayList<Stasiun> stasiun = mainFrame.getDataStore().getStasiunList();
        String[] kodeStasiun = new String[stasiun.size()];
        for (int i = 0; i < stasiun.size(); i++) {
            kodeStasiun[i] = stasiun.get(i).getCode();
        }
        cmbAsal.setModel(new DefaultComboBoxModel<String>(kodeStasiun));
        cmbTujuan.setModel(new DefaultComboBoxModel<String>(kodeStasiun));

        tableModel.setRowCount(0);
        for (Kereta k : mainFrame.getDataStore().getKeretaList()) {
            tableModel.addRow(new Object[]{
                k.getTrainNumber(),
                k.getName(),
                k.getJenisLayanan(),
                k.getOriginCode() + " → " + k.getDestinationCode(),
                FormatUtil.formatRupiah(k.getBaseFare()),
                k.getRingkasanGerbong()
            });
        }
    }

    private void isiFormDariTabel() {
        int row = tblKereta.getSelectedRow();
        if (row < 0 || mainFrame == null) {
            return;
        }
        nomorTerpilih = String.valueOf(tableModel.getValueAt(row, 0));
        Kereta k = mainFrame.getDataStore().cariKeretaByNumber(nomorTerpilih);
        if (k == null) {
            return;
        }
        txtNomor.setText(k.getTrainNumber());
        txtNama.setText(k.getName());
        cmbJenis.setSelectedItem(k.getJenisLayanan());
        cmbAsal.setSelectedItem(k.getOriginCode());
        cmbTujuan.setSelectedItem(k.getDestinationCode());
        txtTarif.setText(String.valueOf(k.getBaseFare()));
        txtGerbong.setText(k.getRingkasanGerbong());
    }

    private int bacaTarif() throws ValidasiException {
        try {
            return Integer.parseInt(txtTarif.getText().trim());
        } catch (NumberFormatException ex) {
            throw new ValidasiException("Tarif dasar harus berupa angka bulat.");
        }
    }

    private void prosesTambah() {
        if (mainFrame == null) {
            return;
        }
        try {
            mainFrame.getJadwalService().tambahKereta(
                    String.valueOf(cmbJenis.getSelectedItem()),
                    txtNomor.getText(),
                    txtNama.getText(),
                    String.valueOf(cmbAsal.getSelectedItem()),
                    String.valueOf(cmbTujuan.getSelectedItem()),
                    bacaTarif(),
                    txtGerbong.getText()
            );
            refreshData();
            resetForm();
            JOptionPane.showMessageDialog(this, "Kereta baru berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void prosesUbah() {
        if (mainFrame == null) {
            return;
        }
        if (FormatUtil.kosong(nomorTerpilih)) {
            JOptionPane.showMessageDialog(this, "Pilih kereta pada tabel yang akan diubah.", "Informasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            mainFrame.getJadwalService().ubahKereta(
                    nomorTerpilih,
                    String.valueOf(cmbJenis.getSelectedItem()),
                    txtNomor.getText(),
                    txtNama.getText(),
                    String.valueOf(cmbAsal.getSelectedItem()),
                    String.valueOf(cmbTujuan.getSelectedItem()),
                    bacaTarif(),
                    txtGerbong.getText()
            );
            refreshData();
            resetForm();
            JOptionPane.showMessageDialog(this, "Data kereta berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void prosesHapus() {
        if (mainFrame == null) {
            return;
        }
        if (FormatUtil.kosong(nomorTerpilih)) {
            JOptionPane.showMessageDialog(this, "Pilih kereta pada tabel yang akan dihapus.", "Informasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int jawab = JOptionPane.showConfirmDialog(this,
                "Hapus kereta " + nomorTerpilih + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (jawab != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            mainFrame.getJadwalService().hapusKereta(nomorTerpilih);
            refreshData();
            resetForm();
            JOptionPane.showMessageDialog(this, "Data kereta berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal Hapus", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void resetForm() {
        nomorTerpilih = null;
        txtNomor.setText("");
        txtNama.setText("");
        cmbJenis.setSelectedIndex(0);
        if (cmbAsal.getItemCount() > 0) {
            cmbAsal.setSelectedIndex(0);
            cmbTujuan.setSelectedIndex(Math.min(1, cmbTujuan.getItemCount() - 1));
        }
        txtTarif.setText("");
        txtGerbong.setText("");
        tblKereta.clearSelection();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlBody = new javax.swing.JPanel();
        pnlTableCard = new javax.swing.JPanel();
        scrKereta = new javax.swing.JScrollPane();
        tblKereta = new javax.swing.JTable();
        pnlFormCard = new javax.swing.JPanel();
        lblFormTitle = new javax.swing.JLabel();
        pnlFields = new javax.swing.JPanel();
        lblNomor = new javax.swing.JLabel();
        txtNomor = new javax.swing.JTextField();
        lblNama = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        lblJenis = new javax.swing.JLabel();
        cmbJenis = new javax.swing.JComboBox<>();
        lblAsal = new javax.swing.JLabel();
        cmbAsal = new javax.swing.JComboBox<>();
        lblTujuan = new javax.swing.JLabel();
        cmbTujuan = new javax.swing.JComboBox<>();
        lblTarif = new javax.swing.JLabel();
        txtTarif = new javax.swing.JTextField();
        lblGerbong = new javax.swing.JLabel();
        txtGerbong = new javax.swing.JTextField();
        pnlButtons = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Kelola Kereta");
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Tambah, ubah, atau hapus armada kereta beserta konfigurasi gerbong");
        pnlHeader.add(lblSubtitle);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlBody.setLayout(new java.awt.BorderLayout(16, 0));

        pnlTableCard.setLayout(new java.awt.BorderLayout());

        scrKereta.setViewportView(tblKereta);

        pnlTableCard.add(scrKereta, java.awt.BorderLayout.CENTER);

        pnlBody.add(pnlTableCard, java.awt.BorderLayout.CENTER);

        pnlFormCard.setLayout(new java.awt.BorderLayout(0, 14));

        lblFormTitle.setText("Form Data Kereta");
        pnlFormCard.add(lblFormTitle, java.awt.BorderLayout.NORTH);

        pnlFields.setLayout(new java.awt.GridLayout(0, 2, 10, 10));

        lblNomor.setText("Nomor Kereta");
        pnlFields.add(lblNomor);
        pnlFields.add(txtNomor);

        lblNama.setText("Nama Kereta");
        pnlFields.add(lblNama);
        pnlFields.add(txtNama);

        lblJenis.setText("Jenis Layanan");
        pnlFields.add(lblJenis);

        pnlFields.add(cmbJenis);

        lblAsal.setText("Kode Stasiun Asal");
        pnlFields.add(lblAsal);

        pnlFields.add(cmbAsal);

        lblTujuan.setText("Kode Stasiun Tujuan");
        pnlFields.add(lblTujuan);

        pnlFields.add(cmbTujuan);

        lblTarif.setText("Tarif Dasar (Rp)");
        pnlFields.add(lblTarif);
        pnlFields.add(txtTarif);

        lblGerbong.setText("Gerbong (Kelas:Jml:Kursi)");
        pnlFields.add(lblGerbong);
        pnlFields.add(txtGerbong);

        pnlFormCard.add(pnlFields, java.awt.BorderLayout.CENTER);

        pnlButtons.setLayout(new java.awt.GridLayout(2, 2, 8, 8));

        btnTambah.setText("Tambah");
        pnlButtons.add(btnTambah);

        btnUbah.setText("Ubah");
        pnlButtons.add(btnUbah);

        btnHapus.setText("Hapus");
        pnlButtons.add(btnHapus);

        btnReset.setText("Reset");
        pnlButtons.add(btnReset);

        pnlFormCard.add(pnlButtons, java.awt.BorderLayout.SOUTH);

        pnlBody.add(pnlFormCard, java.awt.BorderLayout.EAST);

        add(pnlBody, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbAsal;
    private javax.swing.JComboBox<String> cmbJenis;
    private javax.swing.JComboBox<String> cmbTujuan;
    private javax.swing.JLabel lblAsal;
    private javax.swing.JLabel lblFormTitle;
    private javax.swing.JLabel lblGerbong;
    private javax.swing.JLabel lblJenis;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblNomor;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTarif;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTujuan;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlFields;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlTableCard;
    private javax.swing.JScrollPane scrKereta;
    private javax.swing.JTable tblKereta;
    private javax.swing.JTextField txtGerbong;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNomor;
    private javax.swing.JTextField txtTarif;
    // End of variables declaration//GEN-END:variables
}
