package com.sugarcrm.test.KnowledgeBase;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class KnowledgeBase_30518 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that error message appears correctly when create a blank Category
	 *
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30518_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB list view
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// TODO: VOOD-1754 - Need Lib support for Categories in Knowledge Base Module
		// Click on the Create button
		new VoodooControl("a", "css", "a[name='add_node_button']").click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1437 and CB-252
		// Remove the default title "Default title" and Hit enter
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(" " + '\uE007');
		VoodooUtils.waitForReady();

		FieldSet fs = new FieldSet();
		fs = testData.get(testName).get(0);

		// Verify that error message appears
		sugar().alerts.getError().assertVisible(true);
		// Verify that the error message in the red message bar says "Error You cannot add a category without title".
		sugar().alerts.getError().assertEquals(fs.get("error_msg"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}