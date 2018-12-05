package com.sugarcrm.test.KnowledgeBase;

import org.joda.time.DateTime;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooDate;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29488_c extends SugarTest {
	public void setup() throws Exception {
		sugar().cases.api.create();

		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Creating a KB record with past expiration date and past published date
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(sugar().knowledgeBase.getDefaultData().get("name"));
		sugar().knowledgeBase.createDrawer.showMore();
		String pastDate = DateTime.now().minusDays(2).toString("MM/dd/yyyy");
		sugar().knowledgeBase.createDrawer.getEditField("date_expiration").set(pastDate);
		sugar().knowledgeBase.createDrawer.getEditField("date_publish").set(pastDate);
		sugar().knowledgeBase.createDrawer.save();

		// Navigate to Admin > studio > Cases > sub panel > KB sub panel > Add publish date & expiration date to default column 
		// TODO: VOOD-542 and VOOD-1511
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "studiolink_Cases").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "subpanelsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#Buttons tr:nth-child(2) td:nth-child(2) a").click();
		VoodooUtils.waitForReady();
		VoodooControl dropCtrl = new VoodooControl("li", "css",  "[data-name='language']");
		new VoodooControl("li", "css", "[data-name='active_date']").dragNDrop(dropCtrl);
		new VoodooControl("li", "css",  "[data-name='exp_date']").dragNDrop(dropCtrl);
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that correct handling when edit a KB by changing status to Approved in Cases sub panel
	 * KnowledgeBase_29488_c : Verify the KB with past published date and past expiration date in Cases sub panel gives
	 * correct results when edited and status changed to Approved
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29488_c_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to cases sub panel
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		StandardSubpanel kbSubpanel = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);
		kbSubpanel.scrollIntoViewIfNeeded(false);

		// TODO: SC-5260 - Non-standard class for link Existing record link in KB sub panel of Case record view hinders automation
		kbSubpanel.getChildElement("a", "css", ".layout_KBContents .dropdown-toggle").click();
		new VoodooControl("a", "css", ".fld_select_button a").click();

		// Adding KB record to Cases sub panel
		sugar().knowledgeBase.searchSelect.selectRecord(1);
		sugar().knowledgeBase.searchSelect.link();

		// Updating the status of KB record
		VoodooControl statusField = kbSubpanel.getEditField(1, "status");
		FieldSet fs = testData.get(testName).get(0);
		kbSubpanel.editRecord(1);
		statusField.set(fs.get("status"));
		kbSubpanel.saveAction(1);

		// Asserting the Error message
		sugar().alerts.getError().assertVisible(true);
		sugar().alerts.getError().assertContains(fs.get("errorMessagePublishDate"), true);
		VoodooUtils.waitForReady();

		// Cancel action and re navigate
		kbSubpanel.cancelAction(1);
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		kbSubpanel.scrollIntoViewIfNeeded(false);
		kbSubpanel.expandSubpanel();
		kbSubpanel.scrollIntoViewIfNeeded(false);
		kbSubpanel.editRecord(1);
		statusField.set(fs.get("status"));

		// Define controls for publish date field and expiration date field
		// TODO: VOOD-1489
		VoodooDate publishDate = new VoodooDate("input", "css", ".fld_active_date div input");
		VoodooDate expirationDate = new VoodooDate("input", "css", ".fld_exp_date.edit input");

		// Setting future date in publish date column
		String newPublishExpirationDate = DateTime.now().plusMonths(1).toString("MM/dd/yyyy");
		publishDate.scrollIntoViewIfNeeded(false);
		publishDate.set(newPublishExpirationDate);
		kbSubpanel.saveAction(1);

		// Asserting the Error message
		sugar().alerts.getError().assertVisible(true);
		sugar().alerts.getError().assertContains(fs.get("errorMessageExpirationDate"), true);
		VoodooUtils.waitForReady();

		// Cancel action and re navigate
		kbSubpanel.cancelAction(1);
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		kbSubpanel.scrollIntoViewIfNeeded(false);
		kbSubpanel.expandSubpanel();
		kbSubpanel.scrollIntoViewIfNeeded(false);
		kbSubpanel.editRecord(1);
		statusField.set(fs.get("status"));
		publishDate.scrollIntoViewIfNeeded(false);
		publishDate.set(newPublishExpirationDate);

		// Setting up future date in expiration date column
		expirationDate.scrollIntoViewIfNeeded(false);
		expirationDate.set(newPublishExpirationDate);
		kbSubpanel.saveAction(1);

		// Asserting the publish date,expiration date and status field after save
		// TODO: VOOD-1489
		new VoodooControl("div", "css", ".fld_active_date div").assertEquals(newPublishExpirationDate, true);
		new VoodooControl("div", "css", ".fld_exp_date div").assertEquals(newPublishExpirationDate, true);
		VoodooControl statusCtrl = new VoodooControl("span", "css", ".list.fld_status");
		statusCtrl.scrollIntoViewIfNeeded(false);
		statusCtrl.assertEquals(fs.get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}