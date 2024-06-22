public class Patron {
    private int patronId;
    private String name;
    private String email; // New field

    public Patron(int patronId, String name, String email) {
        this.patronId = patronId;
        this.name = name;
        this.email = email;
    }

    public Patron(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Patron(String text) {
    }

    public Patron(int i, String text) {
    }

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
