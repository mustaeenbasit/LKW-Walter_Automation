package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28813 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify correct default fields are present in studio for KB
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28813_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource kbFields = testData.get(testName);

		// Navigate to Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1504 - Support Studio Module Fields View
		// Navigate to the Knowledge Base' Fields view in Studio
		new VoodooControl("a", "id", "studiolink_KBContents").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();

		// Verify correct default fields are present in studio for KB
		for(int i=0;i<kbFields.size();i++){
			new VoodooControl("td", "css", "#field_table tr:nth-child("+ (i+2) + ") td:nth-child(2)").
			assertEquals(kbFields.get(i).get("fieldsKB"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}