package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class Meetings_19753 extends SugarTest {
	DataSource customData;

	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar().login();
	}

	/**
	 * There is no direction field in the available list of list view layout of the meetings module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_19753_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-938
		// Go to Admin > Studio > Meetings > Layouts
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		new VoodooControl("a", "id", "studiolink_Meetings").click();
		new VoodooControl("td", "id", "layoutsBtn").click();
		// Select "ListView" 
		new VoodooControl("td", "id", "viewBtnlistview").click();

		// Observe there is no Direction field on the opened page
		for (int i = 0 ; i < customData.size() ; i++){
			new VoodooControl("td", "id", "Default").assertContains(customData.get(i).get("directionField"), false);
			new VoodooControl("td", "id", "Available").assertContains(customData.get(i).get("directionField"), false);
			new VoodooControl("td", "id", "Hidden").assertContains(customData.get(i).get("directionField"), false);
		}
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}