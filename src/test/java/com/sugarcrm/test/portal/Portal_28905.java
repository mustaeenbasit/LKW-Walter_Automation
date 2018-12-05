package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.PortalTest;

public class Portal_28905 extends PortalTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that "Found in Release" and "Fixed in Release" fields are not appearing in List and Record view 
	 * layout of Bugs module of SugarPortal
	 * 
	 * @throws Exception
	 */
	@Test
	public void Portal_28905_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet fs = testData.get(testName).get(0);
		
		// Go to SugarPortal under admin section
		sugar().admin.navToAdminPanelLink("portalSettings");
		VoodooUtils.focusFrame("bwc-frame");
		
		// Go to Bugs module -> Listview in SugarPortal under admin section
		// TODO: VOOD-1119
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#Layouts > table > tbody > tr:nth-child(2) > td > a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#Buttons table td:nth-child(1) > table tr:nth-child(2) > td > a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnListView tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1119
		VoodooControl fieldsInListView = new VoodooControl("td", "id", "Default");
		VoodooControl fieldsInRecordView = new VoodooControl("div", "id", "panels");
		
		// Verify that "Found in Release" and "Fixed in Release" fields should not appear in List view layout
		fieldsInListView.assertContains(fs.get("found_in_release"), false);
		fieldsInListView.assertContains(fs.get("fixed_in_release"), false);
		
		VoodooUtils.focusDefault();
		
		// Go to record view
		// Go to SugarPortal under admin section
		sugar().admin.navToAdminPanelLink("portalSettings");
		VoodooUtils.focusFrame("bwc-frame");
		
		// Go to Bugs module -> Recordview in SugarPortal under admin section
		// TODO: VOOD-1119
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#Layouts > table > tbody > tr:nth-child(2) > td > a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#Buttons table td:nth-child(1) > table tr:nth-child(2) > td > a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnRecordView table tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();
		
		
		// Verify that "Found in Release" and "Fixed in Release" fields should not appear in Record view layout
		fieldsInRecordView.assertContains(fs.get("found_in_release"), false);
		fieldsInRecordView.assertContains(fs.get("fixed_in_release"), false);

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}