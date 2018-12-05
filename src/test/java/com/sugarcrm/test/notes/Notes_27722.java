package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Notes_27722 extends SugarTest {
	VoodooControl notesSubPanelCtrl;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify custom field is working properly in Notes sub-panel
	 * @throws Exception
	 */
	@Test
	public void Notes_27722_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		VoodooControl layoutSubPanelCtrl, recordViewSubPanelCtrl, fieldCtrl;
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Add a custom field in Notes module and save
		// TODO: VOOD-1504
		sugar().admin.adminTools.getControl("studio").click();
		notesSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Notes");
		VoodooUtils.waitForReady();
		notesSubPanelCtrl.click();
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		fieldCtrl.click();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		sugar().admin.studio.clickStudio();
		notesSubPanelCtrl.click();

		// TODO: VOOD-1511
		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();

		// TODO: VOOD-1506
		// Add custom field into Notes record layout.
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		VoodooControl moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",customData.get("display_name"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		sugar().admin.studio.clickStudio();
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "subpanelsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#Buttons tr:nth-child(1) td:nth-child(4) tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1511
		// Studio -> Accounts > Subpanels > Notes, Add custom field in subpanel
		VoodooControl defaultSubPanelCtrl = new VoodooControl("li", "css", "#Default li:nth-child(1)");
		String dataNameDraggableLi = String.format("li[data-name=%s]",customData.get("display_name"));
		VoodooControl customDataDragNdrop = new VoodooControl("li", "css", dataNameDraggableLi);
		customDataDragNdrop.scrollIntoViewIfNeeded(false);
		customDataDragNdrop.dragNDropViaJS(defaultSubPanelCtrl);
		VoodooUtils.waitForReady();
		VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Create a record in Notes Sub panel and fill custom field.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel notesSubpanel = sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.addRecord();
		sugar().notes.createDrawer.getEditField("subject").set(customData.get("notes_subject"));
		String dataName = String.format("[name=%s]",customData.get("display_name")); 
		new VoodooControl("input", "css", dataName).set(customData.get("myvalue"));
		sugar().notes.createDrawer.save();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify custom field under notes subpanel.
		notesSubpanel.expandSubpanel();
		new VoodooControl("div", "css", ".layout_Notes .list.fld_myfield_c div").assertContains(customData.get("myvalue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}