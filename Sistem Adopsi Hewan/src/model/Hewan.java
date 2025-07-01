package model;

/**
 * Kelas Hewan merepresentasikan data hewan yang tersedia untuk diadopsi.
 */
public class Hewan {
    private int id;
    private String nama;
    private String jenis;
    private int umur;
    private String status;

    /**
     * Konstruktor untuk objek Hewan.
     */
    public Hewan(int id, String nama, String jenis, int umur, String status) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.umur = umur;
        this.status = status;
    }

    /**
     * Pola Builder untuk membangun objek Hewan.
     */
    public static class Builder {
        private int id;
        private String nama;
        private String jenis;
        private int umur;
        private String status;

        public Builder id(int id) { this.id = id; return this; }
        public Builder nama(String nama) { this.nama = nama; return this; }
        public Builder jenis(String jenis) { this.jenis = jenis; return this; }
        public Builder umur(int umur) { this.umur = umur; return this; }
        public Builder status(String status) { this.status = status; return this; }

        public Hewan build() {
            return new Hewan(id, nama, jenis, umur, status);
        }
    }

    // Getter dan Setter
    public int getId() { return this.id; }
    public String getNama() { return this.nama; }
    public String getJenis() { return this.jenis; }
    public int getUmur() { return this.umur; }
    public String getStatus() { return this.status; }

    public void setId(int id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public void setUmur(int umur) { this.umur = umur; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format(
            "Hewan{id=%d, nama='%s', jenis='%s', umur=%d, status='%s'}",
            id, nama, jenis, umur, status
        );
    }
}
