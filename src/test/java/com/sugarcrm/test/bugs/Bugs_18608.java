package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Bugs_18608 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().bugs.api.create();
		sugar().login();
		
		// Enable Bugs Module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
	}

	/** 
	 * Verify that Bug list view uses "option" definition for enum fields
	 * @throws Exception
	 */
	@Test
	public void Bugs_18608_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().bugs.navToListView();
		sugar().bugs.listView.verifyField(1, "priority", customData.get("priority1"));

		// Studio > Bugs > Fields 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-542
		new VoodooControl("a", "id", "studiolink_Bugs").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1504 
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();

		// Change the drop down for fields priority
		new VoodooControl("a", "id", "priority").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "options").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "[value='activity_dom']").click();
		VoodooUtils.waitForReady();
		
		// Save
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigate to Bugs List View
		sugar().bugs.navToListView();
		sugar().bugs.listView.editRecord(1);
		
		// TODO: VOOD-1425
		new VoodooControl("span", "css", ".fld_priority.edit").click();
		VoodooControl priorityCallFieldCtrl = new VoodooControl("li", "css", ".select2-results li:nth-child(1)");
		
		// Verify new drop down definition is used
		priorityCallFieldCtrl.assertEquals(customData.get("priority2"), true);
		new VoodooControl("li", "css", ".select2-results li:nth-child(1)").click();
		VoodooUtils.waitForReady();
		
		sugar().bugs.listView.cancelRecord(1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}