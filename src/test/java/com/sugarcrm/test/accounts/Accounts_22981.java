package com.sugarcrm.test.accounts;

import java.util.ArrayList;
import java.util.Map;


import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22981 extends SugarTest {
	DataSource leadData = new DataSource();
	StandardSubpanel leadsSubpanelCtrl;

	public void setup() throws Exception {
		leadData = testData.get(testName);
		ArrayList<Record> leadRecords = new ArrayList<Record>();

		// Create an Account record
		sugar().accounts.api.create();

		// Login as a valid user
		sugar().login();

		// Create Leads records and add them into an ArrayList
		// VOOD-444
		leadRecords.addAll(sugar().leads.create(leadData));

		// Link the Leads records with the created Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		leadsSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanelCtrl.scrollIntoViewIfNeeded(false);
		leadsSubpanelCtrl.linkExistingRecords(leadRecords);
	}

	/**
	 * Account Detail - Leads sub-panel - Sort_Verify that lead records related to the accounts can be sorted  by column titles on "LEADS" sub-panel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22981_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigates to the record view of the created Account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		VoodooUtils.waitForReady();

		// Add the Leads Subpanel headers

		ArrayList<String> columnsHeaderName = new ArrayList<String>();
		columnsHeaderName.add("headerPhonework");
		columnsHeaderName.add("headerEmail");
		columnsHeaderName.add("headerAssignedusername");
		columnsHeaderName.add("headerFullname");
		columnsHeaderName.add("headerLeadsource");

		// Add all the keys(i.e. fieldName) in ArrayList
		ArrayList<String> keys = new ArrayList<String>();

		for(Map.Entry<String,String> entry : leadData.get(0).entrySet()) {
			keys.add(entry.getKey().toString());
		}

		// Perform sorting and verify the results
		int recordsCount = leadData.size();
		for(int i = 0; i < columnsHeaderName.size(); i++) {

			// Sort by Column (Descending)
			leadsSubpanelCtrl.sortBy(columnsHeaderName.get(i), false);
			VoodooUtils.waitForReady();

			// Verify that the Lead records are sorted according to the column titles on "Leads" sub-panel
			for(int j = recordsCount; j > 0; j--) {
				int rowNumber = recordsCount - j + 1;
				int recordsKey = j-1;
				if (i < 3) {
					leadsSubpanelCtrl.getDetailField(rowNumber, keys.get(i+1)).assertContains(leadData.get(recordsKey).get(keys.get(i+1)), true);
					leadsSubpanelCtrl.getDetailField(rowNumber, "fullName").assertContains(leadData.get(recordsKey).get(keys.get(0)), true);
				} else if (i == 3) {
					// For Full Name
					leadsSubpanelCtrl.getDetailField(rowNumber, "fullName").assertContains(leadData.get(recordsKey).get(keys.get(0)), true);
				} else {
					// For Lead Source
					// TODO: VOOD-627, VOOD-598 and VOOD-710
					new VoodooControl("div", "css", ".layout_Leads .flex-list-view-content tr:nth-child(" + rowNumber + ") .fld_lead_source.list div").assertContains(leadData.get(recordsKey).get(keys.get(4)), true);
					leadsSubpanelCtrl.getDetailField(rowNumber, "fullName").assertContains(leadData.get(recordsKey).get(keys.get(0)), true);
				}
			}
			// Sort by Column (Ascending)
			leadsSubpanelCtrl.sortBy(columnsHeaderName.get(i), true);
			VoodooUtils.waitForReady();

			// Verify that the Lead records are sorted according to the column titles on "Leads" sub-panel.
			for(int j = 0; j < recordsCount; j++) {
				int rowNumber = j+1;
				int recordsKey = j;
				if (i < 3) {
					leadsSubpanelCtrl.getDetailField(rowNumber, keys.get(i+1)).assertContains(leadData.get(recordsKey).get(keys.get(i+1)), true);
					leadsSubpanelCtrl.getDetailField(rowNumber, "fullName").assertContains(leadData.get(recordsKey).get(keys.get(0)), true);
				} else if (i == 3) {
					// For Full Name
					leadsSubpanelCtrl.getDetailField(rowNumber, "fullName").assertContains(leadData.get(recordsKey).get(keys.get(0)), true);
				} else {
					// For Lead Source
					// TODO: VOOD-627, VOOD-598 and VOOD-710
					new VoodooControl("div", "css", ".layout_Leads .flex-list-view-content tr:nth-child(" + rowNumber + ") .fld_lead_source.list div").assertContains(leadData.get(recordsKey).get(keys.get(4)), true);
					leadsSubpanelCtrl.getDetailField(rowNumber, "fullName").assertContains(leadData.get(recordsKey).get(keys.get(0)), true);
				}
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}