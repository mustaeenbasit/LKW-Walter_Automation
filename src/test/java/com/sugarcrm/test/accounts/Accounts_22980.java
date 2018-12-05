package com.sugarcrm.test.accounts;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22980 extends SugarTest {
	DataSource leadData = new DataSource();
	StandardSubpanel leadsSubpanelCtrl;

	public void setup() throws Exception {
		leadData = testData.get(testName + "_" + sugar().leads.moduleNamePlural);
		ArrayList<Record> leadRecords = new ArrayList<Record>();

		// Create an Account record
		sugar().accounts.api.create();

		// Create Leads records and add them into an ArrayList
		leadRecords.addAll(sugar().leads.api.create(leadData));

		// Login as a valid user
		sugar().login();

		// Link the Leads records with the created Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		leadsSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanelCtrl.linkExistingRecords(leadRecords);
	}

	/**
	 * Account Detail - Leads sub-panel - Pagination_Verify that corresponding lead records list view is displayed after 
	 * clicking the pagination control link on "LEADS" sub-panel of an account record view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22980_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet paginationData = testData.get(testName).get(0);

		// Navigates to the record view of the created Account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		VoodooUtils.waitForReady();

		// Observe the number of records are displayed in the Leads sub panel
		leadsSubpanelCtrl.expandSubpanel();
		leadsSubpanelCtrl.scrollIntoViewIfNeeded(false);
		leadsSubpanelCtrl.getControl("count").assertContains(paginationData.get("collectionCountByDefault"), true);
		Assert.assertTrue("Leads records are not equals to 5 in the leads subpanel", leadsSubpanelCtrl.countRows() == Integer.parseInt(paginationData.get("defaultRecordsCount")));

		// Click "More Leads..." in "LEADS" sub-panel.
		leadsSubpanelCtrl.showMore();
		VoodooUtils.waitForReady();

		// Verify that the next leads records list view are displayed
		leadsSubpanelCtrl.getControl("count").assertContains(paginationData.get("collectionCountAfterShowMore"), true);
		Assert.assertTrue("Leads records are not equals to 7 in the leads subpanel", leadsSubpanelCtrl.countRows() == leadData.size());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}