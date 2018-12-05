package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooUtils;

/**
 * Models Replies to an Activity Stream Comments. 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class ActivityStreamReply extends View {
	
	String dateAndTime;

	/**
	 * ActivityStreamReply Constructor
	 * @param content string content for this reply
	 * @throws Exception
	 */
	public ActivityStreamReply(String content) throws Exception { 
		// format to SugarCRM date and time format happens to be YYYY/MM/DD HH:MM
		dateAndTime = VoodooUtils.getCurrentTimeStamp("yyyy/MM/dd"); 
	}
	
	/**
	 * Gets a formatted Date and Time Stamp for this Reply
	 * @return formatted string representing the date/time stamp
	 * @throws Exception
	 */
	public String getDateTimeStamp() throws Exception {
		return dateAndTime;
	}
}
