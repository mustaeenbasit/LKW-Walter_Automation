package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26072 extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		ds = testData.get(testName+"_contacts");
		sugar().contacts.api.create(ds);
		sugar().login();
	}

	/**
	 * Verify that  team id can be replaced by  mass update(update more than one record) 
	 * @throws Exception
	 */
	@Test
	public void Teams_26072_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().contacts.navToListView();

		sugar().contacts.listView.toggleSelectAll();
		// Mass update
		FieldSet fs = sugar().users.getQAUser();
		FieldSet myMassUpdate = new FieldSet();
		myMassUpdate.put(sugar().teams.moduleNamePlural, fs.get("userName"));
		sugar().contacts.massUpdate.performMassUpdate(myMassUpdate);

		// Verify team's info of all the records is updated.
		// Sort records in list view to verify records.
		sugar().contacts.listView.sortBy("headerFullname", true);
		// TODO: VOOD-1005
		VoodooControl teamCtrl = new VoodooControl("span", "css", ".fld_team_name[data-voodoo-name='team_name']");
		for (int i = 1; i <= ds.size(); i++) {
			sugar().contacts.listView.clickRecord(i);
			sugar().contacts.recordView.showMore();
			VoodooUtils.waitForReady();
			teamCtrl.assertContains(fs.get("userName"), true);
			teamCtrl.assertContains(customData.get("primary_team_label"), true);
			sugar().contacts.navToListView();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}