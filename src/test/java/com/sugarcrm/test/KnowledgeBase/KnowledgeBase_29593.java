package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.KBRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.PortalTest;

public class KnowledgeBase_29593 extends PortalTest {
	KBRecord myKbRecord;
	
	public void setup() throws Exception {
		sugar().cases.api.create();
		myKbRecord = (KBRecord) sugar().knowledgeBase.api.create();
		sugar().login();
	}

	/**
	 * Verify preview can display correctly for Knowledge Base subpanel in Cases module
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29593_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Cases record view
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		
		// In Knowledge Base subpanel, link an existing KB (or create a KB)
		StandardSubpanel KBSubPanel = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);
		KBSubPanel.linkExistingRecord(myKbRecord);
		
		// Verify that the KB article displays in subpanel
		FieldSet fs = new FieldSet();
		fs.put("name", myKbRecord.get("name"));
		KBSubPanel.verify(1, fs, true);
		
		// Verify in KnowldegeBase SubPenl, click Preview button
		KBSubPanel.clickPreview(1);
		myKbRecord.verifyPreview();

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
} 