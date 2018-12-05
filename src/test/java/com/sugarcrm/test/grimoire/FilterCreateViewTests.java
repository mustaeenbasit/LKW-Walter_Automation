package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class FilterCreateViewTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyElements() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElements()...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();
		sugar().accounts.listView.filterCreate.getControl("removeFilterRow01").assertVisible(true);
		sugar().accounts.listView.filterCreate.getControl("addFilterRow01").assertVisible(true);
		sugar().accounts.listView.filterCreate.getControl("filterName").assertVisible(true);
		sugar().accounts.listView.filterCreate.getControl("saveButton").assertVisible(true);
		sugar().accounts.listView.filterCreate.getControl("cancelButton").assertVisible(true);
		sugar().accounts.listView.filterCreate.getControl("resetButton").assertVisible(true);
		sugar().accounts.listView.filterCreate.getControl("deleteButton").assertExists(true);
		sugar().accounts.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info("verifyElements() completed.");
	}

	@Test
	public void verifySearchFilterAddRemoveRow() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySearchFilterAddRemoveRow()...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();
		sugar().accounts.listView.filterCreate.clickAddRow(1);
		sugar().accounts.listView.filterCreate.getControl("removeFilterRow02").assertVisible(true);
		sugar().accounts.listView.filterCreate.clickAddRow(2);
		sugar().accounts.listView.filterCreate.getControl("removeFilterRow03").assertVisible(true);
		sugar().accounts.listView.filterCreate.clickRemoveRow(2);
		sugar().accounts.listView.filterCreate.getControl("addFilterRow03").assertVisible(false);
		sugar().accounts.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info("verifySearchFilterAddRemoveRow() completed.");
	}

	@Test
	public void verifyOppFilter() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyOppFilter()...");

		FieldSet oppData = new FieldSet();
		oppData.put("name", "Second Opp");
		sugar().opportunities.api.create(oppData);
		sugar().opportunities.api.create();
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.openFilterDropdown();
		sugar().opportunities.listView.selectFilterCreateNew();
		sugar().opportunities.listView.filterCreate.setFilterFields("name", "Opportunity Name", "starts with", oppData.get("name"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().opportunities.listView.verifyField(1, "name", oppData.get("name"));

		VoodooUtils.voodoo.log.info("verifyOppFilter() completed.");
	}

	@Test
	public void verifyContactsFilter() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyContactsFilter()...");

		sugar().contacts.api.create();
		sugar().accounts.api.create();
		sugar().contacts.navToListView();
		sugar().contacts.listView.editRecord(1);
		sugar().contacts.listView.getEditField(1, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.listView.saveRecord(1);
		sugar().contacts.listView.openFilterDropdown();
		sugar().contacts.listView.selectFilterCreateNew();

		sugar().contacts.listView.filterCreate.setFilterFields("firstName", "First Name", "starts with", sugar().contacts.getDefaultData().get("firstName"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().contacts.listView.verifyField(1, "fullName", sugar().contacts.getDefaultData().get("fullName"));

		sugar().contacts.listView.filterCreate.setFilterFields("lastName", "Last Name", "starts with", sugar().contacts.getDefaultData().get("lastName"), 1);
		sugar().contacts.listView.verifyField(1, "fullName", sugar().contacts.getDefaultData().get("lastName"));

		sugar().contacts.listView.filterCreate.setFilterFields("relAccountName", "Account Name", "is any of", sugar().accounts.getDefaultData().get("name"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().contacts.listView.verifyField(1, "relAccountName", sugar().accounts.getDefaultData().get("name"));

		sugar().contacts.listView.filterCreate.setFilterFields("leadSource", "Lead Source", "is any of", sugar().contacts.getDefaultData().get("leadSource"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().contacts.listView.verifyField(1, "fullName", sugar().contacts.getDefaultData().get("fullName"));

		sugar().contacts.listView.filterCreate.setFilterFields("date_entered_date", "Date Created", "is equal to", "10/10/2025", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().contacts.listView.assertIsEmpty();

		sugar().contacts.listView.filterCreate.setFilterFields("title", "Title", "starts with", sugar().contacts.getDefaultData().get("title"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().contacts.listView.verifyField(1, "fullName", sugar().contacts.getDefaultData().get("fullName"));

		sugar().contacts.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info("verifyContactsFilter() completed.");
	}

	@Test
	public void verifyLeadsFilter() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyLeadsFilter()...");

		sugar().leads.api.create();
		sugar().leads.navToListView();
		sugar().leads.listView.openFilterDropdown();
		sugar().leads.listView.selectFilterCreateNew();

		sugar().leads.listView.filterCreate.setFilterFields("firstName", "First Name", "starts with", sugar().leads.getDefaultData().get("firstName"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().leads.listView.verifyField(1, "fullName", sugar().leads.getDefaultData().get("fullName"));

		sugar().leads.listView.filterCreate.setFilterFields("lastName", "Last Name", "starts with", sugar().leads.getDefaultData().get("lastName"), 1);
		sugar().leads.listView.verifyField(1, "fullName", sugar().leads.getDefaultData().get("lastName"));

		sugar().leads.listView.filterCreate.setFilterFields("leadSource", "Lead Source", "is any of", sugar().leads.getDefaultData().get("leadSource"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().leads.listView.verifyField(1, "fullName", sugar().leads.getDefaultData().get("fullName"));

		sugar().leads.listView.filterCreate.setFilterFields("phoneWork", "Phone", "starts with", sugar().leads.getDefaultData().get("phoneWork"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().leads.listView.verifyField(1, "phoneWork", sugar().leads.getDefaultData().get("phoneWork"));

		sugar().leads.listView.filterCreate.setFilterFields("status", "Status", "is any of", sugar().leads.getDefaultData().get("status"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().leads.listView.verifyField(1, "status", sugar().leads.getDefaultData().get("status"));

		sugar().leads.listView.filterCreate.setFilterFields("primaryAddressStreet", "Street", "starts with", "1 Google", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().leads.listView.verifyField(1, "fullName", sugar().leads.getDefaultData().get("fullName"));

		sugar().leads.listView.filterCreate.setFilterFields("primaryAddressCity", "City", "exactly matches", sugar().leads.getDefaultData().get("primaryAddressCity"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().leads.listView.verifyField(1, "fullName", sugar().leads.getDefaultData().get("fullName"));

		sugar().leads.listView.filterCreate.setFilterFields("primaryAddressState", "State", "exactly matches", sugar().leads.getDefaultData().get("primaryAddressState"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().leads.listView.verifyField(1, "fullName", sugar().leads.getDefaultData().get("fullName"));

		sugar().leads.listView.filterCreate.setFilterFields("primaryAddressPostalCode", "Postal Code", "exactly matches", "110024", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().leads.listView.assertIsEmpty();

		sugar().leads.listView.filterCreate.setFilterFields("primaryAddressCountry", "Country", "exactly matches", sugar().leads.getDefaultData().get("primaryAddressCountry"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().leads.listView.verifyField(1, "fullName", sugar().leads.getDefaultData().get("fullName"));

		sugar().leads.listView.filterCreate.setFilterFields("date_entered_date", "Date Created", "is equal to", "10/10/2025", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().leads.listView.assertIsEmpty();

		sugar().leads.listView.filterCreate.setFilterFields("date_modified_date", "Date Modified", "is equal to", "10/10/2025", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().leads.listView.assertIsEmpty();

		sugar().leads.listView.filterCreate.setFilterFields("relAssignedTo", "Assigned to", "is any of", sugar().leads.getDefaultData().get("relAssignedTo"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().leads.listView.verifyField(1, "relAssignedTo", sugar().leads.getDefaultData().get("relAssignedTo"));

		sugar().leads.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info("verifyLeadsFilter() completed.");
	}

	@Test
	public void verifyNotesFilter() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyNotesFilter()...");

		sugar().notes.api.create();
		sugar().contacts.api.create();
		sugar().accounts.api.create();
		sugar().tags.api.create();
		sugar().notes.navToListView();
		sugar().notes.listView.openFilterDropdown();
		sugar().notes.listView.selectFilterCreateNew();

		sugar().notes.listView.filterCreate.setFilterFields("subject", "Subject", "exactly matches", sugar().notes.getDefaultData().get("subject"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().notes.listView.verifyField(1, "subject", sugar().notes.getDefaultData().get("subject"));

		// TODO: VOOD-444 
		sugar().notes.listView.editRecord(1);
		sugar().notes.listView.getEditField(1, "contact").set(sugar().contacts.getDefaultData().get("lastName"));
		sugar().notes.listView.getEditField(1, "relRelatedToModule").set(sugar().accounts.moduleNameSingular);
		sugar().notes.listView.getEditField(1, "relRelatedToValue").set(sugar().accounts.getDefaultData().get("name"));
		sugar().notes.listView.saveRecord(1);

		sugar().notes.listView.filterCreate.setFilterFields("contact", "Contact", "is any of", sugar().contacts.getDefaultData().get("lastName"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().notes.listView.verifyField(1, "contact", sugar().contacts.getDefaultData().get("lastName"));

		// TODO: VOOD-1488 
		/*sugar().notes.listView.filterCreate.setFilterFields("relRelatedToValue", "Related To", "Account", sugar().accounts.getDefaultData().get("name"), 1);
			sugar().alerts.waitForLoadingExpiration();
			sugar().notes.listView.verifyField(1, "relRelatedToValue", sugar().accounts.getDefaultData().get("name"));*/

		sugar().notes.listView.filterCreate.setFilterFields("date_entered_date", "Date Created", "is equal to", "10/10/2025", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().notes.listView.assertIsEmpty();

		sugar().notes.listView.filterCreate.setFilterFields("date_modified_date", "Date Modified", "is equal to", "10/10/2025", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().notes.listView.assertIsEmpty();

		sugar().notes.listView.filterCreate.setFilterFields("tags", "Tags", "is any of", "Tag", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().notes.listView.assertIsEmpty();

		sugar().notes.listView.filterCreate.cancel();
		
		VoodooUtils.voodoo.log.info("verifyNotesFilter() completed.");
	}
	
	@Test
	public void verifyTasksFilter() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyTasksFilter()...");

		sugar().tasks.api.create();
		sugar().contacts.api.create();
		sugar().accounts.api.create();
		sugar().tasks.navToListView();
		sugar().tasks.listView.openFilterDropdown();
		sugar().tasks.listView.selectFilterCreateNew();

		sugar().tasks.listView.filterCreate.setFilterFields("subject", "Subject", "exactly matches", sugar().tasks.getDefaultData().get("subject"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.listView.verifyField(1, "subject", sugar().tasks.getDefaultData().get("subject"));

		// TODO: VOOD-444
		sugar().tasks.listView.editRecord(1);
		sugar().tasks.listView.getEditField(1, "contactName").set(sugar().contacts.getDefaultData().get("lastName"));
		sugar().tasks.listView.getEditField(1, "relRelatedToParentType").set(sugar().accounts.moduleNameSingular);
		sugar().tasks.listView.getEditField(1, "relRelatedToParent").set(sugar().accounts.getDefaultData().get("name"));
		sugar().tasks.listView.saveRecord(1);

		sugar().tasks.listView.filterCreate.setFilterFields("contactName", "Contact Name", "is any of", sugar().contacts.getDefaultData().get("lastName"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.listView.verifyField(1, "contactName", sugar().contacts.getDefaultData().get("lastName"));

		// TODO: VOOD-1488
		/*sugar().tasks.listView.filterCreate.setFilterFields("relRelatedToParent", "Related To", "Account", sugar().accounts.getDefaultData().get("name"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.listView.verifyField(1, "relRelatedToParent", sugar().accounts.getDefaultData().get("name"));*/

		sugar().tasks.listView.filterCreate.setFilterFields("status", "Status", "is any of", "In Progress", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.listView.assertIsEmpty();

		sugar().tasks.listView.filterCreate.setFilterFields("date_start_date", "Start Date", "is equal to", "10/10/2025", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.listView.assertIsEmpty();

		sugar().tasks.listView.filterCreate.setFilterFields("date_due_date", "Due Date", "is equal to", "10/10/2025", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().tasks.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info("verifyTasksFilter() completed.");
	}

	public void cleanup() throws Exception {}
}