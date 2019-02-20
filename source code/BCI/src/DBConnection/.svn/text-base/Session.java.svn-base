/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DBConnection;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author Ratan
 */
public class Session {

    private Connection conn;
    private String dbName;
    private int portNo;
    private String ipaddr;
    private String dbusername;
    private String password;

    public Session(String ipaddr,int portNo,String dbName,String dbusername,String password) throws SQLException {
        this.dbName=dbName;
        this.ipaddr=ipaddr;
        this.portNo=portNo;
        this.dbusername = dbusername;
         this.password=password;
        getConnection();
    }



     public void Connect() {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            SQLServerDataSource ds = new SQLServerDataSource();
            //ds.setIntegratedSecurity(true);
            String addr1=InetAddress.getLocalHost().getHostName();
            ds.setServerName(ipaddr+"\\SQLEXPRESS");
            ds.setPortNumber(portNo);
            ds.setDatabaseName(dbName);
            ds.setUser(dbusername);
             ds.setPassword(password);
            conn = ds.getConnection();
        } catch (Exception ex) {
           ex.printStackTrace();
            JOptionPane.showMessageDialog(null,"Connection to server failed"+ex.getMessage());
            conn=null;
        }
    }

    public Connection getConnection() throws SQLException{
        if(conn==null || conn.isClosed()==true){

            Connect();

        }
        return conn;
    }

    @Override
    protected void finalize() throws Throwable {
        conn.close();
    }
/*    public static void main(String ags[]){
        try{
            Session obj = new Session("192.168.1.151", 1443, "eTimeTrackv3","");
            obj.getConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
    } */
}

