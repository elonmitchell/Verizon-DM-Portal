package motive.reports.reportconsole;

public class DisplayableException extends Exception {
	/**
     * 
     */
    private static final long serialVersionUID = 9307366179312919L;
    
    Object[] args = null;

    public DisplayableException(String key) {
        super(key);
    }

    public DisplayableException(String key,Object[] args) {
        this(key);
        this.args = args;
    }

    public DisplayableException(String key,Object arg) {
        this(key);
        this.args = new Object[] { arg };
    }

    public DisplayableException(String key,Throwable cause) {
        super(key,cause);
    }

    public DisplayableException(String key,Object[] args,Throwable cause) {
        this(key,cause);
        this.args = args;
    }

    public DisplayableException(String key,Object arg,Throwable cause) {
        this(key,cause);
        this.args = new Object[] { arg };
    }
}
