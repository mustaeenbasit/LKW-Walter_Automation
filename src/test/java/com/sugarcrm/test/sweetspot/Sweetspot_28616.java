package com.sugarcrm.test.sweetspot;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Sweetspot_28616 extends SugarTest {
	DataSource customDS = new DataSource();
	
	public void setup() throws Exception {
		customDS = testData.get(testName);
		sugar.login();
	}

	/**
	 * Verify user can add a few hotkeys and use them with Sweet Spot, and can remove them
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28616_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to sweetSpot section
		sugar.sweetspot.show();
		sugar.sweetspot.configure();
		for(int i = 0; i < customDS.size(); i ++) {
			// TODO: VOOD-1840
			new VoodooControl("span", "css", ".columns.layout_Home .config-list-row:nth-of-type("+(i+1)+") span.edit.fld_action").click();
			VoodooUtils.waitForReady();
			new VoodooSelect("input", "css", ".select2-drop-active[style*=block] .select2-search input").set(customDS.get(i).get("adminSetting"));
			VoodooUtils.waitForReady();
			new VoodooControl("span", "css", ".columns.layout_Home .config-list-row:nth-of-type("+(i+1)+") span.edit.fld_keyword input").set(customDS.get(i).get("hotKey"));
			VoodooUtils.waitForReady();
			new VoodooControl("span", "css", ".columns.layout_Home .config-list-row:nth-of-type("+(i+1)+") button[data-action='add']").click();
			VoodooUtils.waitForReady();
		}
		sugar.sweetspot.saveConfiguration();
		
		// Show sweetSpot search
		sugar.sweetspot.show();
		VoodooControl searchResult = sugar.sweetspot.getKeywordsResult();
		for(int i = 0; i < customDS.size(); i ++) {
			// Search
			sugar.sweetspot.search(customDS.get(i).get("hotKey"));
			
			// Verify that Non-admin user cannot search Sweet Spot for admin actions
			searchResult.assertContains(customDS.get(i).get("adminSetting"), true);
		}

		// Call Sweet Spot again -> open Sweet Spot Config Drawer and remove one of the hotkeys you created to remove it -> click Save
		sugar.sweetspot.configure();
		sugar.sweetspot.removeHotkey();
		sugar.sweetspot.saveConfiguration();
		
		// Verify that the removed Hotkey does NOT work
		sugar.sweetspot.show();
		sugar.sweetspot.search(customDS.get(0).get("hotKey"));
		searchResult.assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}