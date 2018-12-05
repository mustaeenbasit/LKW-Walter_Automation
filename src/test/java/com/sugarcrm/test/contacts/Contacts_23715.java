package com.sugarcrm.test.contacts;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Contacts_23715 extends SugarTest {
	VoodooFileField browseToImport;
	VoodooControl nextButton, publishButtonCtrl, deleteButtonCtrl;
	
	public void setup() throws Exception {
		sugar().login();

		// Click on Imports Contacts in Contacts module
		sugar().navbar.selectMenuItem(sugar().contacts, "importContacts");

		// Need to change focus for bwc-frame
		VoodooUtils.focusFrame("bwc-frame");

		// Create Contact record 
		// TODO: VOOD-1396 - Need Controls for the Import Tasks functionality
		browseToImport = new VoodooFileField("input", "id", "userfile");
		nextButton = new VoodooControl("input", "id", "gonext");
		publishButtonCtrl =  new VoodooControl("input", "css", "[value='Publish']");
		nextButton.click();
		VoodooUtils.waitForReady();
		browseToImport.set("src/test/resources/data/" + testName + ".csv");
		
		// Need to click next button for importing
		nextButton.click();
		nextButton.click();
		nextButton.click();
		VoodooUtils.waitForReady();
		
		// Save import setting
		new VoodooControl("input", "id", "save_map_as").set(testName);
		new VoodooControl("input", "id", "importnow").click();
		VoodooUtils.focusDefault();
		
		// Making import setting publish 
		sugar().navbar.selectMenuItem(sugar().contacts, "importContacts");

		// Need to change focus for bwc-frame
		VoodooUtils.focusFrame("bwc-frame");
		nextButton.click();
		VoodooUtils.waitForReady();
		publishButtonCtrl.click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Import contacts_Verify that published "My Saved Sources" option can be unpublished.
	 * @throws Exception
	 */
	@Test
	public void Contacts_23715_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on Imports Contact to importing another contact
		sugar().navbar.selectMenuItem(sugar().contacts, "importContacts");

		// Need to change focus for bwc-frame
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1396 - Need Controls for the Import Tasks functionality
		nextButton.click();
		
		// Verifying Unpublish and Delete buttons visibility 
		VoodooControl unpublishButtonCtrl = new VoodooControl("input", "css", "[value='Un-Publish']");
		deleteButtonCtrl = new VoodooControl("input", "css", "[value='Delete']");
		deleteButtonCtrl.assertVisible(true);
		unpublishButtonCtrl.click();
		
		// Verifying 'Un-Publish' button changes to 'Publish'
		publishButtonCtrl.assertVisible(true);
		unpublishButtonCtrl.assertVisible(false);
		VoodooUtils.waitForReady();
		browseToImport.set("src/test/resources/data/" + testName + "_newContact.csv");
		
		// Need to click next button for importing
		nextButton.click();
		nextButton.click();
		nextButton.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "importnow").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().logout();
		
		// Login as qauser
		sugar().login(sugar().users.getQAUser());
		sugar().navbar.selectMenuItem(sugar().contacts, "importContacts");
		VoodooUtils.focusFrame("bwc-frame");
		nextButton.click();
		VoodooUtils.waitForReady();
		
		// Verifying 'pre-set import settings' from set up is not displayed to qauser
		new VoodooControl("table", "class", "preset-settings").assertContains(testName, false);
	}

	public void cleanup() throws Exception {}
}