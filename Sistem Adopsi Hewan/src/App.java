import java.awt.*;
import javax.swing.*;
import view.*;


public class App extends JFrame {
    public App() {
        setTitle("Sistem Informasi Adopsi Hewan");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel header (logo + judul)
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        headerPanel.setBackground(Color.WHITE);

        // Tambahkan logo
        try {
            ImageIcon icon = new ImageIcon("src/assets/logo.png"); // path logo
            Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            headerPanel.add(logoLabel, BorderLayout.NORTH);
        } catch (Exception e) {
            System.out.println("Logo tidak ditemukan, lanjut tanpa logo.");
        }

        // Judul
        JLabel lblJudul = new JLabel("Menu Utama", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblJudul, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel tombol utama
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton btnHewan = new JButton("Data Hewan");
        JButton btnAdopter = new JButton("Data Adopter");
        JButton btnAdopsi = new JButton("Bukti Adopsi");
        JButton btnAdmin = new JButton("Manajemen Admin");
        JButton btnExit = new JButton("Logout / Keluar");

        Font buttonFont = new Font("SansSerif", Font.PLAIN, 16);
        for (JButton btn : new JButton[]{btnHewan, btnAdopter, btnAdopsi, btnAdmin, btnExit}) {
            btn.setFont(buttonFont);
            buttonPanel.add(btn);
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Aksi tombol
        btnHewan.addActionListener(e -> new HewanForm());
        btnAdopter.addActionListener(e -> new AdopterForm());
        btnAdopsi.addActionListener(e -> new AdopsiForm());
        btnAdmin.addActionListener(e -> new AdminForm());
        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); // menutup jendela
                System.exit(0); // keluar dari program
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}
