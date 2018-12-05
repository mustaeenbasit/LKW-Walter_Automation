package com.sugarcrm.test.calls;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vaibhav Singhal <vsinghal@sugarcrm.com>
 */
public class Calls_17068 extends SugarTest {
	FieldSet customData,systemSettings;
	CallRecord myCall;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		DataSource rliDs = testData.get(testName+"_RLI");
		sugar.revLineItems.api.create(rliDs);
		myCall = (CallRecord)sugar.calls.api.create();		
		sugar.login();
		
		// In System Settings save view to show 5 entries in list view
		systemSettings = new FieldSet();
		systemSettings.put("maxEntriesPerPage", customData.get("numberOfEntries"));
		sugar.admin.setSystemSettings(systemSettings);
	}

	/**
	 * Check related to field search results auto load function on detail view inline edit mode.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_17068_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to related to field in detailed view
		myCall.navToRecord();
		sugar.calls.recordView.edit();
		sugar.calls.createDrawer.getEditField("relatedToParentType").set(customData.get("rliModuleNamePlural"));
		VoodooSelect relatedToParent = (VoodooSelect)sugar.calls.recordView.getEditField("relatedToParentName");
		
		// Click on Search for more to bring up RLI Search and Select View
		relatedToParent.clickSearchForMore();

		// TODO: VOOD-1487 : Need lib support for verification of sugar-fields on SSV
		VoodooControl showMoreRLI = new VoodooControl("button", "css", ".layout_RevenueLineItems [data-action='show-more']");
		
		// Verify that 5 records are present and Show more link is available
		showMoreRLI.assertVisible(true);
		int rliCount = sugar.revLineItems.searchSelect.countRows();
		Assert.assertTrue("Count Not Equal", rliCount == 5);
		showMoreRLI.click();
		
		// Verify that 2 more records are visible and Show more link is not available
		showMoreRLI.assertVisible(false);
		rliCount = sugar.revLineItems.searchSelect.countRows();
		Assert.assertTrue("Count Not Equal", rliCount == 7);
		
		sugar.revLineItems.searchSelect.cancel();
		sugar.calls.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
