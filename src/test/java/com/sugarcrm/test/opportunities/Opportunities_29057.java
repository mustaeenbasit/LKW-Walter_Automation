package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Opportunities_29057 extends SugarTest {
	FieldSet data = new FieldSet();

	public void setup() throws Exception {
		DataSource rliRecords = testData.get(testName+"_rli");
		data = testData.get(testName).get(0);
		OpportunityRecord myOpportunityRecord = (OpportunityRecord) sugar().opportunities.api.create();
		sugar().revLineItems.api.create(rliRecords);
		sugar().login();
		sugar().revLineItems.navToListView();

		// Checking all the RLI checkboxes created above except one
		sugar().revLineItems.listView.toggleSelectAll();
		sugar().revLineItems.listView.uncheckRecord(rliRecords.size());

		// Linking the RLIs to the Opportunity created above
		FieldSet massUpdate = new FieldSet();
		massUpdate.put(data.get("massUpdateField"), myOpportunityRecord.getRecordIdentifier());
		sugar().revLineItems.massUpdate.performMassUpdate(massUpdate);
	}

	/**
	 * Verify that only RLI linked with opportunity should be dispalyed on selecting checkbox 
	 * "Select all records" from RLI subpanel in Opportunity.
	 * @throws Exception
	 */
	@Test
	public void Opportunities_29057_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunities module and select first record
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Move to the RLI subpanel
		StandardSubpanel rliSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubPanel.expandSubpanel();

		// click the 'Select all records' link in the subpanel
		rliSubPanel.toggleSelectAll();
		rliSubPanel.clickSelectAllRecordsLink();

		// Verify that message 'You have selected all 6 records in the result set. Clear selections.' is visible
		rliSubPanel.getControl("selectedRecordsAlert").assertEquals(data.get("message"), true);
		rliSubPanel.clickClearSelectionsLink();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}