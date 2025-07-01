package view;

import database.Koneksi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Form untuk manajemen data hewan di sistem adopsi.
 */
public class HewanForm extends JFrame {
    private JTextField tfNama, tfJenis, tfUmur;
    private JComboBox<String> cbStatus;
    private JTable table;
    private DefaultTableModel model;
    private Connection conn;

    public HewanForm() {
        setTitle("Manajemen Data Hewan");
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
        tfJenis = new JTextField(20);
        tfUmur = new JTextField(20);
        cbStatus = new JComboBox<>(new String[]{"Tersedia", "Sudah Diadopsi"});

        addFormField(formPanel, gbc, "Nama:", tfNama, 0);
        addFormField(formPanel, gbc, "Jenis:", tfJenis, 1);
        addFormField(formPanel, gbc, "Umur:", tfUmur, 2);
        addFormField(formPanel, gbc, "Status:", cbStatus, 3);

        JPanel buttonPanel = createButtonPanel();
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        model = new DefaultTableModel(new String[]{"ID", "Nama", "Jenis", "Umur", "Status"}, 0);
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
                tfJenis.setText(model.getValueAt(r, 2).toString());
                tfUmur.setText(model.getValueAt(r, 3).toString());
                cbStatus.setSelectedItem(model.getValueAt(r, 4).toString());
            }
        });
    }

    // Fungsionalitas database (untuk diisi penuh sesuai skema)
    private void createTable() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS hewan (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nama TEXT," +
                    "jenis TEXT," +
                    "umur INTEGER," +
                    "status TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM hewan")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("jenis"),
                        rs.getInt("umur"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void simpanData() {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO hewan (nama, jenis, umur, status) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, tfNama.getText());
            ps.setString(2, tfJenis.getText());
            ps.setInt(3, Integer.parseInt(tfUmur.getText()));
            ps.setString(4, cbStatus.getSelectedItem().toString());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan.");
        } catch (SQLException | NumberFormatException e) {
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
                "UPDATE hewan SET nama=?, jenis=?, umur=?, status=? WHERE id=?")) {
            ps.setString(1, tfNama.getText());
            ps.setString(2, tfJenis.getText());
            ps.setInt(3, Integer.parseInt(tfUmur.getText()));
            ps.setString(4, cbStatus.getSelectedItem().toString());
            ps.setInt(5, id);
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
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM hewan WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HewanForm::new);
    }
}