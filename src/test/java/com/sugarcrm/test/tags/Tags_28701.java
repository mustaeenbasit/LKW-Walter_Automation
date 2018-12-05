package com.sugarcrm.test.tags;
import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28701 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that tags are trimmed of spaces when they are inputted
	 * 
	 * @throws Exception
	 */
	@Ignore ("VOOD-1784 : Unable to set tag string in the Tags field with leading and trailing space string.")
	@Test
	public void Tags_28701_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet tagsData = testData.get(testName).get(0);

		// Go to Create a record (or edit one) where you can add tags
		sugar.accounts.navToListView();
		sugar.accounts.listView.create();
		sugar.accounts.createDrawer.showMore();

		// Define control for Tags edit field as used number of times
		VoodooControl tagEditFieldCtrl = sugar.accounts.createDrawer.getEditField("tags");

		// Input a few tags with spaces, e.g. " tag1 "
		tagEditFieldCtrl.set(tagsData.get("firstTag"));

		// Verify that the spaces in the tags should be trimmed when they are saved, e.g. " tag1 " should become "tag1"
		tagEditFieldCtrl.assertContains(tagsData.get("firstTag"), false);
		tagEditFieldCtrl.assertContains(tagsData.get("firstTag").trim(), true);

		// Cancel the account record
		sugar.accounts.createDrawer.cancel();

		// Create account record again
		sugar.accounts.listView.create();
		sugar.accounts.createDrawer.showMore();

		// Input a few tags with spaces, e.g. "sugar crm"
		tagEditFieldCtrl.set(tagsData.get("secondTag"));

		// Verify that the spaces between the string should be considered as unique string e.g., "sugar crm"
		tagEditFieldCtrl.assertContains(tagsData.get("secondTag"), true);

		// Cancel the account record
		sugar.accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}