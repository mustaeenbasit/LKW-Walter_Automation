package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25960_b extends SugarTest {
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
	public void Studio_25960_b_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		editViewCtrl = new VoodooControl("td", "id", "viewBtneditview");
		detailViewCtrl = new VoodooControl("td", "id", "viewBtndetailview");
		isChecked = new VoodooControl("input", "css", ".le_panel [type='checkbox'][checked]");
		studioFooterLnk = new VoodooControl("input", "css", "#footerHTML input[value='Studio']");
		
		// Verify for Documents module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Documents").click();
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
		new VoodooControl("a", "id", "studiolink_Documents").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		detailViewCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Employees module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Employees").click();
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
		new VoodooControl("a", "id", "studiolink_Employees").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		detailViewCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();

		// Verify for Leads module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Leads").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Meetings module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Meetings").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Notes module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Notes").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Opportunities module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Opportunities").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Product Catalog module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_ProductTemplates").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
