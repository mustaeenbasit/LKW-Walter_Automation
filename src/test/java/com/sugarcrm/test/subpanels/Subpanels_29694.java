package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_29694 extends SugarTest {
	ContactRecord myContactsRecord;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		myContactsRecord = (ContactRecord) sugar().contacts.api.create();

		// Login as valid user
		sugar().login();
	}

	/**
	 * Preview button and Action drop-down should not disappear while link existing record.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_29694_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Account module -> open any record in detail view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		FieldSet subpanelData = testData.get(testName).get(0);

		// Navigate to Contact sub-panel (Make sure sub-panel should be in collapse state)
		StandardSubpanel contactsSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanelCtrl.getControl("subpanelStatus").assertAttribute("class", subpanelData.get("closed"), true);

		// Click "Link existing record" and select any record -> Click "Add"
		contactsSubpanelCtrl.linkExistingRecord(myContactsRecord);

		// Expand Contact Sub-panel and Observe (no need to expand the sub-panel as already expended after linking the record)
		// Verify that the Preview button, Action drop-down and slider should not disappear from contact sub-panel
		contactsSubpanelCtrl.getControl("previewRow01").assertVisible(true);
		contactsSubpanelCtrl.getControl("expandActionRow01").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}