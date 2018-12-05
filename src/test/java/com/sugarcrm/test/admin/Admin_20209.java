package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Admin_20209 extends SugarTest {

	public void setup() throws Exception {
		//moduleName = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that changing module name will sync to corresponding subpanel on a
	 * record detail view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20209_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Update Contacts module name, set "Plural Label" as "Cons"
		FieldSet moduleName = testData.get(testName).get(0);
		String modulePluralName = moduleName.get("pluralLabel");
		sugar().admin.renameModule(sugar().contacts, moduleName.get("singularLabel"), modulePluralName);

		// Navigate to Accounts record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel conSub  = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);

		// Verify new module name sync to corresponding subpanel
		conSub.getControl("subpanelName").assertEquals(modulePluralName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}