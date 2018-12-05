package com.sugarcrm.test.calls;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21072 extends SugarTest {
		
	public void setup() throws Exception {
		sugar.login();
		sugar.calls.api.create();
		sugar.calls.navToListView();
	}

	/**
	 * Verify that selected calls record information is not updated by Mass Update with invalid date format for "Start Date" field.
	 * 
	 * @throws Exception
	 */
	@Test
	@Ignore ("This test is written as per old BWC Calls module. Story of test need to be updated for SideCar Module")
	public void Calls_21072_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds= testData.get(testName);
		sugar.calls.listView.toggleSelectAll();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.calls.listView.getControl("actionDropdown").click();
		VoodooUtils.focusDefault();
		sugar.calls.listView.massUpdate();	
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO VOOD-768
		new VoodooControl("input", "css" ,"div#massupdate_form input#date_start_date").set(ds.get(0).get("date_start"));
		new VoodooControl("option", "css" ,"div#massupdate_form select#date_start_hours option:nth-of-type(3)").click();
		new VoodooControl("input", "css" ,"div#massupdate_form input#update_button").click();
		new VoodooControl("div", "css" ,"div#massupdate_form div.required.validation-message").assertContains(ds.get(0).get("assert"), true);
		// TODO VOOD-760
		new VoodooControl("td", "css", "table.list.view tbody tr:nth-of-type(3) td:nth-of-type(9)").assertContains(ds.get(0).get("date_start"), false);
		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}