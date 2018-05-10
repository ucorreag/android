package cu.Plurilingual;

import android.graphics.SumPathEffect;

public class ExerciseTypeOne extends Exercise {
    String question;
    Sentence optionOne;
    Sentence optionTwo;
    Sentence optionThree;
    Sentence optionFour;
    Sentence optionFive;
    Sentence answer;

    public ExerciseTypeOne(int id, String question, Sentence optionOne,
                           Sentence optionTwo, Sentence optionThree,
                           Sentence optionFour, Sentence optionFive, Sentence answer) {
        super(id);
        this.question = question;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        this.optionFour = optionFour;
        this.optionFive = optionFive;
        this.answer = answer;

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Sentence getOptionOne() {
        return optionOne;
    }

    public void setOptionOne(Sentence optionOne) {
        this.optionOne = optionOne;
    }

    public Sentence getOptionTwo() {
        return optionTwo;
    }

    public void setOptionTwo(Sentence optionTwo) {
        this.optionTwo = optionTwo;
    }

    public Sentence getOptionThree() {
        return optionThree;
    }

    public void setOptionThree(Sentence optionThree) {
        this.optionThree = optionThree;
    }

    public Sentence getOptionFour() {
        return optionFour;
    }

    public void setOptionFour(Sentence optionFour) {
        this.optionFour = optionFour;
    }

    public Sentence getOptionFive() {
        return optionFive;
    }

    public void setOptionFive(Sentence optionFive) {
        this.optionFive = optionFive;
    }

    public Sentence getAnswer() {
        return answer;
    }

    public void setAnswer(Sentence answer) {
        this.answer = answer;
    }
}
