package cu.Plurilingual;

public class ExerciseTypeTwo extends Exercise {

    Sentence question;
    Sentence answer;

    public ExerciseTypeTwo(int id, Sentence question, Sentence answer) {
        super(id);
        this.question = question;
        this.answer = answer;
    }

    public Sentence getQuestion() {
        return question;
    }

    public void setQuestion(Sentence question) {
        this.question = question;
    }

    public Sentence getAnswer() {
        return answer;
    }

    public void setAnswer(Sentence answer) {
        this.answer = answer;
    }
}
