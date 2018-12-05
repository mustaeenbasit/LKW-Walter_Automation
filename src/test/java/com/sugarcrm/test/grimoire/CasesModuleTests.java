package com.sugarcrm.test.grimoire;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooTag;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for menu dropdown items, listview headers, sortBy, edit/detail list & detail hook values,
 * preview pane & subpanels on record view.
 * 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class CasesModuleTests extends SugarTest{
	CaseRecord myCase;

	public void setup() throws Exception {
		myCase = (CaseRecord)sugar().cases.api.create();
		sugar().login();
		sugar().cases.navToListView();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().cases);

		// Verify menu items
		sugar().cases.menu.getControl("createCase").assertVisible(true);
		sugar().cases.menu.getControl("viewCases").assertVisible(true);
		sugar().cases.menu.getControl("viewCaseReports").assertVisible(true);
		sugar().cases.menu.getControl("importCases").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().cases); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		sugar().cases.listView.toggleHeaderColumn("team_name");

		// TODO: VOOD-1768 - Once resolved "team_name" header check should remove it from for loop
		new VoodooControl("th", "css", "th[data-fieldname=team_name]").assertVisible(true);

		// Verify all sort headers in listview
		for(String header : sugar().cases.listView.getHeaders()) {
			// team_name without sort option
			if(!header.equals("team_name")) {
				sugar().cases.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
			}
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() complete.");
	}

	@Test
	public void sortOrderByName() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByName()...");

		// 2 Cases having 1 with default data and another with custom data
		FieldSet customCase = new FieldSet();
		customCase.put("name", "Latest build#1234 crash on login.");
		sugar().cases.api.create(customCase);
		sugar().cases.navToListView(); // need to reload UI, recently data created via API, refresh method sometimes working or vice-versa

		// Verify records after sort by 'name ' in descending and ascending order
		sugar().cases.listView.sortBy("headerName", false);
		sugar().cases.listView.verifyField(1, "name", myCase.getRecordIdentifier());
		sugar().cases.listView.verifyField(2, "name", customCase.get("name"));

		sugar().cases.listView.sortBy("headerName", true);
		sugar().cases.listView.verifyField(1, "name", customCase.get("name"));
		sugar().cases.listView.verifyField(2, "name", myCase.getRecordIdentifier());

		VoodooUtils.voodoo.log.info("sortOrderByName() complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().cases.listView.clickRecord(1);

		// Verify subpanels
		sugar().cases.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertVisible(true);
		sugar().cases.recordView.subpanels.get(sugar().meetings.moduleNamePlural).assertVisible(true);
		sugar().cases.recordView.subpanels.get(sugar().tasks.moduleNamePlural).assertVisible(true);
		sugar().cases.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);
		sugar().cases.recordView.subpanels.get(sugar().documents.moduleNamePlural).assertVisible(true);
		sugar().cases.recordView.subpanels.get(sugar().contacts.moduleNamePlural).assertVisible(true);
		sugar().cases.recordView.subpanels.get(sugar().emails.moduleNamePlural).assertVisible(true);
		sugar().cases.recordView.subpanels.get(sugar().bugs.moduleNamePlural).assertVisible(false);
		sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural).assertVisible(true);

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().accounts.api.create();

		sugar().cases.listView.editRecord(1);

		// case number (read only)
		sugar().cases.listView.getEditField(1, "caseNumber").assertVisible(true);

		// subject
		sugar().cases.listView.getEditField(1, "name").assertEquals(myCase.get("name"), true);

		// account name
		FieldSet accountsFS = sugar().accounts.getDefaultData();
		VoodooSelect accountName = (VoodooSelect)sugar().cases.listView.getEditField(1, "relAccountName");
		accountName.set(accountsFS.get("name"));
		accountName.assertEquals(accountsFS.get("name"), true);

		// priority
		VoodooSelect priority = (VoodooSelect)sugar().cases.listView.getEditField(1, "priority");
		priority.set(myCase.get("priority"));
		priority.assertEquals(myCase.get("priority"), true);

		// status
		// TODO: VOOD-1843
		VoodooControl status = sugar().cases.listView.getEditField(1, "status");
		status.getChildElement("span", "css", status.getHookString() + " span.select2-chosen").assertEquals(myCase.get("status"), true);

		// assignedTo
		FieldSet qauserFS = sugar().users.getQAUser();
		VoodooSelect assignedTo = (VoodooSelect)sugar().cases.listView.getEditField(1, "relAssignedTo");
		assignedTo.scrollIntoViewIfNeeded(false);
		assignedTo.set(qauserFS.get("userName"));
		assignedTo.assertEquals(qauserFS.get("userName"), true);

		// date created (read-only)
		VoodooControl dateCreated = sugar().cases.listView.getEditField(1, "date_entered_date");
		dateCreated.assertExists(true);
		dateCreated.assertAttribute("class", "edit", false);

		// date modified (read-only)
		VoodooControl dateModified = sugar().cases.listView.getEditField(1, "date_modified_date");
		dateModified.assertExists(true);
		dateModified.assertAttribute("class", "edit", false);

		// save record
		sugar().cases.listView.saveRecord(1);
		if(sugar().alerts.getSuccess().queryVisible()) {
			sugar().alerts.getSuccess().closeAlert();
		}

		// verify detail fields
		// case number
		sugar().cases.listView.getDetailField(1, "caseNumber").assertVisible(true);

		// subject
		sugar().cases.listView.getDetailField(1, "name").assertContains(myCase.get("name"), true);

		// account name
		sugar().cases.listView.getDetailField(1, "relAccountName").assertEquals(accountsFS.get("name"), true);

		// priority
		sugar().cases.listView.getDetailField(1, "priority").assertEquals(myCase.get("priority"), true);

		// status
		sugar().cases.listView.getDetailField(1, "status").assertEquals(myCase.get("status"), true);

		// assigned
		sugar().cases.listView.getDetailField(1, "relAssignedTo").assertEquals(qauserFS.get("userName"), true);

		// Date Created
		sugar().cases.listView.getDetailField(1, "date_entered_date").assertExists(true);

		// Date modified
		sugar().cases.listView.getDetailField(1, "date_modified_date").assertExists(true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		// preview record
		sugar().cases.listView.previewRecord(1);
		sugar().previewPane.showMore();

		// Verify preview field values
		// number id
		sugar().previewPane.getPreviewPaneField("caseNumber").assertVisible(true);

		// subject 
		sugar().previewPane.getPreviewPaneField("name").assertEquals(myCase.get("name"), true);

		// account name
		sugar().previewPane.getPreviewPaneField("relAccountName").assertVisible(true);

		// priority
		sugar().previewPane.getPreviewPaneField("priority").assertVisible(true);

		// status
		sugar().previewPane.getPreviewPaneField("status").assertEquals(myCase.get("status"), true);

		// show in portal (disabled and not checked)
		VoodooControl showInPortal = sugar().previewPane.getPreviewPaneField("check_portal_viewable");
		Assert.assertTrue("portal viewable is enabled", showInPortal.isDisabled());
		showInPortal.assertChecked(false);

		// description
		sugar().previewPane.getPreviewPaneField("description").assertEquals(myCase.get("description"), true);

		// resolution
		sugar().previewPane.getPreviewPaneField("resolution").assertEquals(myCase.get("resolution"), true);

		// type
		sugar().previewPane.getPreviewPaneField("type").assertEquals(myCase.get("type"), true);

		// source
		sugar().previewPane.getPreviewPaneField("source").assertEquals(myCase.get("source"), true);

		// teams
		sugar().previewPane.getPreviewPaneField("relTeam").assertVisible(true);

		// assigned
		sugar().cases.listView.getDetailField(1, "relAssignedTo").assertEquals("Administrator", true);

		// Date Created
		sugar().previewPane.getPreviewPaneField("date_entered_date").assertVisible(true);

		// Date modified
		sugar().previewPane.getPreviewPaneField("date_modified_date").assertVisible(true);

		// tags
		sugar().previewPane.getPreviewPaneField("tags").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().cases.listView.clickRecord(1);
		sugar().cases.recordView.edit();
		sugar().cases.recordView.showMore();

		// number id (read-only)
		VoodooControl caseNumber = sugar().cases.recordView.getEditField("caseNumber");
		caseNumber.assertVisible(true);
		caseNumber.assertAttribute("class", "edit", false);

		// subject
		sugar().cases.recordView.getEditField("name").assertEquals(myCase.get("name"), true);

		// account name
		sugar().cases.recordView.getEditField("relAccountName").assertVisible(true);

		// priority
		VoodooSelect priority = (VoodooSelect)sugar().cases.recordView.getEditField("priority");
		priority.set(myCase.get("priority"));
		priority.assertEquals(myCase.get("priority"), true);

		// show portal
		VoodooControl showInPortal = sugar().cases.recordView.getEditField("check_portal_viewable");
		showInPortal.set("true");
		showInPortal.assertChecked(true);

		// status
		sugar().cases.recordView.getEditField("status").assertEquals(myCase.get("status"), true);

		// assignedTo
		sugar().cases.recordView.getEditField("relAssignedTo").assertEquals("Administrator", true);

		// description
		sugar().cases.recordView.getEditField("description").assertEquals(myCase.get("description"), true);

		// resolution
		sugar().cases.recordView.getEditField("resolution").assertEquals(myCase.get("resolution"), true);

		// type
		sugar().cases.recordView.getEditField("type").assertEquals(myCase.get("type"), true);

		// source
		sugar().cases.recordView.getEditField("source").assertEquals(myCase.get("source"), true);

		// teams
		sugar().cases.recordView.getEditField("relTeam").assertEquals("Global", true);

		// date created
		sugar().cases.recordView.getEditField("date_entered_date").assertVisible(true);

		// date modified
		sugar().cases.recordView.getEditField("date_modified_date").assertVisible(true);

		// tags
		VoodooTag tag = (VoodooTag)sugar().cases.recordView.getEditField("tags");
		tag.set(testName);
		tag.assertContains(testName, true);

		// cancel the record 
		sugar().cases.recordView.cancel();

		// verify detail view fields
		// number
		sugar().cases.recordView.getDetailField("caseNumber").assertVisible(true);

		// name
		sugar().cases.recordView.getDetailField("name").assertEquals(myCase.get("name"), true);

		// account name
		sugar().cases.recordView.getDetailField("relAccountName").assertVisible(true);

		// priority
		sugar().cases.recordView.getDetailField("priority").assertVisible(true);

		// show portal
		sugar().cases.recordView.getDetailField("check_portal_viewable").assertChecked(false);

		// status
		sugar().cases.recordView.getDetailField("status").assertEquals(myCase.get("status"), true);

		// assignedTo
		sugar().cases.recordView.getDetailField("relAssignedTo").assertEquals("Administrator", true);

		// description
		sugar().cases.recordView.getDetailField("description").assertEquals(myCase.get("description"), true);

		// resolution
		sugar().cases.recordView.getDetailField("resolution").assertEquals(myCase.get("resolution"), true);

		// type
		sugar().cases.recordView.getDetailField("type").assertEquals(myCase.get("type"), true);

		// source
		sugar().cases.recordView.getDetailField("source").assertEquals(myCase.get("source"), true);

		// teams
		sugar().cases.recordView.getDetailField("relTeam").assertContains("Global", true);

		// date created
		sugar().cases.recordView.getDetailField("date_entered_date").assertVisible(true);

		// date modified
		sugar().cases.recordView.getDetailField("date_modified_date").assertVisible(true);

		// tags
		sugar().cases.recordView.getDetailField("tags").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	public void cleanup() throws Exception {}
}