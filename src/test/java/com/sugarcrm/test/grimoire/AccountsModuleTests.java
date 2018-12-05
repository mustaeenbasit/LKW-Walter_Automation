package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for menu dropdown items, listview headers, sortBy, edit/detail list & record hook values,
 * preview pane and subpanels on record view.
 * 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class AccountsModuleTests extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
		sugar().accounts.navToListView();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().accounts);

		// Verify menu items
		sugar().accounts.menu.getControl("createAccount").assertVisible(true);
		sugar().accounts.menu.getControl("viewAccounts").assertVisible(true);
		sugar().accounts.menu.getControl("viewAccountReports").assertVisible(true);
		sugar().accounts.menu.getControl("importAccounts").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().accounts); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		// Verify all sort headers in listview
		for(String header : sugar().accounts.listView.getHeaders()){
			sugar().accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() test complete.");
	}

	@Test
	public void sortOrderByName() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByName()...");

		// 2 accounts having 1 with default data and another with custom data
		FieldSet data = new FieldSet();
		data.put("name", "Nerd");
		sugar().accounts.api.create(data);
		sugar().accounts.navToListView(); // reload data

		// Verify records after sort by 'name ' in descending and ascending order
		sugar().accounts.listView.sortBy("headerName", false);
		sugar().accounts.listView.verifyField(1, "name", data.get("name"));
		sugar().accounts.listView.verifyField(2, "name", myAccount.getRecordIdentifier());

		sugar().accounts.listView.sortBy("headerName", true);
		sugar().accounts.listView.verifyField(1, "name", myAccount.getRecordIdentifier());
		sugar().accounts.listView.verifyField(2, "name", data.get("name"));

		VoodooUtils.voodoo.log.info("sortOrderByName() test complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		// preview record
		sugar().accounts.listView.previewRecord(1);
		sugar().previewPane.showMore();

		// Verify preview field values
		// name 
		sugar().previewPane.getPreviewPaneField("name").assertEquals(myAccount.get("name"), true);

		// website
		sugar().previewPane.getPreviewPaneField("website").assertEquals(myAccount.get("website"), true);

		// industry
		sugar().previewPane.getPreviewPaneField("industry").assertEquals(myAccount.get("industry"), true);

		// member of 
		sugar().previewPane.getPreviewPaneField("memberOf").assertVisible(true);

		// type
		sugar().previewPane.getPreviewPaneField("type").assertEquals(myAccount.get("type"), true);

		// assigned to
		sugar().previewPane.getPreviewPaneField("relAssignedTo").assertEquals("Administrator", true);

		// office phone
		sugar().previewPane.getPreviewPaneField("workPhone").assertEquals(myAccount.get("workPhone"), true);

		// billing address
		sugar().previewPane.getPreviewPaneField("billingAddressStreet").assertContains(myAccount.get("billingAddressStreet"), true);

		// billing city
		sugar().previewPane.getPreviewPaneField("billingAddressCity").assertContains(myAccount.get("billingAddressCity"), true);

		// billing state
		sugar().previewPane.getPreviewPaneField("billingAddressState").assertContains(myAccount.get("billingAddressState"), true);

		// billing postal code
		sugar().previewPane.getPreviewPaneField("billingAddressPostalCode").assertContains(myAccount.get("billingAddressPostalCode"), true);

		// billing country
		sugar().previewPane.getPreviewPaneField("billingAddressCountry").assertContains(myAccount.get("billingAddressCountry"), true);

		// alternate phone 
		sugar().previewPane.getPreviewPaneField("alternatePhone").assertVisible(true);

		// email address
		sugar().previewPane.getPreviewPaneField("emailAddress").assertVisible(true);

		// fax
		sugar().previewPane.getPreviewPaneField("fax").assertEquals(myAccount.get("fax"), true);

		// campaign
		sugar().previewPane.getPreviewPaneField("campaign").assertVisible(true);

		// twitter account
		sugar().previewPane.getPreviewPaneField("twitter").assertVisible(true);

		// description
		sugar().previewPane.getPreviewPaneField("description").assertVisible(true);

		// sic code
		sugar().previewPane.getPreviewPaneField("sicCode").assertEquals(myAccount.get("sicCode"), true);

		// ticker symbol
		sugar().previewPane.getPreviewPaneField("tickerSymbol").assertVisible(true);

		// annual Revenue
		sugar().previewPane.getPreviewPaneField("annualRevenue").assertVisible(true);

		// employees
		sugar().previewPane.getPreviewPaneField("employees").assertVisible(true);

		// ownership
		sugar().previewPane.getPreviewPaneField("ownership").assertVisible(true);

		// rating
		sugar().previewPane.getPreviewPaneField("rating").assertVisible(true);

		// TODO: TR-10415 Once resolved below line will work
		// DUNS
		//sugar().previewPane.getPreviewPaneField("dunsNum").assertVisible(true);

		// date created
		sugar().previewPane.getPreviewPaneField("date_entered_date").assertVisible(true);

		// created time
		sugar().previewPane.getPreviewPaneField("date_entered_time").assertVisible(true);

		// created by
		sugar().previewPane.getPreviewPaneField("dateEnteredBy").assertVisible(true);

		// teams
		sugar().previewPane.getPreviewPaneField("relTeam").assertVisible(true);

		// date modified
		sugar().previewPane.getPreviewPaneField("date_modified_date").assertVisible(true);

		// modified time
		sugar().previewPane.getPreviewPaneField("date_modified_time").assertVisible(true);

		// modified by
		sugar().previewPane.getPreviewPaneField("dateModifiedBy").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().accounts.listView.editRecord(1);

		// name
		sugar().accounts.listView.getEditField(1, "name").assertEquals(myAccount.get("name"), true);

		// city
		sugar().accounts.listView.getEditField(1, "billingAddressCity").assertEquals(myAccount.get("billingAddressCity"), true);

		// country
		sugar().accounts.listView.getEditField(1, "billingAddressCountry").assertEquals(myAccount.get("billingAddressCountry"), true);

		// office phone
		sugar().accounts.listView.getEditField(1, "workPhone").assertEquals(myAccount.get("workPhone"), true);

		// user
		sugar().accounts.listView.getEditField(1, "relAssignedTo").assertVisible(true);

		// email address
		sugar().accounts.listView.getEditField(1, "emailAddress").assertVisible(true);

		// date created (read-only)
		VoodooControl dateCreated = sugar().accounts.listView.getEditField(1, "date_entered_date");
		dateCreated.scrollIntoViewIfNeeded(false);
		dateCreated.assertVisible(true);
		dateCreated.assertAttribute("class", "edit", false);

		// time created (read-only)
		VoodooControl timeCreated = sugar().accounts.listView.getEditField(1, "date_entered_time");
		timeCreated.assertVisible(true);
		timeCreated.assertAttribute("class", "edit", false);

		// date modified (read-only)
		VoodooControl dateModified = sugar().accounts.listView.getEditField(1, "date_modified_date");
		dateModified.assertVisible(true);
		dateModified.assertAttribute("class", "edit", false);

		// time modified (read-only)
		VoodooControl timeModified = sugar().accounts.listView.getEditField(1, "date_modified_time");
		timeModified.assertVisible(true);
		timeModified.assertAttribute("class", "edit", false);

		// cancel record
		sugar().accounts.listView.cancelRecord(1);

		// verify detail fields
		// name
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(myAccount.get("name"), true);

		// city
		sugar().accounts.listView.getDetailField(1, "billingAddressCity").assertEquals(myAccount.get("billingAddressCity"), true);

		// country
		sugar().accounts.listView.getDetailField(1, "billingAddressCountry").assertEquals(myAccount.get("billingAddressCountry"), true);

		// phone
		sugar().accounts.listView.getDetailField(1, "workPhone").assertEquals(myAccount.get("workPhone"), true);

		// user
		sugar().accounts.listView.getDetailField(1, "relAssignedTo").assertVisible(true);

		// Date Created
		sugar().accounts.listView.getDetailField(1, "date_entered_date").assertVisible(true);

		// Time Created
		sugar().accounts.listView.getDetailField(1, "date_entered_time").assertVisible(true);

		// Date modified
		sugar().accounts.listView.getDetailField(1, "date_modified_date").assertVisible(true);

		// Time modified
		sugar().accounts.listView.getDetailField(1, "date_modified_time").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();

		// name
		sugar().accounts.recordView.getEditField("name").assertEquals(myAccount.get("name"), true);

		// website
		sugar().accounts.recordView.getEditField("website").assertEquals(myAccount.get("website"), true);

		// industry
		sugar().accounts.recordView.getEditField("industry").assertEquals(myAccount.get("industry"), true);

		// member of
		sugar().accounts.recordView.getEditField("memberOf").assertVisible(true);

		// type
		sugar().accounts.recordView.getEditField("type").assertEquals(myAccount.get("type"), true);

		// assigned to
		sugar().accounts.recordView.getEditField("relAssignedTo").assertEquals("Administrator", true);

		// office phone
		sugar().accounts.recordView.getEditField("workPhone").assertEquals(myAccount.get("workPhone"), true);

		// billing address street, city, zip, country, state
		sugar().accounts.recordView.getEditField("billingAddressStreet").assertEquals(myAccount.get("billingAddressStreet"), true);
		sugar().accounts.recordView.getEditField("billingAddressCity").assertEquals(myAccount.get("billingAddressCity"), true);
		sugar().accounts.recordView.getEditField("billingAddressState").assertEquals(myAccount.get("billingAddressState"), true);
		sugar().accounts.recordView.getEditField("billingAddressPostalCode").assertEquals(myAccount.get("billingAddressPostalCode"), true);
		sugar().accounts.recordView.getEditField("billingAddressCountry").assertEquals(myAccount.get("billingAddressCountry"), true);

		// shipping	
		sugar().accounts.recordView.getEditField("shippingAddressStreet").assertVisible(true);
		sugar().accounts.recordView.getEditField("shippingAddressCity").assertVisible(true);
		sugar().accounts.recordView.getEditField("shippingAddressState").assertVisible(true);
		sugar().accounts.recordView.getEditField("shippingAddressPostalCode").assertVisible(true);
		sugar().accounts.recordView.getEditField("shippingAddressCountry").assertVisible(true);

		// alternate phone
		sugar().accounts.recordView.getEditField("alternatePhone").assertVisible(true);

		// email
		sugar().accounts.recordView.getEditField("emailAddress").assertVisible(true);

		// fax
		sugar().accounts.recordView.getEditField("fax").assertVisible(true);

		// campaign
		sugar().accounts.recordView.getEditField("campaign").assertVisible(true);

		// twitter
		sugar().accounts.recordView.getEditField("twitter").assertVisible(true);

		// description
		sugar().accounts.recordView.getEditField("description").assertVisible(true);

		// SIC code
		sugar().accounts.recordView.getEditField("sicCode").assertVisible(true);

		// ticker symbol
		sugar().accounts.recordView.getEditField("tickerSymbol").assertVisible(true);

		// annual revenue
		sugar().accounts.recordView.getEditField("annualRevenue").assertVisible(true);

		// employees
		sugar().accounts.recordView.getEditField("employees").assertVisible(true);

		// ownership
		sugar().accounts.recordView.getEditField("ownership").assertVisible(true);

		// rating
		sugar().accounts.recordView.getEditField("rating").assertVisible(true);

		// DUNS (read only)
		VoodooControl dunsNum = sugar().accounts.recordView.getEditField("dunsNum");
		dunsNum.assertVisible(true);
		dunsNum.assertAttribute("class", "edit", false);

		// date created (read only)
		VoodooControl dateCreated = sugar().accounts.recordView.getEditField("date_entered_date");
		dateCreated.assertVisible(true);
		dateCreated.assertAttribute("class", "edit", false);

		// time created (read only)
		VoodooControl timeCreated = sugar().accounts.recordView.getEditField("date_entered_time");
		timeCreated.assertVisible(true);
		timeCreated.assertAttribute("class", "edit", false);

		// created By
		VoodooControl createdBy = sugar().accounts.recordView.getEditField("dateEnteredBy");
		createdBy.assertVisible(true);
		createdBy.assertAttribute("class", "edit", false);

		// date modified (read only)
		VoodooControl dateModified = sugar().accounts.recordView.getEditField("date_modified_date");
		dateModified.assertVisible(true);
		dateModified.assertAttribute("class", "edit", false);

		// time modified (read only)
		VoodooControl timeModified = sugar().accounts.recordView.getEditField("date_modified_time");
		timeModified.assertVisible(true);
		timeModified.assertAttribute("class", "edit", false);

		// modified by
		VoodooControl modifiedBy = sugar().accounts.recordView.getEditField("dateModifiedBy");
		modifiedBy.assertVisible(true);
		modifiedBy.assertAttribute("class", "edit", false);

		// teams
		sugar().accounts.recordView.getEditField("relTeam").assertEquals("Global", true);		

		// cancel the record 
		sugar().accounts.recordView.cancel();

		// verify detail view fields
		// name
		sugar().accounts.recordView.getDetailField("name").assertEquals(myAccount.get("name"), true);

		// website
		sugar().accounts.recordView.getDetailField("website").assertEquals(myAccount.get("website"), true);

		// industry
		sugar().accounts.recordView.getDetailField("industry").assertEquals(myAccount.get("industry"), true);

		// member of
		sugar().accounts.recordView.getDetailField("memberOf").assertVisible(true);

		// type
		sugar().accounts.recordView.getDetailField("type").assertEquals(myAccount.get("type"), true);

		// assigned to
		sugar().accounts.recordView.getDetailField("relAssignedTo").assertEquals("Administrator", true);

		// office phone
		sugar().accounts.recordView.getDetailField("workPhone").assertEquals(myAccount.get("workPhone"), true);

		// billing address street, city, zip, country, state
		sugar().accounts.recordView.getDetailField("billingAddressStreet").assertEquals(myAccount.get("billingAddressStreet"), true);
		sugar().accounts.recordView.getDetailField("billingAddressCity").assertEquals(myAccount.get("billingAddressCity"), true);
		sugar().accounts.recordView.getDetailField("billingAddressState").assertEquals(myAccount.get("billingAddressState"), true);
		sugar().accounts.recordView.getDetailField("billingAddressPostalCode").assertEquals(myAccount.get("billingAddressPostalCode"), true);
		sugar().accounts.recordView.getDetailField("billingAddressCountry").assertEquals(myAccount.get("billingAddressCountry"), true);

		// shipping	
		sugar().accounts.recordView.getDetailField("shippingAddressStreet").assertVisible(true);
		sugar().accounts.recordView.getDetailField("shippingAddressCity").assertVisible(true);
		sugar().accounts.recordView.getDetailField("shippingAddressState").assertVisible(true);
		sugar().accounts.recordView.getDetailField("shippingAddressPostalCode").assertVisible(true);
		sugar().accounts.recordView.getDetailField("shippingAddressCountry").assertVisible(true);

		// alternate phone
		sugar().accounts.recordView.getDetailField("alternatePhone").assertVisible(true);

		// email
		sugar().accounts.recordView.getDetailField("emailAddress").assertVisible(true);

		// fax
		sugar().accounts.recordView.getDetailField("fax").assertVisible(true);

		// campaign
		sugar().accounts.recordView.getDetailField("campaign").assertVisible(true);

		// twitter
		sugar().accounts.recordView.getDetailField("twitter").assertVisible(true);

		// description
		sugar().accounts.recordView.getDetailField("description").assertVisible(true);

		// SIC code
		sugar().accounts.recordView.getDetailField("sicCode").assertVisible(true);

		// ticker symbol
		sugar().accounts.recordView.getDetailField("tickerSymbol").assertVisible(true);

		// annual revenue
		sugar().accounts.recordView.getDetailField("annualRevenue").assertVisible(true);

		// employees
		sugar().accounts.recordView.getDetailField("employees").assertVisible(true);

		// ownership
		sugar().accounts.recordView.getDetailField("ownership").assertVisible(true);

		// rating
		sugar().accounts.recordView.getDetailField("rating").assertVisible(true);

		// DUNS (read only)
		sugar().accounts.recordView.getDetailField("dunsNum").assertVisible(true);;

		// date created
		sugar().accounts.recordView.getDetailField("date_entered_date").assertVisible(true);;

		// time created
		sugar().accounts.recordView.getDetailField("date_entered_time").assertVisible(true);;

		// created By
		sugar().accounts.recordView.getDetailField("dateEnteredBy").assertVisible(true);;

		// date modified
		sugar().accounts.recordView.getDetailField("date_modified_date").assertVisible(true);;

		// time modified
		sugar().accounts.recordView.getDetailField("date_modified_time").assertVisible(true);;

		// modified by
		sugar().accounts.recordView.getDetailField("dateModifiedBy").assertVisible(true);

		// teams
		sugar().accounts.recordView.getDetailField("relTeam").assertEquals("Global (Primary)", true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().accounts.listView.clickRecord(1);

		// Verify subpanels
		sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().meetings.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().emails.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().quotes.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural).assertVisible(true);		
		sugar().accounts.recordView.subpanels.get(sugar().documents.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural).assertVisible(true);
		// Verify existence of QLI subpanel is by default hide
		sugar().accounts.recordView.subpanels.get(sugar().quotedLineItems.moduleNamePlural).assertVisible(false);
		// TODO: VOOD-1344
		// for Campaign log support

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	public void cleanup() throws Exception {}
}