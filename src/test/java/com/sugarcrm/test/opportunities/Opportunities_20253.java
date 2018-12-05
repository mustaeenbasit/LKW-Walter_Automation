package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_20253 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Navigation and Cancel from menu Quick Create Opportunity
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_20253_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Quick create menu is implemented
		sugar().navbar.quickCreateAction(sugar().opportunities.moduleNamePlural);
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.cancel();
		
		// Verify that no record is created
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.assertIsEmpty();
	}

	public void cleanup() throws Exception {}
}