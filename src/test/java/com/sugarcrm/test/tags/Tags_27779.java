package com.sugarcrm.test.tags;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Tags_27779 extends SugarTest {
	DataSource tagData = new DataSource();
	VoodooControl accountsCtrl, studioCtrl, saveBtnCtrl, subpanelsCtrl, contactSubpanelCtrl;
	ArrayList<Record> myContacts = new ArrayList<Record>();

	public void setup() throws Exception {
		sugar.accounts.api.create();
		tagData = testData.get(testName);
		sugar.tags.api.create(tagData);
		DataSource contactRecords = new DataSource();
		contactRecords = testData.get(testName + "_contactData");
		myContacts = sugar.contacts.api.create(contactRecords);
		sugar.login();

		// Navigate to Contacts module
		sugar.contacts.navToListView();
		// Add multiple tags to contact records
		sugar.contacts.listView.toggleSelectAll();
		sugar.contacts.listView.openActionDropdown();
		sugar.contacts.listView.massUpdate();

		// TODO: VOOD-1003
		sugar().contacts.massUpdate.getControl("massUpdateField02").set("Tags");
		VoodooControl massUpdateTagDataCtrl = new VoodooControl("input", "css", ".filter-value.controls div.span9 > div > ul input");
		VoodooControl tagNameCtrl = new VoodooControl("li", "css", "#select2-drop ul li:nth-child(1)");
		for (int i = 0; i < tagData.size(); i++) {
			massUpdateTagDataCtrl.set(tagData.get(i).get("name"));
			VoodooUtils.waitForReady();
			tagNameCtrl.click();
		}
		sugar.contacts.massUpdate.update();
	}

	/**
	 * Verify ability to view tags associated to a record on sidecar module subpanel
	 *
	 * @throws Exception
	 */
	@Test
	public void Tags_27779_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to admin
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar.admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate to Accounts -> Subpanels -> Contacts in studio
		// TODO: VOOD-1504 - Support Studio Module Fields View
		accountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsCtrl.click();
		subpanelsCtrl = new VoodooControl("td", "id", "subpanelsBtn");
		subpanelsCtrl.click();
		contactSubpanelCtrl = new VoodooControl("table", "css", "tbody tr:nth-child(2) td:nth-child(1) table");
		contactSubpanelCtrl.click();

		// Move tags field from "Hidden" to "Default"
		new VoodooControl("li", "css", ".draggable[data-name='tag']").dragNDrop(new VoodooControl("li", "css", ".draggable[data-name='primary_address_state']"));

		// Save & Deploy
		saveBtnCtrl = new VoodooControl("input", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.focusDefault();

		// Navigate to Accounts module
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		VoodooUtils.waitForReady();
		// Link Existing contact records to account record.
		StandardSubpanel contactsSubpanel = sugar.accounts.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		contactsSubpanel.linkExistingRecords(myContacts);

		// Observe the tags column under Contacts Subpanel,  list of tags associated to a record
		// under subpanel list view column for any sidecar modules. 
		// TODO: VOOD-1380
		VoodooControl tagDataCtrl = new VoodooControl("div", "css", ".list.fld_tag .ellipsis_inline");
		for (int i = 0; i < tagData.size(); i++) {
			tagDataCtrl.hover();
			VoodooUtils.waitForReady();
			
			// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
			new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(tagData.get(i).get("name"), true);
			VoodooUtils.waitForReady();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}