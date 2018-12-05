package com.sugarcrm.test.grimoire;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooTag;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

import org.joda.time.DateTime;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class CallsModuleTests extends SugarTest {
	FieldSet callsFS = new FieldSet();

	public void setup() throws Exception {
		callsFS = sugar().calls.getDefaultData();
		sugar().calls.api.create();
		sugar().login();
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().leads.api.create();
		sugar().calls.navToListView();

		// Enable End Date
		sugar().calls.listView.toggleHeaderColumn("date_end");

		sugar().calls.listView.editRecord(1);

		// Verify edit field values
		// subject
		sugar().calls.listView.getEditField(1, "name").assertEquals(callsFS.get("name"), true);

		// status
		sugar().calls.listView.getEditField(1, "status").assertEquals(callsFS.get("status"), true);

		// direction
		sugar().calls.listView.getEditField(1, "direction").assertEquals(callsFS.get("direction"), true);

		// user
		sugar().calls.listView.getEditField(1, "assignedTo").assertEquals("Administrator", true);

		// start date
		sugar().calls.listView.getEditField(1, "date_start_date").assertContains(callsFS.get("date_start_date"), true);

		// start time
		sugar().calls.listView.getEditField(1, "date_start_time").assertContains(callsFS.get("date_start_time"), true);

		// end date
		sugar().calls.listView.getEditField(1, "date_end_date").assertContains(callsFS.get("date_end_date"), true);

		// end time
		sugar().calls.listView.getEditField(1, "date_end_time").assertContains(callsFS.get("date_end_time"), true);

		// created date
		sugar().calls.listView.getEditField(1, "date_created_date").assertContains(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"), true);

		// parent type
		VoodooSelect relatedToParentType = (VoodooSelect) sugar().calls.listView.getEditField(1, "relatedToParentType");
		relatedToParentType.set(sugar().leads.moduleNameSingular);
		relatedToParentType.assertEquals(sugar().leads.moduleNameSingular, true);

		// parent record
		FieldSet leadsFS = sugar().leads.getDefaultData();
		VoodooSelect relatedToParentName = (VoodooSelect) sugar().calls.listView.getEditField(1, "relatedToParentName");
		relatedToParentName.set(leadsFS.get("lastName"));
		relatedToParentName.assertContains(leadsFS.get("lastName"), true);

		// save record
		sugar().calls.listView.saveRecord(1);

		// Verify detail field values
		// subject
		sugar().calls.listView.getDetailField(1, "name").assertEquals(callsFS.get("name"), true);

		// parent name
		sugar().calls.listView.getDetailField(1, "relatedToParentName").assertContains(leadsFS.get("lastName"), true);

		// start date
		sugar().calls.listView.getDetailField(1, "date_start_date").assertContains(callsFS.get("date_start_date"), true);

		// start time
		sugar().calls.listView.getDetailField(1, "date_start_time").assertContains(callsFS.get("date_start_time"), true);

		// end date
		sugar().calls.listView.getDetailField(1, "date_end_date").assertContains(callsFS.get("date_end_date"), true);

		// end time
		sugar().calls.listView.getDetailField(1, "date_end_time").assertContains(callsFS.get("date_end_time"), true);

		// status
		sugar().calls.listView.getDetailField(1, "status").assertEquals(callsFS.get("status"), true);

		// direction
		sugar().calls.listView.getDetailField(1, "direction").assertEquals(callsFS.get("direction"), true);

		// parent name
		sugar().calls.listView.getDetailField(1, "relatedToParentName").assertEquals(leadsFS.get("fullName"), true);

		// date created
		sugar().calls.listView.getDetailField(1, "date_created_date").assertContains(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"), true);

		// user
		sugar().calls.listView.getDetailField(1, "assignedTo").assertEquals("Administrator", true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		String[] args = sugar().calls.getDefaultData().get("date_start_date").split("/");
		int monthOfYear = Integer.parseInt(args[0]);
		int dayOfMonth = Integer.parseInt(args[1]);
		int year = Integer.parseInt(args[2]);
		LocalDate date = new LocalDate(year, monthOfYear, dayOfMonth);
		String dayOfWeek = date.dayOfWeek().getAsShortText();

		String reminderVal = "5 minutes prior";
		String repeatType = "Weekly";
		String repeatInt = "3";
		String repeatOcc = "3";
		String repeatOccurType = "Until";
		String repeatOccurTypeDefault = "Occurrences";

		sugar().leads.api.create();
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.edit();

		// Verify edit field values
		// subject
		sugar().calls.recordView.getEditField("name").assertEquals(callsFS.get("name"), true);

		// status
		sugar().calls.recordView.getEditField("status").assertEquals(callsFS.get("status"), true);

		// direction
		sugar().calls.recordView.getEditField("direction").assertEquals(callsFS.get("direction"), true);

		// start date
		sugar().calls.recordView.getEditField("date_start_date").assertContains(callsFS.get("date_start_date"), true);

		// start time
		sugar().calls.recordView.getEditField("date_start_time").assertContains(callsFS.get("date_start_time"), true);

		// end date
		sugar().calls.recordView.getEditField("date_end_date").assertContains(callsFS.get("date_end_date"), true);

		// end time
		sugar().calls.recordView.getEditField("date_end_time").assertContains(callsFS.get("date_end_time"), true);

		// description
		sugar().calls.recordView.getEditField("description").assertEquals(callsFS.get("description"), true);

		// user
		sugar().calls.recordView.getEditField("assignedTo").assertEquals("Administrator", true);

		// teams
		sugar().calls.recordView.getEditField("teams").assertContains("Global", true);

		// parent type
		VoodooSelect parentType = (VoodooSelect) sugar().calls.recordView.getEditField("relatedToParentType");
		parentType.set(sugar().leads.moduleNameSingular);
		parentType.assertEquals(sugar().leads.moduleNameSingular, true);

		// parent record
		FieldSet leadsFS = sugar().leads.getDefaultData();
		VoodooSelect parentName = (VoodooSelect) sugar().calls.recordView.getEditField("relatedToParentName");
		parentName.set(leadsFS.get("lastName"));
		parentName.assertContains(leadsFS.get("lastName"), true);

		// reminder popup
		VoodooSelect remPopup = (VoodooSelect) sugar().calls.recordView.getEditField("remindersPopup");
		remPopup.set(reminderVal);
		remPopup.assertContains(reminderVal, true);

		// repeat type
		VoodooSelect repeatTypeCtrl = (VoodooSelect) sugar().calls.recordView.getEditField("repeatType");
		repeatTypeCtrl.set(repeatType);
		repeatTypeCtrl.assertContains(repeatType, true);

		// repeat interval
		VoodooSelect repeatInterval = (VoodooSelect) sugar().calls.recordView.getEditField("repeatInterval");
		repeatInterval.set(repeatInt);
		repeatInterval.assertContains(repeatInt, true);

		// repeat days of week
		sugar().calls.recordView.getEditField("repeatDays").assertVisible(true);
		new VoodooControl("a", "css", ".edit.fld_repeat_dow a").click();
		sugar().calls.recordView.getEditField("repeatDays").set(dayOfWeek);

		// repeat until
		sugar().calls.recordView.getEditField("repeatOccurType").set(repeatOccurType);
		sugar().calls.recordView.getEditField("repeatUntil").assertVisible(true);
		sugar().calls.recordView.getEditField("repeatOccurType").set(repeatOccurTypeDefault);

		// repeat occurences (Set either repeat until or repeat occurences)
		VoodooControl repeatOccur = sugar().calls.recordView.getEditField("repeatOccur");
		repeatOccur.set(repeatOcc);
		repeatOccur.assertContains(repeatOcc, true);

		// tags
		VoodooTag tag = (VoodooTag) sugar().calls.recordView.getEditField("tags");
		tag.set(testName);
		tag.assertContains(testName, true);

		// save record
		sugar().calls.recordView.save();
		sugar().alerts.closeAllSuccess();

		// Verify detail view field values
		// subject
		sugar().calls.recordView.getDetailField("name").assertEquals(callsFS.get("name"), true);

		// status
		sugar().calls.recordView.getDetailField("status").assertEquals(callsFS.get("status"), true);

		// start date
		sugar().calls.recordView.getDetailField("date_start_date").assertContains(callsFS.get("date_start_date"), true);

		// start time
		sugar().calls.recordView.getDetailField("date_start_time").assertContains(callsFS.get("date_start_time"), true);

		// end date
		sugar().calls.recordView.getDetailField("date_end_date").assertContains(callsFS.get("date_end_date"), true);

		// end time
		sugar().calls.recordView.getDetailField("date_end_time").assertContains(callsFS.get("date_end_time"), true);

		// direction
		sugar().calls.recordView.getDetailField("direction").assertEquals(callsFS.get("direction"), true);

		// parent type
		sugar().calls.recordView.getDetailField("relatedToParentType").assertEquals(sugar().leads.moduleNameSingular, true);

		// parent record
		sugar().calls.recordView.getDetailField("relatedToParentName").assertContains(leadsFS.get("lastName"), true);

		// reminder popup
		sugar().calls.recordView.getDetailField("remindersPopup").assertContains(reminderVal, true);

		// description
		sugar().calls.recordView.getDetailField("description").assertEquals(callsFS.get("description"), true);

		// user
		sugar().calls.recordView.getDetailField("assignedTo").assertEquals("Administrator", true);

		// teams
		sugar().calls.recordView.getDetailField("teams").assertContains("Global", true);

		// repeat type
		sugar().calls.recordView.getDetailField("repeatType").assertContains(repeatType, true);

		// repeat interval
		sugar().calls.recordView.getDetailField("repeatInterval").assertContains(repeatInt, true);

		// repeat days of week
		sugar().calls.recordView.getDetailField("repeatDays").assertVisible(true);

		// repeat occurences
		sugar().calls.recordView.getDetailField("repeatOccur").assertContains(repeatOcc, true);

		// tags
		sugar().calls.recordView.getDetailField("tags").assertContains(testName, true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		sugar().calls.navToListView();
		// preview record
		sugar().calls.listView.previewRecord(1);

		// Verify preview field values
		// subject
		sugar().previewPane.getPreviewPaneField("name").assertEquals(callsFS.get("name"), true);

		// status
		sugar().previewPane.getPreviewPaneField("status").assertEquals(callsFS.get("status"), true);

		// start date
		sugar().previewPane.getPreviewPaneField("date_start_date").assertContains(callsFS.get("date_start_date"), true);

		// start time
		sugar().previewPane.getPreviewPaneField("date_start_time").assertContains(callsFS.get("date_start_time"), true);

		// end date
		sugar().previewPane.getPreviewPaneField("date_end_date").assertContains(callsFS.get("date_end_date"), true);

		// end time
		sugar().previewPane.getPreviewPaneField("date_end_time").assertContains(callsFS.get("date_end_time"), true);

		// repeat type
		sugar().previewPane.getPreviewPaneField("repeatType").assertVisible(true);

		// direction
		sugar().previewPane.getPreviewPaneField("direction").assertEquals(callsFS.get("direction"), true);

		// description
		sugar().previewPane.getPreviewPaneField("description").assertEquals(callsFS.get("description"), true);

		// parent name
		sugar().previewPane.getPreviewPaneField("relatedToParentName").assertVisible(true);

		// user
		sugar().previewPane.getPreviewPaneField("assignedTo").assertEquals("Administrator", true);

		// teams
		sugar().previewPane.getPreviewPaneField("teams").assertContains("Global", true);

		// show more
		sugar().previewPane.showMore();

		// date created
		sugar().previewPane.getPreviewPaneField("date_created_date").assertContains(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"), true);

		// tags
		sugar().previewPane.getPreviewPaneField("tags").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyListHeadersWithSortIcon() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeadersWithSortIcon()...");

		sugar().calls.navToListView();
		sugar().calls.listView.toggleHeaderColumn("date_end");

		// TODO: VOOD-1768 - Once resolved "parent_name" header check should remove it from for loop
		new VoodooControl("th", "css", "th[data-fieldname=parent_name]").assertVisible(true);


		// Verify all sort headers in listview, except "parent_name" header
		for (String header : sugar().calls.listView.getHeaders()) {
			// Related to field has no sort feature
			if (!header.equals("parent_name")) {
				sugar().calls.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
			}
		}

		VoodooUtils.voodoo.log.info("verifyListHeadersWithSortIcon() complete.");
	}

	@Test
	public void sortOrderBySubject() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderBySubject()...");

		// 2 Calls having 1 with default data and another with custom data
		FieldSet secondCallData = new FieldSet();
		secondCallData.put("name", "Ashish");
		secondCallData.put("status", "Held");
		secondCallData.put("direction", "Outbound");
		CallRecord myCall = (CallRecord)sugar().calls.api.create(secondCallData);

		sugar().calls.navToListView();

		// sort by 'subject' in descending and ascending order
		sugar().calls.listView.sortBy("headerName", false);
		sugar().calls.listView.verifyField(1, "name", myCall.getRecordIdentifier());
		sugar().calls.listView.verifyField(2, "name", callsFS.get("name"));

		sugar().calls.listView.sortBy("headerName", true);
		sugar().calls.listView.verifyField(1, "name", callsFS.get("name"));
		sugar().calls.listView.verifyField(2, "name", myCall.getRecordIdentifier());

		VoodooUtils.voodoo.log.info("sortOrderBySubject() test complete.");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().calls.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().calls);

		// Verify menu items
		sugar().calls.menu.getControl("createCall").assertVisible(true);
		sugar().calls.menu.getControl("viewCalls").assertVisible(true);
		sugar().calls.menu.getControl("importCalls").assertVisible(true);
		sugar().calls.menu.getControl("activitiesReport").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().calls); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);

		// Verify subpanel
		sugar().calls.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	@Test
	public void callsUICreateforNewUser() throws Exception {
		VoodooUtils.voodoo.log.info("Running callsUICreateforNewUser()...");

		UserRecord chris = (UserRecord)sugar().users.create();
		sugar().logout();
		chris.login();
		sugar().calls.create();
		sugar().calls.listView.getDetailField(1, "name").assertEquals(callsFS.get("name"), true);

		VoodooUtils.voodoo.log.info("callsUICreateforNewUser() complete.");
	}

	@Test
	public void verifyCallsFilter() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyCallsFilter()...");

		sugar().tags.api.create();
		sugar().calls.navToListView();
		sugar().calls.listView.openFilterDropdown();
		sugar().calls.listView.selectFilterCreateNew();

		sugar().calls.listView.filterCreate.setFilterFields("name", "Subject", "exactly matches", callsFS.get("name"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().calls.listView.verifyField(1, "name", callsFS.get("name"));

		sugar().calls.listView.filterCreate.setFilterFields("status", "Status", "is any of", callsFS.get("status"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().calls.listView.verifyField(1, "status", callsFS.get("status"));

		sugar().calls.listView.filterCreate.setFilterFields("date_created_date", "Date Created", "is equal to", "10/10/2025", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().calls.listView.assertIsEmpty();

		sugar().calls.listView.filterCreate.setFilterFields("tags", "Tags", "is any of", "Tag", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().calls.listView.assertIsEmpty();

		sugar().calls.listView.filterCreate.setFilterFields("assignedTo", "Assigned to", "is any of", "qauser", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().calls.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info("verifyCallsFilter() completed.");
	}

	public void cleanup() throws Exception {}
}