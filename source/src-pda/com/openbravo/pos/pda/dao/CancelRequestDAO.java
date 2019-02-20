package com.openbravo.pos.pda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CancelRequestDAO extends BaseJdbcDAO {

    public int deleteRequestedFacility(String mfuid) {
        int count=0;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        ResultSet rs = null;
        int status = 0;
        String sqlStr = "DELETE FROM FACILITYMANAGER WHERE MFUID=? AND STATUS_ IS NULL";
        String sqlStr1 = "UPDATE MEMFACILITYUSAGE SET STATUS_=? WHERE ID=? ";
        String sqlStr2 = "DELETE FROM MEMFACILITYUSAGE WHERE ID=?";
        String sqlStr3 = "SELECT STATUS_ FROM MEMFACILITYUSAGE WHERE ID=?";
        
        try {
            con = getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(sqlStr);
            ps.setString(1, mfuid);
            ps.executeUpdate();
            ps3 = con.prepareStatement(sqlStr3);
            ps3.setString(1, mfuid);
            rs = ps3.executeQuery();
            if (rs.next()) {
                status = rs.getInt(1);
            }
            if (status == 3) {
                ps1 = con.prepareStatement(sqlStr1);
                ps1.setInt(1, 0);
                ps1.setString(2, mfuid);
                ps1.executeUpdate();
            } else {
                ps2 = con.prepareStatement(sqlStr2);
                ps2.setString(1, mfuid);
                ps2.executeUpdate();
            }
            con.commit();
            count=1;
        } catch (Exception ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace();
            }
            ex.printStackTrace();
        } finally {
            {
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
           
        }
        return count;
    }
    public int deleteRequestedFacilityHsql(String mfuid) {
        int count=0;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        ResultSet rs = null;
        int status = 0;
        String sqlStr = "DELETE FROM FACILITYMANAGER WHERE MFUID=? AND STATUS IS NULL";
        String sqlStr1 = "UPDATE MEMFACILITYUSAGE SET STATUS=? WHERE ID=? ";
        String sqlStr2 = "DELETE FROM MEMFACILITYUSAGE WHERE ID=?";
        String sqlStr3 = "SELECT STATUS FROM MEMFACILITYUSAGE WHERE ID=?";

        try {
            con = getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(sqlStr);
            ps.setString(1, mfuid);
            ps.executeUpdate();
            ps3 = con.prepareStatement(sqlStr3);
            ps3.setString(1, mfuid);
            rs = ps3.executeQuery();
            if (rs.next()) {
                status = rs.getInt(1);
            }
            if (status == 3) {
                ps1 = con.prepareStatement(sqlStr1);
                ps1.setInt(1, 0);
                ps1.setString(2, mfuid);
                ps1.executeUpdate();
            } else {
                ps2 = con.prepareStatement(sqlStr2);
                ps2.setString(1, mfuid);
                ps2.executeUpdate();
            }
            con.commit();
            count=1;
        } catch (Exception ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace();
            }
            ex.printStackTrace();
        } finally {
            {
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

        }
        return count;
    }


    @Override
    protected Object map2VO(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
