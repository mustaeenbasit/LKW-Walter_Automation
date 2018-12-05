package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class Subpanels_17736 extends SugarTest {
	ContactRecord myContact;
	CaseRecord myCase;
	StandardSubpanel casesSubpanel,documentSubpanel;
	
	public void setup() throws Exception {
		myContact = (ContactRecord) sugar.contacts.api.create();
		sugar.accounts.api.create();
		sugar.login();
		
		casesSubpanel = sugar.contacts.recordView.subpanels.get(sugar.cases.moduleNamePlural);
		documentSubpanel = sugar.contacts.recordView.subpanels.get(sugar.documents.moduleNamePlural);
		
		// Relate a case to a contact
		// TODO: Use the API lib once VOOD-444 is implemented.
		myCase = (CaseRecord) sugar.cases.create();
		
		// Link Contact record to existing Case record
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);
		documentSubpanel.hover();
		casesSubpanel.linkExistingRecord(myCase);
	}

	/**
	 * Verify the subpanel row level action - Unlink and confirm
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17736_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click arrow down key next to preview icon on the record right side, Click Unlink 
		casesSubpanel.expandSubpanel();
		casesSubpanel.expandSubpanelRowActions(1);
		casesSubpanel.getControl("unlinkActionRow01").click();
		
		// Verify, a pop up warning confirmation message shown after clicking Unlink, click Confirm
		sugar.alerts.getAlert().confirmAlert();

		// Verify that record is removed from the sub panel
		casesSubpanel.assertContains(myCase.get("name"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}