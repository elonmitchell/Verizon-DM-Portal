package motive.reports.reportconsole.rest.util;

public class ReportServiceValidator {
	
	@SuppressWarnings("unused")
	public boolean validateRequiredParametersForWebServiceReport(
			JSONScheduleVO jsonSchedule) throws ReportServiceException {
		
		String errMessage = "";
		if (jsonSchedule.getReqId() == null || jsonSchedule.getReqId().equals("")) 
		{
			errMessage = errMessage + "Null or Blank Request Id. ";
		}
		if (jsonSchedule.getUsrId() == null || jsonSchedule.getUsrId().equals("")) 
		{
			errMessage = errMessage + "Null or Blank User Id. ";
		}
		if (jsonSchedule.getSource() == null || jsonSchedule.getSource().equals("")) 
		{
			errMessage = errMessage + "Null or Blank Source. ";
		}
		if (jsonSchedule.getTrnType() == null || jsonSchedule.getTrnType().equals("")) 
		{
			errMessage = errMessage + "Null or Blank Transaction Type. ";
		} else {
			if (!jsonSchedule.getTrnType().equals("GenerateReport")) 
			{
				errMessage = errMessage + "Transaction Type is invalid. ";
			}
		}
		if (jsonSchedule.getReportName() == null || jsonSchedule.getReportName().equals("")) 
		{
			errMessage = errMessage + "Null or Blank Report name. ";
		}
		if (jsonSchedule.getEMailRecipient() == null || jsonSchedule.getEMailRecipient().equals("")) 
		{
			errMessage = errMessage + "Null or Blank eMail Recipient ";
		} else {
			String emailRec[] = jsonSchedule.getEMailRecipient().split(",");
			
		}
		if (jsonSchedule.getParticipantName() == null || jsonSchedule.getParticipantName().equals("")) 
		{
			errMessage = errMessage + "Null or Blank participant name. ";
		}
		if (errMessage.length() > 0) {
			throw new ReportServiceException(4, errMessage);
		}
		
		return true;
	}
	
	public boolean isValidLength(String strObj, int min, int max)
	{
		return true;
	}
	
	public boolean isValidDate(String strDate, String format)
	{
		return true;
	}
	
	public boolean isValidFrequency(String freq)
	{
		return true;
	} 
	
	public boolean isEmptyOrNull(String strObj)
	{
		return true;
	}
	
	public boolean isValidTrnType(String strObj)
	{
		return true;
	}

}
