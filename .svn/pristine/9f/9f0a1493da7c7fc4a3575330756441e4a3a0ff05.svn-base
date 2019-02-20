/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bci;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ratan
 */
public class Property {
    private String rdbServer;
    private String rdbport;
    private String rdbdbname;
    private String adbServer;
    private String adbport;
    private String adbdbname;
    private String clubName;
    private String clubLogo;
    private String serialNumber;
    private String licenseKey;
    private String countcheckval;
    private String DBusername;
    private String DBpassword;
    private String fingerprintfolder;
    private String vbexe;

    public void setAdbServer(String adbServer) {
        this.adbServer = adbServer;
    }

    public void setAdbdbname(String adbdbname) {
        this.adbdbname = adbdbname;
    }

    public void setAdbport(String adbport) {
        this.adbport = adbport;
    }

    public void setRdbServer(String rdbServer) {
        this.rdbServer = rdbServer;
    }

    public void setRdbdbname(String rdbdbname) {
        this.rdbdbname = rdbdbname;
    }

    public void setRdbport(String rdbport) {
        this.rdbport = rdbport;
    }
    public void setSerailNumber(String serialNumber){
        this.serialNumber = serialNumber;
    }
    public void setLicenseKey(String licenseKey){
        this.licenseKey = licenseKey;
    }
    public void setfingerprintFolder(String fingerprintfolder){
        this.fingerprintfolder = fingerprintfolder;
    }
    public void setvbExe(String vbexe){
        this.vbexe = vbexe;
    }
    public void setcountcheckval(String countcheckval){
        this.countcheckval = countcheckval;
    }


    public String getfingerprintFolder() {
        return fingerprintfolder;
    }
    public String getvbExe() {
        return vbexe;
    }
    public String getAdbServer() {
        return adbServer;
    }

    public String getAdbdbname() {
        return adbdbname;
    }

    public String getAdbport() {
        return adbport;
    }

    public String getRdbServer() {
        return rdbServer;
    }

    public String getRdbdbname() {
        return rdbdbname;
    }

    public String getRdbport() {
        return rdbport;
    }
    public String getSerailNumber(){
        return serialNumber;
    }
    public String getLicenseKey(){
        return licenseKey;
    }
    public String getcountcheckval(){
        return countcheckval;
    }
    public String getDBusername(){
        return DBusername;
    }
   public String getDBpassword(){
        return DBpassword;
    } 
   public synchronized  void store() throws FileNotFoundException, IOException{
       Properties prop=new Properties();
       prop.setProperty("rServer",rdbServer);
       prop.setProperty("rDBName", rdbdbname);
       prop.setProperty("rDBPort", rdbport);
       prop.setProperty("aDBPort", adbport);
       prop.setProperty("aDBName", adbdbname);
       prop.setProperty("aDBServer", adbServer);
       prop.setProperty("DBusername", "sa");
       prop.setProperty("DBpassword", "");
       File f=new File("GarduaRegistration.properties");
       if(!f.exists())
       f.createNewFile();
       FileOutputStream fout=new FileOutputStream(f);
       prop.store(fout, "Properties");
       fout.close();

   }

    public synchronized void Read(){
        InputStreamReader reader = null;
        Properties prop = new Properties();
        try {
            reader = new InputStreamReader(new FileInputStream("GarduaRegistration.properties"));
            prop.load(reader);
            rdbServer=prop.getProperty("rServer");
            rdbdbname=prop.getProperty("rDBName");
            rdbport=prop.getProperty("rDBPort");
            adbServer=prop.getProperty("aDBServer");
            adbdbname=prop.getProperty("aDBName");
            adbport=prop.getProperty("aDBPort");
            DBusername=prop.getProperty("DBusername");
            DBpassword=prop.getProperty("DBpassword");
            reader.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }  finally {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //return prop;
    }

    // modified by Arun

   public void setClubName(String clubName){
       this.clubName = clubName;
   }
   public void setClubLogo(String clubLogo){
       this.clubLogo = clubLogo;
   }
   public String getClubName(){
       return clubName;
   }
   public String getClubLogo(){
       return clubLogo;
   }
   public synchronized void readclubName(){
       InputStreamReader propReader = null;
       Properties property = new Properties();
       try{
           propReader = new InputStreamReader(new FileInputStream("C:\\GarudaClubNameManagement.properties"));
           property.load(propReader);
           clubName = property.getProperty("clubName");
           propReader.close();
       }catch(Exception e){
           e.printStackTrace();
       }
   }
   public synchronized void readclubLogo(){
       InputStreamReader propReader = null;
       Properties property = new Properties();
       try{
           propReader = new InputStreamReader(new FileInputStream("C:\\GarudaClubLogoManagement.properties"));
           property.load(propReader);
           clubLogo = property.getProperty("clublogo");
           propReader.close();
       }catch(Exception e){
           e.printStackTrace();
       }
   }
   public synchronized void readLicence() throws IOException{
       InputStreamReader licReader = null;
       Properties property = new Properties();
        try {
            licReader = new InputStreamReader(new FileInputStream("C:\\Garudalic.properties"));
            property.load(licReader);
            serialNumber = property.getProperty("serialNumber");
            licenseKey = property.getProperty("LicenceKey");
            countcheckval = property.getProperty("garuda");
            licReader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
   }
   public synchronized void readfingerprintInfo() throws IOException{
       InputStreamReader licReader = null;
       Properties property = new Properties();
        try {
            licReader = new InputStreamReader(new FileInputStream("C:\\GarudaFingerprint.properties"));
            property.load(licReader);
            fingerprintfolder = property.getProperty("Folderpath");
            vbexe = property.getProperty("Applicationpath");
            licReader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
   }
   public synchronized void storeFingerprintfolder() throws IOException{
       Properties prop = new Properties();
       prop.setProperty("Folderpath", fingerprintfolder);
       prop.setProperty("Applicationpath", vbexe);
       prop.setProperty("SystemName", "");
       File file = new File("C:\\GarudaFingerprint.properties");
       if(!file.exists()){
           file.createNewFile();
       }
       FileOutputStream fos = new FileOutputStream(file);
       prop.store(fos, "Properties");
       fos.close();
   }
   public synchronized void storeclubName() throws IOException{
       Properties prop = new Properties();
       prop.setProperty("clubName", clubName);
       File file = new File("C:\\GarudaClubNameManagement.properties");
       if(!file.exists()){
           file.createNewFile();
       }
       FileOutputStream fos = new FileOutputStream(file);
       prop.store(fos, "Properties");
       fos.close();
   }
   public synchronized void storeclubLogo() throws IOException{
       Properties prop = new Properties();
       prop.setProperty("clublogo", clubLogo);
       File file = new File("C:\\GarudaClubLogoManagement.properties");
       if(!file.exists()){
           file.createNewFile();
       }
       FileOutputStream fos = new FileOutputStream(file);
       prop.store(fos, "Properties");
       fos.close();
   }
   public synchronized void storeLicenseKey() throws IOException{
       Properties prop = new Properties();
       prop.setProperty("serialNumber", serialNumber);
       prop.setProperty("LicenceKey", licenseKey);
       if(serialNumber.equals("123456789") && licenseKey.equals("0000-0000-0000")){
             prop.setProperty("garuda", "False");
       }else{
           prop.setProperty("garuda", "True");
       }
       File myFile = new File("C:\\Garudalic.properties");
       if(!myFile.exists()){
           myFile.createNewFile();
       }
       FileOutputStream licfos = new FileOutputStream(myFile);
       prop.store(licfos, "properties");
       licfos.close();
   }
}
