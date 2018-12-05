package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_29824 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Case subject name is hyperlinked in account record subpanel
	 * @throws Exception
	 **/
	@Test
	public void Cases_29824_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to accounts list view and click record from list view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Added a record in cases subpanel
		StandardSubpanel casesSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural);
		casesSubpanel.addRecord();
		String caseName = sugar().cases.getDefaultData().get("name");
		sugar().cases.createDrawer.getEditField("name").set(caseName);
		sugar().cases.createDrawer.save();

		// Click record in cases subpanel to verify the name is link and verify the user is navigated to cases record
		casesSubpanel.clickLink(caseName, 1);
		sugar().cases.recordView.getDetailField("name").assertEquals(caseName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}