package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_26074 extends SugarTest {

	public void setup() throws Exception {
		DataSource accountData = testData.get(testName+"_accountData");
		
		sugar().accounts.api.create(accountData);
		
		sugar().login();
	}

	/**
	 *  Verify the "add team (+)" button of team id mass update(update more than one record)
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26074_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to modules that can mass update team id
		sugar().accounts.navToListView();

		// Mass update the team of all the records
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.massUpdate();

		// Input one or more team id in the mass update panel
		FieldSet teamData = testData.get(testName).get(0);
		sugar().accounts.massUpdate.getControl("massUpdateField02").set(sugar().teams.moduleNamePlural);
		sugar().accounts.massUpdate.getControl("massUpdateValue02").set(teamData.get("teamName"));

		// Click on "+" button besides the team drop down
		// TODO: VOOD-1160
		new VoodooControl("button", "css", ".layout_Accounts [name='add']").click();	
		VoodooUtils.waitForReady();

		// Select second team 
		new VoodooSelect("a", "css", ".layout_Accounts div.filter-body.clearfix  div div div:nth-child(2) a").click();

		// Click link 'search for more'.  
		new VoodooControl("div", "css", "#select2-drop ul li div").click();

		// Select team record in SSV
		FieldSet fs = sugar().users.getQAUser();
		UserRecord qauser = new UserRecord(fs);
		sugar().teams.searchSelect.selectRecord(qauser);

		VoodooUtils.waitForReady();

		// Press update button and commit the update
		sugar().accounts.massUpdate.update();

		// Navigate to record & check new team info on record view
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		VoodooControl teamNameCtrl = new VoodooControl("span", "css", ".fld_team_name[data-voodoo-name='team_name']");
		teamNameCtrl.assertContains(teamData.get("teamName"), true);
		teamNameCtrl.assertContains(fs.get("userName"), true);

		// Navigate to another record & check the new team info
		sugar().accounts.recordView.gotoNextRecord();
		teamNameCtrl.assertContains(teamData.get("teamName"), true);
		teamNameCtrl.assertContains(fs.get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
