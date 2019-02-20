

package com.openbravo.pos.payment;

public class PaymentInfoFree extends PaymentInfo {
    
    private double m_dTotal;
   
    /** Creates a new instance of PaymentInfoFree */
    public PaymentInfoFree(double dTotal) {
        m_dTotal = dTotal;
    }
    
    public PaymentInfo copyPayment(){
        return new PaymentInfoFree(m_dTotal);
    }    
    public String getName() {
        return "free";
    }   
    public double getTotal() {
        return m_dTotal;
    }

    @Override
    public ChequeDetails getChequeDetails() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
