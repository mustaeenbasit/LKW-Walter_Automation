package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.DataSource;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Contacts_24184 extends SugarTest {
	StandardSubpanel directReportsSub;
	DataSource contDS;

	public void setup() throws Exception {
		ContactRecord myContact = (ContactRecord) sugar().contacts.api.create();
		contDS = testData.get(testName);

		sugar().login();

		// Navigate to the contact record view and add a related direct report from DR subpanel
		myContact.navToRecord();
		directReportsSub = sugar().contacts.recordView.subpanels.get("Contacts");
		directReportsSub.addRecord();
		sugar().contacts.createDrawer.getEditField("lastName").set(contDS.get(0).get("lastName"));
		sugar().contacts.createDrawer.save();
	}

	/**
	 * Verify that direct report related with contact can be correctly unlinked from Direct Reports subpanel of the contact
	 * @throws Exception
	 */
	@Test
	public void Contacts_24184_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		directReportsSub.hover();
		// Unlink the contact from Direct Reports subpanel
		directReportsSub.unlinkRecord(1);

		// Verify that related contact is correctly unlinked from the Direct Reports subpanel
		directReportsSub.expandSubpanel();
		directReportsSub.assertContains(contDS.get(0).get("lastName"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
