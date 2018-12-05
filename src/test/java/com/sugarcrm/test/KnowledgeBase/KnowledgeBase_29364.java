package com.sugarcrm.test.KnowledgeBase;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29364 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// Create a Draft KB with an expiration date is past
		FieldSet fs = new FieldSet();
		fs.put("status", customData.get("statusDraft"));
		fs.put("date_expiration", customData.get("date_expiration"));
		sugar().knowledgeBase.api.create(fs);

		// Login
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Should prompt an user error when edit status=published in KB list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29364_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to KB list view
		sugar().knowledgeBase.navToListView();

		// In KB list view, edit the record by changing status to Published.
		sugar().knowledgeBase.listView.editRecord(1);
		VoodooControl ststusListViewEditFieldCtrl = sugar().knowledgeBase.listView.getEditField(1, "status");
		ststusListViewEditFieldCtrl.set(customData.get("statusPublished"));
		sugar().knowledgeBase.listView.saveRecord(1);

		// Verify, A red color message bar saying "The expiration date can not be before of the date of publishing". 
		sugar().alerts.getError().assertEquals(customData.get("errorMsg"), true);
		sugar().knowledgeBase.listView.cancelRecord(1); // Cancel editing record

		// Go to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Go to Studio -> KB -> Layouts -> List View
		// TODO: VOOD-542
		new VoodooControl("a", "id", "studiolink_KBContents").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1506
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();

		// Add published date, expiration date in the default pane.
		VoodooControl defaultPanel = new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "#Hidden li[data-name='active_date']").dragNDrop(defaultPanel);
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#Hidden li[data-name='exp_date']").dragNDrop(defaultPanel);
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();

		// In KB list view, edit the same record again by changing status=Published.
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.editRecord(1);
		ststusListViewEditFieldCtrl.set(customData.get("statusPublished"));
		sugar().knowledgeBase.listView.saveRecord(1);

		// A red color message bar appears as above message. In Expiration Date cell also appear red.  Mouse over it and read the message as above too. 
		sugar().alerts.getError().assertEquals(customData.get("errorMsg"), true);

		// Change expiration date becomes future and change status=published.
		sugar().knowledgeBase.listView.getEditField(1, "date_expiration").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		VoodooUtils.waitForReady();
		ststusListViewEditFieldCtrl.set(customData.get("statusPublished"));
		sugar().knowledgeBase.listView.saveRecord(1);

		// Verify that the KB is saved with correct dates - expiration date and today as published date. 
		sugar().knowledgeBase.listView.getDetailField(1, "status").assertEquals(customData.get("statusPublished"), true);
		sugar().alerts.getError().assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}