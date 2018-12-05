package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28991 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}
	
	/**
	 * Verify global search settings in Admin
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28991_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Navigate to Admin -> Search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("globalSearch").click();
		VoodooUtils.waitForReady();
		
		// Verify that the page heading is "Full Text Search Configuration"
		// TODO: VOOD-1860 
		VoodooControl pageHeading = new VoodooControl("h2", "css", "#contentTable h2");
		pageHeading.assertEquals(customData.get("heading1"), true);
		
		// Verify that the page heading is "Full Text Search Configuration"
		new VoodooControl("td", "css", "#contentTable tr:nth-child(2) td").assertContains
			(customData.get("description"), true);
		
		// Verifying that the Enabled Modules list is visible
		new VoodooControl("div", "id", "enabled_div").assertVisible(true);
		
		// Verifying that the Disabled Modules list is visible
		new VoodooControl("div", "id", "disabled_div").assertVisible(true);
		
		// Verifying that the Save button is visible
		new VoodooControl("input", "css", "[title='Save']").assertVisible(true);

		// Verifying that the 'Schedule System Index' button is visible
		new VoodooControl("input", "css", "[title='Schedule System Index']").assertVisible(true);
		
		// Verifying that the Cancel button is visible
		new VoodooControl("input", "css", "[title='Cancel']").assertVisible(true);		
		
		// Verifying that the 'Legacy Global Search Configuration' hyperlink  is visible
		VoodooControl legacyLink = new VoodooControl("a", "css", "#contentTable a:nth-child(3)");
		legacyLink.assertEquals(customData.get("heading2"), true);	
		legacyLink.click();
		
		// Verifying that upon clicking 'Legacy Global Search Configuration' hyperlink, user is 
		// navigate to 'Legacy Global Search Configuration' page
		pageHeading.assertEquals(customData.get("heading2"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}