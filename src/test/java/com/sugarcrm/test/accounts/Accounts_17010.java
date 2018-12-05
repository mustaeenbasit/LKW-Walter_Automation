package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17010 extends SugarTest {
	DataSource ds;
	
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		sugar().accounts.create(ds.get(0));
		sugar().accounts.create(ds.get(1));
	}

	/**
	 * Test Case 17010: Phone type field display format on list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17010_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Verify that normal (not truncated) phone is displayed correctly on list view  
		sugar().accounts.listView.verifyField(2, "workPhone",testData.get(testName).get(0).get("workPhone"));
		// Verify that truncated phone is displayed in popup (title attribute)		
		sugar().accounts.listView.addControl("toVerify","div", "css", ".fld_phone_office.list div");
		sugar().accounts.listView.getControl("toVerify").assertAttribute("data-original-title", ds.get(1).get("workPhone"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}