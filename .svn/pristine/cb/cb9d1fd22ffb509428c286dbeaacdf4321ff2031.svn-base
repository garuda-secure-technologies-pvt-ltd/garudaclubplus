package com.openbravo.pos.pda.dao;

import java.sql.Connection;
import com.openbravo.pos.ticket.BalanceInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author user
 */
public class BalanceDAO extends BaseJdbcDAO {

    public BalanceInfo findbalance(String accid) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BalanceInfo binfo = null;


        String sql = "SELECT SUM(A.CREDIT+A.CURCREDIT-A.DEBIT-A.CURDEBIT) FROM AJPERIODTOTALS A WHERE A.ACCOUNTID=?";
        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sql);
            ps.setString(1, accid);



            //execute
            rs = ps.executeQuery();
            //transform to VO
            binfo = map2VO(rs);

        } catch (SQLException ex) {
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

        return binfo;

    }

    @Override
    protected BalanceInfo map2VO(ResultSet rs) throws SQLException {
        BalanceInfo binfo = null;
        if (rs.next()) {
            binfo = new BalanceInfo();

            binfo.setBal(Double.parseDouble(rs.getString(1)));
        }
        return binfo;

    }
}
