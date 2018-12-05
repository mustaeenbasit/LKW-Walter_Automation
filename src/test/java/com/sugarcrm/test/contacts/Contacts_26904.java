package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class Contacts_26904 extends SugarTest {
	VoodooControl resetButtonCtrl,resetClickCtrl,relationshipsCtrl,fieldsCtrl,labelsCtrl,layoutsCtrl,extensionsCtrl,contactsCtrl;

	public void setup() throws Exception {
		sugar().login();

		// TODO: VOOD-938
		contactsCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");
	}

	/**
	 * Verify that Setting 'Required' on email1 field makes field required
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_26904_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Studio > Contacts > Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		contactsCtrl.click();
		// TODO: VOOD-938
		new VoodooControl("td", "id", "fieldsBtn").click();

		// Select email1
		new VoodooControl("a","id", "email").click();
		// Click "Required" Checkbox
		new VoodooControl("input", "css", "[name='required']").click();
		// Save
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Create Contact
		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		// Fill in the required fields, Leave Email field blank
		sugar().contacts.createDrawer.getEditField("lastName").set(testName);
		// Save
		sugar().contacts.createDrawer.save();

		// Verify Email address field flashes red/alerts user that field is required.
		sugar().contacts.createDrawer.getEditField("emailAddress").assertAttribute("class", "required", true);

		sugar().alerts.getError().closeAlert();
		sugar().contacts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
