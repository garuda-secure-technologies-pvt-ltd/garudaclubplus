
package com.openbravo.pos.pda.dao;


import com.openbravo.pos.ticket.AbbrivationInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
    

public class AbbrivationDAO extends BaseJdbcDAO{

    public List<AbbrivationInfo> findAbbrivation(){

        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<AbbrivationInfo> abbrIn=null;
        String sqlStr = "SELECT NAME,SMSFORM FROM FACILITY ORDER BY NAME";
         try {
        con = getConnection();

            ps = con.prepareStatement(sqlStr);

            rs = ps.executeQuery();

            abbrIn = (List<AbbrivationInfo> ) transformSet(rs);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
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




        return abbrIn;


    }
    public List<AbbrivationInfo> findAbbrivationHsql(){

        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<AbbrivationInfo> abbrIn=null;
        String sqlStr = "SELECT NAME,SMSFORM FROM FACILITY ORDER BY NAME";
         try {
        con = getConnection();

            ps = con.prepareStatement(sqlStr);

            rs = ps.executeQuery();

            abbrIn = (List<AbbrivationInfo> ) transformSet(rs);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
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




        return abbrIn;


    }

   
    @Override
    public Object map2VO(ResultSet rs) throws SQLException {
         AbbrivationInfo abinfo = new AbbrivationInfo();
         
         abinfo.setFName(rs.getString(1));
         abinfo.setSmsForm(rs.getString(2));
         
         return abinfo;

        
    }

}
