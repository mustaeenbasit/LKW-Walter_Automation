package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_29747 extends SugarTest {
	String oppPluralName, conPluralName = "";

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify user is able to rename modules.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_29747_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Rename Opportunity module.
		DataSource customData = testData.get(testName);
		String newOppName = customData.get(0).get("pluralLabel");
		sugar().admin.renameModule(sugar().opportunities, customData.get(0).get("singularLabel"), newOppName);

		// Rename Contacts module.
		String newConName = customData.get(1).get("pluralLabel");
		sugar().admin.renameModule(sugar().contacts, customData.get(1).get("singularLabel"), newConName);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify module name is renamed correctly in rename modules page for Opportunities
		// TODO: VOOD-1367
		new VoodooControl("span", "css", "span.module-opportunities-title.rename-slot-title").assertEquals(newOppName, true);

		// Verify module name is renamed correctly in rename modules page for Contacts
		new VoodooControl("span", "css", "span.module-contacts-title.rename-slot-title").assertEquals(newConName, true);
		VoodooUtils.focusDefault();

		// Verify  module's name should display correctly in subpanel view, Opportunities subpanel
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		oppPluralName = sugar().opportunities.moduleNamePlural;
		sugar().accounts.recordView.subpanels.get(oppPluralName).assertElementContains(newOppName, true);

		// Verify  module's name should display correctly in subpanel view, Contacts subpanel
		conPluralName = sugar().contacts.moduleNamePlural;
		sugar().accounts.recordView.subpanels.get(conPluralName).assertElementContains(newConName, true);

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Verify Module name should be renamed correctly in Studio page for Opportunities
		// TODO: VOOD-542
		new VoodooControl("a", "id", "studiolink_Opportunities").assertEquals(newOppName, true);

		// Verify Module name should be renamed correctly in Studio page for Contacts
		// TODO: VOOD-542
		new VoodooControl("a", "id", "studiolink_Contacts").assertEquals(newConName, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}