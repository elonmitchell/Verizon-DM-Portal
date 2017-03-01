package motive.reports.reportconsole.rest.util;

public class JSONReportParamVO {
	
	private String name;
	private String value;
	
	public JSONReportParamVO() {
		super();
	}

	/**
	 * @return the parameter
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param parameter the parameter to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReportParameter [name=" + name + ", value=" + value
				+ "]";
	}

}
