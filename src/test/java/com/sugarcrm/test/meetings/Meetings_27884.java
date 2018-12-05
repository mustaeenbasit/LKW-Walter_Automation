package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27884 extends SugarTest {
	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().login();
	}
	
	/**
	 * Verify that "View Change Log" action link is not included in record view
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27884_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to meetings Record view
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		
		// Verify that 'View Change Log' action link isn't appearing in the drop down list.
		// TODO: VOOD-691
		FieldSet fs;
		fs = testData.get(testName).get(0);
		String linkToAssert = fs.get("linkToAssert");
		// xpath is needed here to find out a string in drop drown list
		VoodooControl linkInActionDropDown = new VoodooControl("li", "xpath", "//*[@id='content']/div/div/div[1]/div/div[1]/div/div[1]/h1/div/span[3]/ul/li[contains(.,'"+linkToAssert+"')]");
		linkInActionDropDown.assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}