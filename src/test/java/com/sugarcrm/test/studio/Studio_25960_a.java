package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25960_a extends SugarTest {
	VoodooControl layoutSubPanelCtrl, recordViewSubPanelCtrl, editViewCtrl, detailViewCtrl, isChecked, studioFooterLnk;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the default for the Collapse checkbox.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_25960_a_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		editViewCtrl = new VoodooControl("td", "id", "viewBtneditview");
		detailViewCtrl = new VoodooControl("td", "id", "viewBtndetailview");
		isChecked = new VoodooControl("input", "css", ".le_panel [type='checkbox'][checked]");
		studioFooterLnk = new VoodooControl("input", "css", "#footerHTML input[value='Studio']");
		
		// Verify for Accounts module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Bugs module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Bugs").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Calls module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Calls").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Campaigns module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Campaigns").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		// Verify Editview
		editViewCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify Detailview
		new VoodooControl("a", "id", "studiolink_Campaigns").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		detailViewCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Cases module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Cases").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Contacts module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Contacts").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Contracts module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Contracts").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		// Verify Editview
		editViewCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify Detailview
		new VoodooControl("a", "id", "studiolink_Contracts").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		detailViewCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
