package tiketkereta.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import tiketkereta.model.OpsiJadwal;
import tiketkereta.model.Pelanggan;
import tiketkereta.model.Pemesanan;
import tiketkereta.model.Penumpang;
import tiketkereta.model.User;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.Tema;
import tiketkereta.util.ValidasiException;

public class PesanKursiPanel extends javax.swing.JPanel {

    private static class BarisInputPenumpang {
        JTextField txtNama;
        JComboBox<String> cmbJenisId;
        JTextField txtNoId;
    }

    private final MainFrame mainFrame;
    private final SeatMapPanel seatMapPanel;
    private final ArrayList<BarisInputPenumpang> inputPenumpangList;
    private final LinkedHashSet<String> kursiTerpilih;
    private ArrayList<Integer> daftarNomorGerbong;
    private Set<String> kursiTerisi;
    private OpsiJadwal opsiAktif;
    private int jumlahPenumpang;

    public PesanKursiPanel() {
        this(null);
    }

    public PesanKursiPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.seatMapPanel = new SeatMapPanel();
        this.inputPenumpangList = new ArrayList<BarisInputPenumpang>();
        this.kursiTerpilih = new LinkedHashSet<String>();
        this.daftarNomorGerbong = new ArrayList<Integer>();
        this.kursiTerisi = new LinkedHashSet<String>();
        this.jumlahPenumpang = 1;
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlHeader.setOpaque(false);
        pnlTitleBox.setOpaque(false);
        pnlMain.setOpaque(false);

        Tema.styleCard(pnlPenumpangCard, 18, 18);
        pnlPenumpangCard.setPreferredSize(Tema.dim(390, 480));
        pnlPenumpangTop.setOpaque(false);
        pnlPenumpangList.setBackground(Tema.SURFACE);
        pnlPenumpangList.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scrPenumpang.getVerticalScrollBar().setUnitIncrement(16);
        Tema.styleScrollPane(scrPenumpang);

        Tema.styleCard(pnlSeatCard, 16, 18);
        pnlSeatTop.setOpaque(false);
        pnlGerbongPick.setOpaque(false);
        pnlSeatHost.setOpaque(false);
        pnlSeatHost.add(seatMapPanel, BorderLayout.CENTER);

        Tema.styleCard(pnlBottomCard, 14, 20);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblInfoKereta);
        Tema.styleSubTitle(lblPenumpangTitle);
        Tema.styleBody(lblGerbong);
        lblKursiTerpilih.setFont(Tema.FONT_BODY_BOLD);
        lblKursiTerpilih.setForeground(Tema.PRIMARY);
        lblTotalBayar.setFont(Tema.FONT_SUBTITLE);
        lblTotalBayar.setForeground(Tema.PRIMARY);

        Tema.styleComboBox(cmbGerbong);
        Tema.styleSecondaryButton(btnKembali);
        Tema.styleSecondaryButton(btnIsiAkun);
        Tema.styleAccentButton(btnLanjutBayar);
    }

    private void setupEvents() {
        btnKembali.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.kembaliKePilihKereta();
                }
            }
        });

        btnIsiAkun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isiDariAkunLogin();
            }
        });

        cmbGerbong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                muatDenahGerbongTerpilih();
            }
        });

        seatMapPanel.setSeatSelectionListener(new SeatMapPanel.SeatSelectionListener() {
            @Override
            public void onSelectionChanged(Set<String> selectedSeats) {
                perbaruiLabelKursi();
            }
        });

        btnLanjutBayar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesBuatPesanan();
            }
        });
    }

    public void siapkanPemesanan(OpsiJadwal opsi, int jumlahPenumpang) {
        this.opsiAktif = opsi;
        this.jumlahPenumpang = jumlahPenumpang;
        this.kursiTerpilih.clear();

        lblInfoKereta.setText(opsi.getKereta().getTrainNumber() + " " + opsi.getKereta().getName()
                + " (" + opsi.getKelas() + ")  •  "
                + opsi.getStasiunAsal().getCode() + " (" + opsi.getJamBerangkat() + ") → "
                + opsi.getStasiunTujuan().getCode() + " (" + opsi.getJamTiba() + ")");

        long total = opsi.getTarifPerPenumpang() * jumlahPenumpang;
        lblTotalBayar.setText("Total Bayar (" + jumlahPenumpang + " penumpang): " + FormatUtil.formatRupiah(total));

        bangunFormPenumpang();
        isiDariAkunLogin();

        if (mainFrame != null) {
            kursiTerisi = mainFrame.getPemesananService().getKursiTerisi(opsi.getJadwal().getScheduleId());
        }
        daftarNomorGerbong = opsi.getKereta().getNomorGerbongUntukKelas(opsi.getKelas());
        String[] itemGerbong = new String[daftarNomorGerbong.size()];
        for (int i = 0; i < daftarNomorGerbong.size(); i++) {
            int g = daftarNomorGerbong.get(i);
            itemGerbong[i] = "Gerbong " + g + " (" + opsi.getKelas() + ")";
        }
        cmbGerbong.setModel(new DefaultComboBoxModel<String>(itemGerbong));
        if (itemGerbong.length > 0) {
            cmbGerbong.setSelectedIndex(0);
        }
        perbaruiLabelKursi();
        muatDenahGerbongTerpilih();
    }

    private void bangunFormPenumpang() {
        pnlPenumpangList.removeAll();
        inputPenumpangList.clear();

        for (int i = 1; i <= jumlahPenumpang; i++) {
            JPanel kartu = new JPanel(new GridLayout(0, 1, 0, 6));
            Tema.styleCard(kartu, 12, 14);
            kartu.setBackground(Tema.CANVAS);

            JLabel lblJudul = new JLabel("Penumpang " + i);
            Tema.styleSubTitle(lblJudul);
            kartu.add(lblJudul);

            JLabel lblNama = new JLabel("Nama Lengkap");
            Tema.styleMuted(lblNama);
            JTextField txtNama = new JTextField();
            Tema.styleTextField(txtNama, "Nama sesuai KTP / Paspor");
            kartu.add(lblNama);
            kartu.add(txtNama);

            JLabel lblJenis = new JLabel("Jenis Identitas");
            Tema.styleMuted(lblJenis);
            JComboBox<String> cmbJenis = new JComboBox<String>(new String[] { "KTP", "PASPOR" });
            Tema.styleComboBox(cmbJenis);
            kartu.add(lblJenis);
            kartu.add(cmbJenis);

            JLabel lblNo = new JLabel("Nomor Identitas");
            Tema.styleMuted(lblNo);
            JTextField txtNo = new JTextField();
            Tema.styleTextField(txtNo, "NIK atau Nomor Paspor");
            kartu.add(lblNo);
            kartu.add(txtNo);

            BarisInputPenumpang baris = new BarisInputPenumpang();
            baris.txtNama = txtNama;
            baris.cmbJenisId = cmbJenis;
            baris.txtNoId = txtNo;
            inputPenumpangList.add(baris);
            pnlPenumpangList.add(kartu);
        }

        pnlPenumpangList.revalidate();
        pnlPenumpangList.repaint();
    }

    private void isiDariAkunLogin() {
        if (mainFrame == null || inputPenumpangList.isEmpty()) {
            return;
        }
        User user = mainFrame.getAuthService().getCurrentUser();
        if (user instanceof Pelanggan) {
            Pelanggan p = (Pelanggan) user;
            BarisInputPenumpang pertama = inputPenumpangList.get(0);
            pertama.txtNama.setText(p.getFullName());
            pertama.cmbJenisId.setSelectedItem(p.getIdType());
            pertama.txtNoId.setText(p.getIdNumber());
        }
    }

    private void muatDenahGerbongTerpilih() {
        if (opsiAktif == null || daftarNomorGerbong.isEmpty()) {
            return;
        }
        int idx = cmbGerbong.getSelectedIndex();
        if (idx < 0 || idx >= daftarNomorGerbong.size()) {
            return;
        }
        int nomor = daftarNomorGerbong.get(idx);
        int kapasitas = opsiAktif.getKereta().getKapasitasGerbong(nomor);
        seatMapPanel.configure(nomor, opsiAktif.getKelas(), kapasitas, kursiTerisi, kursiTerpilih, jumlahPenumpang);
    }

    private void perbaruiLabelKursi() {
        if (kursiTerpilih.isEmpty()) {
            lblKursiTerpilih.setText("Kursi Dipilih: - (0/" + jumlahPenumpang + ")");
            return;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String kode : kursiTerpilih) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(kode);
            i++;
        }
        lblKursiTerpilih
                .setText("Kursi Dipilih: " + sb.toString() + " (" + kursiTerpilih.size() + "/" + jumlahPenumpang + ")");
    }

    private void prosesBuatPesanan() {
        if (mainFrame == null || opsiAktif == null) {
            return;
        }
        if (kursiTerpilih.size() != jumlahPenumpang) {
            JOptionPane.showMessageDialog(this,
                    "Pilih tepat " + jumlahPenumpang + " kursi pada denah kursi sebelum melanjutkan.",
                    "Kursi Belum Lengkap",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        ArrayList<String> kursiList = new ArrayList<String>(kursiTerpilih);
        ArrayList<Penumpang> penumpangList = new ArrayList<Penumpang>();
        for (int i = 0; i < inputPenumpangList.size(); i++) {
            BarisInputPenumpang b = inputPenumpangList.get(i);
            penumpangList.add(new Penumpang(
                    FormatUtil.rapi(b.txtNama.getText()),
                    String.valueOf(b.cmbJenisId.getSelectedItem()),
                    FormatUtil.rapi(b.txtNoId.getText()),
                    kursiList.get(i)));
        }

        try {
            Pemesanan pesanan = mainFrame.getPemesananService().buatPemesanan(
                    mainFrame.getAuthService().getCurrentUser(), opsiAktif, penumpangList);
            mainFrame.tampilkanPembayaran(pesanan);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal Memesan", JOptionPane.WARNING_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        pnlTitleBox = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblInfoKereta = new javax.swing.JLabel();
        btnKembali = new javax.swing.JButton();
        pnlMain = new javax.swing.JPanel();
        pnlPenumpangCard = new javax.swing.JPanel();
        pnlPenumpangTop = new javax.swing.JPanel();
        lblPenumpangTitle = new javax.swing.JLabel();
        btnIsiAkun = new javax.swing.JButton();
        scrPenumpang = new javax.swing.JScrollPane();
        pnlPenumpangList = new javax.swing.JPanel();
        pnlSeatCard = new javax.swing.JPanel();
        pnlSeatTop = new javax.swing.JPanel();
        pnlGerbongPick = new javax.swing.JPanel();
        lblGerbong = new javax.swing.JLabel();
        cmbGerbong = new javax.swing.JComboBox<>();
        lblKursiTerpilih = new javax.swing.JLabel();
        pnlSeatHost = new javax.swing.JPanel();
        pnlBottomCard = new javax.swing.JPanel();
        lblTotalBayar = new javax.swing.JLabel();
        btnLanjutBayar = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(0, 14));

        pnlHeader.setLayout(new java.awt.BorderLayout(12, 0));

        pnlTitleBox.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Input Penumpang & Pilih Kursi");
        pnlTitleBox.add(lblTitle);

        lblInfoKereta.setText("Detail perjalanan");
        pnlTitleBox.add(lblInfoKereta);

        pnlHeader.add(pnlTitleBox, java.awt.BorderLayout.CENTER);

        btnKembali.setText("Kembali");
        pnlHeader.add(btnKembali, java.awt.BorderLayout.EAST);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlMain.setLayout(new java.awt.BorderLayout(16, 0));

        pnlPenumpangCard.setLayout(new java.awt.BorderLayout(0, 10));

        pnlPenumpangTop.setLayout(new java.awt.BorderLayout());

        lblPenumpangTitle.setText("Data Penumpang");
        pnlPenumpangTop.add(lblPenumpangTitle, java.awt.BorderLayout.WEST);

        btnIsiAkun.setText("Isi dari Akun");
        pnlPenumpangTop.add(btnIsiAkun, java.awt.BorderLayout.EAST);

        pnlPenumpangCard.add(pnlPenumpangTop, java.awt.BorderLayout.NORTH);

        pnlPenumpangList.setLayout(new java.awt.GridLayout(0, 1, 0, 12));
        scrPenumpang.setViewportView(pnlPenumpangList);

        pnlPenumpangCard.add(scrPenumpang, java.awt.BorderLayout.CENTER);

        pnlMain.add(pnlPenumpangCard, java.awt.BorderLayout.WEST);

        pnlSeatCard.setLayout(new java.awt.BorderLayout(0, 12));

        pnlSeatTop.setLayout(new java.awt.BorderLayout(12, 0));

        pnlGerbongPick.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));

        lblGerbong.setText("Gerbong:");
        pnlGerbongPick.add(lblGerbong);

        pnlGerbongPick.add(cmbGerbong);

        pnlSeatTop.add(pnlGerbongPick, java.awt.BorderLayout.WEST);

        lblKursiTerpilih.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKursiTerpilih.setText("Kursi Dipilih: -");
        pnlSeatTop.add(lblKursiTerpilih, java.awt.BorderLayout.CENTER);

        pnlSeatCard.add(pnlSeatTop, java.awt.BorderLayout.NORTH);

        pnlSeatHost.setLayout(new java.awt.BorderLayout());
        pnlSeatCard.add(pnlSeatHost, java.awt.BorderLayout.CENTER);

        pnlMain.add(pnlSeatCard, java.awt.BorderLayout.CENTER);

        add(pnlMain, java.awt.BorderLayout.CENTER);

        pnlBottomCard.setLayout(new java.awt.BorderLayout());

        lblTotalBayar.setText("Total Bayar: Rp 0");
        pnlBottomCard.add(lblTotalBayar, java.awt.BorderLayout.WEST);

        btnLanjutBayar.setText("Lanjut ke Pembayaran");
        pnlBottomCard.add(btnLanjutBayar, java.awt.BorderLayout.EAST);

        add(pnlBottomCard, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIsiAkun;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnLanjutBayar;
    private javax.swing.JComboBox<String> cmbGerbong;
    private javax.swing.JLabel lblGerbong;
    private javax.swing.JLabel lblInfoKereta;
    private javax.swing.JLabel lblKursiTerpilih;
    private javax.swing.JLabel lblPenumpangTitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotalBayar;
    private javax.swing.JPanel pnlBottomCard;
    private javax.swing.JPanel pnlGerbongPick;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlPenumpangCard;
    private javax.swing.JPanel pnlPenumpangList;
    private javax.swing.JPanel pnlPenumpangTop;
    private javax.swing.JPanel pnlSeatCard;
    private javax.swing.JPanel pnlSeatHost;
    private javax.swing.JPanel pnlSeatTop;
    private javax.swing.JPanel pnlTitleBox;
    private javax.swing.JScrollPane scrPenumpang;
    // End of variables declaration//GEN-END:variables
}
