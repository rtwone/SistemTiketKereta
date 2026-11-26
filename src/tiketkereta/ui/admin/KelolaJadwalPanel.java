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
import tiketkereta.model.Jadwal;
import tiketkereta.model.Kereta;
import tiketkereta.model.Stasiun;
import tiketkereta.ui.MainFrame;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.Tema;
import tiketkereta.util.ValidasiException;

public class KelolaJadwalPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;
    private DefaultTableModel tableModel;
    private String idTerpilih;

    public KelolaJadwalPanel() {
        this(null);
    }

    public KelolaJadwalPanel(MainFrame mainFrame) {
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
        pnlFormCard.setPreferredSize(Tema.dim(440, 540));
        pnlTopFields.setOpaque(false);
        pnlFormHead.setOpaque(false);
        pnlGridMeta.setOpaque(false);
        pnlIdBox.setOpaque(false);
        pnlKeretaBox.setOpaque(false);
        pnlArahBox.setOpaque(false);
        pnlRuteBox.setOpaque(false);
        pnlAsalTujuan.setOpaque(false);
        pnlButtons.setOpaque(false);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblSubtitle);
        Tema.styleFieldLabel(lblCari);
        Tema.styleSubTitle(lblFormTitle);

        Tema.styleFieldLabel(lblIdJadwal);
        Tema.styleFieldLabel(lblKereta);
        Tema.styleFieldLabel(lblArah);
        Tema.styleFieldLabel(lblRute);
        Tema.styleFieldLabel(lblStops);

        Tema.styleTextField(txtCari, "Ketik ID jadwal, kode kereta, atau kode stasiun...");
        Tema.styleTextField(txtIdJadwal, "Contoh: JD-101-05");
        Tema.styleComboBox(cmbKereta);
        cmbArah.setModel(new DefaultComboBoxModel<String>(new String[]{"pergi", "balik"}));
        Tema.styleComboBox(cmbArah);
        Tema.styleComboBox(cmbAsal);
        Tema.styleComboBox(cmbTujuan);
        Tema.styleTextArea(txtStops);
        Tema.styleScrollPane(scrStops);

        Tema.styleAccentButton(btnTambah);
        Tema.stylePrimaryButton(btnUbah);
        Tema.styleDangerButton(btnHapus);
        Tema.styleSecondaryButton(btnReset);

        tableModel = new DefaultTableModel(
                new Object[]{"ID Jadwal", "No. Kereta", "Arah", "Rute", "Berangkat", "Tiba", "Stops"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblJadwal.setModel(tableModel);
        tblJadwal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Tema.styleTable(tblJadwal);
        Tema.styleScrollPane(scrJadwal);
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

        tblJadwal.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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
        ArrayList<Kereta> keretaList = mainFrame.getDataStore().getKeretaList();
        String[] nomorKereta = new String[keretaList.size()];
        for (int i = 0; i < keretaList.size(); i++) {
            nomorKereta[i] = keretaList.get(i).getTrainNumber();
        }
        cmbKereta.setModel(new DefaultComboBoxModel<String>(nomorKereta));

        ArrayList<Stasiun> stasiunList = mainFrame.getDataStore().getStasiunList();
        String[] kodeStasiun = new String[stasiunList.size()];
        for (int i = 0; i < stasiunList.size(); i++) {
            kodeStasiun[i] = stasiunList.get(i).getCode();
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
        for (Jadwal j : mainFrame.getDataStore().getJadwalList()) {
            if (!q.isEmpty()) {
                boolean cocok = j.getScheduleId().toLowerCase().contains(q)
                        || j.getTrainNumber().toLowerCase().contains(q)
                        || j.getOriginCode().toLowerCase().contains(q)
                        || j.getDestinationCode().toLowerCase().contains(q)
                        || j.getDirection().toLowerCase().contains(q);
                if (!cocok) {
                    continue;
                }
            }
            tableModel.addRow(new Object[]{
                j.getScheduleId(),
                j.getTrainNumber(),
                j.getDirection(),
                j.getOriginCode() + " → " + j.getDestinationCode(),
                j.getJamBerangkatAwal(),
                j.getJamTibaAkhir(),
                j.getStops().size() + " stasiun"
            });
        }
    }

    private void isiFormDariTabel() {
        int row = tblJadwal.getSelectedRow();
        if (row < 0 || mainFrame == null) {
            return;
        }
        idTerpilih = String.valueOf(tableModel.getValueAt(row, 0));
        Jadwal j = mainFrame.getDataStore().cariJadwalById(idTerpilih);
        if (j == null) {
            return;
        }
        txtIdJadwal.setText(j.getScheduleId());
        cmbKereta.setSelectedItem(j.getTrainNumber());
        cmbArah.setSelectedItem(j.getDirection());
        cmbAsal.setSelectedItem(j.getOriginCode());
        cmbTujuan.setSelectedItem(j.getDestinationCode());
        txtStops.setText(mainFrame.getJadwalService().formatStops(j.getStops()));
        perbaruiModeForm();
    }

    private void perbaruiModeForm() {
        boolean edit = !FormatUtil.kosong(idTerpilih);
        if (edit) {
            lblModeBadge.setText("Edit: " + idTerpilih);
            Tema.styleBadge(lblModeBadge, Tema.ACCENT_SOFT, Tema.ACCENT);
        } else {
            lblModeBadge.setText("Mode: Tambah Baru");
            Tema.styleBadge(lblModeBadge, Tema.PRIMARY_SOFT, Tema.PRIMARY);
        }
        btnUbah.setEnabled(edit);
        btnHapus.setEnabled(edit);
    }

    private void prosesTambah() {
        if (mainFrame == null) {
            return;
        }
        try {
            mainFrame.getJadwalService().tambahJadwal(
                    txtIdJadwal.getText(),
                    String.valueOf(cmbKereta.getSelectedItem()),
                    String.valueOf(cmbArah.getSelectedItem()),
                    String.valueOf(cmbAsal.getSelectedItem()),
                    String.valueOf(cmbTujuan.getSelectedItem()),
                    txtStops.getText()
            );
            muatTabel();
            resetForm();
            JOptionPane.showMessageDialog(this, "Jadwal baru berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void prosesUbah() {
        if (mainFrame == null) {
            return;
        }
        if (FormatUtil.kosong(idTerpilih)) {
            JOptionPane.showMessageDialog(this, "Pilih jadwal pada tabel yang akan diubah.", "Informasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            mainFrame.getJadwalService().ubahJadwal(
                    idTerpilih,
                    txtIdJadwal.getText(),
                    String.valueOf(cmbKereta.getSelectedItem()),
                    String.valueOf(cmbArah.getSelectedItem()),
                    String.valueOf(cmbAsal.getSelectedItem()),
                    String.valueOf(cmbTujuan.getSelectedItem()),
                    txtStops.getText()
            );
            muatTabel();
            resetForm();
            JOptionPane.showMessageDialog(this, "Jadwal berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void prosesHapus() {
        if (mainFrame == null) {
            return;
        }
        if (FormatUtil.kosong(idTerpilih)) {
            JOptionPane.showMessageDialog(this, "Pilih jadwal pada tabel yang akan dihapus.", "Informasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int jawab = JOptionPane.showConfirmDialog(this,
                "Hapus jadwal " + idTerpilih + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (jawab != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            mainFrame.getJadwalService().hapusJadwal(idTerpilih);
            muatTabel();
            resetForm();
            JOptionPane.showMessageDialog(this, "Jadwal berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal Hapus", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void resetForm() {
        idTerpilih = null;
        txtIdJadwal.setText("");
        if (cmbKereta.getItemCount() > 0) {
            cmbKereta.setSelectedIndex(0);
        }
        cmbArah.setSelectedIndex(0);
        if (cmbAsal.getItemCount() > 0) {
            cmbAsal.setSelectedIndex(0);
            cmbTujuan.setSelectedIndex(Math.min(1, cmbTujuan.getItemCount() - 1));
        }
        txtStops.setText("SMP|-|06:00\nPMK|06:44|06:46\nKML|08:15|-");
        tblJadwal.clearSelection();
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
        scrJadwal = new javax.swing.JScrollPane();
        tblJadwal = new javax.swing.JTable();
        pnlFormCard = new javax.swing.JPanel();
        pnlTopFields = new javax.swing.JPanel();
        pnlFormHead = new javax.swing.JPanel();
        lblFormTitle = new javax.swing.JLabel();
        lblModeBadge = new javax.swing.JLabel();
        pnlGridMeta = new javax.swing.JPanel();
        pnlIdBox = new javax.swing.JPanel();
        lblIdJadwal = new javax.swing.JLabel();
        txtIdJadwal = new javax.swing.JTextField();
        pnlKeretaBox = new javax.swing.JPanel();
        lblKereta = new javax.swing.JLabel();
        cmbKereta = new javax.swing.JComboBox<>();
        pnlArahBox = new javax.swing.JPanel();
        lblArah = new javax.swing.JLabel();
        cmbArah = new javax.swing.JComboBox<>();
        pnlRuteBox = new javax.swing.JPanel();
        lblRute = new javax.swing.JLabel();
        pnlAsalTujuan = new javax.swing.JPanel();
        cmbAsal = new javax.swing.JComboBox<>();
        cmbTujuan = new javax.swing.JComboBox<>();
        lblStops = new javax.swing.JLabel();
        scrStops = new javax.swing.JScrollPane();
        txtStops = new javax.swing.JTextArea();
        pnlButtons = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Kelola Jadwal");
        pnlHeader.add(lblTitle);

        lblSubtitle.setText("Cari jadwal dengan cepat, klik baris untuk mengedit pemberhentian, atau tambahkan jadwal baru");
        pnlHeader.add(lblSubtitle);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlBody.setLayout(new java.awt.BorderLayout(16, 0));

        pnlTableCard.setLayout(new java.awt.BorderLayout(0, 12));

        pnlSearchBar.setLayout(new java.awt.BorderLayout(10, 0));

        lblCari.setText("Cari Jadwal:");
        pnlSearchBar.add(lblCari, java.awt.BorderLayout.WEST);
        pnlSearchBar.add(txtCari, java.awt.BorderLayout.CENTER);

        pnlTableCard.add(pnlSearchBar, java.awt.BorderLayout.NORTH);

        scrJadwal.setViewportView(tblJadwal);

        pnlTableCard.add(scrJadwal, java.awt.BorderLayout.CENTER);

        pnlBody.add(pnlTableCard, java.awt.BorderLayout.CENTER);

        pnlFormCard.setLayout(new java.awt.BorderLayout(0, 12));

        pnlTopFields.setLayout(new java.awt.BorderLayout(0, 10));

        pnlFormHead.setLayout(new java.awt.BorderLayout());

        lblFormTitle.setText("Form Jadwal");
        pnlFormHead.add(lblFormTitle, java.awt.BorderLayout.WEST);

        lblModeBadge.setText("Mode: Tambah Baru");
        pnlFormHead.add(lblModeBadge, java.awt.BorderLayout.EAST);

        pnlTopFields.add(pnlFormHead, java.awt.BorderLayout.NORTH);

        pnlGridMeta.setLayout(new java.awt.GridLayout(2, 2, 10, 8));

        pnlIdBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblIdJadwal.setText("ID Jadwal");
        pnlIdBox.add(lblIdJadwal, java.awt.BorderLayout.NORTH);
        pnlIdBox.add(txtIdJadwal, java.awt.BorderLayout.CENTER);

        pnlGridMeta.add(pnlIdBox);

        pnlKeretaBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblKereta.setText("Kereta");
        pnlKeretaBox.add(lblKereta, java.awt.BorderLayout.NORTH);
        pnlKeretaBox.add(cmbKereta, java.awt.BorderLayout.CENTER);

        pnlGridMeta.add(pnlKeretaBox);

        pnlArahBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblArah.setText("Arah");
        pnlArahBox.add(lblArah, java.awt.BorderLayout.NORTH);
        pnlArahBox.add(cmbArah, java.awt.BorderLayout.CENTER);

        pnlGridMeta.add(pnlArahBox);

        pnlRuteBox.setLayout(new java.awt.BorderLayout(0, 4));

        lblRute.setText("Asal → Tujuan");
        pnlRuteBox.add(lblRute, java.awt.BorderLayout.NORTH);

        pnlAsalTujuan.setLayout(new java.awt.GridLayout(1, 2, 6, 0));
        pnlAsalTujuan.add(cmbAsal);
        pnlAsalTujuan.add(cmbTujuan);

        pnlRuteBox.add(pnlAsalTujuan, java.awt.BorderLayout.CENTER);

        pnlGridMeta.add(pnlRuteBox);

        pnlTopFields.add(pnlGridMeta, java.awt.BorderLayout.CENTER);

        lblStops.setText("Pemberhentian per Baris (KODE|TIBA|BERANGKAT)");
        pnlTopFields.add(lblStops, java.awt.BorderLayout.SOUTH);

        pnlFormCard.add(pnlTopFields, java.awt.BorderLayout.NORTH);

        txtStops.setColumns(20);
        txtStops.setRows(6);
        scrStops.setViewportView(txtStops);

        pnlFormCard.add(scrStops, java.awt.BorderLayout.CENTER);

        pnlButtons.setLayout(new java.awt.GridLayout(2, 2, 8, 8));

        btnTambah.setText("+ Tambah Baru");
        pnlButtons.add(btnTambah);

        btnUbah.setText("Simpan Ubah");
        pnlButtons.add(btnUbah);

        btnHapus.setText("Hapus Jadwal");
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
    private javax.swing.JComboBox<String> cmbArah;
    private javax.swing.JComboBox<String> cmbAsal;
    private javax.swing.JComboBox<String> cmbKereta;
    private javax.swing.JComboBox<String> cmbTujuan;
    private javax.swing.JLabel lblArah;
    private javax.swing.JLabel lblCari;
    private javax.swing.JLabel lblFormTitle;
    private javax.swing.JLabel lblIdJadwal;
    private javax.swing.JLabel lblKereta;
    private javax.swing.JLabel lblModeBadge;
    private javax.swing.JLabel lblRute;
    private javax.swing.JLabel lblStops;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlArahBox;
    private javax.swing.JPanel pnlAsalTujuan;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlFormHead;
    private javax.swing.JPanel pnlGridMeta;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlIdBox;
    private javax.swing.JPanel pnlKeretaBox;
    private javax.swing.JPanel pnlRuteBox;
    private javax.swing.JPanel pnlSearchBar;
    private javax.swing.JPanel pnlTableCard;
    private javax.swing.JPanel pnlTopFields;
    private javax.swing.JScrollPane scrJadwal;
    private javax.swing.JScrollPane scrStops;
    private javax.swing.JTable tblJadwal;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtIdJadwal;
    private javax.swing.JTextArea txtStops;
    // End of variables declaration//GEN-END:variables
}
