package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Calls_30865 extends SugarTest {
	DataSource customData = new DataSource();

	public void setup() throws Exception {
		FieldSet roleRecorddata = testData.get("env_role_setup").get(0);
		customData = testData.get(testName);

		// Create role with bugs acess type disabled
		sugar().login();  
		AdminModule.createRole(roleRecorddata);
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		new VoodooControl("div", "css", "td #ACLEditView_Access_Bugs div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td #ACLEditView_Access_Bugs div select").set(customData.get(0).get("access"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign role to the created user
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecorddata);
		VoodooUtils.waitForReady();
		sugar().logout();		
	}

	/**
	 * Verify that Related to drop down field should not be disabled in Calls and Meetings module when user has no access on Bugs module
	 * @throws Exception
	 */
	@Test
	public void Calls_30865_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login with the created user and navigate to calls module
		sugar().login(sugar().users.getQAUser());
		sugar().calls.navToListView();
		sugar().calls.listView.create();

		// Verify that in the "related-to" field, bugs module is not present
		// TODO: VOOD-629 Add support for accessing and manipulating individual components of a VoodooSelect
		// TODO: VOOD-1488 Support Flex "Related to" control (dropdown+field) for all layouts (views and filters)
		new VoodooControl("span", "css", ".flex-relate-module .select2-choice .select2-arrow").click();
		new VoodooControl("input", "css", "#select2-drop .select2-search input").set(sugar().bugs.moduleNamePlural);
		new VoodooControl("ul", "css", "#select2-drop .select2-results").assertEquals(customData.get(0).get("message"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}