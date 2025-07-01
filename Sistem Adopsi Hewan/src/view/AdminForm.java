package view;
import database.Koneksi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminForm extends JFrame {
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JTable table;
    private DefaultTableModel model;
    private Connection conn;

    public AdminForm() {
        setTitle("Manajemen Admin");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        tfUsername = new JTextField(20);
        formPanel.add(tfUsername, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        tfPassword = new JPasswordField(20);
        formPanel.add(tfPassword, gbc);
        
        // Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton simpan = new JButton("Simpan");
        JButton hapus = new JButton("Hapus");
        JButton kembali = new JButton("Kembali");
        buttonPanel.add(simpan);
        buttonPanel.add(hapus);
        buttonPanel.add(kembali);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        // Tabel
        model = new DefaultTableModel(new String[]{"ID", "Username"}, 0);
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(500, 150));
        JScrollPane scrollPane = new JScrollPane(table);
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
        
        conn = Koneksi.getConnection();
        createTable();
        loadData();

        simpan.addActionListener(e -> { simpanData(); loadData(); });
        hapus.addActionListener(e -> { hapusData(); loadData(); });
        kembali.addActionListener(e -> {
            this.dispose();
            new App();
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                tfUsername.setText(model.getValueAt(r,1).toString());
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    void createTable() {
        try {
            Statement st = conn.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS admin (id_admin INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)");
        } catch (Exception e) { e.printStackTrace(); }
    }

    void loadData() {
        model.setRowCount(0);
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT id_admin, username FROM admin");
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString(2)});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    void simpanData() {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO admin (username, password) VALUES (?, ?)");
            ps.setString(1, tfUsername.getText());
            ps.setString(2, new String(tfPassword.getPassword()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data admin disimpan.");
        } catch (Exception e) { e.printStackTrace(); }
    }

    void hapusData() {
        try {
            int row = table.getSelectedRow();
            int id = (int) model.getValueAt(row, 0);
            PreparedStatement ps = conn.prepareStatement("DELETE FROM admin WHERE id_admin = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data admin dihapus.");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void main(String[] args) {
        new AdminForm();
    }
}