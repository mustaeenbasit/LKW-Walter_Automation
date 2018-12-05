package com.sugarcrm.test.RevenueLineItems;

import java.text.NumberFormat;
import java.util.Locale;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_30763 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  [ RLI ] Verify that Unit Price, Best/Worst amounts populated from Likely field 
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30763_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//  Go to RLI module and hit Create button
		sugar().revLineItems.navToListView();		
		sugar().revLineItems.listView.create();
		
		// Enter Likely amount and click on any other field
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("quantity").set(sugar().revLineItems.getDefaultData().get("quantity"));
		sugar().revLineItems.createDrawer.showMore();
		
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
		String likelyAmount = n.format(Integer.parseInt(sugar().revLineItems.getDefaultData().get("likelyCase")));
		
		// Verify that Likely amount value is copied into Unit Price and Best/Worst case fields
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertEquals(likelyAmount.substring(1), true);
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertEquals(likelyAmount.substring(1), true);
		sugar().revLineItems.createDrawer.getEditField("unitPrice").assertEquals(likelyAmount.substring(1), true);
		
		// close the createDrawer 
		sugar().revLineItems.createDrawer.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}