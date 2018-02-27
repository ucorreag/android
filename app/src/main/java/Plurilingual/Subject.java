package Plurilingual;

/**
 * Created by Isai on 7/01/2018.
 */

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
