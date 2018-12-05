package com.sugarcrm.test.sweetspot;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Sweetspot_28586 extends SugarTest {
	public void setup() throws Exception {
		sugar.login(sugar.users.getQAUser());
	}

	/**
	 * Verify that non-admin user does not have access to any admin actions through Sweet Spot
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28586_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to sweetSpot section
		DataSource customDS = new DataSource();
		customDS = testData.get(testName);
		sugar.sweetspot.show();
		VoodooControl searchResult = sugar.sweetspot.getActionsResult();
		
		for(int i = 0; i < customDS.size(); i ++) {
			// Search
			sugar.sweetspot.search(customDS.get(i).get("adminSetting"));
			
			// Verify that Non-admin user cannot search Sweet Spot for admin actions
			searchResult.assertExists(false);
		}

		// Go to Sweet Spot Config Drawer - try to add a hotkey for an admin action
		sugar.sweetspot.configure();
		sugar.sweetspot.getControl("hotkeysAction").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1836
		new VoodooControl("input", "css", ".select2-drop-active .select2-search input").set(customDS.get(0).get("adminSetting"));
		
		// Verify that Non-admin user cannot add hotkey (or search for) an admin action through the config drawer 
		new VoodooControl("li", "css", ".select2-drop-active ul .select2-no-results").assertContains(customDS.get(0).get("noMatchFound"),true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}