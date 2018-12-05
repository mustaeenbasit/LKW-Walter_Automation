package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;
public class KnowledgeBase_28861 extends SugarTest {
	FieldSet roleRecord = new FieldSet();

	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		roleRecord = testData.get("env_role_setup").get(0);
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create a role with default permissions and assign qauser to the default role
		AdminModule.createRole(roleRecord);
		AdminModule.assignUserToRole(roleRecord);
		sugar().logout();
	}

	/**
	 * Verify "Useful"/"Not Useful" links/buttons are available to non-owners
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28861_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login with qauser
		sugar().login(sugar().users.qaUser);

		// Navigate to KnowledgeBase list view and open first record
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);

		// Click showMore on KnowledgeBase edit view
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.showMore();

		// Asserting the visibility of Useful / NotUseful button 
		// TODO: VOOD-1783 
		new VoodooControl("a", "css", ".detail.fld_usefulness a").assertVisible(true);
		new VoodooControl("a", "css", ".detail.fld_usefulness a:nth-child(2)").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}