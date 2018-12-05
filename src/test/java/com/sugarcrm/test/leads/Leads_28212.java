package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_28212 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that on Lead conversion Account Panel collapses and Opportunity panel expands.
	 * @throws Exception
	 */
	@Test
	public void Leads_28212_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-585: Need to have method (library support) to define Convert function in Leads
		// Click on convert link
		new VoodooControl("a", "css", ".fld_lead_convert_button.detail a").click();
		
		// Confirm with Accounts info
		VoodooControl accFieldInput = new VoodooControl("input", "css", "div[data-module='Accounts'] .fld_name.edit input");
		accFieldInput.waitForVisible();
		accFieldInput.set(testName);
		VoodooControl accAssociate = new VoodooControl("a", "css", "div[data-module='Accounts'] .fld_associate_button.convert-panel-header a");
		accAssociate.click();
		
		// Verify that Account Panel collapses and Opportunity panel expands.
		VoodooControl oppInputField = new VoodooControl("input", "css", "div[data-module='Opportunities'] .fld_name.edit input");
		oppInputField.assertVisible(true);
		
		// While Account panel is collapsed and shows Reset, expand panel by clicking empty area of title bar
		new VoodooControl("div", "css", ".accordion-heading.enabled[data-module='Accounts']").click();
		
		// Verify that Account Panel expands and Opportunity panel collapses.
		oppInputField.assertVisible(false);
		
		// While expanded, click Reset
		new VoodooControl("a", "css", "[data-module='Accounts'] [name='reset_button']").click();
		
		// Click Associate Account button
		accAssociate.click();
		
		// Verify that Account Panel collapses and Opportunity panel expands.
		oppInputField.assertVisible(true);
		
		// Cancel the conversion
		new VoodooControl("a", "css", ".fld_cancel_button.convert-headerpane a").click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}