package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29977 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to Studio -> KB-> Fields -> Mark Body field as Required
		// TODO: VOOD-1504
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "studiolink_KBContents").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "kbdocument_body").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='required']").click();
		new VoodooControl("input", "css", "[name='fsavebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * User should able to enter text in Body field if it is a Required field,
	 * once it is left blank in Knowledge Base Module.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29977_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet errorMsg = testData.get(testName).get(0);

		// Navigate to Knowledge Base and Create a KB leaving body field as blank
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.save();

		// Verify Error is displayed when required field Body is left blank
		sugar().alerts.getError().assertVisible(true);
		sugar().alerts.getError().assertEquals(errorMsg.get("errorMessage"), true);

		// Enter some value in the KB Body field.
		VoodooUtils.focusFrame(0);
		sugar().knowledgeBase.createDrawer.getEditField("body").set(testName);
		VoodooUtils.focusDefault();

		// Navigate to any other module (for e.g. Accounts)
		sugar().accounts.navToListView();

		// Verify Warning is displayed
		sugar().alerts.getWarning().assertVisible(true);

		// Confirm, the Warning displayed
		sugar().alerts.getWarning().confirmAlert();

		// Verify user is navigated to the Accounts module.
		sugar().accounts.listView.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}