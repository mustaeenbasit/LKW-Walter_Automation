package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_29772 extends SugarTest {
	UserRecord chrisUser;

	public void setup() throws Exception {
		chrisUser = (UserRecord)sugar().users.api.create();
		sugar().login();
	}

	/**
	 * Verify that no error should display to admin on update of My team via user module.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_29772_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to user management page.
		sugar().admin.navToAdminPanelLink("userManagement");
		VoodooUtils.waitForReady();
		// Click on the newly created user and navigate to access tab.
		sugar().users.listView.basicSearch("Chris");
		sugar().users.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-956
		VoodooControl accessTab = new VoodooControl("a", "css", "#user_detailview_tabs #tab3");
		accessTab.click();

		// Remove global team of respective user.
		new VoodooControl("img", "css", ".listViewTdToolsS1 [alt='remove']").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusFrame("bwc-frame");
		accessTab.click();

		// TODO: VOOD-1040
		// Edit private team of respective user.
		new VoodooControl("img", "css", "#user_detailview_tabs [alt='edit']").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Change the name of team for particular user.
		new VoodooControl("input", "css", "table.edit.view input[type='text']").set(testName);

		// Click on save button.
		new VoodooControl("input", "id", "btn_save").click();
		VoodooUtils.waitForReady();

		// Verify that after saving edit view, Admin should redirected to detail view of updated record.
		sugar().users.detailView.getDetailField("fullName").assertEquals(chrisUser.get("fullName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}