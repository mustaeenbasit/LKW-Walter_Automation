package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_29513 extends SugarTest {
	FieldSet fs = new FieldSet();

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify Custom Textarea field can save with more than 4k string
	 * 
	 * @throws Exception
	 */
	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938		
		sugar().admin.adminTools.getControl("studio").click(); // studio
		VoodooUtils.waitForReady();
		VoodooControl accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		VoodooControl saveFieldCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		
		// Create a new text area field resume_text in Studio->Accounts
		VoodooControl fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		fieldCtrl.click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady(); 
		new VoodooControl("select", "css", "#type").set(fs.get("field_type"));
		VoodooUtils.waitForReady();
		fieldNameCtrl.set(fs.get("field_name"));
		saveFieldCtrl.click();
		VoodooUtils.waitForReady();

		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		accountsSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// layout subpanel
		VoodooControl layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();

		// Add the field to Accounts module’s detail view
		VoodooControl recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooControl rowControl = new VoodooControl("div", "css", ".le_row.special");
		VoodooControl layoutControl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)");
		rowControl.dragNDrop(layoutControl);

		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]","resume_text"); 
		VoodooControl fieldNameControl = new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel);
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		fieldNameControl.dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Go to create a new account record filling the following content (more than 4K string) to the resume_text_c field and the description field
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		// TODO: VOOD-1036
		new VoodooControl("textarea", "css", "[name='resume_text_c']").set(fs.get("field_data"));
		sugar().accounts.createDrawer.getEditField("description").set(fs.get("field_data"));
		sugar().accounts.createDrawer.save();
		sugar().accounts.listView.clickRecord(1);
		// TODO: VOOD-1036
		new VoodooControl("button", "css", ".detail.fld_resume_text_c .btn.btn-link.btn-invisible.toggle-text").click();
		// Verify Custom Textarea field can save with more than 4k string
		new VoodooControl("div", "css", ".record-cell[data-name='resume_text_c']").assertContains(fs.get("field_data"), true);
		sugar().accounts.recordView.getDetailField("description").scrollIntoViewIfNeeded(false);
		new VoodooControl("button", "css", ".detail.fld_description .btn.btn-link.btn-invisible.toggle-text").click();
		// Verify description textarea field can save with more than 4k string
		sugar().accounts.recordView.getDetailField("description").assertContains(fs.get("field_data"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {
	}
}