package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_20288 extends SugarTest {
	public void setup() throws Exception {
		FieldSet fs = new FieldSet();
		fs.put("date_start_date", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		sugar().meetings.api.create(fs);
		sugar().revLineItems.api.create();
		sugar().login();
	}

	/**
	 * RLI can be opened and viewed correctly from meeting's info link
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_20288_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Relate Meeting with an existing RLI record
		sugar().meetings.navToListView();
		sugar().meetings.listView.editRecord(1);
		sugar().meetings.listView.getEditField(1,"relatedToParentType").set("Revenue Line Items");
		sugar().meetings.listView.getEditField(1,"relatedToParentName").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().meetings.listView.saveRecord(1);
		
		// Navigate to Calendar module and find out the meeting just created
		sugar().calendar.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click on the little i icon to open info popup
		// TODO: VOOD-863
		new VoodooControl("div", "css", ".adicon").scrollIntoViewIfNeeded(true);
	
		// Click on the RLI link
		// TODO: VOOD-863
		new VoodooControl("a", "css", ".ui-dialog-content a").click();
		VoodooUtils.focusDefault();
		
		VoodooUtils.waitForReady();
		
		// Verify that RLI record is opened and in sidecar view correctly
		sugar().revLineItems.recordView.getControl("moduleIDLabel").hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains("Revenue Line Item", true);
		sugar().revLineItems.recordView.getDetailField("name").assertEquals(sugar().revLineItems.getDefaultData().get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}