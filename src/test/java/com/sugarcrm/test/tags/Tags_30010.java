package com.sugarcrm.test.tags;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_30010 extends SugarTest {
	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify Tags disappear from Tag field after clicking on cancel in record view
	 * @throws Exception
	 */
	@Test
	public void Tags_30010_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Contacts module
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		VoodooControl tagsDetailCtrl = sugar().contacts.recordView.getDetailField("tags");
		tagsDetailCtrl.hover();

		// TODO: VOOD-854
		new VoodooControl("i", "css", "[data-name='tag'] i").click();
		sugar().contacts.recordView.getEditField("tags").set(testName);
		 
		// Clicking on 'Do Not Call' to shift focus from tags field
		sugar().contacts.recordView.getDetailField("checkDoNotCall").set("true");
		
		// Click Cancel on Contact record view
		sugar().contacts.recordView.cancel();
		
		// Asserting that Tags field will remain blank after cancel
		tagsDetailCtrl.assertContains(testName, false);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}