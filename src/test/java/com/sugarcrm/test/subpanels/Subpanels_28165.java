package com.sugarcrm.test.subpanels;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class Subpanels_28165 extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * To verify_Custom URL field should be clickable in subpanels
	 * @throws Exception
	 */
	
	@Test
	public void Subpanels_28165_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		VoodooControl contactsCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "id", "savebtn");

		// TODO: VOOD-1504
		// Go to Admin > Studio > Contacts > Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		contactsCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();

		// Add field and save
		new VoodooControl("input", "css", "#studiofields input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "type").set(customData.get("dataType"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("fieldName"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1506
		// Add created URL field to Record view
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		contactsCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();
		
		// Add one row
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		
		// Add Custom URL field
		String dataNameDraggableField = String.format("div[data-name=%s_c]",customData.get("fieldName"));
		new VoodooControl("div", "css", dataNameDraggableField).dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "publishBtn").click();   
		VoodooUtils.waitForReady();

		// TODO: VOOD-1507
		// Add created URL field to List view
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		contactsCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		VoodooControl dropHere1 =  new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "[data-name='urlfield_c']").dragNDrop(dropHere1);
		VoodooUtils.waitForReady();
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1511
		// Go to Admin > Studio > Accounts > Subpanels > Contacts
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		VoodooControl accountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl subpanelCtrl = new VoodooControl("td", "id", "subpanelsBtn");
		subpanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("table", "css", "tbody tr:nth-child(2) td:nth-child(1) table").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li","css", "[data-name='urlfield_c']").dragNDrop(dropHere1);
		VoodooUtils.waitForReady();
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Link Existing Contact in the Accounts subpanel
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel contactsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.scrollIntoView();
		contactsSubpanel.linkExistingRecord(myContact);
		sugar().contacts.navToListView();
		sugar().contacts.listView.editRecord(1);
		
		// TODO: VOOD-1036
		// Need library support for any sidecar module for newly created custom fields
		new VoodooControl("input", "css", "[name='urlfield_c']").set(customData.get("url"));
		VoodooUtils.waitForReady();
		sugar().contacts.listView.saveRecord(1);

		// Go to contact's associated account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.scrollIntoView();

		// Verify Link is clickable in subpanels
		new VoodooControl("a", "css", "[data-voodoo-name='urlfield_c'] a").assertAttribute("href", customData.get("url"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}