/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rfidCardReaderNew;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author swathi
 */
public class MainCard {

 private String cardvalueReturn;
    /**
     * @param args the command line arguments
     */
 public static Object obj;
      public static void card1(String memberName,String memberCode,String cardCombovalue) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception ex) {

        }
        cardReaderMain crMain = new cardReaderMain(new JFrame(), true,memberName, memberCode, cardCombovalue);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int h = crMain.getSize().height;
        int w = crMain.getSize().width;
        Dimension scrnsize = toolkit.getScreenSize();
        obj = crMain.getmethodObject();
        crMain.setLocationRelativeTo(null);
        crMain.setVisible(true);
    }
      public static void card(String memberName,String memberCode,String cardCombovalue,Object obj) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception ex) {

        }
        cardReaderMain crMain = new cardReaderMain(new JFrame(), true,memberName, memberCode, cardCombovalue,obj);
        System.out.println("sete : "+obj);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int h = crMain.getSize().height;
        int w = crMain.getSize().width;
        Dimension scrnsize = toolkit.getScreenSize();
        obj = crMain.getmethodObject();
        crMain.setLocationRelativeTo(null);
        crMain.setVisible(true);
    } 
}
