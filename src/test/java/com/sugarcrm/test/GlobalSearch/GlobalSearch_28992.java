package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28992 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify user can navigate a link to configure unified search settings
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28992_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Navigate to Admin -> Search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("globalSearch").click();
		VoodooUtils.waitForReady();	

		// Verifying that text "To enable modules for global searches in SugarCRM Mobile and 
		// Sugar Portal, please use the Legacy Global Search Configuration." is visible
		// TODO: VOOD-1860 
		new VoodooControl("a", "css", "#contentTable tr:nth-child(2) td").assertContains
			(customData.get("description"), true);
		
		// Click the 'Legacy Global Search Configuration' hyperlink
		new VoodooControl("a", "css", "#contentTable a:nth-child(3)").click();

		// Verifying that upon clicking 'Legacy Global Search Configuration' hyperlink, user is 
		// navigate to 'Legacy Global Search Configuration' page
		new VoodooControl("h2", "css", "#contentTable h2").assertEquals(customData.get("link"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}