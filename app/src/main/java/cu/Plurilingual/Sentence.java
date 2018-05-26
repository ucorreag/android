package cu.Plurilingual;

public class Sentence {
    private int id;
    private String language;
    private String Sentence;

    public Sentence(int id, String language, String sentence) {
        this.id = id;
        this.language=language;
        this.Sentence=sentence;
    }

    public int getId() {
        return this.id;
    }

    public String getLanguage() {
        return language;
    }

    public String getSentence() {
        return this.Sentence;
    }
}
