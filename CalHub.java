// CalHub
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class CalHub extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private static HistoryPanel historyPanel;
    // Original color constants for light mode
    private static final Color PRIMARY = new Color(44, 62, 80);
    private static final Color ACCENT = new Color(52, 152, 219);
    private static final Color BG = new Color(236, 240, 241);
    private static final Color BTN_BG = new Color(236, 240, 241);
    private static final Color BTN_FG = new Color(44, 62, 80);
    // Dark theme colors (matching screenshot)
    private static final Color DARK_PRIMARY = new Color(35, 35, 35); // Sidebar/topbar
    private static final Color DARK_ACCENT = new Color(80, 80, 80); // Button hover
    private static final Color DARK_BG = new Color(35, 35, 35);     // Main background
    private static final Color DARK_BTN_BG = new Color(100, 100, 100); // Button background
    private static final Color DARK_BTN_FG = Color.WHITE;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 30);
    private static final Font NAV_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font BTN_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 20);
    private ScientificCalculatorPanel scientificCalculatorPanel; // Store reference
    private SimpleCalculatorPanel simpleCalculatorPanel; // Store reference

    public CalHub() {
        setTitle("CalHub - Multi-Functional Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG); // Default to light mode

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(PRIMARY); // Will be updated by theme
        JLabel titleLabel = new JLabel("CalHub", SwingConstants.LEFT);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        JLabel versionLabel = new JLabel("v1.0", SwingConstants.RIGHT);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        versionLabel.setForeground(Color.WHITE);
        JToggleButton themeToggle = new JToggleButton("Dark Mode");
        themeToggle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        themeToggle.setBackground(ACCENT);
        themeToggle.setForeground(Color.WHITE);
        themeToggle.setFocusPainted(false);
        themeToggle.addActionListener(e -> {
            boolean dark = themeToggle.isSelected();
            applyTheme(dark);
            themeToggle.setText(dark ? "Light Mode" : "Dark Mode");
        });
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(versionLabel);
        rightPanel.add(themeToggle);
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(topPanel, BorderLayout.NORTH);

        // Navigation panel (sidebar)
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(10, 1, 10, 10));
        navPanel.setBackground(PRIMARY); // Will be updated by theme
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        String[] modules = {
            "Simple Calculator", "Scientific Calculator", "Currency Converter", "BMI Calculator",
            "Age Calculator", "Unit Converter", "EMI Calculator", "Discount Calculator", "History Log"
        };
        JButton[] navButtons = new JButton[modules.length];
        for (int i = 0; i < modules.length; i++) {
            navButtons[i] = new JButton(modules[i]);
            navButtons[i].setFont(NAV_FONT);
            navButtons[i].setBackground(PRIMARY);
            navButtons[i].setForeground(Color.WHITE);
            navButtons[i].setFocusPainted(false);
            navButtons[i].setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            navButtons[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            final int idx = i;
            navButtons[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    navButtons[idx].setBackground(ACCENT);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    navButtons[idx].setBackground(PRIMARY);
                }
            });
            navPanel.add(navButtons[i]);
        }
        add(navPanel, BorderLayout.WEST);

        // Main content area with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BG);
        simpleCalculatorPanel = new SimpleCalculatorPanel();
        mainPanel.add(simpleCalculatorPanel, "Simple Calculator");
        scientificCalculatorPanel = new ScientificCalculatorPanel();
        mainPanel.add(scientificCalculatorPanel, "Scientific Calculator");
        mainPanel.add(new CurrencyConverterPanel(), "Currency Converter");
        mainPanel.add(new BMICalculatorPanel(), "BMI Calculator");
        mainPanel.add(new AgeCalculatorPanel(), "Age Calculator");
        mainPanel.add(new UnitConverterPanel(), "Unit Converter");
        mainPanel.add(new EMICalculatorPanel(), "EMI Calculator");
        mainPanel.add(new DiscountCalculatorPanel(), "Discount Calculator");
        historyPanel = new HistoryPanel();
        mainPanel.add(historyPanel, "History Log");
        add(mainPanel, BorderLayout.CENTER);

        // Button actions to switch panels
        for (int i = 0; i < modules.length; i++) {
            final String module = modules[i];
            navButtons[i].addActionListener(e -> cardLayout.show(mainPanel, module));
        }

        // Apply default theme (light)
        applyTheme(false);
    }

    public static void addToHistory(String entry) {
        if (historyPanel != null) {
            historyPanel.log(entry);
        }
    }

    // Theme application method
    private void applyTheme(boolean dark) {
        // Top bar
        Component topPanel = getContentPane().getComponent(0);
        if (topPanel instanceof JPanel) {
            topPanel.setBackground(dark ? DARK_PRIMARY : PRIMARY);
            Component[] topComps = ((JPanel) topPanel).getComponents();
            for (Component c : topComps) {
                if (c instanceof JLabel) {
                    c.setForeground(Color.WHITE);
                } else if (c instanceof JPanel) {
                    c.setBackground(dark ? DARK_PRIMARY : PRIMARY);
                    for (Component btn : ((JPanel) c).getComponents()) {
                        if (btn instanceof JToggleButton) {
                            btn.setBackground(dark ? DARK_ACCENT : ACCENT);
                            btn.setForeground(Color.WHITE);
                        } else if (btn instanceof JLabel) {
                            btn.setForeground(Color.WHITE);
                        }
                    }
                }
            }
        }
        // Sidebar
        Component navPanel = getContentPane().getComponent(1);
        if (navPanel instanceof JPanel) {
            navPanel.setBackground(dark ? DARK_PRIMARY : PRIMARY);
            for (Component btn : ((JPanel) navPanel).getComponents()) {
                if (btn instanceof JButton) {
                    btn.setBackground(dark ? DARK_PRIMARY : PRIMARY);
                    btn.setForeground(Color.WHITE);
                    ((JButton) btn).addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            btn.setBackground(dark ? DARK_ACCENT : ACCENT);
                        }
                        public void mouseExited(java.awt.event.MouseEvent evt) {
                            btn.setBackground(dark ? DARK_PRIMARY : PRIMARY);
                        }
                    });
                }
            }
        }
        // Main content
        mainPanel.setBackground(dark ? DARK_BG : BG);
        updateComponentTreeUI(mainPanel, dark);
        getContentPane().setBackground(dark ? DARK_BG : BG);
        // Update scientific calculator theme state and button colors
        if (scientificCalculatorPanel != null) {
            scientificCalculatorPanel.setDarkMode(dark);
            scientificCalculatorPanel.updateButtonColors();
        }
        // Update simple calculator theme state and button colors
        if (simpleCalculatorPanel != null) {
            simpleCalculatorPanel.setDarkMode(dark);
            simpleCalculatorPanel.updateButtonColors();
        }
        repaint();
    }

    // Recursively update all components in the main panel
    private void updateComponentTreeUI(Component comp, boolean dark) {
        if (comp instanceof JPanel || comp instanceof JFrame) {
            comp.setBackground(dark ? DARK_BG : BG);
        }
        if (comp instanceof JButton) {
            if (dark) {
                comp.setBackground(DARK_BTN_BG);
                comp.setForeground(DARK_BTN_FG);
            } else {
                JButton btn = (JButton) comp;
                // Accent buttons (like Calculate, Convert, Export, etc.)
                String text = btn.getText().toLowerCase();
                if (text.contains("calculate") || text.contains("convert") || text.contains("export") || text.contains("swap") || text.contains("=") || text.contains("log")) {
                    btn.setBackground(ACCENT);
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setBackground(BTN_BG);
                    btn.setForeground(BTN_FG);
                }
            }
        }
        if (comp instanceof JLabel) {
            if (dark) {
                comp.setForeground(Color.WHITE);
            } else {
                JLabel label = (JLabel) comp;
                // Section labels (like field names) use PRIMARY, result labels use ACCENT
                String text = label.getText().toLowerCase();
                if (text.contains(":") || text.contains("date of birth") || text.contains("type") || text.contains("from") || text.contains("to") || text.contains("value") || text.contains("price") || text.contains("discount") || text.contains("principal") || text.contains("rate") || text.contains("tenure") || text.contains("height") || text.contains("weight") || text.contains("amount")) {
                    label.setForeground(PRIMARY);
                } else if (text.contains("bmi") || text.contains("emi") || text.contains("final price") || text.contains("years") || text.contains("months") || text.contains("days") || text.contains("history log")) {
                    label.setForeground(ACCENT);
                } else {
                    label.setForeground(PRIMARY);
                }
            }
        }
        if (comp instanceof JTextField || comp instanceof JTextArea) {
            comp.setBackground(dark ? DARK_BTN_BG : BTN_BG);
            comp.setForeground(dark ? DARK_BTN_FG : BTN_FG);
            if (comp instanceof JTextField) {
                ((JTextField) comp).setCaretColor(dark ? Color.WHITE : Color.BLACK);
            }
        }
        if (comp instanceof JComboBox) {
            comp.setBackground(dark ? DARK_BTN_BG : BTN_BG);
            comp.setForeground(dark ? DARK_BTN_FG : BTN_FG);
        }
        if (comp instanceof JScrollPane) {
            comp.setBackground(dark ? DARK_BG : BG);
        }
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                updateComponentTreeUI(child, dark);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CalHub().setVisible(true);
        });
    }

    // Utility: Add context menu for copy/paste to a JTextComponent
    private static void addCopyPasteMenu(JTextComponent field) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");
        copy.addActionListener(e -> field.copy());
        paste.addActionListener(e -> field.paste());
        menu.add(copy);
        menu.add(paste);
        field.setComponentPopupMenu(menu);
    }

    // BMI Calculator Panel
    private static class BMICalculatorPanel extends JPanel {
        private final JTextField heightField = new JTextField(10);
        private final JTextField weightField = new JTextField(10);
        private final JLabel resultLabel = new JLabel(" ");
        private final JButton calcBtn = new JButton("Calculate BMI");
        BMICalculatorPanel() {
            setLayout(new GridBagLayout());
            setBackground(BG);
            setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(8, 8, 8, 8);
            c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_END;
            JLabel hLabel = new JLabel("Height (cm):"); hLabel.setFont(FIELD_FONT); hLabel.setForeground(PRIMARY);
            add(hLabel, c);
            c.gridy++;
            JLabel wLabel = new JLabel("Weight (kg):"); wLabel.setFont(FIELD_FONT); wLabel.setForeground(PRIMARY);
            add(wLabel, c);
            c.gridx = 1; c.gridy = 0; c.anchor = GridBagConstraints.LINE_START;
            heightField.setFont(FIELD_FONT); heightField.setBackground(BTN_BG); heightField.setForeground(BTN_FG);
            add(heightField, c);
            c.gridy++;
            weightField.setFont(FIELD_FONT); weightField.setBackground(BTN_BG); weightField.setForeground(BTN_FG);
            add(weightField, c);
            c.gridx = 0; c.gridy++; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
            calcBtn.setFont(BTN_FONT); calcBtn.setBackground(ACCENT); calcBtn.setForeground(Color.WHITE);
            add(calcBtn, c);
            c.gridy++;
            resultLabel.setFont(BTN_FONT); resultLabel.setForeground(ACCENT);
            add(resultLabel, c);
            calcBtn.addActionListener(e -> calculate());
            heightField.addActionListener(e -> calculate());
            weightField.addActionListener(e -> calculate());
            addCopyPasteMenu(heightField);
            addCopyPasteMenu(weightField);
        }
        private void calculate() {
            if (heightField.getText().isEmpty() || weightField.getText().isEmpty()) {
                resultLabel.setText("Please enter both height and weight");
                return;
            }
            try {
                double h = Double.parseDouble(heightField.getText()) / 100.0;
                double w = Double.parseDouble(weightField.getText());
                double bmi = w / (h * h);
                String cat = bmi < 18.5 ? "Underweight" : bmi < 25 ? "Normal" : bmi < 30 ? "Overweight" : "Obese";
                String res = String.format("BMI: %.2f (%s)", bmi, cat);
                resultLabel.setText(res);
                CalHub.addToHistory("BMI: Height=" + heightField.getText() + "cm, Weight=" + weightField.getText() + "kg → " + res);
            } catch (Exception ex) {
                resultLabel.setText("Invalid input");
            }
        }
    }
    // Age Calculator Panel
    private static class AgeCalculatorPanel extends JPanel {
        private final JTextField dobField = new JTextField(12);
        private final JLabel resultLabel = new JLabel(" ");
        private final JButton calcBtn = new JButton("Calculate Age");
        AgeCalculatorPanel() {
            setLayout(new GridBagLayout());
            setBackground(BG);
            setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(8, 8, 8, 8);
            c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_END;
            JLabel dobLabel = new JLabel("Date of Birth (yyyy-mm-dd):"); dobLabel.setFont(FIELD_FONT); dobLabel.setForeground(PRIMARY);
            add(dobLabel, c);
            c.gridx = 1; c.anchor = GridBagConstraints.LINE_START;
            dobField.setFont(FIELD_FONT); dobField.setBackground(BTN_BG); dobField.setForeground(BTN_FG);
            add(dobField, c);
            c.gridx = 0; c.gridy++; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
            calcBtn.setFont(BTN_FONT); calcBtn.setBackground(ACCENT); calcBtn.setForeground(Color.WHITE);
            add(calcBtn, c);
            c.gridy++;
            resultLabel.setFont(BTN_FONT); resultLabel.setForeground(ACCENT);
            add(resultLabel, c);
            calcBtn.addActionListener(e -> calculate());
            dobField.addActionListener(e -> calculate());
            addCopyPasteMenu(dobField);
        }
        private void calculate() {
            if (dobField.getText().isEmpty()) {
                resultLabel.setText("Please enter a date of birth");
                return;
            }
            try {
                LocalDate dob = LocalDate.parse(dobField.getText());
                LocalDate now = LocalDate.now();
                Period p = Period.between(dob, now);
                String res = String.format("%d years, %d months, %d days", p.getYears(), p.getMonths(), p.getDays());
                resultLabel.setText(res);
                CalHub.addToHistory("Age: DOB=" + dobField.getText() + " → " + res);
            } catch (Exception ex) {
                resultLabel.setText("Invalid date");
            }
        }
    }
    // Unit Converter Panel
    private static class UnitConverterPanel extends JPanel {
        private final JComboBox<String> typeBox = new JComboBox<>(new String[]{"Length", "Weight", "Temperature"});
        private final JComboBox<String> fromBox = new JComboBox<>();
        private final JComboBox<String> toBox = new JComboBox<>();
        private final JTextField inputField = new JTextField(10);
        private final JLabel resultLabel = new JLabel(" ");
        private final JButton convertBtn = new JButton("Convert");
        private static final Map<String, String[]> units = Map.of(
            "Length", new String[]{"Meter", "Kilometer", "Centimeter", "Mile", "Yard", "Foot", "Inch"},
            "Weight", new String[]{"Kilogram", "Gram", "Pound", "Ounce"},
            "Temperature", new String[]{"Celsius", "Fahrenheit", "Kelvin"}
        );
        UnitConverterPanel() {
            setLayout(new GridBagLayout());
            setBackground(BG);
            setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(8, 8, 8, 8);
            c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_END;
            JLabel typeLabel = new JLabel("Type:"); typeLabel.setFont(FIELD_FONT); typeLabel.setForeground(PRIMARY);
            add(typeLabel, c);
            c.gridx = 1; c.anchor = GridBagConstraints.LINE_START;
            typeBox.setFont(FIELD_FONT); typeBox.setBackground(BTN_BG); typeBox.setForeground(BTN_FG);
            add(typeBox, c);
            c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.LINE_END;
            JLabel fromLabel = new JLabel("From:"); fromLabel.setFont(FIELD_FONT); fromLabel.setForeground(PRIMARY);
            add(fromLabel, c);
            c.gridx = 1; c.anchor = GridBagConstraints.LINE_START;
            fromBox.setFont(FIELD_FONT); fromBox.setBackground(BTN_BG); fromBox.setForeground(BTN_FG);
            add(fromBox, c);
            c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.LINE_END;
            JLabel toLabel = new JLabel("To:"); toLabel.setFont(FIELD_FONT); toLabel.setForeground(PRIMARY);
            add(toLabel, c);
            c.gridx = 1; c.anchor = GridBagConstraints.LINE_START;
            toBox.setFont(FIELD_FONT); toBox.setBackground(BTN_BG); toBox.setForeground(BTN_FG);
            add(toBox, c);
            c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.LINE_END;
            JLabel valLabel = new JLabel("Value:"); valLabel.setFont(FIELD_FONT); valLabel.setForeground(PRIMARY);
            add(valLabel, c);
            c.gridx = 1; c.anchor = GridBagConstraints.LINE_START;
            inputField.setFont(FIELD_FONT); inputField.setBackground(BTN_BG); inputField.setForeground(BTN_FG);
            add(inputField, c);
            c.gridx = 0; c.gridy++; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
            convertBtn.setFont(BTN_FONT); convertBtn.setBackground(ACCENT); convertBtn.setForeground(Color.WHITE);
            add(convertBtn, c);
            c.gridy++;
            resultLabel.setFont(BTN_FONT); resultLabel.setForeground(ACCENT);
            add(resultLabel, c);
            typeBox.addActionListener(e -> updateUnits());
            convertBtn.addActionListener(e -> convert());
            inputField.addActionListener(e -> convert());
            updateUnits();
            addCopyPasteMenu(inputField);
        }
        private void updateUnits() {
            String type = (String) typeBox.getSelectedItem();
            fromBox.setModel(new DefaultComboBoxModel<>(units.get(type)));
            toBox.setModel(new DefaultComboBoxModel<>(units.get(type)));
        }
        private void convert() {
            if (inputField.getText().isEmpty()) {
                resultLabel.setText("Please enter a value");
                return;
            }
            try {
                String type = (String) typeBox.getSelectedItem();
                String from = (String) fromBox.getSelectedItem();
                String to = (String) toBox.getSelectedItem();
                double val = Double.parseDouble(inputField.getText());
                double res = switch (type) {
                    case "Length" -> convertLength(val, from, to);
                    case "Weight" -> convertWeight(val, from, to);
                    case "Temperature" -> convertTemp(val, from, to);
                    default -> throw new IllegalArgumentException();
                };
                String resultStr = String.format("%.4f %s", res, to);
                resultLabel.setText(resultStr);
                CalHub.addToHistory("Unit: " + val + " " + from + " → " + resultStr);
            } catch (Exception ex) {
                resultLabel.setText("Invalid input");
            }
        }
        private double convertLength(double v, String from, String to) {
            double m = switch (from) {
                case "Meter" -> v;
                case "Kilometer" -> v * 1000;
                case "Centimeter" -> v / 100;
                case "Mile" -> v * 1609.344;
                case "Yard" -> v * 0.9144;
                case "Foot" -> v * 0.3048;
                case "Inch" -> v * 0.0254;
                default -> throw new IllegalArgumentException();
            };
            return switch (to) {
                case "Meter" -> m;
                case "Kilometer" -> m / 1000;
                case "Centimeter" -> m * 100;
                case "Mile" -> m / 1609.344;
                case "Yard" -> m / 0.9144;
                case "Foot" -> m / 0.3048;
                case "Inch" -> m / 0.0254;
                default -> throw new IllegalArgumentException();
            };
        }
        private double convertWeight(double v, String from, String to) {
            double kg = switch (from) {
                case "Kilogram" -> v;
                case "Gram" -> v / 1000;
                case "Pound" -> v * 0.45359237;
                case "Ounce" -> v * 0.0283495231;
                default -> throw new IllegalArgumentException();
            };
            return switch (to) {
                case "Kilogram" -> kg;
                case "Gram" -> kg * 1000;
                case "Pound" -> kg / 0.45359237;
                case "Ounce" -> kg / 0.0283495231;
                default -> throw new IllegalArgumentException();
            };
        }
        private double convertTemp(double v, String from, String to) {
            double c = switch (from) {
                case "Celsius" -> v;
                case "Fahrenheit" -> (v - 32) * 5 / 9;
                case "Kelvin" -> v - 273.15;
                default -> throw new IllegalArgumentException();
            };
            return switch (to) {
                case "Celsius" -> c;
                case "Fahrenheit" -> c * 9 / 5 + 32;
                case "Kelvin" -> c + 273.15;
                default -> throw new IllegalArgumentException();
            };
        }
    }
    // EMI Calculator Panel
    private static class EMICalculatorPanel extends JPanel {
        private final JTextField principalField = new JTextField(12);
        private final JTextField rateField = new JTextField(12);
        private final JTextField tenureField = new JTextField(12);
        private final JLabel resultLabel = new JLabel(" ");
        private final JButton calcBtn = new JButton("Calculate EMI");
        EMICalculatorPanel() {
            setLayout(new GridBagLayout());
            setBackground(BG);
            setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(8, 8, 8, 8);
            c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_END;
            JLabel pLabel = new JLabel("Principal:"); pLabel.setFont(FIELD_FONT); pLabel.setForeground(PRIMARY);
            add(pLabel, c);
            c.gridy++;
            JLabel rLabel = new JLabel("Rate (% p.a.):"); rLabel.setFont(FIELD_FONT); rLabel.setForeground(PRIMARY);
            add(rLabel, c);
            c.gridy++;
            JLabel tLabel = new JLabel("Tenure (months):"); tLabel.setFont(FIELD_FONT); tLabel.setForeground(PRIMARY);
            add(tLabel, c);
            c.gridx = 1; c.gridy = 0; c.anchor = GridBagConstraints.LINE_START;
            principalField.setFont(FIELD_FONT); principalField.setBackground(BTN_BG); principalField.setForeground(BTN_FG);
            add(principalField, c);
            c.gridy++;
            rateField.setFont(FIELD_FONT); rateField.setBackground(BTN_BG); rateField.setForeground(BTN_FG);
            add(rateField, c);
            c.gridy++;
            tenureField.setFont(FIELD_FONT); tenureField.setBackground(BTN_BG); tenureField.setForeground(BTN_FG);
            add(tenureField, c);
            c.gridx = 0; c.gridy++; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
            calcBtn.setFont(BTN_FONT); calcBtn.setBackground(ACCENT); calcBtn.setForeground(Color.WHITE);
            add(calcBtn, c);
            c.gridy++;
            resultLabel.setFont(BTN_FONT); resultLabel.setForeground(ACCENT);
            add(resultLabel, c);
            calcBtn.addActionListener(e -> calculate());
            principalField.addActionListener(e -> calculate());
            rateField.addActionListener(e -> calculate());
            tenureField.addActionListener(e -> calculate());
            addCopyPasteMenu(principalField);
            addCopyPasteMenu(rateField);
            addCopyPasteMenu(tenureField);
        }
        private void calculate() {
            if (principalField.getText().isEmpty() || rateField.getText().isEmpty() || tenureField.getText().isEmpty()) {
                resultLabel.setText("Please enter all fields");
                return;
            }
            try {
                double p = Double.parseDouble(principalField.getText());
                double r = Double.parseDouble(rateField.getText()) / 12 / 100;
                int n = Integer.parseInt(tenureField.getText());
                double emi = (p * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);
                String res = String.format("EMI: %.2f", emi);
                resultLabel.setText(res);
                CalHub.addToHistory("EMI: Principal=" + principalField.getText() + ", Rate=" + rateField.getText() + ", Tenure=" + tenureField.getText() + " → " + res);
            } catch (Exception ex) {
                resultLabel.setText("Invalid input");
            }
        }
    }
    // Discount Calculator Panel
    private static class DiscountCalculatorPanel extends JPanel {
        private final JTextField priceField = new JTextField(10);
        private final JTextField discountField = new JTextField(10);
        private final JLabel resultLabel = new JLabel(" ");
        private final JButton calcBtn = new JButton("Calculate");
        DiscountCalculatorPanel() {
            setLayout(new GridBagLayout());
            setBackground(BG);
            setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(8, 8, 8, 8);
            c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_END;
            JLabel pLabel = new JLabel("Price:"); pLabel.setFont(FIELD_FONT); pLabel.setForeground(PRIMARY);
            add(pLabel, c);
            c.gridy++;
            JLabel dLabel = new JLabel("Discount (%):"); dLabel.setFont(FIELD_FONT); dLabel.setForeground(PRIMARY);
            add(dLabel, c);
            c.gridx = 1; c.gridy = 0; c.anchor = GridBagConstraints.LINE_START;
            priceField.setFont(FIELD_FONT); priceField.setBackground(BTN_BG); priceField.setForeground(BTN_FG);
            add(priceField, c);
            c.gridy++;
            discountField.setFont(FIELD_FONT); discountField.setBackground(BTN_BG); discountField.setForeground(BTN_FG);
            add(discountField, c);
            c.gridx = 0; c.gridy++; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
            calcBtn.setFont(BTN_FONT); calcBtn.setBackground(ACCENT); calcBtn.setForeground(Color.WHITE);
            add(calcBtn, c);
            c.gridy++;
            resultLabel.setFont(BTN_FONT); resultLabel.setForeground(ACCENT);
            add(resultLabel, c);
            calcBtn.addActionListener(e -> calculate());
            priceField.addActionListener(e -> calculate());
            discountField.addActionListener(e -> calculate());
            addCopyPasteMenu(priceField);
            addCopyPasteMenu(discountField);
        }
        private void calculate() {
            if (priceField.getText().isEmpty() || discountField.getText().isEmpty()) {
                resultLabel.setText("Please enter both price and discount");
                return;
            }
            try {
                double price = Double.parseDouble(priceField.getText());
                double disc = Double.parseDouble(discountField.getText());
                double finalPrice = price * (1 - disc / 100);
                String res = String.format("Final Price: %.2f", finalPrice);
                resultLabel.setText(res);
                CalHub.addToHistory("Discount: Price=" + priceField.getText() + ", Discount=" + discountField.getText() + "% → " + res);
            } catch (Exception ex) {
                resultLabel.setText("Invalid input");
            }
        }
    }
    // History Log Panel
    private static class HistoryPanel extends JPanel {
        private final JTextArea area = new JTextArea(18, 32);
        private String lastEntry = "";
        HistoryPanel() {
            setLayout(new BorderLayout());
            setBackground(BG);
            area.setFont(FIELD_FONT);
            area.setBackground(BTN_BG);
            area.setForeground(BTN_FG);
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);
            scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT), "History Log", 0, 0, NAV_FONT, ACCENT));
            add(scroll, BorderLayout.CENTER);
            JButton exportBtn = new JButton("Export History");
            exportBtn.setFont(BTN_FONT.deriveFont(14f));
            exportBtn.setBackground(ACCENT);
            exportBtn.setForeground(Color.WHITE);
            exportBtn.setFocusPainted(false);
            exportBtn.addActionListener(e -> exportHistory());
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.setBackground(BG);
            btnPanel.add(exportBtn);
            add(btnPanel, BorderLayout.SOUTH);
        }
        void log(String entry) {
            if (!entry.equals(lastEntry)) {
                area.append(entry + "\n");
                area.setCaretPosition(area.getDocument().getLength());
                lastEntry = entry;
            }
        }
        void clear() {
            area.setText("");
            lastEntry = "";
        }
        void exportHistory() {
            javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
            chooser.setDialogTitle("Export History Log");
            if (chooser.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
                try (java.io.FileWriter fw = new java.io.FileWriter(chooser.getSelectedFile())) {
                    fw.write(area.getText());
                } catch (Exception ex) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Failed to export history.");
                }
            }
        }
    }

    // Simple Calculator Panel
    private static class SimpleCalculatorPanel extends JPanel {
        private final JTextField display = new JTextField(18);
        private String expr = "";
        private boolean dark = false;
        private java.util.List<JButton> allButtons = new java.util.ArrayList<>();
        SimpleCalculatorPanel() {
            setLayout(new BorderLayout(0, 2)); // even less vertical gap
            setBackground(BG);
            display.setFont(new Font("Segoe UI", Font.BOLD, 28));
            display.setEditable(false);
            display.setHorizontalAlignment(JTextField.RIGHT);
            display.setBackground(BTN_BG);
            display.setForeground(BTN_FG);
            display.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4)); // reduce padding
            display.setPreferredSize(new Dimension(0, 180)); // much larger display for Simple Calculator
            add(display, BorderLayout.NORTH);
            addCopyPasteMenu(display);
            String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C", "←"
            };
            JPanel btnPanel = new JPanel(new GridLayout(5, 4, 2, 2)); // minimal gaps
            btnPanel.setBackground(BG);
            btnPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // minimal panel padding
            Font smallBtnFont = BTN_FONT.deriveFont(15f); // keep font readable
            Dimension btnSize = new Dimension(18, 8); // requested box size
            for (String text : buttons) {
                JButton btn = new JButton(text);
                btn.setFont(smallBtnFont);
                if (isSpecialButton(text)) {
                    btn.setBackground(dark ? DARK_ACCENT : ACCENT);
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setBackground(dark ? DARK_BTN_BG : BTN_BG);
                    btn.setForeground(dark ? DARK_BTN_FG : BTN_FG);
                }
                btn.setFocusPainted(false);
                btn.setPreferredSize(btnSize);
                btn.addActionListener(e -> onButton(text));
                btnPanel.add(btn);
                allButtons.add(btn);
            }
            btnPanel.setPreferredSize(new Dimension(0, 40));
            add(btnPanel, BorderLayout.CENTER);
            // Keyboard support
            setFocusable(true);
            display.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    char ch = e.getKeyChar();
                    if (Character.isDigit(ch) || ch == '.') onButton(String.valueOf(ch));
                    else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') onButton(String.valueOf(ch));
                    else if (ch == '=' || ch == '\n') onButton("=");
                    else if (ch == '\b') onButton("←");
                }
            });
        }
        public void setDarkMode(boolean dark) {
            this.dark = dark;
        }
        public void updateButtonColors() {
            for (JButton btn : allButtons) {
                String text = btn.getText();
                if (isSpecialButton(text)) {
                    btn.setBackground(dark ? DARK_ACCENT : ACCENT);
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setBackground(dark ? DARK_BTN_BG : BTN_BG);
                    btn.setForeground(dark ? DARK_BTN_FG : BTN_FG);
                }
            }
        }
        private boolean isSpecialButton(String text) {
            return text.equals("=") || text.equals("C") || text.equals("←");
        }
        private void onButton(String text) {
            if ("0123456789.".contains(text) || "+-*/".contains(text)) {
                if ("+-*/".contains(text)) {
                    if (expr.isEmpty() && text.equals("-")) {
                        expr += text; // allow negative at start
                    } else if (!expr.isEmpty() && !"+-*/".contains("" + expr.charAt(expr.length() - 1))) {
                        expr += text;
                    }
                    // else: do not append another operator
                } else {
                    expr += text;
                }
                display.setText(expr);
            } else if (text.equals("C")) {
                expr = "";
                display.setText("");
            } else if (text.equals("←")) {
                if (!expr.isEmpty()) {
                    expr = expr.substring(0, expr.length() - 1);
                    display.setText(expr);
                }
            } else if (text.equals("=")) {
                if (expr.isEmpty()) {
                    display.setText("Please enter a value");
                    return;
                }
                try {
                    double val = new ScientificCalculatorPanel.ExpressionEvaluator(expr, true).parse();
                    String resStr = (val == Math.floor(val) && !Double.isInfinite(val)) ? String.valueOf((long)val) : String.valueOf(val);
                    display.setText(resStr);
                    CalHub.addToHistory("Simple: " + expr + " = " + resStr);
                    expr = resStr;
                } catch (ArithmeticException ex) {
                    display.setText("Division by zero");
                } catch (Exception ex) {
                    display.setText("Malformed expression");
                }
            }
        }
    }
    // Scientific Calculator Panel
    private static class ScientificCalculatorPanel extends JPanel {
        private final JTextField display = new JTextField(22);
        private boolean degreeMode = true;
        private String expr = "";
        private boolean dark = false; // add this field
        private java.util.List<JButton> allButtons = new java.util.ArrayList<>(); // store all buttons

        ScientificCalculatorPanel() {
            setLayout(new BorderLayout(0, 2)); // even less vertical gap
            setBackground(BG);
            display.setFont(new Font("Segoe UI", Font.BOLD, 28));
            display.setEditable(true); // allow paste and editing
            display.setHorizontalAlignment(JTextField.RIGHT);
            display.setBackground(BTN_BG);
            display.setForeground(BTN_FG);
            display.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            display.setPreferredSize(new Dimension(0, 180));
            add(display, BorderLayout.NORTH);
            // Arrange scientific functions in the first row, then numbers and operators
            String[][] buttonRows = {
                {"sin", "cos", "tan", "log", "ln", "sqrt"},
                {"(", ")", "^", "pi", "e", "C"},
                {"7", "8", "9", "/", "Deg", "←"},
                {"4", "5", "6", "*", "", ""},
                {"1", "2", "3", "-", "", ""},
                {"0", ".", "=", "+", "", ""}
            };
            JPanel btnPanel = new JPanel(new GridLayout(6, 6, 2, 2)); // 6x6 grid, minimal gaps
            btnPanel.setBackground(BG);
            btnPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // minimal panel padding
            Font smallBtnFont = BTN_FONT.deriveFont(14f); // keep font readable
            Dimension btnSize = new Dimension(16, 6); // requested box size
            for (int row = 0; row < buttonRows.length; row++) {
                for (int col = 0; col < buttonRows[row].length; col++) {
                    String text = buttonRows[row][col];
                    if (text.isEmpty()) {
                        btnPanel.add(Box.createGlue());
                        continue;
                    }
                    JButton btn = new JButton(text);
                    btn.setFont(smallBtnFont);
                    if (isSpecialButton(text)) {
                        btn.setBackground(dark ? DARK_ACCENT : ACCENT);
                        btn.setForeground(Color.WHITE);
                    } else {
                        btn.setBackground(dark ? DARK_BTN_BG : BTN_BG);
                        btn.setForeground(dark ? DARK_BTN_FG : BTN_FG);
                    }
                    btn.setFocusPainted(false);
                    btn.setPreferredSize(btnSize);
                    if (text.equals("Deg")) {
                        btn.setText(degreeMode ? "Deg" : "Rad");
                        btn.addActionListener(e -> {
                            degreeMode = !degreeMode;
                            btn.setText(degreeMode ? "Deg" : "Rad");
                        });
                    } else if (text.equals("←")) {
                        btn.addActionListener(e -> onButton("←"));
                    } else {
                        btn.addActionListener(e -> onButton(text));
                    }
                    btnPanel.add(btn);
                    allButtons.add(btn); // store reference
                }
            }
            add(btnPanel, BorderLayout.CENTER);
            // Enhanced keyboard support
            setFocusable(true);
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    requestFocusInWindow();
                }
            });
            display.addKeyListener(new java.awt.event.KeyAdapter() {
                private StringBuilder funcBuffer = new StringBuilder();
                public void keyTyped(java.awt.event.KeyEvent e) {
                    char ch = e.getKeyChar();
                    if (Character.isDigit(ch) || ch == '.') onButton(String.valueOf(ch));
                    else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^') onButton(String.valueOf(ch));
                    else if (ch == '(' || ch == ')') onButton(String.valueOf(ch));
                    else if (ch == '\n' || ch == '=') onButton("=");
                    else if (Character.isLetter(ch)) {
                        funcBuffer.append(ch);
                        String buf = funcBuffer.toString();
                        // Accept function names when fully typed
                        for (String func : new String[]{"sin","cos","tan","log","ln","sqrt","pi","e"}) {
                            if (buf.equals(func)) {
                                if (func.equals("pi") || func.equals("e")) {
                                    onButton(func);
                                } else {
                                    onButton(func + "(");
                                }
                                funcBuffer.setLength(0);
                                break;
                            }
                        }
                        // Clear buffer if too long or not matching
                        if (funcBuffer.length() > 5) funcBuffer.setLength(0);
                    } else {
                        funcBuffer.setLength(0);
                    }
                }
                public void keyPressed(java.awt.event.KeyEvent e) {
                    // Ctrl+C: Copy
                    if (e.isControlDown() && e.getKeyCode() == java.awt.event.KeyEvent.VK_C) {
                        display.copy();
                    }
                    // Ctrl+V: Paste
                    if (e.isControlDown() && e.getKeyCode() == java.awt.event.KeyEvent.VK_V) {
                        display.paste();
                        expr = display.getText();
                    }
                    // Backspace: delete last function/operator
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE) {
                        if (!expr.isEmpty()) {
                            // Try to remove last function/operator
                            String[] funcs = {"sin(", "cos(", "tan(", "log(", "ln(", "sqrt(", "pi", "e"};
                            boolean found = false;
                            for (String func : funcs) {
                                if (expr.endsWith(func)) {
                                    expr = expr.substring(0, expr.length() - func.length());
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                expr = expr.substring(0, expr.length() - 1);
                            }
                            display.setText(expr);
                        }
                    }
                    // Enter: Evaluate
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                        onButton("=");
                    }
                }
            });
            // Sync expr with display after paste or edit
            display.addCaretListener(e -> expr = display.getText());
            addCopyPasteMenu(display);
        }
        public void setDarkMode(boolean dark) {
            this.dark = dark;
        }
        public void updateButtonColors() {
            for (JButton btn : allButtons) {
                String text = btn.getText();
                if (isSpecialButton(text)) {
                    btn.setBackground(dark ? DARK_ACCENT : ACCENT);
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setBackground(dark ? DARK_BTN_BG : BTN_BG);
                    btn.setForeground(dark ? DARK_BTN_FG : BTN_FG);
                }
            }
        }
        private boolean isSpecialButton(String text) {
            return text.equals("=") || text.equals("C") || text.equals("Deg") || text.equals("Rad") || text.equals("←");
        }
        private void onButton(String text) {
            if ("0123456789.".contains(text) || "()+-*/^".contains(text) || text.equals("pi") || text.equals("e")) {
                expr += text;
                display.setText(expr);
            } else if (text.equals("C")) {
                expr = "";
                display.setText("");
            } else if (text.equals("←")) {
                if (!expr.isEmpty()) {
                    expr = expr.substring(0, expr.length() - 1);
                    display.setText(expr);
                }
            } else if (text.equals("=")) {
                try {
                    double val = new ExpressionEvaluator(expr, degreeMode).parse();
                    String resStr = (val == Math.floor(val) && !Double.isInfinite(val)) ? String.valueOf((long)val) : String.valueOf(val);
                    display.setText(resStr);
                    CalHub.addToHistory("Scientific: " + expr + " = " + resStr);
                    expr = resStr;
                } catch (Exception ex) {
                    display.setText("Error");
                }
            } else if (text.equals("sin") || text.equals("cos") || text.equals("tan") || text.equals("log") || text.equals("ln") || text.equals("sqrt")) {
                expr += text + "(";
                display.setText(expr);
            }
        }
        // Expression evaluator as inner class
        private static class ExpressionEvaluator {
            private final String expr;
            private final boolean degreeMode;
            private int pos = -1, ch;
            ExpressionEvaluator(String expr, boolean degreeMode) {
                this.expr = expr.replace("pi", String.valueOf(Math.PI)).replace("e", String.valueOf(Math.E));
                this.degreeMode = degreeMode;
            }
            void nextChar() { ch = (++pos < expr.length()) ? expr.charAt(pos) : -1; }
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) { nextChar(); return true; }
                return false;
            }
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expr.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expr.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') {
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = expr.substring(startPos, this.pos);
                    x = parseFactor();
                    x = switch (func) {
                        case "sqrt" -> Math.sqrt(x);
                        case "sin" -> degreeMode ? Math.sin(Math.toRadians(x)) : Math.sin(x);
                        case "cos" -> degreeMode ? Math.cos(Math.toRadians(x)) : Math.cos(x);
                        case "tan" -> degreeMode ? Math.tan(Math.toRadians(x)) : Math.tan(x);
                        case "log" -> Math.log10(x);
                        case "ln" -> Math.log(x);
                        default -> throw new RuntimeException("Unknown function: " + func);
                    };
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }
                if (eat('^')) x = Math.pow(x, parseFactor());
                return x;
            }
        }
    }
    // Currency Converter Panel
    private static class CurrencyConverterPanel extends JPanel {
        private final JComboBox<String> fromBox = new JComboBox<>(new String[]{"USD", "EUR", "INR", "GBP", "JPY"});
        private final JComboBox<String> toBox = new JComboBox<>(new String[]{"USD", "EUR", "INR", "GBP", "JPY"});
        private final JTextField amountField = new JTextField(10);
        private final JLabel resultLabel = new JLabel(" ");
        private final JButton convertBtn = new JButton("Convert");
        private final JButton swapBtn = new JButton("Swap");
        private static Map<String, Double> rates = Map.of(
            "USD", 1.0,
            "EUR", 0.92,
            "INR", 83.0,
            "GBP", 0.78,
            "JPY", 155.0
        );
        CurrencyConverterPanel() {
            setLayout(new GridBagLayout());
            setBackground(BG);
            setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(8, 8, 8, 8);
            c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_END;
            JLabel fromLabel = new JLabel("From:"); fromLabel.setFont(FIELD_FONT); fromLabel.setForeground(PRIMARY);
            add(fromLabel, c);
            c.gridx = 1; c.anchor = GridBagConstraints.LINE_START;
            fromBox.setFont(FIELD_FONT); fromBox.setBackground(BTN_BG); fromBox.setForeground(BTN_FG);
            add(fromBox, c);
            c.gridx = 2; c.anchor = GridBagConstraints.LINE_START;
            swapBtn.setFont(BTN_FONT.deriveFont(14f)); swapBtn.setBackground(ACCENT); swapBtn.setForeground(Color.WHITE);
            add(swapBtn, c);
            c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.LINE_END;
            JLabel toLabel = new JLabel("To:"); toLabel.setFont(FIELD_FONT); toLabel.setForeground(PRIMARY);
            add(toLabel, c);
            c.gridx = 1; c.anchor = GridBagConstraints.LINE_START;
            toBox.setFont(FIELD_FONT); toBox.setBackground(BTN_BG); toBox.setForeground(BTN_FG);
            add(toBox, c);
            c.gridx = 0; c.gridy++; c.anchor = GridBagConstraints.LINE_END;
            JLabel amtLabel = new JLabel("Amount:"); amtLabel.setFont(FIELD_FONT); amtLabel.setForeground(PRIMARY);
            add(amtLabel, c);
            c.gridx = 1; c.anchor = GridBagConstraints.LINE_START;
            amountField.setFont(FIELD_FONT); amountField.setBackground(BTN_BG); amountField.setForeground(BTN_FG);
            add(amountField, c);
            c.gridx = 0; c.gridy++; c.gridwidth = 3; c.anchor = GridBagConstraints.CENTER;
            convertBtn.setFont(BTN_FONT); convertBtn.setBackground(ACCENT); convertBtn.setForeground(Color.WHITE);
            add(convertBtn, c);
            c.gridy++;
            resultLabel.setFont(BTN_FONT); resultLabel.setForeground(ACCENT);
            add(resultLabel, c);
            convertBtn.addActionListener(e -> convert());
            amountField.addActionListener(e -> convert());
            swapBtn.addActionListener(e -> swapCurrencies());
            addCopyPasteMenu(amountField);
            fetchRates();
        }
        private void swapCurrencies() {
            int fromIdx = fromBox.getSelectedIndex();
            fromBox.setSelectedIndex(toBox.getSelectedIndex());
            toBox.setSelectedIndex(fromIdx);
        }
        private void fetchRates() {
            new Thread(() -> {
                try {
                    java.net.URL url = new java.net.URL("https://api.exchangerate.host/latest?base=USD&symbols=USD,EUR,INR,GBP,JPY");
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(2000);
                    conn.setReadTimeout(2000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == 200) {
                        java.io.InputStream is = conn.getInputStream();
                        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                        String json = s.hasNext() ? s.next() : "";
                        s.close();
                        is.close();
                        int idx = json.indexOf("rates");
                        if (idx != -1) {
                            Map<String, Double> newRates = new java.util.HashMap<>();
                            for (String cur : new String[]{"USD","EUR","INR","GBP","JPY"}) {
                                String pat = "\""+cur+"\":([0-9.]+)";
                                java.util.regex.Matcher m = java.util.regex.Pattern.compile(pat).matcher(json);
                                if (m.find()) newRates.put(cur, Double.parseDouble(m.group(1)));
                            }
                            if (newRates.size() == 5) rates = newRates;
                        }
                    }
                } catch (Exception ignore) {}
            }).start();
        }
        private void convert() {
            if (amountField.getText().isEmpty()) {
                resultLabel.setText("Please enter an amount");
                return;
            }
            try {
                String from = (String) fromBox.getSelectedItem();
                String to = (String) toBox.getSelectedItem();
                double amt = Double.parseDouble(amountField.getText());
                double usd = amt / rates.get(from);
                double res = usd * rates.get(to);
                String resultStr = String.format("%.2f %s", res, to);
                resultLabel.setText(resultStr);
                CalHub.addToHistory("Currency: " + amt + " " + from + " → " + resultStr);
            } catch (Exception ex) {
                resultLabel.setText("Invalid input");
            }
        }
    }
} 