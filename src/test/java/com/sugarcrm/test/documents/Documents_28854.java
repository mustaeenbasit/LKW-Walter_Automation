package com.sugarcrm.test.documents;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Documents_28854 extends SugarTest {
	public void setup() throws Exception {
		sugar().documents.api.create();
		sugar().login();

		// Go to studio -> Documents -> Layouts -> List view
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "studiolink_Documents").click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-542
		new VoodooControl("td", "id", "layoutsBtn").click();
		new VoodooControl("td", "id", "viewBtnlistview").click();

		// Add status field to List view
		// TODO: VOOD-542
		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		defaultSubPanelCtrl.waitForVisible();
		new VoodooControl("li", "css", "[data-name='status_id']").dragNDrop(defaultSubPanelCtrl);
		VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that data is displayed in the Status field under Documents module list view
	 *
	 * @throws Exception
	 */
	@Test
	public void Documents_28854_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Documents Listview
		sugar().documents.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// In the list view, Verify the data in 'Status' field
		// TODO: VOOD-1980, used xpath as position of the column is not fixed in CI and local
		new VoodooControl("td", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[3]/td[contains(.,'Active')]").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}