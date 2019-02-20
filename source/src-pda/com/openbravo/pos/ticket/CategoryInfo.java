
package com.openbravo.pos.ticket;

/**
 *
 * @author jaroslawwozniak
 */
public class CategoryInfo {

    private String id;
    private String name;
    private String parentid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
