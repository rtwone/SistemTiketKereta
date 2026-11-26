package tiketkereta.ui;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import tiketkereta.model.Pemesanan;
import tiketkereta.model.Penumpang;
import tiketkereta.model.Stasiun;
import tiketkereta.model.User;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.Tema;

public class CetakTiketPanel extends javax.swing.JPanel {

    private static final String STAGE_IDLE = "IDLE";
    private static final String STAGE_PRINTING = "PRINTING";
    private static final String STAGE_DONE = "DONE";
    private static final Color PAPER_BG = new Color(0xFFFDF9);
    private static final Color STUB_BG = new Color(0xF8FAFC);

    private final MainFrame mainFrame;
    private ArrayList<Pemesanan> daftarTiketLunas;
    private Pemesanan pesananAktif;
    private Timer timerSimulasi;
    private int nilaiProgress;

    public CetakTiketPanel() {
        this(null);
    }

    public CetakTiketPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.daftarTiketLunas = new ArrayList<Pemesanan>();
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlHeader.setOpaque(false);
        pnlTitleBox.setOpaque(false);
        Tema.styleCard(pnlControlCard, 14, 18);
        pnlInputRow.setOpaque(false);
        pnlStage.setOpaque(false);

        pnlIdleWrap.setOpaque(false);
        Tema.centerFlowPanel(pnlIdleWrap, pnlIdleCard);
        Tema.styleCard(pnlIdleCard, 28, 34);
        pnlIdleCard.setPreferredSize(Tema.dim(620, 230));
        pnlIdleDesc.setOpaque(false);

        pnlPrintWrap.setOpaque(false);
        Tema.centerFlowPanel(pnlPrintWrap, pnlPrintCard);
        Tema.styleCard(pnlPrintCard, 30, 36);
        pnlPrintCard.setPreferredSize(Tema.dim(620, 240));
        pnlPrintHead.setOpaque(false);
        pnlProgressBox.setOpaque(false);

        pnlResultWrap.setOpaque(false);
        Tema.styleSoftCard(pnlDoneBanner, 12, 16);
        pnlDoneBanner.setBackground(Tema.SUCCESS_SOFT);
        pnlDoneActions.setOpaque(false);

        Tema.stylePageTitle(lblTitle);
        Tema.styleMuted(lblSubtitle);
        Tema.styleFieldLabel(lblPilihTiket);
        Tema.styleComboBox(cmbTiketLunas);
        Tema.styleTextField(txtKodeManual, "Atau ketik kode (mis: TKT-0001)");
        txtKodeManual.setPreferredSize(Tema.dim(240, 40));
        Tema.styleAccentButton(btnMulaiCetak);

        Tema.styleSubTitle(lblIdleTitle);
        lblIdleTitle.setForeground(Tema.PRIMARY);
        Tema.styleBody(lblIdleDesc1);
        Tema.styleBody(lblIdleDesc2);
        Tema.styleBody(lblIdleDesc3);
        Tema.styleSecondaryButton(btnKeTiketSayaIdle);

        lblPrintBadge.setFont(Tema.FONT_SMALL);
        lblPrintBadge.setForeground(Tema.ACCENT);
        lblPrintTitle.setFont(Tema.FONT_TITLE);
        lblPrintTitle.setForeground(Tema.PRIMARY);
        Tema.styleBody(lblPrintMeta);
        lblPrintStep.setFont(Tema.FONT_BODY_BOLD);
        lblPrintStep.setForeground(Tema.TEXT_MUTED);

        prgCetak.setMinimum(0);
        prgCetak.setMaximum(100);
        prgCetak.setStringPainted(true);
        prgCetak.setFont(Tema.FONT_BODY_BOLD);
        prgCetak.setForeground(Tema.ACCENT);
        prgCetak.setPreferredSize(Tema.dim(520, 30));

        lblDoneInfo.setFont(Tema.FONT_BODY_BOLD);
        lblDoneInfo.setForeground(Tema.STATUS_GREEN);
        Tema.styleSecondaryButton(btnCetakUlang);
        Tema.stylePrimaryButton(btnLihatETiketDigital);

        pnlTiketHolder.setBackground(Tema.CANVAS);
        pnlTiketHolder.setLayout(new BorderLayout() {
            @Override
            public Dimension preferredLayoutSize(Container target) {
                Dimension d = super.preferredLayoutSize(target);
                int vw = scrTiketFisik.getViewport().getWidth();
                if (vw > 0) {
                    d.width = vw;
                }
                return d;
            }
        });
        pnlTiketHolder.add(pnlTiketList, BorderLayout.NORTH);
        pnlTiketList.setOpaque(false);
        pnlTiketList.setBorder(BorderFactory.createEmptyBorder(Tema.s(4), Tema.s(2), Tema.s(12), Tema.s(8)));
        scrTiketFisik.setBorder(BorderFactory.createEmptyBorder());
        scrTiketFisik.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrTiketFisik.getViewport().setBackground(Tema.CANVAS);
        scrTiketFisik.getVerticalScrollBar().setUnitIncrement(Tema.s(20));
    }

    private void setupEvents() {
        btnMulaiCetak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aksiKlikMulaiCetak();
            }
        });
        txtKodeManual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aksiKlikMulaiCetak();
            }
        });
        btnKeTiketSayaIdle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {
                    mainFrame.tampilkanTiketSaya();
                }
            }
        });
        btnCetakUlang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pesananAktif != null) {
                    jalankanAnimasiCetak(pesananAktif);
                }
            }
        });
        btnLihatETiketDigital.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pesananAktif != null && mainFrame != null) {
                    mainFrame.tampilkanETiket(pesananAktif);
                }
            }
        });
    }

    public void refreshData() {
        hentikanTimer();
        muatDaftarTiketLunas(null);
        txtKodeManual.setText("");
        tampilkanStage(STAGE_IDLE);
    }

    public void mulaiSimulasiCetak(Pemesanan pesanan) {
        hentikanTimer();
        muatDaftarTiketLunas(pesanan);
        txtKodeManual.setText("");
        if (pesanan == null) {
            tampilkanStage(STAGE_IDLE);
            return;
        }
        if (!Pemesanan.STATUS_LUNAS.equalsIgnoreCase(pesanan.getStatus())) {
            tampilkanStage(STAGE_IDLE);
            JOptionPane.showMessageDialog(this,
                    "Pesanan " + pesanan.getBookingCode() + " berstatus " + pesanan.getStatus()
                    + ".\nHanya tiket berstatus Lunas yang dapat dicetak menjadi Boarding Pass fisik.",
                    "Belum Dapat Dicetak", JOptionPane.WARNING_MESSAGE);
            return;
        }
        jalankanAnimasiCetak(pesanan);
    }

    private void muatDaftarTiketLunas(Pemesanan pilihTarget) {
        daftarTiketLunas = new ArrayList<Pemesanan>();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
        if (mainFrame != null) {
            User user = mainFrame.getAuthService().getCurrentUser();
            if (user != null) {
                ArrayList<Pemesanan> milikUser = mainFrame.getPemesananService().getPemesananByUser(user.getIdentifier());
                for (Pemesanan p : milikUser) {
                    if (Pemesanan.STATUS_LUNAS.equalsIgnoreCase(p.getStatus())) {
                        daftarTiketLunas.add(p);
                        model.addElement(p.getBookingCode() + " • " + p.getTrainNumber() + " " + p.getTrainName()
                                + " (" + p.getOriginCode() + " → " + p.getDestinationCode() + ") • "
                                + p.getPenumpangList().size() + " Penumpang");
                    }
                }
            }
        }
        if (daftarTiketLunas.isEmpty()) {
            model.addElement("-- Belum ada tiket berstatus Lunas --");
        }
        cmbTiketLunas.setModel(model);
        if (pilihTarget != null) {
            for (int i = 0; i < daftarTiketLunas.size(); i++) {
                if (daftarTiketLunas.get(i).getBookingCode().equalsIgnoreCase(pilihTarget.getBookingCode())) {
                    cmbTiketLunas.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void aksiKlikMulaiCetak() {
        if (mainFrame == null) {
            return;
        }
        Pemesanan target = cariTargetCetak();
        if (target == null) {
            return;
        }
        jalankanAnimasiCetak(target);
    }

    private Pemesanan cariTargetCetak() {
        String manual = txtKodeManual.getText() == null ? "" : txtKodeManual.getText().trim();
        User user = mainFrame.getAuthService().getCurrentUser();
        if (!manual.isEmpty()) {
            Pemesanan ditemukan = mainFrame.getDataStore().cariPemesananByCode(manual);
            if (ditemukan == null || (user != null && !user.getIdentifier().equalsIgnoreCase(ditemukan.getUserIdentifier()))) {
                JOptionPane.showMessageDialog(this,
                        "Kode booking '" + manual + "' tidak ditemukan pada akun Anda.",
                        "Kode Tidak Ditemukan", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            if (!Pemesanan.STATUS_LUNAS.equalsIgnoreCase(ditemukan.getStatus())) {
                JOptionPane.showMessageDialog(this,
                        "Pesanan " + ditemukan.getBookingCode() + " berstatus " + ditemukan.getStatus()
                        + ".\nSilakan lunasi pembayaran terlebih dahulu sebelum mencetak tiket.",
                        "Belum Lunas", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            return ditemukan;
        }
        int idx = cmbTiketLunas.getSelectedIndex();
        if (idx < 0 || idx >= daftarTiketLunas.size()) {
            JOptionPane.showMessageDialog(this,
                    "Anda belum memiliki tiket berstatus Lunas untuk dicetak.\nSilakan pesan dan bayar tiket terlebih dahulu.",
                    "Belum Ada Tiket Lunas", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return daftarTiketLunas.get(idx);
    }

    private void jalankanAnimasiCetak(final Pemesanan pesanan) {
        hentikanTimer();
        this.pesananAktif = pesanan;
        this.nilaiProgress = 0;
        btnMulaiCetak.setEnabled(false);

        lblPrintMeta.setText("Kode Booking: " + pesanan.getBookingCode() + "  •  "
                + pesanan.getTrainNumber() + " " + pesanan.getTrainName() + "  •  "
                + pesanan.getPenumpangList().size() + " Lembar Boarding Pass");
        prgCetak.setValue(0);
        perbaruiTeksTahapCetak(0);
        tampilkanStage(STAGE_PRINTING);

        timerSimulasi = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nilaiProgress += 4;
                if (nilaiProgress >= 100) {
                    prgCetak.setValue(100);
                    perbaruiTeksTahapCetak(100);
                    hentikanTimer();
                    selesaikanSimulasiCetak();
                    return;
                }
                prgCetak.setValue(nilaiProgress);
                perbaruiTeksTahapCetak(nilaiProgress);
            }
        });
        timerSimulasi.start();
    }

    public void selesaikanSimulasiInstan() {
        if (pesananAktif == null) {
            return;
        }
        hentikanTimer();
        selesaikanSimulasiCetak();
    }

    private void perbaruiTeksTahapCetak(int persen) {
        if (persen < 30) {
            lblPrintStep.setText("Memverifikasi kode booking & status pembayaran Lunas...");
        } else if (persen < 60) {
            lblPrintStep.setText("Menyiapkan data penumpang, gerbong, dan nomor kursi...");
        } else if (persen < 88) {
            lblPrintStep.setText("Mencetak salinan fisik Boarding Pass ke kertas termal KAI...");
        } else {
            lblPrintStep.setText("Memotong kertas tiket & memvalidasi seri barcode...");
        }
    }

    private void selesaikanSimulasiCetak() {
        btnMulaiCetak.setEnabled(true);
        if (pesananAktif == null) {
            return;
        }
        renderDaftarTiketFisik(pesananAktif);
        lblDoneInfo.setText("•  Tiket Selesai Dicetak! Salinan Tiket Fisik Resmi Kode "
                + pesananAktif.getBookingCode() + " (" + pesananAktif.getPenumpangList().size() + " Lembar)");
        tampilkanStage(STAGE_DONE);
    }

    private void renderDaftarTiketFisik(Pemesanan pesanan) {
        pnlTiketList.removeAll();
        String asalLabel = namaStasiunLengkap(pesanan.getOriginCode());
        String tujuanLabel = namaStasiunLengkap(pesanan.getDestinationCode());
        String waktuCetak = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date()) + " WIB";
        int totalLembar = Math.max(1, pesanan.getPenumpangList().size());
        long tarifPerOrang = pesanan.getTotalBayar() / totalLembar;

        int nomor = 1;
        for (Penumpang pen : pesanan.getPenumpangList()) {
            JPanel slip = buatLembarBoardingPass(
                    pesanan, pen, nomor, totalLembar, asalLabel, tujuanLabel, tarifPerOrang, waktuCetak);
            pnlTiketList.add(slip);
            nomor++;
        }
        pnlTiketList.revalidate();
        pnlTiketList.repaint();
    }

    private JPanel buatLembarBoardingPass(Pemesanan pesanan, Penumpang pen, int nomor, int total,
            String asalLabel, String tujuanLabel, long tarifPerOrang, String waktuCetak) {
        JPanel slip = new JPanel(new BorderLayout(0, 0));
        slip.setBackground(PAPER_BG);
        slip.setBorder(BorderFactory.createLineBorder(new Color(0xCBD5E1), 1));
        slip.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        slip.add(buatHeaderSlip(nomor, total), BorderLayout.NORTH);
        slip.add(buatBagianUtamaSlip(pesanan, pen, asalLabel, tujuanLabel, waktuCetak), BorderLayout.CENTER);
        slip.add(buatPotonganPetugasSlip(pesanan, pen, tarifPerOrang), BorderLayout.EAST);
        return slip;
    }

    private JPanel buatHeaderSlip(int nomor, int total) {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(Tema.PRIMARY);
        Border aksenBawah = BorderFactory.createMatteBorder(0, 0, Tema.s(4), 0, Tema.ACCENT);
        Border pad = BorderFactory.createEmptyBorder(Tema.s(10), Tema.s(20), Tema.s(10), Tema.s(20));
        header.setBorder(BorderFactory.createCompoundBorder(aksenBawah, pad));

        JLabel lblKiri = new JLabel("PT KERETA API INDONESIA (PERSERO)  •  BOARDING PASS RESMI");
        lblKiri.setFont(Tema.FONT_BODY_BOLD);
        lblKiri.setForeground(Color.WHITE);

        JLabel lblKanan = new JLabel("SALINAN TIKET FISIK  •  LEMBAR " + nomor + " / " + total);
        lblKanan.setFont(Tema.FONT_SMALL);
        lblKanan.setForeground(new Color(0xFDBA74));

        header.add(lblKiri, BorderLayout.WEST);
        header.add(lblKanan, BorderLayout.EAST);
        return header;
    }

    private JPanel buatBagianUtamaSlip(Pemesanan pesanan, Penumpang pen,
            String asalLabel, String tujuanLabel, String waktuCetak) {
        JPanel main = new JPanel(new BorderLayout(0, Tema.s(14)));
        main.setBackground(PAPER_BG);
        main.setBorder(BorderFactory.createEmptyBorder(Tema.s(18), Tema.s(22), Tema.s(16), Tema.s(22)));

        JPanel rowAtas = new JPanel(new GridLayout(1, 3, Tema.s(16), 0));
        rowAtas.setOpaque(false);
        rowAtas.add(buatKolomData("NAMA & IDENTITAS PENUMPANG",
                pen.getNama().toUpperCase(), pen.getIdType() + " : " + pen.getIdNumber(), false));
        rowAtas.add(buatKolomData("ARMADA & KELAS LAYANAN",
                pesanan.getTrainNumber() + " - " + pesanan.getTrainName(),
                "Kelas " + pesanan.getKelas(), false));
        rowAtas.add(buatKolomData("TEMPAT DUDUK & KODE BOOKING",
                formatKursiPanjang(pen.getKodeKursi()),
                "Kode Booking: " + pesanan.getBookingCode(), true));

        JPanel rowRute = new JPanel(new GridLayout(1, 3, Tema.s(12), 0));
        Tema.styleSoftCard(rowRute, 12, 16);
        rowRute.add(buatKolomData("STASIUN KEBERANGKATAN",
                asalLabel, "Berangkat Pukul " + pesanan.getDepartureTime() + " WIB", false));

        JPanel pnlArah = new JPanel(new GridLayout(2, 1, 0, Tema.s(4)));
        pnlArah.setOpaque(false);
        JLabel lblPanah = new JLabel("- - - -  PERJALANAN LANGSUNG  - - - →", SwingConstants.CENTER);
        lblPanah.setFont(Tema.FONT_BODY_BOLD);
        lblPanah.setForeground(Tema.ACCENT);
        JLabel lblCatatan = new JLabel("Siapkan KTP / Paspor asli saat boarding", SwingConstants.CENTER);
        Tema.styleMuted(lblCatatan);
        pnlArah.add(lblPanah);
        pnlArah.add(lblCatatan);
        rowRute.add(pnlArah);

        rowRute.add(buatKolomData("STASIUN TUJUAN",
                tujuanLabel, "Tiba Pukul " + pesanan.getArrivalTime() + " WIB", false));

        JPanel rowBawah = new JPanel(new BorderLayout(Tema.s(16), 0));
        rowBawah.setOpaque(false);
        String seri = "CTM-KAI-" + pesanan.getBookingCode().replace("-", "") + "-" + pen.getKodeKursi();
        rowBawah.add(buatPanelBarcode(seri), BorderLayout.WEST);

        JPanel metaCetak = new JPanel(new GridLayout(2, 1, 0, Tema.s(2)));
        metaCetak.setOpaque(false);
        JLabel lblSeri = new JLabel("NO. SERI CETAK: " + seri + "   •   STATUS: LUNAS");
        lblSeri.setFont(Tema.FONT_BODY_BOLD);
        lblSeri.setForeground(Tema.PRIMARY);
        JLabel lblJamCetak = new JLabel("Dicetak: " + waktuCetak + "   •   Boarding dibuka 30 menit sebelum keberangkatan");
        Tema.styleMuted(lblJamCetak);
        metaCetak.add(lblSeri);
        metaCetak.add(lblJamCetak);
        rowBawah.add(metaCetak, BorderLayout.CENTER);

        main.add(rowAtas, BorderLayout.NORTH);
        main.add(rowRute, BorderLayout.CENTER);
        main.add(rowBawah, BorderLayout.SOUTH);
        return main;
    }

    private JPanel buatKolomData(String tag, String utama, String sub, boolean sorot) {
        JPanel box = new JPanel(new GridLayout(3, 1, 0, Tema.s(2)));
        if (sorot) {
            Tema.styleAccentCard(box, 8, 12);
        } else {
            box.setOpaque(false);
        }
        JLabel lblTag = new JLabel(tag);
        Tema.styleMuted(lblTag);
        JLabel lblUtama = new JLabel(utama);
        lblUtama.setFont(Tema.FONT_SUBTITLE);
        lblUtama.setForeground(sorot ? Tema.ACCENT : Tema.PRIMARY);
        JLabel lblSub = new JLabel(sub);
        lblSub.setFont(Tema.FONT_BODY_BOLD);
        lblSub.setForeground(Tema.TEXT_PRIMARY);

        box.add(lblTag);
        box.add(lblUtama);
        box.add(lblSub);
        return box;
    }

    private JPanel buatPotonganPetugasSlip(Pemesanan pesanan, Penumpang pen, long tarifPerOrang) {
        JPanel stub = new JPanel(new BorderLayout(0, Tema.s(10)));
        stub.setBackground(STUB_BG);
        stub.setPreferredSize(Tema.dim(265, 210));
        stub.setBorder(buatBorderPerforasiKiri());

        JPanel head = new JPanel(new GridLayout(2, 1, 0, Tema.s(2)));
        head.setOpaque(false);
        JLabel lblStubTag = new JLabel("POTONGAN PETUGAS / ARSIP");
        Tema.styleMuted(lblStubTag);
        JLabel lblKode = new JLabel(pesanan.getBookingCode());
        lblKode.setFont(Tema.FONT_TITLE);
        lblKode.setForeground(Tema.PRIMARY);
        head.add(lblStubTag);
        head.add(lblKode);

        JPanel detail = new JPanel(new GridLayout(4, 1, 0, Tema.s(4)));
        detail.setOpaque(false);
        JLabel l1 = new JLabel("Penumpang: " + pen.getNama());
        JLabel l2 = new JLabel("Rute: " + pesanan.getOriginCode() + " → " + pesanan.getDestinationCode()
                + " (" + pesanan.getDepartureTime() + ")");
        JLabel l3 = new JLabel("Kursi: " + pen.getKodeKursi() + " • " + pesanan.getKelas());
        JLabel l4 = new JLabel("Tarif: " + FormatUtil.formatRupiah(tarifPerOrang));
        Tema.styleBody(l1);
        Tema.styleBody(l2);
        l3.setFont(Tema.FONT_BODY_BOLD);
        l3.setForeground(Tema.ACCENT);
        Tema.styleBody(l4);
        detail.add(l1);
        detail.add(l2);
        detail.add(l3);
        detail.add(l4);

        JLabel badgeValid = new JLabel("•  LUNAS - VALID BOARDING");
        Tema.styleBadge(badgeValid, Tema.SUCCESS_SOFT, Tema.STATUS_GREEN);

        stub.add(head, BorderLayout.NORTH);
        stub.add(detail, BorderLayout.CENTER);
        stub.add(badgeValid, BorderLayout.SOUTH);
        return stub;
    }

    private Border buatBorderPerforasiKiri() {
        return new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0x94A3B8));
                Stroke putusPutus = new BasicStroke(
                        Math.max(1f, Tema.s(2)),
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10f,
                        new float[]{Tema.s(6), Tema.s(5)},
                        0f);
                g2.setStroke(putusPutus);
                g2.drawLine(x + 1, y + Tema.s(8), x + 1, y + height - Tema.s(8));
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(Tema.s(16), Tema.s(20), Tema.s(16), Tema.s(18));
            }
        };
    }

    private JPanel buatPanelBarcode(final String teksSeri) {
        JPanel barcode = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Tema.TEXT_PRIMARY);
                int x = Tema.s(6);
                int tinggi = getHeight() - Tema.s(8);
                int y = Tema.s(4);
                int idx = 0;
                while (x < getWidth() - Tema.s(8)) {
                    char ch = teksSeri.charAt(idx % teksSeri.length());
                    int lebarBatang = Tema.s((ch % 3) + 1);
                    int spasi = Tema.s(((ch + idx) % 2) + 2);
                    if (x + lebarBatang > getWidth() - Tema.s(6)) {
                        break;
                    }
                    g2.fillRect(x, y, lebarBatang, tinggi);
                    x += lebarBatang + spasi;
                    idx++;
                }
                g2.dispose();
            }
        };
        barcode.setBackground(Color.WHITE);
        barcode.setBorder(BorderFactory.createLineBorder(Tema.BORDER, 1));
        barcode.setPreferredSize(Tema.dim(210, 42));
        return barcode;
    }

    private String formatKursiPanjang(String kodeKursi) {
        if (kodeKursi == null || !kodeKursi.startsWith("G") || !kodeKursi.contains("-")) {
            return "Kursi " + (kodeKursi == null ? "-" : kodeKursi);
        }
        int idxStrip = kodeKursi.indexOf('-');
        String noGerbong = kodeKursi.substring(1, idxStrip);
        String noKursi = kodeKursi.substring(idxStrip + 1);
        return "GERBONG " + noGerbong + "  •  KURSI " + noKursi;
    }

    private String namaStasiunLengkap(String code) {
        if (mainFrame != null) {
            Stasiun s = mainFrame.getDataStore().cariStasiunByCode(code);
            if (s != null) {
                return s.getName() + " (" + s.getCode() + ")";
            }
        }
        return code;
    }

    private void tampilkanStage(String stage) {
        ((CardLayout) pnlStage.getLayout()).show(pnlStage, stage);
    }

    private void hentikanTimer() {
        if (timerSimulasi != null && timerSimulasi.isRunning()) {
            timerSimulasi.stop();
        }
        btnMulaiCetak.setEnabled(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        pnlTitleBox = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlControlCard = new javax.swing.JPanel();
        lblPilihTiket = new javax.swing.JLabel();
        pnlInputRow = new javax.swing.JPanel();
        cmbTiketLunas = new javax.swing.JComboBox<>();
        txtKodeManual = new javax.swing.JTextField();
        btnMulaiCetak = new javax.swing.JButton();
        pnlStage = new javax.swing.JPanel();
        pnlIdleWrap = new javax.swing.JPanel();
        pnlIdleCard = new javax.swing.JPanel();
        lblIdleTitle = new javax.swing.JLabel();
        pnlIdleDesc = new javax.swing.JPanel();
        lblIdleDesc1 = new javax.swing.JLabel();
        lblIdleDesc2 = new javax.swing.JLabel();
        lblIdleDesc3 = new javax.swing.JLabel();
        btnKeTiketSayaIdle = new javax.swing.JButton();
        pnlPrintWrap = new javax.swing.JPanel();
        pnlPrintCard = new javax.swing.JPanel();
        pnlPrintHead = new javax.swing.JPanel();
        lblPrintBadge = new javax.swing.JLabel();
        lblPrintTitle = new javax.swing.JLabel();
        lblPrintMeta = new javax.swing.JLabel();
        pnlProgressBox = new javax.swing.JPanel();
        prgCetak = new javax.swing.JProgressBar();
        lblPrintStep = new javax.swing.JLabel();
        pnlResultWrap = new javax.swing.JPanel();
        pnlDoneBanner = new javax.swing.JPanel();
        lblDoneInfo = new javax.swing.JLabel();
        pnlDoneActions = new javax.swing.JPanel();
        btnCetakUlang = new javax.swing.JButton();
        btnLihatETiketDigital = new javax.swing.JButton();
        scrTiketFisik = new javax.swing.JScrollPane();
        pnlTiketHolder = new javax.swing.JPanel();
        pnlTiketList = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout(0, 16));

        pnlHeader.setLayout(new java.awt.BorderLayout(0, 14));

        pnlTitleBox.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblTitle.setText("Cetak Tiket Mandiri (CTM)");
        pnlTitleBox.add(lblTitle);

        lblSubtitle.setText("Simulasi mesin cetak Boarding Pass stasiun KAI • Pilih tiket Lunas untuk mencetak salinan tiket fisik");
        pnlTitleBox.add(lblSubtitle);

        pnlHeader.add(pnlTitleBox, java.awt.BorderLayout.NORTH);

        pnlControlCard.setLayout(new java.awt.BorderLayout(14, 0));

        lblPilihTiket.setText("Pilih Tiket Lunas:");
        pnlControlCard.add(lblPilihTiket, java.awt.BorderLayout.WEST);

        pnlInputRow.setLayout(new java.awt.BorderLayout(10, 0));
        pnlInputRow.add(cmbTiketLunas, java.awt.BorderLayout.CENTER);
        pnlInputRow.add(txtKodeManual, java.awt.BorderLayout.EAST);

        pnlControlCard.add(pnlInputRow, java.awt.BorderLayout.CENTER);

        btnMulaiCetak.setText("Cetak Tiket Sekarang");
        pnlControlCard.add(btnMulaiCetak, java.awt.BorderLayout.EAST);

        pnlHeader.add(pnlControlCard, java.awt.BorderLayout.CENTER);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlStage.setLayout(new java.awt.CardLayout());

        pnlIdleWrap.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        pnlIdleCard.setLayout(new java.awt.BorderLayout(0, 14));

        lblIdleTitle.setText("Mesin Cetak Tiket Mandiri Siap Digunakan");
        pnlIdleCard.add(lblIdleTitle, java.awt.BorderLayout.NORTH);

        pnlIdleDesc.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblIdleDesc1.setText("•  Pastikan pesanan tiket Anda sudah berstatus LUNAS.");
        pnlIdleDesc.add(lblIdleDesc1);

        lblIdleDesc2.setText("•  Pilih pesanan pada daftar di atas atau ketik Kode Booking, lalu klik Cetak Tiket Sekarang.");
        pnlIdleDesc.add(lblIdleDesc2);

        lblIdleDesc3.setText("•  Sistem akan mensimulasikan pencetakan Boarding Pass dan menampilkan salinan tiket fisik resmi.");
        pnlIdleDesc.add(lblIdleDesc3);

        pnlIdleCard.add(pnlIdleDesc, java.awt.BorderLayout.CENTER);

        btnKeTiketSayaIdle.setText("Lihat Daftar Tiket Saya");
        pnlIdleCard.add(btnKeTiketSayaIdle, java.awt.BorderLayout.SOUTH);

        pnlIdleWrap.add(pnlIdleCard);

        pnlStage.add(pnlIdleWrap, "IDLE");

        pnlPrintWrap.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        pnlPrintCard.setLayout(new java.awt.BorderLayout(0, 18));

        pnlPrintHead.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblPrintBadge.setText("SIMULASI MESIN CETAK TIKET MANDIRI (CTM)");
        pnlPrintHead.add(lblPrintBadge);

        lblPrintTitle.setText("Sedang Mencetak Tiket Fisik...");
        pnlPrintHead.add(lblPrintTitle);

        lblPrintMeta.setText("Kode Booking: -");
        pnlPrintHead.add(lblPrintMeta);

        pnlPrintCard.add(pnlPrintHead, java.awt.BorderLayout.NORTH);

        pnlProgressBox.setLayout(new java.awt.BorderLayout(0, 10));
        pnlProgressBox.add(prgCetak, java.awt.BorderLayout.NORTH);

        lblPrintStep.setText("Memverifikasi kode booking...");
        pnlProgressBox.add(lblPrintStep, java.awt.BorderLayout.SOUTH);

        pnlPrintCard.add(pnlProgressBox, java.awt.BorderLayout.CENTER);

        pnlPrintWrap.add(pnlPrintCard);

        pnlStage.add(pnlPrintWrap, "PRINTING");

        pnlResultWrap.setLayout(new java.awt.BorderLayout(0, 14));

        pnlDoneBanner.setLayout(new java.awt.BorderLayout(12, 0));

        lblDoneInfo.setText("•  Tiket Selesai Dicetak!");
        pnlDoneBanner.add(lblDoneInfo, java.awt.BorderLayout.WEST);

        pnlDoneActions.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));

        btnCetakUlang.setText("Cetak Ulang");
        pnlDoneActions.add(btnCetakUlang);

        btnLihatETiketDigital.setText("Buka E-Tiket Digital");
        pnlDoneActions.add(btnLihatETiketDigital);

        pnlDoneBanner.add(pnlDoneActions, java.awt.BorderLayout.EAST);

        pnlResultWrap.add(pnlDoneBanner, java.awt.BorderLayout.NORTH);

        pnlTiketHolder.setLayout(new java.awt.BorderLayout());

        pnlTiketList.setLayout(new java.awt.GridLayout(0, 1, 0, 18));
        pnlTiketHolder.add(pnlTiketList, java.awt.BorderLayout.NORTH);

        scrTiketFisik.setViewportView(pnlTiketHolder);

        pnlResultWrap.add(scrTiketFisik, java.awt.BorderLayout.CENTER);

        pnlStage.add(pnlResultWrap, "DONE");

        add(pnlStage, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetakUlang;
    private javax.swing.JButton btnKeTiketSayaIdle;
    private javax.swing.JButton btnLihatETiketDigital;
    private javax.swing.JButton btnMulaiCetak;
    private javax.swing.JComboBox<String> cmbTiketLunas;
    private javax.swing.JLabel lblDoneInfo;
    private javax.swing.JLabel lblIdleDesc1;
    private javax.swing.JLabel lblIdleDesc2;
    private javax.swing.JLabel lblIdleDesc3;
    private javax.swing.JLabel lblIdleTitle;
    private javax.swing.JLabel lblPilihTiket;
    private javax.swing.JLabel lblPrintBadge;
    private javax.swing.JLabel lblPrintMeta;
    private javax.swing.JLabel lblPrintStep;
    private javax.swing.JLabel lblPrintTitle;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlControlCard;
    private javax.swing.JPanel pnlDoneActions;
    private javax.swing.JPanel pnlDoneBanner;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlIdleCard;
    private javax.swing.JPanel pnlIdleDesc;
    private javax.swing.JPanel pnlIdleWrap;
    private javax.swing.JPanel pnlInputRow;
    private javax.swing.JPanel pnlPrintCard;
    private javax.swing.JPanel pnlPrintHead;
    private javax.swing.JPanel pnlPrintWrap;
    private javax.swing.JPanel pnlProgressBox;
    private javax.swing.JPanel pnlResultWrap;
    private javax.swing.JPanel pnlStage;
    private javax.swing.JPanel pnlTiketHolder;
    private javax.swing.JPanel pnlTiketList;
    private javax.swing.JPanel pnlTitleBox;
    private javax.swing.JProgressBar prgCetak;
    private javax.swing.JScrollPane scrTiketFisik;
    private javax.swing.JTextField txtKodeManual;
    // End of variables declaration//GEN-END:variables
}
