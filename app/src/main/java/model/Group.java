package model;

/**
 * Created by Andy on 5/23/2016.
 */
public class Group {
    private String id;
    private String name;

    public Group() {

    }

    public Group(String i, String n) {
        this.id = i;
        this.name = n;
    }

    public void setId(String i) {
        this.id = i;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
