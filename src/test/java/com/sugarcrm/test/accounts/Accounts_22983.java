package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22983 extends SugarTest {
	StandardSubpanel leadsSubpanelCtrl;
	LeadRecord myLead;

	public void setup() throws Exception {
		// Create an Account record
		sugar().accounts.api.create();

		// Create a Leads record
		myLead = (LeadRecord) sugar().leads.api.create();

		// Login as a valid user
		sugar().login();

		// Link the Leads record with the created Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		leadsSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanelCtrl.linkExistingRecord(myLead);
	}

	/**
	 * Account Detail - Leads sub-panel - Remove Relationship_Verify that only the relationship between the lead and the account is removed by clicking "Unlink" button
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22983_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigates to the record view of the created Account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		VoodooUtils.waitForReady();

		// Click "Unlink" button on the right edge of a lead record on "LEADS" sub-panel,Click "Confirm" button on the pop-up dialog 
		leadsSubpanelCtrl.scrollIntoViewIfNeeded(false);
		leadsSubpanelCtrl.expandSubpanel();
		leadsSubpanelCtrl.unlinkRecord(1);

		// Verify that the related lead record is removed from "LEADS" sub-panel
		leadsSubpanelCtrl.isEmpty();

		// Search the newly unlinked lead record with search function under the lead module
		sugar().leads.navToListView();
		sugar().leads.listView.setSearchString(myLead.getRecordIdentifier());
		VoodooUtils.waitForReady();

		// Verify that the lead record is still displayed on the leads list view under lead module
		sugar().leads.listView.verifyField(1, "fullName", myLead.getRecordIdentifier());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}