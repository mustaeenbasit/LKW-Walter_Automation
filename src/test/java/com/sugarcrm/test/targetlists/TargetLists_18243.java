package com.sugarcrm.test.targetlists;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.VoodooUtils;

public class TargetLists_18243 extends SugarTest{
	public void setup() throws Exception {
		sugar.login();	
	}
	
	/**
	 * Verify 5 modules in the Related dropdown in targetlist sub panel
	 * @author Eric Yang <eyang@sugarcrm.com>
	 */
	@Test
	public void TargetLists_18243_execute() throws Exception {
		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create a target list and Nav to recordView
		
		TargetListRecord myTargetList = (TargetListRecord)sugar.targetlists.api.create();
		myTargetList.navToRecord();
		
		new VoodooControl("div", "css", ".select2-container.select2.related-filter a").click();
		new VoodooControl("div", "xpath", "//div[@id='select2-drop']//div[contains(text(),'Targets')]").assertVisible(true);
		new VoodooControl("div", "xpath", "//div[@id='select2-drop']//div[contains(text(),'Contacts')]").assertVisible(true);
		new VoodooControl("div", "xpath", "//div[@id='select2-drop']//div[contains(text(),'Leads')]").assertVisible(true);
		new VoodooControl("div", "xpath", "//div[@id='select2-drop']//div[contains(text(),'Users')]").assertVisible(true);
		new VoodooControl("div", "xpath", "//div[@id='select2-drop']//div[contains(text(),'Accounts')]").assertVisible(true);
		new VoodooControl("div", "xpath", "//div[@id='select2-drop']//div[contains(text(),'All')]").click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}
