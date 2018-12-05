package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_20613 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar.login();		
	}

	/**
	 * Verify that user folder can be successfully created under 'My Emails' folder in Emails module.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20613_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1168
		// Create custom folders under 'My Emails' folder.
		for (int i=0; i<ds.size(); i++ ){
			// Right click on My Emails folder.
			new VoodooControl("table", "css", "#emailtree > div > div > div > table").rightClick();
			
			// Select "Create Folder".
			new VoodooControl("a", "css", "#folderContextMenu div.bd ul li:nth-child(3) a").click();
			
			// Enter valid data into text field, then OK.
			new VoodooControl("input", "id", "sugar-message-prompt").set(ds.get(i).get("folder_name"));
			new VoodooControl("button", "css", "#sugarMsgWindow .yui-push-button.default button").click();
			VoodooUtils.pause(3000);
		}		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}