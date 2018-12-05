package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19970 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  verify change password section exists.
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19970_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("edit").waitForVisible();
		VoodooUtils.focusDefault();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();
		
		sugar().users.editView.getEditField("newPassword").assertExists(true);
		sugar().users.editView.getEditField("confirmPassword").assertExists(true);
		VoodooUtils.focusDefault();
		sugar().users.editView.cancel();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO VOOD-956
		new VoodooControl("ul", "css", "div#user_detailview_tabs ul.yui-nav").assertElementContains(ds.get(0).get("tab"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}