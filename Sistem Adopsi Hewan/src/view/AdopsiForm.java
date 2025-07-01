package view;

import database.Koneksi;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Form untuk proses adopsi hewan oleh adopter.
 */
public class AdopsiForm extends JFrame {
    private JComboBox<String> cbHewan, cbAdopter;
    private JButton btnAdopsi;
    private Connection conn;

    // Menyimpan mapping nama -> id
    private Map<String, Integer> hewanMap = new HashMap<>();
    private Map<String, Integer> adopterMap = new HashMap<>();

    public AdopsiForm() {
        setTitle("Form Adopsi");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();

        conn = Koneksi.getConnection();
        createTable();
        loadComboData();

        btnAdopsi.addActionListener(e -> simpanAdopsi());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ComboBox Hewan
        cbHewan = new JComboBox<>();
        cbHewan.setPreferredSize(new Dimension(200, 25));
        addFormField(formPanel, gbc, "Pilih Hewan:", cbHewan, 0);

        // ComboBox Adopter
        cbAdopter = new JComboBox<>();
        cbAdopter.setPreferredSize(new Dimension(200, 25));
        addFormField(formPanel, gbc, "Pilih Adopter:", cbAdopter, 1);

        // Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdopsi = new JButton("Simpan Adopsi");
        JButton kembali = new JButton("Kembali");

        kembali.addActionListener(e -> {
            dispose();
            new App();
        });

        buttonPanel.add(btnAdopsi);
        buttonPanel.add(kembali);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void createTable() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS adopsi (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_hewan INTEGER, " +
                    "id_adopter INTEGER, " +
                    "tanggal DATE DEFAULT CURRENT_DATE)");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal membuat tabel: " + e.getMessage());
        }
    }

    private void loadComboData() {
        cbHewan.removeAllItems();
        cbAdopter.removeAllItems();
        hewanMap.clear();
        adopterMap.clear();

        try (Statement st = conn.createStatement()) {
            ResultSet rsHewan = st.executeQuery("SELECT id, nama FROM hewan WHERE status = 'Tersedia'");
            while (rsHewan.next()) {
                int id = rsHewan.getInt("id");
                String nama = rsHewan.getString("nama");
                hewanMap.put(nama, id);
                cbHewan.addItem(nama);
            }

            ResultSet rsAdopter = st.executeQuery("SELECT id, nama FROM adopter");
            while (rsAdopter.next()) {
                int id = rsAdopter.getInt("id");
                String nama = rsAdopter.getString("nama");
                adopterMap.put(nama, id);
                cbAdopter.addItem(nama);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }

    private void simpanAdopsi() {
        String namaHewan = (String) cbHewan.getSelectedItem();
        String namaAdopter = (String) cbAdopter.getSelectedItem();

        if (namaHewan == null || namaAdopter == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih hewan dan adopter.");
            return;
        }

        int idHewan = hewanMap.get(namaHewan);
        int idAdopter = adopterMap.get(namaAdopter);

        try {
            conn.setAutoCommit(false); // Mulai transaksi

            // Simpan data adopsi
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO adopsi (id_hewan, id_adopter) VALUES (?, ?)")) {
                ps.setInt(1, idHewan);
                ps.setInt(2, idAdopter);
                ps.executeUpdate();
            }

            // Update status hewan
            try (PreparedStatement updateStatus = conn.prepareStatement(
                    "UPDATE hewan SET status = 'Sudah Diadopsi' WHERE id = ?")) {
                updateStatus.setInt(1, idHewan);
                updateStatus.executeUpdate();
            }

            conn.commit(); // Commit transaksi

            JOptionPane.showMessageDialog(this, "Adopsi berhasil disimpan!");
            loadComboData(); // refresh hewan yang tersedia

        } catch (SQLException e) {
            try {
                conn.rollback(); // jika gagal, rollback
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Gagal menyimpan adopsi: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true); // Kembalikan ke auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdopsiForm::new);
    }
}