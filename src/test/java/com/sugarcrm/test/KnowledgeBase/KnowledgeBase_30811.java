package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Alert;
import com.sugarcrm.sugar.views.ListView;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30811 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that correct behavior when mass update a blank published date KB to status=Approved
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30811_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		ListView kbListView = sugar().knowledgeBase.listView;
		VoodooControl massUpdateCtrl = sugar().knowledgeBase.massUpdate.getControl("update");
		Alert massUpdateWarning = sugar().alerts.getWarning();
		String kbStatus = sugar().knowledgeBase.getDefaultData().get("status");
		
		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Creating KB record with blank publish and expiration date
		FieldSet kbData = new FieldSet();
		kbData.put("date_expiration", "");
		kbData.put("date_publish", "");
		sugar().knowledgeBase.create(kbData);
		kbData.clear();

		// Mass Update this KB record by changing status=Approved
		kbListView.checkRecord(1);
		kbListView.openActionDropdown();
		kbListView.massUpdate();
		VoodooControl massUpdateFieldCtrl = sugar().knowledgeBase.massUpdate.getControl("massUpdateField02");
		VoodooControl massUpdateValueCtrl = sugar().knowledgeBase.massUpdate.getControl("massUpdateValue02");
		massUpdateFieldCtrl.set(customData.get("filterName"));
		massUpdateValueCtrl.set(customData.get("filterValue"));
		massUpdateCtrl.click();

		// Verifying warning message is appearing
		massUpdateWarning.assertContains(customData.get("warningMessage"), true);

		// Choosing No when warning message appears
		massUpdateWarning.cancelAlert();

		// Verifying No changes are made
		// Verifying status
		kbListView.getDetailField(1, "status").assertEquals(kbStatus, true);

		// Click on mass update again
		massUpdateCtrl.click();

		// Choosing yes when warning appears
		massUpdateWarning.confirmAlert();

		// Verifying status has changed to Approved in List View
		kbListView.getDetailField(1, "status").assertEquals(customData.get("filterValue"), true);

		// Navigate to admin=>studio=>KB=>Layout=>List view
		// TODO: VOOD-1507 - Support Studio Module ListView Layouts View
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "studiolink_KBContents").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();

		// Add published date, expiration date in the default pane in List view layout.
		VoodooControl defaultPanel = new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "#Hidden li[data-name='active_date']").dragNDrop(defaultPanel);
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#Hidden li[data-name='exp_date']").dragNDrop(defaultPanel);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Creating one more kb record with status=darft, blank expiration and published date
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createArticle");
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.save();

		// Mass Update this KB record by changing status=Approved
		kbListView.checkRecord(1);
		kbListView.openActionDropdown();
		kbListView.massUpdate();
		massUpdateFieldCtrl.set(customData.get("filterName"));
		massUpdateValueCtrl.set(customData.get("filterValue"));
		massUpdateCtrl.click();

		// Verifying warning message is appearing
		massUpdateWarning.assertContains(customData.get("warningMessage"), true);

		// Choosing No when warning message appears
		massUpdateWarning.cancelAlert();

		// Verifying No changes are made
		// Verifying status
		kbListView.getDetailField(1, "status").assertEquals(kbStatus, true);

		// Click on mass update again
		massUpdateCtrl.click();

		// Choosing yes when warning appears
		massUpdateWarning.confirmAlert();

		// Verifying status has changed to Approved
		kbListView.getDetailField(1, "status").assertEquals(customData.get("filterValue"), true);
		
		// Verifying expiration date and publish date remain blank
		// TODO: VOOD-1761 -  Define hidden columns on ListView in Knowledge Base.
		new VoodooControl("div", "css", ".list.fld_exp_date").assertEquals("", true);
		new VoodooControl("div", "css", ".list.fld_active_date").assertEquals("", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}