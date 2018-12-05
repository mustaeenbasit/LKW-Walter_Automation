package com.sugarcrm.test.calls;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21156 extends SugarTest {
	FieldSet filterData;
	String currentYear;

	public void setup() throws Exception {
		filterData = testData.get(testName).get(0);

		// Find last year's, current year's and next year's date 
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		currentYear = sdf.format(date);
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date);
		cal.add(Calendar.YEAR, 1);
		date = cal.getTime();
		String nextYear = sdf.format(date);
		cal.setTime(date);
		cal.add(Calendar.YEAR, -2);
		date = cal.getTime();
		String lastYear = sdf.format(date);

		ArrayList<String> callDates = new ArrayList<String>();
		callDates.add(currentYear);
		callDates.add(nextYear);
		callDates.add(lastYear);

		// Create the Call records with current year, last year and next year date
		FieldSet callData = new FieldSet();
		for (int i = 0; i < callDates.size(); i++) {
			callData.put("date_start_date", callDates.get(i));
			sugar.calls.api.create(callData);
			callData.clear();
		}

		// Login to sugar
		sugar.login();
	}

	/**
	 * Calls-Advanced Search-Range search functionality of "start date" should work properly
	 * @throws Exception
	 */
	@Test
	public void Calls_21156_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Calls List View -> Click on Create filter.
		sugar.calls.navToListView();
		sugar.calls.listView.openFilterDropdown();
		sugar.calls.listView.selectFilterCreateNew();

		// TODO: VOOD-1879
		VoodooSelect filterFieldCtrl = new VoodooSelect("div", "css", "div.filter-definition-container div:nth-child(1) div div[data-filter='field']");
		VoodooSelect operatorFieldCtrl = new VoodooSelect("div", "css",  "div.filter-definition-container div:nth-child(1) div div[data-filter='operator']");

		// Select the "Start Date" field from the drop down list and select "This Year" for the range search.
		filterFieldCtrl.set(filterData.get("startDate"));
		operatorFieldCtrl.set(filterData.get("thisYear"));
		VoodooUtils.waitForReady();

		// Verify that it shows all of call records with start date equal "This year" in search result
		sugar.calls.listView.verifyField(1, "date_start_date", currentYear);
		sugar.calls.listView.getControl("checkbox03").assertExists(false);

		// Cancel the filter page
		sugar.calls.listView.filterCreate.getControl("cancelButton").click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 
