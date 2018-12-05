package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Calls_19088 extends SugarTest {
	DataSource callsData, accountsData;

	public void setup() throws Exception {
		callsData = testData.get(testName);
		accountsData = testData.get(testName+"_Accounts");
		sugar.accounts.api.create(accountsData);
		sugar.calls.api.create(callsData);
		sugar.login();

		// TODO: VOOD-444 Support creating relationships via API.
		sugar.calls.navToListView();
		sugar.calls.listView.sortBy("headerName", true);
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();
		sugar.calls.recordView.getEditField("assignedTo").set(callsData.get(0).get("assignedTo"));
		sugar.calls.recordView.getEditField("teams").set(callsData.get(0).get("teams"));
		sugar.calls.recordView.getEditField("relatedToParentType").set(sugar.accounts.moduleNameSingular);
		sugar.calls.recordView.getEditField("relatedToParentName").set(accountsData.get(0).get("name"));
		sugar.calls.recordView.save();
	}

	/**
	 * Verify that selected call records' information is updated by Mass Update.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_19088_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		sugar.calls.listView.checkRecord(2);
		sugar.calls.listView.checkRecord(3);
		sugar.calls.listView.checkRecord(4);
		sugar.calls.listView.openActionDropdown();
		sugar.calls.listView.massUpdate();
		
		sugar.calls.massUpdate.getControl("massUpdateField02").set("Assigned to");
		sugar.calls.massUpdate.getControl("massUpdateValue02").set(callsData.get(3).get("assignedTo"));

		sugar.calls.massUpdate.addRow(2);
		sugar.calls.massUpdate.getControl("massUpdateField03").set("Teams");
		sugar.calls.massUpdate.getControl("massUpdateValue03").set(callsData.get(3).get("teams"));
		
		sugar.calls.massUpdate.addRow(3);
		sugar.calls.massUpdate.getControl("massUpdateField04").set("Related to");
		
		// TODO: VOOD-1003 Lib support needed for mass update controls on list view
		//new VoodooControl("span", "css", ".filter-value .flex-relate-record").click();
		//new VoodooSelect("input", "css", "#select2-drop div input").set(accountsData.get(3).get("name"));
		new VoodooSelect("span", "css", ".filter-value .flex-relate-record").set(accountsData.get(3).get("name"));
		
		sugar.calls.massUpdate.addRow(4);
		sugar.calls.massUpdate.getControl("massUpdateField05").set("Status");
		sugar.calls.massUpdate.getControl("massUpdateValue05").set(callsData.get(3).get("status"));

		sugar.calls.massUpdate.addRow(5);
		sugar.calls.massUpdate.getControl("massUpdateField06").set("Direction");
		sugar.calls.massUpdate.getControl("massUpdateValue06").set(callsData.get(3).get("direction"));
		sugar.calls.massUpdate.update();
		sugar.calls.navToListView();
		sugar.calls.listView.sortBy("headerName", true);
		
		VoodooControl assignedTo = sugar.calls.recordView.getDetailField("assignedTo");
		VoodooControl teams = sugar.calls.recordView.getDetailField("teams");
		VoodooControl relatedToParentName = sugar.calls.recordView.getDetailField("relatedToParentName");
		VoodooControl status = sugar.calls.recordView.getDetailField("status");
		VoodooControl direction = sugar.calls.recordView.getDetailField("direction");
		
		// verify that call records have been successfully mass updated
		for(int i=2; i<=callsData.size();i++) {
			sugar.calls.listView.clickRecord(i);
			VoodooUtils.waitForReady();
			assignedTo.assertEquals(callsData.get(3).get("assignedTo"), true);
			teams.assertContains(callsData.get(3).get("teams"), true);
			relatedToParentName.assertEquals(accountsData.get(3).get("name"), true);
			status.assertEquals(callsData.get(3).get("status"), true);
			direction.assertEquals(callsData.get(3).get("direction"), true);
			sugar.calls.navToListView();
		}

		// verify that the remaining call records are not affected by the mass update
		sugar.calls.listView.clickRecord(1);
		VoodooUtils.waitForReady();
		assignedTo.assertEquals(callsData.get(0).get("assignedTo"), true);
		teams.assertContains(callsData.get(0).get("teams"), true);
		relatedToParentName.assertEquals(accountsData.get(0).get("name"), true);
		status.assertEquals(callsData.get(0).get("status"), true);
		direction.assertEquals(callsData.get(0).get("direction"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}