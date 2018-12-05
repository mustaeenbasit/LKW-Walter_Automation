package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30536 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that KB module appears the Default module selection list in the Lead convert
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30536_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		// Go to studio->Leads->Layouts->Lead Convert
		new VoodooControl("a", "id", "studiolink_Leads").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		
		// Verify that the KB is available in the dropDown list
		new VoodooSelect("select", "css", "#convertSelectNewModule").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("option", "css", "#convertSelectNewModule option[value='"+sugar().knowledgeBase.moduleNamePlural+"']").assertExists(true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}