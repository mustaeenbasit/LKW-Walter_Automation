package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17158 extends SugarTest {
	
	AccountRecord account1;
	
	public void setup() throws Exception {
		sugar().login();
		account1 = (AccountRecord) sugar().accounts.api.create();
	}

	/**
	 * Team set should be reverted on cancel of edit
	 * @throws Exception
	 */		
	@Test
	public void Accounts_17158_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet toVerify = testData.get(testName).get(0);
		
		account1.navToRecord();
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		
		//TODO VOOD-518 need lib support for team widget
		new VoodooControl("button", "css", ".fld_team_name button[name='add']").click();
		new VoodooSelect("a", "css", "span[data-voodoo-name='team_name'] div.control-group:nth-child(2) a.select2-choice").set(toVerify.get("team1"));
		new VoodooControl("i", "css", "span[data-voodoo-name='team_name'] div.control-group:nth-child(2) .fa.fa-star").click();

		sugar().accounts.recordView.cancel();

		// Verification of the team on the record view
		sugar().accounts.recordView.getDetailField("relTeam").assertElementContains(toVerify.get("team"), true);
		sugar().accounts.recordView.getDetailField("relTeam").assertElementContains(toVerify.get("primary"), true);
		sugar().accounts.recordView.getDetailField("relTeam").assertElementContains(toVerify.get("team1"), false);


		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
