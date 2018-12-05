package com.sugarcrm.test.KnowledgeBase;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.KBRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class KnowledgeBase_update extends SugarTest {
	KBRecord kbRecord;
	
	public void setup() throws Exception {
		kbRecord = (KBRecord)sugar().knowledgeBase.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	@Test
	public void KnowledgeBase_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet editData = new FieldSet();
		editData.put("name", "Edited Article Name");
		kbRecord.edit(editData);
		kbRecord.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}