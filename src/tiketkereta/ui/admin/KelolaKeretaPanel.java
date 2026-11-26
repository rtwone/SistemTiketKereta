package tiketkereta.ui.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
        perbaruiModeForm();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlHeader.setOpaque(false);
        pnlBody.setOpaque(false);
        Tema.styleCard(pnlTableCard, 16, 18);
        pnlSearchBar.setOpaque(false);

        Tema.styleCard(pnlFormCard, 18, 20);
        pnlFormCard.setPreferredSize(Tema.dim(440, 500));
        pnlFormHead.setOpaque(false);
        pnlFieldsWrap.setOpaque(false);
        pnlFields.setOpaque(false);
        pnlNomorBox.setOpaque(false);
        pnlNamaBox.setOpaque(false);
        pnlJenisBox.setOpaque(false);
        pnlTarifBox.setOpaque(false);
        pnlAsalBox.setOpaque(false);
        pnlTujuanBox.setOpaque(false);
        pnlGerbongBox.setOpaque(false);
        pnlButtons.setOpaque(false);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblSubtitle);
        Tema.styleFieldLabel(lblCari);
        Tema.styleSubTitle(lblFormTitle);

        Tema.styleFieldLabel(lblNomor);
        Tema.styleFieldLabel(lblNama);
        Tema.styleFieldLabel(lblJenis);
        Tema.styleFieldLabel(lblTarif);
        Tema.styleFieldLabel(lblAsal);
        Tema.styleFieldLabel(lblTujuan);
        Tema.styleFieldLabel(lblGerbong);

        Tema.styleTextField(txtCari, "Ketik nomor kereta, nama kereta, atau jenis layanan...");
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
        txtCari.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                muatTabel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                muatTabel();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                muatTabel();
            }
        });

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

        txtCari.setText("");
        muatTabel();
        resetForm();
    }

    private void muatTabel() {
        if (mainFrame == null) {
            return;
        }
        String q = FormatUtil.rapi(txtCari.getText()).toLowerCase();
        tableModel.setRowCount(0);
        for (Kereta k : mainFrame.getDataStore().getKeretaList()) {
            if (!q.isEmpty()) {
                boolean cocok = k.getTrainNumber().toLowerCase().contains(q)
                        || k.getName().toLowerCase().contains(q)
                        || k.getJenisLayanan().toLowerCase().contains(q)
                        || k.getRingkasanGerbong().toLowerCase().contains(q);
                if (!cocok) {
                    continue;
                }
            }
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
        perbaruiModeForm();
    }

    private void perbaruiModeForm() {
        boolean edit = !FormatUtil.kosong(nomorTerpilih);
        if (edit) {
            lblModeBadge.setText("Edit: " + nomorTerpilih);
            Tema.styleBadge(lblModeBadge, Tema.ACCENT_SOFT, Tema.ACCENT);
        } else {
            lblModeBadge.setText("Mode: Tambah Baru");
            Tema.styleBadge(lblModeBadge, Tema.PRIMARY_SOFT, Tema.PRIMARY);
        }
        btnUbah.setEnabled(edit);
        btnHapus.setEnabled(edit);
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
            muatTabel();
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
            muatTabel();
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
            muatTabel();
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
        perbaruiModeForm();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlBody = new javax.swing.JPanel();
        pnlTableCard = new javax.swing.JPanel();
        pnlSearchBar = new javax.swing.JPanel();
        lblCari = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        scrKereta = new javax.swing.JScrollPane();
        tblKereta = new javax.swing.JTable();
        pnlFormCard = new javax.swing.JPanel();
        pnlFormHead = new javax.swing.JPanel();
        lblFormTitle = new javax.swing.JLabel();
        lblModeBadge = new javax.swing.JLabel();
        pnlFieldsWrap = new javax.swing.JPanel();
        pnlFields = new javax.swing.JPanel();
        pnlNomorBox = new javax.swing.JPanel();
        lblNomor = new javax.swing.JLabel();
        txtNomor = new javax.swing.JTextField();
        pnlNamaBox = new javax.swing.JPanel();
        lblNama = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        pnlJenisBox = new javax.swing.JPanel();
        lblJenis = new javax.swing.JLabel();
        cmbJenis = new javax.swing.JComboBox<>();
        pnlTarifBox = new javax.swing.JPanel();
        lblTarif = new javax.swing.JLabel();
        txtTarif = new javax.swing.JTextField();
        pnlAsalBox = new javax.swing.JPanel();
        lblAsal = new javax.swing.JLabel();
        cmbAsal = new javax.swing.JComboBox<>();
        pnlTujuanBox = new javax.swing.JPanel();
        lblTujuan = new javax.swing.JLabel();
        cmbTujuan = new javax.swing.JComboBox<>();
        pnlGerbongBox = new javax.swing.JPanel();
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

        lblSubtitle.setText("Klik baris tabel untuk mengubah data armada kereta, atau klik Bersihkan untuk menambah baru");
        pnlHeader.add(lblSubtitle);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlBody.setLayout(new java.awt.BorderLayout(16, 0));

        pnlTableCard.setLayout(new java.awt.BorderLayout(0, 12));

        pnlSearchBar.setLayout(new java.awt.BorderLayout(10, 0));

        lblCari.setText("Cari Kereta:");
        pnlSearchBar.add(lblCari, java.awt.BorderLayout.WEST);
        pnlSearchBar.add(txtCari, java.awt.BorderLayout.CENTER);

        pnlTableCard.add(pnlSearchBar, java.awt.BorderLayout.NORTH);

        scrKereta.setViewportView(tblKereta);

        pnlTableCard.add(scrKereta, java.awt.BorderLayout.CENTER);

        pnlBody.add(pnlTableCard, java.awt.BorderLayout.CENTER);

        pnlFormCard.setLayout(new java.awt.BorderLayout(0, 14));

        pnlFormHead.setLayout(new java.awt.BorderLayout());

        lblFormTitle.setText("Form Kereta");
        pnlFormHead.add(lblFormTitle, java.awt.BorderLayout.WEST);

        lblModeBadge.setText("Mode: Tambah Baru");
        pnlFormHead.add(lblModeBadge, java.awt.BorderLayout.EAST);

        pnlFormCard.add(pnlFormHead, java.awt.BorderLayout.NORTH);

        pnlFieldsWrap.setLayout(new java.awt.BorderLayout(0, 10));

        pnlFields.setLayout(new java.awt.GridLayout(3, 2, 10, 10));

        pnlNomorBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblNomor.setText("Nomor Kereta");
        pnlNomorBox.add(lblNomor, java.awt.BorderLayout.NORTH);
        pnlNomorBox.add(txtNomor, java.awt.BorderLayout.CENTER);

        pnlFields.add(pnlNomorBox);

        pnlNamaBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblNama.setText("Nama Kereta");
        pnlNamaBox.add(lblNama, java.awt.BorderLayout.NORTH);
        pnlNamaBox.add(txtNama, java.awt.BorderLayout.CENTER);

        pnlFields.add(pnlNamaBox);

        pnlJenisBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblJenis.setText("Jenis Layanan");
        pnlJenisBox.add(lblJenis, java.awt.BorderLayout.NORTH);
        pnlJenisBox.add(cmbJenis, java.awt.BorderLayout.CENTER);

        pnlFields.add(pnlJenisBox);

        pnlTarifBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblTarif.setText("Tarif Dasar (Rp)");
        pnlTarifBox.add(lblTarif, java.awt.BorderLayout.NORTH);
        pnlTarifBox.add(txtTarif, java.awt.BorderLayout.CENTER);

        pnlFields.add(pnlTarifBox);

        pnlAsalBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblAsal.setText("Stasiun Asal");
        pnlAsalBox.add(lblAsal, java.awt.BorderLayout.NORTH);
        pnlAsalBox.add(cmbAsal, java.awt.BorderLayout.CENTER);

        pnlFields.add(pnlAsalBox);

        pnlTujuanBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblTujuan.setText("Stasiun Tujuan");
        pnlTujuanBox.add(lblTujuan, java.awt.BorderLayout.NORTH);
        pnlTujuanBox.add(cmbTujuan, java.awt.BorderLayout.CENTER);

        pnlFields.add(pnlTujuanBox);

        pnlFieldsWrap.add(pnlFields, java.awt.BorderLayout.CENTER);

        pnlGerbongBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblGerbong.setText("Konfigurasi Gerbong (Format: Kelas:Jumlah:Kursi)");
        pnlGerbongBox.add(lblGerbong, java.awt.BorderLayout.NORTH);
        pnlGerbongBox.add(txtGerbong, java.awt.BorderLayout.CENTER);

        pnlFieldsWrap.add(pnlGerbongBox, java.awt.BorderLayout.SOUTH);

        pnlFormCard.add(pnlFieldsWrap, java.awt.BorderLayout.CENTER);

        pnlButtons.setLayout(new java.awt.GridLayout(2, 2, 8, 8));

        btnTambah.setText("+ Tambah Baru");
        pnlButtons.add(btnTambah);

        btnUbah.setText("Simpan Ubah");
        pnlButtons.add(btnUbah);

        btnHapus.setText("Hapus Kereta");
        pnlButtons.add(btnHapus);

        btnReset.setText("Bersihkan / Baru");
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
    private javax.swing.JLabel lblCari;
    private javax.swing.JLabel lblFormTitle;
    private javax.swing.JLabel lblGerbong;
    private javax.swing.JLabel lblJenis;
    private javax.swing.JLabel lblModeBadge;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblNomor;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTarif;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTujuan;
    private javax.swing.JPanel pnlAsalBox;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlFields;
    private javax.swing.JPanel pnlFieldsWrap;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlFormHead;
    private javax.swing.JPanel pnlGerbongBox;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlJenisBox;
    private javax.swing.JPanel pnlNamaBox;
    private javax.swing.JPanel pnlNomorBox;
    private javax.swing.JPanel pnlSearchBar;
    private javax.swing.JPanel pnlTableCard;
    private javax.swing.JPanel pnlTarifBox;
    private javax.swing.JPanel pnlTujuanBox;
    private javax.swing.JScrollPane scrKereta;
    private javax.swing.JTable tblKereta;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtGerbong;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNomor;
    private javax.swing.JTextField txtTarif;
    // End of variables declaration//GEN-END:variables
}
