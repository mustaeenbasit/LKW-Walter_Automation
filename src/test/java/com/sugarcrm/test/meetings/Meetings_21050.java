package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21050 extends SugarTest {
	DataSource meetingDS;

	public void setup() throws Exception {
		meetingDS = testData.get(testName);
		sugar().meetings.api.create(meetingDS);
		sugar().login();
	}

	/**
	 * Verify that selected meetings record information is updated by Mass Update. 
	 * @throws Exception
	 */
	@Test
	public void Meetings_21050_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName+"_1").get(0);
		sugar().navbar.selectMenuItem(sugar().meetings, "view"+sugar().meetings.moduleNamePlural);
		sugar().alerts.waitForLoadingExpiration();
		// Verify records with their data which we are going to mass update below
		// TODO: VOOD-1217
		for (int i = 1; i <= meetingDS.size(); i++) {
			new VoodooControl("a", "css", "tr:nth-of-type("+i+") .fld_assigned_user_name.list").assertEquals(customData.get("assigned_to"), true);
			new VoodooControl("a", "css", "tr:nth-of-type("+i+") .fld_status.list").assertEquals(sugar().meetings.getDefaultData().get("status"), true);
		}

		// 2 meeting records and mass update
		sugar().meetings.listView.checkRecord(1);
		sugar().meetings.listView.checkRecord(2);
		FieldSet massData = new FieldSet();
		massData.put("Assigned to", sugar().users.getQAUser().get("userName"));
		massData.put("Status", customData.get("status"));
		sugar().meetings.massUpdate.performMassUpdate(massData);
		sugar().alerts.waitForLoadingExpiration(25000); // extra time needed to populate/reflect data on listview

		// Verify assigned to, status field, after mass update
		// sugar().meetings.listView.verifyField(1, "assignedTo", sugar().users.getQAUser().get("userName"));
		// sugar().meetings.listView.verifyField(1, "status", customData.get("status"));

		// TODO: VOOD-1217 - Once fixed, above commented code should work
		for (int i = 1; i <= meetingDS.size(); i++) {
			VoodooControl assignedTo = new VoodooControl("a", "css", "tr:nth-of-type("+i+") .fld_assigned_user_name.list");
			VoodooControl status = new VoodooControl("a", "css", "tr:nth-of-type("+i+") .fld_status.list");

			if(i!=meetingDS.size()){ // only 2 records are being updated
				assignedTo.assertEquals(sugar().users.getQAUser().get("userName"), true);
				status.assertEquals(customData.get("status"), true);
			}else{ // 3rd record remains same with default data
				assignedTo.assertEquals(customData.get("assigned_to"), true);
				status.assertEquals(sugar().meetings.getDefaultData().get("status"), true);
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}