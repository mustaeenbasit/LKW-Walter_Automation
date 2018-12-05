package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20241 extends SugarTest {
	// Common VoodooControl references
	// TODO VOOD-648 - Admin Oauth LIB support: for all VoodooControls
	VoodooControl oauthName;
	FieldSet oauthRecord;

	public void setup() throws Exception {
		// Initialize the common Control references
		oauthName = new VoodooControl("input", "id", "name");
		oauthRecord = testData.get("Admin_20241").get(0);
		sugar().login();
	}

	/**
	 * Verify the user created OAuth key can be edited
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20241_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "oauth").click();
		// Create an OAuth Key
		new VoodooControl("button", "css",
				"li[data-module='OAuthKeys'] button[data-toggle='dropdown']")
				.click();
		new VoodooControl("a", "css",
				"li[data-module='OAuthKeys'] ul[role='menu'] a:nth-of-type(1)")
				.click();
		VoodooUtils.focusFrame("bwc-frame");
		oauthName.set(oauthRecord.get("name"));
		new VoodooControl("input", "id", "c_key").set(oauthRecord.get("key"));
		new VoodooControl("input", "id", "c_secret").set(oauthRecord
				.get("secret"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();

		new VoodooControl("span", "id", "name").assertEquals(
				oauthRecord.get("name"), true);
		new VoodooControl("span", "id", "c_key").assertEquals(
				oauthRecord.get("key"), true);

		new VoodooControl("a", "id", "edit_button").click();
		oauthName.set(oauthRecord.get("nameChange"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();

		new VoodooControl("span", "id", "name").assertEquals(
				oauthRecord.get("nameChange"), true);

		// Delete the record
		new VoodooControl("span", "css", ".ab").click();
		new VoodooControl("a", "id", "delete_button").click();
		VoodooUtils.acceptDialog();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
