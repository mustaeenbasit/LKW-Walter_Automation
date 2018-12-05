package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29763 extends SugarTest {
	VoodooControl usefulCtrl;

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

		// Set it to useful
		// TODO: VOOD-1783 
		usefulCtrl = new VoodooControl("a", "css", ".detail.fld_usefulness a:nth-child(2)");
		usefulCtrl.click();

		// Save
		sugar().knowledgeBase.recordView.save();
	}

	/**
	 * Verify user should be able to click on Useful/Not Useful in KB edit page.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29763_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit Record
		sugar().knowledgeBase.recordView.edit();

		// Verify record is already set as useful
		FieldSet customData = testData.get(testName).get(0);
		usefulCtrl.assertAttribute("class", customData.get("activeClass"), true);

		// Change it to "Not useful”.
		// TODO: VOOD-1783 
		VoodooControl notUsefulCtrl = new VoodooControl("a", "css", ".detail.fld_usefulness a");
		notUsefulCtrl.click();
		VoodooUtils.waitForReady();

		// Verify record is edited as "Not Useful"
		notUsefulCtrl.assertAttribute("class", customData.get("activeClass"), true);

		// Verify useful button turns inactive.
		usefulCtrl.assertAttribute("class", customData.get("activeClass"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}