package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_26251 extends SugarTest {
	FieldSet myTestData = new FieldSet();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		myTestData = testData.get(testName).get(0);

		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
		sugar().quotedLineItems.create();
	}

	/**
	 * TC 26251: Verify that Quoted Line Item record can be copied successfully
	 *
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_26251_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().quotedLineItems.navToListView();
		sugar().quotedLineItems.listView.clickRecord(1);
		sugar().quotedLineItems.recordView.copy();

		// Change some fields in QLI created through copy
		sugar().quotedLineItems.createDrawer.getEditField("name").set(myTestData.get("name"));
		sugar().quotedLineItems.createDrawer.getEditField("quantity").set(myTestData.get("quantity"));

		// Save the record
		sugar().quotedLineItems.createDrawer.save();
		sugar().quotedLineItems.recordView.showMore();

		// Verify that new QLI record is successfully created
		sugar().quotedLineItems.recordView.getDetailField("name").assertContains(myTestData.get("name"), true);
		sugar().quotedLineItems.recordView.getDetailField("accountName").assertContains(myTestData.get("account"), true);
		sugar().quotedLineItems.recordView.getDetailField("status").assertContains(myTestData.get("status"), true);
		sugar().quotedLineItems.recordView.getDetailField("quantity").assertContains(myTestData.get("quantity"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}