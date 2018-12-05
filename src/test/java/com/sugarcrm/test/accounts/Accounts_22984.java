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

public class Accounts_22984 extends SugarTest {
	DataSource bugsData = new DataSource();
	StandardSubpanel bugsSubpanelCtrl;

	public void setup() throws Exception {
		bugsData = testData.get(testName + "_" + sugar().bugs.moduleNamePlural);
		ArrayList<Record> bugRecords = new ArrayList<Record>();

		// Create an Account record
		sugar().accounts.api.create();

		// Create Bugs records and add them into an ArrayList
		bugRecords.addAll(sugar().bugs.api.create(bugsData));

		// Login as a valid user
		sugar().login();

		// Enable Bugs sub panel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);

		// Link the Bugs records with the created Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		bugsSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugsSubpanelCtrl.scrollIntoViewIfNeeded(false);
		bugsSubpanelCtrl.linkExistingRecords(bugRecords);
	}

	/**
	 * Account Detail - Bugs sub-panel - Pagination_Verify that corresponding bug records list view is displayed after
	 * clicking the pagination control link on "BUGS" sub-panel of an account record view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22984_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet paginationData = testData.get(testName).get(0);

		// Navigates to the record view of the created Account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		VoodooUtils.waitForReady();

		// Observe the number of records are displayed in the Bugs sub panel
		bugsSubpanelCtrl.expandSubpanel();
		bugsSubpanelCtrl.scrollIntoViewIfNeeded(false);
		bugsSubpanelCtrl.getControl("count").assertContains(paginationData.get("collectionCountByDefault"), true);
		Assert.assertTrue("Bugs records are not equals to 5 in the Bugs subpanel", bugsSubpanelCtrl.countRows() == Integer.parseInt(paginationData.get("defaultRecordsCount")));

		// Click "More Bugs..." in "BUGS" sub-panel.
		bugsSubpanelCtrl.showMore();
		VoodooUtils.waitForReady();

		// Verify that the next Bugs records are displayed
		bugsSubpanelCtrl.getControl("count").assertContains(paginationData.get("collectionCountAfterShowMore"), true);
		Assert.assertTrue("Bugs records are not equals to 7 in the Bugs subpanel", bugsSubpanelCtrl.countRows() == bugsData.size());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}