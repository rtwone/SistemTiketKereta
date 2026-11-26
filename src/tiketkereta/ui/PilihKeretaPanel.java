package tiketkereta.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import tiketkereta.model.OpsiJadwal;
import tiketkereta.model.Stasiun;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.Tema;

public class PilihKeretaPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;
    private ArrayList<OpsiJadwal> semuaOpsi;
    private ArrayList<OpsiJadwal> opsiTampil;
    private int jumlahPenumpang;
    private DefaultTableModel tableModel;

    public PilihKeretaPanel() {
        this(null);
    }

    public PilihKeretaPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.semuaOpsi = new ArrayList<OpsiJadwal>();
        this.opsiTampil = new ArrayList<OpsiJadwal>();
        this.jumlahPenumpang = 1;
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlTopArea.setOpaque(false);
        pnlStepper.setOpaque(false);
        Tema.styleStepItem(lblStep1, false, true);
        Tema.styleStepItem(lblStep2, true, false);
        Tema.styleStepItem(lblStep3, false, false);
        Tema.styleStepItem(lblStep4, false, false);

        pnlHeader.setOpaque(false);
        pnlTitleBox.setOpaque(false);
        Tema.styleCard(pnlTableCard, 18, 20);
        pnlToolbar.setOpaque(false);
        pnlFilterWrap.setOpaque(false);
        Tema.styleAccentCard(pnlFooter, 12, 16);

        Tema.stylePageTitle(lblTitle);
        lblRuteInfo.setFont(Tema.FONT_BODY_BOLD);
        lblRuteInfo.setForeground(Tema.TEXT_MUTED);
        Tema.styleMuted(lblJumlahHasil);
        Tema.styleBody(lblFilterKelas);
        lblPreviewPilihan.setFont(Tema.FONT_BODY_BOLD);
        lblPreviewPilihan.setForeground(Tema.PRIMARY);

        cmbFilterKelas.setModel(new DefaultComboBoxModel<String>(
                new String[]{"Semua Kelas", "Eksekutif", "Bisnis", "Ekonomi Premium", "Ekonomi"}));
        Tema.styleComboBox(cmbFilterKelas);

        Tema.styleSecondaryButton(btnKembali);
        Tema.styleAccentButton(btnPilih);

        tableModel = new DefaultTableModel(
                new Object[]{"Kode & Nama Kereta", "Jenis Layanan", "Kelas", "Berangkat", "Tiba", "Sisa Kursi", "Tarif / Org"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblJadwal.setModel(tableModel);
        tblJadwal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Tema.styleTable(tblJadwal);
        tblJadwal.getColumnModel().getColumn(0).setPreferredWidth(Tema.s(250));
        Tema.styleScrollPane(scrJadwal);
    }

    private void setupEvents() {
        btnKembali.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.tampilkanCariTiket();
                }
            }
        });

        cmbFilterKelas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                terapkanFilterTabel();
            }
        });

        tblJadwal.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    perbaruiPreviewPilihan();
                }
            }
        });

        btnPilih.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lanjutKeKursi();
            }
        });

        tblJadwal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    lanjutKeKursi();
                }
            }
        });
    }

    public void tampilkanHasil(Stasiun asal, Stasiun tujuan, int jumlahPenumpang, ArrayList<OpsiJadwal> hasil) {
        this.jumlahPenumpang = jumlahPenumpang;
        this.semuaOpsi = hasil == null ? new ArrayList<OpsiJadwal>() : hasil;
        lblRuteInfo.setText(asal.getLabelSingkat() + "  →  " + tujuan.getLabelSingkat()
                + "   •   " + jumlahPenumpang + " Penumpang");
        cmbFilterKelas.setSelectedIndex(0);
        terapkanFilterTabel();
    }

    private void terapkanFilterTabel() {
        String filter = String.valueOf(cmbFilterKelas.getSelectedItem());
        opsiTampil = new ArrayList<OpsiJadwal>();
        tableModel.setRowCount(0);

        for (OpsiJadwal opsi : semuaOpsi) {
            if (!"Semua Kelas".equalsIgnoreCase(filter) && !opsi.getKelas().equalsIgnoreCase(filter)) {
                continue;
            }
            opsiTampil.add(opsi);
            tableModel.addRow(new Object[]{
                opsi.getKereta().getTrainNumber() + " - " + opsi.getKereta().getName(),
                opsi.getKereta().getJenisLayanan(),
                opsi.getKelas(),
                opsi.getJamBerangkat(),
                opsi.getJamTiba(),
                opsi.getSisaKursi() + " kursi",
                FormatUtil.formatRupiah(opsi.getTarifPerPenumpang())
            });
        }

        if (opsiTampil.isEmpty()) {
            lblJumlahHasil.setText("Tidak ada jadwal pada pilihan kelas/rute ini.");
            lblPreviewPilihan.setText("Silakan ubah filter kelas atau pilih rute stasiun lain.");
            btnPilih.setEnabled(false);
        } else {
            lblJumlahHasil.setText("Menampilkan " + opsiTampil.size() + " opsi keberangkatan (klik 2x baris untuk memilih cepat)");
            btnPilih.setEnabled(true);
            tblJadwal.setRowSelectionInterval(0, 0);
            perbaruiPreviewPilihan();
        }
    }

    private void perbaruiPreviewPilihan() {
        int row = tblJadwal.getSelectedRow();
        if (row < 0 || row >= opsiTampil.size()) {
            lblPreviewPilihan.setText("Pilih salah satu jadwal kereta pada tabel di atas");
            return;
        }
        OpsiJadwal opsi = opsiTampil.get(row);
        long total = opsi.getTarifPerPenumpang() * jumlahPenumpang;
        lblPreviewPilihan.setText("Terpilih: " + opsi.getKereta().getName() + " (" + opsi.getKelas()
                + ", " + opsi.getJamBerangkat() + " - " + opsi.getJamTiba()
                + ")   •   Estimasi Total " + jumlahPenumpang + " Org: " + FormatUtil.formatRupiah(total));
    }

    private void lanjutKeKursi() {
        if (mainFrame == null) {
            return;
        }
        int row = tblJadwal.getSelectedRow();
        if (row < 0 || row >= opsiTampil.size()) {
            JOptionPane.showMessageDialog(this, "Silakan pilih salah satu jadwal kereta terlebih dahulu.",
                    "Belum Ada Pilihan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        OpsiJadwal dipilih = opsiTampil.get(row);
        if (dipilih.getSisaKursi() < jumlahPenumpang) {
            JOptionPane.showMessageDialog(this,
                    "Sisa kursi tidak mencukupi untuk " + jumlahPenumpang + " penumpang.",
                    "Kursi Penuh", JOptionPane.WARNING_MESSAGE);
            return;
        }
        mainFrame.tampilkanPesanKursi(dipilih, jumlahPenumpang);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTopArea = new javax.swing.JPanel();
        pnlStepper = new javax.swing.JPanel();
        lblStep1 = new javax.swing.JLabel();
        lblStep2 = new javax.swing.JLabel();
        lblStep3 = new javax.swing.JLabel();
        lblStep4 = new javax.swing.JLabel();
        pnlHeader = new javax.swing.JPanel();
        pnlTitleBox = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblRuteInfo = new javax.swing.JLabel();
        btnKembali = new javax.swing.JButton();
        pnlTableCard = new javax.swing.JPanel();
        pnlToolbar = new javax.swing.JPanel();
        lblJumlahHasil = new javax.swing.JLabel();
        pnlFilterWrap = new javax.swing.JPanel();
        lblFilterKelas = new javax.swing.JLabel();
        cmbFilterKelas = new javax.swing.JComboBox<>();
        scrJadwal = new javax.swing.JScrollPane();
        tblJadwal = new javax.swing.JTable();
        pnlFooter = new javax.swing.JPanel();
        lblPreviewPilihan = new javax.swing.JLabel();
        btnPilih = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(0, 14));

        pnlTopArea.setLayout(new java.awt.BorderLayout(0, 12));

        pnlStepper.setLayout(new java.awt.GridLayout(1, 4, 10, 0));

        lblStep1.setText("•  1. Cari Rute (Selesai)");
        pnlStepper.add(lblStep1);

        lblStep2.setText("2. Pilih Kereta");
        pnlStepper.add(lblStep2);

        lblStep3.setText("3. Penumpang & Kursi");
        pnlStepper.add(lblStep3);

        lblStep4.setText("4. Bayar & E-Tiket");
        pnlStepper.add(lblStep4);

        pnlTopArea.add(pnlStepper, java.awt.BorderLayout.NORTH);

        pnlHeader.setLayout(new java.awt.BorderLayout(12, 0));

        pnlTitleBox.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Pilih Jadwal & Kelas Kereta");
        pnlTitleBox.add(lblTitle);

        lblRuteInfo.setText("Menampilkan jadwal tersedia");
        pnlTitleBox.add(lblRuteInfo);

        pnlHeader.add(pnlTitleBox, java.awt.BorderLayout.CENTER);

        btnKembali.setText("←  Ubah Pencarian");
        pnlHeader.add(btnKembali, java.awt.BorderLayout.EAST);

        pnlTopArea.add(pnlHeader, java.awt.BorderLayout.CENTER);

        add(pnlTopArea, java.awt.BorderLayout.NORTH);

        pnlTableCard.setLayout(new java.awt.BorderLayout(0, 12));

        pnlToolbar.setLayout(new java.awt.BorderLayout());

        lblJumlahHasil.setText("0 opsi ditemukan");
        pnlToolbar.add(lblJumlahHasil, java.awt.BorderLayout.WEST);

        pnlFilterWrap.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));

        lblFilterKelas.setText("Filter Kelas:");
        pnlFilterWrap.add(lblFilterKelas);
        pnlFilterWrap.add(cmbFilterKelas);

        pnlToolbar.add(pnlFilterWrap, java.awt.BorderLayout.EAST);

        pnlTableCard.add(pnlToolbar, java.awt.BorderLayout.NORTH);

        scrJadwal.setViewportView(tblJadwal);

        pnlTableCard.add(scrJadwal, java.awt.BorderLayout.CENTER);

        pnlFooter.setLayout(new java.awt.BorderLayout(16, 0));

        lblPreviewPilihan.setText("Pilih salah satu jadwal kereta pada tabel di atas");
        pnlFooter.add(lblPreviewPilihan, java.awt.BorderLayout.CENTER);

        btnPilih.setText("Lanjut Pilih Kursi  →");
        pnlFooter.add(btnPilih, java.awt.BorderLayout.EAST);

        pnlTableCard.add(pnlFooter, java.awt.BorderLayout.SOUTH);

        add(pnlTableCard, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnPilih;
    private javax.swing.JComboBox<String> cmbFilterKelas;
    private javax.swing.JLabel lblFilterKelas;
    private javax.swing.JLabel lblJumlahHasil;
    private javax.swing.JLabel lblPreviewPilihan;
    private javax.swing.JLabel lblRuteInfo;
    private javax.swing.JLabel lblStep1;
    private javax.swing.JLabel lblStep2;
    private javax.swing.JLabel lblStep3;
    private javax.swing.JLabel lblStep4;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlFilterWrap;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlStepper;
    private javax.swing.JPanel pnlTableCard;
    private javax.swing.JPanel pnlTitleBox;
    private javax.swing.JPanel pnlToolbar;
    private javax.swing.JPanel pnlTopArea;
    private javax.swing.JScrollPane scrJadwal;
    private javax.swing.JTable tblJadwal;
    // End of variables declaration//GEN-END:variables
}
