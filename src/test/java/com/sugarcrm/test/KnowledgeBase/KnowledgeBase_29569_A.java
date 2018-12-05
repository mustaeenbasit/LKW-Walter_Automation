package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29569_A extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify Publish Date field behavior in relation to KB Article Status
	 * Status = "Draft", "In Review", "Expired"
	 * & Empty Publish Date for all status
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29569_A_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify user can save with any Publish Date value
		// while KB article Status is in: Draft
		sugar().knowledgeBase.navToListView();
		// Create a new KB article
		sugar().knowledgeBase.listView.create();
		DataSource customData = testData.get(testName);
		// Fill name
		VoodooControl nameEditCtrl = sugar().knowledgeBase.createDrawer.getEditField("name");
		nameEditCtrl.set(customData.get(0).get("name"));

		// Select status = "Draft"
		VoodooControl statusEditCtrl = sugar().knowledgeBase.createDrawer.getEditField("status");
		statusEditCtrl.set(customData.get(0).get("status"));
		sugar().knowledgeBase.createDrawer.showMore();
		VoodooUtils.waitForReady();

		// Set Publish Date = todaysDate , past date and future date
		String todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		VoodooControl publishDateEditCtrl = sugar().knowledgeBase.createDrawer.getEditField("date_publish");
		VoodooControl publishDateDetailCtrl = sugar().knowledgeBase.recordView.getDetailField("date_publish");
		VoodooControl statusDetailCtrl = sugar().knowledgeBase.recordView.getDetailField("status");
		for (int j = 0 ; j < customData.size()-6 ; j++){
			if (j == 2) 
				// Setting Publish Date = today's date
				publishDateEditCtrl.set(todaysDate);
			else
				// Setting Publish Date = past/future date 
				publishDateEditCtrl.set(customData.get(j).get("date_publish"));

			sugar().knowledgeBase.createDrawer.save();

			// Verify User can save with Publish Date as 
			// current date/past date/future date while KB article Status is in: Draft
			sugar().knowledgeBase.listView.sortBy("headerName", false);
			sugar().knowledgeBase.listView.clickRecord(1);
			if (j == 2)
				// Verifying for today's date & status = "Draft"
				publishDateDetailCtrl.assertEquals(todaysDate, true);
			else
				// Verifying for past/future date & status = "Draft"
				publishDateDetailCtrl.assertEquals(customData.get(j).get("date_publish"), true);

			statusDetailCtrl.assertEquals(customData.get(0).get("status"), true);
			if (j ==2)
				break;

			sugar().knowledgeBase.navToListView();
			sugar().knowledgeBase.listView.create();
			nameEditCtrl.set(customData.get(j+1).get("name"));
		}

		// Verify user can save with any Publish Date value
		// while KB article Status is in: Review
		sugar().knowledgeBase.navToListView();
		// Create a new KB article
		sugar().knowledgeBase.listView.create();
		nameEditCtrl.set(customData.get(3).get("name"));
		// Select status = "In Review"
		statusEditCtrl.set(customData.get(1).get("status"));
		sugar().knowledgeBase.createDrawer.showMore();
		VoodooUtils.waitForReady();

		// Set Publish Date = todaysDate , past date and future date
		for (int j = 3 ; j < customData.size()-3 ; j++){
			if (j == 5)
				// Setting publish date = today's date
				publishDateEditCtrl.set(todaysDate);
			else
				// Setting publish date = past/future date
				publishDateEditCtrl.set(customData.get(j).get("date_publish"));
			sugar().knowledgeBase.createDrawer.save();

			// Verify User can save with Publish Date as current date/past date/future date while KB article Status is in: In Review 
			sugar().knowledgeBase.listView.sortBy("headerName", false);
			sugar().knowledgeBase.listView.clickRecord(1);
			if (j == 5)
				// Verifying for publish date = today's date & status = "In Review"
				publishDateDetailCtrl.assertEquals(todaysDate, true);
			else
				// Verifying for publish date = past/future date & status = "In Review"
				publishDateDetailCtrl.assertEquals(customData.get(j).get("date_publish"), true);
			statusDetailCtrl.assertEquals(customData.get(1).get("status"), true);
			if (j == 5)
				break;
			sugar().knowledgeBase.navToListView();
			sugar().knowledgeBase.listView.create();
			nameEditCtrl.set(customData.get(j+1).get("name"));
			statusEditCtrl.set(customData.get(1).get("status"));
		}

		// Verify user can save with any Publish Date value
		// while KB article Status is in: Expired
		sugar().knowledgeBase.navToListView();
		// Create a new KB article
		sugar().knowledgeBase.listView.create();
		nameEditCtrl.set(customData.get(6).get("name"));
		// Select status = "Expired"
		statusEditCtrl.set(customData.get(2).get("status"));
		sugar().knowledgeBase.createDrawer.showMore();
		VoodooUtils.waitForReady();

		// Set Publish Date = todaysDate , past date and future date
		for (int j = 6 ; j < customData.size() ; j++){
			if (j == 8)
				// Setting publish date = today's date
				publishDateEditCtrl.set(todaysDate);
			else
				// Setting publish date = past/future date
				publishDateEditCtrl.set(customData.get(j).get("date_publish"));
			sugar().knowledgeBase.createDrawer.save();

			// Verify User can save with Publish Date as current date/past date/future date while KB article Status is in: Expired
			sugar().knowledgeBase.listView.sortBy("headerName", false);
			sugar().knowledgeBase.listView.clickRecord(1);
			sugar().knowledgeBase.recordView.showMore();
			if (j == 8)
				// Verifying for publish date = today's date & status = Expired
				publishDateDetailCtrl.assertEquals(todaysDate, true);
			else
				// Verifying for publish date = past/future date & status = Expired
				publishDateDetailCtrl.assertEquals(customData.get(j).get("date_publish"), true);
			statusDetailCtrl.assertEquals(customData.get(2).get("status"), true);
			if (j == 8)
				break;
			sugar().knowledgeBase.navToListView();
			sugar().knowledgeBase.listView.create();
			nameEditCtrl.set(customData.get(j+1).get("name"));
			statusEditCtrl.set(customData.get(2).get("status"));
		}

		// Verify empty publish date field for different status
		// TODO: VOOD-1349
		VoodooControl publishDateValueCtrl = new VoodooControl ("span", "css", "[data-voodoo-name='active_date']");
		for (int k=0 ; k <customData.size()-4 ; k++){
			sugar().knowledgeBase.navToListView();
			sugar().knowledgeBase.listView.create();
			nameEditCtrl.set(testName+"_"+k);			
			statusEditCtrl.set(customData.get(k).get("status"));
			sugar().knowledgeBase.createDrawer.save();

			// Status = "Approved"
			if (k ==3){
				// Verify when status = "Approved", publish is set to empty, user receives alert
				sugar().alerts.getWarning().assertVisible(true);

				// Verify Alert text
				sugar().alerts.getWarning().assertContains(customData.get(0).get("warningText"), true);
				sugar().alerts.getWarning().confirmAlert();
			}

			// Status = "Published"
			else if (k ==4){
				sugar().knowledgeBase.listView.sortBy("headerName", false);
				sugar().knowledgeBase.listView.clickRecord(1);

				// Verify when status = "Published", publish is set to empty, publish date will 
				// automatically change to todays date
				publishDateDetailCtrl.assertEquals(todaysDate, true);
			}

			// Status = "Draft", "In Review", "Expired"
			else{
				sugar().knowledgeBase.listView.sortBy("headerName", false);
				sugar().knowledgeBase.listView.clickRecord(1);

				// Verify when status = "Draft", "In Review", "Expired", publish date can be set to empty
				publishDateValueCtrl.assertEquals("", true);
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 