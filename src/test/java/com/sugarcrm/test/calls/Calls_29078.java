package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_29078 extends SugarTest{
	VoodooControl callStudioCtrl, studioLink;
	FieldSet customFieldData = new FieldSet();
	String fieldNameLwrCase = "";

	public void setup() throws Exception {
		customFieldData = testData.get(testName).get(0);
		fieldNameLwrCase = String.format("%s%s", customFieldData.get("fieldName").toLowerCase(), "_c");

		// Log-in as an Administrator
		sugar().login();

		// Navigate to the Studio page
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioLink = sugar().admin.adminTools.getControl("studio");
		studioLink.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1504 - Support Studio Module Fields View
		// Add a custom dropdown type field in Calls module
		callStudioCtrl = new VoodooControl("a", "id", "studiolink_Calls");
		callStudioCtrl.click();
		// Click the Fields tab
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		// Click the "Add Field" button
		new VoodooControl("input", "css", "#studiofields input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		// Select Data Type = DropDown
		new VoodooControl("select", "id", "type").set(customFieldData.get("fieldType"));
		VoodooUtils.waitForReady();
		// Add Field Name
		new VoodooControl("input", "id", "field_name_id").set(customFieldData.get("fieldName"));
		VoodooUtils.waitForReady();
		// Set dependent = Parent drop down and Parent drop down = Status
		new VoodooControl("select", "id", "depTypeSelect").set(customFieldData.get("dependentType"));
		new VoodooControl("select", "id", "parent_dd").set(customFieldData.get("dependentOnStatus"));
		// Click on Edit Visibility
		new VoodooControl("button", "css", "#visGridRow button").click();
		VoodooUtils.waitForReady();
		// Drag and drop Analyst to the Scheduled, Competitor to the Held 
		new VoodooControl("li", "css", "#childTable li[val='" + customFieldData.get("dependentValueAnalyst") + "']").
		dragNDropViaJS(new VoodooControl("ul", "css", ".bd div:nth-of-type(3) #ddd_Planned_list"));
		new VoodooControl("li", "css", "#childTable li[val='" + customFieldData.get("dependentValueCompetitor") + "']").
		dragNDropViaJS(new VoodooControl("ul", "css", ".bd div:nth-of-type(3) #ddd_Held_list"));
		// Click on the Save button
		new VoodooControl("button", "css", ".bd div:nth-of-type(4) button:nth-of-type(2)").click();
		VoodooUtils.waitForReady();
		// Again click on Save
		new VoodooControl("input", "css", "input[name='fsavebtn']").click();
		VoodooUtils.waitForReady();
		// Click on the Calls crumblink
		new VoodooControl("a", "css", ".bodywrapper a:nth-child(4)").click();
		VoodooUtils.waitForReady();

		// Add field to both Record View and List View layouts.
		// Click the Layouts icon
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1506 - Support Studio Module RecordView Layouts View
		// Add the custom field in the Record view of Calls
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();
		// Inserting a new row in the record view layout
		new VoodooControl("div", "css", "#toolbox .le_row").
		dragNDrop(new VoodooControl("div", "css", "#panels div:nth-of-type(1).le_panel"));
		// Drag and drop the custom field in the filler
		new VoodooControl("div", "css", "#toolbox div[data-name='" + fieldNameLwrCase + "']").
		dragNDrop(new VoodooControl("div", "css", ".le_row div:nth-child(1).le_field.special"));
		// Save and Deploy
		new VoodooControl("input", "css", "#layoutEditorButtons #publishBtn").click();
		VoodooUtils.waitForReady();
		// Click the layouts crumblink
		new VoodooControl("a", "css", ".bodywrapper a:nth-child(5)").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1507 - Support Studio Module ListView Layouts View
		// Add the custom field in the List View layout
		// Click the list view icon
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		// Drag and drop the custom field from Hidden to Default
		new VoodooControl("li", "css", "#Hidden li[data-name='" + fieldNameLwrCase + "']").
		dragNDropViaJS(new VoodooControl("li", "css", "#Default li[data-name='parent_name']"));
		// Click the save button
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();

		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that detail view shows new field in layout in calls module
	 * @throws Exception
	 */
	@Test
	public void Calls_29078_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1036 - Need library support for Accounts/any sidecar module for newly created custom fields
		VoodooControl customEditField = new VoodooControl("div", "css", ".layout_Calls.drawer.active .fld_" + fieldNameLwrCase + ".edit div");
		VoodooControl statusEditField = sugar().calls.createDrawer.getEditField("status");

		// Navigate to the Calls list view and click the create button
		sugar().navbar.navToModule(sugar().calls.moduleNamePlural);
		sugar().calls.listView.create();

		// Assert that the New field displays
		customEditField.assertVisible(true);

		// Assert that the custom field = Analyst when call Status = Scheduled
		statusEditField.assertEquals(customFieldData.get("statusScheduled"), true);
		customEditField.assertEquals(customFieldData.get("dependentValueAnalyst"), true);

		// Assert that on changing the call status to "Held", the custom field = Competitor
		statusEditField.set(customFieldData.get("statusHeld"));
		customEditField.assertEquals(customFieldData.get("dependentValueCompetitor"), true);

		// Enter the Call name and save it
		sugar().calls.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.save();

		// Assert that the call Status = Held and custom field = Competitor in the List View
		new VoodooControl("span", "css", ".list.fld_" + fieldNameLwrCase).assertEquals(customFieldData.get("dependentValueCompetitor"), true);
		sugar().calls.listView.getDetailField(1, "status").assertEquals(customFieldData.get("statusHeld"), true);

		// Navigate to the call record view
		sugar().calls.listView.clickRecord(1);

		// Assert that the call Status = Held and custom field = Competitor in the Record View
		sugar().calls.recordView.getDetailField("status").assertEquals(customFieldData.get("statusHeld"), true);
		new VoodooControl("div", "css", ".layout_Calls .detail.fld_" + fieldNameLwrCase + " div").
		assertEquals(customFieldData.get("dependentValueCompetitor"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}