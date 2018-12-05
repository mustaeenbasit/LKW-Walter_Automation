package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22979 extends SugarTest {
	DataSource oppData = new DataSource();
	FieldSet massUpdateData = new FieldSet();
	StandardSubpanel opportunitiesSubpanelCtrl;

	public void setup() throws Exception {
		oppData = testData.get(testName);
		massUpdateData = testData.get(testName + "_MassUpdate").get(0);

		// Create an Account record
		sugar().accounts.api.create();

		// Create Opportunity records
		sugar().opportunities.api.create(oppData);

		// Login as a valid user
		sugar().login();

		// Link the Opportunity records with the created Account record
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.toggleSelectAll();
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.massUpdate();

		// TODO: VOOD-1587 and VOOD-1003
		new VoodooSelect("div", "css", ".filter-field .select2-container").set(massUpdateData.get("accountName"));
		new VoodooSelect("div", "css", ".massupdate.fld_account_name .select2-container").set(sugar().accounts.getDefaultData().get("name"));
		new VoodooControl("a", "css", ".massupdate.fld_update_button a").click();
		VoodooUtils.waitForReady(30000); // Extra wait needed
	}

	/**
	 * Account Detail - Opportunities sub-panel - Pagination_Verify that corresponding opportunity records list view is displayed after clicking the pagination control link on "OPPORTUNITIES" sub-panel of an account record view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22979_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigates to the record view of the created Account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Observe the number of records are displayed in the Opportunity sub panel
		opportunitiesSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		opportunitiesSubpanelCtrl.expandSubpanel();
		opportunitiesSubpanelCtrl.scrollIntoViewIfNeeded(false);
		opportunitiesSubpanelCtrl.getControl("count").assertContains(massUpdateData.get("collectionCountByDefault"), true);
		Assert.assertTrue("Opportonity records are not equals to 5 in the opportunity subpanel", opportunitiesSubpanelCtrl.countRows() == Integer.parseInt(massUpdateData.get("defaultRecordsCount")));

		// Click "More Opportunities..."in "OPPORTUNITIES" sub-panel
		opportunitiesSubpanelCtrl.showMore();
		VoodooUtils.waitForReady();

		// Verify that the next opportunity records list view are displayed
		opportunitiesSubpanelCtrl.getControl("count").assertContains(massUpdateData.get("collectionCountAfterShowMore"), true);
		Assert.assertTrue("Opportonity records are not equals to 7 in the opportunity subpanel", opportunitiesSubpanelCtrl.countRows() == oppData.size());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}