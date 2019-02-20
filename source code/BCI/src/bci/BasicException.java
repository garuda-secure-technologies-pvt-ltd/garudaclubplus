/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bci;

/**
 *
 * @author swathi
 */
public class BasicException extends java.lang.Exception {

    /**
     * Creates a new instance of <code>DataException</code> without detail message.
     */
    public BasicException() {
    }

    public BasicException(String msg) {
        super(msg);
    }

    public BasicException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BasicException(Throwable cause) {
        super(cause);
    }
}
