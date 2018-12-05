package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26069 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.contacts.api.create();
		FieldSet newContact = new FieldSet();
		newContact.put("firstName", customData.get("first_name"));
		newContact.put("lastName", customData.get("last_name"));
		sugar.contacts.api.create(newContact);
		sugar.login();
	}

	/**
	 * Verify team can be mass updated correctly in sidecar modules 
	 * @throws Exception
	 */
	@Test
	public void Teams_26069_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1160 - Need lib support for adding multiple teams during mass update
		VoodooControl addMultipleTeamValueCtrl = new VoodooControl("button", "css", ".btn.first");

		// TODO: VOOD-999 - Valid testability hooks for dashlets on the Select Dashlet drawer. 
		// Need a unique, consistent, and does not get translated when the language is changed
		VoodooControl team2Ctrl = new VoodooControl("div","css", "span[data-voodoo-name='team_name'] div:nth-of-type(2)");
		VoodooControl primaryCtrl = new VoodooControl("label","css", "span[data-voodoo-name='team_name'] div:nth-of-type(2) label");

		// Default team is Global (Primary)
		// If Append checkbox is not checked, the mass update teams will be replaced the original teams
		sugar.contacts.navToListView();
		sugar.contacts.listView.toggleSelectAll();
		sugar.contacts.listView.openActionDropdown();
		sugar.contacts.listView.massUpdate();
		VoodooControl massUpdateField = sugar.contacts.massUpdate.getControl(String.format("massUpdateField%02d", 2));
		massUpdateField.set(customData.get("select_team"));
		VoodooControl massUpdateValue = sugar.contacts.massUpdate.getControl(String.format("massUpdateValue%02d", 2));
		massUpdateValue.set(sugar.users.getQAUser().get("userName"));
		addMultipleTeamValueCtrl.click();
		massUpdateValue.set(customData.get("admin"));
		sugar.contacts.massUpdate.update();
		sugar.contacts.listView.clickRecord(1);
		for (int i = 1; i < 3; i++) { // 2 contacts
			// Verify Administrator & qauser(Primary) team only
			sugar.contacts.recordView.showMore();
			sugar.contacts.recordView.getDetailField("relTeam").assertContains(customData.get("admin"), true);
			team2Ctrl.assertContains(sugar.users.getQAUser().get("userName"), true);
			primaryCtrl.assertContains(customData.get("primary_team_label"), true);
			sugar().contacts.recordView.gotoNextRecord();
		}

		// If Append checkbox is checked, the mass update teams will be appended to the original teams.
		sugar.contacts.navToListView();
		sugar.contacts.listView.toggleSelectAll();
		sugar.contacts.listView.openActionDropdown();
		sugar.contacts.listView.massUpdate();
		massUpdateField.set(customData.get("select_team"));

		// TODO: VOOD-1160 - Need lib support for adding multiple teams during mass update
		new VoodooControl("input", "css", "input[name='append_team']").click();
		addMultipleTeamValueCtrl.click();
		massUpdateValue.set(customData.get("admin"));
		sugar.contacts.massUpdate.update();
		sugar.contacts.listView.clickRecord(1);
		for (int j = 1; j < 3; j++) { // 2 contacts
			// Verify appended teams i.e Administrator, Global(Primary) & qauser teams only
			sugar.contacts.recordView.showMore();
			sugar.contacts.recordView.getDetailField("relTeam").assertContains(customData.get("admin"), true);
			team2Ctrl.assertContains(customData.get("global"), true);
			primaryCtrl.assertContains(customData.get("primary_team_label"), true);

			// TODO: VOOD-999 - Valid testability hooks for dashlets on the Select Dashlet drawer. 
			// Need a unique, consistent, and does not get translated when the language is changed
			new VoodooControl("div","css", "span[data-voodoo-name='team_name'] div:nth-of-type(3)").assertContains(sugar.users.getQAUser().get("userName"), true);
			sugar().contacts.recordView.gotoNextRecord();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}