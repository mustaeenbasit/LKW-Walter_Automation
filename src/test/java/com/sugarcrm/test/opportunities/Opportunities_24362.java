package com.sugarcrm.test.opportunities;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24362 extends SugarTest {
	DataSource leadsData = new DataSource();
	ArrayList<Record> leadRecords = new ArrayList<Record>();

	public void setup() throws Exception {
		leadsData = testData.get(testName);

		// Create Opportunity and Leads record(s)
		sugar().opportunities.api.create();
		leadRecords.addAll(sugar().leads.api.create(leadsData));

		// Login
		sugar().login();
	}

	/**
	 * Select Lead_Verify that an existing leads can be selected for the opportunity by using check box
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24362_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Opportunity record view
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Link Leads record
		StandardSubpanel leadsSubpanelCtrl = sugar().opportunities.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanelCtrl.scrollIntoViewIfNeeded(false);
		leadsSubpanelCtrl.linkExistingRecords(leadRecords);

		// Verify that the selected leads are displayed in "Leads" sub-panel
		Assert.assertTrue("Selected leads are not displayed in 'Leads' sub-panel", leadsSubpanelCtrl.countRows() == leadsData.size());

		// TODO: VOOD-1828
		leadsSubpanelCtrl.sortBy("headerFullname", false);
		VoodooUtils.waitForReady();

		leadsSubpanelCtrl.getDetailField(1, "fullName").assertElementContains(leadsData.get(2).get("lastName"), true);
		leadsSubpanelCtrl.getDetailField(2, "fullName").assertElementContains(leadsData.get(1).get("lastName"), true);
		leadsSubpanelCtrl.getDetailField(3, "fullName").assertElementContains(leadsData.get(0).get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}