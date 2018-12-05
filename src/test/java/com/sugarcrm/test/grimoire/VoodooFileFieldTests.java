package com.sugarcrm.test.grimoire;

import java.util.Map;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class VoodooFileFieldTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void importDataViaCSV() throws Exception {
		VoodooUtils.voodoo.log.info("Running importDataViaCSV()...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().navbar.selectMenuItem(sugar().accounts, "importAccounts");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1396
		new VoodooFileField("input", "id", "userfile").set("src/test/resources/data/VoodooFileFieldTests.csv");
		new VoodooControl("input", "id", "gonext").click();
		VoodooUtils.waitForReady();
		VoodooControl importedDataKeys = new VoodooControl("div", "css", "#confirm_table #importTable tbody tr");
		VoodooControl importedDataValues = new VoodooControl("div", "css", "#confirm_table #importTable tbody tr:nth-of-type(2)");

		// Verify import data with key and values in table
		for(Map.Entry<String,String> entry : customData.entrySet()) {
			importedDataKeys.assertElementContains(entry.getKey().toString(), true);
			importedDataValues.assertElementContains(entry.getValue().toString(), true);
		}
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("importDataViaCSV() complete.");
	}

	public void cleanup() throws Exception {}
}