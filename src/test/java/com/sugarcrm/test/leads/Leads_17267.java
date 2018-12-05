package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_17267 extends SugarTest {	
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify you can preview a Converted Leads record from the Leads module's List View.
	 * @throws Exception
	 */
	@Test
	public void Leads_17267_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar().leads.navToListView();
		
		//Go to listview of Leads module and open the action drop down
		sugar().leads.listView.openRowActionDropdown(1);
		VoodooUtils.waitForReady();

		//TODO: VOOD-585
		//Click on Convert action link
		new VoodooControl("a", "css", ".fld_lead_convert_button.list a").click();
		VoodooUtils.waitForReady();

		// Confirm with Accounts info		
		new VoodooControl("input", "css", "div[data-module='Accounts'] .fld_name.edit input")
			.set(sugar().accounts.getDefaultData().get("name"));
		new VoodooControl("a", "css", "div[data-module='Accounts'] .fld_associate_button.convert"
				+ "-panel-header a").click();
		VoodooUtils.waitForReady();

		// Confirm with Opportunities info		
		new VoodooControl("input", "css", "div[data-module='Opportunities'] .fld_name.edit input")
			.set(sugar().opportunities.getDefaultData().get("name"));
		new VoodooControl("a", "css", "div[data-module='Opportunities'] .fld_associate_button.convert"
				+ "-panel-header a").click();
		VoodooUtils.waitForReady();
		
		// Save the conversion 
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		//Verify and preview the converted Leads record in list view
		sugar().leads.navToListView();
		VoodooUtils.waitForReady();
		sugar().leads.listView.previewRecord(1);
		VoodooUtils.waitForReady();		
		new VoodooControl("span", "css", ".detail.fld_converted span").assertEquals("Converted", true);
		
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}