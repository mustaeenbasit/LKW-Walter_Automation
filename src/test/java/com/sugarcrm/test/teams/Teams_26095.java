package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26095 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();	
	}

	/**
	 * Edit record-Select only one team and save.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26095_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Leads,choose a record
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showMore();
		sugar().leads.recordView.edit();

		// Add Team and save.
		FieldSet fs = sugar().users.getQAUser();
		sugar().leads.recordView.getEditField("relTeam").set(fs.get("userName"));
		sugar().leads.recordView.save();

		// TODO: VOOD-518, VOOD-1397
		// Verify that selected team display in Detail view and Edit view.
		new VoodooControl("span", "css", ".fld_team_name.detail").assertContains(fs.get("userName"), true);
		sugar().leads.recordView.edit();
		new VoodooControl("span", "css", ".fld_team_name.edit").assertContains(fs.get("userName"), true);
		sugar().leads.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}