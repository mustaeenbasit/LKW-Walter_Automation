package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_21689 extends SugarTest {

	public void setup() throws Exception {
		sugar().revLineItems.api.create();
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Test Case 21689: Closed opportunity records won't get deleted in Mass Delete in Ent
	 * @throws Exception
	 */
	@Test
	public void Opportunities_21689_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create Opp records with RLI linked
		DataSource oppRliData = testData.get(testName+"_customData");
		sugar().opportunities.create(oppRliData);

		// Relate one of the above created opportunity record to a RLI record having Sales Stage 'Closed Won'.
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.sortBy("headerName", true);
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		FieldSet oppData = testData.get(testName).get(0);
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(oppRliData.get(0).get("name"));
		sugar().revLineItems.recordView.getEditField("salesStage").set(oppData.get("salesStage"));
		sugar().revLineItems.recordView.save();

		// Go to list view of opportunity module, check all records.
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.sortBy("headerName", true);
		sugar().opportunities.listView.toggleSelectAll();

		// Click on Mass Delete action.
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.delete();

		sugar().opportunities.listView.getControl("checkbox01").assertChecked(false);
		sugar().opportunities.listView.getControl("checkbox02").assertChecked(true);

		// Verify the warning message "One or more of the selected records has a RLI with status of Closed Won or Closed Lost and cannot be deleted." appears.
		sugar().alerts.getWarning().assertElementContains(oppData.get("warningMessage"),true);

		// Close the warning.
		sugar().alerts.closeAllWarning();

		// Perform Mass Delete action once again
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.delete();
		sugar().alerts.getAlert().confirmAlert();

		// Verify that the Closed opportunity records won't get deleted and is still in list view.
		sugar().opportunities.listView.verifyField(1, "name", oppRliData.get(0).get("name"));
		sugar().opportunities.listView.previewRecord(1);
		sugar().previewPane.getPreviewPaneField("name").assertElementContains(oppRliData.get(0).get("name"), true);

		// Verify that the second record should not populate.
		sugar().accounts.listView.getControl("checkbox02").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}