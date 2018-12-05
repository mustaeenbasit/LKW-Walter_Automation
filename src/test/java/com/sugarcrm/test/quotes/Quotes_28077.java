package com.sugarcrm.test.quotes;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class Quotes_28077 extends SugarTest {
	FieldSet customData;
	VoodooControl resetButtonCtrl,resetClickCtrl,relationshipsCtrl,fieldsCtrl,labelsCtrl,layoutsCtrl,extensionsCtrl,quotesCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.quotes.api.create();

		sugar.login();
		quotesCtrl = new VoodooControl("a", "id", "studiolink_Quotes");
		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");
	}
	/**
	 * Verify correct Team Name is shown in list View of BWC Modules.
	 *
	 * @throws Exception
	 */
	@Test
	public void Quotes_28077_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-938
		// Navigate to Admin -> Studio -> Quotes -> Layout -> List View
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		quotesCtrl.click();
		new VoodooControl("td", "id", "layoutsBtn").click();
		new VoodooControl("td", "id", "viewBtnlistview").click();

		// Move "Teams" field from the "Available" to "Default" column.
		VoodooControl dropHere1 = new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "[data-name='team_name']").dragNDrop(dropHere1);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to Quotes module list view.
		sugar.quotes.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		// Verify Correct team name is shown in Teams column
		// TODO: VOOD-1426
		new VoodooControl("td", "css", "tr.oddListRowS1").assertContains(customData.get("teamName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}