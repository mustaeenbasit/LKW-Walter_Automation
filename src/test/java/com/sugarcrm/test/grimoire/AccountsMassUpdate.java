package com.sugarcrm.test.grimoire;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;

public class AccountsMassUpdate extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().accounts.api.create();
		sugar().accounts.api.create();
		sugar().login();
	}

	@Test
	public void AccountsMassUpdate_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		FieldSet massUpdateData = new FieldSet();
		massUpdateData.put("Assigned to", "qauser");

		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.checkRecord(2);
		sugar().accounts.massUpdate.performMassUpdate(massUpdateData);

		// Verify mass update record
		sugar().accounts.listView.verifyField(1, "relAssignedTo", massUpdateData.get("Assigned to"));
		sugar().accounts.listView.verifyField(2, "relAssignedTo", massUpdateData.get("Assigned to"));
		sugar().accounts.listView.verifyField(3, "relAssignedTo", "Administrator");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}