/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ratan
 */
public class User {
    private String name;
    private String skey;
    private byte[] fpData;
    private String card;
    private List<Dependent> depList;

    public User(String name, String skey, byte[] fpData, String card) {
        this.name = name;
        this.skey = skey;
        this.fpData = fpData;
        this.card = card;
        depList=new ArrayList<Dependent>();
    }

    public String getCard() {
        return card;
    }

    public byte[] getFpData() {
        return fpData;
    }

    public String getName() {
        return name;
    }

    public String getSkey() {
        return skey;
    }
    
   public void addDependent(Dependent dep){
     depList.add(dep);
   }
   
   public List<Dependent> getDependents(){
      return depList;
   }

}
