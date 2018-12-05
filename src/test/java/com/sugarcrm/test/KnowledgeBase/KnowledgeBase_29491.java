package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29491 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Calendar should be disappeared when Cancel inline edit in KB listview
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29491_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin > Studio > KB > Layout > List view > add "Published Date" and "Expiration Date" in the default list view.
		// TODO: VOOD-542
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "studiolink_KBContents").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		VoodooControl dropHereCtrl = new VoodooControl("li", "css",  "[data-name='language']");
		new VoodooControl("li", "css", "[data-name='active_date']").dragNDrop(dropHereCtrl);
		new VoodooControl("li", "css",  "[data-name='exp_date']").dragNDrop(dropHereCtrl);
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();

		// Create a Draft KB with Expiration Date and Published Date are blank.
		FieldSet fs = new FieldSet();
		fs.put("date_publish", "");
		fs.put("date_expiration", "");
		sugar().knowledgeBase.create(fs);

		// In list view, edit the KB by changing status to Approved
		FieldSet customFS = testData.get(testName).get(0);
		sugar().knowledgeBase.listView.editRecord(1);
		sugar().knowledgeBase.listView.getEditField(1, "status").set(customFS.get("status"));
		sugar().knowledgeBase.listView.saveRecord(1);

		// Verify that A yellow message bar appears, "Schedule this article to be published by specifying the Publish Date. Do you wish to continue without entering a Publish Date?"
		sugar().alerts.getWarning().assertContains(customFS.get("warningMessage"), true);

		// Click on No.
		sugar().alerts.cancelAllWarning();
		VoodooUtils.waitForReady();

		// TODO: VOOD-910
		// Verify that A calendar appears for Published Date field.
		VoodooControl calendarWidget = new VoodooControl("div", "css", ".layout_KBContents .datepicker.dropdown-menu");
		calendarWidget.assertVisible(true);

		// Click on Cancel of edit.
		sugar().knowledgeBase.listView.cancelRecord(1);

		// Verify that the Calendar should be disappeared
		calendarWidget.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 