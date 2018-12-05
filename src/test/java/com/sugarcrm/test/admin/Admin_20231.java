package com.sugarcrm.test.admin;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20231 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify that 'Related To' Field for Notes is Syncing with Updated Module Name
	 * 
	 * @throws Exception
	 */
	@Test
	@Ignore("TR-2215 - (SI 67574) not all module name changes reflect on field level")
	public void Admin_20231_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.renameModule(sugar().accounts, customData.get("singularName"), customData.get("pluralName"));
		
		// Verify that 'Related To' Field for Notes Sync with Updated Module Name
		sugar().notes.navToListView();
		sugar().notes.listView.create();
		sugar().notes.recordView.getEditField("relRelatedToModule").assertEquals(customData.get("singularName"), true);
		sugar().notes.recordView.getEditField("relRelatedToModule").click();
		new VoodooControl("ul", "css", "div.select2-drop-active ul.select2-results").assertElementContains(customData.get("singularName"), true);
		new VoodooControl("ul", "css", "div.select2-drop-active ul.select2-results").assertElementContains(sugar().accounts.moduleNameSingular, false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
