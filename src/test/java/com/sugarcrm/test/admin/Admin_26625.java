
package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_26625 extends SugarTest {
	FieldSet fs;

	public void setup() throws Exception {
		sugar().login();
		fs = testData.get(testName).get(0);
	}

	/**
	 * Verify that release record can be successfully deleted from the Releases list view 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_26625_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();

		// TODO: VOOD-1196 - Need library support for Admin -> Releases 
		sugar().admin.adminTools.getControl("release").click();

		// New Page. Need to re-focus.
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Create record
		new VoodooControl("input","css","input[name='New']").click();

		// New Page. Need to re-focus.
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("input","css",".edit.view tbody tr [name='name']").set(fs.get("recordName"));
		new VoodooControl("input","css","input[name='button']").click();

		// New Page. Need to re-focus.
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Delete record
		new VoodooControl("a","css",".oddListRowS1 .clickMenu.SugarActionMenu a").click();
		VoodooUtils.acceptDialog();
		sugar().alerts.waitForLoadingExpiration();
				
		// Assert that release record is successfully removed from Releases list view
		new VoodooControl("tr","css",".oddListRowS1").assertExists(false); 

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}