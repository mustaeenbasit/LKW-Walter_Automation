package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_28804 extends SugarTest {
	VoodooControl planned, inputPlanned, held, inputHeld, saveBtn, callStatusDom;
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		sugar().calls.api.create();
		sugar().login();
	}

	/**
	 * Verify that the Value of Status Field is displayed correctly after Modifying the dropdown values through Studio
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_28804_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		customData = testData.get(testName).get(0);
		
		// Go to Admin -> Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-781 -need lib support of dropdown editor functions
		sugar.admin.studio.getControl("dropdownEditorButton").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-781
		planned = new VoodooControl("a", "css", "#Planned td:nth-child(2) a:nth-child(1)");
		inputPlanned = new VoodooControl("input", "id", "input_Planned");
		held = new VoodooControl("a", "css", "#Held td:nth-child(2) a:nth-child(1)");
		inputHeld = new VoodooControl("input", "id", "input_Held");
		saveBtn = new VoodooControl("input", "id", "saveBtn");
		callStatusDom = new VoodooControl("a", "css", "#dropdowns tr:nth-child(5) td:nth-child(1) a");
		
		// Select call_status_dom
		callStatusDom.click();
				
		// Change "Planned" to "Planned/Scheduled" and "Held" to "Held/HeldTesting"
		planned.click();
		inputPlanned.set(customData.get("scheduled"));
		held.click();
		inputHeld.set(customData.get("heldTesting"));
		
		// Click save button
		saveBtn.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Go to Calls listview 
		sugar().calls.navToListView();
		sugar().calls.listView.editRecord(1);
		sugar().calls.listView.getEditField(1, "status").click();
		
		// Verify that the Value of the "Status" field should be displayed as per the modification done
		// TODO: VOOD-1463
		new VoodooControl("div", "css", "#select2-drop ul li:nth-child(1) div").assertContains(customData.get("scheduled"), true);
		new VoodooControl("div", "css", "#select2-drop ul li:nth-child(2) div").assertContains(customData.get("heldTesting"), true);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}