package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_31153 extends SugarTest{
	StandardSubpanel leadsSubpanelCtrl;
	
	public void setup() throws Exception {
		// Create an Account record
		sugar().accounts.api.create();

		// Create a Leads record
		LeadRecord myLead = (LeadRecord) sugar().leads.api.create();

		// Login as a valid user
		sugar().login();

		// Link the Leads record with the created Account record
		// TODO: VOOD-444
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		leadsSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanelCtrl.linkExistingRecord(myLead);
	}

	/** Verify that name field can be inline edited in subpanels for sidecar records
	 *@throws Exception
	 */
	@Test
	public void Accounts_31153_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet leadDefaultData = sugar().leads.getDefaultData();
		
		// Navigates to the record view of the created Account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Verifying the header fullName and existing record in the lead subpannel
		// TODO: VOOD-1424
		leadsSubpanelCtrl.getDetailField(1, "fullName").assertEquals(leadDefaultData.get("fullName"), true);

		// Moving to the lead subpanel and update the name
		leadsSubpanelCtrl.editRecord(1);
		leadsSubpanelCtrl.getEditField(1,"lastName").set(testName);
		leadsSubpanelCtrl.saveAction(1);

		// Verify that lead is correctly updated
		String lead_Name = leadDefaultData.get("salutation") + " " + leadDefaultData.get("firstName") + " " + testName;
		leadsSubpanelCtrl.getDetailField(1, "fullName").assertEquals(lead_Name, true);
		leadsSubpanelCtrl.clickRecord(1);

		// Verify that lead is correctly modified in record view
		sugar().leads.recordView.getDetailField("fullName").assertEquals(lead_Name, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}