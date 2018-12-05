package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_23681 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Create quote_Verify that a related quote can be created from contact detail view
	 * @throws Exception
	 */
	@Test
	public void Contacts_23681_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Contacts Recordview
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Click "Create" button in "Quotes" sub-panel
		// TODO: VOOD-1000
		// Once VOOD-1000 resolved, line #31 to #33 will be uncommented
		// StandardSubpanel quotesSubpanel = sugar().contacts.recordView.subpanels.get(sugar().quotes.moduleNamePlural);
		// quotesSubpanel.scrollIntoView();
		// quotesSubpanel.addRecord();
		VoodooControl quotesSubpanel = new VoodooControl("div", "css", ".layout_Quotes");
		quotesSubpanel.scrollIntoView();
		new VoodooControl("i", "css", ".layout_Quotes .fa.fa-plus").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Enter all the Mandatory fields and Save
		sugar().quotes.editView.getEditField("name").set(sugar().quotes.getDefaultData().get("name"));
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(sugar().quotes.getDefaultData().get("date_quote_expected_closed"));
		sugar().quotes.editView.getEditField("billingAccountName").set(sugar().quotes.getDefaultData().get("billingAccountName"));
		sugar().quotes.editView.getControl("saveButton").click();
		VoodooUtils.waitForReady();

		// Verify that created quote is displayed in "Quotes" sub-panel
		// TODO: VOOD-1000
		quotesSubpanel.scrollIntoViewIfNeeded(true);
		new VoodooControl("a", "css", ".layout_Quotes .list.fld_name a").assertEquals(sugar().quotes.getDefaultData().get("name"), true);
		new VoodooControl("a", "css", ".layout_Quotes .list.fld_account_name a").assertEquals(sugar().quotes.getDefaultData().get("billingAccountName"), true);
	}

	public void cleanup() throws Exception {}
}
