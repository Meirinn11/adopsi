package model;

import java.time.LocalDate;

/**
 * Kelas Adopsi merepresentasikan proses adopsi hewan oleh adopter.
 */
public class Adopsi {
    private int id;
    private int idHewan;
    private int idAdopter;
    private LocalDate tanggal;

    /**
     * Konstruktor untuk membuat objek Adopsi.
     */
    public Adopsi(int id, int idHewan, int idAdopter, LocalDate tanggal) {
        this.id = id;
        this.idHewan = idHewan;
        this.idAdopter = idAdopter;
        this.tanggal = tanggal;
    }

    /**
     * Pola Builder untuk membangun objek Adopsi.
     */
    public static class Builder {
        private int id;
        private int idHewan;
        private int idAdopter;
        private LocalDate tanggal;

        public Builder id(int id) { this.id = id; return this; }
        public Builder idHewan(int idHewan) { this.idHewan = idHewan; return this; }
        public Builder idAdopter(int idAdopter) { this.idAdopter = idAdopter; return this; }
        public Builder tanggal(LocalDate tanggal) { this.tanggal = tanggal; return this; }

        public Adopsi build() {
            return new Adopsi(id, idHewan, idAdopter, tanggal);
        }
    }

    // Getter dan Setter
    public int getId() { return this.id; }
    public int getIdHewan() { return this.idHewan; }
    public int getIdAdopter() { return this.idAdopter; }
    public LocalDate getTanggal() { return this.tanggal; }

    public void setId(int id) { this.id = id; }
    public void setIdHewan(int idHewan) { this.idHewan = idHewan; }
    public void setIdAdopter(int idAdopter) { this.idAdopter = idAdopter; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }

    @Override
    public String toString() {
        return String.format(
            "Adopsi{id=%d, idHewan=%d, idAdopter=%d, tanggal=%s}",
            id, idHewan, idAdopter, tanggal.toString()
        );
    }
}
