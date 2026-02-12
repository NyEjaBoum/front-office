package com.frontoffice.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

/**
 * Main standalone pour tester la verification du token cote Back Office.
 * Teste 4 scenarios : token valide, sans token, mauvais token, token expire.
 *
 * Prerequis : avoir execute TokenGenerator et mis a jour api.token dans application.properties.
 */
public class TokenTest {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        try (InputStream is = TokenTest.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (is == null) {
                System.err.println("Fichier application.properties introuvable dans le classpath.");
                return;
            }
            props.load(is);
        }

        String apiUrl = props.getProperty("backoffice.api.url", "http://localhost:8080/voiture/api");
        String validToken = props.getProperty("api.token", "");
        String reservationsUrl = apiUrl + "/reservations";

        System.out.println("=== Test de verification du token ===");
        System.out.println("URL API : " + reservationsUrl);
        System.out.println("Token   : " + validToken);
        System.out.println();

        // Test 1 : Token valide
        System.out.println("--- Test 1 : Token valide ---");
        callApi(reservationsUrl, validToken);

        // Test 2 : Sans token
        System.out.println("--- Test 2 : Sans token ---");
        callApi(reservationsUrl, null);

        // Test 3 : Token invalide (UUID aleatoire non present en base)
        System.out.println("--- Test 3 : Token invalide (inexistant en base) ---");
        callApi(reservationsUrl, UUID.randomUUID().toString());

        // Test 4 : Token expire (insere en base avec date passee, puis nettoye)
        System.out.println("--- Test 4 : Token expire ---");
        String expiredToken = insererTokenExpire();
        if (expiredToken != null) {
            callApi(reservationsUrl, expiredToken);
            supprimerToken(expiredToken);
        }
    }

    private static void callApi(String url, String token) {
        try {
            HttpURLConnection conn = (HttpURLConnection) URI.create(url).toURL().openConnection();
            conn.setRequestMethod("GET");
            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("X-API-TOKEN", token);
            }

            int code = conn.getResponseCode();
            System.out.println("Code HTTP : " + code);

            InputStream is = (code >= 400) ? conn.getErrorStream() : conn.getInputStream();
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                String body = sb.toString();
                if (body.length() > 300) {
                    body = body.substring(0, 300) + "...";
                }
                System.out.println("Reponse   : " + body);
            }
            conn.disconnect();
        } catch (Exception e) {
            System.err.println("Erreur connexion : " + e.getMessage());
        }
        System.out.println();
    }

    private static String insererTokenExpire() {
        String expiredToken = UUID.randomUUID().toString();
        String sql = "INSERT INTO token (token, date_expiration) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, UUID.fromString(expiredToken));
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().minusHours(1)));
            ps.executeUpdate();
            System.out.println("Token expire insere : " + expiredToken);
            return expiredToken;

        } catch (Exception e) {
            System.err.println("Erreur insertion token expire : " + e.getMessage());
            return null;
        }
    }

    private static void supprimerToken(String token) {
        String sql = "DELETE FROM token WHERE token = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, UUID.fromString(token));
            ps.executeUpdate();
            System.out.println("Token expire nettoye de la base.");
        } catch (Exception e) {
            System.err.println("Erreur nettoyage token : " + e.getMessage());
        }
    }
}
