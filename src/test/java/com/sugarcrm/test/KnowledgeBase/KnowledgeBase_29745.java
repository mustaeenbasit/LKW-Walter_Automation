package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29745 extends SugarTest {

	public void setup() throws Exception {
		// Create KB record, Need one existing KB record
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB module.
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);

		// Edit Record
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.showMore();

		// Change it to useful
		// TODO: VOOD-1783 
		new VoodooControl("a", "css", ".detail.fld_usefulness a:nth-child(2)").click();

		// Save
		sugar().knowledgeBase.recordView.save();
	}

	/**
	 * KB: Changing from Useful to Not Useful should not show error while saving the record.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29745_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit Record-> change it to "Not useful”.
		sugar().knowledgeBase.recordView.edit();
		// TODO: VOOD-1783 
		new VoodooControl("a", "css", ".detail.fld_usefulness a:nth-child(1)").click();

		// Save
		sugar().knowledgeBase.recordView.save();

		// Verify no Error message "Error:Invalid field value:usefulness_user_vote in module:KB contents".
		sugar().alerts.getError().assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}