/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.pda.struts;

import com.openbravo.pos.pda.dao.BaseJdbcDAO;
import com.openbravo.pos.pda.struts.beans.CompanyInfo;
import com.openbravo.pos.pda.util.PropertyUtils;
import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author swathi
 */
public class HomePageAction extends ActionSupport implements SessionAware{
    
    
    private PropertyUtils properties;
    private Connection conn=null;
    CompanyInfo cInfo = null;
    BaseJdbcDAO baseDAO;
    boolean var = false;

    public HomePageAction() {
        baseDAO = new BaseJdbcDAO() {

            @Override
            public Object map2VO(ResultSet rs) throws SQLException {
                if(!var)
                {
                   CompanyInfo cInfo2 = new CompanyInfo();
                   if (rs.next()) {
                    
                       cInfo2.setId(rs.getString(1));
                       cInfo2.setName(rs.getString(2));
                       cInfo2.setDesc(rs.getString(3));
                       cInfo2.setAddress(rs.getString(4));
                       cInfo2.setLogoPath(rs.getString(5));
                   }
                   return cInfo2;
                }
                
                return null;
            }
        };
        properties = new PropertyUtils();
    }
    
    @Override
    public String execute() {
    if(cInfo==null)
    {
        addActionError(getText("errors.loginfailed"));
        return Constants.FAILURE;
    }
    else
        return Constants.SUCCESS;
    
    }

    public CompanyInfo getcInfo() {
        return cInfo;
    }

    public void setcInfo(CompanyInfo cInfo) {
        this.cInfo = cInfo;
    }
    
    @Override
    public void setSession(Map map) {
        
    }
    
    private CompanyInfo getCompInfo()
    {
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sqlStr = "SELECT ID,NAME,DESCR,ADDRESS, LOGO FROM COMPANY WHERE name = ?";
        
        try
        {
            conn = baseDAO.getConnection();
            if(conn!=null)
            {
                ps = conn.prepareStatement("SELECT ID,NAME,DESCR,ADDRESS, LOGO FROM COMPANY WHERE name = ?");
                ps.setString(1, properties.getCompName());
                rs = ps.executeQuery();
                cInfo = (CompanyInfo) baseDAO.map2VO(rs);
            }
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        } finally {
            try {
                // close the resources 
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqlee) {
                sqlee.printStackTrace();
            }
        }
       
        return cInfo;
    }
    
}
