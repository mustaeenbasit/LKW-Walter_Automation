package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23556 extends SugarTest{
	BugRecord bugRec;
	StandardSubpanel bugSubpanel;

	public void setup() throws Exception {
		bugRec = (BugRecord) sugar().bugs.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
		sugar().contacts.create();

		// Linking the existing bug Record with an existing Contact
		sugar().contacts.listView.clickRecord(1);
		bugSubpanel =  sugar().contacts.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugSubpanel.linkExistingRecord(bugRec);
	}

	/**  Edit bug_Verify that Bug for contact is edited in "Bugs" sub-panel of "Contact Detail View" page.
	 */
	@Test
	public void Contacts_23556_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Bug detail view page
		// Used hover to get the bugs subpanel clearly visible
		bugSubpanel.editRecord(1);
		bugSubpanel.getEditField(1, "name").set(testName);
		bugSubpanel.saveAction(1);

		// Expected result - Verify updated bug record
		// TODO: VOOD-1424
		bugSubpanel.getDetailField(1, "name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
