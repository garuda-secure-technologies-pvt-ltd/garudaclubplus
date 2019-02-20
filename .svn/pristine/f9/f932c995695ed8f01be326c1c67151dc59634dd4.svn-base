/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rfidCardReaderNew;

import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.JTextField;
//import org.jdesktop.layout;

/**
 *
 * @author swathi
 */
public class CardReader implements Serializable{
    private SerialParameters defaultParameters= new SerialParameters ("COM1",9600,0,0,8,1,0);
    SerialConnection mySerial =null;
    JTextField text;
    JTextField text1;
    JTextField fac;
    JTextField cardno;
    public Object obj;
    
    public CardReader(String portname,JTextField text,JTextField fac,JTextField cardno) {
        defaultParameters= new SerialParameters (portname,9600,0,0,8,1,0);      
        this.text=text;
        this.fac=fac;
        this.cardno=cardno;
    }

    //Aru
    public CardReader(String portname,JTextField text,JTextField text1){
         defaultParameters= new SerialParameters (portname,9600,0,0,8,1,0);
         this.text = text;
         this.text1 = text1;
    }
    public CardReader(String portname){
         defaultParameters= new SerialParameters (portname,9600,0,0,8,1,0);
    }
    public void ConfigurePort() throws UnsupportedCommOperationException, PortInUseException,  SerialConnectionException{
        mySerial =new SerialConnection (defaultParameters,text,text1,fac,cardno);
        mySerial.openConnection();
        
    }

    public String getData(){
       return mySerial.getIncommingString();
    }
   
   public void closeconnection(){
      mySerial.closeConnection();
   }
   public boolean isConnected(){
       return mySerial.isOpen();
   }
}
