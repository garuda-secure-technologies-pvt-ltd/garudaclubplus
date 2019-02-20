

package com.openbravo.pos.ticket;


public class OtherFacility {
    private String facilityName;
    private String fId;
    private String fNmae;
    private double joiningAmt;
     private double renewalAmt;
     private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   
    public String getFNmae() {
        return fNmae;
    }

    public void setFNmae(String fNmae) {
        this.fNmae = fNmae;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public String getFId() {
        return fId;
    }

    public void setFId(String fId) {
        this.fId = fId;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public double getJoiningAmt() {
        return joiningAmt;
    }

    public void setJoiningAmt(double joiningAmt) {
        this.joiningAmt = joiningAmt;
    }

    public double getRenewalAmt() {
        return renewalAmt;
    }

    public void setRenewalAmt(double renewalAmt) {
        this.renewalAmt = renewalAmt;
    }

    


}
