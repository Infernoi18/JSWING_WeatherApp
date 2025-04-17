package com.weatherapp;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class WeatherApp extends JFrame {
    private JTextField cityInput;
    private JButton getWeatherBtn;
    private JTextArea outputArea;
    private JTextArea historyArea;
    private JToggleButton themeToggle;

    private List<String> searchHistory = new ArrayList<>();

    private boolean isDarkMode = false;

    // Colors
    private final Color LIGHT_PRIMARY = new Color(245, 242, 255);         // Lavender background
    private final Color LIGHT_ACCENT = new Color(230, 225, 250);          // Light purple accents
    private final Color LIGHT_TEXT = new Color(50, 50, 80);

    private final Color DARK_PRIMARY = new Color(40, 40, 45);             // Dark grey
    private final Color DARK_ACCENT = new Color(65, 65, 75);              // Mid grey
    private final Color DARK_TEXT = new Color(230, 230, 240);

    public WeatherApp() {
        setTitle("☀️ Weather App");
        setSize(600, 550);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();

        applyTheme();  // Initial theme
        setVisible(true);
    }

    private void initUI() {
        Font uiFont = new Font("Segoe UI Emoji", Font.PLAIN, 16);

        // Input
        cityInput = new JTextField();
        cityInput.setFont(uiFont);
        cityInput.setHorizontalAlignment(JTextField.LEFT);

        // Button
        getWeatherBtn = new JButton("🌧️ Get Weather");
        getWeatherBtn.setFont(uiFont);
        getWeatherBtn.setFocusPainted(false);
        getWeatherBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Toggle Button
        themeToggle = new JToggleButton("Dark Mode: OFF");
        themeToggle.setFont(uiFont);
        themeToggle.setFocusPainted(false);
        themeToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeToggle.addItemListener(e -> {
            isDarkMode = themeToggle.isSelected();
            themeToggle.setText("Dark Mode: " + (isDarkMode ? "ON" : "OFF"));
            applyThemeWithAnimation();
        });

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JLabel cityLabel = new JLabel("Enter City:");
        cityLabel.setFont(uiFont);
        cityLabel.setPreferredSize(new Dimension(100, 30));

        topPanel.add(cityLabel, BorderLayout.WEST);
        topPanel.add(cityInput, BorderLayout.CENTER);
        topPanel.add(getWeatherBtn, BorderLayout.EAST);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Weather Info"));

        // History Area
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        historyArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setBorder(BorderFactory.createTitledBorder("Search History"));
        historyScroll.setPreferredSize(new Dimension(500, 120));

        // Add components
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(outputScroll, BorderLayout.CENTER);
        centerPanel.add(historyScroll, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(themeToggle, BorderLayout.SOUTH);

        // Button Logic
        getWeatherBtn.addActionListener(e -> {
            String city = cityInput.getText().trim();
            if (!city.isEmpty()) {
                String result = WeatherService.getWeather(city);
                outputArea.setText(result);
                searchHistory.add("🔍 " + city + ":\n" + result + "\n");
                updateHistoryView();
            } else {
                outputArea.setText("Please enter a city name.");
            }
        });
    }

    private void updateHistoryView() {
        StringBuilder sb = new StringBuilder();
        for (String item : searchHistory) {
            sb.append(item).append("\n--------------------------\n");
        }
        historyArea.setText(sb.toString());
    }

    private void applyThemeWithAnimation() {
        Timer timer = new Timer(10, null);
        int[] step = {0};

        Color startBg = getContentPane().getBackground();
        Color endBg = isDarkMode ? DARK_PRIMARY : LIGHT_PRIMARY;

        timer.addActionListener(e -> {
            float ratio = step[0] / 20f;
            int r = (int) (startBg.getRed() + ratio * (endBg.getRed() - startBg.getRed()));
            int g = (int) (startBg.getGreen() + ratio * (endBg.getGreen() - startBg.getGreen()));
            int b = (int) (startBg.getBlue() + ratio * (endBg.getBlue() - startBg.getBlue()));

            Color interpolated = new Color(r, g, b);
            getContentPane().setBackground(interpolated);
            step[0]++;
            if (step[0] > 20) {
                timer.stop();
                applyTheme(); // Snap final state
            }
        });
        timer.start();
    }

    private void applyTheme() {
        Color bg = isDarkMode ? DARK_PRIMARY : LIGHT_PRIMARY;
        Color accent = isDarkMode ? DARK_ACCENT : LIGHT_ACCENT;
        Color text = isDarkMode ? DARK_TEXT : LIGHT_TEXT;

        getContentPane().setBackground(bg);
        cityInput.setBackground(accent);
        cityInput.setForeground(text);
        cityInput.setCaretColor(text);
        cityInput.setBorder(BorderFactory.createLineBorder(accent, 2));

        getWeatherBtn.setBackground(accent);
        getWeatherBtn.setForeground(text);

        themeToggle.setBackground(new Color(100, 100, 110)); // Mid-grey
        themeToggle.setForeground(Color.WHITE);

        outputArea.setBackground(accent);
        outputArea.setForeground(text);

        historyArea.setBackground(accent);
        historyArea.setForeground(text);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WeatherApp::new);
    }
}
