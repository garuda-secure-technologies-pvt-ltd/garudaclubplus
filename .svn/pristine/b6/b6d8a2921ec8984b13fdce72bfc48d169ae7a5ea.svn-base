package com.openbravo.pos.pda.dao;

import java.sql.Connection;
import com.openbravo.pos.ticket.EventsInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class EventsDAO extends BaseJdbcDAO {

    public List<EventsInfo> findEvents() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<EventsInfo> einfo = null;
        java.util.Date today = new java.util.Date();
        java.sql.Date sqlToday = new java.sql.Date(today.getTime());

        String sql = "SELECT NAME,DESCRIPTION,EDATE,STIME,ETIME FROM EVENTS WHERE EDATE>=? AND ACTIVE=1 ORDER BY EDATE";
        try {
            //get connection,
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sql);
            ps.setDate(1, sqlToday);
           
            //execute
            rs = ps.executeQuery();
            //transform to VO
            einfo = transformSet(rs);

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
        return einfo;

    }

    @Override
    protected EventsInfo map2VO(ResultSet rs) throws SQLException {
        DateFormat df=new SimpleDateFormat("dd-MM-yy");
        EventsInfo eventinfo = new EventsInfo();
        eventinfo.setEname(rs.getString(1));
        eventinfo.setDescription(rs.getString(2));
        eventinfo.setEdate(df.format(rs.getDate(3)));

        eventinfo.setStime(rs.getString(4));
        eventinfo.setEtime(rs.getString(5));

        
        return eventinfo;

    }
}
