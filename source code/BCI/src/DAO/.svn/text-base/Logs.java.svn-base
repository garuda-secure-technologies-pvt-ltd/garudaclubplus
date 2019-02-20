/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import java.sql.Date;
import java.util.Comparator;

/**
 *
 * @author Ratan
 */
public class Logs {
    private String StaffCode;
    private Date logDate;

    public Logs(String StaffCode, Date logDate) {
        this.StaffCode = StaffCode;
        this.logDate = logDate;
    }

    public String getStaffCode() {
        return StaffCode;
    }

    public Date getLogDate() {
        return logDate;
    }

    @Override
    public boolean equals(Object o) {
        return this.getStaffCode().equals(((Logs)o).getStaffCode());
    }

    @Override
    public int hashCode() {
        return  super.hashCode();
    }



}
