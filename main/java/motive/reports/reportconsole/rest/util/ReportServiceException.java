package motive.reports.reportconsole.rest.util;

public class ReportServiceException extends Exception 
{
	
	private static final long serialVersionUID = 3716728054015908325L;
	
	Object[] args = null;
	int errorCode = 0;

    public ReportServiceException(String msg) 
    {
        super(msg);
    }

    public ReportServiceException(String msg, Object[] args) 
    {
        this(msg);
        this.args = args;
    }
    
    public ReportServiceException(int errorCode, String msg) 
    {
        this(msg);
        this.errorCode = errorCode;
    }

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

}
