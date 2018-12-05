package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30246 extends SugarTest {
	DataSource kbRecords = new DataSource();

	public void setup() throws Exception {
		kbRecords = testData.get(testName + "_kbRecords");
		sugar().knowledgeBase.api.create(kbRecords);
		sugar().cases.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Mass Update several fields without any error.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30246_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource massUpdateData = testData.get(testName);
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.toggleSelectAll();
		sugar().knowledgeBase.listView.openActionDropdown();
		sugar().knowledgeBase.listView.massUpdate();

		// Setting fields for mass update
		for (int i = 0; i < massUpdateData.size(); i++) {
			sugar().knowledgeBase.massUpdate.getControl("massUpdateField0" + (i+2)).set(massUpdateData.get(i).get("massUpdateKey"));
			sugar().knowledgeBase.massUpdate.getControl("massUpdateValue0" + (i+2)).set(massUpdateData.get(i).get("massUpdateValue"));
			if(i < massUpdateData.size()-1)
				sugar().knowledgeBase.massUpdate.addRow(i+2);
		}
		// Updating the record
		sugar().knowledgeBase.massUpdate.update();
		sugar().alerts.getSuccess().closeAlert();

		// Go to KB record view
		sugar().knowledgeBase.listView.clickRecord(1);
		VoodooControl externalArticleField = sugar().knowledgeBase.recordView.getDetailField("isExternal");
		VoodooControl relatedCaseField = sugar().knowledgeBase.recordView.getDetailField("relCase");
		VoodooControl relatedAssignedToField = sugar().knowledgeBase.recordView.getDetailField("relAssignedTo");

		// Verifying mass updated changes reflected in all record view of kb's as well
		for (int i = 0; i < kbRecords.size(); i++) {
			sugar().knowledgeBase.recordView.showMore();
			externalArticleField.assertChecked(true);
			relatedAssignedToField.assertEquals(massUpdateData.get(1).get("massUpdateValue"), true);
			relatedCaseField.assertEquals(massUpdateData.get(2).get("massUpdateValue"), true);
			if(i < kbRecords.size()-1)
				sugar().knowledgeBase.recordView.gotoNextRecord();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}