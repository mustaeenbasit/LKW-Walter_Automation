package com.sugarcrm.test.leads;

import junit.framework.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_27895 extends SugarTest {
	VoodooControl leadsModuleCtrl, fieldsBtn, layoutSubPanelCtrl, recordViewSubPanelCtrl;
	FieldSet myData;

	public void setup() throws Exception {
		myData = testData.get(testName).get(0);
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * HTML field should not allow script injection
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_27895_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a new HTML field in Leads module
		// TODO: VOOD-938
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		leadsModuleCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		leadsModuleCtrl.click();
		VoodooUtils.waitForReady();
		fieldsBtn = new VoodooControl("td", "id", "fieldsBtn");
		fieldsBtn.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#type option[value='html']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("htmlarea_ifr");
		new VoodooControl("body", "id", "tinymce").set(myData.get("script"));
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "field_name_id").set(myData.get("field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		// Add field to record view
		// TODO: VOOD-938
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		leadsModuleCtrl.click();
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",myData.get("display_name"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Go to Leads record view
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		
		// Verify that Javascript alert is not displayed
		Assert.assertFalse(VoodooUtils.isDialogVisible());
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}