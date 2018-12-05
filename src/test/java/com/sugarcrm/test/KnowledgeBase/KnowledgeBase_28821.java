package com.sugarcrm.test.KnowledgeBase;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

/**
 * @author Basant Chandak <bchandak@sugarcrm.com>
 */
public class KnowledgeBase_28821 extends SugarTest {
	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Knowledge Base: make sure user is directed back after using "Create Article" on Cases record
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28821_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		sugar().cases.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-695
		// Click Create Article link from Action Dropdown
		new VoodooControl("a", "css", ".detail.fld_create_button .rowaction[name='create_button']").click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.createDrawer.cancel();

		// Verify user is navigated to back to same case record
		sugar().cases.recordView.assertVisible(true);
		sugar().cases.recordView.getDetailField("name").assertEquals(sugar().cases.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
