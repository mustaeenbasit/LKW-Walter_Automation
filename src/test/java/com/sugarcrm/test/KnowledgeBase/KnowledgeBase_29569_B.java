package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29569_B extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify Publish Date field behavior in relation to KB Article Status
	 * Status = "Approved", "Published"
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29569_B_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().knowledgeBase.navToListView();
		// Create a new KB article
		sugar().knowledgeBase.listView.create();
		DataSource customData = testData.get(testName);
		
		// Fill name
		VoodooControl nameEditCtrl = sugar().knowledgeBase.createDrawer.getEditField("name");
		nameEditCtrl.set(customData.get(0).get("name"));

		// Select status = "Approved"
		VoodooControl statusEditCtrl = sugar().knowledgeBase.createDrawer.getEditField("status");
		statusEditCtrl.set(customData.get(0).get("status"));
		sugar().knowledgeBase.createDrawer.showMore();
		VoodooUtils.waitForReady();

		// Set Publish Date = todaysDate , past date and future date
		String todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		VoodooControl publishDateEditCtrl = sugar().knowledgeBase.createDrawer.getEditField("date_publish");
		VoodooControl publishDateDetailCtrl = sugar().knowledgeBase.recordView.getDetailField("date_publish");
		VoodooControl statusDetailCtrl = sugar().knowledgeBase.recordView.getDetailField("status");
		VoodooControl errIconCtrl = new VoodooControl("i", "css", ".error-tooltip.add-on .fa.fa-exclamation-circle");
		VoodooControl toolTipValueCtrl = new VoodooControl("span", "css", ".error-tooltip");
		for (int j = 0 ; j < customData.size()-3 ; j++){
			if (j == 2)
				publishDateEditCtrl.set(todaysDate);
			else
				publishDateEditCtrl.set(customData.get(j).get("date_publish"));

			sugar().knowledgeBase.createDrawer.save();

			// Verify user can save with publish date as future date for "Approved" status
			if (j == 1){
				sugar().knowledgeBase.listView.clickRecord(1);
				publishDateDetailCtrl.assertEquals(customData.get(j).get("date_publish"), true);
				statusDetailCtrl.assertEquals(customData.get(0).get("status"), true);
			}

			// Verify User cannot save with Publish Date as current date & past date
			// while KB article Status is in: Approved
			else{
				sugar().alerts.getError().assertVisible(true);
				sugar().alerts.getError().closeAlert();
				// TODO: VOOD-1292
				errIconCtrl.hover();
				toolTipValueCtrl.assertVisible(true);
				toolTipValueCtrl.assertAttribute("data-original-title", customData.get(0).get("tooltipText"), true);
				sugar().knowledgeBase.createDrawer.cancel();
			}

			if (j ==2)
				break;

			sugar().knowledgeBase.navToListView();
			sugar().knowledgeBase.listView.create();
			nameEditCtrl.set(customData.get(j+1).get("name"));
			statusEditCtrl.set(customData.get(0).get("status"));
		}

		sugar().knowledgeBase.navToListView();
		// Create a new KB article
		sugar().knowledgeBase.listView.create();
		nameEditCtrl.set(customData.get(3).get("name"));
		// Select status = "Published"
		statusEditCtrl.set(customData.get(1).get("status"));
		sugar().knowledgeBase.createDrawer.showMore();
		VoodooUtils.waitForReady();

		// Set Publish Date = todaysDate , past date and future date
		for (int j = 3 ; j < customData.size() ; j++){
			if (j == 5)
				publishDateEditCtrl.set(todaysDate);
			else
				publishDateEditCtrl.set(customData.get(j).get("date_publish"));
			sugar().knowledgeBase.createDrawer.save();

			// Verify User can save with Publish Date as current date/past date/future date 
			// while KB article Status is in: Published, but Publish date will automatically change to Today's Date.
			sugar().knowledgeBase.listView.sortBy("headerName", false);
			sugar().knowledgeBase.listView.clickRecord(1);
			publishDateDetailCtrl.assertEquals(todaysDate, true);
			statusDetailCtrl.assertEquals(customData.get(1).get("status"), true);
			if (j == 5)
				break;
			sugar().knowledgeBase.navToListView();
			sugar().knowledgeBase.listView.create();
			nameEditCtrl.set(customData.get(j+1).get("name"));
			statusEditCtrl.set(customData.get(1).get("status"));
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}