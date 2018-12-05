package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_26222 extends SugarTest {
	public void setup() throws Exception {	
		sugar().login();
		sugar().leads.api.create();
	}

	/**
	 * Stickyness in Leads record view
	 * @throws Exception
	 */
	@Test
	public void Leads_26222_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showMore();
		VoodooUtils.pause(1000);
		//Verify that Teams field is in the record view screen
		sugar().leads.recordView.getDetailField("relTeam").assertContains("Global", true);
		//Verify that Show Less link in the record view
		sugar().leads.recordView.getControl("showLess").assertVisible(true);
		//Navigate away to Accounts module
		sugar().accounts.navToListView();
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		//Verify that Teams field remains in the record view screen
		sugar().leads.recordView.getDetailField("relTeam").assertContains("Global", true);
		//Verify that Show Less link in the record view
		sugar().leads.recordView.getControl("showLess").assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
