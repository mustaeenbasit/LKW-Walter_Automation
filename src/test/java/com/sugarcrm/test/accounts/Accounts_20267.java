package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_20267 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}
	/**
	 * Verify Inactive tasks dashboard can be added to the RHS on the Accounts record view
	 * (and displays 2 tabs within it).
	 * @throws Exception
	 */
	@Test
	public void Accounts_20267_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		
		// Go to Accounts Record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Adding the Active tasks dashboard to RHS of an account record
		sugar().accounts.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set(customData.get("dashBoardName"));
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(1,1);

		// TODO: VOOD-960 - Dashlet selection
		VoodooControl searchDashlet = new VoodooControl("input", "css", ".span4.search");
		VoodooControl activeTasks = new VoodooControl("a", "css", ".table-striped tr a");
		searchDashlet.set(customData.get("dashletName"));
		activeTasks.waitForVisible();
		activeTasks.click();
		sugar().accounts.createDrawer.save();
		sugar().accounts.dashboard.save();
		
		// Verifying that the Inactive Tasks dashlet is displayed 
		new VoodooControl("h4", "class", "dashlet-title").assertEquals(customData.get("dashletName"), true);
		
		// Verifying that the first tab displays "Deferred"
		new VoodooControl("div", "css", ".dashlet-tab").assertContains(customData.get("tab1"), true);
		
		// Verifying that the second tab displays "Completed"
		new VoodooControl("div", "css", ".dashlet-tab:nth-child(2)").assertContains(customData
				.get("tab2"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}