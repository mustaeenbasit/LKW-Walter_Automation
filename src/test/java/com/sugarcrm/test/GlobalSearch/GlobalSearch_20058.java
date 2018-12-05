package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_20058 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that default modules are enabled in global search in Admin settings
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_20058_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin -> search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("globalSearch").click();
		VoodooUtils.waitForReady();

		DataSource customDS = testData.get(testName);
		for(int i = 0; i < customDS.size(); i++)
			// TODO: VOOD-1860
			// Verify that Verify that default enabled modules are:
			// Accounts, Bugs, calls, campaigns, Cases, Contacts, Contracts, Documents, Emails, Employees, Knowledge Base, Leads, 
			// Manufacturers, Meetings, Notes, Opportunities, Product categories, Quoted Line Items, projects, Project Tasks, 
			// Target Lists, Targets, Quotes, Revenue Line Items, Tags, Tasks are in enabled modules by default
			new VoodooControl("tr", "css", "#enabled_div tbody[tabindex='0'] tr:nth-of-type("+(i+1)+")").assertContains(customDS.get(i).get("moduleName"), true);
		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}