package com.sugarcrm.test.KnowledgeBase;

import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30549 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that the error message appears correctly when expiration date is after published date
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30549_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		// Navigate to KB list view
		sugar().knowledgeBase.navToListView();
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createArticle");
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.showMore();
		
		// Setting today's date for publish and tomorrow date for expiration.
		sugar().knowledgeBase.createDrawer.getEditField("date_publish").set(DateTime.now().toString("MM/dd/yyyy"));
		sugar().knowledgeBase.createDrawer.getEditField("date_expiration").set(DateTime.now().plusDays(1).toString("MM/dd/yyyy"));
		sugar().knowledgeBase.createDrawer.save();
		
		// Edit record with status Draft to Approved
		sugar().knowledgeBase.listView.editRecord(1);
		sugar().knowledgeBase.listView.getEditField(1, "status").set(customData.get("status"));
		sugar().knowledgeBase.listView.saveRecord(1);
		
		// Verifying alert error visible with after saving record with status approved and publish date = today.
		sugar().alerts.getError().assertVisible(true);
		sugar().alerts.getError().assertEquals(customData.get("errorMessage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}