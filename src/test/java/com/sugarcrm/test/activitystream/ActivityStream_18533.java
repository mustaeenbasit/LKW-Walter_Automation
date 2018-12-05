package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_18533 extends SugarTest{
	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * Verify that no activities message is generated when user delete records
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_18533_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts module.Select at least one record.In action drop down, select Delete
		sugar.accounts.deleteAll();

		// Go to Activities Stream in the Listview.
		sugar.accounts.listView.showActivityStream();

		// TODO: VOOD-969
		// Verify that no activities messages are generated about above deletion action, so assert the first line is still showing created info.
		VoodooControl activityStreamCtrl = new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type(1) div .tagged");
		activityStreamCtrl.assertContains("Created " + sugar.accounts.getDefaultData().get("name"), true);

		// Go to Home->Activities. 
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");

		// Verify that no activities messages are generated about above deletion action, so assert the first line is still showing created info.
		activityStreamCtrl.assertContains("Created " + sugar.accounts.getDefaultData().get("name"), true);

		// TODO: VOOD-954 the clean up will fail if not change back to dashboard page
		sugar.navbar.clickModuleDropdown(sugar.home);

		// TODO: VOOD-953
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}