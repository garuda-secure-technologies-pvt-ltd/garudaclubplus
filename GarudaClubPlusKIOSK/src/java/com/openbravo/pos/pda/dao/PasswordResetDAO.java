/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.pda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import com.openbravo.pos.pda.util.*;


/**
 *
 * @author user
 */
public class PasswordResetDAO extends BaseJdbcDAO {

    public void changePassword(String memid, String memname, String newPass) {
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        String pid = UUID.randomUUID().toString();
        String encPass=StringUtils.hashString(newPass);
        

        String sql1 = "UPDATE CUSTOMERS SET PASSWORD=?,PASSREST=TRUE WHERE ID=?";
        String sql2 = "INSERT INTO PASSWORDLOG(ID,CUSTID,PASSWORD,CRDATE,CREATEDBY) VALUES (?,?,?,?,?)";

        try {
            //get connection
            con = getConnection();
            con.setAutoCommit(false);
            //prepare statement
            ps1 = con.prepareStatement(sql1);
            ps1.setString(1, encPass);
            ps1.setString(2, memid);

            ps2 = con.prepareStatement(sql2);
            ps2.setString(1, pid);
            ps2.setString(2, memid);
            ps2.setString(3, encPass);
            ps2.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
            ps2.setString(5, memname);

            ps1.executeUpdate();
            ps2.executeUpdate();

            con.commit();


        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace();
            }
            ex.printStackTrace();
        } finally {
            try {
                // close the resources
                if (ps1 != null) {
                    ps1.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqlee) {
                sqlee.printStackTrace();
            }
        }


    }

    @Override
    protected Object map2VO(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
