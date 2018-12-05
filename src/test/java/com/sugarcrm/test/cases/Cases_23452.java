package com.sugarcrm.test.cases;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;

public class Cases_23452 extends SugarTest {

	ArrayList<String> al = new ArrayList<String>();
	AccountRecord myAcc;

	public void setup() throws Exception {

		myAcc = (AccountRecord) sugar().accounts.api.create();

		sugar().login();

		al.add(sugar().cases.moduleNamePlural);

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1150 -Need Library support for Admin > Configure Navigation Bar Quick Create.
		new VoodooControl("a", "id", "config_prod_bar").click();
		VoodooUtils.waitForReady();

		// Move Cases modules to "Enable Columns".
		for(int i = 0; i < al.size(); i++){
			new VoodooControl("tr", "xpath", "//*[@id='disabled_div']/div[3]/table/tbody[2]/tr[contains(.,'" + al.get(i) + "')]")
			.dragNDrop(new VoodooControl("tr", "css", "#enabled_div .yui-dt-data tr:nth-child(1)"));
		}

		// Save
		new VoodooControl("input", "css", "[value='Save']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Check user can not insert case number in quick create view
	 */
	@Test
	public void Cases_23452_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create case using Quick Create
		sugar().navbar.quickCreateAction(sugar().cases.moduleNamePlural);
		sugar().alerts.waitForLoadingExpiration();

		// Verify case number shows "No Data" in Create Drawer
		new VoodooControl("span","css",".fld_case_number.nodata").assertContains("No data", true);

		sugar().cases.createDrawer.getEditField("name").set(testName);
		sugar().cases.createDrawer.getEditField("relAccountName").set(myAcc.getRecordIdentifier());
		sugar().cases.createDrawer.save();

		// Verify that case is successfully created by Quick Create
		sugar().cases.navToListView();
		sugar().cases.listView.verifyField(1, "name", testName);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
