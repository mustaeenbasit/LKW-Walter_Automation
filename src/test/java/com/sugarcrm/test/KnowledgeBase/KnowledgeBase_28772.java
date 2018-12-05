package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28772 extends SugarTest {
	DataSource kbData = new DataSource();

	public void setup() throws Exception {
		kbData = testData.get(testName+"_kbData");
		sugar().knowledgeBase.api.create(kbData);

		// Login as an Admin
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that if the score is the same, then first is an article with larger number of positive votes
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28772_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create users Will, Jim, Max, Sally 
		// TODOD: VOOD-1200 - Authentication failed on calling Users default data
		DataSource customData = new DataSource();
		FieldSet userDetail = new FieldSet();
		customData = testData.get(testName);
		sugar().users.create(customData);

		// TODO: VOOD-960 - Dashlet selection 
		// Choose "Most useful publish KB articles" dashlet
		FieldSet dashletData = testData.get(testName+"_dashlet").get(0);
		VoodooControl dashletSearchCtrl = new VoodooControl("input", "css", ".layout_Home .search");
		VoodooControl saveButton = new VoodooControl("a", "css", ".active .fld_save_button a");
		VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");

		// TODO: VOOD-1783 - Need lib support for Vote buttons('Not Useful' and 'Useful') on KB record view page.
		VoodooControl usefulBtnCtrl = new VoodooControl("a", "css", "[data-action='useful']");

		// Login as Will / Jim / Max / Sally
		for (int userCount = 0; userCount < customData.size(); userCount++) {
			// Log out as we need to login with different users( Will / Jim / Max / Sally)
			sugar().logout();
			userDetail.put("userName", customData.get(userCount).get("userName"));
			userDetail.put("password", customData.get(userCount).get("newPassword"));
			sugar().login(userDetail);
			userDetail.clear();

			// Navigate to KB list view
			sugar().knowledgeBase.navToListView();

			// Create a dashboard by adding a dashlet
			sugar().dashboard.clickCreate();
			sugar().dashboard.getControl("title").set(testName);
			sugar().dashboard.addRow();
			sugar().dashboard.addDashlet(1, 1);

			// TODO: VOOD-960 - Dashlet selection 
			// Choose "Most useful publish KB articles" dashlet
			dashletSearchCtrl.set(dashletData.get("dashletName"));
			VoodooUtils.waitForReady();
			dashletSelectCtrl.click();
			VoodooUtils.waitForReady();
			saveButton.click();
			VoodooUtils.waitForReady();

			// Save the dashboard
			sugar().dashboard.save();

			// Sort in ascending order 
			sugar().knowledgeBase.listView.sortBy("headerName", true);

			// Set KB records as "Useful" / "Not Useful" for Will / Jim / Max / Sally user
			switch (userCount) {
			case 0: 
				// Choose useful vote in KB1 for Will
				sugar().knowledgeBase.listView.clickRecord(1);
				usefulBtnCtrl.click();
				VoodooUtils.waitForReady();

				// Choose useful vote in KB2 for Will
				sugar().knowledgeBase.recordView.gotoNextRecord();
				usefulBtnCtrl.click();
				VoodooUtils.waitForReady();
				break;

			case 1:
				// Choose UN-useful vote in kb1 for Jim
				sugar().knowledgeBase.listView.clickRecord(1);
				new VoodooControl("a", "css", "[data-action='notuseful']").click();
				VoodooUtils.waitForReady();

				// Choose useful vote for KB2 for Jim
				sugar().knowledgeBase.recordView.gotoNextRecord();
				usefulBtnCtrl.click();
				VoodooUtils.waitForReady();
				break;

			case 2:
				// Choose useful vote in KB1 for Max
				sugar().knowledgeBase.listView.clickRecord(1);
				usefulBtnCtrl.click();
				VoodooUtils.waitForReady();
				break;

			case 3:
				// Choose useful vote in KB1 for Sally
				sugar().knowledgeBase.listView.clickRecord(1);
				usefulBtnCtrl.click();
				VoodooUtils.waitForReady();
				sugar().knowledgeBase.navToListView();

				// Verify in "Most useful published KB articles" Dashlet
				// kb1 is listed first, kb2 is listed at the second
				// TODO: VOOD-670 - More Dashlet Support
				new VoodooControl("li", "css", ".unstyled.listed.kbcontents  li").assertContains(kbData.get(0).get("name"), true);
				new VoodooControl("li", "css", ".unstyled.listed.kbcontents  li:nth-child(2)").assertContains(kbData.get(1).get("name"), true);
				break;

			default :
				break;
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}