package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_30040 extends SugarTest {
	UserRecord chrisUser;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Module name is showing in the Process dashlets
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_30040_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Import process definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + "_7700.bpm");
		
		// Enable process definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
		
		// Create Process Business Rule,  Process Email Template
		sugar().processBusinessRules.create();
		sugar().alerts.getSuccess().closeAlert();
		sugar().processEmailTemplates.create();
		sugar().alerts.getSuccess().closeAlert();
		
		// Creating chris user with custom name to help debug failures
		FieldSet userFS = new FieldSet();
		userFS.put("userName",testName);
		userFS.put("lastName",testName);
		chrisUser = (UserRecord) sugar().users.create(userFS);
		
		// Update user type to admin to access process dashboards
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("select", "id", "UserType").set("System Administrator User");
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		VoodooUtils.waitForReady();
		
		// Logout admin and login with custom user
		sugar().logout();

		try {
			sugar().login(chrisUser);
			
			// Create account to trigger process
			sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
			sugar().accounts.createDrawer.getEditField("name").set(testName);
			sugar().accounts.createDrawer.save();
			
			// Create a new dashboard
			DataSource dashletsDS = testData.get(testName);
			sugar().navbar.navToModule(sugar().home.moduleNamePlural);
			sugar().dashboard.clickCreate();
			sugar().dashboard.getControl("title").set(testName);

			// Create Process dashlets Process, Definition, Email Template, Business Rule
			for (int i = 0; i < dashletsDS.size(); i++) {
				sugar().dashboard.addRow();
				sugar().dashboard.addDashlet(i+1, 1);            // TODO: VOOD-960 - Dashlet Selection
				VoodooUtils.waitForReady();
				new VoodooControl("a", "css", "[data-original-title='"
						+ dashletsDS.get(i).get("DashletName") + "'] a").click();
				VoodooUtils.waitForReady();
				new VoodooControl("a", "css", ".active .fld_save_button a").click();
				VoodooUtils.waitForReady();
			}
			sugar().dashboard.save();
			VoodooUtils.waitForReady();
			
			// TODO: VOOD-960 - Dashlet Selection
			// Verifying account name is displayed in processes dashlet
			new VoodooControl("ul", "css", ".dashlet-row .row-fluid.sortable:nth-of-type(1) .details").assertContains(testName, true);

			// Verifying Target Module name is displayed in process definition dashlet
			new VoodooControl("button", "css", "[data-original-title='All Process Definitions']").click();
			new VoodooControl("ul", "css", ".dashlet-row .row-fluid.sortable:nth-of-type(2) .details").assertContains(sugar.accounts.moduleNamePlural, true);

			// Verifying Target Module name is displayed in process bussiness rule
			new VoodooControl("button", "css", "[data-original-title='All Process Business Rules']").click();
			new VoodooControl("ul", "css", ".dashlet-row .row-fluid.sortable:nth-of-type(3) .details").assertContains(sugar.leads.moduleNamePlural, true);

			// Verifying Target Module name is displayed in process process Email template
			new VoodooControl("button", "css", "[data-original-title='All Process Email Templates']").click();
			VoodooUtils.waitForReady();
			new VoodooControl("ul", "css", ".dashlet-row .row-fluid.sortable:nth-of-type(4) .details").assertContains(sugar.leads.moduleNamePlural, true);
		} 
		finally	{ 
			// logout custom Admin user and login default admin
			sugar().logout();
			sugar().login();
		};

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}