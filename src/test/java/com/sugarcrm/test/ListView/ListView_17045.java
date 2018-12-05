package com.sugarcrm.test.ListView;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_17045 extends SugarTest {
	DataSource accountsRecordData = new DataSource();
	FieldSet filterData = new FieldSet();

	public void setup() throws Exception {
		filterData = testData.get(testName).get(0);
		accountsRecordData = testData.get(testName + "_" + sugar().accounts.moduleNamePlural);
		sugar().accounts.api.create(accountsRecordData);
		sugar().login();

		// Create few Favorites records
		sugar().accounts.navToListView();
		sugar().accounts.listView.toggleFavorite(1);
		sugar().accounts.listView.toggleFavorite(3);
		sugar().accounts.listView.toggleFavorite(5);
	}

	/**
	 * Verify Next/Previous widget on record view header filtered list
	 * 
	 * @throws Exception
	 */
	@Test
	public void ListView_17045_WithExistingFilter_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// In the Accounts list view Select one of the existing filters for the list view
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterMyFavorites();

		// Verify that only Favorites records are visible
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(accountsRecordData.get(5).get("name"), true);
		sugar().accounts.listView.getDetailField(2, "name").assertEquals(accountsRecordData.get(3).get("name"), true);
		sugar().accounts.listView.getDetailField(3, "name").assertEquals(accountsRecordData.get(1).get("name"), true);
		Assert.assertTrue("Favorites records are not filtered correctly", sugar().accounts.listView.countRows() == (accountsRecordData.size()/2));

		// Click on the first account of list view to display the detail view
		sugar().accounts.listView.clickRecord(1);

		// Define Controls
		// TODO: VOOD-1445
		VoodooControl previousBtnCtrl = new VoodooControl("button", "css", ".btn-group-previous-next .previous-row");
		VoodooControl nextBtnCtrl = new VoodooControl("button", "css", ".btn-group-previous-next .next-row");
		VoodooControl accountsNameCtrl = sugar().accounts.recordView.getDetailField("name");

		// Verify that the account record is displayed properly
		accountsNameCtrl.assertEquals(accountsRecordData.get(5).get("name"), true);

		// Verify that for the first record the "Left" or "Previous" arrow is disabled and the "Right" or "Next" arrow is enabled if there are more records following the first record
		previousBtnCtrl.assertAttribute("class", filterData.get("disabled"), true);
		nextBtnCtrl.assertAttribute("class", filterData.get("disabled"), false);

		// Click the "Next" arrow
		sugar().accounts.recordView.gotoNextRecord();

		// Verify that the next record is displayed properly
		accountsNameCtrl.assertEquals(accountsRecordData.get(3).get("name"), true);

		// Click the "Previous" (left) arrow.
		sugar().accounts.recordView.gotoPreviousRecord();

		// Verify that the previous record is displayed
		accountsNameCtrl.assertEquals(accountsRecordData.get(5).get("name"), true);

		// Go back to the list view and click on the last record on the list
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(3);

		// Verify that the account record is displayed properly
		accountsNameCtrl.assertEquals(accountsRecordData.get(1).get("name"), true);

		// Verify that the "Next" arrow is grayed out since there are no more records.
		previousBtnCtrl.assertAttribute("class", filterData.get("disabled"), false);
		nextBtnCtrl.assertAttribute("class", filterData.get("disabled"), true);

		// Go back to list view and click on a column header to change the sort order
		sugar().accounts.navToListView();
		sugar().accounts.listView.sortBy("headerName", true);

		// Verify that the records should sort properly
		sugar().accounts.listView.getDetailField(3, "name").assertEquals(accountsRecordData.get(5).get("name"), true);
		sugar().accounts.listView.getDetailField(2, "name").assertEquals(accountsRecordData.get(3).get("name"), true);
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(accountsRecordData.get(1).get("name"), true);

		// Click a record and note the previous and next record name on the list view
		sugar().accounts.listView.clickRecord(1);

		// Verify that the account record is displayed properly
		accountsNameCtrl.assertEquals(accountsRecordData.get(1).get("name"), true);

		// Verify that for the first record the "Left" or "Previous" arrow is disabled and the "Right" or "Next" arrow is enabled if there are more records following the first record
		previousBtnCtrl.assertAttribute("class", filterData.get("disabled"), true);
		nextBtnCtrl.assertAttribute("class", filterData.get("disabled"), false);

		// Click the "Next" arrow
		sugar().accounts.recordView.gotoNextRecord();

		// Verify that the next record is displayed properly
		accountsNameCtrl.assertEquals(accountsRecordData.get(3).get("name"), true);

		// Click the "Previous" (left) arrow.
		sugar().accounts.recordView.gotoPreviousRecord();

		// Verify that the previous record is displayed
		accountsNameCtrl.assertEquals(accountsRecordData.get(1).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Test
	public void ListView_17045_WithCustomFilter_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a custom filter
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();
		sugar().accounts.listView.filterCreate.setFilterFields("name", filterData.get("columnDisplayName"), filterData.get("operator"), sugar().accounts.moduleNameSingular, 1);
		sugar().accounts.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().accounts.listView.filterCreate.save();

		// Verify that only filtered records are visible
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(accountsRecordData.get(4).get("name"), true);
		sugar().accounts.listView.getDetailField(2, "name").assertEquals(accountsRecordData.get(2).get("name"), true);
		sugar().accounts.listView.getDetailField(3, "name").assertEquals(accountsRecordData.get(0).get("name"), true);
		Assert.assertTrue("Filtered records are not filtered correctly", sugar().accounts.listView.countRows() == (accountsRecordData.size()/2));

		// Click on the first account of list view to display the detail view
		sugar().accounts.listView.clickRecord(1);

		// Define Controls
		// TODO: VOOD-1445
		VoodooControl previousBtnCtrl = new VoodooControl("button", "css", ".btn-group-previous-next .previous-row");
		VoodooControl nextBtnCtrl = new VoodooControl("button", "css", ".btn-group-previous-next .next-row");
		VoodooControl accountsNameCtrl = sugar().accounts.recordView.getDetailField("name");

		// Verify that the account record is displayed properly
		accountsNameCtrl.assertEquals(accountsRecordData.get(4).get("name"), true);

		// Verify that for the first record the "Left" or "Previous" arrow is disabled and the "Right" or "Next" arrow is enabled if there are more records following the first record
		previousBtnCtrl.assertAttribute("class", filterData.get("disabled"), true);
		nextBtnCtrl.assertAttribute("class", filterData.get("disabled"), false);

		// Click the "Next" arrow
		sugar().accounts.recordView.gotoNextRecord();

		// Verify that the next record is displayed properly
		accountsNameCtrl.assertEquals(accountsRecordData.get(2).get("name"), true);

		// Click the "Previous" (left) arrow.
		sugar().accounts.recordView.gotoPreviousRecord();

		// Verify that the previous record is displayed
		accountsNameCtrl.assertEquals(accountsRecordData.get(4).get("name"), true);

		// Go back to the list view and click on the last record on the list
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(3);

		// Verify that the account record is displayed properly
		accountsNameCtrl.assertEquals(accountsRecordData.get(0).get("name"), true);

		// Verify that the "Next" arrow is grayed out since there are no more records.
		previousBtnCtrl.assertAttribute("class", filterData.get("disabled"), false);
		nextBtnCtrl.assertAttribute("class", filterData.get("disabled"), true);

		// Go back to list view and click on a column header to change the sort order
		sugar().accounts.navToListView();
		sugar().accounts.listView.sortBy("headerName", true);

		// Verify that the records should sort properly
		sugar().accounts.listView.getDetailField(3, "name").assertEquals(accountsRecordData.get(4).get("name"), true);
		sugar().accounts.listView.getDetailField(2, "name").assertEquals(accountsRecordData.get(2).get("name"), true);
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(accountsRecordData.get(0).get("name"), true);

		// Click a record and note the previous and next record name on the list view
		sugar().accounts.listView.clickRecord(1);

		// Verify that the account record is displayed properly
		accountsNameCtrl.assertEquals(accountsRecordData.get(0).get("name"), true);

		// Verify that for the first record the "Left" or "Previous" arrow is disabled and the "Right" or "Next" arrow is enabled if there are more records following the first record
		previousBtnCtrl.assertAttribute("class", filterData.get("disabled"), true);
		nextBtnCtrl.assertAttribute("class", filterData.get("disabled"), false);

		// Click the "Next" arrow
		sugar().accounts.recordView.gotoNextRecord();

		// Verify that the next record is displayed properly
		accountsNameCtrl.assertEquals(accountsRecordData.get(2).get("name"), true);

		// Click the "Previous" (left) arrow.
		sugar().accounts.recordView.gotoPreviousRecord();

		// Verify that the previous record is displayed
		accountsNameCtrl.assertEquals(accountsRecordData.get(0).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}