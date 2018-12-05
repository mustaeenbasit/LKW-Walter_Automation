package com.sugarcrm.test.grimoire;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ContactsModuleTests extends SugarTest{
	ContactRecord myContact;

	public void setup() throws Exception {
		myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();
		sugar().contacts.navToListView();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().contacts);

		// Verify menu items
		sugar().contacts.menu.getControl("createContact").assertVisible(true);
		sugar().contacts.menu.getControl("createContactFromVcard").assertVisible(true);
		sugar().contacts.menu.getControl("viewContacts").assertVisible(true);
		sugar().contacts.menu.getControl("viewContactReports").assertVisible(true);
		sugar().contacts.menu.getControl("importContacts").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().contacts); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		// Verify all sort headers in listview
		for(String header : sugar().contacts.listView.getHeaders()) {
			sugar().contacts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() complete.");
	}

	@Test
	public void sortOrderByName() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByName()...");

		// 2 contacts having 1 with default data and another with custom data
		FieldSet data = new FieldSet();
		data.put("firstName", "AJ");
		data.put("lastName", "Nerd");
		data.put("fullName", data.get("firstName") + " " + data.get("lastName"));
		sugar().contacts.api.create(data);
		sugar().contacts.navToListView(); // reload data

		// Verify records after sort by 'name ' in descending and ascending order
		sugar().contacts.listView.sortBy("headerFullname", false);
		sugar().contacts.listView.verifyField(1, "fullName", myContact.getRecordIdentifier());
		sugar().contacts.listView.verifyField(2, "fullName", data.get("fullName"));

		sugar().contacts.listView.sortBy("headerFullname", true);
		sugar().contacts.listView.verifyField(1, "fullName", data.get("fullName"));
		sugar().contacts.listView.verifyField(2, "fullName", myContact.getRecordIdentifier());

		VoodooUtils.voodoo.log.info("sortOrderByName() complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().contacts.listView.clickRecord(1);

		// Verify subpanels
		sugar().contacts.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertVisible(true);
		sugar().contacts.recordView.subpanels.get(sugar().meetings.moduleNamePlural).assertVisible(true);
		sugar().contacts.recordView.subpanels.get(sugar().tasks.moduleNamePlural).assertVisible(true);
		sugar().contacts.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);
		sugar().contacts.recordView.subpanels.get(sugar().leads.moduleNamePlural).assertVisible(true);
		sugar().contacts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural).assertVisible(true);
		sugar().contacts.recordView.subpanels.get(sugar().emails.moduleNamePlural).assertVisible(true);
		sugar().contacts.recordView.subpanels.get(sugar().cases.moduleNamePlural).assertVisible(true);
		sugar().contacts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).assertVisible(true);
		sugar().contacts.recordView.subpanels.get(sugar().documents.moduleNamePlural).assertVisible(true);

		// TODO: VOOD-2019 Quotes Bill to and Ship to subpanels
		new VoodooControl("div", "css", "[data-subpanel-link='quotes']").assertVisible(true);
		new VoodooControl("div", "css", "[data-subpanel-link='billing_quotes']").assertVisible(true);

		// TODO: VOOD-1499
		//sugar().contacts.recordView.subpanels.get(sugar().campaigns.moduleNamePlural).assertVisible(true);		

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		// preview record
		sugar().contacts.listView.previewRecord(1);
		sugar().previewPane.showMore();

		// Verify preview field values
		// full name 
		sugar().previewPane.getPreviewPaneField("fullName").assertContains(myContact.get("fullName"), true);

		// title
		sugar().previewPane.getPreviewPaneField("title").assertEquals(myContact.get("title"), true);

		// mobile
		sugar().previewPane.getPreviewPaneField("phoneMobile").assertEquals(myContact.get("phoneMobile"), true);

		// department
		sugar().previewPane.getPreviewPaneField("department").assertEquals(myContact.get("department"), true);

		// donot call
		sugar().previewPane.getPreviewPaneField("checkDoNotCall").assertVisible(true);

		// account name
		sugar().previewPane.getPreviewPaneField("relAccountName").assertVisible(true);

		// email address
		sugar().previewPane.getPreviewPaneField("emailAddress").assertVisible(true);

		// tag
		sugar().previewPane.getPreviewPaneField("tags").assertVisible(true);

		// primary address
		sugar().previewPane.getPreviewPaneField("primaryAddressStreet").assertContains(myContact.get("primaryAddressStreet"), true);

		// primary city
		sugar().previewPane.getPreviewPaneField("primaryAddressCity").assertContains(myContact.get("primaryAddressCity"), true);

		// primary state
		sugar().previewPane.getPreviewPaneField("primaryAddressState").assertContains(myContact.get("primaryAddressState"), true);

		// primary postal code
		sugar().previewPane.getPreviewPaneField("primaryAddressPostalCode").assertContains(myContact.get("primaryAddressPostalCode"), true);

		// primary country
		sugar().previewPane.getPreviewPaneField("primaryAddressCountry").assertContains(myContact.get("primaryAddressCountry"), true);

		// phone work
		sugar().previewPane.getPreviewPaneField("phoneWork").assertEquals(myContact.get("phoneWork"), true);

		// phone fax
		sugar().previewPane.getPreviewPaneField("phoneFax").assertEquals(myContact.get("phoneFax"), true);

		// description
		sugar().previewPane.getPreviewPaneField("description").assertVisible(true);

		// reportsTo
		sugar().previewPane.getPreviewPaneField("reportsTo").assertVisible(true);

		// lead Source
		sugar().previewPane.getPreviewPaneField("leadSource").assertEquals(myContact.get("leadSource"), true);

		// assigned to
		sugar().previewPane.getPreviewPaneField("relAssignedTo").assertEquals("Administrator", true);

		// language preferred
		sugar().previewPane.getPreviewPaneField("preferred_language").assertEquals(myContact.get("preferred_language"), true);

		// date created
		sugar().previewPane.getPreviewPaneField("date_entered_date").assertVisible(true);

		// created by
		sugar().previewPane.getPreviewPaneField("dateEnteredBy").assertVisible(true);

		// teams
		sugar().previewPane.getPreviewPaneField("relTeam").assertVisible(true);

		// date modified
		sugar().previewPane.getPreviewPaneField("date_modified_date").assertVisible(true);

		// modified by
		sugar().previewPane.getPreviewPaneField("dateModifiedBy").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		// TODO: VOOD-444 Create dependencies for account name, Once resolved it should create via API
		AccountRecord myAcc = (AccountRecord)sugar().accounts.api.create();
		sugar().contacts.listView.editRecord(1);

		// salutation
		sugar().contacts.listView.getEditField(1, "salutation").assertEquals(myContact.get("salutation"), true);

		// first name
		sugar().contacts.listView.getEditField(1, "firstName").assertEquals(myContact.get("firstName"), true);

		// last name
		sugar().contacts.listView.getEditField(1, "lastName").assertEquals(myContact.get("lastName"), true);

		// title
		sugar().contacts.listView.getEditField(1, "title").assertEquals(myContact.get("title"), true);

		// account name
		VoodooControl accountName = sugar().contacts.listView.getEditField(1, "relAccountName");
		accountName.set(myAcc.getRecordIdentifier());
		accountName.assertEquals(myAcc.getRecordIdentifier(), true);
		sugar().alerts.getWarning().cancelAlert();

		// office phone
		sugar().contacts.listView.getEditField(1, "phoneWork").assertEquals(myContact.get("phoneWork"), true);

		// email Address
		sugar().contacts.listView.getEditField(1, "emailAddress").assertVisible(true);

		// user
		sugar().contacts.listView.getEditField(1, "relAssignedTo").assertVisible(true);

		// date created (read-only)
		VoodooControl dateCreated = sugar().contacts.listView.getEditField(1, "date_entered_date");
		dateCreated.scrollIntoViewIfNeeded(false);
		dateCreated.assertVisible(true);
		dateCreated.assertAttribute("class", "edit", false);

		// date modified (read-only)
		VoodooControl dateModified = sugar().contacts.listView.getEditField(1, "date_modified_date");
		dateModified.assertVisible(true);
		dateModified.assertAttribute("class", "edit", false);

		// save record
		sugar().contacts.listView.saveRecord(1);

		// verify detail fields
		// name
		sugar().contacts.listView.getDetailField(1, "fullName").assertEquals(myContact.get("fullName"), true);

		// status
		sugar().contacts.listView.getDetailField(1, "title").assertEquals(myContact.get("title"), true);

		// account name
		sugar().contacts.listView.getDetailField(1, "relAccountName").assertEquals(myAcc.getRecordIdentifier(), true);

		// phone
		sugar().contacts.listView.getDetailField(1, "phoneWork").assertEquals(myContact.get("phoneWork"), true);

		// user
		sugar().contacts.listView.getDetailField(1, "relAssignedTo").assertVisible(true);

		// email Address
		sugar().contacts.listView.getDetailField(1, "emailAddress").assertExists(true);

		// Date Created
		sugar().contacts.listView.getDetailField(1, "date_entered_date").assertVisible(true);

		// Date modified
		sugar().contacts.listView.getDetailField(1, "date_modified_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		// TODO: VOOD-444 Create dependencies for account name and email address field, Once resolved it should create via API
		AccountRecord myAcc = (AccountRecord)sugar().accounts.api.create();
		String emailAddress = "ajabble@sugarcrm.com";
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();

		// salutation
		sugar().contacts.recordView.getEditField("salutation").assertEquals(myContact.get("salutation"), true);

		// first name
		sugar().contacts.recordView.getEditField("firstName").assertEquals(myContact.get("firstName"), true);

		// last name
		sugar().contacts.recordView.getEditField("lastName").assertEquals(myContact.get("lastName"), true);

		// title
		sugar().contacts.recordView.getEditField("title").assertEquals(myContact.get("title"), true);

		// mobile
		sugar().contacts.recordView.getEditField("phoneMobile").assertEquals(myContact.get("phoneMobile"), true);

		// account name
		VoodooControl accountName = sugar().contacts.recordView.getEditField("relAccountName");
		accountName.set(myAcc.getRecordIdentifier());
		accountName.assertEquals(myAcc.getRecordIdentifier(), true);
		sugar().alerts.getWarning().cancelAlert();

		// email
		VoodooControl email = sugar().contacts.recordView.getEditField("emailAddress");
		email.set(emailAddress);
		email.assertEquals(emailAddress, true);

		// tags
		sugar().contacts.recordView.getEditField("tags").assertVisible(true);

		// primary address street, city, zip, country, state
		sugar().contacts.recordView.getEditField("primaryAddressStreet").assertEquals(myContact.get("primaryAddressStreet"), true);
		sugar().contacts.recordView.getEditField("primaryAddressCity").assertEquals(myContact.get("primaryAddressCity"), true);
		sugar().contacts.recordView.getEditField("primaryAddressState").assertEquals(myContact.get("primaryAddressState"), true);
		sugar().contacts.recordView.getEditField("primaryAddressPostalCode").assertEquals(myContact.get("primaryAddressPostalCode"), true);
		sugar().contacts.recordView.getEditField("primaryAddressCountry").assertEquals(myContact.get("primaryAddressCountry"), true);

		// department
		sugar().contacts.recordView.getEditField("department").assertEquals(myContact.get("department"), true);

		// office phone
		sugar().contacts.recordView.getEditField("phoneWork").assertEquals(myContact.get("phoneWork"), true);

		// fax
		sugar().contacts.recordView.getEditField("phoneFax").assertEquals(myContact.get("phoneFax"), true);

		// description
		sugar().contacts.recordView.getEditField("description").assertVisible(true);

		// lead source
		sugar().contacts.recordView.getEditField("leadSource").assertEquals(myContact.get("leadSource"), true);

		// assigned to
		sugar().contacts.recordView.getEditField("relAssignedTo").assertEquals("Administrator", true);

		// date created (read only)
		VoodooControl dateCreated = sugar().contacts.recordView.getEditField("date_entered_date");
		dateCreated.assertVisible(true);
		dateCreated.assertAttribute("class", "edit", false);

		// created By
		sugar().contacts.recordView.getEditField("dateEnteredBy").assertVisible(true);

		// date modified (read only)
		VoodooControl dateModified = sugar().contacts.recordView.getEditField("date_modified_date");
		dateModified.assertVisible(true);
		dateModified.assertAttribute("class", "edit", false);

		// modified by
		sugar().contacts.recordView.getEditField("dateModifiedBy").assertVisible(true);

		// teams
		sugar().contacts.recordView.getEditField("relTeam").assertEquals("Global", true);

		// save the record 
		sugar().contacts.recordView.save();

		// verify detail view fields
		// full name
		sugar().contacts.recordView.getDetailField("fullName").assertEquals(myContact.get("fullName"), true);

		// title
		sugar().contacts.recordView.getDetailField("title").assertEquals(myContact.get("title"), true);

		// mobile
		sugar().contacts.recordView.getDetailField("phoneMobile").assertEquals(myContact.get("phoneMobile"), true);

		// account name
		sugar().contacts.recordView.getDetailField("relAccountName").assertEquals(myAcc.getRecordIdentifier(), true);

		// email
		sugar().contacts.recordView.getDetailField("emailAddress").assertEquals(emailAddress, true);

		// tags
		sugar().contacts.recordView.getDetailField("tags").assertVisible(true);

		// primary address street, city, zip, country, state
		sugar().contacts.recordView.getDetailField("primaryAddressStreet").assertEquals(myContact.get("primaryAddressStreet"), true);
		sugar().contacts.recordView.getDetailField("primaryAddressCity").assertEquals(myContact.get("primaryAddressCity"), true);
		sugar().contacts.recordView.getDetailField("primaryAddressState").assertEquals(myContact.get("primaryAddressState"), true);
		sugar().contacts.recordView.getDetailField("primaryAddressPostalCode").assertEquals(myContact.get("primaryAddressPostalCode"), true);
		sugar().contacts.recordView.getDetailField("primaryAddressCountry").assertEquals(myContact.get("primaryAddressCountry"), true);

		// department
		sugar().contacts.recordView.getDetailField("department").assertEquals(myContact.get("department"), true);

		// office phone
		sugar().contacts.recordView.getDetailField("phoneWork").assertEquals(myContact.get("phoneWork"), true);

		// fax
		sugar().contacts.recordView.getDetailField("phoneFax").assertEquals(myContact.get("phoneFax"), true);

		// description
		sugar().contacts.recordView.getDetailField("description").assertVisible(true);

		// lead source
		sugar().contacts.recordView.getDetailField("leadSource").assertEquals(myContact.get("leadSource"), true);

		// assigned to
		sugar().contacts.recordView.getDetailField("relAssignedTo").assertEquals("Administrator", true);

		// date created
		sugar().contacts.recordView.getDetailField("date_entered_date").assertVisible(true);

		// created By
		sugar().contacts.recordView.getDetailField("dateEnteredBy").assertVisible(true);

		// date modified
		sugar().contacts.recordView.getDetailField("date_modified_date").assertVisible(true);

		// modified by
		sugar().contacts.recordView.getDetailField("dateModifiedBy").assertVisible(true);

		// teams
		sugar().contacts.recordView.getDetailField("relTeam").assertEquals("Global (Primary)", true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void verifyQuotaSubpanel() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyQuotaSubpanel()...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		sugar().contacts.recordView.subpanels.get("Quotes (Bill To)").assertVisible(true);
		sugar().contacts.recordView.subpanels.get("Quotes (Ship To)").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyQuotaSubpanel() complete.");
	}

	public void cleanup() throws Exception {}
}