package com.weatherapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONObject;

public class WeatherService {

    public static String getWeather(String city) {
        try {
            String apiKey = Config.API_KEY;
            String encodedCity = URLEncoder.encode(city, "UTF-8");
            String urlString = "http://api.weatherapi.com/v1/current.json?key=" +
                               apiKey + "&q=" + encodedCity;

            System.out.println(" Sending request to: " + urlString); // Debug line

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            System.out.println(" HTTP response code: " + responseCode); // Debug line

            if (responseCode != 200) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                errorReader.close();
                System.out.println(" Error response from API: " + errorResponse.toString());
                return "Error: " + errorResponse.toString();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();

            System.out.println(" API response: " + json.toString()); // Debug line

            JSONObject obj = new JSONObject(json.toString());
            JSONObject current = obj.getJSONObject("current");
            JSONObject condition = current.getJSONObject("condition");

            String weather = condition.getString("text");
            double temp = current.getDouble("temp_c");
            double feelsLike = current.getDouble("feelslike_c");
            int humidity = current.getInt("humidity");
            double windKph = current.getDouble("wind_kph");
            String windDir = current.getString("wind_dir");
            double pressureMb = current.getDouble("pressure_mb");
            double visibilityKm = current.getDouble("vis_km");
            double uvIndex = current.getDouble("uv");
            String localTime = obj.getJSONObject("location").getString("localtime");

            return String.format(
                "📍 Local Time: %s\n" +
                "🌤️  Weather: %s\n" +
                "🌡️  Temperature: %.1f °C (Feels like %.1f °C)\n" +
                "💧 Humidity: %d%%\n" +
                "🌬️  Wind: %.1f kph (%s)\n" +
                "🔍 Visibility: %.1f km\n" +
                "🔵 Pressure: %.1f mb\n" +
                "🌞 UV Index: %.1f",
                localTime, weather, temp, feelsLike, humidity, windKph, windDir,
                visibilityKm, pressureMb, uvIndex
            );

        } catch (Exception e) {
            System.out.println(" Exception caught:");
            e.printStackTrace();
            return "Error: Unable to get weather for \"" + city + "\"";
        }
    }
}
