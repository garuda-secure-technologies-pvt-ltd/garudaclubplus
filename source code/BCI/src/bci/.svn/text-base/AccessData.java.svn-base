/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bci;

import DAO.Logs;
import DBConnection.Session;
import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Ratan
 */
public class AccessData extends Thread {
    private boolean status;
    @Override
    public void run() {
        Session eSession = null;
        Property prop=new Property();
        prop.Read();
        status=true;
        //Properties property = PropertyReader.Read();
        String eServer = prop.getAdbServer();
        String eDBName = prop.getAdbdbname();
        String pno = prop.getAdbport();
        String eDBuser = prop.getDBusername();
        String pass = prop.getDBpassword();
        int eportNo = -1;
        if (pno != null) {
            eportNo = Integer.valueOf(String.valueOf(pno));
        }
        try {
            eSession = new Session(eServer, eportNo, eDBName, eDBuser,pass);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error Occured While connecting to " + eDBName, eServer + eportNo, JOptionPane.ERROR_MESSAGE);
        }
        if (eSession != null) {
            try {
                //int Count=0;
                // String staffCode
                List<Logs> logList = new ArrayList();
                Connection conn=null;

                

                while(true){
                    while(status){
                        try {
                            this.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                conn = eSession.getConnection();
                PreparedStatement pstam=conn.prepareStatement("SELECT VALUE FROM GENERAL WHERE NAME = ?");
                pstam.setString(1, "LastAccessDate");
                ResultSet rs1=pstam.executeQuery();
                Object dateObj =null;
                if(rs1.next()){
                   dateObj=rs1.getString(1);
                }

                Date d = new Date(new java.util.Date().getTime());
                if (dateObj != null) {
                    d = (Date) dateObj;
                }
                Date tempDate = new Date(new java.util.Date().getTime());
                PreparedStatement pStam = conn.prepareStatement("SELECT L.DATETIME,S.STAFFCODE FROM LOG L JOIN STAFF S WHERE L.DEVICESTAFFID=S.STAFFCODE WHERE L.DATETIME >= ? ");
                pStam.setDate(1, d);
                ResultSet rs = pStam.executeQuery();
                while (rs.next()) {
                    Logs log = new Logs(rs.getString(2), rs.getDate(1));
                    logList.add(log);
                    
                }
                d.setTime(tempDate.getTime());
                PreparedStatement pstam1=conn.prepareStatement("UPDATE GENERAL SET VALUE=? WHERE NAME = ?");
                pstam1.setString(1, tempDate.toString());
                pstam1.setString(2, "LastAccessDate");
                pstam1.executeUpdate();
                RandomAccessFile rAccess=null;
                boolean flag = true;
                while (flag) {
                    try {
                         rAccess=new RandomAccessFile("GLogs", "w");
                         rAccess.seek(rAccess.length());
                        // rAccess.writeChars(eDBName);
                        for (Logs log : logList) {
                            ThreadSafeList.addNewEntry(log);
                            rAccess.writeChars("AA "+log.getStaffCode());
                            rAccess.writeChars("   "+log.getLogDate());
                            rAccess.writeChars("\r\n");
                        }
                        rAccess.close();
                        flag=false;
                    } catch (IOException ex) {
                        try {
                            this.sleep(1000);
                        } catch (InterruptedException ex1) {
                           ex.printStackTrace();
                        }

                    }
                }
                    try {
                        this.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }

    public synchronized void setStatus(boolean flag){
       status=flag;
    }



}
