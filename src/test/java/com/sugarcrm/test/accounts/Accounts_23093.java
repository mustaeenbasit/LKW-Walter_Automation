package com.sugarcrm.test.accounts;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Assert;
import org.junit.Test;

public class Accounts_23093 extends SugarTest {
	AccountRecord myAcc;

	public void setup() throws Exception {
		myAcc = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);
	}

	/**
	 * Test Case 23093: Verify that the duplicate related record is related to this account too
	 * @throws Exception
	 */
	@Test
	public void Accounts_23093_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// For sidecar module
		// Create a related note
		myAcc.navToRecord();
		StandardSubpanel notesSubpanel = sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.addRecord();
		sugar().notes.createDrawer.showMore();
		sugar().notes.createDrawer.setFields(sugar().notes.getDefaultData());
		sugar().notes.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		notesSubpanel.clickRecord(1);

		// Duplicate the note
		sugar().notes.recordView.copy();
		sugar().alerts.waitForLoadingExpiration();
		sugar().notes.createDrawer.save();

		// Verify first note and duplicate are related to an account
		myAcc.navToRecord();
		notesSubpanel.scrollIntoViewIfNeeded(false);
		Assert.assertEquals("Row count is not equal to 2 in subpanel", 2, notesSubpanel.countRows());
		for (int i = 1; i <= 2; i++) 
			notesSubpanel.getDetailField(i, "subject").assertEquals(sugar().notes.getDefaultData().get("subject"), true);

		// For bwc module
		// Create a related contract
		StandardSubpanel contractSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contracts.moduleNamePlural);
		sugar().contracts.create();
		
		// Duplicate the contract
		sugar().contracts.detailView.copy();
		sugar().contracts.editView.save();
		
		// Verify first contract and duplicate are related to an account
		myAcc.navToRecord();
		contractSubpanel.expandSubpanel();
		Assert.assertEquals("Row count is not equal to 2 in subpanel", 2, contractSubpanel.countRows());
		for (int i = 1; i <= 2; i++) {
			//TODO: VOOD-1846
			new VoodooControl("tr", "css", "[data-voodoo-name='Contracts'] tr:nth-child(" + i + ")[class='single']")
			.assertContains(sugar().contracts.getDefaultData().get("name"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}