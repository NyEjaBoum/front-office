package com.frontoffice.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Main standalone pour generer des tokens et les inserer dans la base du Back Office.
 * Insere un token valide (expire dans 24h) et un token expire (expire il y a 1h).
 * Apres execution, copier le token valide affiche dans application.properties (api.token=...).
 */
public class TokenGenerator {

    public static void main(String[] args) {
        System.out.println("=== Generation de tokens ===");
        System.out.println();

        // Token valide : expire dans 24h
        String tokenValide = UUID.randomUUID().toString();
        insererToken(tokenValide, Timestamp.valueOf(LocalDateTime.now().plusHours(24)), "valide");

        System.out.println();

        // Token expire : date_expiration dans le passe
        String tokenExpire = UUID.randomUUID().toString();
        insererToken(tokenExpire, Timestamp.valueOf(LocalDateTime.now().minusHours(1)), "expire");

        System.out.println();
        System.out.println(">>> Copiez cette ligne dans application.properties :");
        System.out.println("api.token=" + tokenValide);
    }

    private static void insererToken(String token, Timestamp expiration, String label) {
        String sql = "INSERT INTO token (token, date_expiration) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, UUID.fromString(token));
            ps.setTimestamp(2, expiration);
            ps.executeUpdate();

            System.out.println("Token " + label + " insere : " + token);
            System.out.println("Expiration       : " + expiration);

        } catch (Exception e) {
            System.err.println("Erreur insertion token " + label + " :");
            e.printStackTrace();
        }
    }
}
