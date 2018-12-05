package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_28842 extends SugarTest {
	VoodooControl taxRateCtrl;
	
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * User should not get any error message while creating Tax Rate
	 * 
	 * @throws Exception
	 */
	@Test
	public void Products_28842_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		taxRateCtrl = sugar.admin.adminTools.getControl("taxRate");
		taxRateCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		FieldSet customFS = testData.get(testName).get(0);

		// TODO: VOOD-1198-Need library support for controls on Admin -> Tax Rates page
		// Create record
		new VoodooControl("input","css",".formHeader.h3Row [title='Create']").click();
		new VoodooControl("input","css",".edit.view [name='name']").set(testName);
		new VoodooControl("input","css","#contentTable table tbody tr:nth-child(2) td:nth-child(2) slot input[type='text']").set(customFS.get("taxPercent"));
		new VoodooControl("input","id","btn_save_and_create").click();
		VoodooUtils.waitForReady();
		
		// Verify that User should not get any error message.
		new VoodooControl("body", "css", "body").assertContains(customFS.get("errorMsg"), false);
		
		// Verify that Record created successfully
		new VoodooControl("a", "css", ".list.view .oddListRowS1 td:nth-child(1) a").assertContains(testName, true);
		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}