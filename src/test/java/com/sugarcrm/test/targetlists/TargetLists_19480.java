package com.sugarcrm.test.targetlists;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;


public class TargetLists_19480 extends SugarTest{
	TargetRecord mytarget;
	FieldSet targetDetails;

	public void setup() throws Exception {
		targetDetails =testData.get(testName).get(0);
		sugar.targetlists.api.create();
		mytarget =(TargetRecord)sugar.targets.api.create();
		sugar.login();		
	}

	/**
	 * Target List - Targets management_Verify that "Edit" function in "Targets" sub-panel works correctly.
	 */
	@Test
	public void TargetLists_19480_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);

		// Link Targets in TargetLists Subpanel	
		StandardSubpanel targetsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.targets.moduleNamePlural);
		targetsSubpanel.linkExistingRecord(mytarget);

		// Editing the Target Fields in TargetList Subpanel
		targetsSubpanel.editRecord(1);
		targetsSubpanel.getEditField(1,"firstName").set(targetDetails.get("firstName"));
		targetsSubpanel.getEditField(1,"lastName").set(targetDetails.get("lastName"));
		targetsSubpanel.getEditField(1,"title").set(targetDetails.get("title"));
		targetsSubpanel.saveAction(1);

		// Verifying the newly Modified Target is Displayed in TargetList Subpanel
		int targetRows = targetsSubpanel.countRows();
		Assert.assertTrue("Total no. of Rows not equal to 1", targetRows==1);

		// Verifying the Targets Modified Value is shown correctly
		targetsSubpanel.getDetailField(1,"fullName").assertEquals(targetDetails.get("firstName")+ " "
				+targetDetails.get("lastName"), true);
		targetsSubpanel.getDetailField(1,"title").assertEquals(targetDetails.get("title"), true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}