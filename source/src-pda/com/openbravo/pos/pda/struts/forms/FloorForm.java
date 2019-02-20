

package com.openbravo.pos.pda.struts.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author jaroslawwozniak
 */
public class FloorForm extends org.apache.struts.action.ActionForm {

    private String floorId = "";
    private String mode = "";
    private String id = "";
    private String[] parameters = null;
    private String lineNo = "";

    /**
     *
     */
    public FloorForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public String getLine() {
        return lineNo;
    }

    public void setLine(String line) {
        this.lineNo = line;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return null;
    }
}
