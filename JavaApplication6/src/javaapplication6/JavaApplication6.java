/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication6;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author swathi
 */
public class JavaApplication6 extends JApplet
      implements ActionListener {
     private JPanel pane = null;
     private JScrollPane scrolling = null;
     private JTextPane fileBox = null;
     private JTextField tfFilename = null;
     private JButton butLoad = null;
     private final String LOAD = "load";

   public void init() {
   try {
       jbInit();
     } catch(Exception e) {
       e.printStackTrace();
     }
   }

 // method which will read data from file, and return it in
      // String
   public String readFile(String fn) {
     String thisLine, ret = "";
    try {
    FileInputStream fin =  new FileInputStream(fn);
      BufferedReader myInput = new BufferedReader
                         (new InputStreamReader(fin));
       while ((thisLine = myInput.readLine()) != null) {  
        ret += thisLine + "\n";
       }
     } catch (Exception e) {
       ret = "Cannot load, exception!";
     }
     return ret;
   }

   private void jbInit() throws Exception {
     pane = new JPanel();
     pane.setBounds(new Rectangle(0, 0, 500, 325));
     pane.setLayout(null);
     pane.setBorder(BorderFactory.createEtchedBorder(
                       EtchedBorder.LOWERED));
     pane.setBackground(new Color(221, 194, 219));

     fileBox = new JTextPane();
     fileBox.setText("");
     fileBox.setEditable(false);
     scrolling = new JScrollPane(fileBox);
     scrolling.setBounds(new Rectangle(16, 65, 295, 225));

     tfFilename = new JTextField();
     tfFilename.setText("");
     tfFilename.setBounds(new Rectangle(16, 23, 206, 29));

     butLoad = new JButton();
     butLoad.setBounds(new Rectangle(231, 23, 80, 30));
     butLoad.setText("Load");
     butLoad.setActionCommand(LOAD);
     butLoad.addActionListener(this);

     pane.add(scrolling);
     pane.add(tfFilename);
     pane.add(butLoad);

     setContentPane(pane);
   }

   public void actionPerformed(ActionEvent e) {
     if (e.getActionCommand().equals(LOAD)) {
         fileBox.setText(readFile(tfFilename.getText()));
     }
   }
}
