package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20239 extends SugarTest {
	// Common VoodooControl references
	// TODO VOOD-648 - Admin Oauth LIB support: for all VoodooControls
	VoodooControl editName;
	FieldSet oauthRecord;

	public void setup() throws Exception {
		// Initialize the common Control references
		editName = new VoodooControl("input", "id", "name");
		oauthRecord = testData.get("Admin_20239").get(0);
		sugar().login();
	}

	/**
	 * Verify the OAuth Version field is read-only after the creation
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20239_execute() throws Exception {
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
		editName.set(oauthRecord.get("name"));
		new VoodooControl("input", "id", "c_key").set(oauthRecord.get("key"));
		new VoodooControl("input", "id", "c_secret").set(oauthRecord
				.get("secret"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();

		new VoodooControl("span", "id", "name").assertEquals(
				oauthRecord.get("name"), true);
		new VoodooControl("span", "id", "c_key").assertEquals(
				oauthRecord.get("key"), true);

		new VoodooControl("a", "id", "edit_button").click();
		// Assert that the Consumer Key Name is editable (input) and the OAuth
		// Version is not
		editName.assertVisible(true);
		new VoodooControl("input", "id", "oauth_type").assertVisible(false);
		new VoodooControl("a", "id", "CANCEL_HEADER").click();

		// Delete the record
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", ".ab").click();
		new VoodooControl("a", "id", "delete_button").click();
		VoodooUtils.acceptDialog();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
