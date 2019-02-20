
package com.openbravo.pos.ticket;

import java.util.Date;

/**
 *
 * @author jaroslawwozniak
 */
public class UserInfo {

    private String searchkey;
    private String password;
    private String name;
    private String accid;
    private String mid;
    private Date joindate;
    private boolean passrest;

    public boolean isPassrest() {
        return passrest;
    }

    public void setPassrest(boolean passrest) {
        this.passrest = passrest;
    }

    

    public Date getJoindate() {
        return joindate;
    }

    public void setJoindate(Date joindate) {
        this.joindate = joindate;
    }

    

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
    

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSearchkey() {
        return searchkey;
    }

    public void setSearchkey(String searchkey) {
        this.searchkey = searchkey;
    }



    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
