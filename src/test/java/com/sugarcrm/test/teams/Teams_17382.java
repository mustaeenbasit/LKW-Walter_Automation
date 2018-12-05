package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_17382 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();		
		sugar.accounts.api.create();
	}

	/**
	 * auto set the first team in teams list as primary team after the primary is deleted
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_17382_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);

		// open account create/edit page
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.recordView.edit();
		sugar.accounts.recordView.showMore();

		// TODO VOOD-518
		// Add teams
		VoodooControl addTeamCtrl = new VoodooControl("button","css", "span.fld_team_name.edit button.btn.first");
		addTeamCtrl.click();
		new VoodooSelect("div","css", ".fld_team_name.edit div:nth-child(2) div.select2-container.select2.inherit-width").set(sugar.users.getQAUser().get("userName"));
		addTeamCtrl.click();
		new VoodooSelect("div","css", ".fld_team_name.edit div:nth-child(3) div.select2-container.select2.inherit-width").set(ds.get(0).get("team2"));

		// remove current primary team
		new VoodooControl("i","css", "span.fld_team_name.edit div.control-group i.fa-minus").click();

		// Verify that the first team in the remaining teams list is auto set as primary team
		new VoodooControl("span","css", "span.fld_team_name div.control-group:nth-of-type(1) span.select2-chosen").assertEquals(sugar.users.getQAUser().get("userName"), true);
		new VoodooControl("i","css", "span.fld_team_name.edit div.control-group button.btn.third.active").assertVisible(true);

		// save the record
		sugar.accounts.recordView.save();

		// Verify that the teams are saved correctly
		new VoodooControl("div","css", "span.fld_team_name").assertContains(ds.get(0).get("team2"), true);
		new VoodooControl("div","xpath", "//span[contains(@class,'fld_team_name')]//label/..").assertEquals(ds.get(0).get("assert"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}