package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.InboundEmailRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Emails_20717 extends SugarTest {
	FieldSet emailRecord;
	InboundEmailRecord myInBoundData;

	public void setup() throws Exception {
		emailRecord = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Verify Admin Inbound Email creation
	 * @throws Exception
	 */
	@Test
	public void Emails_20717_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create Inbound email settings
		FieldSet fs = new FieldSet();
		fs.put("name", emailRecord.get("emailName"));
		sugar.inboundEmail.create(fs);
		
		// Verify the Inbound Email has been saved correctly
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css",
				".detail.view table tr:nth-of-type(2) td:nth-of-type(2)")
				.assertContains(emailRecord.get("emailName"), true);
		VoodooUtils.focusDefault();
		
		// Verify Inbound email deleted successfully
		sugar.inboundEmail.deleteAll();
		VoodooUtils.focusDefault();
		
		// Verified that the deleted mail account name link is not displayed in Last Viewed bar.
		sugar.inboundEmail.navToListView();
		sugar.inboundEmail.listView.assertContains(emailRecord.get("emailName"), false);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}