package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25960_c extends SugarTest {
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
	public void Studio_25960_c_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		editViewCtrl = new VoodooControl("td", "id", "viewBtneditview");
		detailViewCtrl = new VoodooControl("td", "id", "viewBtndetailview");
		isChecked = new VoodooControl("input", "css", ".le_panel [type='checkbox'][checked]");
		studioFooterLnk = new VoodooControl("input", "css", "#footerHTML input[value='Studio']");
		
		// Verify for Project Tasks module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_ProjectTask").click();
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
		new VoodooControl("a", "id", "studiolink_ProjectTask").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		detailViewCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Projects module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Project").click();
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
		new VoodooControl("a", "id", "studiolink_Project").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		detailViewCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Quoted Line Items.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Products").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Quotes module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Quotes").click();
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
		new VoodooControl("a", "id", "studiolink_Quotes").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		detailViewCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
				
		// Verify for Revenue Line Items.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_RevenueLineItems").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Targets module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Prospects").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Tasks module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Tasks").click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		isChecked.assertExists(false);
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		
		// Verify for Users module.
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Users").click();
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
		new VoodooControl("a", "id", "studiolink_Users").click();
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
