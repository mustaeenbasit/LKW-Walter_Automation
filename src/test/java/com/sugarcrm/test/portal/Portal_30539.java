package com.sugarcrm.test.portal;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;
import org.junit.Test;

public class Portal_30539 extends PortalTest {
	ContactRecord myContact;
	VoodooControl portalSettings, layout, recordView, saveButton;

	public void setup() throws Exception {
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		portalSettings = sugar().admin.adminTools.getControl("portalSettings");
		sugar().accounts.api.create();
		sugar().login();

		// TODO: VOOD-1119
		layout = new VoodooControl("img", "css", "#Layouts img");
		recordView = new VoodooControl("img", "css", "#viewBtnListView img");
		saveButton = new VoodooControl("input", "id", "savebtn");
		String value = "#Buttons td:nth-child(%d) tr:nth-child(2) td a";

		for (int i = 1; i <= 4; i++) {
			if (i != 3) {
				sugar().navbar.navToAdminTools();
				VoodooUtils.focusFrame("bwc-frame");
				// Portal settings
				portalSettings.click();
				VoodooUtils.waitForReady();
				layout.click();
				VoodooUtils.waitForReady();
				// For Contacts
				new VoodooControl("a", "css", String.format(value, (i))).click();
				VoodooUtils.waitForReady();
				recordView.click();
				VoodooUtils.waitForReady();

				// Add & remove some fields to detail view layout ('Title' field is replaced by 'Alternate Address City')
				new VoodooControl("li", "css", "#Hidden [data-name='created_by_name']").dragNDrop(new VoodooControl("li", "css", "#Available #topslot1"));

				// Save and deploy.
				saveButton.click();
				VoodooUtils.waitForReady();
				VoodooUtils.focusDefault();
			}
		}

		// Enable Bugs & KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Enable portal in admin, portal contact set up
		sugar().admin.portalSetup.enablePortal();
		myContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);

		String accountName = sugar().accounts.getDefaultData().get("name");
		// Accounts is linked to Contacts
		// TODO: VOOD-444
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(accountName);
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.recordView.save();

		sugar().logout();
	}

	/**
	 * Verify that Related columns are disabled for sorting in Portal
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_30539_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to portal URL, login as portal user
		portal().loginScreen.navigateToPortal();
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().login(portalUser);

		// Cases
		portal().navbar.navToModule(sugar().cases.moduleNamePlural);

		// TODO: VOOD-1768 ListView header element defs don't work for unsortable headers.
		VoodooControl createdBy = new VoodooControl("th", "css", "[data-fieldname=created_by_name]");
		// Verify that the related fields (column) i.e created_by_name is disabled for sorting.
		createdBy.assertAttribute("class", "sorting", false);

		// Bugs
		portal().navbar.navToModule(sugar().bugs.moduleNamePlural);
		// Verify that the related fields (column) i.e created_by_name is disabled for sorting.
		createdBy.assertAttribute("class", "sorting", false);

		// Knowledge Base
		portal().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Verify that the related fields (column) i.e created_by_name is disabled for sorting.
		createdBy.assertAttribute("class", "sorting", false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}