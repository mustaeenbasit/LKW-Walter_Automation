package com.sugarcrm.test.grimoire;

import java.util.ArrayList;
import java.util.Arrays;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooTag;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class MeetingsModuleTests extends SugarTest {
	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().login();
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().leads.api.create();
		ArrayList<String> hiddenHeaders = new ArrayList<String>(Arrays.asList("date_end", "team_name"));

		sugar().meetings.navToListView();

		// Enable End date and Team field
		sugar().meetings.listView.toggleHeaderColumns(hiddenHeaders);

		sugar().meetings.listView.editRecord(1);

		// Verify edit field values
		// subject
		sugar().meetings.listView.getEditField(1, "name").assertEquals(sugar().meetings.getDefaultData().get("name"), true);

		// status
		sugar().meetings.listView.getEditField(1, "status").assertEquals(sugar().meetings.getDefaultData().get("status"), true);

		// user
		sugar().meetings.listView.getEditField(1, "assignedTo").assertEquals("Administrator", true);

		// start date
		sugar().meetings.listView.getEditField(1, "date_start_date").assertContains(sugar().meetings.getDefaultData().get("date_start_date"), true);

		// start time
		sugar().meetings.listView.getEditField(1, "date_start_time").assertContains(sugar().meetings.getDefaultData().get("date_start_time"), true);

		// end date
		sugar().meetings.listView.getEditField(1, "date_end_date").assertContains(sugar().meetings.getDefaultData().get("date_end_date"), true);

		// end time
		sugar().meetings.listView.getEditField(1, "date_end_time").assertContains(sugar().meetings.getDefaultData().get("date_end_time"), true);

		// created date
		sugar().meetings.listView.getEditField(1, "date_created_date").assertContains(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"), true);

		// team
		sugar().meetings.listView.getEditField(1, "teams").assertVisible(true);

		// parent type
		VoodooSelect relatedToParentType = (VoodooSelect) sugar().meetings.listView.getEditField(1, "relatedToParentType");
		relatedToParentType.set(sugar().leads.moduleNameSingular);
		relatedToParentType.assertEquals(sugar().leads.moduleNameSingular, true);

		// parent record
		VoodooSelect relatedToParentName = (VoodooSelect) sugar().meetings.listView.getEditField(1, "relatedToParentName");
		relatedToParentName.set(sugar().leads.getDefaultData().get("lastName"));
		relatedToParentName.assertContains(sugar().leads.getDefaultData().get("lastName"), true);

		// save record
		sugar().meetings.listView.saveRecord(1);

		// Verify detail field values
		// subject
		sugar().meetings.listView.getDetailField(1, "name").assertEquals(sugar().meetings.getDefaultData().get("name"), true);

		// parent name
		sugar().meetings.listView.getDetailField(1, "relatedToParentName").assertContains(sugar().leads.getDefaultData().get("lastName"), true);

		// start date
		sugar().meetings.listView.getDetailField(1, "date_start_date").assertContains(sugar().meetings.getDefaultData().get("date_start_date"), true);

		// start time
		sugar().meetings.listView.getDetailField(1, "date_start_time").assertContains(sugar().meetings.getDefaultData().get("date_start_time"), true);

		// end date
		sugar().meetings.listView.getDetailField(1, "date_end_date").assertContains(sugar().meetings.getDefaultData().get("date_end_date"), true);

		// end time
		sugar().meetings.listView.getDetailField(1, "date_end_time").assertContains(sugar().meetings.getDefaultData().get("date_end_time"), true);

		// status
		sugar().meetings.listView.getDetailField(1, "status").assertEquals(sugar().meetings.getDefaultData().get("status"), true);

		// date created
		sugar().meetings.listView.getDetailField(1, "date_created_date").assertContains(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"), true);

		// user
		sugar().meetings.listView.getDetailField(1, "assignedTo").assertEquals("Administrator", true);

		// team
		sugar().meetings.listView.getDetailField(1, "teams").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");
		sugar().leads.api.create();

		String[] args = sugar().meetings.getDefaultData().get("date_start_date").split("/");
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

		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// Verify edit field values
		// subject
		sugar().meetings.recordView.getEditField("name").assertEquals(sugar().meetings.getDefaultData().get("name"), true);

		// status
		sugar().meetings.recordView.getEditField("status").assertEquals(sugar().meetings.getDefaultData().get("status"), true);

		// start date
		sugar().meetings.recordView.getEditField("date_start_date").assertContains(sugar().meetings.getDefaultData().get("date_start_date"), true);

		// start time
		sugar().meetings.recordView.getEditField("date_start_time").assertContains(sugar().meetings.getDefaultData().get("date_start_time"), true);

		// end date
		sugar().meetings.recordView.getEditField("date_end_date").assertContains(sugar().meetings.getDefaultData().get("date_end_date"), true);

		// end time
		sugar().meetings.recordView.getEditField("date_end_time").assertContains(sugar().meetings.getDefaultData().get("date_end_time"), true);

		// type
		sugar().meetings.recordView.getEditField("type").assertContains(sugar().meetings.getDefaultData().get("type"), true);

		// location
		sugar().meetings.recordView.getEditField("location").assertEquals(sugar().meetings.getDefaultData().get("location"), true);

		// description
		sugar().meetings.recordView.getEditField("description").assertEquals(sugar().meetings.getDefaultData().get("description"), true);

		// user
		sugar().meetings.recordView.getEditField("assignedTo").assertEquals("Administrator", true);

		// teams
		sugar().meetings.recordView.getEditField("teams").assertContains("Global", true);

		// parent type
		VoodooSelect parentType = (VoodooSelect) sugar().meetings.recordView.getEditField("relatedToParentType");
		parentType.set(sugar().leads.moduleNameSingular);
		parentType.assertEquals(sugar().leads.moduleNameSingular, true);

		// parent record
		VoodooSelect parentName = (VoodooSelect) sugar().meetings.recordView.getEditField("relatedToParentName");
		parentName.set(sugar().leads.getDefaultData().get("lastName"));
		parentName.assertContains(sugar().leads.getDefaultData().get("lastName"), true);

		// reminder popup
		VoodooSelect remPopup = (VoodooSelect) sugar().meetings.recordView.getEditField("remindersPopup");
		remPopup.set(reminderVal);
		remPopup.assertContains(reminderVal, true);

		// repeat type
		VoodooSelect repeatTypeCtrl = (VoodooSelect) sugar().meetings.recordView.getEditField("repeatType");
		repeatTypeCtrl.set(repeatType);
		repeatTypeCtrl.assertContains(repeatType, true);

		// repeat interval
		VoodooSelect repeatInterval = (VoodooSelect) sugar().meetings.recordView.getEditField("repeatInterval");
		repeatInterval.set(repeatInt);
		repeatInterval.assertContains(repeatInt, true);

		// repeat days of week
		sugar().meetings.recordView.getEditField("repeatDays").assertVisible(true);
		new VoodooControl("a", "css", ".edit.fld_repeat_dow a").click();
		sugar().meetings.recordView.getEditField("repeatDays").set(dayOfWeek);

		// repeat occurences (Set either repeat until or repeat occurences)
		VoodooControl repeatOccur = sugar().meetings.recordView.getEditField("repeatOccur");
		repeatOccur.set(repeatOcc);
		repeatOccur.assertContains(repeatOcc, true);

		// repeat until
		sugar().meetings.recordView.getEditField("repeatOccurType").set(repeatOccurType);
		sugar().meetings.recordView.getEditField("repeatUntil").assertVisible(true);
		sugar().meetings.recordView.getEditField("repeatOccurType").set(repeatOccurTypeDefault);

		// tag
		VoodooTag tag = (VoodooTag) sugar().meetings.recordView.getEditField("tags");
		tag.set(testName);
		tag.assertContains(testName, true);

		// save record
		sugar().meetings.recordView.save();
		sugar().alerts.closeAllSuccess();

		// Verify detail view field values
		// subject
		sugar().meetings.recordView.getDetailField("name").assertEquals(sugar().meetings.getDefaultData().get("name"), true);

		// status
		sugar().meetings.recordView.getDetailField("status").assertEquals(sugar().meetings.getDefaultData().get("status"), true);

		// start date
		sugar().meetings.recordView.getDetailField("date_start_date").assertContains(sugar().meetings.getDefaultData().get("date_start_date"), true);

		// start time
		sugar().meetings.recordView.getDetailField("date_start_time").assertContains(sugar().meetings.getDefaultData().get("date_start_time"), true);

		// end date
		sugar().meetings.recordView.getDetailField("date_end_date").assertContains(sugar().meetings.getDefaultData().get("date_end_date"), true);

		// end time
		sugar().meetings.recordView.getDetailField("date_end_time").assertContains(sugar().meetings.getDefaultData().get("date_end_time"), true);

		// parent type
		sugar().meetings.recordView.getDetailField("relatedToParentType").assertEquals(sugar().leads.moduleNameSingular, true);

		// parent record
		sugar().meetings.recordView.getDetailField("relatedToParentName").assertContains(sugar().leads.getDefaultData().get("lastName"), true);

		// reminder popup
		sugar().meetings.recordView.getDetailField("remindersPopup").assertContains(reminderVal, true);

		// description
		sugar().meetings.recordView.getDetailField("description").assertEquals(sugar().meetings.getDefaultData().get("description"), true);

		// user
		sugar().meetings.recordView.getDetailField("assignedTo").assertEquals("Administrator", true);

		// teams
		sugar().meetings.recordView.getDetailField("teams").assertContains("Global", true);

		// type
		sugar().meetings.recordView.getDetailField("type").assertContains(sugar().meetings.getDefaultData().get("type"), true);

		// location
		sugar().meetings.recordView.getDetailField("location").assertEquals(sugar().meetings.getDefaultData().get("location"), true);

		// repeat type
		sugar().meetings.recordView.getDetailField("repeatType").assertContains(repeatType, true);

		// repeat interval
		sugar().meetings.recordView.getDetailField("repeatInterval").assertContains(repeatInt, true);

		// repeat days of week
		sugar().meetings.recordView.getDetailField("repeatDays").assertVisible(true);

		// repeat occurences
		sugar().meetings.recordView.getDetailField("repeatOccur").assertContains(repeatOcc, true);

		// tags
		sugar().meetings.recordView.getDetailField("tags").assertContains(testName, true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		sugar().meetings.navToListView();
		// preview record
		sugar().meetings.listView.previewRecord(1);

		// Verify preview field values
		// subject
		sugar().previewPane.getPreviewPaneField("name").assertEquals(sugar().meetings.getDefaultData().get("name"), true);

		// status
		sugar().previewPane.getPreviewPaneField("status").assertEquals(sugar().meetings.getDefaultData().get("status"), true);

		// start date
		sugar().previewPane.getPreviewPaneField("date_start_date").assertContains(sugar().meetings.getDefaultData().get("date_start_date"), true);

		// start time
		sugar().previewPane.getPreviewPaneField("date_start_time").assertContains(sugar().meetings.getDefaultData().get("date_start_time"), true);

		// end date
		sugar().previewPane.getPreviewPaneField("date_end_date").assertContains(sugar().meetings.getDefaultData().get("date_end_date"), true);

		// end time
		sugar().previewPane.getPreviewPaneField("date_end_time").assertContains(sugar().meetings.getDefaultData().get("date_end_time"), true);

		// repeat type
		sugar().previewPane.getPreviewPaneField("repeatType").assertVisible(true);

		// location
		sugar().previewPane.getPreviewPaneField("location").assertEquals(sugar().meetings.getDefaultData().get("location"), true);

		// description
		sugar().previewPane.getPreviewPaneField("description").assertEquals(sugar().meetings.getDefaultData().get("description"), true);

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

		sugar().meetings.navToListView();

		// Enable End date and Team field
		ArrayList<String> hiddenHeaders = new ArrayList<String>(Arrays.asList("date_end", "team_name"));
		sugar().meetings.listView.toggleHeaderColumns(hiddenHeaders);

		// TODO: VOOD-1768 - Once resolved "parent_name" header check should remove it from for loop
		new VoodooControl("th", "css", "th[data-fieldname=parent_name]").assertVisible(true);

		// Verify all sort headers in listview, except "parent_name" header
		for (String header : sugar().meetings.listView.getHeaders()) {
			if (!header.equals("parent_name")) // Related to field has no sort feature
				sugar().meetings.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeadersWithSortIcon() test complete.");
	}

	@Test
	public void sortOrderByAssignedTo() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByAssignedTo()...");

		// 2 meetings having 1 with default data and another with custom data
		FieldSet secondMeetingData = new FieldSet();
		secondMeetingData.put("name", "Ashish");
		secondMeetingData.put("status", "Held");
		sugar().meetings.api.create(secondMeetingData);

		// Edit and Save record
		sugar().meetings.navToListView();
		sugar().meetings.listView.editRecord(1);

		// TODO: VOOD-1330, VOOD-1371 - workaround on more appropriate scroll. Once fixed we need to change below line#22
		sugar().meetings.listView.getEditField(1, "assignedTo").scrollIntoViewIfNeeded(sugar().meetings.listView.getControl("horizontalScrollBar"), false);
		sugar().meetings.listView.getEditField(1, "assignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().meetings.listView.saveRecord(1);

		// sort by 'assigned to' in descending and ascending order
		sugar().meetings.listView.sortBy("headerAssignedusername", false);
		sugar().meetings.listView.verifyField(1, "assignedTo", sugar().users.getQAUser().get("userName"));
		sugar().meetings.listView.verifyField(2, "assignedTo", "Administrator");

		sugar().meetings.listView.sortBy("headerAssignedusername", true);
		sugar().meetings.listView.verifyField(1, "assignedTo", "Administrator");
		sugar().meetings.listView.verifyField(2, "assignedTo", sugar().users.getQAUser().get("userName"));

		VoodooUtils.voodoo.log.info("sortOrderByAssignedTo() test complete.");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().meetings.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().meetings);

		// Verify menu items
		sugar().meetings.menu.getControl("createMeeting").assertVisible(true);
		sugar().meetings.menu.getControl("viewMeetings").assertVisible(true);
		sugar().meetings.menu.getControl("importMeetings").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().meetings); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}


	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);

		// Verify subpanel
		sugar().meetings.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	@Test
	public void verifyMeetingsFilter() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMeetingsFilter()...");

		sugar().tags.api.create();
		sugar().meetings.navToListView();
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterCreateNew();

		sugar().meetings.listView.filterCreate.setFilterFields("name", "Subject", "exactly matches", sugar().meetings.getDefaultData().get("name"), 1);
		VoodooUtils.waitForReady();
		sugar().meetings.listView.verifyField(1, "name", sugar().meetings.getDefaultData().get("name"));

		sugar().meetings.listView.filterCreate.setFilterFields("status", "Status", "is any of", sugar().meetings.getDefaultData().get("status"), 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().meetings.listView.verifyField(1, "status", sugar().meetings.getDefaultData().get("status"));

		sugar().meetings.listView.filterCreate.setFilterFields("type", "Meeting Type", "is any of", "Sugar", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().meetings.listView.verifyField(1, "name", sugar().meetings.getDefaultData().get("name"));

		sugar().meetings.listView.filterCreate.setFilterFields("tags", "Tags", "is any of", "Tag", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().meetings.listView.assertIsEmpty();

		sugar().meetings.listView.filterCreate.setFilterFields("assignedTo", "Assigned to", "is any of", "qauser", 1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().meetings.listView.assertIsEmpty();
		sugar().meetings.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info("verifyMeetingsFilter() completed.");
	}

	public void cleanup() throws Exception {}
}