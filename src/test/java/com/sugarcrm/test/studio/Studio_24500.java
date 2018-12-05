package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_24500 extends SugarTest {
	DataSource customData;
	VoodooControl accountsSubPanelCtrl; 

	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar().login();
	}

	/**
	 * Calculated Fields can be of any data type that exists in the application
	 * @throws Exception
	 */
	@Test
	public void Studio_24500_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio 
		sugar().admin.adminTools.getControl("studio").click();
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		VoodooControl calculatedCtrl = new VoodooControl("input", "id", "calculated"); 
		VoodooControl cancelFieldCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=cancelbtn]");
		VoodooControl dataTypeCtrl = new VoodooControl("select", "css", "#type");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");

		// TODO: VOOD-999
		VoodooControl editFormulaCtrl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		VoodooControl formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
		VoodooControl cancelFormulaCtrl = new VoodooControl("input", "css", "input[name=formulacancelbtn]");
		VoodooControl studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		VoodooControl fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubPanelCtrl.click();

		// Adding multiple custom fields 
		for(int i=0; i< customData.size(); i++){
			fieldCtrl.click();
			addFieldCtrl.click();
			VoodooUtils.waitForReady();
			dataTypeCtrl.set(customData.get(i).get("data_type"));
			VoodooUtils.waitForReady();
			fieldNameCtrl.set(customData.get(i).get("module_field_name"));

			// Verifying  there is a Calculated check box on the field edit page
			calculatedCtrl.assertExists(true);
			calculatedCtrl.click();
			editFormulaCtrl.click();
			VoodooUtils.waitForReady();

			// Verifying pop up a formula builder window
			formulaInputCtrl.assertExists(true);
			cancelFormulaCtrl.click();
			cancelFieldCtrl.click();
			studioFooterCtrl.click();
			accountsSubPanelCtrl.click(); 
		}
		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}