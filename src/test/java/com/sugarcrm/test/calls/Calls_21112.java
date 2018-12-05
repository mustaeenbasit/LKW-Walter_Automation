package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21112 extends SugarTest {
	VoodooControl moduleCtrl, studioCtrl;
	DataSource contactsData = new DataSource();

	public void setup() throws Exception {
		contactsData = testData.get(testName + "_" + sugar().contacts.moduleNamePlural);

		// Create two Contact records
		sugar().contacts.api.create(contactsData);

		// Login
		sugar().login();
	}

	/**
	 * Contact field does work properly after adding it to the Call Edit View layout  
	 * @throws Exception
	 */
	@Test
	public void Calls_21112_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fieldData = testData.get(testName).get(0);

		// Go to Admin -> Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// Go to Calls -> Fields
		// TODO: VOOD-542
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Calls");
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();

		// Add a Relate filed, set the Module to Contacts
		// TODO: VOOD-1504
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "select#type").set(fieldData.get("dataType"));
		VoodooUtils.waitForReady();
		String fieldName = fieldData.get("fieldName");
		new VoodooControl("input", "id", "field_name_id").set(fieldName);
		new VoodooControl("select", "css", "#popup_form_id > table:nth-child(13) #ext2").set(sugar().contacts.moduleNamePlural);
		new VoodooControl("input", "css", "input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();

		// Add this field to EditView
		// TODO: VOOD-1506
		sugar().admin.studio.clickStudio();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		new VoodooControl("div", "css", "div[data-name=" + fieldName + "_c]").dragNDrop(moveToNewFilter);

		// Save & Deploy
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to the Calls module and log a Call
		sugar().calls.navToListView();
		sugar().calls.listView.create();

		// Define Edit and Detail Controls for custom created field
		// TODO: VOOD-1036
		VoodooSelect customEditFieldCtrl = new VoodooSelect("div", "css", ".fld_" + fieldName + "_c.edit div");
		VoodooSelect customDeatilFieldCtrl = new VoodooSelect("div", "css", ".fld_" + fieldName + "_c.detail a");

		// Select a contact from the Select button and enter other required data
		customEditFieldCtrl.set(contactsData.get(0).get("lastName"));

		// Enter other required data and Save
		sugar().calls.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Go to record view of the created Call record
		sugar().calls.listView.clickRecord(1);

		// Verify that the Contact name is the selected one
		customDeatilFieldCtrl.assertContains(contactsData.get(0).get("lastName"), true);

		// Edit the call, change the contact to another contact and Save
		sugar().calls.recordView.edit();
		customEditFieldCtrl.set(contactsData.get(1).get("lastName"));
		sugar().calls.recordView.save();

		// Verify that the Contact name is the selected one
		customDeatilFieldCtrl.assertContains(contactsData.get(1).get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}