package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.BillPeriods;
import java.sql.Connection;
import com.openbravo.pos.ticket.MinimumUsageInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author user
 */
public class MinUsageDAO extends BaseJdbcDAO {

    public double findUsage(BillPeriods bp,String mid) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        double usage=0.0;
        java.sql.Date sdateSql=new java.sql.Date(bp.getStartDate().getTime());
        java.sql.Date edateSql=new java.sql.Date(bp.getEndDate().getTime());




        String sql = "SELECT SUM(AMT) FROM (SELECT SUM(AMOUNT) AS AMT FROM BILL WHERE CUSTOMER=? AND CREATEDDATE>=? AND CREATEDDATE<=? UNION ALL"
 +" SELECT SUM(AMOUNT) AS AMT FROM BILL_ARV WHERE CUSTOMER=? AND CREATEDDATE>=? AND CREATEDDATE<=?) as amt";
        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sql);
            ps.setString(1, mid);
            ps.setDate(2,sdateSql);
            ps.setDate(3,edateSql);
            ps.setString(4, mid);
            ps.setDate(5,sdateSql);
            ps.setDate(6,edateSql);






            //execute
            rs = ps.executeQuery();
            
            if(rs.next())
            {
                usage=rs.getDouble(1);
            }

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






        return usage;
    }

    public List<MinimumUsageInfo> findMinUsage(String mid) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<MinimumUsageInfo> muinfo = null;


        String sql = "SELECT M.NAME,M.AMOUNT AS ALIMIT ,MU.LBILLDATE,P.TYPE_ AS PERIODTYPE,P.NO,P.DATE,P.ACCURATE,P.DOJ,P.FMONTH " +
                " FROM MINUSAGE M,MEMMINUSAGE MU,PERIODICITY P WHERE M.ACHEAD!='NULL' AND M.ID=MU.USAGETYPE AND P.ID=M.PERIOD AND MU.MEMNO=?";
        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sql);
            ps.setString(1, mid);



            //execute
            rs = ps.executeQuery();
            //transform to VO
            muinfo = transformSet(rs);

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



        return muinfo;

    }

    public List<MinimumUsageInfo> findMinUsageHsql(String mid) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<MinimumUsageInfo> muinfo = null;


        String sql = "SELECT M.NAME,M.AMOUNT AS ALIMIT ,MU.LBILLDATE,P.TYPE AS PERIODTYPE,P.NO,P.DATE,P.ACCURATE,P.DOJ,P.FMONTH" +
                " FROM MINUSAGE M,MEMMINUSAGE MU,PERIODICITY P WHERE M.ACHEAD!='NULL' AND M.ID=MU.USAGETYPE AND P.ID=M.PERIOD AND MU.MEMNO=?";
        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sql);
            ps.setString(1, mid);



            //execute
            rs = ps.executeQuery();
            //transform to VO
            muinfo = transformSet(rs);

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



        return muinfo;

    }

    @Override
    protected MinimumUsageInfo map2VO(ResultSet rs) throws SQLException {
        
           MinimumUsageInfo minfo = new MinimumUsageInfo();

            minfo.setMname(rs.getString(1));
           
            minfo.setLimit(rs.getDouble(2));
            
            minfo.setLBilldate(rs.getDate(3));
            minfo.setPtype(rs.getString(4));
            minfo.setPno(rs.getInt(5));
            minfo.setPdate(rs.getString(6));
            minfo.setPaccurate(rs.getBoolean(7));
            minfo.setPdoj(rs.getBoolean(8));


        return minfo;

    }
}
