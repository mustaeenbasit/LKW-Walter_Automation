package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Teams_26100_leads extends SugarTest {
	FieldSet customData;
	StandardSubpanel taskSubpanel;
	FieldSet taskSubject = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		taskSubject.put("subject", customData.get("taskSubject"));
		sugar.leads.api.create();
		sugar.login();
	}

	/**
	 * [Pro/Ent Edition]-[Dynamic team]-Create record in subpanel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26100_leads_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to leads,choose a record
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);

		// Choose a subpanel 'Task' in detail view, and create a record having two teams.
		taskSubpanel = sugar.leads.recordView.subpanels.get("Tasks");
		taskSubpanel.addRecord();
		sugar.tasks.createDrawer.getEditField("subject").set(customData.get("taskSubject"));
		sugar.tasks.createDrawer.showMore();

		// TODO: VOOD-518
		// Add Team and save.
		new VoodooControl("button","css", "span.fld_team_name.edit button.btn.first").click();
		new VoodooSelect("div","css", ".fld_team_name.edit div:nth-child(2) div.select2-container.select2.inherit-width").set(sugar.users.getQAUser().get("userName"));
		sugar.tasks.createDrawer.save();

		// Verify that record is created.
		taskSubpanel.verify(1,taskSubject,true);

		// Verify that created record has multiple teams.
		sugar.tasks.navToListView();
		sugar.tasks.listView.clickRecord(1);
		sugar.tasks.recordView.showMore();

		// TODO: VOOD-518, VOOD-1217
		VoodooControl teamNameCtrl = new VoodooControl("span", "css", ".fld_team_name.detail");
		teamNameCtrl.assertContains(customData.get("team1"), true);
		teamNameCtrl.assertContains(sugar.users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}