package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_26492 extends SugarTest{
	DataSource ds;

	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
		sugar().accounts.api.create();
		ds = testData.get(testName);
	}

	/**
	 * Verify that List view is refreshed after mass update
	 */
	@Test
	public void Accounts_26492_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		FieldSet massUpdateField = new FieldSet();
		massUpdateField.put("Assigned to", ds.get(0).get("assignedUserName"));
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.checkRecord(2);
		sugar().accounts.massUpdate.performMassUpdate(massUpdateField);
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.listView.verifyField(1, "relAssignedTo", ds.get(0).get("assignedUserName"));
		sugar().accounts.listView.verifyField(2, "relAssignedTo", ds.get(0).get("assignedUserName"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
