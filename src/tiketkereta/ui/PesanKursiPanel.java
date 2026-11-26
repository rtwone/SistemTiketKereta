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
        JLabel lblSeatBadge;
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
        pnlTopArea.setOpaque(false);
        pnlStepper.setOpaque(false);
        Tema.styleStepItem(lblStep1, false, true);
        Tema.styleStepItem(lblStep2, false, true);
        Tema.styleStepItem(lblStep3, true, false);
        Tema.styleStepItem(lblStep4, false, false);

        pnlHeader.setOpaque(false);
        pnlTitleBox.setOpaque(false);
        pnlMain.setOpaque(false);

        Tema.styleCard(pnlPenumpangCard, 18, 18);
        pnlPenumpangCard.setPreferredSize(Tema.dim(400, 480));
        pnlPenumpangTop.setOpaque(false);
        pnlPenumpangList.setBackground(Tema.SURFACE);
        pnlPenumpangList.setBorder(BorderFactory.createEmptyBorder(Tema.s(6), Tema.s(6), Tema.s(6), Tema.s(6)));
        scrPenumpang.getVerticalScrollBar().setUnitIncrement(16);
        Tema.styleScrollPane(scrPenumpang);

        Tema.styleCard(pnlSeatCard, 18, 18);
        pnlSeatTop.setOpaque(false);
        pnlGerbongPick.setOpaque(false);
        pnlSeatHost.setOpaque(false);
        pnlSeatHost.removeAll();
        pnlSeatHost.add(seatMapPanel, BorderLayout.CENTER);

        Tema.styleAccentCard(pnlBottomCard, 14, 20);

        Tema.stylePageTitle(lblTitle);
        lblInfoKereta.setFont(Tema.FONT_BODY_BOLD);
        lblInfoKereta.setForeground(Tema.TEXT_MUTED);
        Tema.styleSubTitle(lblPenumpangTitle);
        Tema.styleFieldLabel(lblGerbong);
        lblKursiTerpilih.setFont(Tema.FONT_BODY_BOLD);
        lblKursiTerpilih.setForeground(Tema.PRIMARY);
        lblTotalBayar.setFont(Tema.FONT_SUBTITLE);
        lblTotalBayar.setForeground(Tema.PRIMARY);

        Tema.styleComboBox(cmbGerbong);
        cmbGerbong.setPreferredSize(Tema.dim(200, 40));
        Tema.styleSecondaryButton(btnKembali);
        Tema.styleSecondaryButton(btnIsiAkun);
        Tema.stylePrimaryButton(btnPilihOtomatis);
        Tema.styleSecondaryButton(btnResetKursi);
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

        btnPilihOtomatis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean lengkap = seatMapPanel.pilihKursiOtomatis();
                if (!lengkap) {
                    JOptionPane.showMessageDialog(PesanKursiPanel.this,
                            "Kursi kosong di gerbong ini tidak cukup untuk " + jumlahPenumpang + " penumpang. Silakan cek gerbong lain.",
                            "Info Gerbong", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        btnResetKursi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seatMapPanel.resetPilihanKursi();
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
                + " (" + opsi.getKelas() + ")   •   "
                + opsi.getStasiunAsal().getLabelSingkat() + " (" + opsi.getJamBerangkat() + ") → "
                + opsi.getStasiunTujuan().getLabelSingkat() + " (" + opsi.getJamTiba() + ")");

        long total = opsi.getTarifPerPenumpang() * jumlahPenumpang;
        lblTotalBayar.setText("Total Tagihan (" + jumlahPenumpang + " × "
                + FormatUtil.formatRupiah(opsi.getTarifPerPenumpang()) + "):  " + FormatUtil.formatRupiah(total));

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
            JPanel kartu = new JPanel(new BorderLayout(0, Tema.s(8)));
            Tema.styleSoftCard(kartu, 12, 14);

            JPanel head = new JPanel(new BorderLayout());
            head.setOpaque(false);
            JLabel lblJudul = new JLabel("Penumpang " + i);
            Tema.styleSubTitle(lblJudul);
            JLabel lblBadge = new JLabel("Kursi: Belum dipilih");
            Tema.styleBadge(lblBadge, Tema.PRIMARY_SOFT, Tema.PRIMARY);
            head.add(lblJudul, BorderLayout.WEST);
            head.add(lblBadge, BorderLayout.EAST);
            kartu.add(head, BorderLayout.NORTH);

            JPanel fields = new JPanel(new GridLayout(3, 1, 0, Tema.s(8)));
            fields.setOpaque(false);

            JPanel boxNama = new JPanel(new BorderLayout(0, Tema.s(4)));
            boxNama.setOpaque(false);
            JLabel lblNama = new JLabel("Nama Lengkap");
            Tema.styleMuted(lblNama);
            JTextField txtNama = new JTextField();
            Tema.styleTextField(txtNama, "Nama sesuai KTP / Paspor");
            boxNama.add(lblNama, BorderLayout.NORTH);
            boxNama.add(txtNama, BorderLayout.CENTER);
            fields.add(boxNama);

            JPanel boxJenis = new JPanel(new BorderLayout(0, Tema.s(4)));
            boxJenis.setOpaque(false);
            JLabel lblJenis = new JLabel("Jenis Identitas");
            Tema.styleMuted(lblJenis);
            JComboBox<String> cmbJenis = new JComboBox<String>(new String[]{"KTP", "PASPOR"});
            Tema.styleComboBox(cmbJenis);
            boxJenis.add(lblJenis, BorderLayout.NORTH);
            boxJenis.add(cmbJenis, BorderLayout.CENTER);
            fields.add(boxJenis);

            JPanel boxNo = new JPanel(new BorderLayout(0, Tema.s(4)));
            boxNo.setOpaque(false);
            JLabel lblNo = new JLabel("Nomor Identitas");
            Tema.styleMuted(lblNo);
            JTextField txtNo = new JTextField();
            Tema.styleTextField(txtNo, "NIK atau Nomor Paspor");
            boxNo.add(lblNo, BorderLayout.NORTH);
            boxNo.add(txtNo, BorderLayout.CENTER);
            fields.add(boxNo);

            kartu.add(fields, BorderLayout.CENTER);

            BarisInputPenumpang baris = new BarisInputPenumpang();
            baris.lblSeatBadge = lblBadge;
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
        ArrayList<String> urut = new ArrayList<String>(kursiTerpilih);
        for (int i = 0; i < inputPenumpangList.size(); i++) {
            JLabel badge = inputPenumpangList.get(i).lblSeatBadge;
            if (i < urut.size()) {
                badge.setText("Kursi: " + urut.get(i));
                Tema.styleBadge(badge, Tema.SUCCESS_SOFT, Tema.STATUS_GREEN);
            } else {
                badge.setText("Kursi: Belum dipilih");
                Tema.styleBadge(badge, Tema.PRIMARY_SOFT, Tema.PRIMARY);
            }
        }
        if (kursiTerpilih.isEmpty()) {
            lblKursiTerpilih.setText("Kursi Terpilih: 0/" + jumlahPenumpang);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < urut.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(urut.get(i));
        }
        lblKursiTerpilih.setText("Terpilih (" + urut.size() + "/" + jumlahPenumpang + "): " + sb.toString());
    }

    private void prosesBuatPesanan() {
        if (mainFrame == null || opsiAktif == null) {
            return;
        }
        if (kursiTerpilih.size() != jumlahPenumpang) {
            JOptionPane.showMessageDialog(this,
                    "Pilih tepat " + jumlahPenumpang + " kursi pada denah kursi (atau klik tombol Pilih Kursi Otomatis).",
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
                    kursiList.get(i)
            ));
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
        lblInfoKereta = new javax.swing.JLabel();
        btnKembali = new javax.swing.JButton();
        pnlMain = new javax.swing.JPanel();
        pnlPenumpangCard = new javax.swing.JPanel();
        pnlPenumpangTop = new javax.swing.JPanel();
        lblPenumpangTitle = new javax.swing.JLabel();
        btnIsiAkun = new javax.swing.JButton();
        scrPenumpang = new javax.swing.JScrollPane();
        pnlPenumpangList = new javax.swing.JPanel();
        lblPenumpangPreview = new javax.swing.JLabel();
        pnlSeatCard = new javax.swing.JPanel();
        pnlSeatTop = new javax.swing.JPanel();
        pnlGerbongPick = new javax.swing.JPanel();
        lblGerbong = new javax.swing.JLabel();
        cmbGerbong = new javax.swing.JComboBox<>();
        btnPilihOtomatis = new javax.swing.JButton();
        btnResetKursi = new javax.swing.JButton();
        lblKursiTerpilih = new javax.swing.JLabel();
        pnlSeatHost = new javax.swing.JPanel();
        lblSeatPreview = new javax.swing.JLabel();
        pnlBottomCard = new javax.swing.JPanel();
        lblTotalBayar = new javax.swing.JLabel();
        btnLanjutBayar = new javax.swing.JButton();

        setBackground(new java.awt.Color(245, 247, 250));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 26, 22, 26));
        setPreferredSize(new java.awt.Dimension(1120, 680));
        setLayout(new java.awt.BorderLayout(0, 14));

        pnlTopArea.setOpaque(false);
        pnlTopArea.setLayout(new java.awt.BorderLayout(0, 12));

        pnlStepper.setOpaque(false);
        pnlStepper.setLayout(new java.awt.GridLayout(1, 4, 10, 0));

        lblStep1.setText("•  1. Cari Rute (Selesai)");
        lblStep1.setBackground(new java.awt.Color(220, 252, 231));
        lblStep1.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        lblStep1.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblStep1.setForeground(new java.awt.Color(22, 163, 74));
        lblStep1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStep1.setOpaque(true);
        pnlStepper.add(lblStep1);

        lblStep2.setText("•  2. Pilih Kereta (Selesai)");
        lblStep2.setBackground(new java.awt.Color(220, 252, 231));
        lblStep2.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        lblStep2.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblStep2.setForeground(new java.awt.Color(22, 163, 74));
        lblStep2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStep2.setOpaque(true);
        pnlStepper.add(lblStep2);

        lblStep3.setText("3. Penumpang & Kursi");
        lblStep3.setBackground(new java.awt.Color(243, 112, 33));
        lblStep3.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        lblStep3.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblStep3.setForeground(new java.awt.Color(255, 255, 255));
        lblStep3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStep3.setOpaque(true);
        pnlStepper.add(lblStep3);

        lblStep4.setText("4. Bayar & E-Tiket");
        lblStep4.setBackground(new java.awt.Color(248, 250, 252));
        lblStep4.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        lblStep4.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblStep4.setForeground(new java.awt.Color(100, 116, 139));
        lblStep4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStep4.setOpaque(true);
        pnlStepper.add(lblStep4);
        pnlTopArea.add(pnlStepper, java.awt.BorderLayout.NORTH);

        pnlHeader.setOpaque(false);
        pnlHeader.setLayout(new java.awt.BorderLayout(12, 0));

        pnlTitleBox.setOpaque(false);
        pnlTitleBox.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Data Penumpang & Pilih Kursi Gerbong");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 20));
        lblTitle.setForeground(new java.awt.Color(27, 54, 93));
        pnlTitleBox.add(lblTitle);

        lblInfoKereta.setText("Detail perjalanan");
        lblInfoKereta.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblInfoKereta.setForeground(new java.awt.Color(100, 116, 139));
        pnlTitleBox.add(lblInfoKereta);
        pnlHeader.add(pnlTitleBox, java.awt.BorderLayout.CENTER);

        btnKembali.setText("←  Kembali ke Jadwal");
        btnKembali.setBackground(new java.awt.Color(255, 255, 255));
        btnKembali.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnKembali.setForeground(new java.awt.Color(15, 23, 42));
        btnKembali.setFocusPainted(false);
        pnlHeader.add(btnKembali, java.awt.BorderLayout.EAST);
        pnlTopArea.add(pnlHeader, java.awt.BorderLayout.CENTER);
        add(pnlTopArea, java.awt.BorderLayout.NORTH);

        pnlMain.setOpaque(false);
        pnlMain.setLayout(new java.awt.BorderLayout(16, 0));

        pnlPenumpangCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlPenumpangCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        pnlPenumpangCard.setPreferredSize(new java.awt.Dimension(400, 480));
        pnlPenumpangCard.setLayout(new java.awt.BorderLayout(0, 10));

        pnlPenumpangTop.setOpaque(false);
        pnlPenumpangTop.setLayout(new java.awt.BorderLayout());

        lblPenumpangTitle.setText("Identitas Penumpang");
        lblPenumpangTitle.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblPenumpangTitle.setForeground(new java.awt.Color(15, 23, 42));
        pnlPenumpangTop.add(lblPenumpangTitle, java.awt.BorderLayout.WEST);

        btnIsiAkun.setText("Isi dari Profil");
        btnIsiAkun.setBackground(new java.awt.Color(255, 255, 255));
        btnIsiAkun.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnIsiAkun.setForeground(new java.awt.Color(15, 23, 42));
        btnIsiAkun.setFocusPainted(false);
        pnlPenumpangTop.add(btnIsiAkun, java.awt.BorderLayout.EAST);
        pnlPenumpangCard.add(pnlPenumpangTop, java.awt.BorderLayout.NORTH);

        scrPenumpang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        pnlPenumpangList.setBackground(new java.awt.Color(255, 255, 255));
        pnlPenumpangList.setLayout(new java.awt.GridLayout(0, 1, 0, 12));

        lblPenumpangPreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPenumpangPreview.setText("Form Penumpang 1 - 4 (Otomatis sesuai jumlah penumpang)");
        lblPenumpangPreview.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblPenumpangPreview.setForeground(new java.awt.Color(100, 116, 139));
        pnlPenumpangList.add(lblPenumpangPreview);
        scrPenumpang.setViewportView(pnlPenumpangList);
        pnlPenumpangCard.add(scrPenumpang, java.awt.BorderLayout.CENTER);
        pnlMain.add(pnlPenumpangCard, java.awt.BorderLayout.WEST);

        pnlSeatCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlSeatCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        pnlSeatCard.setLayout(new java.awt.BorderLayout(0, 12));

        pnlSeatTop.setOpaque(false);
        pnlSeatTop.setLayout(new java.awt.BorderLayout(12, 0));

        pnlGerbongPick.setOpaque(false);
        pnlGerbongPick.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));

        lblGerbong.setText("Gerbong:");
        lblGerbong.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblGerbong.setForeground(new java.awt.Color(15, 23, 42));
        pnlGerbongPick.add(lblGerbong);

        cmbGerbong.setFont(new java.awt.Font("Segoe UI", 0, 13));
        cmbGerbong.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gerbong 1 - Eksekutif (50 Kursi)", "Gerbong 2 - Bisnis (64 Kursi)" }));
        cmbGerbong.setPreferredSize(new java.awt.Dimension(200, 40));
        pnlGerbongPick.add(cmbGerbong);

        btnPilihOtomatis.setText("Pilih Kursi Otomatis");
        btnPilihOtomatis.setBackground(new java.awt.Color(27, 54, 93));
        btnPilihOtomatis.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnPilihOtomatis.setForeground(new java.awt.Color(255, 255, 255));
        btnPilihOtomatis.setFocusPainted(false);
        pnlGerbongPick.add(btnPilihOtomatis);

        btnResetKursi.setText("Reset");
        btnResetKursi.setBackground(new java.awt.Color(255, 255, 255));
        btnResetKursi.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnResetKursi.setForeground(new java.awt.Color(15, 23, 42));
        btnResetKursi.setFocusPainted(false);
        pnlGerbongPick.add(btnResetKursi);
        pnlSeatTop.add(pnlGerbongPick, java.awt.BorderLayout.WEST);

        lblKursiTerpilih.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKursiTerpilih.setText("Kursi Dipilih: -");
        lblKursiTerpilih.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblKursiTerpilih.setForeground(new java.awt.Color(27, 54, 93));
        pnlSeatTop.add(lblKursiTerpilih, java.awt.BorderLayout.CENTER);
        pnlSeatCard.add(pnlSeatTop, java.awt.BorderLayout.NORTH);

        pnlSeatHost.setOpaque(false);
        pnlSeatHost.setLayout(new java.awt.BorderLayout());

        lblSeatPreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSeatPreview.setText("Area Denah Kursi Gerbong Interaktif (SeatMapPanel: Hijau = Kosong, Biru = Dipilih, Abu-abu = Terisi)");
        lblSeatPreview.setBackground(new java.awt.Color(248, 250, 252));
        lblSeatPreview.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(24, 24, 24, 24)));
        lblSeatPreview.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblSeatPreview.setForeground(new java.awt.Color(27, 54, 93));
        lblSeatPreview.setOpaque(true);
        pnlSeatHost.add(lblSeatPreview, java.awt.BorderLayout.CENTER);
        pnlSeatCard.add(pnlSeatHost, java.awt.BorderLayout.CENTER);
        pnlMain.add(pnlSeatCard, java.awt.BorderLayout.CENTER);
        add(pnlMain, java.awt.BorderLayout.CENTER);

        pnlBottomCard.setBackground(new java.awt.Color(255, 247, 237));
        pnlBottomCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(254, 215, 170)), javax.swing.BorderFactory.createEmptyBorder(14, 20, 14, 20)));
        pnlBottomCard.setLayout(new java.awt.BorderLayout());

        lblTotalBayar.setText("Total Bayar: Rp 0");
        lblTotalBayar.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblTotalBayar.setForeground(new java.awt.Color(27, 54, 93));
        pnlBottomCard.add(lblTotalBayar, java.awt.BorderLayout.WEST);

        btnLanjutBayar.setText("Buat Pesanan & Lanjut Bayar  →");
        btnLanjutBayar.setBackground(new java.awt.Color(243, 112, 33));
        btnLanjutBayar.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnLanjutBayar.setForeground(new java.awt.Color(255, 255, 255));
        btnLanjutBayar.setFocusPainted(false);
        pnlBottomCard.add(btnLanjutBayar, java.awt.BorderLayout.EAST);
        add(pnlBottomCard, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIsiAkun;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnLanjutBayar;
    private javax.swing.JButton btnPilihOtomatis;
    private javax.swing.JButton btnResetKursi;
    private javax.swing.JComboBox<String> cmbGerbong;
    private javax.swing.JLabel lblGerbong;
    private javax.swing.JLabel lblInfoKereta;
    private javax.swing.JLabel lblKursiTerpilih;
    private javax.swing.JLabel lblPenumpangPreview;
    private javax.swing.JLabel lblPenumpangTitle;
    private javax.swing.JLabel lblSeatPreview;
    private javax.swing.JLabel lblStep1;
    private javax.swing.JLabel lblStep2;
    private javax.swing.JLabel lblStep3;
    private javax.swing.JLabel lblStep4;
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
    private javax.swing.JPanel pnlStepper;
    private javax.swing.JPanel pnlTitleBox;
    private javax.swing.JPanel pnlTopArea;
    private javax.swing.JScrollPane scrPenumpang;
    // End of variables declaration//GEN-END:variables
}
