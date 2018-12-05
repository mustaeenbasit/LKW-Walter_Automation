package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_21306 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that proper validation message is displayed while creating custom field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_21306_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource customData = testData.get(testName);

		// TODO: VOOD-517 - Studio Lib Support, for all custom controls defined here
		VoodooControl accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");		

		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusFrame("bwc-frame");
		accountsSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();

		for (FieldSet fs : customData) {
			VoodooUtils.focusDefault();
			VoodooUtils.focusFrame("bwc-frame");

			// Text Field Validation
			new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
			VoodooUtils.waitForReady();
			
			// Set Field type - TextField
			new VoodooControl("select", "css", "#type").set(fs.get("data_type"));
			VoodooUtils.waitForReady();
			
			// Verify no error message on page as of now
			new VoodooControl("div", "xpath", "//form[@name='popup_form']//td[.//input[@id='field_name_id']]//div[contains(@class,'validation-message')]")
				.assertExists(false);
			
			// Save without field name
			new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
			VoodooUtils.waitForReady();
			
			// Verify Error message
			new VoodooControl("div", "xpath", "//form[@name='popup_form']//td[.//input[@id='field_name_id']]//div[contains(@class,'validation-message')]")
				.assertContains("Missing required field: Field Name", true);
			
			// Perform additional check for TextField
			if (fs.get("data_type").equals("Textfield")) {
				// Wrong Field name
				new VoodooControl("input", "id", "field_name_id").set(fs.get("field_name"));
				
				// Wrong Field length
				new VoodooControl("input", "id", "field_len").set(fs.get("field_len"));
				
				// Save
				new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
				VoodooUtils.waitForReady();
				
				// Verify Error message
				new VoodooControl("div", "xpath", "//form[@name='popup_form']//td[.//input[@id='field_name_id']]//div[contains(@class,'validation-message')]")
					.assertContains("Field name should contain only the following characters: a-zA-Z_", true);
				
				// Verify Field Len set back to 255
				new VoodooControl("input", "id", "field_len").assertContains("255", true);
			}
			
			// Perform additional check for Integer
			if (fs.get("data_type").equals("Integer")) {
				// Set Field name
				new VoodooControl("input", "id", "field_name_id").set(fs.get("field_name"));
				
				// Set Field length
				new VoodooControl("input", "id", "int_len").set(fs.get("field_len"));
				
				// Verify Default value cannot be greater than max value.
				// Set Field min
				new VoodooControl("input", "id", "int_min").set(fs.get("field_min"));
				// Set Field max
				new VoodooControl("input", "id", "int_max").set(fs.get("field_max"));
				// Set Field Default
				new VoodooControl("input", "id", "int_default").set(fs.get("field_default"));
				// Save
				new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
				VoodooUtils.waitForReady();
				// Verify Error message
				new VoodooControl("div", "xpath", "//form[@name='popup_form']//td[.//input[@id='int_min']]//div[contains(@class,'validation-message')]")
				.assertContains("Invalid Logic.", true);
				new VoodooControl("div", "xpath", "//form[@name='popup_form']//td[.//input[@id='int_max']]//div[contains(@class,'validation-message')]")
				.assertContains("Invalid Logic.", true);
				new VoodooControl("div", "xpath", "//form[@name='popup_form']//td[.//input[@id='int_default']]//div[contains(@class,'validation-message')]")
				.assertContains("Invalid Logic.", true);

				// Verify Default value cannot be less than min value.
				// Set Field min
				new VoodooControl("input", "id", "int_min").set(fs.get("field_min1"));
				// Set Field max
				new VoodooControl("input", "id", "int_max").set(fs.get("field_max1"));
				// Set Field Default
				new VoodooControl("input", "id", "int_default").set(fs.get("field_default1"));
				// Save
				new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
				VoodooUtils.waitForReady();
				// Verify Error message
				new VoodooControl("div", "xpath", "//form[@name='popup_form']//td[.//input[@id='int_min']]//div[contains(@class,'validation-message')]")
				.assertContains("Invalid Logic.", true);
				new VoodooControl("div", "xpath", "//form[@name='popup_form']//td[.//input[@id='int_max']]//div[contains(@class,'validation-message')]")
				.assertContains("Invalid Logic.", true);
				new VoodooControl("div", "xpath", "//form[@name='popup_form']//td[.//input[@id='int_default']]//div[contains(@class,'validation-message')]")
				.assertContains("Invalid Logic.", true);

				// Verify that Max value must be greater than Min Value.
				// Set Field min
				new VoodooControl("input", "id", "int_min").set(fs.get("field_min2"));
				// Set Field max
				new VoodooControl("input", "id", "int_max").set(fs.get("field_max2"));
				// Set Field Default
				new VoodooControl("input", "id", "int_default").set(fs.get("field_default2"));
				// Save
				new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
				VoodooUtils.waitForReady();
				// Verify Error message
				new VoodooControl("div", "xpath", "//form[@name='popup_form']//td[.//input[@id='int_min']]//div[contains(@class,'validation-message')]")
				.assertContains("Invalid Logic.", true);
				new VoodooControl("div", "xpath", "//form[@name='popup_form']//td[.//input[@id='int_max']]//div[contains(@class,'validation-message')]")
				.assertContains("Invalid Logic.", true);
				new VoodooControl("div", "xpath", "//form[@name='popup_form']//td[.//input[@id='int_default']]//div[contains(@class,'validation-message')]")
				.assertContains("Invalid Logic.", true);
			}
			
			// Perform additional check for Decimal
			if (fs.get("data_type").equals("Decimal")) {
				// Set Field name
				new VoodooControl("input", "id", "field_name_id").set(fs.get("field_name"));
				
				// Set Field Default
				new VoodooControl("input", "name", "default").set(fs.get("field_default"));
				
				// Set Field length
				new VoodooControl("input", "name", "len").set(fs.get("field_len"));
				
				// Set Field Precision
				new VoodooControl("input", "name", "precision").set(fs.get("field_precision"));
				
				// Save
				new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
				VoodooUtils.waitForReady();
				
				// Verify Error message
				new VoodooControl("div", "xpath", "//form[@name='popup_form']//td[.//input[@id='default']]//div[contains(@class,'validation-message')]")
					.assertContains("Default Value exceeds the max length specified", true);
			}
			
			// Cancel
			new VoodooControl("input", "css", "input[name='cancelbtn']").click();
			VoodooUtils.waitForReady();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
