package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_21521 extends SugarTest {
	FieldSet customData;
	
	public void setup() throws Exception {
		sugar().login();
		customData = testData.get(testName).get(0);
		sugar().admin.renameModule(sugar().targets, customData.get("singularName"), customData.get("pluralName"));
	}

	/**
	 * Verify Rename Modules should change drop down module menu items
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_21521_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Verify Rename Modules should change drop down module menu items
		sugar().targets.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().targets);
		sugar().targets.menu.getControl("createTarget").assertElementContains(customData.get("assert1"), true);
		sugar().targets.menu.getControl("viewtargets").assertElementContains(customData.get("assert2"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
