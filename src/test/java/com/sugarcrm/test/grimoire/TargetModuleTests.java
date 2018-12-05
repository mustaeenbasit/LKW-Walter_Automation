package com.sugarcrm.test.grimoire;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooTag;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for menu dropdown items, listview headers, sortBy, edit/detail list & detail hook values,
 * preview pane & subpanels on record view.
 * 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class TargetModuleTests extends SugarTest {
	TargetRecord myTarget;
	String emailAddress = "ajabble@sugarcrm.com";

	public void setup() throws Exception {
		myTarget = (TargetRecord)sugar().targets.api.create();
		sugar().login();
		sugar().targets.navToListView();
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().targets.listView.editRecord(1);

		// first name
		sugar().targets.listView.getEditField(1, "firstName").assertEquals(myTarget.get("firstName"), true);

		// last name
		sugar().targets.listView.getEditField(1, "lastName").assertEquals(myTarget.get("lastName"), true);

		// title
		sugar().targets.listView.getEditField(1, "title").assertEquals(myTarget.get("title"), true);

		// email address
		VoodooControl emailAddressFieldCtrl = sugar().targets.listView.getEditField(1, "emailAddress");
		emailAddressFieldCtrl.set(emailAddress);
		emailAddressFieldCtrl.assertEquals(emailAddress, true);

		// phone work
		sugar().targets.listView.getEditField(1, "phoneWork").assertEquals(myTarget.get("phoneWork"), true);

		// save the record with relation
		sugar().targets.listView.saveRecord(1);

		// verify detail fields
		// full name
		sugar().targets.listView.getDetailField(1, "fullName").assertContains(myTarget.get("fullName"), true);

		// title
		sugar().targets.listView.getDetailField(1, "title").assertEquals(myTarget.get("title"), true);

		// email address
		sugar().targets.listView.getDetailField(1, "emailAddress").assertEquals(emailAddress, true);

		// phone work
		sugar().targets.listView.getDetailField(1, "phoneWork").assertEquals(myTarget.get("phoneWork"), true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().targets.listView.clickRecord(1);
		sugar().targets.recordView.edit();
		sugar().targets.recordView.showMore();

		// first name
		sugar().targets.recordView.getEditField("firstName").assertEquals(myTarget.get("firstName"), true);

		// last name
		sugar().targets.recordView.getEditField("lastName").assertContains(myTarget.get("lastName"), true);

		// account name
		sugar().targets.recordView.getEditField("account_name").assertEquals(myTarget.get("account_name"), true);

		// title
		sugar().targets.recordView.getEditField("title").assertEquals(myTarget.get("title"), true);

		// department
		sugar().targets.recordView.getEditField("department").assertEquals(myTarget.get("department"), true);

		// phone mobile
		sugar().targets.recordView.getEditField("phoneMobile").assertEquals(myTarget.get("phoneMobile"), true);

		// phone work
		sugar().targets.recordView.getEditField("phoneWork").assertEquals(myTarget.get("phoneWork"), true);

		// primary address street
		sugar().targets.recordView.getEditField("primaryAddressStreet").assertEquals(myTarget.get("primaryAddressStreet"), true);

		// primary address city
		sugar().targets.recordView.getEditField("primaryAddressCity").assertEquals(myTarget.get("primaryAddressCity"), true);

		// primary address state
		sugar().targets.recordView.getEditField("primaryAddressState").assertEquals(myTarget.get("primaryAddressState"), true);

		// primary address postal code
		sugar().targets.recordView.getEditField("primaryAddressPostalCode").assertEquals(myTarget.get("primaryAddressPostalCode"), true);

		// primary address country
		sugar().targets.recordView.getEditField("primaryAddressCountry").assertEquals(myTarget.get("primaryAddressCountry"), true);

		// email address
		VoodooControl emailAddressFieldCtrl = sugar().targets.recordView.getEditField("emailAddress");
		emailAddressFieldCtrl.set(emailAddress);
		emailAddressFieldCtrl.assertEquals(emailAddress, true);

		// assigned to
		sugar().targets.recordView.getEditField("assignedTo").assertEquals("Administrator", true);

		// description
		sugar().targets.recordView.getEditField("description").assertVisible(true);

		// team Name
		sugar().targets.recordView.getEditField("teamName").assertVisible(true);

		// twitter account
		sugar().targets.recordView.getEditField("twitterAccount").assertVisible(true);

		// tags
		VoodooTag tag = (VoodooTag)sugar().targets.recordView.getEditField("tags");
		tag.set(testName);
		tag.assertContains(testName, true);

		// save the record 
		sugar().targets.recordView.save();

		// verify detail view fields
		// full name because myTarget.getRecordIdentifier(); -> only returns firstName
		sugar().targets.recordView.getDetailField("fullName").assertEquals(myTarget.get("fullName"), true);

		// account name
		sugar().targets.recordView.getDetailField("account_name").assertEquals(myTarget.get("account_name"), true);

		// title
		sugar().targets.recordView.getDetailField("title").assertEquals(myTarget.get("title"), true);

		// department
		sugar().targets.recordView.getDetailField("department").assertEquals(myTarget.get("department"), true);

		// phone mobile
		sugar().targets.recordView.getDetailField("phoneMobile").assertEquals(myTarget.get("phoneMobile"), true);

		// phone work
		sugar().targets.recordView.getDetailField("phoneWork").assertEquals(myTarget.get("phoneWork"), true);

		// primary address street
		sugar().targets.recordView.getDetailField("primaryAddressStreet").assertEquals(myTarget.get("primaryAddressStreet"), true);

		// primary address city
		sugar().targets.recordView.getDetailField("primaryAddressCity").assertEquals(myTarget.get("primaryAddressCity"), true);

		// primary address state
		sugar().targets.recordView.getDetailField("primaryAddressState").assertEquals(myTarget.get("primaryAddressState"), true);

		// primary address postal code
		sugar().targets.recordView.getDetailField("primaryAddressPostalCode").assertEquals(myTarget.get("primaryAddressPostalCode"), true);

		// primary address country
		sugar().targets.recordView.getDetailField("primaryAddressCountry").assertEquals(myTarget.get("primaryAddressCountry"), true);

		// email address
		sugar().targets.recordView.getDetailField("emailAddress").assertEquals(emailAddress, true);

		// assigned to
		sugar().targets.recordView.getDetailField("assignedTo").assertEquals("Administrator", true);

		// description
		sugar().targets.recordView.getDetailField("description").assertVisible(true);

		// team Name
		sugar().targets.recordView.getDetailField("teamName").assertVisible(true);

		// twitter account
		sugar().targets.recordView.getDetailField("twitterAccount").assertVisible(true);

		// tags
		sugar().targets.recordView.getDetailField("tags").assertContains(testName, true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		// preview record
		sugar().targets.listView.previewRecord(1);

		// Verify preview field values
		// full name 
		sugar().previewPane.getPreviewPaneField("fullName").assertContains(myTarget.get("fullName"), true);

		// account_name
		sugar().previewPane.getPreviewPaneField("account_name").assertEquals(myTarget.get("account_name"), true);

		// title
		sugar().previewPane.getPreviewPaneField("title").assertEquals(myTarget.get("title"), true);

		// email address
		sugar().previewPane.getPreviewPaneField("emailAddress").assertVisible(true);

		// department
		sugar().previewPane.getPreviewPaneField("department").assertEquals(myTarget.get("department"), true);

		// phone mobile
		sugar().previewPane.getPreviewPaneField("phoneMobile").assertEquals(myTarget.get("phoneMobile"), true);

		// show more fields
		sugar().previewPane.showMore();

		// primary address street
		sugar().previewPane.getPreviewPaneField("primaryAddressStreet").assertEquals(myTarget.get("primaryAddressStreet"), true);

		// primary address city
		sugar().previewPane.getPreviewPaneField("primaryAddressCity").assertEquals(myTarget.get("primaryAddressCity"), true);

		// primary address state
		sugar().previewPane.getPreviewPaneField("primaryAddressState").assertEquals(myTarget.get("primaryAddressState"), true);

		// primary address postal code
		sugar().previewPane.getPreviewPaneField("primaryAddressPostalCode").assertEquals(myTarget.get("primaryAddressPostalCode"), true);

		// primary address country
		sugar().previewPane.getPreviewPaneField("primaryAddressCountry").assertEquals(myTarget.get("primaryAddressCountry"), true);

		// phone work
		sugar().previewPane.getPreviewPaneField("phoneWork").assertEquals(myTarget.get("phoneWork"), true);

		// assigned to
		sugar().previewPane.getPreviewPaneField("assignedTo").assertEquals("Administrator", true);

		// description
		sugar().previewPane.getPreviewPaneField("description").assertVisible(true);

		// team 
		sugar().previewPane.getPreviewPaneField("teamName").assertContains("Global", true);

		// twitter
		sugar().previewPane.getPreviewPaneField("twitterAccount").assertVisible(true);

		// tags
		sugar().previewPane.getPreviewPaneField("tags").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().targets.listView.clickRecord(1);

		// Verify subpanels
		sugar().targets.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertVisible(true);
		sugar().targets.recordView.subpanels.get(sugar().meetings.moduleNamePlural).assertVisible(true);
		sugar().targets.recordView.subpanels.get(sugar().tasks.moduleNamePlural).assertVisible(true);
		sugar().targets.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);
		Assert.assertTrue("Records are in Campaign Log Subpanel", sugar().targets.recordView.subpanels.get(sugar().campaigns.moduleNamePlural).isEmpty());
		// TODO: VOOD-1499 assertion on campaign log is not working on DOM, however its hook value is there on page
		// Really weird, we can perform actions on same control, but not able to check its existence and visibilty of an element
		//sugar().targets.recordView.subpanels.get(sugar().campaigns.moduleNamePlural).assertExists(true);
		sugar().targets.recordView.subpanels.get(sugar().emails.moduleNamePlural).assertVisible(true);		

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().targets);

		// Verify menu items
		sugar().targets.menu.getControl("createTarget").assertVisible(true);
		sugar().targets.menu.getControl("createtargetFromVcard").assertVisible(true);
		sugar().targets.menu.getControl("viewtargets").assertVisible(true);
		sugar().targets.menu.getControl("importTargets").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().targets); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		// TODO: VOOD-1768 - Once resolved "email" header check should remove it from for loop
		new VoodooControl("th", "css", "th[data-fieldname=email]").assertVisible(true);

		// Verify all sort headers in listview
		for(String header : sugar().targets.listView.getHeaders()) {
			// Email doesnot having sort option
			if (!header.equals("email")) {
				sugar().targets.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
			}
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() complete.");
	}

	@Test
	public void sortOrderByTargetName() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByTargetName()...");

		// 2 Target having 1 with default data and another with custom data
		FieldSet customTarget = new FieldSet();
		customTarget.put("firstName", "Ashish");
		customTarget.put("lastName", "Jabble");
		customTarget.put("fullName", "Ashish Jabble");
		sugar().targets.api.create(customTarget);
		sugar().targets.navToListView(); // needed to reload UI, recently data created via API

		// Verify records after sort by 'target name ' in descending and ascending order
		sugar().targets.listView.sortBy("headerFullname", false);
		sugar().targets.listView.verifyField(1, "fullName", myTarget.getRecordIdentifier());
		sugar().targets.listView.verifyField(2, "fullName", customTarget.get("fullName"));

		sugar().targets.listView.sortBy("headerFullname", true);
		sugar().targets.listView.verifyField(1, "fullName", customTarget.get("fullName"));
		sugar().targets.listView.verifyField(2, "fullName", myTarget.getRecordIdentifier());

		VoodooUtils.voodoo.log.info("sortOrderByTargetName() complete.");
	}

	public void cleanup() throws Exception {}
}