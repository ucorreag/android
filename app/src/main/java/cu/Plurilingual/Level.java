package cu.Plurilingual;

public class Level {
    private int id;
    private String nivelName;

    public Level(int id, String nivelName) {
        this.id = id;
        this.nivelName = nivelName;
    }

    public int getId() {
        return id;
    }

    public String getNivelName() {
        return nivelName;
    }
}
