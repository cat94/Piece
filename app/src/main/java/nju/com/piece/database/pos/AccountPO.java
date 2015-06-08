package nju.com.piece.database.pos;

/**
 * Created by shen on 15/6/8.
 */
public class AccountPO {
    private String name;
    private String pswd;

    public AccountPO(String name, String pswd) {
        this.name = name;
        this.pswd = pswd;
    }

    public String getPswd() {
        return pswd;
    }

    public String getName() {
        return name;
    }
}
