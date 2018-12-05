package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25726 extends SugarTest {
	VoodooControl layoutCtrl;
	VoodooControl recordViewSubPanelCtrl, accountsSubPanelCtrl;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Studio_History_Listed_Date&time
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_25726_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubPanelCtrl.click();

		// TODO: VOOD-938
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();

		// TODO: VOOD-938
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();

		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		new VoodooControl("div", "css", "div[data-name='billing_address']").dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();

		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		new VoodooControl("div", "css", "div[data-name='phone_alternate']").dragNDrop(moveToNewFilter);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();

		String currentTimeStamp = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");

		// Verify that the history of layout saved is listed by date&time
		new VoodooControl("td", "id", "historyBtn").click();
		new VoodooControl("td", "css", "#histWindow .mbLBLL").assertExists(false);
		new VoodooControl("a", "css", ".tabform tr:nth-child(2) td:nth-child(1) a").assertContains(currentTimeStamp, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
