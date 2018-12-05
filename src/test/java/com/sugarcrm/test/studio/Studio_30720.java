package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_30720 extends SugarTest {
	VoodooControl accountBtnCtrl;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Dependent dropdown field should updated on record veiw.
	 * @throws Exception
	 */
	@Test
	public void Studio_30720_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-542
		// click on studio link  
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// click on Accounts in studio panel 
		accountBtnCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountBtnCtrl.click();
		VoodooUtils.waitForReady();

		// click on Fields
		new VoodooControl("a", "css", "#fieldsBtn tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();

		// Click on Industry field
		new VoodooControl("a", "id", "industry").click();
		VoodooUtils.waitForReady();

		// Setting Parent Dropdown in dependent list
		new VoodooControl("select", "id", "depTypeSelect").set(customData.get("dependentIndustryFieldValue"));

		// Clicking on edit visibility
		new VoodooControl("button", "css", "#visGridRow button").click();

		// Dragging available options in visibility editor
		VoodooControl blankOptionCtrl = new VoodooControl("li", "css", "#childTable [val='--blank--']");
		VoodooControl apparelOptionCtrl = new VoodooControl("li", "css", "#childTable [val='Apparel']");
		VoodooControl blankListCtrl = new VoodooControl("ul", "id", "ddd_--blank--_list");
		VoodooControl analystListCtrl = new VoodooControl("ul", "id", "ddd_Analyst_list");
		blankOptionCtrl.dragNDrop(blankListCtrl);
		apparelOptionCtrl.dragNDrop(blankListCtrl);
		blankOptionCtrl.dragNDrop(analystListCtrl);
		apparelOptionCtrl.dragNDrop(analystListCtrl);
		new VoodooControl("li", "css", "#childTable [val='Banking']").dragNDrop(analystListCtrl);
		new VoodooControl("li", "css", "#childTable [val='Construction']").dragNDrop(new VoodooControl("ul", "id", "ddd_Competitor_list"));
		new VoodooControl("ul", "css", "#visGridWindow button:nth-child(2)").click();
		VoodooUtils.waitForReady();

		// Saving new settings
		new VoodooControl("ul", "css", "[name='fsavebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Creating account
		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");

		// Setting Competitor in type field of account in create drawer
		sugar().accounts.createDrawer.getEditField("type").set(customData.get("accountType"));

		// Verifying 'Industry' field change to 'Construction'.
		sugar().accounts.createDrawer.getEditField("industry").assertEquals(customData.get("accountIndustry"), true);
		sugar().accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}