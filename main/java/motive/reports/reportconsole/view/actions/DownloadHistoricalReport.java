package motive.reports.reportconsole.view.actions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import motive.exceptions.InvalidArgumentException;
import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.actions.BaseAction;
import motive.reports.reportconsole.manage.actions.DownloadReport;
import motive.reports.reportconsole.util.ReportSystemPropertyAdapter;
import motive.reports.service.reportmanager.HistoricalReport;


import motive.reports.service.reportmanager.ReportManagerService;
import motive.trace.TraceLogger;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DownloadHistoricalReport extends BaseAction {


    /** trace logger */
    private static Logger logger = TraceLogger.getLogger(DownloadReport.class);

     /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
    	OutputStream outputStream = null;
    	OutputStream out = null;
    	
        try
        {
        	ReportSystemPropertyAdapter prop = ReportSystemPropertyAdapter.getInstance();
    	   	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();
            String reportId = (String) request.getParameter("histReportID");
            String ftpServer = prop.getPropValue("FTP_SERVER");
            String ftpUserName = prop.getPropValue("FTP_USER_NAME");
            String ftpPwd = prop.getPropValue("FTP_PASSWORD");
            
            HistoricalReport theReport = rms.getHistReportById(new Long( reportId));
            String filename = URLEncoder.encode( theReport.getFileName(), "UTF-8");
            // Build a localized file name
            if(ftpServer != null && ftpUserName != null && ftpPwd != null) {
            	
            	String contentDisp = "attachment; filename=\"" + filename + "\"";
                
                FTPClient client = new FTPClient();
                client.connect(ftpServer);
                client.login(ftpUserName, ftpPwd);
        		client.setFileType(FTP.BINARY_FILE_TYPE,FTP.BINARY_FILE_TYPE);
        		client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
        		String remoteFile = theReport.getFilePath() + File.separator + theReport.getFileName();
        		
        		out = response.getOutputStream();
                
        		response.setContentType("application/x-download; charset=utf-8");
                response.setHeader("Content-Disposition", contentDisp);
                response.setHeader("Pragma", "public");
                response.setHeader("Cache-Control", "must-revalidate");
                
                outputStream = new BufferedOutputStream(out);
                boolean success = client.retrieveFile(remoteFile, outputStream);
                
                if (success) {
                	logger.info("Historical Report been downloaded successfully.");
                } else {
                	logger.error("Historical Report download failed.");
                	throw new Exception("An error occurred while downloading the Historical Report from FTP site." +
                			" Please see the server log for more details.");
                }
            	
            } else {
            	throw new InvalidArgumentException("The FTP server information is not configured. Please configure correctly.");
            }            
            
        }
        catch (Exception e)
        {
            logger.error("An error occurred while downloading the report xml. Please see the server log for more details.");
            e.printStackTrace();
        } finally {
        	if(outputStream != null)
        		outputStream.close();
        	if(out != null)
        		out.close();
        }
        return null;
    }
}
