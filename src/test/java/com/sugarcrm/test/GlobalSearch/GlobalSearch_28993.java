package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28993 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Unified Search Settings 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28993_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Navigate to Admin -> Search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("globalSearch").click();
		VoodooUtils.waitForReady();	
		
		// Click the 'Legacy Global Search Configuration' link
		// TODO: VOOD-1860 
		new VoodooControl("a", "css", "#contentTable a:nth-child(3)").click();

		// Verifying that text "Legacy global search will only be used to return results in
		// SugarCRM Mobile or Sugar Portal global searches. In order for legacy global search 
		// to perform a search on a particular module, it must be enabled below." is visible
		new VoodooControl("tr", "css", "#contentTable tr:nth-child(2)").assertContains
			(customData.get("description"), true);
		
		// Verifying that the 'Schedule System Index' button is not visible
		new VoodooControl("input", "css", "[title='Schedule System Index']").assertVisible(false);
		
		// Verifying that the 'Full Text Search Configuration' link is visible;
		VoodooControl ftsLink = new VoodooControl("a", "css", "#contentTable td tbody a:nth-child(2)");
		ftsLink.assertEquals(customData.get("link"), true);
		ftsLink.click();
		
		// Verifying that upon clicking 'Full Text Search Configuration' link, user is 
		// navigated to 'Full Text Search Configuration' page
		new VoodooControl("h2", "css", "#contentTable h2").assertEquals(customData.get("link"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}