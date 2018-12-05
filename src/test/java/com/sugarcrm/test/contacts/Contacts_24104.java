package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Contacts_24104 extends SugarTest {
	BugRecord myBug;

	public void setup() throws Exception {
		myBug = (BugRecord) sugar().bugs.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
		sugar().contacts.create();
	}

	/**
	 * Test Case 24104: Verify that the selected bugs are displayed
	 * in "Bug" sub-panel of Contact record view
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_24104_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to a Contact record view and click "Select" button in "Bugs" sub-panel of "Contact Record View" page
		sugar().contacts.listView.clickRecord(1);
		StandardSubpanel bugsSubpanel = sugar().contacts.recordView.subpanels.get("Bugs");
		bugsSubpanel.linkExistingRecord(myBug);

		// Reload contact record view and verify that created bug is available in Bugs sub-panel
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Verify that the selected bug record(s) are displayed in "Bugs" sub-panel of "Contact Record View" page
		// TODO: VOOD-1424
		bugsSubpanel.scrollIntoViewIfNeeded(false);
		bugsSubpanel.getDetailField(1, "name").assertEquals(myBug.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
