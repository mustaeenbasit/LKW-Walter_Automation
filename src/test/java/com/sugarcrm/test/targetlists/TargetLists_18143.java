package com.sugarcrm.test.targetlists;

import com.sugarcrm.test.SugarTest;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.ContactRecord;

public class TargetLists_18143 extends SugarTest{
	public void setup() throws Exception {
		sugar.login();	
	}
	
	/**
	 * Cancel while "Create new targetlist" on the create inline modal
	 * @author Eric Yang <eyang@sugarcrm.com>
	 */
	@Test
	public void TargetLists_18143_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Nav to contact listview and add a contact to target list
		ContactRecord myContact = (ContactRecord)sugar.contacts.api.create();
		sugar.contacts.navToListView();
		sugar.contacts.listView.setSearchString(myContact.getRecordIdentifier());
		sugar.contacts.listView.toggleSelectAll();
		sugar.contacts.listView.openActionDropdown();
		new VoodooControl("span", "css", ".fld_addtolist_button.list a").click();
		
		// Click on Create New Target List link
		new VoodooControl("span", "css", ".fld_create_button.massaddtolist a").click();
		// Type a nane for a new Target list
		new VoodooControl("span", "css", "#drawers .fld_name.edit input").set("targetList_1");
		// Cancel Target List Creation 
		sugar.targetlists.createDrawer.cancel();
				
		// Verify the page returns to Contact's listview
		new VoodooControl("input", "css", ".toggle-all").assertAttribute("checked", "true");
				
		// Verify there has not target list in listview
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.setSearchString("targetList_1");
		sugar.targetlists.listView.assertIsEmpty();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}
