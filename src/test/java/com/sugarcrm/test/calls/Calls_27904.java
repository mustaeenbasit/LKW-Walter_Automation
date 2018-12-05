package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_27904 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that contact_id and Guests fields are not available in Calls list view
	 * @throws Exception
	 */
	@Test
	public void Calls_27904_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		VoodooControl callsModuleCtrl = new VoodooControl("a", "id", "studiolink_Calls");
		VoodooControl layoutPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl listViewPanelCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		
		// Click on Studio > Calls
		callsModuleCtrl.click();
		VoodooUtils.waitForReady();
			
		// Click on Studio > Calls > Layouts
		layoutPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// Click on Studio > Calls > Layouts > List View
		listViewPanelCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that contact_id and Guests do not exist in Default list
		new VoodooControl("li", "css", "#Default .draggable[data-name='contact_id']").assertExists(false);
		new VoodooControl("li", "css", "#Default .draggable[data-name='guests']").assertExists(false);

		// Verify that contact_id and Guests do not exist in Hidden list
		new VoodooControl("li", "css", "#Hidden .draggable[data-name='contact_id']").assertExists(false);
		new VoodooControl("li", "css", "#Hidden .draggable[data-name='guests']").assertExists(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}