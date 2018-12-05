package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24737 extends SugarTest {

	public void setup() throws Exception {
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Non-admin user cannot edit reports to, title, department & status field in user profile page
	 * @throws Exception
	 */
	@Test
	public void Users_24737_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to User profile
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-563
		// Verify status field is not editable
		FieldSet fs = testData.get(testName).get(0);
		new VoodooControl("input", "id", "status").assertAttribute("class", fs.get("readOnlyAttr"));

		// Verify title field is not editable
		new VoodooControl("span", "id", "title").assertAttribute("class", fs.get("readOnlyAttr"));

		// Verify department field is not editable
		new VoodooControl("span", "id", "department").assertAttribute("class", fs.get("readOnlyAttr"));

		// Verify reports to field is not editable
		new VoodooControl("span", "id", "reports_to_id").assertAttribute("class", fs.get("readOnlyAttr"));

		// Clicking cancel on users detail view
		sugar().users.editView.getControl("cancelButton").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}