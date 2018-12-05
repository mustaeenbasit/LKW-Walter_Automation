package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_28132 extends SugarTest {
	VoodooControl moduleCtrl;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Record is successfully saved when custom set a required field and "Date Entered" field cannot be set as "Required".  
	 * @throws Exception
	 */
	@Test
	public void Studio_28132_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Studio > Targets  > Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Prospects");
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();

		// Verify the 'Date Entered' field
		new VoodooControl("a", "id", "date_entered").assertVisible(true);
		VoodooUtils.waitForReady();

		// Set any field to be required, eg. birthdate or office phone (Taking Office Phone as this field is already in layout)
		new VoodooControl("a", "id", "phone_work").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name='required']").click();

		// Save & Deploy
		new VoodooControl("input", "css", "input[name='fsavebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to the Target module. Create a target.
		sugar().targets.navToListView();
		sugar().targets.listView.create();

		// Enter a name and Save
		sugar().targets.createDrawer.getEditField("lastName").set(testName);
		sugar().targets.createDrawer.getControl("saveButton").click();

		// Verify that the Target Record is not saved. Red error for the custom required field. 
		sugar().alerts.getError().assertVisible(true);
		sugar().alerts.getError().closeAlert();
		sugar().targets.createDrawer.getEditField("phoneWork").assertAttribute("class","required");

		// Enter value in the required field (Office Phone) and Save.
		sugar().targets.createDrawer.getEditField("phoneWork").set(sugar().targets.getDefaultData().get("phoneWork"));
		sugar().targets.createDrawer.save();

		// Verify that the Target record should save successfully.
		sugar().targets.listView.verifyField(1, "fullName", testName);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}