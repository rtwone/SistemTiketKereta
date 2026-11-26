package tiketkereta.ui;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import tiketkereta.util.Tema;

public class SeatMapPanel extends JPanel {

    public interface SeatSelectionListener {
        void onSelectionChanged(Set<String> selectedSeats);
    }

    private final JPanel pnlGridHost;
    private final JLabel lblInfoGerbong;
    private int nomorGerbong;
    private int kapasitas;
    private Set<String> occupiedSeats;
    private Set<String> selectedSeats;
    private int maxSelection;
    private SeatSelectionListener listener;

    public SeatMapPanel() {
        this.occupiedSeats = new LinkedHashSet<String>();
        this.selectedSeats = new LinkedHashSet<String>();
        this.maxSelection = 1;

        setLayout(new BorderLayout(0, 12));
        setBackground(Tema.SURFACE);

        JPanel pnlTop = new JPanel(new BorderLayout(8, 8));
        pnlTop.setOpaque(false);

        lblInfoGerbong = new JLabel("Pilih gerbong untuk melihat denah kursi");
        Tema.styleSubTitle(lblInfoGerbong);
        pnlTop.add(lblInfoGerbong, BorderLayout.WEST);

        JPanel pnlLegend = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        pnlLegend.setOpaque(false);
        pnlLegend.add(buatLegendItem(Tema.STATUS_GREEN, "Kosong"));
        pnlLegend.add(buatLegendItem(Tema.STATUS_BLUE, "Dipilih"));
        pnlLegend.add(buatLegendItem(Tema.SEAT_OCCUPIED, "Terisi"));
        pnlTop.add(pnlLegend, BorderLayout.EAST);

        add(pnlTop, BorderLayout.NORTH);

        pnlGridHost = new JPanel(new GridBagLayout());
        pnlGridHost.setBackground(Tema.CANVAS);
        pnlGridHost.setBorder(BorderFactory.createEmptyBorder(Tema.s(14), Tema.s(14), Tema.s(14), Tema.s(14)));

        JScrollPane scrollPane = new JScrollPane(pnlGridHost);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        Tema.styleScrollPane(scrollPane);
        add(scrollPane, BorderLayout.CENTER);
    }

    public boolean pilihKursiOtomatis() {
        selectedSeats.clear();
        boolean layout33 = kapasitas == 90;
        String[] semuaHuruf = layout33
                ? new String[]{"A", "B", "C", "D", "E", "F"}
                : new String[]{"A", "B", "C", "D"};
        int jumlahBaris = hitungJumlahBaris();
        for (int baris = 1; baris <= jumlahBaris && selectedSeats.size() < maxSelection; baris++) {
            for (int i = 0; i < semuaHuruf.length && selectedSeats.size() < maxSelection; i++) {
                if (kapasitas == 50 && baris == 13 && i >= 2) {
                    continue;
                }
                String kode = "G" + nomorGerbong + "-" + baris + semuaHuruf[i];
                if (!occupiedSeats.contains(kode)) {
                    selectedSeats.add(kode);
                }
            }
        }
        renderGrid();
        if (listener != null) {
            listener.onSelectionChanged(selectedSeats);
        }
        return selectedSeats.size() == maxSelection;
    }

    public void resetPilihanKursi() {
        selectedSeats.clear();
        renderGrid();
        if (listener != null) {
            listener.onSelectionChanged(selectedSeats);
        }
    }

    public void setSeatSelectionListener(SeatSelectionListener listener) {
        this.listener = listener;
    }

    public void configure(int nomorGerbong, String namaKelas, int kapasitas,
            Set<String> occupiedSeats, Set<String> selectedSeats, int maxSelection) {
        this.nomorGerbong = nomorGerbong;
        this.kapasitas = kapasitas;
        this.occupiedSeats = occupiedSeats == null ? new LinkedHashSet<String>() : occupiedSeats;
        this.selectedSeats = selectedSeats == null ? new LinkedHashSet<String>() : selectedSeats;
        this.maxSelection = Math.max(1, maxSelection);
        this.lblInfoGerbong.setText("Gerbong " + nomorGerbong + " (" + namaKelas + " - " + kapasitas + " Kursi)");
        renderGrid();
    }

    private void renderGrid() {
        pnlGridHost.removeAll();
        boolean layout33 = kapasitas == 90;
        String[] hurufKiri = layout33 ? new String[]{"A", "B", "C"} : new String[]{"A", "B"};
        String[] hurufKanan = layout33 ? new String[]{"D", "E", "F"} : new String[]{"C", "D"};
        int jumlahBaris = hitungJumlahBaris();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(Tema.s(4), Tema.s(4), Tema.s(4), Tema.s(4));
        gbc.fill = GridBagConstraints.NONE;

        int totalKolom = 2 + hurufKiri.length + hurufKanan.length;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = totalKolom;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblDepan = new JLabel("▲  ARAH DEPAN KERETA (LOKOMOTIF)  ▲", SwingConstants.CENTER);
        Tema.styleBadge(lblDepan, Tema.PRIMARY_SOFT, Tema.PRIMARY);
        lblDepan.setFont(Tema.FONT_BODY_BOLD);
        pnlGridHost.add(lblDepan, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        tambahHeaderKolom(gbc, hurufKiri, hurufKanan);

        for (int baris = 1; baris <= jumlahBaris; baris++) {
            int col = 0;
            gbc.gridy = baris + 1;

            gbc.gridx = col++;
            JLabel lblRow = new JLabel(String.valueOf(baris), SwingConstants.CENTER);
            lblRow.setFont(Tema.FONT_BODY_BOLD);
            lblRow.setForeground(Tema.TEXT_MUTED);
            lblRow.setPreferredSize(Tema.dim(32, 38));
            pnlGridHost.add(lblRow, gbc);

            for (String h : hurufKiri) {
                gbc.gridx = col++;
                pnlGridHost.add(buatTombolKursi(baris, h), gbc);
            }

            gbc.gridx = col++;
            JLabel lorong = new JLabel("", SwingConstants.CENTER);
            lorong.setPreferredSize(Tema.dim(28, 38));
            pnlGridHost.add(lorong, gbc);

            boolean barisTerakhir50 = (kapasitas == 50 && baris == 13);
            for (String h : hurufKanan) {
                gbc.gridx = col++;
                if (barisTerakhir50) {
                    JLabel kosong = new JLabel("");
                    kosong.setPreferredSize(Tema.dim(58, 38));
                    pnlGridHost.add(kosong, gbc);
                } else {
                    pnlGridHost.add(buatTombolKursi(baris, h), gbc);
                }
            }
        }

        pnlGridHost.revalidate();
        pnlGridHost.repaint();
    }

    private void tambahHeaderKolom(GridBagConstraints gbc, String[] kiri, String[] kanan) {
        int col = 0;
        gbc.gridy = 1;
        gbc.gridx = col++;
        pnlGridHost.add(buatLabelHeader("Baris"), gbc);
        for (String h : kiri) {
            gbc.gridx = col++;
            pnlGridHost.add(buatLabelHeader(h), gbc);
        }
        gbc.gridx = col++;
        pnlGridHost.add(buatLabelHeader(""), gbc);
        for (String h : kanan) {
            gbc.gridx = col++;
            pnlGridHost.add(buatLabelHeader(h), gbc);
        }
    }

    private JLabel buatLabelHeader(String teks) {
        JLabel label = new JLabel(teks, SwingConstants.CENTER);
        label.setFont(Tema.FONT_BODY_BOLD);
        label.setForeground(Tema.PRIMARY);
        return label;
    }

    private int hitungJumlahBaris() {
        switch (kapasitas) {
            case 50:
                return 13;
            case 64:
                return 16;
            case 80:
                return 20;
            case 90:
                return 15;
            default:
                return Math.max(1, (int) Math.ceil(kapasitas / 4.0));
        }
    }

    private JButton buatTombolKursi(int baris, String huruf) {
        final String kodeKursi = "G" + nomorGerbong + "-" + baris + huruf;
        final JButton btn = new JButton(baris + huruf);
        btn.setFont(Tema.FONT_BODY_BOLD);
        btn.setPreferredSize(Tema.dim(58, 38));
        btn.setFocusPainted(false);
        btn.setToolTipText(kodeKursi);

        boolean terisi = occupiedSeats.contains(kodeKursi);
        boolean dipilih = selectedSeats.contains(kodeKursi);

        if (terisi) {
            btn.setEnabled(false);
            btn.putClientProperty(FlatClientProperties.STYLE,
                    "arc: 8; borderWidth: 0; background: #94A3B8; foreground: #FFFFFF; disabledBackground: #94A3B8; disabledText: #FFFFFF");
        } else {
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            terapkanWarnaTombol(btn, dipilih);
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    toggleKursi(kodeKursi, btn);
                }
            });
        }
        return btn;
    }

    private void toggleKursi(String kodeKursi, JButton btn) {
        if (selectedSeats.contains(kodeKursi)) {
            selectedSeats.remove(kodeKursi);
            terapkanWarnaTombol(btn, false);
        } else {
            if (selectedSeats.size() >= maxSelection) {
                JOptionPane.showMessageDialog(this,
                        "Anda sudah memilih " + maxSelection + " kursi. Klik kursi biru terlebih dahulu untuk mengganti pilihan.",
                        "Batas Pilihan Kursi",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            selectedSeats.add(kodeKursi);
            terapkanWarnaTombol(btn, true);
        }
        if (listener != null) {
            listener.onSelectionChanged(selectedSeats);
        }
    }

    private void terapkanWarnaTombol(JButton btn, boolean dipilih) {
        if (dipilih) {
            btn.putClientProperty(FlatClientProperties.STYLE,
                    "arc: 8; borderWidth: 0; focusWidth: 0; background: #2563EB; foreground: #FFFFFF; hoverBackground: #1D4ED8");
        } else {
            btn.putClientProperty(FlatClientProperties.STYLE,
                    "arc: 8; borderWidth: 0; focusWidth: 0; background: #16A34A; foreground: #FFFFFF; hoverBackground: #15803D");
        }
    }

    private JPanel buatLegendItem(Color warna, String teks) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        item.setOpaque(false);
        JPanel kotak = new JPanel();
        kotak.setPreferredSize(Tema.dim(16, 16));
        kotak.setBackground(warna);
        kotak.putClientProperty(FlatClientProperties.STYLE, "arc: 4");
        JLabel lbl = new JLabel(teks);
        lbl.setFont(Tema.FONT_SMALL);
        lbl.setForeground(Tema.TEXT_MUTED);
        item.add(kotak);
        item.add(lbl);
        return item;
    }
}
