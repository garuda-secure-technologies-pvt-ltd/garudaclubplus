package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.OtherFacility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OtherFacilityDAO extends BaseJdbcDAO {

    public List<OtherFacility> findOtherFacility(String memId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<OtherFacility> ofinfo = null;
        String sqlStr = "SELECT F.ID,F.SMSFORM,F.JOINAMT,F.RENEWALAMT,F.NAME FROM FACILITY F WHERE  F.ACTIVE =TRUE AND F.TYPE_='Optional'  AND (F.ID NOT IN (SELECT M.FACILITYTYPE FROM MEMFACILITYUSAGE M,CUSTOMERS C WHERE C.SEARCHKEY=? AND C.ID=M.MEMNO AND  M.ACTIVE=TRUE) OR F.APPTO=1) AND F.RENEWALAMT>0  ORDER BY F.NAME";


        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);// + end);
            ps.setString(1, memId);
            //execute
            rs = ps.executeQuery();
            //transform to VO
            //vos = transformSet(rs);
            ofinfo = (List<OtherFacility>) transformSet(rs);

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

        return ofinfo;
    }

    public List<OtherFacility> findOtherFacilityHsql(String memId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<OtherFacility> ofinfo = null;
        String sqlStr = "SELECT F.ID,F.SMSFORM,F.JOINAMT,F.RENEWALAMT,F.NAME FROM FACILITY F WHERE  F.ACTIVE =TRUE AND F.TYPE='Optional'  AND (F.ID NOT IN (SELECT M.FACILITYTYPE FROM MEMFACILITYUSAGE M,CUSTOMERS C WHERE C.SEARCHKEY=? AND C.ID=M.MEMNO AND  M.ACTIVE=TRUE) OR F.APPTO=1)  ORDER BY F.NAME";


        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);// + end);
            ps.setString(1, memId);
            //execute
            rs = ps.executeQuery();
            //transform to VO
            //vos = transformSet(rs);
            ofinfo = (List<OtherFacility>) transformSet(rs);

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

        return ofinfo;
    }
    @Override
    protected OtherFacility map2VO(ResultSet rs) throws SQLException {
        OtherFacility othfac = null;

        othfac = new OtherFacility();
        othfac.setFId(rs.getString(1));

        othfac.setFacilityName(rs.getString(2));
        othfac.setJoiningAmt(rs.getDouble(3));

        othfac.setRenewalAmt(rs.getDouble(4));
        othfac.setFNmae(rs.getString(5));
        othfac.setStatus("not requested");
        






        return othfac;
    }
}