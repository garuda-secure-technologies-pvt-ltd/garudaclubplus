


package com.openbravo.pos.payment;

import com.openbravo.format.Formats;

public abstract class PaymentInfo {
    
    public abstract String getName();
    public abstract double getTotal();
    public abstract PaymentInfo copyPayment();
    public abstract ChequeDetails getChequeDetails();
    
    public String printTotal() {
        return Formats.CURRENCY.formatValue(new Double(getTotal()));
    }
    
}
