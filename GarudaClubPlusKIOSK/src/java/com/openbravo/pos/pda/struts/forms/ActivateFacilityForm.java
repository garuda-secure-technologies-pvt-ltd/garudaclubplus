package com.openbravo.pos.pda.struts.forms;



public class ActivateFacilityForm {
  
    private String date;
    private String[] fid;

    public String[] getFid() {
        return fid;
    }

    public void setFid(String[] fid) {
        this.fid = fid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
