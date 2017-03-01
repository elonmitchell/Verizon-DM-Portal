package motive.reports.reportconsole.tools;

import motive.reports.commons.VersionUtil;

/**
 * @author herdem
 */
public class VersionTool extends MotiveMessageTool{

	public String version = null;

	public VersionTool() {
		super();
	}
	
	public String getVersion() {

		if (this.version == null || this.version.trim().equals("")) {
			this.version = VersionUtil.getVersion();
		}
		return this.version;
	}

}
