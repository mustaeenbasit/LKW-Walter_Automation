package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class ListViewTests extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().login();
	}

	@Test
	public void verifyControls() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyControls()...");

		sugar().accounts.api.create();
		sugar().accounts.navToListView();

		// Assert every defined control exists
		// Column headers
		for(String header : sugar().accounts.listView.getHeaders()) {
			sugar().accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		// Top row
		sugar().accounts.listView.getControl("moduleTitle").assertVisible(true);
		sugar().accounts.listView.getControl("count").assertVisible(true);
		sugar().accounts.listView.getControl("createButton").assertVisible(true);
		sugar().accounts.listView.getControl("toggleSidebar").assertVisible(true);

		// Filter bar
		sugar().accounts.listView.getControl("showListView").assertVisible(true);
		sugar().accounts.listView.getControl("activityStream").assertVisible(true);
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.getControl("filterAssignedToMe").assertVisible(true);
		sugar().accounts.listView.getControl("filterMyFavorites").assertVisible(true);
		sugar().accounts.listView.getControl("filterRecentlyViewed").assertVisible(true);
		sugar().accounts.listView.getControl("filterRecentlyCreated").assertVisible(true);
		sugar().accounts.listView.getControl("filterCreateNew").assertVisible(true);
		sugar().accounts.listView.getControl("searchSuggestion").assertExists(true);
		sugar().accounts.listView.getControl("searchFilterCurrent").assertVisible(true);
		sugar().accounts.listView.selectFilterAll(); // to close filter dropdown
		sugar().accounts.listView.getControl("searchFilter").assertVisible(true);
		sugar().accounts.listView.getControl("moreColumn").assertVisible(true);

		// Action header row
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.getControl("selectedRecordsAlert").assertVisible(true);
		sugar().accounts.listView.getControl("selectAllRecordsLink").assertVisible(true);
		sugar().accounts.listView.getControl("clearSelectionsLink").assertVisible(true);
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().accounts.listView.getControl("deleteButton").assertVisible(true);
		sugar().accounts.listView.getControl("exportButton").assertVisible(true);
		sugar().accounts.listView.getControl("actionDropdown").click(); // to close action dropdown
		sugar().accounts.listView.getControl("selectAllCheckbox").set(Boolean.toString(false)); // uncheck all

		// First Row controls
		sugar().accounts.listView.getControl("checkbox01").assertVisible(true);
		sugar().accounts.listView.getControl("favoriteStar01").assertVisible(true);
		sugar().accounts.listView.getControl("link01").assertVisible(true);
		sugar().accounts.listView.getControl("preview01").assertVisible(true);
		sugar().accounts.listView.getControl("dropdown01").assertVisible(true);
		sugar().accounts.listView.openRowActionDropdown(1);
		VoodooControl edit = sugar().accounts.listView.getControl("edit01");
		edit.assertVisible(true);
		sugar().accounts.listView.getControl("unfollow01").assertVisible(true);
		sugar().accounts.listView.getControl("follow01").assertVisible(true);
		sugar().accounts.listView.getControl("delete01").assertVisible(true);
		edit.click();
		sugar().accounts.listView.getControl("save01").assertVisible(true);
		sugar().accounts.listView.getControl("cancel01").click();

		// Misc alerts, etc.
		VoodooControl alertCancel = sugar().accounts.listView.getControl("cancelDelete");
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.delete();
		sugar().accounts.listView.getControl("confirmDelete").assertVisible(true);
		alertCancel.click();

		VoodooUtils.voodoo.log.info("verifyControls() test complete.");
	}

	@Test
	public void verifyModule() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyModule()...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.verifyModuleTitle(sugar().accounts.moduleNamePlural);

		VoodooUtils.voodoo.log.info("verifyModule() test complete.");
	}

	@Test
	public void updateRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running updateRecord()...");

		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		FieldSet updateData = new FieldSet();
		updateData.put("name", "Edit Account");
		sugar().accounts.listView.updateRecord(1, updateData);
		sugar().accounts.listView.verifyField(1, "name", updateData.get("name"));

		VoodooUtils.voodoo.log.info("updateRecord() test complete.");
	}

	@Test
	public void getModuleTitle() throws Exception {
		VoodooUtils.voodoo.log.info("Running getModuleTitle()...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.assertElementContains(sugar().accounts.listView.getModuleTitle(), true);

		VoodooUtils.voodoo.log.info("getModuleTitle() test complete.");
	}

	@Test
	public void massUpdate() throws Exception {
		VoodooUtils.voodoo.log.info("Running massUpdate()...");

		// 2 calls
		sugar().calls.api.create();
		sugar().calls.api.create();
		sugar().calls.navToListView();

		// Verify default API record values
		sugar().calls.listView.verifyField(1, "status", sugar().calls.getDefaultData().get("status"));
		sugar().calls.listView.verifyField(2, "status", sugar().calls.getDefaultData().get("status"));
		sugar().calls.listView.verifyField(1, "direction", sugar().calls.getDefaultData().get("direction"));
		sugar().calls.listView.verifyField(2, "direction", sugar().calls.getDefaultData().get("direction"));

		// Mass update with only first record
		sugar().calls.listView.checkRecord(1);
		sugar().calls.listView.openActionDropdown();
		sugar().calls.listView.massUpdate();
		sugar().calls.massUpdate.getControl("massUpdateField02").set("Direction");
		sugar().calls.massUpdate.getControl("massUpdateValue02").set("Outbound");
		sugar().calls.massUpdate.addRow(2);
		sugar().calls.massUpdate.getControl("massUpdateField03").set("Status");
		sugar().calls.massUpdate.getControl("massUpdateValue03").set("Held");
		sugar().calls.massUpdate.update();

		// Verify values updated only in first record
		sugar().calls.listView.verifyField(1, "status", "Held");
		sugar().calls.listView.verifyField(2, "status", sugar().calls.getDefaultData().get("status"));
		sugar().calls.listView.verifyField(1, "direction", "Outbound");
		sugar().calls.listView.verifyField(2, "direction", sugar().calls.getDefaultData().get("direction"));

		VoodooUtils.voodoo.log.info("massUpdate() test complete.");
	}

	@Ignore("Verification for downloaded file is missing i.e External Dependency/NFA")
	@Test
	public void export() throws Exception {
		VoodooUtils.voodoo.log.info("Running export()...");

		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.export();

		VoodooUtils.voodoo.log.info("export() test complete.");
	}

	@Test
	public void deleteRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running deleteRecord()...");

		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.deleteRecord(1);
		sugar().accounts.listView.getControl("confirmDelete").assertVisible(true);
		sugar().alerts.getWarning().confirmAlert();
		sugar().accounts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info("deleteRecord() test complete.");
	}

	@Test
	public void confirmDelete() throws Exception {
		VoodooUtils.voodoo.log.info("Running confirmDelete()...");

		sugar().accounts.api.create();
		sugar().accounts.api.create();
		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.delete();
		sugar().accounts.listView.confirmDelete();
		sugar().accounts.listView.getControl("checkbox03").assertExists(false);

		VoodooUtils.voodoo.log.info("confirmDelete() test complete.");
	}

	@Test
	public void clickRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running clickRecord()...");

		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.getDetailField("name").assertEquals(sugar().accounts.defaultData.get("name"), true);

		VoodooUtils.voodoo.log.info("clickRecord() test complete.");
	}

	@Test
	public void clickClearSelectionsLink() throws Exception {
		VoodooUtils.voodoo.log.info("Running clickClearSelectionsLink()...");

		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.clickClearSelectionsLink();
		Assert.assertTrue("Record is still checked", !sugar().accounts.listView.getControl("checkbox01").isChecked());

		VoodooUtils.voodoo.log.info("clickClearSelectionsLink() test complete.");
	}

	@Test
	public void editAndCancelRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running editAndCancelRecord()...");

		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.editRecord(1);
		sugar().accounts.listView.getEditField(1, "name").set("Edited");
		sugar().accounts.listView.getControl("save01").assertVisible(true);
		sugar().accounts.listView.cancelRecord(1);
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(sugar().accounts.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info("editAndCancelRecord() test complete.");
	}

	@Test
	public void editAndSaveRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running editAndSaveRecord()...");

		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.editRecord(1);
		FieldSet editData = new FieldSet();
		editData.put("name", "Edited");
		sugar().accounts.listView.setEditFields(1, editData);
		sugar().accounts.listView.saveRecord(1);
		sugar().accounts.listView.verifyField(1, "name", editData.get("name"));

		VoodooUtils.voodoo.log.info("editAndSaveRecord() test complete.");
	}

	@Test
	public void selectAllRecords() throws Exception {
		VoodooUtils.voodoo.log.info("Running selectAllRecords()...");

		sugar().accounts.api.create(ds);
		sugar().accounts.navToListView();
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.clickSelectAllRecordsLink();
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.delete();
		sugar().alerts.getWarning().confirmAlert();
		sugar().accounts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info("selectAllRecords() test complete.");
	}

	@Test
	public void checkUncheckRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running checkUncheckRecord()...");

		sugar().accounts.api.create();
		VoodooControl checkbox = sugar().accounts.listView.getControl("checkbox01");
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);
		Assert.assertTrue("Record is not checked", checkbox.isChecked());
		sugar().accounts.listView.uncheckRecord(1);
		Assert.assertTrue("Record is checked", !checkbox.isChecked());

		VoodooUtils.voodoo.log.info("checkUncheckRecord() test complete.");
	}

	@Test
	public void setSearchString() throws Exception {
		VoodooUtils.voodoo.log.info("Running setSearchString()...");

		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.setSearchString("FOO");
		sugar().accounts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info("setSearchString() test complete.");
	}

	@Test
	public void clearSearch() throws Exception {
		VoodooUtils.voodoo.log.info("Running clearSearch()...");

		sugar().leads.api.create();
		sugar().leads.navToListView();
		sugar().leads.listView.setSearchString("Nerd");

		// Verify no data is available
		sugar().leads.listView.assertIsEmpty();

		// Verify clear button existence and its functionality and also verify default record is there after clear search
		sugar().leads.listView.getControl("searchClear").assertExists(true);
		sugar().leads.listView.clearSearch();
		sugar().leads.listView.verifyField(1, "fullName", sugar().leads.getDefaultData().get("fullName"));

		VoodooUtils.voodoo.log.info("clearSearch() test complete.");
	}

	@Test
	public void previewRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running previewRecord()...");

		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.previewRecord(1);
		sugar().previewPane.getControl("close").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("website").assertEquals(sugar().accounts.getDefaultData().get("website"), true);

		VoodooUtils.voodoo.log.info("previewRecord() test complete.");
	}

	@Test
	public void showListViewAndActivityStream() throws Exception {
		VoodooUtils.voodoo.log.info("Running showListViewAndActivityStream()...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.showActivityStream();
		sugar().accounts.listView.activityStream.getControl("submit").assertVisible(true);
		sugar().accounts.listView.showListView();
		sugar().accounts.listView.getControl("createButton").assertVisible(true);

		VoodooUtils.voodoo.log.info("showListViewAndActivityStream() test complete.");
	}

	@Test
	public void showMore() throws Exception {
		VoodooUtils.voodoo.log.info("Running showMore()...");

		sugar().accounts.api.create(ds);
		sugar().accounts.navToListView();
		sugar().accounts.listView.showMore();
		assertTrue("Show More Accounts should not be visible!", !(sugar().accounts.listView.getControl("showMore").queryVisible()));

		VoodooUtils.voodoo.log.info("showMore() test complete.");
	}

	@Test
	public void sortOrder() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrder()...");

		sugar().leads.api.create();
		FieldSet secondLeadData = new FieldSet();
		secondLeadData.put("firstName", "Mazen");
		secondLeadData.put("lastName", "Louis");
		secondLeadData.put("fullName", "Mazen Louis");
		sugar().leads.api.create(secondLeadData);

		sugar().leads.navToListView();
		sugar().leads.listView.sortBy("headerFullname", false);
		sugar().leads.listView.verifyField(1, "fullName", secondLeadData.get("firstName"));
		sugar().leads.listView.verifyField(2, "fullName", sugar().leads.getDefaultData().get("fullName"));

		sugar().leads.listView.sortBy("headerFullname", true);
		sugar().leads.listView.verifyField(1, "fullName", sugar().leads.getDefaultData().get("fullName"));
		sugar().leads.listView.verifyField(2, "fullName", secondLeadData.get("firstName"));

		VoodooUtils.voodoo.log.info("sortOrder() test complete.");
	}

	@Test
	public void toggle() throws Exception {
		VoodooUtils.voodoo.log.info("Running toggle()...");

		FieldSet account1 = new FieldSet();
		account1.put("name", "Account 1");
		sugar().accounts.api.create(account1);

		FieldSet account2 = new FieldSet();
		account2.put("name", "Account 2");
		sugar().accounts.api.create(account2);
		sugar().accounts.navToListView();

		// toggle favorite
		sugar().accounts.listView.toggleFavorite(1);
		sugar().accounts.listView.getControl("favoriteStar01").assertAttribute("aria-pressed", "true", true);
		sugar().accounts.listView.getControl("favoriteStar02").assertAttribute("aria-pressed", "false", true);

		// toggle select all
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.getControl("clearSelectionsLink").assertVisible(true);

		// toggle checkbox
		sugar().accounts.listView.toggleRecordCheckbox(1);
		sugar().accounts.listView.toggleRecordCheckbox(2);
		sugar().accounts.listView.getControl("clearSelectionsLink").assertVisible(false);

		// toggle sidebar
		sugar().accounts.listView.toggleSidebar();
		VoodooControl createDashboard = sugar().accounts.dashboard.getControl("create");
		createDashboard.assertVisible(false);
		sugar().accounts.listView.toggleSidebar();
		createDashboard.assertVisible(true);

		// toggle follow
		sugar().accounts.listView.toggleFollow(1);
		sugar().accounts.listView.clickRecord(1);

		// TODO: VOOD-555
		new VoodooControl("span", "css", ".fld_follow.detail").assertElementContains("Follow", true);

		VoodooUtils.voodoo.log.info("toggle() test complete.");
	}

	@Test
	public void toggleColumnHeader() throws Exception{
		VoodooUtils.voodoo.log.info("Running toggleColumnHeader()...");

		sugar().leads.navToListView();

		// Verify 'fullName' column header is in active state
		VoodooControl fullName = sugar().leads.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("full_name")));
		fullName.assertVisible(true);

		sugar().leads.listView.toggleHeaderColumn("full_name");

		// Verify 'fullName' column header inactive state
		fullName.assertVisible(false);

		sugar().leads.listView.toggleHeaderColumn("full_name"); // always better to reset states

		VoodooUtils.voodoo.log.info("toggleColumnHeader() test complete.");
	}

	@Test
	public void toggleColumnHeaders() throws Exception{
		VoodooUtils.voodoo.log.info("Running toggleColumnHeaders()...");

		ArrayList<String> headers = new ArrayList<String>(Arrays.asList("team_name", "date_end", "status"));
		VoodooControl headerStatus = sugar().meetings.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("status")));
		VoodooControl headerEndDate = sugar().meetings.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("date_end")));
		VoodooControl headerTeam = sugar().meetings.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("team_name")));

		sugar().meetings.navToListView();
		// By default status is active and end date, team name are inactive states
		headerStatus.assertVisible(true);
		headerEndDate.assertVisible(false);
		headerTeam.assertVisible(false);
		sugar().meetings.listView.toggleHeaderColumns(headers);

		// Verify states for name, dateEnd, status header columns after toggling
		headerStatus.assertVisible(false);
		headerEndDate.assertVisible(true);
		headerTeam.assertVisible(true);

		VoodooUtils.voodoo.log.info("toggleColumnHeaders() test complete.");
	}

	@Test
	public void countRows() throws Exception {
		VoodooUtils.voodoo.log.info("Running countRows()...");

		sugar().leads.navToListView();

		// Verify no record count
		int row = sugar().leads.listView.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", row == 0);

		// Verify 1 record count
		sugar().leads.api.create();
		VoodooUtils.refresh(); // to populate data
		sugar().alerts.waitForLoadingExpiration();

		row = sugar().leads.listView.countRows();
		Assert.assertTrue("Number of rows did not equal one.", row == 1);
		sugar().leads.listView.getControl("count").getChildElement("span", "css", " span.count").assertEquals("("+row+")", true);

		// Verify no record after deleting record
		sugar().leads.listView.deleteRecord(1);
		sugar().alerts.getWarning().confirmAlert();
		row = sugar().leads.listView.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", row == 0);

		VoodooUtils.voodoo.log.info("countRows() test complete");
	}

	@Test
	public void selectFilter() throws Exception {
		VoodooUtils.voodoo.log.info("Running selectFilter()...");

		FieldSet account1 = new FieldSet();
		account1.put("name", "Account 1");
		sugar().accounts.api.create(account1);

		FieldSet account2 = new FieldSet();
		account2.put("name", "Account 2");
		account2.put("relAssignedTo", "qauser");
		sugar().accounts.create(account2);

		sugar().accounts.navToListView();
		sugar().accounts.listView.toggleFavorite(1);
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.navToListView();

		// Test selecting of pre-defined Recently Viewed
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterRecentlyViewed();
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.listView.verifyField(1, "name", "Account 2");

		// Test selecting of pre-defined My Favorites filter
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterMyFavorites();
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.listView.verifyField(1, "name", "Account 2");

		// Test selecting of pre-defined Assigned to me filter
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterAssignedToMe();
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.listView.verifyField(1, "name", "Account 1");

		// Test selecting of pre-defined All records
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterAll();
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.listView.verifyField(1, "name", "Account 2");
		sugar().accounts.listView.verifyField(2, "name", "Account 1");

		/*
		 	Test for recently created filter not possible at this time.
			Recently created filter returns all records created in the last couple-few hours
			if we wanted to test this properly we would need to either be able to make a record appear to
			have been created many hours ago via API or make System Time adjustments to mimic a future time.
		 */

		VoodooUtils.voodoo.log.info("selectFilter() test complete.");
	}

	@Test
	public void createAndSelectCustomFilter() throws Exception {
		VoodooUtils.voodoo.log.info("Running createAndSelectCustomFilter()...");

		sugar().accounts.api.create();

		FieldSet accountName = new FieldSet();
		accountName.put("name", "This is an account");
		accountName.put("sicCode", "APL");
		AccountRecord myAccount = (AccountRecord)sugar().accounts.api.create(accountName);
		FieldSet filterData = new FieldSet();
		filterData.put("name", "Name,exactly matches,This is an account,1");
		filterData.put("billingAddressCountry", "Country,exactly matches,USA,2");
		filterData.put("sicCode", "SIC Code,exactly matches,APL,3");
		filterData.put("filterName", "New Filter");
		sugar().accounts.navToListView();
		sugar().accounts.listView.createFilter(filterData);
		sugar().accounts.listView.verifyField(1, "name", myAccount.get("name"));

		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterAll();
		sugar().accounts.listView.verifyField(2, "name", sugar().accounts.getDefaultData().get("name"));

		sugar().accounts.listView.selectFilter(filterData.get("filterName"));
		sugar().accounts.listView.getControl("checkbox02").assertExists(false);

		VoodooUtils.voodoo.log.info("createAndSelectCustomFilter() complete.");
	}

	public void cleanup() throws Exception {}
}