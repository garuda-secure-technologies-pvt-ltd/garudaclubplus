//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.pda.dao;

import com.openbravo.pos.pda.util.PropertyUtils;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.net.URLClassLoader;
/**
 *
 * @author jaroslawwozniak
 * @version 1.0
 */
public abstract class BaseJdbcDAO {

    private PropertyUtils properties;
    private Connection conn=null;

    public BaseJdbcDAO() {
        properties = new PropertyUtils();
    }

    protected Connection getConnection() {
        try {
            if(conn==null){
            ClassLoader cloader = new URLClassLoader(new URL[] {new File(properties.getLibrary()).toURI().toURL()});
//            Class.forName(properties.getDriverName(),true,cloader).newInstance();
//            conn=DriverManager.getConnection(properties.getUrl(), properties.getDBUser(), properties.getDBPassword());
            Class.forName(properties.getDriverName());
            conn = DriverManager.getConnection(properties.getUrl(), properties.getDBUser(), properties.getDBPassword());
            }
       } catch (SQLException sqlex) {
            sqlex.printStackTrace();

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return conn;
    }

    protected List transformSet(ResultSet rs) throws SQLException {
        List voList = new ArrayList();
        Object vo;
        while (rs.next()) {
            vo = map2VO(rs);
            voList.add(vo);
        }
        return voList;
    }

    protected boolean isPostgre() {
        return properties.getDriverName().contains("postgre");
    }

    protected abstract Object map2VO(ResultSet rs) throws SQLException;
}