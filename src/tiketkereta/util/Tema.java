package tiketkereta.util;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public final class Tema {

    public static final Color PRIMARY = new Color(0x1B365D);
    public static final Color PRIMARY_DARK = new Color(0x132846);
    public static final Color PRIMARY_HOVER = new Color(0x274A7E);
    public static final Color ACCENT = new Color(0xF37021);
    public static final Color ACCENT_HOVER = new Color(0xDC5E14);
    public static final Color CANVAS = new Color(0xF5F7FA);
    public static final Color SURFACE = new Color(0xFFFFFF);
    public static final Color BORDER = new Color(0xE2E8F0);
    public static final Color TEXT_PRIMARY = new Color(0x0F172A);
    public static final Color TEXT_MUTED = new Color(0x64748B);
    public static final Color STATUS_GREEN = new Color(0x16A34A);
    public static final Color STATUS_BLUE = new Color(0x2563EB);
    public static final Color STATUS_AMBER = new Color(0xD97706);
    public static final Color STATUS_RED = new Color(0xDC2626);
    public static final Color SEAT_OCCUPIED = new Color(0x94A3B8);
    public static final Color TABLE_ALT = new Color(0xF8FAFC);
    public static final Color TABLE_SELECT = new Color(0xDBEAFE);
    public static final Color SURFACE_SOFT = new Color(0xF8FAFC);
    public static final Color PRIMARY_SOFT = new Color(0xEFF6FF);
    public static final Color ACCENT_SOFT = new Color(0xFFF7ED);
    public static final Color SUCCESS_SOFT = new Color(0xDCFCE7);

    private static final String FONT_NAME = "Segoe UI";

    public static final Font FONT_HERO = new Font(FONT_NAME, Font.BOLD, s(26));
    public static final Font FONT_TITLE = new Font(FONT_NAME, Font.BOLD, s(22));
    public static final Font FONT_KPI = new Font(FONT_NAME, Font.BOLD, s(26));
    public static final Font FONT_SUBTITLE = new Font(FONT_NAME, Font.BOLD, s(16));
    public static final Font FONT_BODY = new Font(FONT_NAME, Font.PLAIN, s(14));
    public static final Font FONT_BODY_BOLD = new Font(FONT_NAME, Font.BOLD, s(14));
    public static final Font FONT_SMALL = new Font(FONT_NAME, Font.PLAIN, s(13));

    private Tema() {
    }

    public static int s(int px) {
        return UIScale.scale(px);
    }

    public static Dimension dim(int width, int height) {
        return new Dimension(s(width), s(height));
    }

    public static void applyGlobalDefaults() {
        UIManager.put("defaultFont", FONT_BODY);
        UIManager.put("OptionPane.messageFont", FONT_BODY);
        UIManager.put("OptionPane.buttonFont", FONT_BODY_BOLD);
    }

    public static void styleCanvas(JPanel panel) {
        panel.setBackground(CANVAS);
        panel.setBorder(BorderFactory.createEmptyBorder(s(22), s(26), s(22), s(26)));
    }

    public static void centerFlowPanel(JPanel wrapper, final JComponent card) {
        wrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0) {
            @Override
            public void layoutContainer(Container target) {
                synchronized (target.getTreeLock()) {
                    Dimension pref = card.getPreferredSize();
                    int x = Math.max(s(16), (target.getWidth() - pref.width) / 2);
                    int y = Math.max(s(16), (target.getHeight() - pref.height) / 2);
                    card.setBounds(x, y, pref.width, pref.height);
                }
            }
        });
    }

    public static void styleCard(JPanel panel) {
        styleCard(panel, 18, 22);
    }

    public static void styleCard(JPanel panel, int padV, int padH) {
        panel.setBackground(SURFACE);
        Border line = BorderFactory.createLineBorder(BORDER, 1);
        Border pad = BorderFactory.createEmptyBorder(s(padV), s(padH), s(padV), s(padH));
        panel.setBorder(BorderFactory.createCompoundBorder(line, pad));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
    }

    public static void styleHeroCard(JPanel panel) {
        panel.setBackground(PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(s(22), s(26), s(22), s(26)));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
    }

    public static void styleSoftCard(JPanel panel, int padV, int padH) {
        panel.setBackground(SURFACE_SOFT);
        Border line = BorderFactory.createLineBorder(BORDER, 1);
        Border pad = BorderFactory.createEmptyBorder(s(padV), s(padH), s(padV), s(padH));
        panel.setBorder(BorderFactory.createCompoundBorder(line, pad));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
    }

    public static void styleAccentCard(JPanel panel, int padV, int padH) {
        panel.setBackground(ACCENT_SOFT);
        Border line = BorderFactory.createLineBorder(new Color(0xFED7AA), 1);
        Border pad = BorderFactory.createEmptyBorder(s(padV), s(padH), s(padV), s(padH));
        panel.setBorder(BorderFactory.createCompoundBorder(line, pad));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
    }

    public static void styleStepItem(JLabel label, boolean active, boolean done) {
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(s(8), s(12), s(8), s(12)));
        if (active) {
            label.setFont(FONT_BODY_BOLD);
            label.setBackground(ACCENT);
            label.setForeground(Color.WHITE);
        } else if (done) {
            label.setFont(FONT_BODY_BOLD);
            label.setBackground(SUCCESS_SOFT);
            label.setForeground(STATUS_GREEN);
        } else {
            label.setFont(FONT_SMALL);
            label.setBackground(SURFACE_SOFT);
            label.setForeground(TEXT_MUTED);
        }
    }

    public static void styleFieldLabel(JLabel label) {
        label.setFont(FONT_BODY_BOLD);
        label.setForeground(TEXT_PRIMARY);
    }

    public static void styleBadge(JLabel label, Color bg, Color fg) {
        label.setFont(FONT_SMALL);
        label.setOpaque(true);
        label.setBackground(bg);
        label.setForeground(fg);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(s(4), s(10), s(4), s(10)));
    }

    public static void stylePageTitle(JLabel label) {
        label.setFont(FONT_TITLE);
        label.setForeground(PRIMARY);
    }

    public static void styleSubTitle(JLabel label) {
        label.setFont(FONT_SUBTITLE);
        label.setForeground(TEXT_PRIMARY);
    }

    public static void styleBody(JLabel label) {
        label.setFont(FONT_BODY);
        label.setForeground(TEXT_PRIMARY);
    }

    public static void styleMuted(JLabel label) {
        label.setFont(FONT_SMALL);
        label.setForeground(TEXT_MUTED);
    }

    public static void styleAccentButton(JButton button) {
        button.setFont(FONT_BODY_BOLD);
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setMinimumSize(dim(110, 40));
        button.putClientProperty(FlatClientProperties.STYLE,
                "arc: 10; borderWidth: 0; focusWidth: 1; margin: 9,18,9,18; "
                + "background: #F37021; foreground: #FFFFFF; hoverBackground: #DC5E14; pressedBackground: #C2510E");
    }

    public static void stylePrimaryButton(JButton button) {
        button.setFont(FONT_BODY_BOLD);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setMinimumSize(dim(100, 40));
        button.putClientProperty(FlatClientProperties.STYLE,
                "arc: 10; borderWidth: 0; focusWidth: 1; margin: 9,16,9,16; "
                + "background: #1B365D; foreground: #FFFFFF; hoverBackground: #274A7E; pressedBackground: #132846");
    }

    public static void styleSecondaryButton(JButton button) {
        button.setFont(FONT_BODY_BOLD);
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(SURFACE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setMinimumSize(dim(96, 40));
        button.putClientProperty(FlatClientProperties.STYLE,
                "arc: 10; borderWidth: 1; borderColor: #CBD5E1; focusWidth: 1; margin: 9,16,9,16; "
                + "background: #FFFFFF; foreground: #0F172A; hoverBackground: #F1F5F9");
    }

    public static void styleDangerButton(JButton button) {
        button.setFont(FONT_BODY_BOLD);
        button.setForeground(Color.WHITE);
        button.setBackground(STATUS_RED);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setMinimumSize(dim(96, 40));
        button.putClientProperty(FlatClientProperties.STYLE,
                "arc: 10; borderWidth: 0; focusWidth: 1; margin: 9,16,9,16; "
                + "background: #DC2626; foreground: #FFFFFF; hoverBackground: #B91C1C");
    }

    public static void styleSuccessButton(JButton button) {
        button.setFont(FONT_BODY_BOLD);
        button.setForeground(Color.WHITE);
        button.setBackground(STATUS_GREEN);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setMinimumSize(dim(100, 40));
        button.putClientProperty(FlatClientProperties.STYLE,
                "arc: 10; borderWidth: 0; focusWidth: 1; margin: 9,16,9,16; "
                + "background: #16A34A; foreground: #FFFFFF; hoverBackground: #15803D");
    }

    public static void styleSidebarButton(JButton button, boolean active) {
        button.setFont(active ? FONT_BODY_BOLD : FONT_BODY);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, s(44)));
        button.setPreferredSize(dim(220, 44));
        if (active) {
            button.putClientProperty(FlatClientProperties.STYLE,
                    "arc: 10; borderWidth: 0; focusWidth: 0; margin: 8,14,8,14; "
                    + "background: #F37021; foreground: #FFFFFF; hoverBackground: #F37021");
        } else {
            button.putClientProperty(FlatClientProperties.STYLE,
                    "arc: 10; borderWidth: 0; focusWidth: 0; margin: 8,14,8,14; "
                    + "background: #1B365D; foreground: #E2E8F0; hoverBackground: #274A7E");
        }
    }

    public static void styleTextField(JTextField field, String placeholder) {
        field.setFont(FONT_BODY);
        int width = Math.max(s(160), field.getPreferredSize().width);
        field.setPreferredSize(new Dimension(width, s(40)));
        field.setMinimumSize(dim(110, 40));
        field.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 5,10,5,10");
        if (placeholder != null) {
            field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        }
    }

    public static void styleTextArea(JTextArea area) {
        area.setFont(FONT_BODY);
        area.setMargin(new Insets(s(8), s(10), s(8), s(10)));
    }

    public static void styleComboBox(JComboBox<?> combo) {
        combo.setFont(FONT_BODY);
        int width = Math.max(s(130), combo.getPreferredSize().width);
        combo.setPreferredSize(new Dimension(width, s(40)));
        combo.setMinimumSize(dim(90, 40));
        combo.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
    }

    public static void styleSpinner(JSpinner spinner) {
        spinner.setFont(FONT_BODY_BOLD);
        spinner.setPreferredSize(dim(96, 40));
        spinner.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
    }

    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        scrollPane.getViewport().setBackground(SURFACE);
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(s(38));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(TABLE_SELECT);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setFillsViewportHeight(true);
        table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BODY_BOLD);
        header.setBackground(CANVAS);
        header.setForeground(PRIMARY);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, s(42)));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (comp instanceof JComponent) {
                    ((JComponent) comp).setBorder(BorderFactory.createEmptyBorder(0, s(10), 0, s(10)));
                }
                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? SURFACE : TABLE_ALT);
                    comp.setForeground(TEXT_PRIMARY);
                }
                return comp;
            }
        };
        table.setDefaultRenderer(Object.class, defaultRenderer);
    }

    public static DefaultTableCellRenderer createStatusRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(FONT_BODY_BOLD);
                label.setBorder(BorderFactory.createEmptyBorder(0, s(8), 0, s(8)));
                String status = value == null ? "" : value.toString();
                if (!isSelected) {
                    label.setBackground(row % 2 == 0 ? SURFACE : TABLE_ALT);
                }
                if ("Lunas".equalsIgnoreCase(status)) {
                    label.setForeground(STATUS_GREEN);
                } else if ("Dipesan".equalsIgnoreCase(status)) {
                    label.setForeground(STATUS_AMBER);
                } else if ("Dibatalkan".equalsIgnoreCase(status)) {
                    label.setForeground(STATUS_RED);
                } else {
                    label.setForeground(TEXT_PRIMARY);
                }
                return label;
            }
        };
    }

    public static Color warnaStatus(String status) {
        if ("Lunas".equalsIgnoreCase(status)) {
            return STATUS_GREEN;
        }
        if ("Dipesan".equalsIgnoreCase(status)) {
            return STATUS_AMBER;
        }
        if ("Dibatalkan".equalsIgnoreCase(status)) {
            return STATUS_RED;
        }
        return TEXT_PRIMARY;
    }
}
