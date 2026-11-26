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

        setLayout(new java.awt.BorderLayout(0, 16));

        pnlTopArea.setLayout(new java.awt.BorderLayout(0, 12));

        pnlStepper.setLayout(new java.awt.GridLayout(1, 4, 10, 0));

        lblStep1.setText("1. Cari Rute");
        pnlStepper.add(lblStep1);

        lblStep2.setText("2. Pilih Kereta");
        pnlStepper.add(lblStep2);

        lblStep3.setText("3. Penumpang & Kursi");
        pnlStepper.add(lblStep3);

        lblStep4.setText("4. Bayar & E-Tiket");
        pnlStepper.add(lblStep4);

        pnlTopArea.add(pnlStepper, java.awt.BorderLayout.NORTH);

        pnlHero.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        lblHeroTag.setText("ACCESS BY KAI • JALUR KERETA API MADURA");
        pnlHero.add(lblHeroTag);

        lblHeroTitle.setText("Mau Bepergian ke Mana Hari Ini?");
        pnlHero.add(lblHeroTitle);

        lblHeroSub.setText("Pilih stasiun keberangkatan, stasiun tujuan, dan jumlah penumpang untuk melihat jadwal terbaik.");
        pnlHero.add(lblHeroSub);

        pnlTopArea.add(pnlHero, java.awt.BorderLayout.CENTER);

        add(pnlTopArea, java.awt.BorderLayout.NORTH);

        pnlBody.setLayout(new java.awt.BorderLayout(0, 16));

        pnlSearchCard.setLayout(new java.awt.BorderLayout(0, 16));

        pnlCardHead.setLayout(new java.awt.BorderLayout());

        lblCardTitle.setText("Rencana Perjalanan Anda");
        pnlCardHead.add(lblCardTitle, java.awt.BorderLayout.WEST);

        btnTukar.setText("« »  Tukar Stasiun Asal & Tujuan");
        pnlCardHead.add(btnTukar, java.awt.BorderLayout.EAST);

        pnlSearchCard.add(pnlCardHead, java.awt.BorderLayout.NORTH);

        pnlStations.setLayout(new java.awt.GridLayout(1, 2, 18, 0));

        pnlAsal.setLayout(new java.awt.BorderLayout(0, 6));

        lblAsal.setText("Stasiun Keberangkatan (Asal)");
        pnlAsal.add(lblAsal, java.awt.BorderLayout.NORTH);
        pnlAsal.add(cmbAsal, java.awt.BorderLayout.CENTER);

        pnlStations.add(pnlAsal);

        pnlTujuan.setLayout(new java.awt.BorderLayout(0, 6));

        lblTujuan.setText("Stasiun Kedatangan (Tujuan)");
        pnlTujuan.add(lblTujuan, java.awt.BorderLayout.NORTH);
        pnlTujuan.add(cmbTujuan, java.awt.BorderLayout.CENTER);

        pnlStations.add(pnlTujuan);

        pnlSearchCard.add(pnlStations, java.awt.BorderLayout.CENTER);

        pnlBottomSearch.setLayout(new java.awt.BorderLayout(16, 0));

        pnlPenumpang.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 0));

        lblPenumpang.setText("Jumlah Penumpang (Maks. 4 Orang):");
        pnlPenumpang.add(lblPenumpang);
        pnlPenumpang.add(spnPenumpang);

        pnlBottomSearch.add(pnlPenumpang, java.awt.BorderLayout.WEST);

        btnCari.setText("Cari Jadwal Kereta  →");
        pnlBottomSearch.add(btnCari, java.awt.BorderLayout.EAST);

        pnlSearchCard.add(pnlBottomSearch, java.awt.BorderLayout.SOUTH);

        pnlBody.add(pnlSearchCard, java.awt.BorderLayout.NORTH);

        pnlBottomGrid.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        pnlQuickCard.setLayout(new java.awt.BorderLayout(0, 12));

        lblQuickTitle.setText("Rute Populer Sekali Klik");
        pnlQuickCard.add(lblQuickTitle, java.awt.BorderLayout.NORTH);

        pnlQuickButtons.setLayout(new java.awt.GridLayout(4, 1, 0, 8));

        btnRute1.setText("Sumenep (SMP)  →  Kamal (KML)");
        pnlQuickButtons.add(btnRute1);

        btnRute2.setText("Bangkalan (BKL)  →  Kalianget (KLA)");
        pnlQuickButtons.add(btnRute2);

        btnRute3.setText("Pamekasan (PMK)  →  Bangkalan (BKL)");
        pnlQuickButtons.add(btnRute3);

        btnRute4.setText("Kamal (KML)  →  Sampang (SPG)");
        pnlQuickButtons.add(btnRute4);

        pnlQuickCard.add(pnlQuickButtons, java.awt.BorderLayout.CENTER);

        pnlBottomGrid.add(pnlQuickCard);

        pnlGuideCard.setLayout(new java.awt.BorderLayout(0, 12));

        lblGuideTitle.setText("Panduan Kelas & Layanan Kereta");
        pnlGuideCard.add(lblGuideTitle, java.awt.BorderLayout.NORTH);

        pnlGuideList.setLayout(new java.awt.GridLayout(4, 1, 0, 8));

        lblGuide1.setText("•  Eksekutif (1.8x) — Gerbong 50 kursi formasi 2-2 lega");
        pnlGuideList.add(lblGuide1);

        lblGuide2.setText("•  Bisnis (1.4x) — Gerbong 64 kursi formasi 2-2 nyaman");
        pnlGuideList.add(lblGuide2);

        lblGuide3.setText("•  Ekonomi Premium (1.2x) — Gerbong 80 kursi formasi 2-2");
        pnlGuideList.add(lblGuide3);

        lblGuide4.setText("•  Ekonomi & Komuter (1.0x) — Gerbong 80 / 90 kursi hemat");
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
