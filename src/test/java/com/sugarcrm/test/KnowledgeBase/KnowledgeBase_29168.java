package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29168 extends SugarTest {
	FieldSet portalUser1 = new FieldSet();
	FieldSet portalUser2 = new FieldSet();
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		DataSource portalContactData = testData.get(testName+"_portalContacts");

		// Making a new KB article as externally (portally) visible with status as Published
		FieldSet kbData = new FieldSet();
		kbData.put("isExternal", customData.get("isExternal"));
		kbData.put("status", customData.get("status"));
		sugar().knowledgeBase.api.create(kbData);

		// Creating portally active contacts
		ContactRecord contact1 = (ContactRecord)sugar().contacts.api.create(portalContactData.get(0));
		ContactRecord contact2 = (ContactRecord)sugar().contacts.api.create(portalContactData.get(1));

		// Logging in as admin
		sugar().login();

		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();

		// Logout as admin user
		sugar().logout();

		// Navigate to portal URL
		portal.loginScreen.navigateToPortal();

		portalUser1.put("userName", contact1.get("portalName"));
		portalUser1.put("password", contact1.get("password"));

		portalUser2.put("userName", contact2.get("portalName"));
		portalUser2.put("password", contact2.get("password"));

		// login as portal user1
		portal.login(portalUser1);
	}

	/**
	 * Verify that field to store voted contact in portal correctly record votes for portal user
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29168_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		try {
			portal.navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

			// TODO: VOOD-1783 : Need lib support for Vote buttons('Not Useful' and 'Useful') on KB record view page.
			VoodooControl usefulButton = new VoodooControl("a", "css", ".detail.fld_usefulness a:nth-child(2)");
			VoodooControl notUsefulButton = new VoodooControl("a", "css", ".detail.fld_usefulness a");

			// TODO: VOOD-1096 : Portal Module Listview support
			// portal.knowledgeBase.listView.clickRecord(1);   -- > not working
			VoodooControl firstRecord = new VoodooControl("a", "css", ".list.fld_name a");

			firstRecord.click();
			usefulButton.click();
			VoodooUtils.waitForReady();

			// Verifying that the useful button becomes dark gray
			usefulButton.assertCssAttribute(customData.get("cssAttribute"), customData.get("cssValue"));
			portal.logout();

			// login as portal user2
			portal.login(portalUser2);
			portal.navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
			firstRecord.click();
			notUsefulButton.click();
			VoodooUtils.waitForReady();

			// Verifying that the not-useful button becomes dark gray
			notUsefulButton.assertCssAttribute(customData.get("cssAttribute"), customData.get("cssValue"));
			portal.logout();

			// login as portal user1
			portal.login(portalUser1);
			portal.navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
			firstRecord.click();

			// Verifying that the useful button is still visible as dark gray
			usefulButton.assertCssAttribute(customData.get("cssAttribute"), customData.get("cssValue"));
			portal.logout();

			// login as portal user2
			portal.login(portalUser2);
			portal.navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
			firstRecord.click();

			// Verifying that the useful button is still visible as dark gray
			notUsefulButton.assertCssAttribute(customData.get("cssAttribute"), customData.get("cssValue"));
			portal.logout();
		}

		finally {
			// Logging in as admin
			sugar().loginScreen.navigateToSugar();
			sugar().login();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}