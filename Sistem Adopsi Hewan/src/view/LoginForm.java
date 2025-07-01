package view;

import database.Koneksi;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Form login untuk administrator sistem.
 */
public class LoginForm extends JFrame {
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JButton btnLogin;
    private Connection conn;

    public LoginForm() {
        setTitle("Login Admin");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        conn = Koneksi.getConnection(); // inisialisasi koneksi
        btnLogin.addActionListener(e -> loginAdmin());

        setVisible(true);
    }

    /**
     * Inisialisasi komponen UI
     */
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfUsername = new JTextField(20);
        tfPassword = new JPasswordField(20);
        addFormField(formPanel, gbc, "Username:", tfUsername, 0);
        addFormField(formPanel, gbc, "Password:", tfPassword, 1);

        btnLogin = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(btnLogin, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    /**
     * Tambahkan label dan field ke form.
     */
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    /**
     * Proses login admin dengan validasi database.
     */
    private void loginAdmin() {
        try {
            String username = tfUsername.getText().trim();
            String password = String.valueOf(tfPassword.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi username dan password.");
                return;
            }

            String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login berhasil!");
                dispose();
                new App(); // pastikan class App tersedia dan berjalan baik
            } else {
                JOptionPane.showMessageDialog(this, "Username atau password salah.");
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }
}