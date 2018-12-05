package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Studio_30689 extends SugarTest {
	VoodooControl accountBtnCtrl,studioLink;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Renaming/Removing subpanel "Title" for modules(Sidecar) should be handled properly
	 * @throws Exception
	 */
	@Test
	public void Studio_30689_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// click on studio link  
		studioLink = sugar().admin.adminTools.getControl("studio");
		studioLink.click();
		VoodooUtils.waitForReady();

		// click on Accounts in studio panel
		// TODO: VOOD-1511
		accountBtnCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Click on Subpanels Button
		new VoodooControl("td", "id", "subpanelsBtn").click();
		VoodooUtils.waitForReady();

		// Click on Cases inside Subpanels
		new VoodooControl("a","css", "#Buttons tr:nth-of-type(2) td:nth-of-type(4) a").click();
		VoodooUtils.waitForReady();

		// Click on title input box
		// TODO: CB-252 and VOOD-1437
		VoodooControl subpanelTitle = new VoodooControl("input", "id", "subpanel_title");
		subpanelTitle.set("");
		subpanelTitle.append("" + '\uE007');
		VoodooUtils.waitForReady();

		// Verify No Error message is displayed
		sugar().alerts.getError().assertVisible(false);
		subpanelTitle.assertEquals(customData.get("title"), true);

		// Enter Some text in the Title Box
		subpanelTitle.set(testName);
		subpanelTitle.append("" + '\uE007');
		VoodooUtils.waitForReady();

		// verify Entered text is displayed
		subpanelTitle.assertEquals(testName, true);
		VoodooUtils.focusDefault();

		// Navigate to Accounts module
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Verify the Title of Cases Subpanel is shown as per above changes
		StandardSubpanel casesSPCtrl = sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural);
		casesSPCtrl.assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}	

	public void cleanup() throws Exception {}
}