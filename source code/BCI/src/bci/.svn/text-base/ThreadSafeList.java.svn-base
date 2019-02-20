/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bci;

import DAO.Logs;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ratan
 */
public class ThreadSafeList {

    private static List list;


    public ThreadSafeList() {
        list=new ArrayList();
    }

    public static synchronized  void addNewEntry(Logs value){
        if(!list.contains(value))
            list.add(value);
    }

    public static List getList(){
         return list;
    }

}
