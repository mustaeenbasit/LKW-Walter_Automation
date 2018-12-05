package com.sugarcrm.test.opportunities;

import java.text.DecimalFormat;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */
public class Opportunities_24262 extends SugarTest {
	FieldSet defOppKS;
	String controlName;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		defOppKS = sugar().opportunities.getDefaultData();
		sugar().login();
		sugar().opportunities.create();
	}

	/**
	 * Create Opportunity_Verify that opportunity can be duplicated when using "Copy" action in "Opportunity" record view
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24262_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Opportunity record view and click "Copy" button then save button
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.copy();
		sugar().opportunities.createDrawer.save();

		sugar().opportunities.createDrawer.getEditField("rli_name").set(sugar().opportunities.getDefaultData().get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(sugar().opportunities.getDefaultData().get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(sugar().opportunities.getDefaultData().get("rli_likely"));
		sugar().opportunities.createDrawer.save();

		// Click "Ignore Duplicate and Save" button
		sugar().opportunities.createDrawer.ignoreDuplicateAndSave();

		// Verify all fields of opportunity record is correctly duplicated
		sugar().opportunities.recordView.showMore();

		for (String controlName : defOppKS.keySet()) {
			if (defOppKS.get(controlName) != null) {
				if (sugar().opportunities.recordView.getDetailField(controlName) == null) {
					continue;
				}
				// TODO: VOOD-1402 Need to support verification of currency
				String toVerify = defOppKS.get(controlName);										
				if (toVerify == sugar().opportunities.getDefaultData().get("likelyCase")){
					double amount = Double.parseDouble(toVerify);
					DecimalFormat formatter = new DecimalFormat("$##,###.00");
					String toVerify1 = formatter.format(amount);
					sugar().opportunities.recordView.getDetailField(controlName).assertContains(toVerify1, true);
				}else
					sugar().opportunities.recordView.getDetailField(controlName).assertContains(toVerify, true);
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}