package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_20098 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Check the data type of fields that not support full text search function
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_20098_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource customDS = testData.get(testName);

		// Go to Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542, VOOD-1504
		// Go to Fields page of a module
		new VoodooControl("a", "id", "studiolink_Accounts").click(); // Click on Accounts module
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();

		// Open a OOTB field or create a new custom field
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();

		// Define controls
		VoodooControl dataTypeDropdownCtrl = new VoodooControl("select", "css", "select#type");
		VoodooControl ftsFieldCtrl = new VoodooControl("select", "id", "fts_field_config");

		for(int i = 0; i < customDS.size(); i++) {
			// Verify if the field data type is: Address, Checkbox ,Currency, Date, Datetime, Decimal, Encrypt, DropDown, Float, HTML, IFrame, Image, MultiSelect, Radio, Relate,
			dataTypeDropdownCtrl.set(customDS.get(i).get("dataType"));
			VoodooUtils.waitForReady();

			// Verify that fields edit page shouldn't show Full Text Searchable drop down
			ftsFieldCtrl.assertExists(false);
		}

		// Cancel the Field edit page
		// TODO: VOOD-1504
		new VoodooControl("input", "css", "input[name='cancelbtn']").click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}