package tiketkereta.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import tiketkereta.model.OpsiJadwal;
import tiketkereta.model.Stasiun;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.Tema;

public class PilihKeretaPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;
    private ArrayList<OpsiJadwal> daftarOpsi;
    private int jumlahPenumpang;
    private DefaultTableModel tableModel;

    public PilihKeretaPanel() {
        this(null);
    }

    public PilihKeretaPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.daftarOpsi = new ArrayList<OpsiJadwal>();
        this.jumlahPenumpang = 1;
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlHeader.setOpaque(false);
        pnlTitleBox.setOpaque(false);
        Tema.styleCard(pnlTableCard, 16, 18);
        pnlFooter.setOpaque(false);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblRuteInfo);
        Tema.styleMuted(lblJumlahHasil);

        Tema.styleSecondaryButton(btnKembali);
        Tema.styleAccentButton(btnPilih);

        tableModel = new DefaultTableModel(
                new Object[]{"Kode & Nama Kereta", "Jenis", "Kelas", "Berangkat", "Tiba", "Sisa Kursi", "Tarif / Org"}, 0) {
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
        btnKembali.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.tampilkanCariTiket();
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
        this.daftarOpsi = hasil == null ? new ArrayList<OpsiJadwal>() : hasil;
        lblRuteInfo.setText(asal.getLabelSingkat() + "  →  " + tujuan.getLabelSingkat()
                + "   •   " + jumlahPenumpang + " Penumpang");

        tableModel.setRowCount(0);
        for (OpsiJadwal opsi : daftarOpsi) {
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

        if (daftarOpsi.isEmpty()) {
            lblJumlahHasil.setText("Tidak ada kereta langsung pada rute ini. Coba stasiun lain.");
        } else {
            lblJumlahHasil.setText(daftarOpsi.size() + " opsi kereta tersedia. Klik baris lalu tekan Lanjut Pilih Kursi.");
            tblJadwal.setRowSelectionInterval(0, 0);
        }
    }

    private void lanjutKeKursi() {
        if (mainFrame == null) {
            return;
        }
        int row = tblJadwal.getSelectedRow();
        if (row < 0 || row >= daftarOpsi.size()) {
            JOptionPane.showMessageDialog(this, "Silakan pilih salah satu jadwal kereta terlebih dahulu.",
                    "Belum Ada Pilihan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        OpsiJadwal dipilih = daftarOpsi.get(row);
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

        pnlHeader = new javax.swing.JPanel();
        pnlTitleBox = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblRuteInfo = new javax.swing.JLabel();
        btnKembali = new javax.swing.JButton();
        pnlTableCard = new javax.swing.JPanel();
        scrJadwal = new javax.swing.JScrollPane();
        tblJadwal = new javax.swing.JTable();
        pnlFooter = new javax.swing.JPanel();
        lblJumlahHasil = new javax.swing.JLabel();
        btnPilih = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setLayout(new java.awt.BorderLayout(12, 0));

        pnlTitleBox.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Pilih Jadwal Kereta");
        pnlTitleBox.add(lblTitle);

        lblRuteInfo.setText("Menampilkan jadwal tersedia");
        pnlTitleBox.add(lblRuteInfo);

        pnlHeader.add(pnlTitleBox, java.awt.BorderLayout.CENTER);

        btnKembali.setText("Ubah Pencarian");
        pnlHeader.add(btnKembali, java.awt.BorderLayout.EAST);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlTableCard.setLayout(new java.awt.BorderLayout(0, 12));

        scrJadwal.setViewportView(tblJadwal);

        pnlTableCard.add(scrJadwal, java.awt.BorderLayout.CENTER);

        pnlFooter.setLayout(new java.awt.BorderLayout());

        lblJumlahHasil.setText("0 opsi ditemukan");
        pnlFooter.add(lblJumlahHasil, java.awt.BorderLayout.WEST);

        btnPilih.setText("Lanjut Pilih Kursi");
        pnlFooter.add(btnPilih, java.awt.BorderLayout.EAST);

        pnlTableCard.add(pnlFooter, java.awt.BorderLayout.SOUTH);

        add(pnlTableCard, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnPilih;
    private javax.swing.JLabel lblJumlahHasil;
    private javax.swing.JLabel lblRuteInfo;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlTableCard;
    private javax.swing.JPanel pnlTitleBox;
    private javax.swing.JScrollPane scrJadwal;
    private javax.swing.JTable tblJadwal;
    // End of variables declaration//GEN-END:variables
}
