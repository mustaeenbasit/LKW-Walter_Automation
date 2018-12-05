package com.sugarcrm.test.meetings;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_20086 extends SugarTest {
	DataSource meetingData;
	FieldSet customData;

	public void setup() throws Exception {
		meetingData = testData.get(testName);
		customData = testData.get(testName+"_custom").get(0);
		sugar().meetings.api.create(meetingData);
		sugar().login();
	}

	/**
	 * Check range search of start date field of meetings module
	 * @throws Exception
	 */
	@Test
	public void Meetings_20086_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create filter
		sugar().meetings.navToListView();
		
		// Verify that all the records in list view are visible
		int rowCount = sugar().meetings.listView.countRows();
		Assert.assertTrue("Number of meetings in listview not equals to 3.", rowCount == 3);
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterCreateNew();
    
		// TODO: VOOD-1460
		VoodooSelect typeDrop = new VoodooSelect("a", "css", "[data-filter='field'] [data-voodoo-type='field'] a");
		VoodooSelect opeartor = new VoodooSelect("a", "css", "[data-filter='operator'] [data-voodoo-type='field'] a");
		VoodooControl startDate = new VoodooControl("input", "css", "div:nth-child(3) .fld_date_start_min input");
		VoodooControl endDate = new VoodooControl("input", "css", "div:nth-child(3) .fld_date_start_max input");
	
		// Setting filter for meetings
		typeDrop.set(customData.get("type"));
		opeartor.set(customData.get("operator"));
		startDate.set(customData.get("startDate"));
		endDate.set(customData.get("endDate"));
		VoodooUtils.waitForReady();
		
		// Verifying that only 2 Meeting records are displayed 
		rowCount = sugar().meetings.listView.countRows();
		Assert.assertTrue("Number of meetings in listview not equals to 2.", rowCount == 2);
		
		// Setting another filter for meetings
		startDate.set(customData.get("startDate2"));
		endDate.set(customData.get("endDate2"));
		VoodooUtils.waitForReady();
		
		// Verifying that no Meeting records are displayed
		rowCount = sugar().meetings.listView.countRows();
		Assert.assertTrue("Number of meetings in listview not equals to 0.", rowCount == 0);
		
		// Setting a third filter for meetings
		startDate.set(customData.get("startDate3"));
		endDate.set(customData.get("endDate3"));
		VoodooUtils.waitForReady();

		// Verifying that all Meeting records are displayed
		rowCount = sugar().meetings.listView.countRows();
		Assert.assertTrue("Number of meetings in listview not equals to 3.", rowCount == 3);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}