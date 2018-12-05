package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_24382 extends SugarTest {
	DataSource oppData = new DataSource();

	public void setup() throws Exception {
		oppData = testData.get(testName + "_" + sugar().opportunities.moduleNamePlural);

		// Create and Account record
		sugar().accounts.api.create();

		// Login
		sugar().login();

		// Create 4 Opportunity records (Need to create duplicate records)
		// TODO: VOOD-444
		sugar().opportunities.navToListView();
		for(int i = 0; i < oppData.size(); i++) {
			sugar().opportunities.listView.create();
			sugar().opportunities.createDrawer.showMore();
			sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.getDefaultData().get("name"));
			sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().opportunities.getDefaultData().get("relAccountName"));
			sugar().opportunities.createDrawer.getEditField("date_closed").set(sugar().opportunities.getDefaultData().get("date_closed"));
			sugar().opportunities.createDrawer.getEditField("likelyCase").set(oppData.get(i).get("likelyCase"));
			sugar().opportunities.createDrawer.save();
			if (i > 0)
				sugar().opportunities.createDrawer.ignoreDuplicateAndSave();
			sugar().alerts.getSuccess().closeAlert();
		}
	}

	/**
	 * Merge Duplicate_Verify that merging record can be removed when more than one opportunities are selected as merging records.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24382_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet filterData = testData.get(testName).get(0);

		// Navigates to Opportunity List View
		sugar().opportunities.navToListView();

		// Verify that 4 Opportunity records are exist
		Assert.assertTrue("Opportunity records count is not exactly 4", sugar().opportunities.listView.countRows() == oppData.size());

		// Navigating to Opportunities record view
		sugar().opportunities.listView.clickRecord(1);

		// Click Find Duplicate; button in the opportunity record view
		sugar().opportunities.recordView.openPrimaryButtonDropdown();
		// TODO: VOOD-738, 691, 568, 578, 695
		new VoodooControl("a", "css", ".fld_find_duplicates_button").click();
		VoodooUtils.waitForReady();

		// Set the Filter Condition; for the selecting fields from Available Fields; to get more than one records result
		// TODO: VOOD-1899 and VOOD-681
		new VoodooControl("span", "css", ".layout_Opportunities:nth-child(2) .filter-view.search .choice-filter-label").click();
		new VoodooSelect("div", "css", ".fld_filter_row_name.detail div").set(filterData.get("field"));
		new VoodooSelect("div", "css", ".fld_filter_row_operator.detail div").set(filterData.get("operator"));
		new VoodooControl("input", "css", ".filter-definition-container [data-filter='row'] .fld_amount input[name='amount']").set(oppData.get(0).get("likelyCase"));
		VoodooUtils.waitForReady();

		// Select two opportunity from the searched opportunities result list
		new VoodooControl("input", "css", ".checkall input").click();

		// Click Perform Merge button
		new VoodooControl("a", "css", ".fld_merge_duplicates_button a").click();
		VoodooUtils.waitForReady();

		// Define controls for records columns
		VoodooControl firstColumnCtrl = new VoodooControl("div", "css", ".col:nth-child(1)");
		VoodooControl secondColumnCtrl = new VoodooControl("div", "css", ".col:nth-child(2)");
		VoodooControl thirdColumnCtrl = new VoodooControl("div", "css", ".col:nth-child(3)");
		VoodooControl deleteBtnCtrl = new VoodooControl("button", "css", " button[data-action='delete']");
		VoodooControl thirdColumnRemoveBtnCtrl = new VoodooControl("button", "css", thirdColumnCtrl.getHookString() + deleteBtnCtrl.getHookString());

		// Ensure that three records columns are exist on the Merge record page
		firstColumnCtrl.assertExists(true);
		secondColumnCtrl.assertExists(true);
		thirdColumnCtrl.assertExists(true);

		// Click Remove link for one of the merging record (remove 3rd record column)
		thirdColumnRemoveBtnCtrl.click();
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();

		// Verify that the merging record disappears in the page
		firstColumnCtrl.assertExists(true);
		secondColumnCtrl.assertExists(true);
		thirdColumnCtrl.assertExists(false);

		// Verify that remove button of another merging record disappeared
		new VoodooControl("button", "css", firstColumnCtrl.getHookString() + deleteBtnCtrl.getHookString()).assertVisible(false);
		new VoodooControl("button", "css", secondColumnCtrl.getHookString() + deleteBtnCtrl.getHookString()).assertVisible(false);
		thirdColumnRemoveBtnCtrl.assertVisible(false);

		// Click merge
		// TODO: VOOD-681
		new VoodooControl("a", "css", ".merge-duplicates-headerpane.fld_save_button a").click();
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady(40000); // Extra wait needed

		// Navigates to Opportunity list view
		sugar().opportunities.navToListView();

		// Verify that two record are merged into one and total 3 record are exist.
		Assert.assertTrue("Opportunity records count is not exactly 3", sugar().opportunities.listView.countRows() == oppData.size() - 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}