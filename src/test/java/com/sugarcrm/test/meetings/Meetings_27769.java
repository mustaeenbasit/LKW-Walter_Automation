package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27769 extends SugarTest {
	FieldSet fs;

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar().meetings.api.create();
		sugar().calls.api.create();
		sugar().login();
	}

	/**
	 * Verify that inline edit a none-repeat meeting/call works correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27769_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verifying for Meetings
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// Click on repeat field and select Daily
		// TODO: VOOD-1169 Provide support for Calls and Meetings Scheduling functionality
		new VoodooSelect("a", "css", ".fld_repeat_type.edit a").set(fs.get("repeat_type"));

		// Verify Repeat Interval, Repeat Until and Repeat Occurrences are appearing
		new VoodooControl("span", "css", "div:nth-child(2) > div > span span.select2-chosen").assertVisible(true);
		new VoodooControl("input", "css", "input[name='repeat_count']").assertVisible(true);
		new VoodooControl("input", "css", ".fld_repeat_until.edit [data-type='date']").assertVisible(true);

		// Select Repeat Untill to 2 days after meeting start date
		new VoodooControl("input", "css", ".fld_repeat_until.edit [data-type='date']").set(fs.get("repeat_until"));
		sugar().meetings.recordView.save();

		// Verify in Meeting list view, 3 meetings are created
		sugar().meetings.navToListView();
		for (int i =1 ; i <= 3 ;i++){	
			VoodooControl meetingRecord = new VoodooControl("tr", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'11/1"+(i+1)+"/2020')]");
			meetingRecord.assertContains(sugar().meetings.defaultData.get("name"), true);

			// Verify date for the meetings created
			new VoodooControl("td", "xpath","//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr/td[contains(.,'11/1"+(i+1)+"/2020')]").assertVisible(true);
		}

		// Verify that inline edit a none-repeat call works correctly
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.edit();

		// Click on repeat field and select Daily
		// TODO: VOOD-1169 Provide support for Calls and Meetings Scheduling functionality
		new VoodooSelect("span", "css", "div.record div:nth-child(1) div:nth-child(2) span span span:nth-child(1)").set(fs.get("repeat_type"));

		// Verify Repeat Interval, Repeat Until and Repeat Occurrences are appearing

		new VoodooControl("span", "css", "div:nth-child(2) > div > span span.select2-chosen").assertVisible(true);
		new VoodooControl("input", "css", "input[name='repeat_count']").assertVisible(true);
		new VoodooControl("input", "css", ".fld_repeat_until.edit [data-type='date']").assertVisible(true);

		//Select Repeat Untill to 2 days after today
		new VoodooControl("input", "css", ".fld_repeat_until.edit [data-type='date']").set(fs.get("repeat_until"));

		sugar().calls.recordView.save();

		// Verify in Calls list view, 3 meetings are created
		sugar().calls.navToListView();
		for (int i =1 ; i <= 3 ;i++){	
			VoodooControl callRecord = new VoodooControl("tr", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'11/1"+(i+1)+"/2020')]");
			callRecord.assertContains(sugar().calls.defaultData.get("name"), true);
			// Verify date for the calls created
			new VoodooControl("td", "xpath","//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr/td[contains(.,'11/1"+(i+1)+"/2020')]").assertVisible(true);			//("td", "xpath", ""//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr/td[contains(.,'11/1"+(i)+"/2020')]").assertVisible(true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}