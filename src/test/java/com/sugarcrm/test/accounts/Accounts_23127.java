package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_23127 extends SugarTest {
	DataSource ds;
	
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		sugar().accounts.create(ds);
		sugar().accounts.navToListView();
	}
	
	/**
	 * Verify that corresponding accounts' detail view is displayed after clicking the pagination control link in detail view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_23127_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.listView.clickRecord(1);
		VoodooUtils.waitForAlertExpiration();
		//TODO VOOD-730
		for(int i=1;i<ds.size();i++){
			new VoodooControl("button", "css", "button.btn-invisible.next-row").click();
			VoodooUtils.waitForAlertExpiration();
			sugar().accounts.recordView.getDetailField("name").assertEquals(ds.get(ds.size()-1-i).get("name"), true);
		}
		for(int i=1;i<ds.size();i++){
			new VoodooControl("button", "css", "button.btn-invisible.previous-row").click();
			VoodooUtils.waitForAlertExpiration();
			sugar().accounts.recordView.getDetailField("name").assertEquals(ds.get(i).get("name"), true);
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}