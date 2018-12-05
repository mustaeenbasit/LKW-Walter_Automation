package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.test.SugarTest;

public class TargetLists_create extends SugarTest{
	public void setup() throws Exception {
		sugar.login();
	}

	@Test
	public void TargetLists_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		TargetListRecord myTargetList = (TargetListRecord)sugar.targetlists.create();
		myTargetList.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}