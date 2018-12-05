package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooUtils;

/**
 * Models Activity Stream Comments. 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class ActivityStreamEntry extends View {

	String dateAndTime;

	/**
	 * ActivityStreamEntry Constructor
	 * @param content string content of this Entry
	 * @throws Exception
	 */
	public ActivityStreamEntry(String content) throws Exception {
		// format to SugarCRM date and time format happens to be YYYY/MM/DD HH:MM
		dateAndTime = VoodooUtils.getCurrentTimeStamp("yyyy/MM/dd");
	}

	/**
	 * Gets a formatted Date and Time Stamp for the Entry.
	 * @return formatted string representing the date/time stamp
	 * @throws Exception
	 */
	public String getDateTimeStamp() throws Exception {
		return dateAndTime;
	}
}
