package com.sugarcrm.test.RevenueLineItems;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_29362 extends SugarTest {

	public void setup() throws Exception {
		FieldSet customFS = testData.get(testName).get(0);
		DataSource customData = testData.get(testName + "_rli_data");
		AccountRecord myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().opportunities.api.create();

		// Creating 7 records of RLI module
		sugar().revLineItems.api.create(customData);
		sugar().login();

		// TODO: VOOD-444 : Support creating relationships via API.
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().opportunities.recordView.save();

		// An opportunity with more then 5 not closed RLI records associated to it(e.g. 7)
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.toggleSelectAll();
		FieldSet massUpdateData = new FieldSet();
		massUpdateData.put(customFS.get("massUpdateField1"), sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.massUpdate.performMassUpdate(massUpdateData);
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Verify that only RLIs related to linked opportunity are deleted when delete all RLI records from RLI subpanel
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_29362_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Opportunity recordView
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Go to RLI subPanel and remove first pagination records(5 records only)
		StandardSubpanel rliOppSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliOppSubpanel.expandSubpanel();
		rliOppSubpanel.toggleSelectAll();
		rliOppSubpanel.openActionDropdown();
		rliOppSubpanel.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Verify that only RLIs records which belong to this particular Opportunity and in first page are deleted i.e.5 records rest and rest two records are still there.
		Assert.assertTrue("Record count in RLI subpanel is not equal to 2.", rliOppSubpanel.countRows() == 2);

		// Go to Accounts recordView
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Go to RLI subPanel and verify there have two records exist
		StandardSubpanel rliAccountSubPanel = sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliAccountSubPanel.expandSubpanel();

		// Verify that User can't delete or perform any action as given in step 6 for Account >> RLI
		Assert.assertTrue("Record count in RLI subpanel is not equal to 2.", rliAccountSubPanel.countRows() == 2);
		rliAccountSubPanel.getControl("selectAllCheckbox").assertExists(false);
		rliAccountSubPanel.getControl("checkbox02").assertExists(false);
		rliAccountSubPanel.expandSubpanelRowActions(1);
		rliAccountSubPanel.getControl("deleteButton").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}