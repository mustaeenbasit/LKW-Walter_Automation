package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27087 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  Verify that meeting end date/time has to be later than starting date/time
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27087_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);
		// Get Date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String startDate = sdf.format(date);

		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.DATE, -3);
		date = c1.getTime();
		String endDate = sdf.format(date);

		// Go to Meetings module.
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();

		// Fill end date earlier than start date.
		sugar().meetings.createDrawer.getEditField("date_start_date").set(startDate);
		sugar().meetings.createDrawer.getEditField("date_end_date").set(endDate);
		// insert name
		VoodooControl nameFieldCtrl = sugar().meetings.createDrawer.getEditField("name");
		nameFieldCtrl.set(fs.get("name"));

		// TODO: VOOD-1292
		// Check error message about required fields	
		new VoodooControl("span", "css", "[data-name='date_start'] span.fld_date_start").assertAttribute("class", "error");
		new VoodooControl("span", "css", "[data-name='date_start'] span .error-tooltip").assertAttribute("data-original-title", fs.get("assert_string_1"));

		// Change start date and end date are in the same day, but change end date time is earlier than start time.
		sugar().meetings.createDrawer.getEditField("date_start_date").set(startDate);
		sugar().meetings.createDrawer.getEditField("date_start_time").set(fs.get("start_time"));
		sugar().meetings.createDrawer.getEditField("date_end_date").set(startDate);
		sugar().meetings.createDrawer.getEditField("date_end_time").set(fs.get("end_time"));
		nameFieldCtrl.click();
		
		// TODO: VOOD-1292
		// Check error message about required fields	
		new VoodooControl("span", "css", "[data-name='date_end'] span.fld_date_end").assertAttribute("class", "error");
		new VoodooControl("span", "css", "[data-name='date_end'] span .error-tooltip").assertAttribute("data-original-title", fs.get("assert_string_2"));

		// Change start date is the same day as in end date, also change start date time is as same as end date time. 
		sugar().meetings.createDrawer.getEditField("date_start_date").set(startDate);
		sugar().meetings.createDrawer.getEditField("date_start_time").set(fs.get("start_time"));
		sugar().meetings.createDrawer.getEditField("date_end_date").set(startDate);
		sugar().meetings.createDrawer.getEditField("date_end_time").set(fs.get("start_time"));
		nameFieldCtrl.click();
		sugar().meetings.createDrawer.save();

		// verify that created meeting is available in listview
		sugar().meetings.listView.verifyField(1, "name", fs.get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}