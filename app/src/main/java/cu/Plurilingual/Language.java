package cu.Plurilingual;

public class Language {
    private int id;
    private String lang;
    private String iso; //key

    public Language(int id, String lang, String iso) {
        this.id = id;
        this.lang = lang;
        this.iso = iso;
    }

    public int getId() {
        return id;
    }

    public String getLang() {
        return lang;
    }

    public String getIso() {
        return iso;
    }
}
