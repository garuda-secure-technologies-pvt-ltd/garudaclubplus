package com.openbravo.pos.pda.dao;

import com.openbravo.pos.pda.util.StringUtils;
import com.openbravo.pos.ticket.UserInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jaroslawwozniak
 */
public class LoginDAO extends BaseJdbcDAO {

    public UserInfo findUser(String login, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserInfo user = null;
        String sqlStr = "SELECT SEARCHKEY, PASSWORD,NAME,ACCOUNT,ID,JOINDATE,PASSREST FROM CUSTOMERS WHERE UPPER(SEARCHKEY) = ? AND PASSWORD ";
        String end = "";
        if (password.equals("")) {
            end = "IS NULL";
        } else {
            end = " = ?";
        }

        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr + end);
            ps.setString(1, login.toUpperCase());
            if (!password.equals("")) {
                ps.setString(2, StringUtils.hashString(password));
            }

            //execute
            rs = ps.executeQuery();
            //transform to VO
            user = map2VO(rs);

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

        return user;
    }

    @Override
    protected UserInfo map2VO(ResultSet rs) throws SQLException {
        UserInfo user = null;
        if (rs.next()) {

            user = new UserInfo();
            user.setSearchkey(rs.getString(1));
            if (rs.getString(2) == null) {
                user.setPassword("");
            } else {
                user.setPassword(rs.getString(2));
            }
            user.setName(rs.getString(3));
            user.setAccid(rs.getString(4));
            user.setMid(rs.getString(5));
            user.setJoindate(rs.getDate(6));
            user.setPassrest(rs.getBoolean(7));

        }
        return user;
    }
}
