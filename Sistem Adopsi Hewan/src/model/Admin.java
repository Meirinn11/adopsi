package model;

public class Admin {
    private int id;
    private String username;
    private String password;

    public Admin(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Builder pattern
    public static class Builder {
        private int id;
        private String username;
        private String password;

        public Builder id(int id) { this.id = id; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder password(String password) { this.password = password; return this; }

        public Admin build() {
            return new Admin(id, username, password);
        }
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}