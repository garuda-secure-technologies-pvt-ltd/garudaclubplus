package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.ActiveFacility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ActiveFacilityDAO extends BaseJdbcDAO {

    public List<ActiveFacility> findActiveFacility(String memId) {

        Connection con = null;
        PreparedStatement ps = null;
        List<ActiveFacility> afinfo = null;        
        String sqlStr = "SELECT M.ID,F.SMSFORM,F.RENEWALAMT,M.STATUS_,F.TYPE_,F.NAME,CASE WHEN M.USERID IS NOT NULL THEN( SELECT DNAME FROM MEMDEPENDENT M1,MEMFACILITYUSAGE M WHERE M1.ID = M.USERID AND NULL) END,M.USERID,FM.TYPE_,M.FACMANGREF FROM CUSTOMERS C, MEMFACILITYUSAGE M JOIN FACILITY F ON  M.FACILITYTYPE=F.ID JOIN PERIODICITY P ON P.ID=F.RPERIODICITY LEFT OUTER JOIN FACILITYMANAGER FM ON M.ID=FM.MFUID AND FM.APPROVEDBY IS NULL WHERE C.SEARCHKEY=? AND M.MEMNO=C.ID AND M.STATUS_ !=2 AND M.ACTIVE=TRUE AND F.RENEWALAMT>0";
        ResultSet rs = null;
        
        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);// + end);
            ps.setString(1, memId);
            rs = ps.executeQuery();
            afinfo = (List<ActiveFacility>) transformSet(rs);
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

        return afinfo;
    }

    public List<ActiveFacility> findActiveFacilityHsql(String memId) {

        Connection con = null;
        PreparedStatement ps = null;
        List<ActiveFacility> afinfo = null;
        //String sqlStr = "SELECT M.ID,F.SMSFORM,F.RENEWALAMT,M.STATUS,F.TYPE,F.NAME,CASE WHEN M.USERID IS NOT NULL THEN  (SELECT DNAME FROM MEMDEPENDENT WHERE ID = M.USERID) ELSE NULL END FROM FACILITY F,MEMFACILITYUSAGE M,CUSTOMERS C WHERE M.MEMNO=C.ID AND C.SEARCHKEY=? AND M.FACILITYTYPE=F.ID  AND M.ACTIVE = TRUE ORDER BY F.NAME";
          String sqlStr = "SELECT M.ID,F.SMSFORM,F.RENEWALAMT,M.STATUS,F.TYPE,F.NAME,CASE WHEN M.USERID IS NOT NULL THEN (SELECT DNAME FROM MEMDEPENDENT WHERE ID = M.USERID)ELSE NULL END,FM.TYPE_  FROM MEMFACILITYUSAGE M,CUSTOMERS C JOIN FACILITY F ON M.FACILITYTYPE=F.ID JOIN PERIODICITY P ON P.ID=F.RPERIODICITY LEFT OUTER JOIN FACILITYMANAGER FM ON M.ID=FM.MFUID AND FM.APPROVEDBY IS NULL WHERE C.SEARCHKEY=? AND M.MEMNO=C.ID AND STATUS != 2 AND M.ACTIVE=TRUE";
        ResultSet rs = null;

        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);// + end);
            ps.setString(1, memId);
            rs = ps.executeQuery();
            afinfo = (List<ActiveFacility>) transformSet(rs);
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

        return afinfo;
    }

    @Override
    protected ActiveFacility map2VO(ResultSet rs) throws SQLException {

        ActiveFacility actfac = null;
        actfac = new ActiveFacility();
        actfac.setMfuId(rs.getString(1));
        actfac.setFacilityName(rs.getString(2));
        actfac.setRenewalAmount(rs.getDouble(3));
        actfac.setType(rs.getString(5));
        actfac.setFName(rs.getString(6));
        if (rs.getString(7) == null) {
            actfac.setDName("Member");
        } else {
            actfac.setDName(rs.getString(7));
        }
        switch (rs.getInt(4)) {
            case 0:
                actfac.setStatus("In Usage");
                break;
            case 1:
                actfac.setStatus("Suspended");
                break;
            case 2:
                actfac.setStatus("Deactivated");
                break;
            case 3:
                actfac.setStatus("Stop Req. under process");
                break;
            case 4:
                actfac.setStatus("Suspension Req. under process");
                break;
            case 5:
                actfac.setStatus("Start Req. under process");
                break;
            /*default:
            actfac.setStatus("In Usage");*/
        }
        return actfac;
    }
}