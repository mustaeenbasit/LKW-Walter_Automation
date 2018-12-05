package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.Record;

public class TargetLists_18141 extends SugarTest{
	DataSource editTarget;
	
	public void setup() throws Exception {
		editTarget = testData.get(testName);
		sugar.login();
	}

	/**
	 * When selecting a target list from the inline section of the list view, user is able to create a new targetlist
	 */
	@Test
	public void TargetLists_18141_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create one contact record
		Record contactRecord = sugar.contacts.api.create();
		
		// Nav to contacts listview and add one contact to target list
		sugar.contacts.navToListView();
		sugar.contacts.listView.setSearchString(contactRecord.getRecordIdentifier());
		sugar.contacts.listView.toggleSelectAll();
		sugar.contacts.listView.openActionDropdown();
		
		// TODO: VOOD-992 - Lib support that create a targetlist from user type listview
		// Open "Add to Targetlist" action
		new VoodooControl("span", "css", ".fld_addtolist_button.list a").click();
		new VoodooControl("span", "css", ".fld_create_button.massaddtolist a").click();
		sugar.targetlists.createDrawer.getEditField("targetlistName").set(editTarget.get(0).get("targetlistName"));
		
		// TOD: VOOD-800 - Lib support for sub panel functions in a Targetlist record
		// Save the targetlist create drawer
		sugar.targetlists.createDrawer.save();
		sugar.alerts.getSuccess().closeAlert();
		
		// TODO: VOOD-992 - Lib support that create a targetlist from user type listview
		new VoodooControl("div", "css", "div.select2-container.select2-allowclear").assertContains(editTarget.get(0).get("targetlistName"), true);

		// Update Contact record
		new VoodooControl("a", "css", "span.fld_update_button a").click();
		sugar.alerts.waitForLoadingExpiration();
		
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);
		sugar.targetlists.recordView.subpanels.get("Contacts").expandSubpanel();
		sugar.targetlists.recordView.subpanels.get("Contacts").scrollIntoViewIfNeeded(false);

		// TODO: VOOD-1708 - Need Lib support to verify existence of a Subpanel record with column(s) value per a provided FieldSet
		new VoodooControl("span", "css", "span.fld_full_name.list").assertContains(contactRecord.getRecordIdentifier(), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}
