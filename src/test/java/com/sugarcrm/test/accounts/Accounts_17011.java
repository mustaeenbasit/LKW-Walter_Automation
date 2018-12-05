package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17011 extends SugarTest {
	DataSource ds;
	AccountRecord acc;
		
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		acc = (AccountRecord)sugar().accounts.api.create(ds.get(0));
	}

	/**
	 * Test Case 17011: Phone type field display format on detail view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17011_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
				
		acc.navToRecord();
		sugar().accounts.recordView.getDetailField("workPhone").assertEquals(ds.get(0).get("workPhone"), true);
		sugar().accounts.recordView.addControl("toVerify", "div", "css", ".fld_phone_office.detail div");
		sugar().accounts.recordView.getControl("toVerify").assertAttribute("data-original-title", ds.get(0).get("workPhone"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}