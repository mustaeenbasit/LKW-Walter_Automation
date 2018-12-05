package com.sugarcrm.test.KnowledgeBase;

import org.joda.time.DateTime;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooDate;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29480 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Inline changing "Published Date" but expiration date is blank 
	 * during mass update is handled correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29480_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Add "Publish Date" and "Expiration Date" in the List View Layout
		// Navigate to Admin > Studio
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// Studio -> KB -> Layouts -> List View
		// TODO: VOOD-542 and VOOD-1507
		new VoodooControl("a", "id", "studiolink_KBContents").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();

		// Drag and Drop Publish Date from "Hidden" to "Default"
		VoodooControl dropHereCtrl = new VoodooControl("li", "css", "#Default li[data-name='language']");
		new VoodooControl("li", "css", "#Hidden li[data-name='active_date']").dragNDrop(dropHereCtrl);
		// Drag and Drop Expiration Date from "Hidden" to "Default"
		new VoodooControl("li", "css", "#Hidden li[data-name='exp_date']").dragNDrop(dropHereCtrl);
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();

		// Create a KB with Status=Draft,  Published Date is past dated, but Expiration Date is blank.
		FieldSet kbRecordData = testData.get(testName).get(0);
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(kbRecordData.get("name"));
		sugar().knowledgeBase.createDrawer.getEditField("date_publish").set(kbRecordData.get("date_publish"));
		sugar().knowledgeBase.createDrawer.save();

		// Mass update this KB by changing status=Approved. 
		sugar().knowledgeBase.listView.checkRecord(1);
		sugar().knowledgeBase.listView.openActionDropdown();
		sugar().knowledgeBase.listView.massUpdate();
		sugar().knowledgeBase.massUpdate.getControl("massUpdateField02").set(kbRecordData.get("massUpdateField"));
		sugar().knowledgeBase.massUpdate.getControl("massUpdateValue02").set(kbRecordData.get("massUpdateValue"));

		// Click on Update button
		sugar().knowledgeBase.massUpdate.update();

		// Verify that a red color error message - The Publish Date must occur on a later date than today's date. appears
		VoodooControl errorMessageCtrl = new VoodooControl("span", "css", ".massupdate.fld_status.error .help-block");
		errorMessageCtrl.assertContains(kbRecordData.get("warningMessage"), true);

		// Change the published date to tomorrow.
		// Get tomorrows Date
		String tomorrowsDate = DateTime.now().plusDays(1).toString("MM/dd/yyyy");

		sugar().knowledgeBase.listView.editRecord(1);
		// TODO: VOOD-1489
		VoodooDate publishDateEditCtrl = new VoodooDate("input", "css", ".fld_active_date.edit .datepicker");
		publishDateEditCtrl.set(tomorrowsDate);

		// Click Update without saving, doing via getControl as we need to verify alert.
		sugar().knowledgeBase.massUpdate.getControl("update").click();

		// Verify that a red color error message - The Publish Date must occur on a later date than today's date. appears
		errorMessageCtrl.assertContains(kbRecordData.get("warningMessage"), true);

		// Verify Published Date is set back to original past date. 
		publishDateEditCtrl.assertEquals(kbRecordData.get("date_publish"), true);

		// Inline edit the KB by changing the published date become tomorrow.
		publishDateEditCtrl.set(tomorrowsDate);

		// Save
		sugar().knowledgeBase.listView.saveRecord(1);

		// Verify Draft KB is saved with tomorrow date in the published date.
		// TODO: VOOD-1489
		VoodooControl publishDateDetailCtrl = new VoodooControl("span", "css", ".fld_active_date.list");
		publishDateDetailCtrl.assertEquals(tomorrowsDate, true);

		// Click on Update.
		sugar().knowledgeBase.massUpdate.update();

		// Verify The status is changed to Approved. 
		sugar().knowledgeBase.listView.getDetailField(1, "status").assertEquals(kbRecordData.get("massUpdateValue"), true);

		// Verify Published Date is tomorrow.  
		publishDateDetailCtrl.assertEquals(tomorrowsDate, true);

		// Verify Expiration Date is blank.
		// TODO: VOOD-1489
		new VoodooControl("div", "css", ".list.fld_exp_date .ellipsis_inline").assertEquals("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}