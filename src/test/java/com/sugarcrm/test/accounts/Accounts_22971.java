package com.sugarcrm.test.accounts;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22971 extends SugarTest {
	DataSource caseData = new DataSource();
	FieldSet customFS;

	public void setup() throws Exception {
		caseData = testData.get(testName + "_" + sugar().cases.moduleNamePlural);
		customFS = testData.get(testName).get(0);
		ArrayList<Record> caseRecords = new ArrayList<Record>();
		AccountRecord myAccount = (AccountRecord) sugar().accounts.api.create();

		// Create multiple cases and add in ArrayList
		caseRecords.addAll(sugar().cases.api.create(caseData));
		sugar().login();

		// Set subPanel listView items = 2
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("systemSettings").click();
		VoodooUtils.waitForReady();
		sugar().admin.systemSettings.getControl("maxEntriesPerSubPanel").set(customFS.get("subpanelItems"));
		sugar().admin.systemSettings.getControl("save").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Associate cases records with Account record
		FieldSet massUpdateData = new FieldSet();
		massUpdateData.put("Account Name", myAccount.getRecordIdentifier());
		sugar().cases.navToListView();
		sugar().cases.listView.toggleSelectAll();
		sugar().cases.massUpdate.performMassUpdate(massUpdateData);
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Verify that corresponding case records' list view is displayed after clicking the pagination control link on "CASES" sub-panel of an account record view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22971_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Account recordView > Cases SubPanel
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel caseSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural);
		caseSubpanelCtrl.scrollIntoViewIfNeeded(false);
		caseSubpanelCtrl.expandSubpanel();
		caseSubpanelCtrl.getControl("count").assertContains(customFS.get("collectionCountByDefault"), true);
		Assert.assertTrue("Cases records are not equals to 2 in cases subpanel", caseSubpanelCtrl.countRows() == Integer.parseInt(customFS.get("subpanelItems")));

		// Click "More Cases..." such times as needed (to view more cases in subpanel)
		caseSubpanelCtrl.showMore();
		VoodooUtils.waitForReady();

		// Verify that the next case records are showed
		caseSubpanelCtrl.getControl("count").assertContains(customFS.get("collectionCountAfterShowMore"), true);
		Assert.assertTrue("Cases records are not equals to 4 in cases subpanel", caseSubpanelCtrl.countRows() == caseData.size());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}