package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_18918 extends SugarTest {
	VoodooControl nameCtrl,searchCtrl;

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Delete private team from list view with the corresponding user still exists
	 * @throws Exception
	 */
	@Test
	public void Teams_18918_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("teamsManagement").click();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1011 Lib support fot Teams module should be added
		nameCtrl=new VoodooControl("input", "css", "#name_basic");
		nameCtrl.set(sugar.users.getQAUser().get("lastName"));
		searchCtrl=new VoodooControl("input", "css", "#search_form_submit");
		searchCtrl.click();	

		new VoodooControl("input", "css", "form#MassUpdate input.checkbox.massall").click();
		new VoodooControl("a", "css", "#delete_listview_top").click();
		VoodooUtils.acceptDialog();

		sugar.alerts.waitForLoadingExpiration();
		// Verify error message
		new VoodooControl("p", "css", "div#content tbody p.error:nth-of-type(2)").assertEquals(ds.get(0).get("assert"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}