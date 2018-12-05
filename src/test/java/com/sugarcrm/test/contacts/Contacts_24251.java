package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Contacts_24251 extends SugarTest {
	TargetListRecord myTargetList;
	VoodooControl studio,resetButtonCtrl,resetClickCtrl,relationshipsCtrl,fieldsCtrl,labelsCtrl,layoutsCtrl,extensionsCtrl,contactsCtrl;
	FieldSet customData = new FieldSet();
	FieldSet contactData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		myTargetList = (TargetListRecord) sugar().targetlists.api.create();
		sugar().contacts.api.create();
		contactData.put("firstName", customData.get("first_name"));
		contactData.put("lastName", customData.get("last_name"));
		contactData.put("fullName", customData.get("fullName"));
		sugar().contacts.api.create(contactData);

		// Controls for Studio
		contactsCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");
		sugar().login();

	}

	/**
	 * Verify that it's possible to add records with custom field selected by button_select_all to Target list
	 * @throws Exception
	 */

	@Test
	public void Contacts_24251_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Add a custom text field to the contacts module
		// TODO: VOOD-1504
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studio = sugar().admin.adminTools.getControl("studio");
		studio.click();
		VoodooUtils.waitForReady();
		contactsCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		sugar().admin.studio.clickStudio();
		contactsCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate to layout
		// TODO: VOOD-1511
		VoodooControl layoutCtrl =  new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate to List view in Studio and add the custom field to Default
		// TODO: VOOD-1507
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		String dataNameDraggableLi = String.format("li[data-name=%s_c]",customData.get("module_field_name"));
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
		new VoodooControl("td", "id", "savebtn").click();
		sugar().admin.studio.clickStudio();
		contactsCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate to Record View in Studio and add the custom field
		// TODO: VOOD-1506
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		VoodooControl moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		VoodooUtils.waitForReady();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		sugar().admin.studio.clickStudio();
		VoodooUtils.focusDefault();

		// Edit contact with custom field & verify after save
		for (int i = 0; i < 2; i++) {
			sugar().contacts.navToListView();
			sugar().contacts.listView.clickRecord(i+1);
			sugar().contacts.recordView.edit();

			// TODO: VOOD-1036 Need library support for any sidecar module for newly created custom fields
			new VoodooControl("input", "css", ".fld_my_field_c.edit input").set(customData.get("field_"+(i+1)+""));
			sugar().contacts.recordView.save();
			new VoodooControl("div", "css", ".fld_my_field_c.detail div").assertEquals(customData.get("field_"+(i+1)+""), true);
		}

		// Add both contacts to a target list
		sugar().contacts.navToListView();
		sugar().contacts.listView.toggleSelectAll();
		sugar().contacts.listView.openActionDropdown();

		// TODO: VOOD-528 Adding lib support for the "Add To Target List" menu item and pane
		new VoodooControl("div", "css", ".fld_addtolist_button a").click();
		new VoodooSelect("a", "css", ".fld_prospect_lists_name div a").set(myTargetList.getRecordIdentifier());
		new VoodooControl("a", "css", ".fld_update_button a").click();
		VoodooUtils.waitForReady();

		// Verify that the both contacts are exist in contacts subpanel of target list
		myTargetList.navToRecord();
		StandardSubpanel contactsSubPanel = sugar().targetlists.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubPanel.expandSubpanel();
		contactsSubPanel.assertContains(sugar().contacts.getDefaultData().get("fullName"), true);
		contactsSubPanel.assertContains(contactData.get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
