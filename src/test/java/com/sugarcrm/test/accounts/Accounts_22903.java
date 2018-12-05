package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22903 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().tasks.api.create();
		sugar().login();

		// TODO: VOOD-444 - Support creating relationships via API
		// Link Account name with Task record
		sugar().tasks.navToListView();
		sugar().tasks.listView.clickRecord(1);
		sugar().tasks.recordView.edit();
		sugar().tasks.recordView.getEditField("relRelatedToParent").set(sugar().accounts.getDefaultData().get("name"));
		sugar().tasks.recordView.save();
	}

	/**
	 * Verify that only the relationship between the task and the account is removed by clicking "Unlink" button
	 * @throws Exception
	 */
	@Test
	public void Accounts_22903_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel taskSub = sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskSub.expandSubpanel();
		taskSub.unlinkRecord(1);
		Assert.assertTrue("The subpanel is not empty", taskSub.isEmpty());

		// Verifying that task record only got unlinked from account record
		sugar().tasks.navToListView();
		sugar().tasks.listView.getDetailField(1, "subject").assertEquals(sugar().tasks.getDefaultData().get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}