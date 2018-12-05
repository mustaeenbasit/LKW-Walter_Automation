package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22982 extends SugarTest {
	StandardSubpanel leadsSubpanelCtrl;

	public void setup() throws Exception {
		// Create an Account record
		sugar().accounts.api.create();

		// Create a Leads record
		LeadRecord myLead = (LeadRecord) sugar().leads.api.create();

		// Login as a valid user
		sugar().login();

		// Link the Leads record with the created Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		leadsSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanelCtrl.linkExistingRecord(myLead);
	}

	/**
	 * Account Detail - Leads sub-panel - Edit_Verify that lead record related to this account can be edited by clicking "Edit" button
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22982_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet leadData = testData.get(testName).get(0);

		// Navigates to the record view of the created Account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		VoodooUtils.waitForReady();

		// Click on "Edit" button from actions on the right edge of a lead record on "LEADS" sub-panel
		leadsSubpanelCtrl.expandSubpanel();
		leadsSubpanelCtrl.scrollIntoViewIfNeeded(false);
		leadsSubpanelCtrl.editRecord(1);

		// Modify values in all available fields
		leadsSubpanelCtrl.getEditField(1, "salutation").set(leadData.get("salutation"));
		leadsSubpanelCtrl.getEditField(1, "firstName").set(testName);
		leadsSubpanelCtrl.getEditField(1, "lastName").set(leadData.get("lastName"));
		leadsSubpanelCtrl.getEditField(1, "phoneWork").set(leadData.get("phoneWork"));
		leadsSubpanelCtrl.getEditField(1, "emailAddress").set(leadData.get("emailAddress"));
		leadsSubpanelCtrl.getEditField(1, "relAssignedTo").set(leadData.get("relAssignedTo"));

		// Click "Save" button
		leadsSubpanelCtrl.saveAction(1);

		// Verify that the modified lead information is displayed on "LEADS" sub-panel
		leadsSubpanelCtrl.getDetailField(1, "fullName").assertEquals(leadData.get("salutation") + " " + testName + " " + leadData.get("lastName"), true);
		leadsSubpanelCtrl.getDetailField(1, "phoneWork").assertEquals(leadData.get("phoneWork"), true);
		leadsSubpanelCtrl.getDetailField(1, "emailAddress").assertEquals(leadData.get("emailAddress"), true);
		leadsSubpanelCtrl.getDetailField(1, "relAssignedTo").assertEquals(leadData.get("relAssignedTo"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}