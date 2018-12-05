package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29768 extends SugarTest {
	String oldApprovedBy = "";
	VoodooControl approvedByEditCtrl;

	public void setup() throws Exception {
		// Pre-requisite: KB record exist having Approved By data.
		sugar().knowledgeBase.api.create();

		// Login as Admin
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB module & add "Approved By" data in existing record.
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);	
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.showMore();
		approvedByEditCtrl = sugar().knowledgeBase.recordView.getEditField("approvedBy");
		oldApprovedBy = sugar().users.getQAUser().get("userName");
		approvedByEditCtrl.set(oldApprovedBy);
		sugar().knowledgeBase.recordView.save();
	}

	/**
	 * KB:Verify "Approve by" field should be shown  correctly in view change log.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29768_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on Edit
		sugar().knowledgeBase.recordView.edit();

		// Change the Approve By Field->Click on save.
		FieldSet customData = testData.get(testName).get(0);
		approvedByEditCtrl.set(customData.get("newApprovedBy"));

		// Save
		sugar().knowledgeBase.recordView.save();

		// Navigate to-> View change log.
		sugar().knowledgeBase.recordView.openPrimaryButtonDropdown();
		// TODO: VOOD-738
		new VoodooControl("a", "css", "[name='audit_button']").click();

		VoodooUtils.waitForReady();
		
		// Verify the new value and old value of approve by fields.
		// TODO: VOOD-1990
		String value = ".list-view table tbody tr td:nth-child(%d) span div";

		// Old Value	
		new VoodooControl("div", "css", String.format(value, 2)).assertEquals(oldApprovedBy, true);

		// New Value
		new VoodooControl("div", "css", String.format(value, 3)).assertEquals(customData.get("newApprovedBy"), true);  

		// Close
		new VoodooControl("a", "css", "[name='close_button']").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}