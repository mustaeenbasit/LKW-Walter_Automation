package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_24384 extends SugarTest {
	DataSource oppData = new DataSource();

	public void setup() throws Exception {
		oppData = testData.get(testName);

		// Create and Account record
		sugar().accounts.api.create();

		// Login
		sugar().login();

		// Create 2 Opportunity records
		// TODO: VOOD-444
		sugar().opportunities.navToListView();
		for(int i = 0; i < oppData.size(); i++) {
			sugar().opportunities.listView.create();
			sugar().opportunities.createDrawer.showMore();
			sugar().opportunities.createDrawer.getEditField("name").set(oppData.get(i).get("name"));
			sugar().opportunities.createDrawer.getEditField("type").set(oppData.get(i).get("type"));
			sugar().opportunities.createDrawer.getEditField("likelyCase").set(oppData.get(i).get("likelyCase"));
			sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().opportunities.getDefaultData().get("relAccountName"));
			sugar().opportunities.createDrawer.getEditField("date_closed").set(sugar().opportunities.getDefaultData().get("date_closed"));
			sugar().opportunities.createDrawer.save();
			sugar().alerts.getSuccess().closeAlert();
		}
	}

	/**
	 * Verify that records selected from opportunities list view can be merged.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24384_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigates to Opportunity List View
		sugar().opportunities.navToListView();

		// Verify that exactly 2 Opportunity records are exist
		Assert.assertTrue("Opportunity records count is not exactly 2", sugar().opportunities.listView.countRows() == oppData.size());

		// Select more than one opportunity from the opportunities list view
		sugar().opportunities.listView.toggleSelectAll();

		// Open the action drop down arrow on list view and click 'Merge' button
		sugar().opportunities.listView.openActionDropdown();
		// TODO: VOOD-689
		new VoodooControl("a", "css", ".fld_merge_button.list a").click();
		VoodooUtils.waitForReady();

		// Define controls for records columns
		// TODO: VOOD-721
		VoodooControl firstColumnCtrl = new VoodooControl("div", "css", ".col:nth-child(1)");
		VoodooControl secondColumnCtrl = new VoodooControl("div", "css", ".col:nth-child(2)");

		// Ensure that two records columns are exist on the Merge record page
		firstColumnCtrl.assertExists(true);
		secondColumnCtrl.assertExists(true); 

		// Select some values from the primary record and some values from the secondary record for different field(s)
		// TODO: VOOD-721
		// Select 'Likely' and 'Type' values from secondary record
		new VoodooControl("input", "css", secondColumnCtrl.getHookString() + " input[name='copy_amount']").click();
		new VoodooControl("input", "css", secondColumnCtrl.getHookString() + " input[name='copy_opportunity_type']").click();

		// Click 'Save' button and 'Confirm' the Warning message to save the merge result
		new VoodooControl("a", "css", ".merge-duplicates-headerpane.fld_save_button a").click();
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady(40000); // Needed extra wait

		// Verify that the List view is displayed after merging of records
		// TODO: VOOD-1887
		sugar().opportunities.listView.assertVisible(true);

		// Remove this block of code after VOOD-1887 is resolved
		// Verifying 'Module Title' and 'Create Button' to ensure user is redirected to the list view
		sugar().opportunities.listView.getControl("moduleTitle").assertEquals(sugar().opportunities.moduleNamePlural, true);
		sugar().opportunities.listView.getControl("createButton").assertEquals(oppData.get(1).get("description"), true);

		// Verify that the List view shows only One Opportunity record(with the selected 'Opportunity Name' while merging)
		Assert.assertTrue("User is now on Opportunities recordview and 1 record is exist", sugar().opportunities.listView.countRows() == 1);
		sugar().opportunities.listView.getDetailField(1, "name").assertEquals(oppData.get(1).get("name"), true);

		// Verify that the Other secondary record(s) are no longer visible
		sugar().opportunities.listView.assertContains(oppData.get(0).get("name"), false);

		// Navigates to the record view of the Merged record
		sugar().opportunities.listView.clickRecord(1);

		// Verify that the record shows the same values in different fields which are selected while merging records
		sugar().opportunities.recordView.getDetailField("likelyCase").assertContains(oppData.get(0).get("likelyCase"), true);
		sugar().opportunities.recordView.getDetailField("type").assertContains(oppData.get(0).get("type"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}