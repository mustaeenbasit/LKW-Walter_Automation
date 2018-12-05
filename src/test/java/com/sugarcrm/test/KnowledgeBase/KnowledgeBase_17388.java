package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_17388 extends SugarTest {

	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * In record creation page, right side displaying instruction/help on how to create record
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_17388_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB list view
		sugar().knowledgeBase.navToListView();
		// Click on the Create button
		sugar().knowledgeBase.listView.create();

		FieldSet kbCreateDrawerDashboardInfo = testData.get(testName).get(0);

		// Assert that the KB record editview is opening.  At the right side of the page, displaying Instruction/Help on how to create a record on this module
		sugar().knowledgeBase.createDrawer.assertVisible(true);

		// TODO: VOOD-1754 - Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer
		new VoodooControl("span", "css", ".layout_KBContents.drawer.active .dashboard .detail").assertEquals(kbCreateDrawerDashboardInfo.get("helpText"), true);
		new VoodooControl("h4", "css", ".layout_KBContents.drawer.active .dashboard .dashlet-title").assertEquals(kbCreateDrawerDashboardInfo.get("dashletTitle"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}