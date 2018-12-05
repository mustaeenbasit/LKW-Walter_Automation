package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RevenueLineItems_17329 extends SugarTest {
	public void setup() throws Exception {
		sugar().revLineItems.api.create();
		sugar().login();
	}

	/**
	 * Verify opportunity's account field on product line item record is read only field
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17329_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);

		// TODO: VOOD-854
		// Verifying pencil edit is available to sales stage
		sugar().revLineItems.recordView.getDetailField("salesStage").hover();
		new VoodooControl("span", "css", "div.record-cell[data-name=sales_stage] span.record-edit-link-wrapper i.fa-pencil").assertVisible(true);

		new VoodooControl("body", "css", "body").click();
		
		// Verifying pencil edit is not available for AccountName
		new VoodooControl("span", "css", ".fld_account_name").hover();
		new VoodooControl("span", "css", "div.record-cell[data-name=account_name] span.record-edit-link-wrapper i.fa-pencil").assertVisible(false);

		// Verify "Account name" field edit view should be ready-only.
		sugar().revLineItems.recordView.edit();
		new VoodooControl("span", "css", ".fld_account_name").assertAttribute("class", "edit", false);
		sugar().revLineItems.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}