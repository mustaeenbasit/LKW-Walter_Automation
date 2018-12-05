package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.Record;

public class TargetLists_18145 extends SugarTest{
	public void setup() throws Exception {
		sugar.login();		
	}

	/**
	 * Without any targetlist is selected, click on Update
	 * @author Eric Yang <eyang@sugarcrm.com>
	 */
	@Test
	public void TargetLists_18145_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create one contact record
		Record contactRecord = sugar.contacts.api.create();
		
		// Nav to contacts listview and add one contact to target list
		sugar.contacts.navToListView();
		sugar.contacts.listView.setSearchString(contactRecord.getRecordIdentifier());
		sugar.contacts.listView.toggleSelectAll();
		sugar.contacts.listView.openActionDropdown();
		
		new VoodooControl("span", "css", ".fld_addtolist_button.list a").click();
		VoodooUtils.pause(1000);	
		new VoodooControl("a", "css", ".fld_update_button a").click();
		VoodooUtils.pause(1000);
		new VoodooControl("span", "css", "#content .help-block").assertContains("Error. This field is required.", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}
