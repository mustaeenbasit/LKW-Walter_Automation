package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_20266 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}
	/**
	 * Verify active tasks dashboard can be added to the RHS on the Accounts record view
	 * (and displays 3 tabs within it).
	 * @throws Exception
	 */
	@Test
	public void Accounts_20266_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts Record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		FieldSet customData = testData.get(testName).get(0);

		// Add the Active tasks dashboard to RHS of an account record
		sugar().accounts.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set(customData.get("dashBoardName"));
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(1,1);

		// TODO: VOOD-960 - Dashlet selection
		// Select Active task from dashlet list
		String dashletName = customData.get("dashletName");
		new VoodooControl("input", "css", ".span4.search").set(dashletName);

		// Click on "Active Tasks" 
		new VoodooControl("a", "css", ".list.fld_title [data-placement='bottom'] a").click();
		sugar().accounts.createDrawer.save();
		sugar().accounts.dashboard.save();

		// Verify the "Active Tasks" dashlet is displayed 
		new VoodooControl("h4", "class", "dashlet-title").assertEquals(dashletName, true);

		// Verify the first tab displays "Due Now"
		new VoodooControl("div", "css", ".dashlet-tab").assertContains(customData.get("tab1"), true);

		// Verify the second tab displays "Upcoming"
		new VoodooControl("div", "css", ".dashlet-tab:nth-child(2)").assertContains(customData
				.get("tab2"), true);

		// Verify the third tab displays "To Do"
		new VoodooControl("div", "css", ".dashlet-tab:nth-child(3)").assertContains(customData
				.get("tab3"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}