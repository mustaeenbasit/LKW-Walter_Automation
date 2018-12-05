package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Bugs_18597 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.bugs.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * Verify that full form created account in sub-panel under bug tracker's
	 * detail view can be displayed
	 * @throws Exception
	 */
	@Test
	public void Bugs_18597_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.bugs.navToListView();
		sugar.bugs.listView.clickRecord(1);
		sugar.alerts.waitForLoadingExpiration();
		sugar.bugs.recordView.showDataView();

		// Add account record under Accounts subpanel
		StandardSubpanel accountsSubpanel = sugar.bugs.recordView.subpanels.get(sugar.accounts.moduleNamePlural);
		accountsSubpanel.addRecord();
		sugar.accounts.createDrawer.getEditField("name").set(customData.get("name"));

		// Verify member field is empty
		sugar.accounts.createDrawer.getEditField("memberOf").assertEquals(customData.get("member"), true);
		sugar.accounts.createDrawer.save();
		sugar.alerts.waitForLoadingExpiration();

		// Verify the Account record is created and displayed in sub panel
		FieldSet nameData = new FieldSet();
		nameData.put("name", customData.get("name"));
		accountsSubpanel.verify(1, nameData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}