package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Contacts_24181 extends SugarTest {
	ContactRecord myContact;
	StandardSubpanel bugsSubpanel;
	DataSource contactDS;

	public void setup() throws Exception {
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
		myContact = (ContactRecord) sugar().contacts.api.create();
		contactDS = testData.get(testName);
	}

	/**
	 * Test Case 24181: Report bug_Verify that the new reported bug is displayed in "Bugs" sub-panel of contact
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_24181_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myContact.navToRecord();
		bugsSubpanel = sugar().contacts.recordView.subpanels.get("Bugs");
		bugsSubpanel.addRecord();
		sugar().bugs.createDrawer.getEditField("name").set(contactDS.get(0).get("subject"));
		sugar().bugs.createDrawer.save();
		VoodooUtils.waitForAlertExpiration();

		// Verify that created bug is available in Bugs subpanel
		bugsSubpanel.expandSubpanel();
		bugsSubpanel.assertContains(contactDS.get(0).get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
