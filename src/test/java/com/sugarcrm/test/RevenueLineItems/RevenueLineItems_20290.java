package com.sugarcrm.test.RevenueLineItems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_20290 extends SugarTest {
	DataSource customData;
	String yesterdaysDate,todaysDate,dateAfter1Day;
	
	public void setup() throws Exception {
		customData = testData.get(testName);
		
		// Get Today's Date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		todaysDate = sdf.format(date);

		// Date after 1 Day
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		date = cal.getTime();
		dateAfter1Day = sdf.format(date);

		// Yesterday's Date
		cal.add(Calendar.DATE, -2);
		date = cal.getTime();
		yesterdaysDate = sdf.format(date);
		
		ArrayList<String> rliDates = new ArrayList<String>();
		rliDates.add(yesterdaysDate);
		rliDates.add(todaysDate);
		rliDates.add(dateAfter1Day);
				
		FieldSet rliData = new FieldSet();
		for (int i = 0; i < rliDates.size(); i++) {
			rliData.put("date_closed", rliDates.get(i));
			rliData.put("name", testName+"_"+i);
			sugar().revLineItems.api.create(rliData);
			rliData.clear();
		}
		sugar().login();
	}

	/**
	 * Verify filter in date works correctly in Revenue Line Item module
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_20290_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	
		
		// Create filter from the "Revenue Line Items" listview
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.openFilterDropdown();
		sugar().revLineItems.listView.selectFilterCreateNew();

		// Select Expected close date and select last 7 days, or next 7 days
		// TODO: VOOD-1766, VOOD-1879
		VoodooSelect filterFieldCtrl = new VoodooSelect("div", "css", "div.filter-definition-container div:nth-child(1) div div[data-filter='field']");
		VoodooSelect operatorFieldCtrl = new VoodooSelect("div", "css",  "div.filter-definition-container div:nth-child(1) div div[data-filter='operator']");
		filterFieldCtrl.set(customData.get(0).get("fieldName"));
		
		// Verify results
		for (int i = 0; i < 3; i++) {
			operatorFieldCtrl.set(customData.get(i).get("operator"));
			VoodooUtils.waitForReady();
			sugar().revLineItems.listView.sortBy("headerName", true);
			
			if(i == 2) {
				sugar().revLineItems.listView.assertIsEmpty();
			}
			else {
				sugar().revLineItems.listView.verifyField(1, "name", testName+"_"+i);
				sugar().revLineItems.listView.verifyField(2, "name", testName+"_"+(i+1));
			}
		}
		
		// Cancel the filter page
		sugar().revLineItems.listView.filterCreate.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}