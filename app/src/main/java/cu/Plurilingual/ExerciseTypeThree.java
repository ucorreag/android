package cu.Plurilingual;

public class ExerciseTypeThree extends Exercise {
    String questionOne;
    Sentence answerOne;
    String getQuestionTwo;
    Sentence getAnswerTwo;
    String questionThree;
    Sentence getAnswerThree;

    public ExerciseTypeThree(int id, String questionOne, Sentence answerOne, String getQuestionTwo,
                             Sentence getAnswerTwo, String questionThree, Sentence getAnswerThree) {
        super(id);
        this.questionOne = questionOne;
        this.answerOne = answerOne;
        this.getQuestionTwo = getQuestionTwo;
        this.getAnswerTwo = getAnswerTwo;
        this.questionThree = questionThree;
        this.getAnswerThree = getAnswerThree;
    }

    public String getQuestionOne() {
        return questionOne;
    }

    public void setQuestionOne(String questionOne) {
        this.questionOne = questionOne;
    }

    public Sentence getAnswerOne() {
        return answerOne;
    }

    public void setAnswerOne(Sentence answerOne) {
        this.answerOne = answerOne;
    }

    public String getGetQuestionTwo() {
        return getQuestionTwo;
    }

    public void setGetQuestionTwo(String getQuestionTwo) {
        this.getQuestionTwo = getQuestionTwo;
    }

    public Sentence getGetAnswerTwo() {
        return getAnswerTwo;
    }

    public void setGetAnswerTwo(Sentence getAnswerTwo) {
        this.getAnswerTwo = getAnswerTwo;
    }

    public String getQuestionThree() {
        return questionThree;
    }

    public void setQuestionThree(String questionThree) {
        this.questionThree = questionThree;
    }

    public Sentence getGetAnswerThree() {
        return getAnswerThree;
    }

    public void setGetAnswerThree(Sentence getAnswerThree) {
        this.getAnswerThree = getAnswerThree;
    }
}
