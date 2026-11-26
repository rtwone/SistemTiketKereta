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
        perbaruiModeForm();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlHeader.setOpaque(false);
        pnlBody.setOpaque(false);
        Tema.styleCard(pnlTableCard, 16, 18);
        pnlSearchBar.setOpaque(false);

        Tema.styleCard(pnlFormCard, 18, 20);
        pnlFormCard.setPreferredSize(Tema.dim(420, 500));
        pnlFormHead.setOpaque(false);
        pnlFieldsWrap.setOpaque(false);
        pnlNamaBox.setOpaque(false);
        pnlFields.setOpaque(false);
        pnlIdBox.setOpaque(false);
        pnlKodeBox.setOpaque(false);
        pnlKecBox.setOpaque(false);
        pnlKabBox.setOpaque(false);
        pnlKelasBox.setOpaque(false);
        pnlJarakBox.setOpaque(false);
        pnlButtons.setOpaque(false);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblSubtitle);
        Tema.styleFieldLabel(lblCari);
        Tema.styleSubTitle(lblFormTitle);

        Tema.styleFieldLabel(lblNama);
        Tema.styleFieldLabel(lblId);
        Tema.styleFieldLabel(lblKode);
        Tema.styleFieldLabel(lblKecamatan);
        Tema.styleFieldLabel(lblKabupaten);
        Tema.styleFieldLabel(lblKelas);
        Tema.styleFieldLabel(lblJarak);

        Tema.styleTextField(txtCari, "Ketik kode, nama stasiun, atau kabupaten...");
        Tema.styleTextField(txtNama, "Contoh: Stasiun Madura Baru");
        Tema.styleTextField(txtId, "Contoh: 22");
        Tema.styleTextField(txtKode, "Contoh: MDU");
        Tema.styleTextField(txtKecamatan, "Kecamatan");
        Tema.styleTextField(txtKabupaten, "Kabupaten");
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
        ArrayList<Stasiun> list = mainFrame.getDataStore().getStasiunList();
        for (Stasiun s : list) {
            if (!q.isEmpty()) {
                boolean cocok = s.getCode().toLowerCase().contains(q)
                        || s.getName().toLowerCase().contains(q)
                        || s.getDistrict().toLowerCase().contains(q)
                        || s.getRegency().toLowerCase().contains(q);
                if (!cocok) {
                    continue;
                }
            }
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
        perbaruiModeForm();
    }

    private void perbaruiModeForm() {
        boolean edit = !FormatUtil.kosong(kodeTerpilih);
        if (edit) {
            lblModeBadge.setText("Edit: " + kodeTerpilih);
            Tema.styleBadge(lblModeBadge, Tema.ACCENT_SOFT, Tema.ACCENT);
        } else {
            lblModeBadge.setText("Mode: Tambah Baru");
            Tema.styleBadge(lblModeBadge, Tema.PRIMARY_SOFT, Tema.PRIMARY);
        }
        btnUbah.setEnabled(edit);
        btnHapus.setEnabled(edit);
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
            muatTabel();
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
            muatTabel();
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
            muatTabel();
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
        scrStasiun = new javax.swing.JScrollPane();
        tblStasiun = new javax.swing.JTable();
        pnlFormCard = new javax.swing.JPanel();
        pnlFormHead = new javax.swing.JPanel();
        lblFormTitle = new javax.swing.JLabel();
        lblModeBadge = new javax.swing.JLabel();
        pnlFieldsWrap = new javax.swing.JPanel();
        pnlNamaBox = new javax.swing.JPanel();
        lblNama = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        pnlFields = new javax.swing.JPanel();
        pnlIdBox = new javax.swing.JPanel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        pnlKodeBox = new javax.swing.JPanel();
        lblKode = new javax.swing.JLabel();
        txtKode = new javax.swing.JTextField();
        pnlKecBox = new javax.swing.JPanel();
        lblKecamatan = new javax.swing.JLabel();
        txtKecamatan = new javax.swing.JTextField();
        pnlKabBox = new javax.swing.JPanel();
        lblKabupaten = new javax.swing.JLabel();
        txtKabupaten = new javax.swing.JTextField();
        pnlKelasBox = new javax.swing.JPanel();
        lblKelas = new javax.swing.JLabel();
        cmbKelas = new javax.swing.JComboBox<>();
        pnlJarakBox = new javax.swing.JPanel();
        lblJarak = new javax.swing.JLabel();
        txtJarak = new javax.swing.JTextField();
        pnlButtons = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();

        setBackground(new java.awt.Color(245, 247, 250));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 26, 22, 26));
        setPreferredSize(new java.awt.Dimension(1120, 680));
        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setOpaque(false);
        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Kelola Stasiun");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 20));
        lblTitle.setForeground(new java.awt.Color(27, 54, 93));
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Klik baris tabel untuk mengedit, atau klik Reset untuk beralih ke mode Tambah Stasiun Baru");
        lblSubtitle.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblSubtitle.setForeground(new java.awt.Color(100, 116, 139));
        pnlHeader.add(lblSubtitle);
        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlBody.setOpaque(false);
        pnlBody.setLayout(new java.awt.BorderLayout(16, 0));

        pnlTableCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlTableCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        pnlTableCard.setLayout(new java.awt.BorderLayout(0, 12));

        pnlSearchBar.setOpaque(false);
        pnlSearchBar.setLayout(new java.awt.BorderLayout(10, 0));

        lblCari.setText("Cari Stasiun:");
        lblCari.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblCari.setForeground(new java.awt.Color(15, 23, 42));
        pnlSearchBar.add(lblCari, java.awt.BorderLayout.WEST);

        txtCari.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txtCari.setPreferredSize(new java.awt.Dimension(180, 38));
        pnlSearchBar.add(txtCari, java.awt.BorderLayout.CENTER);
        pnlTableCard.add(pnlSearchBar, java.awt.BorderLayout.NORTH);

        scrStasiun.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        tblStasiun.setFont(new java.awt.Font("Segoe UI", 0, 13));
        tblStasiun.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Kode", "Nama Stasiun", "Kecamatan", "Kabupaten", "Kelas", "Jarak (KM)"
            }
        ));
        tblStasiun.setRowHeight(34);
        scrStasiun.setViewportView(tblStasiun);
        pnlTableCard.add(scrStasiun, java.awt.BorderLayout.CENTER);
        pnlBody.add(pnlTableCard, java.awt.BorderLayout.CENTER);

        pnlFormCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlFormCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        pnlFormCard.setPreferredSize(new java.awt.Dimension(420, 500));
        pnlFormCard.setLayout(new java.awt.BorderLayout(0, 14));

        pnlFormHead.setOpaque(false);
        pnlFormHead.setLayout(new java.awt.BorderLayout());

        lblFormTitle.setText("Form Stasiun");
        lblFormTitle.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblFormTitle.setForeground(new java.awt.Color(15, 23, 42));
        pnlFormHead.add(lblFormTitle, java.awt.BorderLayout.WEST);

        lblModeBadge.setText("Mode: Tambah Baru");
        pnlFormHead.add(lblModeBadge, java.awt.BorderLayout.EAST);
        pnlFormCard.add(pnlFormHead, java.awt.BorderLayout.NORTH);

        pnlFieldsWrap.setOpaque(false);
        pnlFieldsWrap.setLayout(new java.awt.BorderLayout(0, 10));

        pnlNamaBox.setOpaque(false);
        pnlNamaBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblNama.setText("Nama Stasiun");
        lblNama.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblNama.setForeground(new java.awt.Color(15, 23, 42));
        pnlNamaBox.add(lblNama, java.awt.BorderLayout.NORTH);

        txtNama.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txtNama.setPreferredSize(new java.awt.Dimension(180, 38));
        pnlNamaBox.add(txtNama, java.awt.BorderLayout.CENTER);
        pnlFieldsWrap.add(pnlNamaBox, java.awt.BorderLayout.NORTH);

        pnlFields.setOpaque(false);
        pnlFields.setLayout(new java.awt.GridLayout(3, 2, 10, 10));

        pnlIdBox.setOpaque(false);
        pnlIdBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblId.setText("ID Urut");
        lblId.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblId.setForeground(new java.awt.Color(15, 23, 42));
        pnlIdBox.add(lblId, java.awt.BorderLayout.NORTH);

        txtId.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txtId.setPreferredSize(new java.awt.Dimension(180, 38));
        pnlIdBox.add(txtId, java.awt.BorderLayout.CENTER);
        pnlFields.add(pnlIdBox);

        pnlKodeBox.setOpaque(false);
        pnlKodeBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblKode.setText("Kode Stasiun");
        lblKode.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblKode.setForeground(new java.awt.Color(15, 23, 42));
        pnlKodeBox.add(lblKode, java.awt.BorderLayout.NORTH);

        txtKode.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txtKode.setPreferredSize(new java.awt.Dimension(180, 38));
        pnlKodeBox.add(txtKode, java.awt.BorderLayout.CENTER);
        pnlFields.add(pnlKodeBox);

        pnlKecBox.setOpaque(false);
        pnlKecBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblKecamatan.setText("Kecamatan");
        lblKecamatan.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblKecamatan.setForeground(new java.awt.Color(15, 23, 42));
        pnlKecBox.add(lblKecamatan, java.awt.BorderLayout.NORTH);

        txtKecamatan.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txtKecamatan.setPreferredSize(new java.awt.Dimension(180, 38));
        pnlKecBox.add(txtKecamatan, java.awt.BorderLayout.CENTER);
        pnlFields.add(pnlKecBox);

        pnlKabBox.setOpaque(false);
        pnlKabBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblKabupaten.setText("Kabupaten");
        lblKabupaten.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblKabupaten.setForeground(new java.awt.Color(15, 23, 42));
        pnlKabBox.add(lblKabupaten, java.awt.BorderLayout.NORTH);

        txtKabupaten.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txtKabupaten.setPreferredSize(new java.awt.Dimension(180, 38));
        pnlKabBox.add(txtKabupaten, java.awt.BorderLayout.CENTER);
        pnlFields.add(pnlKabBox);

        pnlKelasBox.setOpaque(false);
        pnlKelasBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblKelas.setText("Kelas Stasiun");
        lblKelas.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblKelas.setForeground(new java.awt.Color(15, 23, 42));
        pnlKelasBox.add(lblKelas, java.awt.BorderLayout.NORTH);

        cmbKelas.setFont(new java.awt.Font("Segoe UI", 0, 13));
        cmbKelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "I", "II", "III" }));
        cmbKelas.setPreferredSize(new java.awt.Dimension(170, 38));
        pnlKelasBox.add(cmbKelas, java.awt.BorderLayout.CENTER);
        pnlFields.add(pnlKelasBox);

        pnlJarakBox.setOpaque(false);
        pnlJarakBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblJarak.setText("Jarak (KM)");
        lblJarak.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblJarak.setForeground(new java.awt.Color(15, 23, 42));
        pnlJarakBox.add(lblJarak, java.awt.BorderLayout.NORTH);

        txtJarak.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txtJarak.setPreferredSize(new java.awt.Dimension(180, 38));
        pnlJarakBox.add(txtJarak, java.awt.BorderLayout.CENTER);
        pnlFields.add(pnlJarakBox);
        pnlFieldsWrap.add(pnlFields, java.awt.BorderLayout.CENTER);
        pnlFormCard.add(pnlFieldsWrap, java.awt.BorderLayout.CENTER);

        pnlButtons.setOpaque(false);
        pnlButtons.setLayout(new java.awt.GridLayout(2, 2, 8, 8));

        btnTambah.setText("+ Tambah Baru");
        btnTambah.setBackground(new java.awt.Color(243, 112, 33));
        btnTambah.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setFocusPainted(false);
        pnlButtons.add(btnTambah);

        btnUbah.setText("Simpan Ubah");
        btnUbah.setBackground(new java.awt.Color(27, 54, 93));
        btnUbah.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnUbah.setForeground(new java.awt.Color(255, 255, 255));
        btnUbah.setFocusPainted(false);
        pnlButtons.add(btnUbah);

        btnHapus.setText("Hapus Stasiun");
        btnHapus.setBackground(new java.awt.Color(220, 38, 38));
        btnHapus.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setFocusPainted(false);
        pnlButtons.add(btnHapus);

        btnReset.setText("Bersihkan / Baru");
        btnReset.setBackground(new java.awt.Color(255, 255, 255));
        btnReset.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnReset.setForeground(new java.awt.Color(15, 23, 42));
        btnReset.setFocusPainted(false);
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
    private javax.swing.JLabel lblCari;
    private javax.swing.JLabel lblFormTitle;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblJarak;
    private javax.swing.JLabel lblKabupaten;
    private javax.swing.JLabel lblKecamatan;
    private javax.swing.JLabel lblKelas;
    private javax.swing.JLabel lblKode;
    private javax.swing.JLabel lblModeBadge;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlFields;
    private javax.swing.JPanel pnlFieldsWrap;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlFormHead;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlIdBox;
    private javax.swing.JPanel pnlJarakBox;
    private javax.swing.JPanel pnlKabBox;
    private javax.swing.JPanel pnlKecBox;
    private javax.swing.JPanel pnlKelasBox;
    private javax.swing.JPanel pnlKodeBox;
    private javax.swing.JPanel pnlNamaBox;
    private javax.swing.JPanel pnlSearchBar;
    private javax.swing.JPanel pnlTableCard;
    private javax.swing.JScrollPane scrStasiun;
    private javax.swing.JTable tblStasiun;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtJarak;
    private javax.swing.JTextField txtKabupaten;
    private javax.swing.JTextField txtKecamatan;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables
}
