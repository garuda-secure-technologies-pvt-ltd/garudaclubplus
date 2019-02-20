package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.TransactionInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionDAO extends BaseJdbcDAO {

    public List<TransactionInfo> findLastTransactions(String accid) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TransactionInfo> tinfo = null;
        


        String sql = "SELECT AJ.DATE,AJ.TRANSNO,AJ.TRANSTYPE,AJ.AMOUNT,F.SMSFORM,AJ.TRANSREF FROM ACCOUNTJOURNAL AJ LEFT JOIN FACILITY F ON AJ.TRANSREF=F.ID " +
                " WHERE AJ.ACCOUNTID=?  ORDER BY AJ.DATE DESC LIMIT 5";
        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sql);
            ps.setString(1, accid);



            //execute
            rs = ps.executeQuery();
            //transform to VO
            tinfo = transformSet(rs);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                // close the resources
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqlee) {
                sqlee.printStackTrace();
            }
        }



        return tinfo;

    }

    @Override
    protected TransactionInfo map2VO(ResultSet rs) throws SQLException {
        TransactionInfo transinfo = new TransactionInfo();
        DateFormat df=new SimpleDateFormat("dd/MM/yy");
        DecimalFormat nf1=new DecimalFormat("###0.00");
        transinfo.setTdate(df.format(rs.getDate(1)));
        transinfo.setReceiptno(rs.getString(2));
        transinfo.setTranstype(rs.getString(3));
        transinfo.setAmount(nf1.format(rs.getDouble(4)));
        if(rs.getString(5)!=null)
        transinfo.setFacility(rs.getString(5));
        else
            transinfo.setFacility(rs.getString(6));


        return transinfo;
    }
}
