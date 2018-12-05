package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.ContactRecord;

public class TargetLists_18144 extends SugarTest{
	public void setup() throws Exception {
		sugar.login();	
	}
	
	/**
	 * Verify that correct error is displayed if try to create target list without providing the name first. 
	 * 
	 */
	@Test
	public void TargetLists_18144_execute() throws Exception {
		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Nav to contact listview and add a contact to target list
		ContactRecord myContact = (ContactRecord)sugar.contacts.api.create();
		sugar.contacts.navToListView();
		sugar.contacts.listView.setSearchString(myContact.getRecordIdentifier());
		sugar.contacts.listView.toggleSelectAll();
		sugar.contacts.listView.openActionDropdown();
		
		new VoodooControl("span", "css", ".fld_addtolist_button.list a").click();
		new VoodooControl("span", "css", ".fld_create_button.massaddtolist a").click();
		
		sugar.targetlists.createDrawer.save();
		
		new VoodooControl("div", "css", "#drawers .input-append.error.input input").assertAttribute("placeholder", "Required");
		new VoodooControl("span", "css", "#drawers .error-tooltip.add-on").assertAttribute("data-original-title", "Error. This field is required.");
		sugar.alerts.closeAllAlerts();
		sugar.targetlists.createDrawer.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}
