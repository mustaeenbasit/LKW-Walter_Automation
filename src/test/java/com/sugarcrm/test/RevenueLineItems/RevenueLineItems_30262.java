package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ruchi Bhatnagar <rbhatnagar@sugarcrm.com>
 */
public class RevenueLineItems_30262 extends SugarTest {
	DataSource rliData = new DataSource();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		rliData = testData.get(testName);
		sugar().revLineItems.api.create(rliData);
		sugar().login();
	}

	/**
	 * Verify that RLIs with sales stage closed won & closed lost could not be deleted from the RLI subpanel
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30262_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create Opportunity with related account and RLI
		sugar().opportunities.create();

		// Link 5 more RLI's to this opp
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.toggleSelectAll();
		FieldSet massUpdateData = new FieldSet();
		massUpdateData.put("Opportunity Name", sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.massUpdate.performMassUpdate(massUpdateData);

		// Open the detail view of Opportunity. 
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.expandSubpanel();

		// In RLI sub-panel, Click on select all check-box next to action button.
		rliSubpanel.toggleSelectAll();

		// Click on 'Select all records' link 
		rliSubpanel.clickSelectAllRecordsLink();
		rliSubpanel.showMore();

		// Verify all 6 records get selected
		for (int i = 1; i <= rliData.size() + 1; i++) {
			rliSubpanel.getControl(String.format("checkbox%02d", i)).assertChecked(true);
		}

		// Click on delete button in 'action' drop-down.
		rliSubpanel.openActionDropdown();
		rliSubpanel.delete();

		// Verify RLI's with "Closed won & Closed Lost" stage should get unchecked.
		for (int i = 1; i <= rliData.size() + 1; i++) {
			Boolean closedWon = rliSubpanel.getDetailField(i, "salesStage").queryContains(rliData.get(0).get("salesStage"), true);
			Boolean closedLost = rliSubpanel.getDetailField(i, "salesStage").queryContains(rliData.get(1).get("salesStage"), true);

			if (closedWon)
				rliSubpanel.getControl(String.format("checkbox%02d", i)).assertChecked(false);

			else if (closedLost)
				rliSubpanel.getControl(String.format("checkbox%02d", i)).assertChecked(false);

			else
				rliSubpanel.getControl(String.format("checkbox%02d", i)).assertChecked(true);
		}

		FieldSet customData = testData.get(testName + "_customData").get(0);

		// Verify Warning message is displayed.
		sugar().alerts.getWarning().assertEquals(customData.get("warningText"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}