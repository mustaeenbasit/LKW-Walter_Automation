package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class Studio_28835 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify "Found in Release" and "Fixed in Release" fields are not repeated twice in the Record view layout
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_28835_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Studio > Opportunities > fields > date_close 
		// TODO: VOOD-542 -need lib support for studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		new VoodooControl("a", "id", "studiolink_Bugs").click();
		new VoodooControl("td", "id", "layoutsBtn").click();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		FieldSet customFS = testData.get(testName).get(0);
		String fixedReleaseField = customFS.get("fixed_in_release_field");
		String foundReleaseField = customFS.get("found_in_release_field");
		
		
		// Verify that Fixed in Release and Found in Release fields should not be displayed twice
		// Verify that the Fields are not displayed in Tool box
		new VoodooControl("div", "xpath", "//*[@id='toolbox']/div[contains(.,'"+fixedReleaseField+"')]").assertExists(false);
		new VoodooControl("div", "xpath", "//*[@id='toolbox']/div[contains(.,'"+foundReleaseField+"')]").assertExists(false);
		
		// Verify that the Fields are displayed in Show more panel
		new VoodooControl("div", "xpath", "//*[@id='panels']/div[2]/div[contains(.,'"+fixedReleaseField+"')]").assertExists(true);
		new VoodooControl("div", "xpath", "//*[@id='panels']/div[2]/div[contains(.,'"+foundReleaseField+"')]").assertExists(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}