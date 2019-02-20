package com.openbravo.pos.pda.dao;

import java.sql.Connection;
import com.openbravo.pos.ticket.DeactivateInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DeactivateFacilityDAO extends BaseJdbcDAO {

    public DeactivateInfo deactivateFacility(String[] mfuId, Date date, String cid) {
        DeactivateInfo dinfo = new DeactivateInfo();

        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        String[] i = new String[20];
        String[] l = new String[20];
        java.sql.Date date3 = new java.sql.Date(new Date().getTime());
        java.sql.Date date5 = new java.sql.Date(date.getTime());
        ArrayList<String> temp1 = new ArrayList<String>();
        ArrayList<String> temp2 = new ArrayList<String>();
        String[] mfuid = mfuId;
        String sqlStr1 = "SELECT * FROM FACILITYMANAGER WHERE MFUID=? AND STATUS_ IS NULL";
        String sqlStr =  "INSERT INTO FACILITYMANAGER(ID,MFUID,TYPE_,FROM_,INITIATEDBY,INITIATEDDATE,MESSAGE) VALUES (?,?,?,?,?,?,?)";
        String sqlStr2 = "UPDATE MEMFACILITYUSAGE SET STATUS_=?,FACMANGREF=? WHERE ID=?";
        int j = 0;
        try {
            //get connection
            con = getConnection();
            //prepare statement
            for (j = 0; j < mfuid.length; j++) {
                ps1 = con.prepareStatement(sqlStr1);
                ps1.setString(1, mfuid[j]);
                ResultSet k = ps1.executeQuery();
                if (k.next()) {
                    temp1.add(mfuid[j]);
                } else {
                    try {
                        con.setAutoCommit(false);
                        ps = con.prepareStatement(sqlStr);
                        ps2 = con.prepareStatement(sqlStr2);
                        String manid = UUID.randomUUID().toString();
                        ps.setString(1, manid);
                        ps.setString(2, mfuid[j]);
                        ps.setInt(3, 3);
                        ps.setDate(4, date5);
                        ps.setString(5, cid);
                        ps.setDate(6, date3);
                        ps.setString(7, "stop dis facility from" + date5);
                        ps2.setInt(1, 3);
                        ps2.setString(2, manid);
                        ps2.setString(3, mfuid[j]);
                        ps.executeUpdate();
                        ps2.executeUpdate();
                        con.commit();
                        temp2.add(mfuid[j]);
                    } catch (SQLException sq1) {
                        con.rollback();
                        sq1.printStackTrace();
                    }
                }
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
        temp1.toArray(i);
        temp2.toArray(l);
        dinfo.setMsg(i);
        dinfo.setSucc(l);
        return dinfo;
    }
    public DeactivateInfo deactivateFacilityHsql(String[] mfuId, Date date, String cid) {
        DeactivateInfo dinfo = new DeactivateInfo();

        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        String[] i = new String[20];
        String[] l = new String[20];
        java.sql.Date date3 = new java.sql.Date(new Date().getTime());
        java.sql.Date date5 = new java.sql.Date(date.getTime());
        ArrayList<String> temp1 = new ArrayList<String>();
        ArrayList<String> temp2 = new ArrayList<String>();
        String[] mfuid = mfuId;
        String sqlStr1 = "SELECT * FROM FACILITYMANAGER WHERE MFUID=? AND STATUS IS NULL";
        String sqlStr =  "INSERT INTO FACILITYMANAGER(ID,MFUID,TYPE_,FROM_,INITIATEDBY,INITIATEDDATE,MESSAGE) VALUES (?,?,?,?,?,?,?)";
        String sqlStr2 = "UPDATE MEMFACILITYUSAGE SET STATUS=?,FACMANGREF=? WHERE ID=?";
        int j = 0;
        try {
            //get connection
            con = getConnection();
            //prepare statement
            for (j = 0; j < mfuid.length; j++) {
                ps1 = con.prepareStatement(sqlStr1);
                ps1.setString(1, mfuid[j]);
                ResultSet k = ps1.executeQuery();
                if (k.next()) {
                    temp1.add(mfuid[j]);
                } else {
                    try {
                        con.setAutoCommit(false);
                        ps = con.prepareStatement(sqlStr);
                        ps2 = con.prepareStatement(sqlStr2);
                        String manid = UUID.randomUUID().toString();
                        ps.setString(1, manid);
                        ps.setString(2, mfuid[j]);
                        ps.setInt(3, 3);
                        ps.setDate(4, date5);
                        ps.setString(5, cid);
                        ps.setDate(6, date3);
                        ps.setString(7, "stop dis facility from" + date5);
                        ps2.setInt(1, 3);
                        ps2.setString(2, manid);
                        ps2.setString(3, mfuid[j]);
                        ps.executeUpdate();
                        ps2.executeUpdate();
                        con.commit();
                        temp2.add(mfuid[j]);
                    } catch (SQLException sq1) {
                        con.rollback();
                        sq1.printStackTrace();
                    }
                }
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
        temp1.toArray(i);
        temp2.toArray(l);
        dinfo.setMsg(i);
        dinfo.setSucc(l);
        return dinfo;
    }

    @Override
    protected Object map2VO(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
