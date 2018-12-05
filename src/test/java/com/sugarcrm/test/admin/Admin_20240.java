package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20240 extends SugarTest {
	FieldSet oauthRecord;

	public void setup() throws Exception {
		oauthRecord = testData.get("Admin_20240").get(0);
		sugar().login();
	}

	/**
	 * Verify the OAuth Key can be created successfully
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20240_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO VOOD-648 - Admin Oauth LIB support: for all VoodooControls
		new VoodooControl("a", "id", "oauth").click();
		// Create an OAuth Key
		new VoodooControl("button", "css",
				"li[data-module='OAuthKeys'] button[data-toggle='dropdown']")
				.click();
		new VoodooControl("a", "css",
				"li[data-module='OAuthKeys'] ul[role='menu'] a:nth-of-type(1)")
				.click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "name").set(oauthRecord.get("name"));
		new VoodooControl("input", "id", "c_key").set(oauthRecord.get("key"));
		new VoodooControl("input", "id", "c_secret").set(oauthRecord
				.get("secret"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();

		new VoodooControl("span", "id", "name").assertEquals(
				oauthRecord.get("name"), true);
		new VoodooControl("span", "id", "c_key").assertEquals(
				oauthRecord.get("key"), true);

		// Delete the Record
		new VoodooControl("span", "css", ".ab").click();
		new VoodooControl("a", "id", "delete_button").click();
		VoodooUtils.acceptDialog();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
