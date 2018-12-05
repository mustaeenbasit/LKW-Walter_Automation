package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_18863 extends SugarTest {

	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that module icon is showing when viewing a record that doesn't have avatar
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_18863_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		// preview the record
		sugar().contacts.listView.previewRecord(1);
		// TODO: VOOD-1419
		// Verify that Contacts module icon, Co,inside  preview pane
		new VoodooControl("span", "css", ".fld_picture.detail .label-module").assertContains("Co", true);;

		// navigate to contacts record view
		sugar().contacts.listView.clickRecord(1);
		// Verify that Contacts module icon, Co, is at left upper corner.
		sugar().contacts.recordView.getControl("moduleIDLabel").assertContains("Co", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
