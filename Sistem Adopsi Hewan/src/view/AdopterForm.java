package view;

import database.Koneksi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Form manajemen data adopter.
 */
public class AdopterForm extends JFrame {
    private JTextField tfNama, tfAlamat, tfTelp;
    private JTable table;
    private DefaultTableModel model;
    private Connection conn;

    public AdopterForm() {
        setTitle("Manajemen Adopter");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        conn = Koneksi.getConnection();
        createTable();
        loadData();
        setupListeners();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfNama = new JTextField(20);
        tfAlamat = new JTextField(20);
        tfTelp = new JTextField(20);

        addFormField(formPanel, gbc, "Nama:", tfNama, 0);
        addFormField(formPanel, gbc, "Alamat:", tfAlamat, 1);
        addFormField(formPanel, gbc, "No. Telp:", tfTelp, 2);

        JPanel buttonPanel = createButtonPanel();
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        model = new DefaultTableModel(new String[]{"ID", "Nama", "Alamat", "Telp"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 250));

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton simpan = new JButton("Simpan");
        JButton update = new JButton("Update");
        JButton hapus = new JButton("Hapus");
        JButton kembali = new JButton("Kembali");

        simpan.addActionListener(e -> { simpanData(); loadData(); });
        update.addActionListener(e -> { updateData(); loadData(); });
        hapus.addActionListener(e -> { hapusData(); loadData(); });
        kembali.addActionListener(e -> { dispose(); new App(); });

        panel.add(simpan);
        panel.add(update);
        panel.add(hapus);
        panel.add(kembali);

        return panel;
    }

    private void setupListeners() {
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                tfNama.setText(model.getValueAt(r, 1).toString());
                tfAlamat.setText(model.getValueAt(r, 2).toString());
                tfTelp.setText(model.getValueAt(r, 3).toString());
            }
        });
    }

    private void createTable() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS adopter (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nama TEXT," +
                    "alamat TEXT," +
                    "telp TEXT)");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal membuat tabel: " + e.getMessage());
        }
    }

    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM adopter")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("alamat"),
                        rs.getString("telp")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }

    private void simpanData() {
        if (tfNama.getText().isEmpty() || tfAlamat.getText().isEmpty() || tfTelp.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap isi semua field.");
            return;
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO adopter (nama, alamat, telp) VALUES (?, ?, ?)")) {
            ps.setString(1, tfNama.getText());
            ps.setString(2, tfAlamat.getText());
            ps.setString(3, tfTelp.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage());
        }
    }

    private void updateData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE adopter SET nama=?, alamat=?, telp=? WHERE id=?")) {
            ps.setString(1, tfNama.getText());
            ps.setString(2, tfAlamat.getText());
            ps.setString(3, tfTelp.getText());
            ps.setInt(4, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data: " + e.getMessage());
        }
    }

    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM adopter WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdopterForm::new);
    }
}