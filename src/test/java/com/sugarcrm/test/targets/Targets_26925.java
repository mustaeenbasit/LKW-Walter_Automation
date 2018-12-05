package com.sugarcrm.test.targets;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;

public class Targets_26925 extends SugarTest {
	VoodooControl ConvertedTarget;

	public void setup() throws Exception {
		// Create a new Target Record
		sugar().targets.api.create();
		sugar().login();
	}

	/**
	 * Verify that target record view is displayed properly after the lead record related to the target is deleted
	 *
	 * @throws Exception
	 */
	@Test
	public void Targets_26925_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().targets.navToListView();
		sugar().targets.listView.clickRecord(1);
		sugar().targets.recordView.openPrimaryButtonDropdown();

		// Click on convert link
		// TODO: VOOD-1151
		new VoodooControl("a", "css", "[name='convert_button']").click();

		sugar().alerts.waitForLoadingExpiration();
		sugar().targets.createDrawer.save();

		// Verify that converted Target displayed properly
		// TODO: VOOD-1298
		ConvertedTarget = new VoodooControl("div", "css", "[data-voodoo-name='convert-results'] > table > tbody > tr > td:nth-child(1) > div");
		ConvertedTarget.assertContains("Converted Lead: "+sugar().targets.getDefaultData().get("firstName")+" "+sugar().targets.getDefaultData().get("lastName"), true);

		// Go to Leads module
		sugar().leads.navToListView();

		// Verify that converted Target display on leads > listview
		sugar().leads.listView.assertContains(sugar().targets.getDefaultData().get("firstName")+" "+sugar().targets.getDefaultData().get("lastName"), true);
		sugar().leads.listView.deleteRecord(1);
		sugar().leads.listView.confirmDelete();
		sugar().alerts.waitForLoadingExpiration(30000); // more time needed to save

		sugar().targets.navToListView();
		sugar().targets.listView.clickRecord(1);

		// Verify that target record view is displayed properly after the lead record related to the target is deleted
		sugar().targets.recordView.getDetailField("title").assertContains(sugar().targets.getDefaultData().get("title"), true);

		// Verify that converted Target text not displayed after the lead record related to the target is deleted
		// TODO: VOOD-1298
		ConvertedTarget.assertExists(false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
