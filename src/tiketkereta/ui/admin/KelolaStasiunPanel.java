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
import tiketkereta.model.Stasiun;
import tiketkereta.ui.MainFrame;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.Tema;
import tiketkereta.util.ValidasiException;

public class KelolaStasiunPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;
    private DefaultTableModel tableModel;
    private String kodeTerpilih;

    public KelolaStasiunPanel() {
        this(null);
    }

    public KelolaStasiunPanel(MainFrame mainFrame) {
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
        pnlFormCard.setPreferredSize(Tema.dim(400, 500));
        pnlFields.setOpaque(false);
        pnlButtons.setOpaque(false);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblSubtitle);
        Tema.styleSubTitle(lblFormTitle);
        Tema.styleMuted(lblId);
        Tema.styleMuted(lblKode);
        Tema.styleMuted(lblNama);
        Tema.styleMuted(lblKecamatan);
        Tema.styleMuted(lblKabupaten);
        Tema.styleMuted(lblKelas);
        Tema.styleMuted(lblJarak);

        Tema.styleTextField(txtId, "Contoh: 22");
        Tema.styleTextField(txtKode, "Contoh: MDU");
        Tema.styleTextField(txtNama, "Stasiun ...");
        Tema.styleTextField(txtKecamatan, "Nama kecamatan");
        Tema.styleTextField(txtKabupaten, "Nama kabupaten");
        cmbKelas.setModel(new DefaultComboBoxModel<String>(new String[]{"I", "II", "III"}));
        Tema.styleComboBox(cmbKelas);
        Tema.styleTextField(txtJarak, "Contoh: 180");

        Tema.styleAccentButton(btnTambah);
        Tema.stylePrimaryButton(btnUbah);
        Tema.styleDangerButton(btnHapus);
        Tema.styleSecondaryButton(btnReset);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Kode", "Nama Stasiun", "Kecamatan", "Kabupaten", "Kelas", "Jarak (KM)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblStasiun.setModel(tableModel);
        tblStasiun.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Tema.styleTable(tblStasiun);
        Tema.styleScrollPane(scrStasiun);
    }

    private void setupEvents() {
        tblStasiun.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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
        tableModel.setRowCount(0);
        ArrayList<Stasiun> list = mainFrame.getDataStore().getStasiunList();
        for (Stasiun s : list) {
            tableModel.addRow(new Object[]{
                s.getId(),
                s.getCode(),
                s.getName(),
                s.getDistrict(),
                s.getRegency(),
                s.getKelasStasiun(),
                s.getDistanceKm()
            });
        }
    }

    private void isiFormDariTabel() {
        int row = tblStasiun.getSelectedRow();
        if (row < 0) {
            return;
        }
        txtId.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        kodeTerpilih = String.valueOf(tableModel.getValueAt(row, 1));
        txtKode.setText(kodeTerpilih);
        txtNama.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        txtKecamatan.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        txtKabupaten.setText(String.valueOf(tableModel.getValueAt(row, 4)));
        cmbKelas.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 5)));
        txtJarak.setText(String.valueOf(tableModel.getValueAt(row, 6)));
    }

    private Stasiun bacaForm() throws ValidasiException {
        int id;
        int jarak;
        try {
            id = Integer.parseInt(txtId.getText().trim());
            jarak = Integer.parseInt(txtJarak.getText().trim());
        } catch (NumberFormatException ex) {
            throw new ValidasiException("ID stasiun dan jarak KM harus berupa angka.");
        }
        return new Stasiun(
                id,
                FormatUtil.rapi(txtKode.getText()).toUpperCase(),
                FormatUtil.rapi(txtNama.getText()),
                FormatUtil.rapi(txtKecamatan.getText()),
                FormatUtil.rapi(txtKabupaten.getText()),
                String.valueOf(cmbKelas.getSelectedItem()),
                jarak
        );
    }

    private void prosesTambah() {
        if (mainFrame == null) {
            return;
        }
        try {
            mainFrame.getJadwalService().tambahStasiun(bacaForm());
            refreshData();
            resetForm();
            JOptionPane.showMessageDialog(this, "Stasiun baru berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void prosesUbah() {
        if (mainFrame == null) {
            return;
        }
        if (FormatUtil.kosong(kodeTerpilih)) {
            JOptionPane.showMessageDialog(this, "Pilih stasiun dari tabel yang akan diubah.", "Informasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            mainFrame.getJadwalService().ubahStasiun(kodeTerpilih, bacaForm());
            refreshData();
            resetForm();
            JOptionPane.showMessageDialog(this, "Data stasiun berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void prosesHapus() {
        if (mainFrame == null) {
            return;
        }
        if (FormatUtil.kosong(kodeTerpilih)) {
            JOptionPane.showMessageDialog(this, "Pilih stasiun dari tabel yang akan dihapus.", "Informasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int jawab = JOptionPane.showConfirmDialog(this,
                "Hapus stasiun dengan kode " + kodeTerpilih + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (jawab != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            mainFrame.getJadwalService().hapusStasiun(kodeTerpilih);
            refreshData();
            resetForm();
            JOptionPane.showMessageDialog(this, "Stasiun berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal Hapus", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void resetForm() {
        kodeTerpilih = null;
        txtId.setText("");
        txtKode.setText("");
        txtNama.setText("");
        txtKecamatan.setText("");
        txtKabupaten.setText("");
        cmbKelas.setSelectedIndex(0);
        txtJarak.setText("");
        tblStasiun.clearSelection();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlBody = new javax.swing.JPanel();
        pnlTableCard = new javax.swing.JPanel();
        scrStasiun = new javax.swing.JScrollPane();
        tblStasiun = new javax.swing.JTable();
        pnlFormCard = new javax.swing.JPanel();
        lblFormTitle = new javax.swing.JLabel();
        pnlFields = new javax.swing.JPanel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblKode = new javax.swing.JLabel();
        txtKode = new javax.swing.JTextField();
        lblNama = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        lblKecamatan = new javax.swing.JLabel();
        txtKecamatan = new javax.swing.JTextField();
        lblKabupaten = new javax.swing.JLabel();
        txtKabupaten = new javax.swing.JTextField();
        lblKelas = new javax.swing.JLabel();
        cmbKelas = new javax.swing.JComboBox<>();
        lblJarak = new javax.swing.JLabel();
        txtJarak = new javax.swing.JTextField();
        pnlButtons = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Kelola Stasiun");
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Tambah, ubah, atau hapus data stasiun jalur Madura");
        pnlHeader.add(lblSubtitle);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlBody.setLayout(new java.awt.BorderLayout(16, 0));

        pnlTableCard.setLayout(new java.awt.BorderLayout());

        scrStasiun.setViewportView(tblStasiun);

        pnlTableCard.add(scrStasiun, java.awt.BorderLayout.CENTER);

        pnlBody.add(pnlTableCard, java.awt.BorderLayout.CENTER);

        pnlFormCard.setLayout(new java.awt.BorderLayout(0, 14));

        lblFormTitle.setText("Form Data Stasiun");
        pnlFormCard.add(lblFormTitle, java.awt.BorderLayout.NORTH);

        pnlFields.setLayout(new java.awt.GridLayout(0, 2, 10, 10));

        lblId.setText("ID Urut Stasiun");
        pnlFields.add(lblId);
        pnlFields.add(txtId);

        lblKode.setText("Kode Stasiun");
        pnlFields.add(lblKode);
        pnlFields.add(txtKode);

        lblNama.setText("Nama Stasiun");
        pnlFields.add(lblNama);
        pnlFields.add(txtNama);

        lblKecamatan.setText("Kecamatan");
        pnlFields.add(lblKecamatan);
        pnlFields.add(txtKecamatan);

        lblKabupaten.setText("Kabupaten");
        pnlFields.add(lblKabupaten);
        pnlFields.add(txtKabupaten);

        lblKelas.setText("Kelas Stasiun");
        pnlFields.add(lblKelas);

        pnlFields.add(cmbKelas);

        lblJarak.setText("Jarak (KM)");
        pnlFields.add(lblJarak);
        pnlFields.add(txtJarak);

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
    private javax.swing.JComboBox<String> cmbKelas;
    private javax.swing.JLabel lblFormTitle;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblJarak;
    private javax.swing.JLabel lblKabupaten;
    private javax.swing.JLabel lblKecamatan;
    private javax.swing.JLabel lblKelas;
    private javax.swing.JLabel lblKode;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlFields;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlTableCard;
    private javax.swing.JScrollPane scrStasiun;
    private javax.swing.JTable tblStasiun;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtJarak;
    private javax.swing.JTextField txtKabupaten;
    private javax.swing.JTextField txtKecamatan;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables
}
