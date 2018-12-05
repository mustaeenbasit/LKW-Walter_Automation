package com.sugarcrm.test.campaigns;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19381 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		sugar.campaigns.api.create();
		customData = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * WebToLead_Verify that the corresponding web to lead form is generated without notice.
	 *
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19381_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Campaigns module 
		sugar.navbar.navToModule(sugar.campaigns.moduleNamePlural);
		sugar.navbar.clickModuleDropdown(sugar.campaigns);
		// Click "Create Lead Form" link .
		sugar.campaigns.menu.getControl("createLeadForm").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify No notices are displayed.
		Assert.assertFalse("No notice is displayed", VoodooUtils.isDialogVisible());

		// Drag and drop lead fields in columns 1 and 2.
		// TODO: VOOD-1532
		new VoodooControl("span", "id", "last_name_row").dragNDrop(new VoodooControl("td", "css", "#SUGAR_GRID td:nth-child(2)"));
		new VoodooControl("span", "id", "status_row").dragNDrop(new VoodooControl("td", "css", "#SUGAR_GRID td:nth-child(3)"));
		// Click "Next" button on "Create Lead Form: Select Fields" page.
		new VoodooControl("input", "id", "lead_next_button").click();
		// Enter data in required fields
		new VoodooControl("input", "css", "tbody tbody tr:nth-child(6) td:nth-child(2) input.button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", ".list.view tbody tr.oddListRowS1 td:nth-child(1) a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		// Generate Form
		// TODO: VOOD-1532
		new VoodooControl("input", "css", "[name='button'][value='Generate Form']").click();
		VoodooUtils.focusFrame("body_html_ifr");

		// Verify The corresponding web to lead preview form is displayed 
		new VoodooControl("h2", "css", "#WebToLeadForm tbody tr:nth-child(1) td h2").assertContains(customData.get("text"), true);
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		// Verify the form is created without notices
		Assert.assertFalse("Form is created without notice i.e No javascript dialog", VoodooUtils.isDialogVisible());
		// Cancel
		new VoodooControl("input", "css", "[value='Cancel']").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}