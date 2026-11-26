package tiketkereta.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import tiketkereta.model.OpsiJadwal;
import tiketkereta.model.Stasiun;
import tiketkereta.util.Tema;
import tiketkereta.util.ValidasiException;

public class CariTiketPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;
    private ArrayList<Stasiun> stasiunAktif;

    public CariTiketPanel() {
        this(null);
    }

    public CariTiketPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.stasiunAktif = new ArrayList<Stasiun>();
        initComponents();
        setupStyle();
        setupEvents();
    }

    private void setupStyle() {
        Tema.styleCanvas(this);
        pnlTopArea.setOpaque(false);
        pnlStepper.setOpaque(false);
        Tema.styleStepItem(lblStep1, true, false);
        Tema.styleStepItem(lblStep2, false, false);
        Tema.styleStepItem(lblStep3, false, false);
        Tema.styleStepItem(lblStep4, false, false);

        Tema.styleHeroCard(pnlHero);
        pnlBody.setOpaque(false);
        Tema.styleCard(pnlSearchCard, 20, 24);
        pnlBottomGrid.setOpaque(false);
        Tema.styleCard(pnlQuickCard, 18, 22);
        Tema.styleCard(pnlGuideCard, 18, 22);

        pnlCardHead.setOpaque(false);
        pnlStations.setOpaque(false);
        pnlAsal.setOpaque(false);
        pnlTujuan.setOpaque(false);
        pnlBottomSearch.setOpaque(false);
        pnlPenumpang.setOpaque(false);
        pnlQuickButtons.setOpaque(false);
        pnlGuideList.setOpaque(false);

        lblHeroTag.setFont(Tema.FONT_SMALL);
        lblHeroTag.setForeground(Tema.ACCENT);
        lblHeroTitle.setFont(Tema.FONT_HERO);
        lblHeroTitle.setForeground(Color.WHITE);
        lblHeroSub.setFont(Tema.FONT_BODY);
        lblHeroSub.setForeground(new Color(0xCBD5E1));

        Tema.styleSubTitle(lblCardTitle);
        Tema.styleFieldLabel(lblAsal);
        Tema.styleFieldLabel(lblTujuan);
        Tema.styleFieldLabel(lblPenumpang);
        Tema.styleSubTitle(lblQuickTitle);
        Tema.styleSubTitle(lblGuideTitle);

        Tema.styleBody(lblGuide1);
        Tema.styleBody(lblGuide2);
        Tema.styleBody(lblGuide3);
        Tema.styleBody(lblGuide4);

        Tema.styleComboBox(cmbAsal);
        Tema.styleComboBox(cmbTujuan);
        spnPenumpang.setModel(new SpinnerNumberModel(1, 1, 4, 1));
        Tema.styleSpinner(spnPenumpang);

        Tema.styleSecondaryButton(btnTukar);
        Tema.styleAccentButton(btnCari);
        btnCari.setPreferredSize(Tema.dim(240, 44));

        Tema.styleSecondaryButton(btnRute1);
        Tema.styleSecondaryButton(btnRute2);
        Tema.styleSecondaryButton(btnRute3);
        Tema.styleSecondaryButton(btnRute4);
    }

    private void setupEvents() {
        btnTukar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idxAsal = cmbAsal.getSelectedIndex();
                int idxTujuan = cmbTujuan.getSelectedIndex();
                cmbAsal.setSelectedIndex(idxTujuan);
                cmbTujuan.setSelectedIndex(idxAsal);
            }
        });

        btnCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesCari();
            }
        });

        btnRute1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pilihRuteCepat("SMP", "KML");
            }
        });
        btnRute2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pilihRuteCepat("BKL", "KLA");
            }
        });
        btnRute3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pilihRuteCepat("PMK", "BKL");
            }
        });
        btnRute4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pilihRuteCepat("KML", "SPG");
            }
        });
    }

    public void refreshData() {
        if (mainFrame == null) {
            return;
        }
        stasiunAktif = new ArrayList<Stasiun>(mainFrame.getDataStore().getStasiunList());
        String[] labels = new String[stasiunAktif.size()];
        for (int i = 0; i < stasiunAktif.size(); i++) {
            labels[i] = stasiunAktif.get(i).toString();
        }
        cmbAsal.setModel(new DefaultComboBoxModel<String>(labels));
        cmbTujuan.setModel(new DefaultComboBoxModel<String>(labels));
        pilihKodeDiCombo(cmbAsal, "SMP");
        pilihKodeDiCombo(cmbTujuan, "KML");
    }

    private void pilihKodeDiCombo(javax.swing.JComboBox<String> combo, String kode) {
        for (int i = 0; i < stasiunAktif.size(); i++) {
            if (stasiunAktif.get(i).getCode().equalsIgnoreCase(kode)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        if (stasiunAktif.size() > 0) {
            combo.setSelectedIndex(0);
        }
    }

    private void pilihRuteCepat(String asal, String tujuan) {
        pilihKodeDiCombo(cmbAsal, asal);
        pilihKodeDiCombo(cmbTujuan, tujuan);
        prosesCari();
    }

    private void prosesCari() {
        if (mainFrame == null || stasiunAktif.isEmpty()) {
            return;
        }
        int idxAsal = cmbAsal.getSelectedIndex();
        int idxTujuan = cmbTujuan.getSelectedIndex();
        if (idxAsal < 0 || idxTujuan < 0) {
            JOptionPane.showMessageDialog(this, "Pilih stasiun asal dan tujuan terlebih dahulu.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Stasiun asal = stasiunAktif.get(idxAsal);
        Stasiun tujuan = stasiunAktif.get(idxTujuan);
        int jumlah = ((Number) spnPenumpang.getValue()).intValue();

        try {
            ArrayList<OpsiJadwal> hasil = mainFrame.getJadwalService().cariJadwalCocok(
                    asal.getCode(), tujuan.getCode(), jumlah,
                    mainFrame.getPemesananService(), mainFrame.getTarifService());
            mainFrame.tampilkanPilihKereta(asal, tujuan, jumlah, hasil);
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Pencarian Gagal", JOptionPane.WARNING_MESSAGE);
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
        pnlHero = new javax.swing.JPanel();
        lblHeroTag = new javax.swing.JLabel();
        lblHeroTitle = new javax.swing.JLabel();
        lblHeroSub = new javax.swing.JLabel();
        pnlBody = new javax.swing.JPanel();
        pnlSearchCard = new javax.swing.JPanel();
        pnlCardHead = new javax.swing.JPanel();
        lblCardTitle = new javax.swing.JLabel();
        btnTukar = new javax.swing.JButton();
        pnlStations = new javax.swing.JPanel();
        pnlAsal = new javax.swing.JPanel();
        lblAsal = new javax.swing.JLabel();
        cmbAsal = new javax.swing.JComboBox<>();
        pnlTujuan = new javax.swing.JPanel();
        lblTujuan = new javax.swing.JLabel();
        cmbTujuan = new javax.swing.JComboBox<>();
        pnlBottomSearch = new javax.swing.JPanel();
        pnlPenumpang = new javax.swing.JPanel();
        lblPenumpang = new javax.swing.JLabel();
        spnPenumpang = new javax.swing.JSpinner();
        btnCari = new javax.swing.JButton();
        pnlBottomGrid = new javax.swing.JPanel();
        pnlQuickCard = new javax.swing.JPanel();
        lblQuickTitle = new javax.swing.JLabel();
        pnlQuickButtons = new javax.swing.JPanel();
        btnRute1 = new javax.swing.JButton();
        btnRute2 = new javax.swing.JButton();
        btnRute3 = new javax.swing.JButton();
        btnRute4 = new javax.swing.JButton();
        pnlGuideCard = new javax.swing.JPanel();
        lblGuideTitle = new javax.swing.JLabel();
        pnlGuideList = new javax.swing.JPanel();
        lblGuide1 = new javax.swing.JLabel();
        lblGuide2 = new javax.swing.JLabel();
        lblGuide3 = new javax.swing.JLabel();
        lblGuide4 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(245, 247, 250));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 26, 22, 26));
        setPreferredSize(new java.awt.Dimension(1120, 680));
        setLayout(new java.awt.BorderLayout(0, 16));

        pnlTopArea.setOpaque(false);
        pnlTopArea.setLayout(new java.awt.BorderLayout(0, 12));

        pnlStepper.setOpaque(false);
        pnlStepper.setLayout(new java.awt.GridLayout(1, 4, 10, 0));

        lblStep1.setText("1. Cari Rute");
        lblStep1.setBackground(new java.awt.Color(243, 112, 33));
        lblStep1.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        lblStep1.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblStep1.setForeground(new java.awt.Color(255, 255, 255));
        lblStep1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStep1.setOpaque(true);
        pnlStepper.add(lblStep1);

        lblStep2.setText("2. Pilih Kereta");
        lblStep2.setBackground(new java.awt.Color(248, 250, 252));
        lblStep2.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        lblStep2.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblStep2.setForeground(new java.awt.Color(100, 116, 139));
        lblStep2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStep2.setOpaque(true);
        pnlStepper.add(lblStep2);

        lblStep3.setText("3. Penumpang & Kursi");
        lblStep3.setBackground(new java.awt.Color(248, 250, 252));
        lblStep3.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        lblStep3.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblStep3.setForeground(new java.awt.Color(100, 116, 139));
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

        pnlHero.setBackground(new java.awt.Color(27, 54, 93));
        pnlHero.setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 26, 22, 26));
        pnlHero.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblHeroTag.setText("ACCESS BY KAI • JALUR KERETA API MADURA");
        lblHeroTag.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblHeroTag.setForeground(new java.awt.Color(243, 112, 33));
        pnlHero.add(lblHeroTag);

        lblHeroTitle.setText("Mau Bepergian ke Mana Hari Ini?");
        lblHeroTitle.setFont(new java.awt.Font("Segoe UI", 1, 24));
        lblHeroTitle.setForeground(new java.awt.Color(255, 255, 255));
        pnlHero.add(lblHeroTitle);

        lblHeroSub.setText("Pilih stasiun keberangkatan, stasiun tujuan, dan jumlah penumpang untuk melihat jadwal terbaik.");
        lblHeroSub.setFont(new java.awt.Font("Segoe UI", 0, 13));
        lblHeroSub.setForeground(new java.awt.Color(203, 213, 225));
        pnlHero.add(lblHeroSub);
        pnlTopArea.add(pnlHero, java.awt.BorderLayout.CENTER);
        add(pnlTopArea, java.awt.BorderLayout.NORTH);

        pnlBody.setOpaque(false);
        pnlBody.setLayout(new java.awt.BorderLayout(0, 16));

        pnlSearchCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlSearchCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24)));
        pnlSearchCard.setLayout(new java.awt.BorderLayout(0, 16));

        pnlCardHead.setOpaque(false);
        pnlCardHead.setLayout(new java.awt.BorderLayout());

        lblCardTitle.setText("Rencana Perjalanan Anda");
        lblCardTitle.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblCardTitle.setForeground(new java.awt.Color(15, 23, 42));
        pnlCardHead.add(lblCardTitle, java.awt.BorderLayout.WEST);

        btnTukar.setText("« »  Tukar Stasiun Asal & Tujuan");
        btnTukar.setBackground(new java.awt.Color(255, 255, 255));
        btnTukar.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnTukar.setForeground(new java.awt.Color(15, 23, 42));
        btnTukar.setFocusPainted(false);
        pnlCardHead.add(btnTukar, java.awt.BorderLayout.EAST);
        pnlSearchCard.add(pnlCardHead, java.awt.BorderLayout.NORTH);

        pnlStations.setOpaque(false);
        pnlStations.setLayout(new java.awt.GridLayout(1, 2, 18, 0));

        pnlAsal.setOpaque(false);
        pnlAsal.setLayout(new java.awt.BorderLayout(0, 6));

        lblAsal.setText("Stasiun Keberangkatan (Asal)");
        lblAsal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblAsal.setForeground(new java.awt.Color(15, 23, 42));
        pnlAsal.add(lblAsal, java.awt.BorderLayout.NORTH);

        cmbAsal.setFont(new java.awt.Font("Segoe UI", 0, 13));
        cmbAsal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Stasiun Kamal (KML)", "Stasiun Bangkalan (BKL)", "Stasiun Sampang (SPG)" }));
        cmbAsal.setPreferredSize(new java.awt.Dimension(170, 38));
        pnlAsal.add(cmbAsal, java.awt.BorderLayout.CENTER);
        pnlStations.add(pnlAsal);

        pnlTujuan.setOpaque(false);
        pnlTujuan.setLayout(new java.awt.BorderLayout(0, 6));

        lblTujuan.setText("Stasiun Kedatangan (Tujuan)");
        lblTujuan.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblTujuan.setForeground(new java.awt.Color(15, 23, 42));
        pnlTujuan.add(lblTujuan, java.awt.BorderLayout.NORTH);

        cmbTujuan.setFont(new java.awt.Font("Segoe UI", 0, 13));
        cmbTujuan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Stasiun Sumenep (SMP)", "Stasiun Pamekasan (PMK)", "Stasiun Kalianget (KLT)" }));
        cmbTujuan.setPreferredSize(new java.awt.Dimension(170, 38));
        pnlTujuan.add(cmbTujuan, java.awt.BorderLayout.CENTER);
        pnlStations.add(pnlTujuan);
        pnlSearchCard.add(pnlStations, java.awt.BorderLayout.CENTER);

        pnlBottomSearch.setOpaque(false);
        pnlBottomSearch.setLayout(new java.awt.BorderLayout(16, 0));

        pnlPenumpang.setOpaque(false);
        pnlPenumpang.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 0));

        lblPenumpang.setText("Jumlah Penumpang (Maks. 4 Orang):");
        lblPenumpang.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblPenumpang.setForeground(new java.awt.Color(15, 23, 42));
        pnlPenumpang.add(lblPenumpang);

        spnPenumpang.setFont(new java.awt.Font("Segoe UI", 1, 16));
        spnPenumpang.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));
        spnPenumpang.setPreferredSize(new java.awt.Dimension(104, 42));
        pnlPenumpang.add(spnPenumpang);
        pnlBottomSearch.add(pnlPenumpang, java.awt.BorderLayout.WEST);

        btnCari.setText("Cari Jadwal Kereta  →");
        btnCari.setBackground(new java.awt.Color(243, 112, 33));
        btnCari.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnCari.setForeground(new java.awt.Color(255, 255, 255));
        btnCari.setFocusPainted(false);
        btnCari.setPreferredSize(new java.awt.Dimension(240, 44));
        pnlBottomSearch.add(btnCari, java.awt.BorderLayout.EAST);
        pnlSearchCard.add(pnlBottomSearch, java.awt.BorderLayout.SOUTH);
        pnlBody.add(pnlSearchCard, java.awt.BorderLayout.NORTH);

        pnlBottomGrid.setOpaque(false);
        pnlBottomGrid.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        pnlQuickCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlQuickCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(18, 22, 18, 22)));
        pnlQuickCard.setLayout(new java.awt.BorderLayout(0, 12));

        lblQuickTitle.setText("Rute Populer Sekali Klik");
        lblQuickTitle.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblQuickTitle.setForeground(new java.awt.Color(15, 23, 42));
        pnlQuickCard.add(lblQuickTitle, java.awt.BorderLayout.NORTH);

        pnlQuickButtons.setOpaque(false);
        pnlQuickButtons.setLayout(new java.awt.GridLayout(4, 1, 0, 8));

        btnRute1.setText("Sumenep (SMP)  →  Kamal (KML)");
        btnRute1.setBackground(new java.awt.Color(255, 255, 255));
        btnRute1.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnRute1.setForeground(new java.awt.Color(15, 23, 42));
        btnRute1.setFocusPainted(false);
        pnlQuickButtons.add(btnRute1);

        btnRute2.setText("Bangkalan (BKL)  →  Kalianget (KLA)");
        btnRute2.setBackground(new java.awt.Color(255, 255, 255));
        btnRute2.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnRute2.setForeground(new java.awt.Color(15, 23, 42));
        btnRute2.setFocusPainted(false);
        pnlQuickButtons.add(btnRute2);

        btnRute3.setText("Pamekasan (PMK)  →  Bangkalan (BKL)");
        btnRute3.setBackground(new java.awt.Color(255, 255, 255));
        btnRute3.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnRute3.setForeground(new java.awt.Color(15, 23, 42));
        btnRute3.setFocusPainted(false);
        pnlQuickButtons.add(btnRute3);

        btnRute4.setText("Kamal (KML)  →  Sampang (SPG)");
        btnRute4.setBackground(new java.awt.Color(255, 255, 255));
        btnRute4.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnRute4.setForeground(new java.awt.Color(15, 23, 42));
        btnRute4.setFocusPainted(false);
        pnlQuickButtons.add(btnRute4);
        pnlQuickCard.add(pnlQuickButtons, java.awt.BorderLayout.CENTER);
        pnlBottomGrid.add(pnlQuickCard);

        pnlGuideCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlGuideCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(18, 22, 18, 22)));
        pnlGuideCard.setLayout(new java.awt.BorderLayout(0, 12));

        lblGuideTitle.setText("Panduan Kelas & Layanan Kereta");
        lblGuideTitle.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblGuideTitle.setForeground(new java.awt.Color(15, 23, 42));
        pnlGuideCard.add(lblGuideTitle, java.awt.BorderLayout.NORTH);

        pnlGuideList.setOpaque(false);
        pnlGuideList.setLayout(new java.awt.GridLayout(4, 1, 0, 8));

        lblGuide1.setText("•  Eksekutif (1.8x) — Gerbong 50 kursi formasi 2-2 lega");
        lblGuide1.setFont(new java.awt.Font("Segoe UI", 0, 13));
        lblGuide1.setForeground(new java.awt.Color(15, 23, 42));
        pnlGuideList.add(lblGuide1);

        lblGuide2.setText("•  Bisnis (1.4x) — Gerbong 64 kursi formasi 2-2 nyaman");
        lblGuide2.setFont(new java.awt.Font("Segoe UI", 0, 13));
        lblGuide2.setForeground(new java.awt.Color(15, 23, 42));
        pnlGuideList.add(lblGuide2);

        lblGuide3.setText("•  Ekonomi Premium (1.2x) — Gerbong 80 kursi formasi 2-2");
        lblGuide3.setFont(new java.awt.Font("Segoe UI", 0, 13));
        lblGuide3.setForeground(new java.awt.Color(15, 23, 42));
        pnlGuideList.add(lblGuide3);

        lblGuide4.setText("•  Ekonomi & Komuter (1.0x) — Gerbong 80 / 90 kursi hemat");
        lblGuide4.setFont(new java.awt.Font("Segoe UI", 0, 13));
        lblGuide4.setForeground(new java.awt.Color(15, 23, 42));
        pnlGuideList.add(lblGuide4);
        pnlGuideCard.add(pnlGuideList, java.awt.BorderLayout.CENTER);
        pnlBottomGrid.add(pnlGuideCard);
        pnlBody.add(pnlBottomGrid, java.awt.BorderLayout.CENTER);
        add(pnlBody, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnRute1;
    private javax.swing.JButton btnRute2;
    private javax.swing.JButton btnRute3;
    private javax.swing.JButton btnRute4;
    private javax.swing.JButton btnTukar;
    private javax.swing.JComboBox<String> cmbAsal;
    private javax.swing.JComboBox<String> cmbTujuan;
    private javax.swing.JLabel lblAsal;
    private javax.swing.JLabel lblCardTitle;
    private javax.swing.JLabel lblGuide1;
    private javax.swing.JLabel lblGuide2;
    private javax.swing.JLabel lblGuide3;
    private javax.swing.JLabel lblGuide4;
    private javax.swing.JLabel lblGuideTitle;
    private javax.swing.JLabel lblHeroSub;
    private javax.swing.JLabel lblHeroTag;
    private javax.swing.JLabel lblHeroTitle;
    private javax.swing.JLabel lblPenumpang;
    private javax.swing.JLabel lblQuickTitle;
    private javax.swing.JLabel lblStep1;
    private javax.swing.JLabel lblStep2;
    private javax.swing.JLabel lblStep3;
    private javax.swing.JLabel lblStep4;
    private javax.swing.JLabel lblTujuan;
    private javax.swing.JPanel pnlAsal;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlBottomGrid;
    private javax.swing.JPanel pnlBottomSearch;
    private javax.swing.JPanel pnlCardHead;
    private javax.swing.JPanel pnlGuideCard;
    private javax.swing.JPanel pnlGuideList;
    private javax.swing.JPanel pnlHero;
    private javax.swing.JPanel pnlPenumpang;
    private javax.swing.JPanel pnlQuickButtons;
    private javax.swing.JPanel pnlQuickCard;
    private javax.swing.JPanel pnlSearchCard;
    private javax.swing.JPanel pnlStations;
    private javax.swing.JPanel pnlStepper;
    private javax.swing.JPanel pnlTopArea;
    private javax.swing.JPanel pnlTujuan;
    private javax.swing.JSpinner spnPenumpang;
    // End of variables declaration//GEN-END:variables
}
