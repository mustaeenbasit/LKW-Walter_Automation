package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_20289 extends SugarTest {
	public void setup() throws Exception {
		sugar().revLineItems.api.create();
		sugar().login();
	}

	/**
	 * RLI can be opened and viewed correctly from call's info link
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_20289_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet revLineItemModule = testData.get(testName).get(0);

		// TODO: VOOD-444, VOOD-1837 - Once resolved call should created via API
		// Relate Call with an existing RLI record
		sugar().navbar.selectMenuItem(sugar().calls, "create"+sugar().calls.moduleNameSingular);
		sugar().calls.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.getEditField("relatedToParentType").set(revLineItemModule.get("rli_plural"));
		sugar().calls.createDrawer.getEditField("relatedToParentName").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().calls.createDrawer.getEditField("date_start_date").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		sugar().calls.createDrawer.save();

		// Navigate to Calendar module and find out the Call just created
		sugar().calendar.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Click on the little i icon to open info popup and click on the RLI
		// TODO: VOOD-863
		new VoodooControl("div", "css", ".adicon").scrollIntoViewIfNeeded(true); // scroll + click
		new VoodooControl("a", "css", ".ui-dialog-content a").click();
		VoodooUtils.focusDefault();

		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1887- Once resolved commented line will work
		//sugar().revLineItems.recordView.assertVisible(true);

		// Verify that RLI record is opened and in sidecar view correctly
		sugar().revLineItems.recordView.getControl("moduleIDLabel").hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(revLineItemModule.get("rli_singular"), true);
		sugar().revLineItems.recordView.getDetailField("name").assertEquals(sugar().revLineItems.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}