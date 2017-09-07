package model;

/**
 * Created by Andy on 5/23/2016.
 */
public class Login {
    private String uid;
    private String id;
    private String company_name;
    private int success;

    public Login() {

    }

    public Login(String u, String i, String c, int s) {
        this.uid = u;
        this.id = i;
        this.company_name = c;
        this.success = s;
    }

    public void setUid(String u) {
        this.uid = u;
    }

    public void setId(String i) {
        this.id = i;
    }

    public void setCompanyName(String c) {
        this.company_name = c;
    }

    public void setSuccess(int s) {
        this.success = s;
    }

    public String getUid() {
        return this.uid;
    }

    public String getId() {
        return this.id;
    }

    public String getCompanyName() {
        return this.company_name;
    }

    public int getSuccess() {
        return this.success;
    }

}
