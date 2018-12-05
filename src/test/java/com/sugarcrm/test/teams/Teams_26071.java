package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_26071 extends SugarTest {
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar.login();
		
		myAccount = (AccountRecord) sugar.accounts.api.create();
	}

	/**
	 * Verify the replace function of team id mass update
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26071_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.accounts.navToListView();
		sugar.accounts.listView.verifyField(1, "name", myAccount.getRecordIdentifier());
		
		sugar.accounts.listView.checkRecord(1);
		sugar.accounts.listView.openActionDropdown();
		sugar.accounts.listView.massUpdate();
		
		// TODO: VOOD-1003 lib support for all below massupdate controls on listview
		
		// Default team - Administrator
		new VoodooControl("a","css", "div.select2-container.mu_attribute a").click();
		new VoodooControl("input","css", "div#select2-drop input").set("Teams");
		new VoodooControl("span","css", "span.select2-match").click();

		// Select first team - qauser
		new VoodooControl("a","css", "div.filter-value a").click();
		new VoodooControl("input","css", "div#select2-drop input").set(sugar.users.getQAUser().get("userName"));
		new VoodooControl("span","css", "span.select2-match").click();
		new VoodooControl("button","css", "div.filter-value button.btn.third.active").click();
		new VoodooControl("button","css", "div.filter-value button.btn.first").click();
		
		// Select second team - Global
		new VoodooControl("a","css", "div.filter-value.controls.span8 div.control-group:nth-of-type(2) div.controls div.select2-container a").click();
		new VoodooControl("input","css", "div#select2-drop input").set("Global");
		new VoodooControl("span","css", "span.select2-match").click();

		// Update
		new VoodooControl("a","css", "span[data-voodoo-name='update_button'] a.btn.btn-primary").click();
		VoodooUtils.pause(3000); // To allow save to finish

		// Verify new Teams
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.recordView.showMore();
		
		// Verify Global
		new VoodooControl("div","css", "span[data-voodoo-name='team_name'] div:nth-of-type(1)").assertContains("Global", true);
		
		// Verify qauser as Primary
		new VoodooControl("div","css", "span[data-voodoo-name='team_name'] div:nth-of-type(2)").assertContains(sugar.users.getQAUser().get("userName"), true);
		new VoodooControl("div","css", "span[data-voodoo-name='team_name'] div:nth-of-type(2)").assertContains("Primary", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}