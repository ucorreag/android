package cu.Plurilingual;

public class Subject {
    private int id;
    private String subject;
    public Subject(int id, String subject) {
        this.id=id;
        this.subject=subject;
    }


    public  String getSubject(){
        return this.subject;
    }
    public  int getId(){
        return this.id;
    }

}
