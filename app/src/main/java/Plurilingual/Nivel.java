package Plurilingual;

/**
 * Created by Isai on 2/02/2018.
 */

public class Nivel {
    private int id;
    private String nivelName;

    public Nivel(int id, String nivelName) {
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
