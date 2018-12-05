package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_30188 extends SugarTest {

	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/** 
	 * Verify that sharing any record from Detail view shows "Record`s Name" in Body panel of Compose Email pop-up window
	 * @throws Exception
	 */
	@Test
	public void Contacts_30188_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Navigating to Contacts record view and click Primary Action Dropdown
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-691 
		new VoodooControl("a","css","[name='share']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("mce_0_ifr");
		
		// TODO: VOOD-843: Need library support for email composer UI
		VoodooControl emailBodyCtrl= new VoodooControl("body", "id", "tinymce");
		emailBodyCtrl.waitForVisible();
		String textToVerify = customData.get("stringPart1") + sugar().contacts.getDefaultData().get("firstName") +" "+ sugar().contacts.getDefaultData().get("lastName") + customData.get("stringPart2");
		
		// Verify text is written on Body panel of Email as "Please checkout Contact <contact name> from SugarCRM"
		emailBodyCtrl.assertContains(textToVerify, true);
		VoodooUtils.focusDefault();
		
		// Clicking email cancel button
		sugar().contacts.recordView.composeEmail.getControl("cancelButton").click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
