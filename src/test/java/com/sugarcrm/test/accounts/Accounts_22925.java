package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Accounts_22925 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Account Detail - Notes sub-panel - Create Note_Verify that new note without Attachment related to the account is created in full form on "Accounts" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22925_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create Note record via subpanels in accounts record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel notesSubpanel = sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.scrollIntoViewIfNeeded(false);
		notesSubpanel.addRecord();
		sugar().notes.createDrawer.showMore();
		sugar().notes.createDrawer.setFields(sugar().notes.getDefaultData());
		sugar().notes.createDrawer.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().notes.createDrawer.getEditField("checkDisplayInPortal").set("true");
		sugar().notes.createDrawer.save();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		notesSubpanel.scrollIntoViewIfNeeded(false);
		// TODO: VOOD-1424 - Once resolved fields verification with verify() method
		notesSubpanel.getDetailField(1, "subject").assertEquals(sugar().notes.getDefaultData().get("subject"), true);
		notesSubpanel.getDetailField(1, "relAssignedTo").assertEquals(sugar().users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}