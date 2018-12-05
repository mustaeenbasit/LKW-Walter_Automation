package com.sugarcrm.test.leads;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_28265 extends SugarTest {
	StandardSubpanel leadSubPanel;

	public void setup() throws Exception {
		ArrayList<Record> myLeads = new ArrayList<Record>();

		// Create two Leads record with different name
		LeadRecord myLeadRecord1 = (LeadRecord) sugar().leads.api.create();
		FieldSet fs = new FieldSet();
		fs.put("lastName", testName);
		LeadRecord myLeadRecord2 = (LeadRecord) sugar().leads.api.create(fs);
		myLeads.add(myLeadRecord1);
		myLeads.add(myLeadRecord2);
		sugar().accounts.api.create();
		sugar().login();

		// Existing lead record with account name (use non-existing account name)
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.edit();
		sugar().leads.recordView.getEditField("accountName").set("ABC"+testName);
		sugar().leads.recordView.save();

		// Go to Account recordView
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		leadSubPanel = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);

		// Under leads sub-panel, link two leads which is created above
		leadSubPanel.linkExistingRecords(myLeads);
	}

	/**
	 * Verify Account name in lead list view and lead record can be updated consistently
	 *
	 * @throws Exception
	 */
	@Test
	public void Leads_28265_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Click on first lead record view
		leadSubPanel.clickRecord(1);

		// Verify that in first lead record view, the account name is updated to "testName".
		VoodooControl accountName = sugar().leads.recordView.getDetailField("accountName");
		accountName.assertEquals(sugar().accounts.getDefaultData().get("name"), true);

		// Go back to Account recordView and click second record from leads sub-panel
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Click on second lead record view
		leadSubPanel.clickRecord(2);

		// Verify that in first lead record view, the account name is updated to "testName".
		accountName.assertEquals(sugar().accounts.getDefaultData().get("name"), true);

		// Go to leads list view
		sugar().leads.navToListView();

		// TODO: TR-7160
		// Once this TR-7160 is resolved, uncomment below code lines
		// Verify that in lead list view, the account name is displayed as testName for both leads.
		// sugar().leads.listView.getDetailField(1,"accountName").assertContains(sugar().accounts.getDefaultData().get("name"), true);
		// sugar().leads.listView.getDetailField(2,"accountName").assertContains(sugar().accounts.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}