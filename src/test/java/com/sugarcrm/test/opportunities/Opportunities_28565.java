package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28565 extends SugarTest {
	DataSource rliData;
	FieldSet updateData;
	StandardSubpanel rliSubpanel;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		rliData = testData.get(testName);
		sugar().revLineItems.api.create(rliData);
		updateData = testData.get(testName+"_data").get(0);
		sugar().login();

		// Link Opp to an account
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.listView.saveRecord(1);

		// More than 5 RLI records linked to the above opportunity
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.toggleSelectAll();
		sugar().revLineItems.massUpdate.performMassUpdate(updateData);
	}	

	/**
	 * Verify that created record appears only once in RLI subpanel of opportunity record view after opportunity record is resaved
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28565_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		// Create one more RLI from the subpanel
		rliSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.addRecord();
		FieldSet fs = sugar().revLineItems.getDefaultData();
		sugar().revLineItems.createDrawer.getEditField("name").set(fs.get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(fs.get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(fs.get("likelyCase"));
		sugar().revLineItems.createDrawer.save();

		// Click on the edit button of the opportunity
		sugar().opportunities.recordView.edit();
		// Save opportunity without changing anything
		sugar().opportunities.recordView.save();

		// In RLI subpanel, click (show more...) to show the new created RLI
		rliSubpanel.showMore();
		VoodooUtils.waitForReady();
		// Verify newly created RLI should load once and should be displayed once.
		Assert.assertTrue("Now total row count is rliData + 1", rliSubpanel.countRows() == (rliData.size()+1));
		rliSubpanel.assertContains(fs.get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}