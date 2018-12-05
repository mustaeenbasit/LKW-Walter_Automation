package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29668 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that new and old values are displayed for “Body” field under “View Change Log” for Knowledge base.
	 *   
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29668_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Knowledge Base Module and Click on Action dropdown and hit “Create Article”.
		FieldSet customFS = testData.get(testName).get(0);
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createArticle");
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		VoodooUtils.focusFrame("mce_0_ifr");
		sugar().knowledgeBase.createDrawer.getEditField("body").set(customFS.get("bodyContent"));
		VoodooUtils.focusDefault();
		sugar().knowledgeBase.createDrawer.save();

		// Go to record view of same record and click on Edit button.
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.edit();

		// Change Body = test123 and click on Save.
		VoodooUtils.focusFrame("mce_9_ifr");
		sugar().knowledgeBase.recordView.getEditField("body").set(customFS.get("bodyContentNew"));
		VoodooUtils.focusDefault();
		sugar().knowledgeBase.recordView.save();

		// Click "View Change Log" button
		sugar().knowledgeBase.recordView.openPrimaryButtonDropdown();
		// TODO: VOOD-738
		new VoodooControl("a", "css", ".detail.fld_audit_button a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1990
		// Verify that the new and Old Values should be displayed.
		new VoodooControl("div", "css", ".list.fld_before div").assertEquals(customFS.get("bodyContent"), true);
		new VoodooControl("div", "css", ".list.fld_after div").assertEquals(customFS.get("bodyContentNew"), true);  

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}