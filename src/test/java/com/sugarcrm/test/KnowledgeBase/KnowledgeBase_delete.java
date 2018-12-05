package com.sugarcrm.test.KnowledgeBase;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.KBRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KnowledgeBase_delete extends SugarTest {
	KBRecord kbRecord;
	
	public void setup() throws Exception {
		kbRecord = (KBRecord)sugar().knowledgeBase.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	@Test
	public void KnowledgeBase_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		kbRecord.delete();

		// Verify the kbrecord was deleted.
		sugar().knowledgeBase.navToListView();
		assertEquals(VoodooUtils.contains(kbRecord.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
