package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_29019 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify 'Edit' button should disappear when clicked on Contracts subpanel of Accounts
	 * @throws Exception
	 */
	@Test
	public void Accounts_29019_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enabling Contract module and subpanel from admin
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);
		
		// Creating contract record with default account record
		sugar().contracts.create();
		
		// Navigate to account record and verifying the Contract subpanel
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel contractSubpanel = sugar().accounts.recordView.subpanels.
				get(sugar().contracts.moduleNamePlural);
		contractSubpanel.expandSubpanel();
		contractSubpanel.scrollIntoViewIfNeeded(true);
		VoodooUtils.waitForReady();
		
		// Verifying the visibility of pencil edit icon and clicking on it
		VoodooControl pencilEditIcon = contractSubpanel.getControl("editActionRow01");
		pencilEditIcon.assertVisible(true);
		pencilEditIcon.click();
		
		// Verifying disappearance of pencil edit icon
		pencilEditIcon.assertVisible(false);
		
		// Verifying visibility of save and cancel button
		contractSubpanel.getControl("cancelActionRow01").assertVisible(true);
		contractSubpanel.getControl("saveActionRow01").assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}