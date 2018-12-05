package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26094 extends SugarTest {
	FieldSet ds;

	public void setup() throws Exception {
		ds = testData.get(testName).get(0);
		sugar.login();	
	}

	/**
	 * Quick create record-Check the default team info.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26094_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Click a quick create icon from the shortcut bar, such as Account.
		sugar.navbar.quickCreateAction(sugar.accounts.moduleNamePlural);
		sugar.accounts.createDrawer.getEditField("name").set(sugar.accounts.getDefaultData().get("name"));
		
		// Save and verify Account record' team.
		sugar.accounts.createDrawer.save();
		
		// Verify that the record's team info is the same as the user's default team.
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.recordView.showMore();
		sugar.accounts.recordView.getDetailField("relTeam").assertContains(ds.get("team"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
