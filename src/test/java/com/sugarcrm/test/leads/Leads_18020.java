package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Leads_18020 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Cancel Converting a Leads record from the Leads module's List View.
	 * @throws Exception
	 */
	@Test
	public void Leads_18020_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().leads.navToListView();
		
		// Open the action dropdown next to record 1 on list view
		sugar().leads.listView.openRowActionDropdown(1);
		
		// Click 'Convert' option
		// TODO : VOOD-585 is reported. Once it is fixed, please replace the following steps.
		new VoodooControl("a", "css", ".fld_lead_convert_button.list a").click();
		VoodooUtils.waitForReady();
		
		// Confirm with Accounts info
		new VoodooControl("input", "css", "div[data-module='Accounts'] .fld_name.edit input").set
			(customData.get("accountName"));
		new VoodooControl("a", "css", "div[data-module='Accounts'] .fld_associate_button.convert-"
				+ "panel-header a").click();
		
		// Confirm with Opportunities info
		new VoodooControl("input", "css", "div[data-module='Opportunities'] .fld_name.edit input").
			set(customData.get("OppName"));
		new VoodooControl("a", "css", "div[data-module='Opportunities'] .fld_associate_button.convert"
				+ "-panel-header a").click();
		
		// Cancel the conversion
		new VoodooControl("a", "css", ".fld_cancel_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		sugar().leads.listView.clickRecord(1);
		
		// Verify the Leads record is not converted
		new VoodooControl("span", "css", ".detail.fld_converted span").assertEquals("Unconverted",true);

		sugar().contacts.navToListView();
		sugar().contacts.listView.assertIsEmpty();
		
		sugar().accounts.navToListView();
		sugar().accounts.listView.assertIsEmpty();
		
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	@Override
	public void cleanup() throws Exception {}
}