package cu.Plurilingual;

public class ExerciseTypeFour extends Exercise {
    Sentence question;

    public ExerciseTypeFour(int id, Sentence question) {
        super(id);
        this.question = question;
    }

    public Sentence getQuestion() {
        return question;
    }

    public void setQuestion(Sentence question) {
        this.question = question;
    }
}
