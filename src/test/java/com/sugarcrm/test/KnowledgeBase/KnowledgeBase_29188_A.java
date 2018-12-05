package com.sugarcrm.test.KnowledgeBase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29188_A extends SugarTest {
	DataSource kbData = new DataSource();
	VoodooControl kbPublishedDateCtrl;
	String todaysDate, pastDayDate;

	public void setup() throws Exception {
		kbData = testData.get(testName);
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Past Day date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		todaysDate = sdf.format(date);
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -2);
		date = calendar.getTime();
		pastDayDate = sdf.format(date);

		// Define Controls for KB 
		kbPublishedDateCtrl = sugar().knowledgeBase.createDrawer.getEditField("date_publish");
	}

	// Fill in required data in KB create drawer and Save the record
	private void createKbWithStatus(int nameIndex, int statusIndex) throws Exception {
		sugar().knowledgeBase.createDrawer.getEditField("name").set(kbData.get(nameIndex).get("name"));
		sugar().knowledgeBase.createDrawer.getEditField("status").set(kbData.get(statusIndex).get("status"));
		kbPublishedDateCtrl.set(pastDayDate);
	}

	/**
	 * Verify that Published Date field has correct validations
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29188_A_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define Controls for KB list view
		VoodooControl kbNameListViewCtrl = sugar().knowledgeBase.listView.getDetailField(1, "name");
		VoodooControl kbStatusListViewCtrl = sugar().knowledgeBase.listView.getDetailField(1, "status");

		// Navigate to KB list view
		sugar().knowledgeBase.navToListView();

		// For Status = Draft
		// Go to KB module -> Create article
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();

		// Fill in required data, Set 'Publish Date' with the date in the past and Draft Status -> Save
		createKbWithStatus(0, 0);
		sugar().knowledgeBase.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// For Draft Status -> Article should be saved. User should not be warned about invalid Publish Date
		sugar().alerts.getWarning().assertExists(false);
		kbNameListViewCtrl.assertEquals(kbData.get(0).get("name"), true);
		kbStatusListViewCtrl.assertEquals(kbData.get(0).get("status"), true);

		// For Status = Approved
		// Go to KB module -> Create article
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();

		// Fill in required data, Set 'Publish Date' with the date in the past and Approved Status -> Save
		createKbWithStatus(1,1);
		sugar().knowledgeBase.createDrawer.getControl("saveButton").click();

		// For Approved Status -> Article should not be saved. User should be warned about invalid Publish Date
		sugar().alerts.getError().closeAlert();
		// TODO: VOOD-1445 and VOOD-1292
		new VoodooControl("span", "css", ".fld_active_date.edit").assertAttribute("class", kbData.get(0).get("error"), true);
		new VoodooControl("span", "css", ".error-tooltip.add-on").assertAttribute("data-original-title", kbData.get(0).get("errorText"));

		// Cancel the KB create drawer
		sugar().knowledgeBase.createDrawer.cancel();

		// For Status = Published
		// Go to KB module -> Create article
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();

		// Fill in required data, Set 'Publish Date' with the date in the past and Published Status -> Save
		createKbWithStatus(2,2);
		sugar().knowledgeBase.createDrawer.getControl("saveButton").click();

		// For Published Status -> Publish Date should change to today's date when you save
		VoodooUtils.pause(100); // Wait needed for verification of Publish Date 
		kbPublishedDateCtrl.assertContains(todaysDate, true);
		VoodooUtils.waitForReady();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
		kbNameListViewCtrl.assertEquals(kbData.get(2).get("name"), true);
		kbStatusListViewCtrl.assertEquals(kbData.get(2).get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}