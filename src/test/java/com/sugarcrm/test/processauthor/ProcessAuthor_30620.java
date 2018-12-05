package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_30620 extends SugarTest {
	VoodooControl meetingsCtrl;
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		sugar().login();

		// Studio > Meetings > Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1503
		meetingsCtrl = new VoodooControl("a", "id", "studiolink_Meetings");
		meetingsCtrl.click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1504
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		// Add datetime type field
		new VoodooControl("input", "css", "[name='addfieldbtn']").click();
		VoodooUtils.waitForReady();
		customData = testData.get(testName).get(0);
		new VoodooControl("select", "css", "select#type").set(customData.get("dataType"));
		VoodooUtils.waitForReady();
		// Field Name
		new VoodooControl("input", "id", "field_name_id").set(customData.get("fieldName"));
		// Display Label
		new VoodooControl ("input", "id", "label_value_id").set(customData.get("displayLabel"));
		// Save
		new VoodooControl("input", "css", "[name='fsavebtn']").click();
		VoodooUtils.waitForReady();

		// Add datetime field to RecordView, Navigate to Studio (Footer Pane) >  Meetings > Layout
		sugar().admin.studio.clickStudio();
		meetingsCtrl.click();
		// TODO: VOOD-1506
		new VoodooControl("td", "id", "layoutsBtn").click();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels div:nth-child(2) div:nth-child(9)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("fieldName"));
		VoodooControl moveToNewFiller =	new VoodooControl("div", "css", "#panels div:nth-child(2) div:nth-child(9) div:nth-child(1)"); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFiller);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify Process Author Activity (Approval) does not resets DateTime fields
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_30620_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Process Definition, Create Process definition on Meeting module.
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create New Meeting
		sugar().navbar.selectMenuItem(sugar().meetings, "createMeeting");

		// Set Name, Start Date, End Date, Custom Datetime field.
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		FieldSet meetingData = sugar().meetings.getDefaultData();
		sugar().meetings.createDrawer.getEditField("date_start_date").set(meetingData.get("date_start_date"));
		sugar().meetings.createDrawer.getEditField("date_end_date").set(meetingData.get("date_end_date"));	
		// VOOD-1036
		new VoodooControl("input", "css", ".fld_test_datetime_c.edit .datepicker").set(meetingData.get("date_start_date"));
		sugar().meetings.createDrawer.save();

		// Navigate to Dashboard
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().home.dashboard.clickCreate();
		sugar().home.dashboard.getControl("title").set(testName);
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(1, 1);
		// TODO: VOOD-960
		new VoodooControl ("input", "css", ".inline-drawer-header.row-fluid .span4.search").set(customData.get("dashletTitle"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "tr.single .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".detail.fld_save_button[data-voodoo-name='save_button'] a").click();
		VoodooUtils.waitForReady();
		sugar().home.dashboard.save();

		// Click on pending process for Meeting
		// TODO: VOOD-670
		new VoodooControl("a", "css", ".unstyled.listed a").click();

		// On meeting record view, click on Approve button 
		// TODO: VOOD-1706
		new VoodooControl("a", "css", "[name='approve_button']").click();
		sugar().alerts.getWarning().confirmAlert();

		// Navigate to Process Management
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");

		//  Verify Process is complete under Processes > Process Management.
		sugar().processes.processManagementListView.getDetailField(1, "status").assertEquals(customData.get("processStatus"), true);

		// Verify Start Date, End Date and custom DateTime fields value on meeting has same value as before Approval.
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.getDetailField("date_start_date").assertContains(meetingData.get("date_start_date"), true);
		sugar().meetings.recordView.getDetailField("date_end_date").assertContains(meetingData.get("date_end_date"), true);
		// TODO: VOOD-1036
		new VoodooControl("span", "css", ".main-pane.span8 div:nth-child(1) div:nth-child(5) div:nth-child(1) span.fld_test_datetime_c").assertContains(meetingData.get("date_start_date"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}