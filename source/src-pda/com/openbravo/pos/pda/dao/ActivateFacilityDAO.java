package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.ActivateInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ActivateFacilityDAO extends BaseJdbcDAO {

    public ActivateInfo activateFacility(String[] fId, Date date, String memid) {

        ActivateInfo atinfo = new ActivateInfo();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;        
        PreparedStatement ps3 = null;      
        java.sql.Date date3 = new java.sql.Date(new Date().getTime());
        java.sql.Date date5 = new java.sql.Date(date.getTime());
        String[] fid = fId;
        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<String> temp1 = new ArrayList<String>();  
        String[] suc = new String[20];
        String[] fai = new String[20];
        String sqlStr = "INSERT INTO FACILITYMANAGER(ID,MFUID,TYPE_,FROM_,INITIATEDBY,INITIATEDDATE) VALUES (?,?,?,?,?,?)";
        String sqlStr1 = "INSERT INTO MEMFACILITYUSAGE(ID,MEMNO,ACTIVE,FACILITYTYPE,STATUS_,SDATE,CREATEDBY,CRDATE,FACMANGREF) VALUES (?,?,?,?,?,?,?,?,?)";
        String sqlStr3 = "SELECT F.ID FROM MEMFACILITYUSAGE M,FACILITYMANAGER F WHERE M.MEMNO=? AND M.FACILITYTYPE=? AND M.ID=F.MFUID AND F.STATUS_ IS NULL";
        try {
            //get connection
            con = getConnection();
            for (int j = 0; j < fid.length; j++) {
                try {
                    ps3 = con.prepareStatement(sqlStr3);
                    ps3.setString(1, memid);
                    ps3.setString(2, fid[j]);
                    ResultSet r = ps3.executeQuery();
                    if (r.next()) {                        
                        temp1.add(fid[j]);
                    } else {
                        con.setAutoCommit(false);
                        String fmid = UUID.randomUUID().toString();
                        String mfuid = UUID.randomUUID().toString();
                        ps1 = con.prepareStatement(sqlStr1);
                        ps1.setString(1, mfuid);
                        ps1.setString(2, memid);
                        ps1.setBoolean(3, false);
                        ps1.setString(4, fid[j]);
                        ps1.setInt(5, 5);
                        ps1.setDate(6, date5);
                        ps1.setString(7, memid);
                        ps1.setDate(8, date3);
                        ps1.setString(9,fmid );
                        ps1.executeUpdate();
                        ps = con.prepareStatement(sqlStr);
                        ps.setString(1, fmid);
                        ps.setString(2, mfuid);
                        ps.setInt(3, 5);
                        ps.setDate(4, date5);
                        ps.setString(5, memid);
                        ps.setDate(6, date3);
                        ps.executeUpdate();                       
                        con.commit();
                        temp.add(fid[j]);                       
                    }
                } catch (SQLException sQLException) {
                    con.rollback();
                    sQLException.printStackTrace();
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
        temp.toArray(suc);
        temp1.toArray(fai);

        atinfo.setSucc(suc);
        atinfo.setFail(fai);
        
        
        return atinfo;
    }
    public ActivateInfo activateFacilityHsql(String[] fId, Date date, String memid) {

        ActivateInfo atinfo = new ActivateInfo();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps3 = null;
        java.sql.Date date3 = new java.sql.Date(new Date().getTime());
        java.sql.Date date5 = new java.sql.Date(date.getTime());
        String[] fid = fId;
        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<String> temp1 = new ArrayList<String>();
        String[] suc = new String[20];
        String[] fai = new String[20];
        String sqlStr = "INSERT INTO FACILITYMANAGER(ID,MFUID,TYPE_,FROM_,INITIATEDBY,INITIATEDDATE) VALUES (?,?,?,?,?,?)";
        String sqlStr1 = "INSERT INTO MEMFACILITYUSAGE(ID,MEMNO,ACTIVE,FACILITYTYPE,STATUS,SDATE,CREATEDBY,CRDATE,FACMANGREF) VALUES (?,?,?,?,?,?,?,?,?)";
        String sqlStr3 = "SELECT F.ID FROM MEMFACILITYUSAGE M,FACILITYMANAGER F WHERE M.MEMNO=? AND M.FACILITYTYPE=? AND M.ID=F.MFUID AND F.STATUS IS NULL";
        try {
            //get connection
            con = getConnection();
            for (int j = 0; j < fid.length; j++) {
                try {
                    ps3 = con.prepareStatement(sqlStr3);
                    ps3.setString(1, memid);
                    ps3.setString(2, fid[j]);
                    ResultSet r = ps3.executeQuery();
                    if (r.next()) {
                        temp1.add(fid[j]);
                    } else {
                        con.setAutoCommit(false);
                        String fmid = UUID.randomUUID().toString();
                        String mfuid = UUID.randomUUID().toString();
                        ps1 = con.prepareStatement(sqlStr1);
                        ps1.setString(1, mfuid);
                        ps1.setString(2, memid);
                        ps1.setBoolean(3, false);
                        ps1.setString(4, fid[j]);
                        ps1.setInt(5, 5);
                        ps1.setDate(6, date5);
                        ps1.setString(7, memid);
                        ps1.setDate(8, date3);
                        ps1.setString(9,fmid );
                        ps1.executeUpdate();
                        ps = con.prepareStatement(sqlStr);
                        ps.setString(1, fmid);
                        ps.setString(2, mfuid);
                        ps.setInt(3, 5);
                        ps.setDate(4, date5);
                        ps.setString(5, memid);
                        ps.setDate(6, date3);
                        ps.executeUpdate();
                        con.commit();
                        temp.add(fid[j]);
                    }
                } catch (SQLException sQLException) {
                    con.rollback();
                    sQLException.printStackTrace();
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
        temp.toArray(suc);
        temp1.toArray(fai);

        atinfo.setSucc(suc);
        atinfo.setFail(fai);


        return atinfo;
    }

    @Override
    protected Object map2VO(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
