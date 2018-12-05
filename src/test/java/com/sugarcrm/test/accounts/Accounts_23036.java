package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23036 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that new Meeting is correctly created on "Meetings" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Accounts_23036_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		StandardSubpanel accountSubPanel = sugar().accounts.recordView.subpanels.get("Meetings");
		accountSubPanel.addRecord();
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.createDrawer.save();
		
		// Verify that new Schedule Meeting is displayed correctly on the detail view.
		FieldSet fs = new FieldSet();
		fs.put("name", sugar().meetings.getDefaultData().get("name"));
		fs.put("", sugar().meetings.getDefaultData().get("name"));
		accountSubPanel.verify(1, fs, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}