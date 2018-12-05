package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21478 extends SugarTest {
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();

		// Login
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify Actions dropdown list in article record view 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21478_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB record view
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);

		// Verify that Edit button is visible
		sugar().knowledgeBase.recordView.getControl("editButton").assertVisible(true);

		// Verify that Create Localization, Create Revision, Share, Copy, View Change Log, Delete are available
		sugar().knowledgeBase.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-695
		new VoodooControl("a","css", ".fld_share.detail a").assertVisible(true);
		new VoodooControl("a", "css", "[name='create_localization_button']").assertVisible(true);
		new VoodooControl("a", "css", "[name='create_revision_button']").assertVisible(true);
		new VoodooControl("a","css", ".fld_audit_button a").assertVisible(true);
		sugar().knowledgeBase.recordView.getControl("copyButton").assertVisible(true);
		VoodooControl deleteButton= sugar().knowledgeBase.recordView.getControl("deleteButton");
		deleteButton.assertVisible(true);

		// Clicking on Delete action and verifying it is triggering the desired action
		deleteButton.click();
		sugar().alerts.getWarning().confirmAlert();
		int rows = sugar().knowledgeBase.listView.countRows();
		Assert.assertTrue("No. of rows is not equal to Zero", rows == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}