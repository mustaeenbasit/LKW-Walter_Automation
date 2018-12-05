package com.sugarcrm.test.grimoire;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class LeadsModuleTests extends SugarTest{
	LeadRecord myLead;

	public void setup() throws Exception {
		myLead = (LeadRecord)sugar().leads.api.create();
		sugar().login();
		sugar().leads.navToListView();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().leads);

		// Verify menu items
		sugar().leads.menu.getControl("createLead").assertVisible(true);
		sugar().leads.menu.getControl("createLeadFromVcard").assertVisible(true);
		sugar().leads.menu.getControl("viewLeads").assertVisible(true);
		sugar().leads.menu.getControl("viewLeadReports").assertVisible(true);
		sugar().leads.menu.getControl("importLeads").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().leads); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		// Verify all sort headers in listview
		for(String header : sugar().leads.listView.getHeaders()) {
			sugar().leads.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() complete.");
	}

	@Test
	public void sortOrderByName() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByName()...");

		// 2 leads having 1 with default data and another with custom data
		FieldSet data = new FieldSet();
		data.put("firstName", "AJ");
		data.put("lastName", "Nerd");
		data.put("fullName", data.get("firstName") + " " + data.get("lastName"));
		sugar().leads.api.create(data);
		sugar().leads.navToListView(); // reload data

		// Verify records after sort by 'name ' in descending and ascending order
		sugar().leads.listView.sortBy("headerFullname", false);
		sugar().leads.listView.verifyField(1, "fullName", data.get("fullName"));
		sugar().leads.listView.verifyField(2, "fullName", myLead.getRecordIdentifier());

		sugar().leads.listView.sortBy("headerFullname", true);
		sugar().leads.listView.verifyField(1, "fullName", myLead.getRecordIdentifier());
		sugar().leads.listView.verifyField(2, "fullName", data.get("fullName"));

		VoodooUtils.voodoo.log.info("sortOrderByName() complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().leads.listView.clickRecord(1);

		// Verify subpanels
		sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertVisible(true);
		sugar().leads.recordView.subpanels.get(sugar().meetings.moduleNamePlural).assertVisible(true);
		sugar().leads.recordView.subpanels.get(sugar().tasks.moduleNamePlural).assertVisible(true);
		sugar().leads.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);
		sugar().leads.recordView.subpanels.get(sugar().emails.moduleNamePlural).assertVisible(true);

		// TODO: VOOD-1499
		//sugar().leads.recordView.subpanels.get(sugar().campaigns.moduleNamePlural).assertVisible(true);		

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		// preview record
		sugar().leads.listView.previewRecord(1);
		sugar().previewPane.showMore();

		// Verify preview field values
		// full name 
		sugar().previewPane.getPreviewPaneField("fullName").assertContains(myLead.get("fullName"), true);

		// title
		sugar().previewPane.getPreviewPaneField("title").assertEquals(myLead.get("title"), true);

		// mobile
		sugar().previewPane.getPreviewPaneField("phoneMobile").assertEquals(myLead.get("phoneMobile"), true);

		// website
		sugar().previewPane.getPreviewPaneField("website").assertEquals(myLead.get("website"), true);

		// account name
		sugar().previewPane.getPreviewPaneField("accountName").assertVisible(true);

		// email address
		sugar().previewPane.getPreviewPaneField("emailAddress").assertVisible(true);

		// tag
		sugar().previewPane.getPreviewPaneField("tags").assertVisible(true);

		// primary address
		sugar().previewPane.getPreviewPaneField("primaryAddressStreet").assertContains(myLead.get("primaryAddressStreet"), true);

		// primary city
		sugar().previewPane.getPreviewPaneField("primaryAddressCity").assertContains(myLead.get("primaryAddressCity"), true);

		// primary state
		sugar().previewPane.getPreviewPaneField("primaryAddressState").assertContains(myLead.get("primaryAddressState"), true);

		// primary postal code
		sugar().previewPane.getPreviewPaneField("primaryAddressPostalCode").assertContains(myLead.get("primaryAddressPostalCode"), true);

		// primary country
		sugar().previewPane.getPreviewPaneField("primaryAddressCountry").assertContains(myLead.get("primaryAddressCountry"), true);

		// department
		sugar().previewPane.getPreviewPaneField("department").assertEquals(myLead.get("department"), true);

		// phone work
		sugar().previewPane.getPreviewPaneField("phoneWork").assertEquals(myLead.get("phoneWork"), true);

		// phone fax
		sugar().previewPane.getPreviewPaneField("phoneFax").assertEquals(myLead.get("phoneFax"), true);

		// description
		sugar().previewPane.getPreviewPaneField("description").assertEquals(myLead.get("description"), true);

		// status
		sugar().previewPane.getPreviewPaneField("status").assertEquals(myLead.get("status"), true);

		// status Description
		sugar().previewPane.getPreviewPaneField("statusDescription").assertEquals(myLead.get("statusDescription"), true);

		// lead Source
		sugar().previewPane.getPreviewPaneField("leadSource").assertEquals(myLead.get("leadSource"), true);

		// lead Source Description
		sugar().previewPane.getPreviewPaneField("leadSourceDescription").assertEquals(myLead.get("leadSourceDescription"), true);

		// description
		sugar().previewPane.getPreviewPaneField("description").assertEquals(myLead.get("description"), true);

		// assigned to
		sugar().previewPane.getPreviewPaneField("relAssignedTo").assertEquals("Administrator", true);	

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

		sugar().leads.listView.editRecord(1);

		// salutation
		sugar().leads.listView.getEditField(1, "salutation").assertEquals(myLead.get("salutation"), true);

		// first name
		sugar().leads.listView.getEditField(1, "firstName").assertEquals(myLead.get("firstName"), true);

		// last name
		sugar().leads.listView.getEditField(1, "lastName").assertEquals(myLead.get("lastName"), true);

		// status
		sugar().leads.listView.getEditField(1, "status").assertEquals(myLead.get("status"), true);

		// account name
		sugar().leads.listView.getEditField(1, "accountName").assertVisible(true);

		// office phone
		sugar().leads.listView.getEditField(1, "phoneWork").assertEquals(myLead.get("phoneWork"), true);

		// email Address
		sugar().leads.listView.getEditField(1, "emailAddress").assertVisible(true);

		// user
		sugar().leads.listView.getEditField(1, "relAssignedTo").assertVisible(true);

		// date created (read-only)
		VoodooControl dateCreated = sugar().leads.listView.getEditField(1, "date_entered_date");
		dateCreated.scrollIntoViewIfNeeded(false);
		dateCreated.assertVisible(true);
		dateCreated.assertAttribute("class", "edit", false);

		// date modified (read-only)
		VoodooControl dateModified = sugar().leads.listView.getEditField(1, "date_modified_date");
		dateModified.assertVisible(true);
		dateModified.assertAttribute("class", "edit", false);

		// cancel record
		sugar().leads.listView.cancelRecord(1);

		// verify detail fields
		// name
		sugar().leads.listView.getDetailField(1, "fullName").assertEquals(myLead.get("fullName"), true);

		// status
		sugar().leads.listView.getDetailField(1, "status").assertEquals(myLead.get("status"), true);

		// account name
		sugar().leads.listView.getDetailField(1, "accountName").assertExists(true);

		// phone
		sugar().leads.listView.getDetailField(1, "phoneWork").assertEquals(myLead.get("phoneWork"), true);

		// user
		sugar().leads.listView.getDetailField(1, "relAssignedTo").assertVisible(true);

		// email Address
		sugar().leads.listView.getDetailField(1, "emailAddress").assertExists(true);

		// Date Created
		sugar().leads.listView.getDetailField(1, "date_entered_date").assertVisible(true);

		// Date modified
		sugar().leads.listView.getDetailField(1, "date_modified_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		// TODO: VOOD-444 Create dependencies for account name and email address fields, Once resolved it should create via API
		String emailAddress = "ajabble@sugarcrm.com";
		String accountName = sugar().accounts.getDefaultData().get("name");
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.edit();
		sugar().leads.recordView.showMore();

		// salutation
		sugar().leads.recordView.getEditField("salutation").assertEquals(myLead.get("salutation"), true);

		// first name
		sugar().leads.recordView.getEditField("firstName").assertEquals(myLead.get("firstName"), true);

		// last name
		sugar().leads.recordView.getEditField("lastName").assertEquals(myLead.get("lastName"), true);

		// title
		sugar().leads.recordView.getEditField("title").assertEquals(myLead.get("title"), true);

		// mobile
		sugar().leads.recordView.getEditField("phoneMobile").assertEquals(myLead.get("phoneMobile"), true);

		// website
		sugar().leads.recordView.getEditField("website").assertEquals(myLead.get("website"), true);

		// account name
		VoodooControl account = sugar().leads.recordView.getEditField("accountName");
		account.set(accountName);
		account.assertEquals(accountName, true);

		// email
		VoodooControl email = sugar().leads.recordView.getEditField("emailAddress");
		email.set(emailAddress);
		email.assertEquals(emailAddress, true);

		// tags
		sugar().leads.recordView.getEditField("tags").assertVisible(true);

		// primary address street, city, zip, country, state
		sugar().leads.recordView.getEditField("primaryAddressStreet").assertEquals(myLead.get("primaryAddressStreet"), true);
		sugar().leads.recordView.getEditField("primaryAddressCity").assertEquals(myLead.get("primaryAddressCity"), true);
		sugar().leads.recordView.getEditField("primaryAddressState").assertEquals(myLead.get("primaryAddressState"), true);
		sugar().leads.recordView.getEditField("primaryAddressPostalCode").assertEquals(myLead.get("primaryAddressPostalCode"), true);
		sugar().leads.recordView.getEditField("primaryAddressCountry").assertEquals(myLead.get("primaryAddressCountry"), true);

		// department
		sugar().leads.recordView.getEditField("department").assertEquals(myLead.get("department"), true);

		// office phone
		sugar().leads.recordView.getEditField("phoneWork").assertEquals(myLead.get("phoneWork"), true);

		// fax
		sugar().leads.recordView.getEditField("phoneFax").assertEquals(myLead.get("phoneFax"), true);

		// description
		sugar().leads.recordView.getEditField("description").assertEquals(myLead.get("description"), true);

		// status
		sugar().leads.recordView.getEditField("status").assertEquals(myLead.get("status"), true);

		// status description
		sugar().leads.recordView.getEditField("statusDescription").assertEquals(myLead.get("statusDescription"), true);

		// lead source
		sugar().leads.recordView.getEditField("leadSource").assertEquals(myLead.get("leadSource"), true);

		// lead source description
		sugar().leads.recordView.getEditField("leadSourceDescription").assertEquals(myLead.get("leadSourceDescription"), true);

		// assigned to
		sugar().leads.recordView.getEditField("relAssignedTo").assertEquals("Administrator", true);

		// date created (read only)
		VoodooControl dateCreated = sugar().leads.recordView.getEditField("date_entered_date");
		dateCreated.assertVisible(true);
		dateCreated.assertAttribute("class", "edit", false);

		// created By
		sugar().leads.recordView.getEditField("dateEnteredBy").assertVisible(true);

		// date modified (read only)
		VoodooControl dateModified = sugar().leads.recordView.getEditField("date_modified_date");
		dateModified.assertVisible(true);
		dateModified.assertAttribute("class", "edit", false);

		// modified by
		sugar().leads.recordView.getEditField("dateModifiedBy").assertVisible(true);

		// teams
		sugar().leads.recordView.getEditField("relTeam").assertEquals("Global", true);		

		// save the record 
		sugar().leads.recordView.save();

		// verify detail view fields
		// full name
		sugar().leads.recordView.getDetailField("fullName").assertEquals(myLead.get("fullName"), true);

		// title
		sugar().leads.recordView.getDetailField("title").assertEquals(myLead.get("title"), true);

		// mobile
		sugar().leads.recordView.getDetailField("phoneMobile").assertEquals(myLead.get("phoneMobile"), true);

		// website
		sugar().leads.recordView.getDetailField("website").assertEquals(myLead.get("website"), true);

		// account name
		sugar().leads.recordView.getDetailField("accountName").assertEquals(accountName, true);

		// email
		sugar().leads.recordView.getDetailField("emailAddress").assertEquals(emailAddress, true);

		// tags
		sugar().leads.recordView.getDetailField("tags").assertVisible(true);

		// primary address street, city, zip, country, state
		sugar().leads.recordView.getDetailField("primaryAddressStreet").assertEquals(myLead.get("primaryAddressStreet"), true);
		sugar().leads.recordView.getDetailField("primaryAddressCity").assertEquals(myLead.get("primaryAddressCity"), true);
		sugar().leads.recordView.getDetailField("primaryAddressState").assertEquals(myLead.get("primaryAddressState"), true);
		sugar().leads.recordView.getDetailField("primaryAddressPostalCode").assertEquals(myLead.get("primaryAddressPostalCode"), true);
		sugar().leads.recordView.getDetailField("primaryAddressCountry").assertEquals(myLead.get("primaryAddressCountry"), true);

		// department
		sugar().leads.recordView.getDetailField("department").assertEquals(myLead.get("department"), true);

		// office phone
		sugar().leads.recordView.getDetailField("phoneWork").assertEquals(myLead.get("phoneWork"), true);

		// fax
		sugar().leads.recordView.getDetailField("phoneFax").assertEquals(myLead.get("phoneFax"), true);

		// description
		sugar().leads.recordView.getDetailField("description").assertEquals(myLead.get("description"), true);

		// status
		sugar().leads.recordView.getDetailField("status").assertEquals(myLead.get("status"), true);

		// status description
		sugar().leads.recordView.getDetailField("statusDescription").assertEquals(myLead.get("statusDescription"), true);

		// lead source
		sugar().leads.recordView.getDetailField("leadSource").assertEquals(myLead.get("leadSource"), true);

		// lead source description
		sugar().leads.recordView.getDetailField("leadSourceDescription").assertEquals(myLead.get("leadSourceDescription"), true);

		// assigned to
		sugar().leads.recordView.getDetailField("relAssignedTo").assertEquals("Administrator", true);

		// date created
		sugar().leads.recordView.getDetailField("date_entered_date").assertVisible(true);;

		// created By
		sugar().leads.recordView.getDetailField("dateEnteredBy").assertVisible(true);;

		// date modified
		sugar().leads.recordView.getDetailField("date_modified_date").assertVisible(true);;

		// modified by
		sugar().leads.recordView.getDetailField("dateModifiedBy").assertVisible(true);

		// teams
		sugar().leads.recordView.getDetailField("relTeam").assertEquals("Global (Primary)", true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	public void cleanup() throws Exception {}
}