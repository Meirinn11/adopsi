package model;

/**
 * Kelas Adopter merepresentasikan data individu yang mengadopsi hewan.
 */
public class Adopter {
    private int id;
    private String nama;
    private String alamat;
    private String noTelp;

    public Adopter(int id, String nama, String alamat, String noTelp) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.noTelp = noTelp;
    }

    /**
     * Pola Builder untuk membuat objek Adopter.
     */
    public static class Builder {
        private int id;
        private String nama;
        private String alamat;
        private String noTelp;

        public Builder id(int id) { this.id = id; return this; }
        public Builder nama(String nama) { this.nama = nama; return this; }
        public Builder alamat(String alamat) { this.alamat = alamat; return this; }
        public Builder noTelp(String noTelp) { this.noTelp = noTelp; return this; }

        public Adopter build() {
            return new Adopter(id, nama, alamat, noTelp);
        }
    }

    // Getter dan Setter
    public int getId() { return this.id; }
    public String getNama() { return this.nama; }
    public String getAlamat() { return this.alamat; }
    public String getNoTelp() { return this.noTelp; }

    public void setId(int id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }

    @Override
    public String toString() {
        return String.format(
            "Adopter{id=%d, nama='%s', alamat='%s', noTelp='%s'}",
            id, nama, alamat, noTelp
        );
    }
}