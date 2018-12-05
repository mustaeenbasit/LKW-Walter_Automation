package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21445 extends SugarTest {
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Knowledge Base_edit article
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21445_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to article's record view
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		// Click "Edit"
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.showMore();
		// Modify fields
		FieldSet customData = testData.get(testName).get(0);
		VoodooControl editNameFieldCtrl = sugar().knowledgeBase.recordView.getEditField("name");
		editNameFieldCtrl.set(customData.get("name"));
		VoodooControl editExpiryDateCtrl = sugar().knowledgeBase.recordView.getEditField("date_expiration");
		editExpiryDateCtrl.set(customData.get("date_expiration1"));
		VoodooControl editPublishDateCtrl = sugar().knowledgeBase.recordView.getEditField("date_publish");
		editPublishDateCtrl.set(customData.get("date_publish1"));
		VoodooControl editStatusFieldCtrl = sugar().knowledgeBase.recordView.getEditField("status");
		editStatusFieldCtrl.set(customData.get("status1"));
		// Save
		sugar().knowledgeBase.recordView.save();

		// Verify modified data is correctly saved.
		VoodooControl nameFieldCtrl = sugar().knowledgeBase.recordView.getDetailField("name");
		nameFieldCtrl.assertEquals(customData.get("name"), true);
		VoodooControl expiryDateCtrl = sugar().knowledgeBase.recordView.getDetailField("date_expiration");
		expiryDateCtrl.assertEquals(customData.get("date_expiration1"), true);
		VoodooControl publishDateCtrl = sugar().knowledgeBase.recordView.getDetailField("date_publish");
		publishDateCtrl.assertEquals(customData.get("date_publish1"),true);
		VoodooControl statusFieldCtrl = sugar().knowledgeBase.recordView.getDetailField("status");
		statusFieldCtrl.assertEquals(customData.get("status1"), true);

		// Click Edit button again
		sugar().knowledgeBase.recordView.edit();

		// Modify fields
		editNameFieldCtrl.set(customData.get("name")+"_"+testName);
		editExpiryDateCtrl.set(customData.get("date_expiration2"));
		editPublishDateCtrl.set(customData.get("date_publish2"));
		editStatusFieldCtrl.set(customData.get("status2"));

		// Cancel
		sugar().knowledgeBase.recordView.cancel();

		// Verify no changes are saved
		nameFieldCtrl.assertEquals(customData.get("name"), true);
		expiryDateCtrl.assertEquals(customData.get("date_expiration1"), true);
		publishDateCtrl.assertEquals(customData.get("date_publish1"),true);
		statusFieldCtrl.assertEquals(customData.get("status1"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}