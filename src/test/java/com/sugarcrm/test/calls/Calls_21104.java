package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21104 extends SugarTest {
	DataSource callsDS;

	public void setup() throws Exception {
		callsDS = testData.get(testName);
		sugar.calls.api.create(callsDS);
		sugar.login();
	}

	/**
	 * Verify that selected calls record information is updated by Mass Update.
	 * @throws Exception
	 */
	@Test
	public void Calls_21104_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName+"_1").get(0);
		sugar.calls.navToListView();
		// Verify records with their data which we are going to mass update below
		// TODO: VOOD-1217
		for (int i = 1; i <= callsDS.size(); i++) {
			new VoodooControl("a", "css", "tr:nth-of-type("+i+") .fld_assigned_user_name.list").assertEquals(customData.get("assigned_to"), true);
			new VoodooControl("a", "css", "tr:nth-of-type("+i+") .fld_status.list").assertEquals(sugar.calls.getDefaultData().get("status"), true);
			new VoodooControl("a", "css", "tr:nth-of-type("+i+") .fld_direction.list").assertEquals(sugar.calls.getDefaultData().get("direction"), true);
		}

		// 2 call records and mass update
		sugar.calls.listView.checkRecord(1);
		sugar.calls.listView.checkRecord(2);
		FieldSet massData = new FieldSet();
		massData.put("Assigned to", sugar.users.getQAUser().get("userName"));
		massData.put("Direction", customData.get("direction"));
		massData.put("Status", customData.get("status"));
		sugar.calls.massUpdate.performMassUpdate(massData);
		sugar.alerts.waitForLoadingExpiration(25000); // extra time needed to populate/reflect data on listview

		// Verify assigned to, status, direction field, after mass update
		// sugar.calls.listView.verifyField(1, "assignedTo", sugar.users.getQAUser().get("userName"));
		// sugar.calls.listView.verifyField(1, "direction", customData.get("direction"));
		// sugar.calls.listView.verifyField(1, "status", customData.get("status"));

		// TODO: VOOD-1217 - Once fixed, above commented code should work
		for (int i = 1; i <= callsDS.size(); i++) {
			VoodooControl assignedTo = new VoodooControl("a", "css", "tr:nth-of-type("+i+") .fld_assigned_user_name.list");
			VoodooControl status = new VoodooControl("a", "css", "tr:nth-of-type("+i+") .fld_status.list");
			VoodooControl direction = new VoodooControl("a", "css", "tr:nth-of-type("+i+") .fld_direction.list");

			if(i!=callsDS.size()){ // only 2 records are being updated
				assignedTo.assertEquals(sugar.users.getQAUser().get("userName"), true);
				status.assertEquals(customData.get("status"), true);
				direction.assertEquals(customData.get("direction"), true);
			}else{ // 3rd record remains same with default data
				assignedTo.assertEquals(customData.get("assigned_to"), true);
				status.assertEquals(sugar.calls.getDefaultData().get("status"), true);
				direction.assertEquals(sugar.calls.getDefaultData().get("direction"), true);
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}