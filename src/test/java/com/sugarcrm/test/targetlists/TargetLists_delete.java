package com.sugarcrm.test.targetlists;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.test.SugarTest;

public class TargetLists_delete extends SugarTest{
	TargetListRecord myTargetList;

	public void setup() throws Exception {
		myTargetList = (TargetListRecord)sugar.targetlists.api.create();
		sugar.login();
	}

	@Test
	public void TargetLists_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the target list using the UI.
		myTargetList.delete();

		// Verify the target list was deleted.
		sugar.targetlists.navToListView();
		assertEquals(VoodooUtils.contains(myTargetList.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
