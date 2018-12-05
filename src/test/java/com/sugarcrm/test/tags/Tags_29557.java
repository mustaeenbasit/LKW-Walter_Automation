package com.sugarcrm.test.tags;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Tags_29557 extends SugarTest {
	AccountRecord myAccount;
	public void setup() throws Exception {
		myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify Tag search result is displayed while click on Tag record that start with wild character
	 * @throws Exception
	 */
	@Test
	public void Tags_29557_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet tagRecord = testData.get(testName).get(0);
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("tags").set(tagRecord.get("tagName"));
		sugar().accounts.recordView.save();
		
		// TODO: VOOD-911
		// Clicking on Tag record
		new VoodooControl("a", "css", ".fld_tag.detail a").click();
		
		// Verifying the Tag search result page is displaying properly.
		sugar().globalSearch.getRow(myAccount).assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}