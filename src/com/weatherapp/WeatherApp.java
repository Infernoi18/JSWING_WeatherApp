package com.weatherapp;

import java.awt.*;
import javax.swing.*;

public class WeatherApp extends JFrame {
    private JTextField cityInput;
    private JButton getWeatherBtn;
    private JTextArea outputArea;

    public WeatherApp() {
        setTitle("☀️ Weather App");
        setSize(500, 350);
        setLayout(new BorderLayout(15, 10));
        getContentPane().setBackground(new Color(245, 245, 250)); // Light background

        // ----- Input Field -----
        cityInput = new JTextField();
        cityInput.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cityInput.setHorizontalAlignment(JTextField.CENTER);
        cityInput.setBackground(new Color(255, 255, 255));
        cityInput.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230), 2));

        // ----- Button -----
        getWeatherBtn = new JButton("🌧️ Get Weather");
        getWeatherBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        getWeatherBtn.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        getWeatherBtn.setForeground(Color.WHITE);
        getWeatherBtn.setFocusPainted(false);
        getWeatherBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ----- Output Area -----
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Arial Unicode MS", Font.PLAIN, 15));
        outputArea.setBackground(new Color(230, 245, 250));
        outputArea.setForeground(new Color(25, 25, 112)); // Midnight Blue
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        outputArea.setBorder(BorderFactory.createTitledBorder("Weather Info"));

        // ----- Top Panel (Label + Input + Button) -----
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(245, 245, 250));

        JLabel cityLabel = new JLabel("Enter City:");
        cityLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cityLabel.setForeground(new Color(70, 130, 180)); // Steel Blue
        cityLabel.setHorizontalAlignment(SwingConstants.LEFT);
        cityLabel.setPreferredSize(new Dimension(100, 30));

        topPanel.add(cityLabel, BorderLayout.WEST);
        topPanel.add(cityInput, BorderLayout.CENTER);
        topPanel.add(getWeatherBtn, BorderLayout.EAST);

        // ----- Scroll Pane for Output -----
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ----- Add components to Frame -----
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // ----- Button Action -----
        getWeatherBtn.addActionListener(e -> {
            String city = cityInput.getText().trim();
            if (!city.isEmpty()) {
                String result = WeatherService.getWeather(city);
                outputArea.setText(result);
            } else {
                outputArea.setText("Please enter a city name.");
            }
        });

        // ----- Final Touches -----
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }
}
