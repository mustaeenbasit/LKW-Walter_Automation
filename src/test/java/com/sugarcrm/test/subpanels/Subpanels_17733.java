package com.sugarcrm.test.subpanels;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class Subpanels_17733 extends SugarTest {
	CaseRecord myCase;
	StandardSubpanel casesSubpanel;

	public void setup() throws Exception {
		sugar.contacts.api.create();
		sugar.accounts.api.create();
		sugar.login();

		// Relate a case to a contact
		// TODO: Use the API lib once VOOD-444 is implemented.
		myCase = (CaseRecord) sugar.cases.create();
	}

	/**
	 * Verify the supported actions are present in the subpanel list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17733_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to a contact record subpanel view
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);
		casesSubpanel = sugar.contacts.recordView.subpanels.get(sugar.cases.moduleNamePlural);
		sugar.contacts.recordView.subpanels.get(sugar.documents.moduleNamePlural).hover();
		
		casesSubpanel.linkExistingRecord(myCase);
		
		// Verify that record is successfully linked
		casesSubpanel.assertContains(myCase.get("name"), true);

		casesSubpanel.expandSubpanelRowActions(1);

		// Verify Edit and Unlink should be available for each record in the Subpanel
		casesSubpanel.getControl("editActionRow01").assertVisible(true);
		casesSubpanel.getControl("unlinkActionRow01").assertVisible(true);
	}

	public void cleanup() throws Exception {}
}