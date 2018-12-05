package com.sugarcrm.test.targetlists;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_29831 extends SugarTest{
	AccountRecord accRecord;

	public void setup() throws Exception {
		sugar().targetlists.api.create();
		accRecord = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Account name field should be hyper linked in accounts sub-panel of TargetList record view
	 * 
	 * @throws Exception
	 */
	@Test
	public void TargetLists_29831_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to TargetLists record view
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.clickRecord(1);

		// Adding Accounts record in Accounts subpanel
		StandardSubpanel accountSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		accountSubpanel.linkExistingRecord(accRecord);

		// Asserting the 'a' tag for Account Name field
		accountSubpanel.scrollIntoViewIfNeeded(false);
		Assert.assertTrue("The Account Name field is not a hyperlink", (accountSubpanel.getDetailField(1, "name").getTag().equals("a")));

		// Clicking on Account Name field in Accounts subpanel
		accountSubpanel.getDetailField(1, "name").click();

		// Verifying the user is navigated to Accounts record view
		FieldSet fs = sugar().accounts.getDefaultData();
		sugar().accounts.recordView.getDetailField("name").assertEquals(fs.get("name"), true);
		sugar().accounts.recordView.getDetailField("website").assertEquals(fs.get("website"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}