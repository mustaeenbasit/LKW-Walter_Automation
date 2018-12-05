package com.sugarcrm.test.admin;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20210 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify  module name in Dashlet sync with updating module  name
	 * @throws Exception
	 */
	@Ignore("SFA-3243")
	@Test
	public void Admin_20210_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.renameModule(sugar().revLineItems, customData.get("singularLabel"), customData.get("pluralLabel"));
		
		// Verify dashlet should be displayed with new values
		sugar().navbar.clickModuleDropdown(sugar().home);
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();	

		// Verify dashlet header
		new VoodooControl("h4", "css", "li.span8.layout_Home li:nth-of-type(2) h4").assertEquals(customData.get("dashlet_msg"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}