package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Kelas untuk mengelola koneksi database MySQL
 */
public class Koneksi {
    // Konfigurasi database MySQL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/adopsi_hewan";
    private static final String DB_USER = "erin11";
    private static final String DB_PASSWORD = "erin1104";
    
    private static volatile Connection conn = null;

    // Private constructor untuk mencegah instantiasi
    private Koneksi() {}

    /**
     * Mendapatkan koneksi database (Singleton pattern)
     * @return Connection object
     * @throws RuntimeException jika gagal membuat koneksi
     */
    public static Connection getConnection() {
        // Double-checked locking untuk thread safety
        if (Objects.isNull(conn)) {
            synchronized (Koneksi.class) {
                if (Objects.isNull(conn)) {
                    try {
                        // Load MySQL JDBC driver
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                        System.out.println("Koneksi database MySQL berhasil dibuat.");
                    } catch (ClassNotFoundException e) {
                        System.err.println("Driver MySQL tidak ditemukan:");
                        e.printStackTrace();
                        throw new DatabaseConnectionException("Driver database tidak ditemukan", e);
                    } catch (SQLException e) {
                        System.err.println("Gagal membuat koneksi database:");
                        e.printStackTrace();
                        throw new DatabaseConnectionException("Gagal terhubung ke database", e);
                    }
                }
            }
        }
        
        try {
            if (conn.isClosed()) {
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Koneksi database direset karena sebelumnya ditutup.");
            }
        } catch (SQLException e) {
            System.err.println("Gagal memeriksa status koneksi:");
            e.printStackTrace();
        }
        
        return conn;
    }

    /**
     * Menutup koneksi database
     */
    public static void closeConnection() {
        if (Objects.nonNull(conn)) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println("Koneksi database ditutup.");
                }
            } catch (SQLException e) {
                System.err.println("Gagal menutup koneksi database:");
                e.printStackTrace();
            } finally {
                conn = null; // Reset connection
            }
        }
    }

    /**
     * Custom exception untuk kesalahan koneksi database
     */
    public static class DatabaseConnectionException extends RuntimeException {
        public DatabaseConnectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Memeriksa apakah koneksi valid
     * @return true jika koneksi valid dan terbuka
     */
    public static boolean isConnectionValid() {
        if (Objects.isNull(conn)) {
            return false;
        }
        
        try {
            return !conn.isClosed() && conn.isValid(5); // Timeout 5 detik
        } catch (SQLException e) {
            System.err.println("Gagal memeriksa validitas koneksi:");
            e.printStackTrace();
            return false;
        }
    }
}