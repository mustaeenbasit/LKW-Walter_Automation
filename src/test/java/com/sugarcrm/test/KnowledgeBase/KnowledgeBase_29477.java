package com.sugarcrm.test.KnowledgeBase;

import org.joda.time.DateTime;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooDate;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29477 extends SugarTest {
	VoodooControl moduleCtrl,layoutCtrl,listViewCtrl,saveBtnCtrl,defaultPanel;

	public void setup() throws Exception {
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Inline changing "Published Date" during mass update is handled correctly
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29477_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet kbData = testData.get(testName).get(0);

		// Studio -> KB -> Layouts -> List View
		// TODO: VOOD-1507
		moduleCtrl = new VoodooControl("a", "id", "studiolink_KBContents");
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		listViewCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		saveBtnCtrl = new VoodooControl("input", "id", "savebtn");
		defaultPanel = new VoodooControl("td", "id", "Default");

		// Navigating to studio Knowledge Base List View and shifting expiration & publish Date to Default
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		listViewCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("li", "css", "#Hidden li[data-name='active_date']").dragNDrop(defaultPanel);
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#Hidden li[data-name='exp_date']").dragNDrop(defaultPanel);
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Creating a KB record with valid expiration date and published date previous
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(sugar().knowledgeBase.getDefaultData().get("name"));
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("date_expiration").set(kbData.get("date_expiration"));
		sugar().knowledgeBase.createDrawer.getEditField("date_publish").set(kbData.get("date_publish"));
		sugar().knowledgeBase.createDrawer.save();
		sugar().alerts.getAlert().closeAlert();

		// Go to KB list view, select above KB, mass update to change Status = Approved.
		sugar().knowledgeBase.listView.toggleSelectAll();
		sugar().knowledgeBase.listView.openActionDropdown();
		sugar().knowledgeBase.listView.massUpdate();
		sugar().knowledgeBase.massUpdate.getControl("massUpdateField02").set(kbData.get("status"));
		sugar().knowledgeBase.massUpdate.getControl("massUpdateValue02").set(kbData.get("approved"));

		// Click on Update button
		sugar().knowledgeBase.massUpdate.getControl("update").click();

		// Verify that a red color error message "The Publish Date must occur on a later date than today's date."
		// TODO: VOOD-1819
		VoodooControl errorMessageCtrl = new VoodooControl("span", "css", ".massupdate.fld_status.error .help-block");
		errorMessageCtrl.assertContains(kbData.get("alertError1"), true);
		errorMessageCtrl.assertCssAttribute("color", kbData.get("redColor"));

		// Inline edit the KbRecord
		// TODO: VOOD-1489
		sugar().knowledgeBase.listView.editRecord(1);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		VoodooDate publishDate = new VoodooDate("input", "css", ".fld_active_date.edit input");
		VoodooDate expirationDate = new VoodooDate("input", "css", ".fld_exp_date.edit input");
		publishDate.set(kbData.get("newdate_publish"));
		sugar().knowledgeBase.listView.saveRecord(1);

		// Verify that a red color error message "Error The expiration date can not be before date of publishing" appear.
		sugar().alerts.getError().assertVisible(true);
		sugar().alerts.getError().assertContains(kbData.get("exp_alert"), true);
		sugar().alerts.getError().closeAlert();
		new VoodooControl("span", "css", ".error-tooltip").assertAttribute("data-original-title", kbData.get("exp_alert_popup"));

		// Tomorrow's date
		String tomorrowDate = DateTime.now().plusDays(1).toString("MM/dd/yyyy");

		// Set the Expiration Date to tommorow's Date through Inline Editing
		expirationDate.set(tomorrowDate);
		sugar().knowledgeBase.listView.saveRecord(1);
		VoodooUtils.waitForReady();
		
		// Verify KB is saved with status as Draft with published date and expiration date as set
		// TODO: VOOD-1489
		VoodooControl status = new VoodooControl("span", "css", ".single .fld_status");
		status.assertEquals(sugar().knowledgeBase.getDefaultData().get("status"), true);
		new VoodooControl("span", "css", ".fld_active_date").assertEquals(kbData.get("newdate_publish"), true);
		new VoodooControl("span", "css", ".fld_exp_date.list").assertEquals(tomorrowDate, true);

		// Click on Update button
		sugar().knowledgeBase.massUpdate.update();

		// Verify that a red color error message - The Publish Date must occur on a later date than today's date. appears
		errorMessageCtrl.assertContains(kbData.get("alertError1"), true);
		errorMessageCtrl.assertCssAttribute("color", kbData.get("redColor"));

		// Setting Publish date to tommmorow's Date
		sugar().knowledgeBase.listView.editRecord(1);
		publishDate.scrollIntoViewIfNeeded(false);
		publishDate.set(tomorrowDate);
		sugar().knowledgeBase.listView.saveRecord(1);
		VoodooUtils.waitForReady();
		
		// Verify KB is saved with status as Draft with published date and expiration date are set to tomorrow.
		// TODO: VOOD-1489
		status.assertEquals(sugar().knowledgeBase.getDefaultData().get("status"), true);
		new VoodooControl("span", "css", ".fld_active_date").assertEquals(tomorrowDate, true);
		new VoodooControl("span", "css", ".fld_exp_date.list").assertEquals(tomorrowDate, true);

		// Click on Update button
		sugar().knowledgeBase.massUpdate.update();

		// Verify the status is now changed to "Approved" for the Kb Record.
		status.assertEquals(kbData.get("approved"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}