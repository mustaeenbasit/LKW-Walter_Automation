package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Alex Nisevich <anisevich@sugarcrm.com>
 */
public class Opportunities_26247 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * ENT/ULT: Verify that calculated fields could not be edited in Opportunity list view
	 *
	 * @throws Exception 
	 */
	@Test
	public void Opportunities_26247_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Inline edit Opportunity record
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);

		// Verify Calculated fields are read-only and could not be edited in opportunity list view
		Assert.assertTrue("Likely Case field is enabled.", sugar().opportunities.listView.getEditField(1, "likelyCase").isDisabled());
		Assert.assertTrue("Expected closed date field is enabled.", sugar().opportunities.listView.getEditField(1, "date_closed").isDisabled());
		sugar().opportunities.listView.getEditField(1, "status").assertAttribute("class", "list", true);

		// Cancel record
		sugar().opportunities.listView.cancelRecord(1);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}