package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22966 extends SugarTest {
	StandardSubpanel memberOrganizations;

	public void setup() throws Exception {
		// Create two Account records with different name
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		AccountRecord myAccount = (AccountRecord) sugar().accounts.api.create(fs);
		sugar().accounts.api.create();
		sugar().login();

		// Go to an account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Existing account member record related to this account needed. 
		memberOrganizations = sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		memberOrganizations.linkExistingRecord(myAccount);
	}

	/**
	 * Verify that removing account member record related to this account is canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22966_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Already in Account > recordView
		// Click "Unlink" icon on the right edge of a account member record on "MEMBER ORGANIZATIONS" sub-panel.
		memberOrganizations.scrollIntoViewIfNeeded(false);
		memberOrganizations.expandSubpanelRowActions(1);
		memberOrganizations.getControl("unlinkActionRow01").click();

		// Click "Cancel" button on the pop-up dialog.
		sugar().alerts.getAlert().cancelAlert();

		// TODO: VOOD-1424
		// Verify that the account member record is still displayed on "MEMBER ORGANIZATIONS" sub-panel.
		new VoodooControl("tr", "css", "[data-subpanel-link='members'] tbody tr:nth-of-type(1)").assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}