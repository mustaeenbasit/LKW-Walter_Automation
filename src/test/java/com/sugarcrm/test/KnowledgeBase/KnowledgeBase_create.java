package com.sugarcrm.test.KnowledgeBase;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.KBRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class KnowledgeBase_create extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	@Test
	public void KnowledgeBase_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		KBRecord kbRecord = (KBRecord)sugar().knowledgeBase.create();
		kbRecord.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}