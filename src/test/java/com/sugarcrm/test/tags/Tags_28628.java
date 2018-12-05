package com.sugarcrm.test.tags;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Tags_28628 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * [Tags] Verify Tags should be in alphabetical order
	 *
	 * @throws Exception
	 */
	@Test
	public void Tags_28628_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);

		// Go to Contacts module
		sugar().contacts.navToListView();
		// Create a new contact with several tags in the Tags field such as "123", "sss", "aaa"
		sugar().contacts.listView.create();
		sugar().contacts.createDrawer.getEditField("lastName").set(sugar().contacts.moduleNamePlural);
		sugar().contacts.createDrawer.getEditField("tags").set(ds.get(0).get("tagName"));
		sugar().contacts.createDrawer.getEditField("tags").set(ds.get(1).get("tagName"));
		sugar().contacts.createDrawer.getEditField("tags").set(ds.get(2).get("tagName"));
		// save the record
		sugar().contacts.createDrawer.save();

		sugar().contacts.listView.clickRecord(1);
		// Refresh the page.
		VoodooUtils.refresh();

		// TODO: VOOD-1349
		// Verify the Tags in alphabetical order. "123", "aaa", "sss".
		new VoodooControl("a", "css", ".detail.fld_tag div span:nth-child(1) span a").assertContains(ds.get(1).get("tagName"), true);
		new VoodooControl("a", "css", ".detail.fld_tag div span:nth-child(2) span a").assertContains(ds.get(2).get("tagName"), true);
		new VoodooControl("a", "css", ".detail.fld_tag div span:nth-child(3) span a").assertContains(ds.get(0).get("tagName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
