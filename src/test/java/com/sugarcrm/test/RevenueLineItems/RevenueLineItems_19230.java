package com.sugarcrm.test.RevenueLineItems;

import java.text.DecimalFormat;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RevenueLineItems_19230 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify that creating Revenue Line Item is available and functional using quick create 
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_19230_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet myTestData = sugar().revLineItems.getDefaultData();
		sugar().navbar.openQuickCreateMenu();

		// Verify RLI option is under quick create (i.e click method already takes care of its visibility/existence)
		sugar().navbar.quickCreate.getControl(sugar().revLineItems.moduleNamePlural).click();		
		VoodooUtils.waitForReady();

		// Verify standard create drawer is open
		// TODO: VOOD-1887- Once resolved commented line should work and remove very next line
		//sugar().revLineItems.createDrawer.assertVisible(true);
		sugar().revLineItems.createDrawer.getEditField("name").assertVisible(true);
		sugar().revLineItems.createDrawer.showMore();
		sugar().revLineItems.createDrawer.setFields(myTestData);
		sugar().revLineItems.createDrawer.save();

		// Verification for currency/double field values below decimal formatting code is required, when currency field verification is implemented in library update verification to use default data 
		DecimalFormat formatter = new DecimalFormat("##,###.00");
		String likelyDoubleValue = String.format("%s%s", "$", formatter.format(Double.parseDouble(myTestData.get("likelyCase"))));
		myTestData.put("likelyCase", likelyDoubleValue);
		myTestData.put("quantity", formatter.format(Double.parseDouble(myTestData.get("quantity"))));

		// Below code added for ease verification of RLI record  
		RevLineItemRecord myRLI = (RevLineItemRecord)Class.forName(sugar().revLineItems.recordClassName).getConstructor(FieldSet.class).newInstance(myTestData);
		myRLI.verify(myTestData);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}