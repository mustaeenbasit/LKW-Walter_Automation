package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25572 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-542
		// Go to Studio > Accounts > Fields   
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#studiofields > input:nth-child(1)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set("Custom");
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Delete Custom Filed_Verify that the created custom field is deleted
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_25572_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-542
		// Go to Studio > Accounts > Fields 
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "custom_c").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='fdeletebtn']").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady();
		
		// Verify that the created custom field is deleted
		new VoodooControl("div", "id", "field_table").assertContains("Custom", false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}