package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21448 extends SugarTest {
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();

		// Login
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * duplicate_article
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21448_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Go to article's record view
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		// Click "Copy"
		sugar().knowledgeBase.recordView.copy();
		VoodooControl nameFldCtrl = sugar().knowledgeBase.createDrawer.getEditField("name");
		nameFldCtrl.set(customData.get("name"));
		sugar().knowledgeBase.createDrawer.showMore();
		VoodooControl expiryDateCtrl = sugar().knowledgeBase.createDrawer.getEditField("date_expiration");
		expiryDateCtrl.set(customData.get("date_expiration"));
		VoodooControl publishDateCtrl = sugar().knowledgeBase.createDrawer.getEditField("date_publish");
		publishDateCtrl.set(customData.get("date_publish"));
		VoodooControl statusFldCtrl = sugar().knowledgeBase.createDrawer.getEditField("status");
		statusFldCtrl.set(customData.get("status"));
		// Save
		sugar().knowledgeBase.createDrawer.save();

		// Navigate to original article
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.sortBy("headerName", true);
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.showMore();

		// Verify that the information of the original article is not updated
		FieldSet fs = sugar().knowledgeBase.getDefaultData();
		VoodooControl nameFieldCtrl = sugar().knowledgeBase.recordView.getDetailField("name");
		nameFieldCtrl.assertEquals(fs.get("name"), true);
		VoodooControl bodyFieldCtrl = sugar().knowledgeBase.recordView.getDetailField("body");
		bodyFieldCtrl.assertEquals(fs.get("body"), true);
		VoodooControl expDateCtrl = sugar().knowledgeBase.recordView.getDetailField("date_expiration");
		expDateCtrl.assertEquals(fs.get("date_expiration"), true);
		VoodooControl pubDateCtrl = sugar().knowledgeBase.recordView.getDetailField("date_publish");
		pubDateCtrl.assertEquals(fs.get("date_publish"),true);
		VoodooControl statusFieldCtrl = sugar().knowledgeBase.recordView.getDetailField("status");
		statusFieldCtrl.assertEquals(fs.get("status"), true);

		// Navigate to the duplicated article 
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(2);
		sugar().knowledgeBase.recordView.showMore();

		// Verify that the duplicated article is saved correctly
		nameFieldCtrl.assertEquals(customData.get("name"), true);
		expDateCtrl.assertEquals(customData.get("date_expiration"), true);
		pubDateCtrl.assertEquals(customData.get("date_publish"),true);
		statusFieldCtrl.assertEquals(customData.get("status"), true); 

		// Navigate to original article
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.sortBy("headerName", true);
		sugar().knowledgeBase.listView.clickRecord(1);
		// Click Copy
		sugar().knowledgeBase.recordView.copy();
		sugar().knowledgeBase.createDrawer.showMore();
		nameFldCtrl.set(customData.get("name")+"_"+testName);
		expiryDateCtrl.set(customData.get("date_expiration"));
		publishDateCtrl.set(customData.get("date_publish"));
		statusFldCtrl.set(customData.get("status"));

		// Click Cancel
		sugar().knowledgeBase.createDrawer.cancel();

		// Navigate to original article
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.sortBy("headerName", true);
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.showMore();

		// Verify that the information of the original article is not updated
		nameFieldCtrl.assertEquals(fs.get("name"), true);
		bodyFieldCtrl.assertEquals(fs.get("body"), true);
		expDateCtrl.assertEquals(fs.get("date_expiration"), true);
		pubDateCtrl.assertEquals(fs.get("date_publish"),true);
		statusFieldCtrl.assertEquals(fs.get("status"), true);

		// Verify that duplicate article is not created
		sugar().knowledgeBase.navToListView();
		Assert.assertTrue("Total rows is not equal to 2" , sugar().knowledgeBase.listView.countRows() == 2);
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(customData.get("name")+"_"+testName, false);
		sugar().knowledgeBase.listView.getDetailField(2, "name").assertEquals(customData.get("name")+"_"+testName, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}