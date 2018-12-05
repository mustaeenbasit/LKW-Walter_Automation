package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24267 extends SugarTest {
	public void setup() throws Exception {
		FieldSet fs = new FieldSet();
		for (int i = 0; i < 3; i++) {
			fs.put("name", "Opp_"+i);
			sugar().opportunities.api.create(fs);
		}
		sugar().login();
	}

	/**
	 * Mass Update Opportunities_Verify that the selected opportunities can be updated by using "Mass Update" sub-panel.
	 *  
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24267_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Go to Opportunities listview
		sugar().opportunities.navToListView();
		
		// Select several opportunities, open action dropdown and click Mass Update
		sugar().opportunities.listView.toggleSelectAll();
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.massUpdate();
		
		// Fill all the fields in "Mass Update" and then click "Update" button
		// TODO: VOOD-1003
		new VoodooSelect("a", "css", ".filter-field a").set(customData.get("filterField"));
		new VoodooSelect("a", "css", ".filter-value a").set(customData.get("filterValue"));
		new VoodooControl("a", "css", ".fld_update_button.massupdate a").click();
		VoodooUtils.waitForReady();
		
		// Verify that the selected opportunities are updated by the values entered in "Mass Update"
		sugar().opportunities.listView.verifyField(1, "type", customData.get("filterValue"));
		sugar().opportunities.listView.verifyField(2, "type", customData.get("filterValue"));
		sugar().opportunities.listView.verifyField(3, "type", customData.get("filterValue"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}