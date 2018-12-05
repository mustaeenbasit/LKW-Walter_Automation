package com.sugarcrm.test.pdfManager;

import org.junit.Assert;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PDFManager_19177 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify deleting a pdf template  through list view can be cancelled
	 * 
	 */
	@Test
	public void PDFManager_19177_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Admin -> PDF Manager
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1158
		sugar.admin.adminTools.getControl("pdfManager").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Assert existence of Invoice template
		new VoodooControl("tr", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'Invoice')]").assertExists(true);

		// Select Invoice template for delete
		new VoodooControl("input", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'Invoice')]/td[1]/input").set("true");;

		// Delete it
		new VoodooControl("a", "id", "delete_listview_top").click();
		
		// Verify Dialog
		// TODO: VOOD-1045. Remove below line when VOOD-1045 will get resolved.
		Assert.assertTrue("This is not a deletion warning Alert!!!", VoodooUtils.iface.wd.switchTo().alert().getText().contains("Are you sure you want to delete the 1 selected record(s)?"));
		VoodooUtils.dismissDialog();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Assert successful cancellation
		new VoodooControl("tr", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'Invoice')]").assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
