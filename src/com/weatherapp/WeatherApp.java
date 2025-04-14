package com.weatherapp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class WeatherApp extends JFrame {
    private JTextField cityInput;
    private JButton getWeatherBtn;
    private JTextArea outputArea;
    private JToggleButton themeToggle;
    private JLabel cityLabel;
    private JPanel topInnerPanel;

    private boolean isDarkMode = false;

    // Font
    private final Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 16);
    private final Font emojiFontBold = new Font("Segoe UI Emoji", Font.BOLD, 16);

    // Light Theme Colors (Lavender themed)
    private final Color LIGHT_PRIMARY = new Color(245, 242, 255);         // Lavender background
    private final Color LIGHT_ACCENT = new Color(230, 225, 250);          // Light purple accents
    private final Color LIGHT_TEXT = new Color(50, 50, 80);               // Deep bluish-purple text

    // Dark Theme Colors (Greys)
    private final Color DARK_PRIMARY = new Color(38, 38, 38);             // Dark gray background
    private final Color DARK_ACCENT = new Color(60, 60, 60);              // Mid gray accents
    private final Color DARK_TEXT = new Color(230, 230, 230);             // Light text for dark mode

    // Toggle color for dark mode
    private final Color TOGGLE_COLOR_DARK = new Color(128, 128, 128);     // Mid gray

    public WeatherApp() {
        setTitle("☀️ Weather App");
        setSize(500, 360);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(false);

        // Input field
        cityInput = new JTextField();
        cityInput.setFont(emojiFont);
        cityInput.setHorizontalAlignment(JTextField.LEFT);
        cityInput.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Button
        getWeatherBtn = new JButton("🌧️ Get Weather");
        getWeatherBtn.setFont(emojiFont);
        getWeatherBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        getWeatherBtn.setFocusPainted(false);
        getWeatherBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(emojiFont);
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Theme toggle
        themeToggle = new JToggleButton("🔘 Dark Mode: OFF");
        themeToggle.setFont(emojiFont);
        themeToggle.setFocusPainted(false);
        themeToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeToggle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        themeToggle.addActionListener(e -> {
            isDarkMode = themeToggle.isSelected();
            themeToggle.setText(isDarkMode ? "🔘 Dark Mode: ON" : "🔘 Dark Mode: OFF");
            fadeThemeSwitch();
        });

        // Label
        cityLabel = new JLabel("🏙️ Enter City:");
        cityLabel.setFont(emojiFontBold);

        // Top panel
        topInnerPanel = new JPanel(new GridBagLayout());
        topInnerPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        topInnerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        topInnerPanel.add(cityLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        topInnerPanel.add(cityInput, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        topInnerPanel.add(getWeatherBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        topInnerPanel.add(themeToggle, gbc);

        add(topInnerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Weather button logic
        getWeatherBtn.addActionListener(e -> {
            String city = cityInput.getText().trim();
            if (!city.isEmpty()) {
                String result = WeatherService.getWeather(city);
                outputArea.setText(result);
            } else {
                outputArea.setText("⚠️ Please enter a city name.");
            }
        });

        applyTheme();
        setVisible(true);
    }

    private void applyTheme() {
        Color bg = isDarkMode ? DARK_PRIMARY : LIGHT_PRIMARY;
        Color accent = isDarkMode ? DARK_ACCENT : LIGHT_ACCENT;
        Color text = isDarkMode ? DARK_TEXT : LIGHT_TEXT;
        Color toggleBtn = isDarkMode ? TOGGLE_COLOR_DARK : LIGHT_ACCENT;

        getContentPane().setBackground(bg);
        topInnerPanel.setBackground(bg);

        cityLabel.setForeground(text);
        themeToggle.setForeground(text);
        themeToggle.setBackground(toggleBtn);

        cityInput.setBackground(accent);
        cityInput.setForeground(text);
        cityInput.setCaretColor(text);

        getWeatherBtn.setBackground(accent);
        getWeatherBtn.setForeground(text);

        outputArea.setBackground(accent);
        outputArea.setForeground(text);
    }

    private void fadeThemeSwitch() {
        Timer timer = new Timer(20, null);
        final float[] alpha = {0f};
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha[0] += 0.05f;
                if (alpha[0] >= 1f) {
                    alpha[0] = 1f;
                    timer.stop();
                }

                applyTheme();
                repaint();
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Enable opacity on decorated window (requires Java 9+)
            JFrame.setDefaultLookAndFeelDecorated(true);
            new WeatherApp();
        });
    }
}
