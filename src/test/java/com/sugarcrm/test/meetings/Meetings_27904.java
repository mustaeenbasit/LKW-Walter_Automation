package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27904 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that contact_id and Guests fields are not available in Meetings/Calls list view
	 * @throws Exception
	 */
	@Test
	public void Meetings_27904_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();

		// TODO: VOOD-938
		VoodooControl meetingsModuleCtrl = new VoodooControl("a", "id", "studiolink_Meetings");
		VoodooControl layoutPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl listViewPanelCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		
		// Click on Studio > Meetings
		meetingsModuleCtrl.click();
			
		// Click on Studio > Meetings/Calls > Layouts
		layoutPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// Click on Studio > Meetings > Layouts > List View
		listViewPanelCtrl.click();

		// Verify that contact_id and Guests are not exist. 
		new VoodooControl("li", "css", "#Hidden .draggable[data-name='contact_id']").assertExists(false);
		new VoodooControl("li", "css", "#Hidden .draggable[data-name='guests']").assertExists(false);
		sugar().admin.studio.clickStudio();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}