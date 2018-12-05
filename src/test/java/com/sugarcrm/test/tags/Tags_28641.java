package com.sugarcrm.test.tags;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28641 extends SugarTest {
	public void setup() throws Exception {
		sugar.calls.api.create();
		sugar.tags.api.create();
		sugar.login();
	}

	/**
	 * [Tags] Verify user can add new tags on Call and Meeting Record View
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28641_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to existing record of Calls Module and click Edit button
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();
		
		// Edit the tags in the Record, Add existing tag to the record
		// TODO: VOOD-1772
		new VoodooControl("span", "css", ".fld_tag.edit div ul li input").set(sugar.tags.getDefaultData().get("name"));
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", ".select2-result-label").click();
		
		// Click save and navigate to Calls record
		sugar.calls.recordView.save();
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		
		// Verify that user is able to add existing tag to the record
		// TODO: VOOD-1772
		new VoodooControl("a", "css", ".tag-name.ellipsis_inline a").assertContains(sugar.tags.getDefaultData().get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}