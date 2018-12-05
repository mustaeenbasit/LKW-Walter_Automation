package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_26624 extends SugarTest {
	FieldSet fs;

	public void setup() throws Exception {
		sugar().login();
		fs = testData.get(testName).get(0);
	}
	/**
	 * Verify that tax rate can be deleted successfully from the Tax Rates list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_26624_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1198-Need library support for controls on Admin -> Tax Rates page
		sugar().admin.adminTools.getControl("taxRate").click();

		// New Page. Need to re-focus.
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Create record
		new VoodooControl("input","css","#btn_create").click();

		// New Page. Need to re-focus.
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("input","css",".edit.view tbody tr [name='name']").set(fs.get("recordName"));
		new VoodooControl("input","css","#contentTable table tbody tr:nth-child(2) td:nth-child(2) slot input[type='text']").set(fs.get("taxPercent"));
		new VoodooControl("input","css","#btn_save").click();

		// New Page. Need to re-focus.
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Delete record
		new VoodooControl("a","css",".oddListRowS1 .clickMenu.SugarActionMenu a").click();
		VoodooUtils.acceptDialog();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify that tax rate can be deleted successfully from the Tax Rates list view
		new VoodooControl("tr","css",".oddListRowS1").assertExists(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
