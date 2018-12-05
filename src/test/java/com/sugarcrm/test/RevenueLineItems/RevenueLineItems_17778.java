package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_17778 extends SugarTest {

	public void setup() throws Exception {
		sugar().revLineItems.api.create();
		sugar().login();
	}

	/**
	 * Verify appropriate actions are listed for Revenue Line Item list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17778_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.openRowActionDropdown(1);
		sugar().revLineItems.listView.getControl("edit01").assertVisible(true);
		sugar().revLineItems.listView.getControl("unfollow01").assertVisible(true);
		sugar().revLineItems.listView.getControl("delete01").assertVisible(true);

		sugar().revLineItems.listView.checkRecord(1);
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().revLineItems.listView.getControl("deleteButton").assertVisible(true);
		sugar().revLineItems.listView.getControl("exportButton").assertVisible(true);

		// TODO: VOOD-689
		new VoodooControl("a", "css", ".fld_merge_button a").assertVisible(true);
		new VoodooControl("a", "css", ".fld_calc_field_button a").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}