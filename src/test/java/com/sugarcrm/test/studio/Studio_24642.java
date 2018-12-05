package com.sugarcrm.test.studio;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_24642 extends SugarTest {
	VoodooControl accountsButtonCtrl;
	VoodooControl fieldCtrl;
	VoodooControl formulaInputCtrl;
	VoodooControl formulaResultCtrl;
	
	DataSource ds, dsType;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * List related module's available fields when use rollupMin function
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24642_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-938
		accountsButtonCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");

		ds = testData.get(testName);
		dsType = testData.get(testName+"_type");
		
		// Navigate to Admin > Studio > Accounts > Fields
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		accountsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Select Fields button
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		// Add field and add Formula
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("input", "id", "field_name_id").set("myField");
		new VoodooControl("input", "id", "calculated").click();
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();

		// TODO: VOOD-938
		// Click Rollup
		new VoodooControl("a", "css", "li.markItUpButton.markItUpButton2.rollup.button a").click();

		// TODO: VOOD-1037 for all Rollup popup controls as mentioned below
		
		// Assert if three select dropdowns exist
		new VoodooControl("select", "id", "rollwiz_type").assertExists(true);
		new VoodooControl("select", "id", "rollwiz_rmodule").assertExists(true);
		new VoodooControl("select", "id", "rollwiz_rfield").assertExists(true);
		
		// Assert if relevant fields appear in field dropdown when a module 
		// is chosen from the module dropdown
		
		String lastModuleLabel = "";
		String optionString = "";
		int optionCount = 0;
		String[] myArray;

		// This outer loop will take care of Types and in turn will also check TCs 24640, 24641 and 24643
		for(int j=0; j <= dsType.size()-1; j++) {
			// Select Type
			new VoodooControl("select", "id", "rollwiz_type").set(dsType.get(j).get("type"));
	
			// Select Module
			lastModuleLabel = "";		
			for(int i=0; i <= ds.size()-1; i++) {
				if (!ds.get(i).get("module_label").toString().contentEquals(lastModuleLabel)) {
					new VoodooControl("select", "id", "rollwiz_rmodule").set(ds.get(i).get("module_label"));
					lastModuleLabel = ds.get(i).get("module_label").toString();
	
					// Calculate no. of items in the Fields Dropdown
					optionString = new VoodooControl("select", "id", "rollwiz_rfield").getText().trim();
					if (optionString.isEmpty())
						optionCount = 0;
					else {
						myArray = optionString.split("stop|\\n");
						optionCount = myArray.length;
					}
				}
				
				// Check if Fields Dropdown items count matches
				if (Integer.valueOf(ds.get(i).get("field_count")) != optionCount)
					assertTrue("Error in Dropdown Items Count", Integer.valueOf(ds.get(i).get("field_count")) == optionCount);
	
				// Check if Fields Dropdown items match with the given values in CSV 
				if (optionCount > 0) {
					new VoodooControl("select", "id", "rollwiz_rfield").set(ds.get(i).get("field_label"));
					new VoodooControl("option", "css", "select[id='rollwiz_rfield'] option[value='"+ds.get(i).get("option_value")+"']").assertExists(true);
				}
			}
		}

		// TODO: VOOD-938
		new VoodooControl("button", "name", "selrf_cancelbtn").click();
		new VoodooControl("input", "name", "formulacancelbtn").click();
		new VoodooControl("input", "name", "cancelbtn").click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}