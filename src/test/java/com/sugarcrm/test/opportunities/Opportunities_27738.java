package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_27738 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();		
	}

	/**
	 * Verify that new RLI row can be successfully deleted
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27738_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();

		// RLI fields filled with data
		FieldSet fs = sugar().opportunities.defaultData;
		sugar().opportunities.createDrawer.getEditField("rli_name").set(fs.get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(fs.get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(fs.get("rli_likely"));

		// TODO: VOOD-1357 and VOOD-1359
		// Add new RLI row and filled with data
		new VoodooControl("a", "css", ".fieldset.edit .addBtn").click();
		VoodooUtils.waitForReady();
		String secondRLIRecord = "[data-voodoo-name='subpanel-for-opportunities-create'] tr:nth-of-type(2)";
		VoodooControl secondRLINameCtrl = new VoodooControl("input", "css", secondRLIRecord+" .fld_name.edit input");
		secondRLINameCtrl.set(testName);
		new VoodooControl("input", "css", secondRLIRecord+" .fld_date_closed.edit input").set(fs.get("date_closed"));
		new VoodooControl("input", "css", secondRLIRecord+" .fld_likely_case.edit input").set(fs.get("likelyCase"));
		VoodooControl secondRLIDelete = new VoodooControl("a", "css", secondRLIRecord+" "+".deleteBtn");
		secondRLIDelete.click();

		// Verify second RLI row deleted successfully
		secondRLIDelete.assertVisible(false);
		secondRLINameCtrl.assertVisible(false);

		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}