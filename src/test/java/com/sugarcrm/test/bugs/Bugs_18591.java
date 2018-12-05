package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Bugs_18591 extends SugarTest {
	FieldSet customData,editedData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// 1 bug, 2 accounts created via API
		sugar.bugs.api.create();
		sugar.accounts.api.create();
		editedData = new FieldSet();
		editedData.put("name", customData.get("name"));
		sugar.accounts.api.create(editedData);
		editedData.clear();
		editedData = sugar.contacts.getDefaultData();
		sugar.login();

		// Enable bugs module
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);

		// 2 contacts created via UI, link email, account name (needed for sorting)
		sugar.contacts.navToListView();
		sugar.contacts.listView.create();
		sugar.contacts.createDrawer.showMore();
		VoodooControl firstNameCtrl = sugar.contacts.createDrawer.getEditField("firstName");
		VoodooControl lastNameCtrl = sugar.contacts.createDrawer.getEditField("lastName");
		VoodooControl phoneCtrl = sugar.contacts.createDrawer.getEditField("phoneWork");
		VoodooControl accNameCtrl = sugar.contacts.createDrawer.getEditField("relAccountName");

		// TODO: VOOD-444
		VoodooControl emailCtrl = new VoodooControl("input", "css", ".fld_email.edit input");
		firstNameCtrl.set(editedData.get("firstName"));
		lastNameCtrl.set(editedData.get("lastName"));
		phoneCtrl.set(editedData.get("phoneWork"));
		accNameCtrl.set(sugar.accounts.getDefaultData().get("name"));
		sugar.alerts.getWarning().cancelAlert();
		emailCtrl.set(customData.get("emailAddress1"));

		// Save
		sugar.contacts.createDrawer.save();

		if(sugar.alerts.getSuccess().queryVisible())
			sugar.alerts.getSuccess().closeAlert();

		// Click on Create from the listview
		sugar.contacts.listView.create();
		sugar.contacts.createDrawer.showMore();
		firstNameCtrl.set(customData.get("firstName"));
		lastNameCtrl.set(customData.get("lastName"));
		accNameCtrl.set(customData.get("name"));
		sugar.alerts.getWarning().confirmAlert();
		emailCtrl.set(customData.get("emailAddress2"));
		sugar.contacts.createDrawer.save();
		if(sugar.alerts.getSuccess().queryVisible())
			sugar.alerts.getSuccess().closeAlert();
	}

	/**
	 * Verify that records can be sorted in the Contacts sub-panel of Bugs 
	 * @throws Exception
	 */
	@Test
	public void Bugs_18591_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// 2 contacts link with bug record
		sugar.bugs.navToListView();
		sugar.bugs.listView.clickRecord(1);
		StandardSubpanel contactSubpanel = sugar.bugs.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		contactSubpanel.scrollIntoViewIfNeeded(false);
		contactSubpanel.clickLinkExisting();
		sugar.contacts.searchSelect.selectRecord(1);
		sugar.contacts.searchSelect.selectRecord(2);
		sugar.contacts.searchSelect.link();

		// TODO: VOOD-1424 - we need to implement verify method once this VOOD story Resolved
		VoodooControl firstRecord = contactSubpanel.getDetailField(1, "fullName");
		VoodooControl secondRecord = contactSubpanel.getDetailField(2, "fullName");
		contactSubpanel.scrollIntoViewIfNeeded(false);
		firstRecord.assertEquals(customData.get("fullName"), true);
		secondRecord.assertEquals(editedData.get("firstName") + " " + editedData.get("lastName"), true);

		// Verification of records
		// 1) sortBy name
		contactSubpanel.sortBy("headerFullname", false);
		VoodooUtils.waitForReady();
		firstRecord.assertEquals(editedData.get("firstName") + " " + editedData.get("lastName"), true);
		secondRecord.assertEquals(customData.get("fullName"), true);

		// 2) sortBy account name
		contactSubpanel.sortBy("headerAccountname", false);

		VoodooUtils.waitForReady();
		contactSubpanel.getDetailField(1, "relAccountName").assertEquals(customData.get("name"), true);
		contactSubpanel.getDetailField(2, "relAccountName").assertEquals(sugar.accounts.getDefaultData().get("name"), true);

		// 3) sortBy email
		contactSubpanel.sortBy("headerEmail", false);

		VoodooUtils.waitForReady();
		contactSubpanel.getDetailField(1, "emailAddress").assertEquals(customData.get("emailAddress2"), true);
		contactSubpanel.getDetailField(2, "emailAddress").assertEquals(customData.get("emailAddress1"), true);

		// 4) sort by phone
		contactSubpanel.sortBy("headerPhonework", false);

		VoodooUtils.waitForReady();
		contactSubpanel.getDetailField(1, "phoneWork").assertEquals(sugar.accounts.getDefaultData().get("workPhone"), true);
		contactSubpanel.getDetailField(2, "phoneWork").assertEquals(editedData.get("phoneWork"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}