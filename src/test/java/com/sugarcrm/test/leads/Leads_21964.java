package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21964 extends SugarTest {

	public void setup() throws Exception {
		// Create a contact to whom the lead would report
		sugar().contacts.api.create();

		// Login as an Admin
		sugar().login();

		// Go to admin -> studio -> leads -> Layout.   
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1506 - Support Studio Module RecordView Layouts View
		VoodooControl studioLeadsCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		VoodooControl studioLeadsLayoutCtrl = new VoodooControl("td", "id", "layoutsBtn");

		studioLeadsCtrl.click();
		VoodooUtils.waitForReady();
		studioLeadsLayoutCtrl.click();
		new VoodooControl("td", "id", "viewBtnrecordview").click();

		// Add "Reports To" field to "RecordView"
		new VoodooControl("div", "css", ".le_row.special").dragNDrop(new VoodooControl("div", "css", ".le_panel .le_row:nth-of-type(2)"));
		new VoodooControl("div", "css", "div[data-name='report_to_name']").dragNDrop(new VoodooControl("span", "css", ".le_panel .le_row:nth-of-type(3) span"));
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();

		// Admin goes to Studio->Leads->Layouts->List View
		sugar().admin.studio.clickStudio();
		studioLeadsCtrl.click();
		VoodooUtils.waitForReady();
		studioLeadsLayoutCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1507 - Support Studio Module ListView Layouts View
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();

		// Add "Reports To" field to "ListView"
		new VoodooControl("li", "css", "#Hidden li[data-name='report_to_name']").dragNDrop(new VoodooControl("li", "css", "#Default li[data-name='status']"));

		// Save and Deploy
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Edit Leads_Verify that "Reports To" is displayed in Lead RecordView and ListView
	 * @throws Exception
	 */
	@Test
	public void Leads_21964_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet contactsDefaultData = sugar().contacts.getDefaultData();
		String contactFullName = contactsDefaultData.get("fullName");

		// Click "Create Lead" link in navigation shortcut
		sugar().navbar.selectMenuItem(sugar().leads, "createLead");

		// Fill all required fields 
		sugar().leads.createDrawer.getEditField("lastName").set(sugar().leads.getDefaultData().get("lastName"));

		// Assert that the "Reports To" field is displayed in Record View and fill it
		// TODO: VOOD-1489 - Need Library Support for All fields moved from Hidden to Default & vice versa for All Modules
		new VoodooSelect("span", "css", ".fld_report_to_name.edit").set(contactsDefaultData.get("firstName"));

		// Click the Save button
		sugar().leads.createDrawer.save();

		// Assert that the "Reports To" field is displayed in List View
		new VoodooControl("span", "css", ".list.fld_report_to_name").assertEquals(contactFullName, true);

		// Navigate to the record view of the Lead
		sugar().leads.listView.clickRecord(1);

		// Assert that the "Reports To" field is displayed in Record View
		new VoodooControl("span", "css", ".fld_report_to_name.detail").assertEquals(contactFullName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}