package com.sugarcrm.test.sweetspot;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Sweetspot_28554 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify Sweet Spot opens correct admin setting
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28554_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);

		for(int i=0; i<ds.size(); i++) {
			sugar.sweetspot.show();

			// Search Sweetspot and verify lists the correct search and its action			
			sugar.sweetspot.search(ds.get(i).get("admin_setting_search_text"));
			sugar.sweetspot.getControl("searchResult").assertContains(ds.get(i).get("admin_setting_search_text"), true);

			// Click on search result record
			sugar.sweetspot.clickActionsResult();

			// Rename module page
			if(i==0) {
				VoodooUtils.focusFrame("bwc-frame");
				sugar.admin.renameModule.getControl("cancelButton").assertVisible(true);
			}

			// Studio page
			else if(i==1) {
				VoodooUtils.focusFrame("bwc-frame");
				sugar.admin.studio.getControl("homeButton").assertVisible(true);
			}

			// Portal page
			else if(i==2) {
				VoodooUtils.focusFrame("bwc-frame");
				sugar.admin.portalSetup.getControl("configurePortal").assertVisible(true);
			}

			// Users listview page
			else if(i==3) {
				VoodooUtils.focusFrame("bwc-frame");
				sugar.users.listView.assertVisible(true);
			}

			// Email settings page
			else if(i==4) {
				VoodooUtils.focusFrame("bwc-frame");
				sugar.admin.emailSettings.getControl("cancel").assertVisible(true);
			}

			// Product Catalog listview
			else if(i==5) {
				sugar.productCatalog.listView.assertVisible(true);
			}

			VoodooUtils.focusDefault();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}