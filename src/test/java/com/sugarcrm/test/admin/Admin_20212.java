package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20212 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		FieldSet fs = new FieldSet();
		fs.put("name", customData.get("meetingName"));
		fs.put("repeatType", customData.get("repeatType"));
		sugar().meetings.api.create(fs);
		sugar().login();
	}

	/**
	 * Verify module name in global search result sync with updating module name
	 * @throws Exception
	 */
	@Test
	public void Admin_20212_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Rename Meetings module
		sugar().admin.renameModule(sugar().meetings, customData.get("singularName"), customData.get("pluralName"));
		
		// Verify that with new Meeting Module name, search works
		// TODO: VOOD-668
		// Type first character, verify both records are shown. 
		new VoodooControl("input", "css", ".search-query").set(customData.get("meetingName"));
		VoodooUtils.waitForReady(30000);
		sugar().navbar.search.getControl("searchResults").assertContains(customData.get("meetingName"), true);

		// Goto Studio
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that All "Meetings" should be "Mings" in Studio modules
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Meetings").assertContains(customData.get("pluralName"), true);
		new VoodooControl("div", "id", "mbTree").assertContains(customData.get("pluralName"), true);
		VoodooUtils.focusDefault();

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Check enable module list in search in admin module
		sugar().admin.adminTools.getControl("globalSearch").click();
		VoodooUtils.waitForReady();

		// Verify that  Enable module list show "Mings" and no "Meetings"
		new VoodooControl("div", "id", "enabled_div").assertContains(customData.get("pluralName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}