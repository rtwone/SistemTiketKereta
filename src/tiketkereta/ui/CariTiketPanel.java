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
        Tema.styleHeroCard(pnlHero);
        pnlBody.setOpaque(false);
        Tema.styleCard(pnlSearchCard, 20, 24);
        Tema.styleCard(pnlQuickCard, 18, 24);

        pnlRouteRow.setOpaque(false);
        pnlStations.setOpaque(false);
        pnlAsal.setOpaque(false);
        pnlTujuan.setOpaque(false);
        pnlSwapWrap.setOpaque(false);
        pnlBottomSearch.setOpaque(false);
        pnlPenumpang.setOpaque(false);
        pnlQuickButtons.setOpaque(false);

        lblHeroTag.setFont(Tema.FONT_SMALL);
        lblHeroTag.setForeground(Tema.ACCENT);
        lblHeroTitle.setFont(Tema.FONT_HERO);
        lblHeroTitle.setForeground(Color.WHITE);
        lblHeroSub.setFont(Tema.FONT_BODY);
        lblHeroSub.setForeground(new Color(0xCBD5E1));

        Tema.styleSubTitle(lblCardTitle);
        Tema.styleBody(lblAsal);
        Tema.styleBody(lblTujuan);
        Tema.styleBody(lblPenumpang);
        Tema.styleSubTitle(lblQuickTitle);

        Tema.styleComboBox(cmbAsal);
        Tema.styleComboBox(cmbTujuan);
        spnPenumpang.setModel(new SpinnerNumberModel(1, 1, 4, 1));
        Tema.styleSpinner(spnPenumpang);

        Tema.styleSecondaryButton(btnTukar);
        Tema.styleAccentButton(btnCari);
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

        pnlHero = new javax.swing.JPanel();
        lblHeroTag = new javax.swing.JLabel();
        lblHeroTitle = new javax.swing.JLabel();
        lblHeroSub = new javax.swing.JLabel();
        pnlBody = new javax.swing.JPanel();
        pnlSearchCard = new javax.swing.JPanel();
        lblCardTitle = new javax.swing.JLabel();
        pnlRouteRow = new javax.swing.JPanel();
        pnlStations = new javax.swing.JPanel();
        pnlAsal = new javax.swing.JPanel();
        lblAsal = new javax.swing.JLabel();
        cmbAsal = new javax.swing.JComboBox<>();
        pnlTujuan = new javax.swing.JPanel();
        lblTujuan = new javax.swing.JLabel();
        cmbTujuan = new javax.swing.JComboBox<>();
        pnlSwapWrap = new javax.swing.JPanel();
        btnTukar = new javax.swing.JButton();
        pnlBottomSearch = new javax.swing.JPanel();
        pnlPenumpang = new javax.swing.JPanel();
        lblPenumpang = new javax.swing.JLabel();
        spnPenumpang = new javax.swing.JSpinner();
        btnCari = new javax.swing.JButton();
        pnlQuickCard = new javax.swing.JPanel();
        lblQuickTitle = new javax.swing.JLabel();
        pnlQuickButtons = new javax.swing.JPanel();
        btnRute1 = new javax.swing.JButton();
        btnRute2 = new javax.swing.JButton();
        btnRute3 = new javax.swing.JButton();
        btnRute4 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(0, 18));

        pnlHero.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblHeroTag.setText("ACCESS BY KAI • JALUR MADURA");
        pnlHero.add(lblHeroTag);

        lblHeroTitle.setText("Cari & Pesan Tiket Kereta Api");
        pnlHero.add(lblHeroTitle);

        lblHeroSub.setText("Pilih stasiun asal, tujuan, dan jumlah penumpang untuk melihat jadwal tersedia.");
        pnlHero.add(lblHeroSub);

        add(pnlHero, java.awt.BorderLayout.NORTH);

        pnlBody.setLayout(new java.awt.BorderLayout(0, 16));

        pnlSearchCard.setLayout(new java.awt.BorderLayout(0, 16));

        lblCardTitle.setText("Rencana Perjalanan");
        pnlSearchCard.add(lblCardTitle, java.awt.BorderLayout.NORTH);

        pnlRouteRow.setLayout(new java.awt.BorderLayout(12, 0));

        pnlStations.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        pnlAsal.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblAsal.setText("Stasiun Asal");
        pnlAsal.add(lblAsal);

        pnlAsal.add(cmbAsal);

        pnlStations.add(pnlAsal);

        pnlTujuan.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        lblTujuan.setText("Stasiun Tujuan");
        pnlTujuan.add(lblTujuan);

        pnlTujuan.add(cmbTujuan);

        pnlStations.add(pnlTujuan);

        pnlRouteRow.add(pnlStations, java.awt.BorderLayout.CENTER);

        pnlSwapWrap.setLayout(new java.awt.BorderLayout());

        btnTukar.setText("Tukar Asal-Tujuan");
        pnlSwapWrap.add(btnTukar, java.awt.BorderLayout.SOUTH);

        pnlRouteRow.add(pnlSwapWrap, java.awt.BorderLayout.EAST);

        pnlSearchCard.add(pnlRouteRow, java.awt.BorderLayout.CENTER);

        pnlBottomSearch.setLayout(new java.awt.BorderLayout(16, 0));

        pnlPenumpang.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        lblPenumpang.setText("Jumlah Penumpang (1-4):");
        pnlPenumpang.add(lblPenumpang);
        pnlPenumpang.add(spnPenumpang);

        pnlBottomSearch.add(pnlPenumpang, java.awt.BorderLayout.WEST);

        btnCari.setText("Cari Tiket Kereta");
        pnlBottomSearch.add(btnCari, java.awt.BorderLayout.EAST);

        pnlSearchCard.add(pnlBottomSearch, java.awt.BorderLayout.SOUTH);

        pnlBody.add(pnlSearchCard, java.awt.BorderLayout.NORTH);

        pnlQuickCard.setLayout(new java.awt.BorderLayout(0, 12));

        lblQuickTitle.setText("Rute Populer Jalur Madura");
        pnlQuickCard.add(lblQuickTitle, java.awt.BorderLayout.NORTH);

        pnlQuickButtons.setLayout(new java.awt.GridLayout(2, 2, 12, 12));

        btnRute1.setText("Sumenep (SMP) → Kamal (KML)");
        pnlQuickButtons.add(btnRute1);

        btnRute2.setText("Bangkalan (BKL) → Kalianget (KLA)");
        pnlQuickButtons.add(btnRute2);

        btnRute3.setText("Pamekasan (PMK) → Bangkalan (BKL)");
        pnlQuickButtons.add(btnRute3);

        btnRute4.setText("Kamal (KML) → Sampang (SPG)");
        pnlQuickButtons.add(btnRute4);

        pnlQuickCard.add(pnlQuickButtons, java.awt.BorderLayout.CENTER);

        pnlBody.add(pnlQuickCard, java.awt.BorderLayout.CENTER);

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
    private javax.swing.JLabel lblHeroSub;
    private javax.swing.JLabel lblHeroTag;
    private javax.swing.JLabel lblHeroTitle;
    private javax.swing.JLabel lblPenumpang;
    private javax.swing.JLabel lblQuickTitle;
    private javax.swing.JLabel lblTujuan;
    private javax.swing.JPanel pnlAsal;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlBottomSearch;
    private javax.swing.JPanel pnlHero;
    private javax.swing.JPanel pnlPenumpang;
    private javax.swing.JPanel pnlQuickButtons;
    private javax.swing.JPanel pnlQuickCard;
    private javax.swing.JPanel pnlRouteRow;
    private javax.swing.JPanel pnlSearchCard;
    private javax.swing.JPanel pnlStations;
    private javax.swing.JPanel pnlSwapWrap;
    private javax.swing.JPanel pnlTujuan;
    private javax.swing.JSpinner spnPenumpang;
    // End of variables declaration//GEN-END:variables
}
