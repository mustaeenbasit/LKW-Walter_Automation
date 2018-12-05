package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Bugs_18600 extends SugarTest {

	public void setup() throws Exception {
		sugar.bugs.api.create();
		sugar.accounts.api.create();
		sugar.login();
		
		// Enable bugs module
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		sugar.admin.enableSubpanelDisplayViaJs(sugar.bugs);
	}

	/**
	 * edit case in case sub-panel of bug detail view
	 * @throws Exception
	 */
	@Test
	public void Bugs_18600_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.bugs.navToListView();
		sugar.bugs.listView.clickRecord(1);

		// Create a case record in Cases sub-panel by in-line page.See Verification Step 0.
		StandardSubpanel casesSubpanel =  sugar.bugs.recordView.subpanels.get(sugar.cases.moduleNamePlural);  
		casesSubpanel.addRecord();
		sugar.cases.createDrawer.getEditField("name").set(testName);
		sugar.cases.createDrawer.getEditField("relAccountName").set(sugar.accounts.getDefaultData().get("name"));
		sugar.cases.createDrawer.save();
		
		// Verify that Case can be created and account name field can be filled.
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		fs.put("relAccountName", sugar.accounts.getDefaultData().get("name"));
		casesSubpanel.verify(1, fs, true);
		
		
		// Create a string variable for update/verify record
		String caseName = "new"+testName;
		
		// Edit the created case by clicking "edit" link.
		fs.clear();
		fs.put("name", caseName);
		casesSubpanel.editRecord(1, fs);
		
		// Verify that Edited case record is displayed as modified in Cases sub-panel.
		casesSubpanel.verify(1, fs, true);
		
		// Click on the sub-panel cases record.
		casesSubpanel.scrollIntoViewIfNeeded(true);
		casesSubpanel.clickRecord(1);
		
		// Verify that edited case record is displayed in Case detail view as modification
		sugar.cases.recordView.getDetailField("name").assertContains(caseName, true);
		
		// Verify that bug record is displayed in Bugs sub-panel on cases recordView.
		StandardSubpanel bugsSubpanel =  sugar.cases.recordView.subpanels.get(sugar.bugs.moduleNamePlural);
		fs.clear();
		fs.put("name", sugar.bugs.getDefaultData().get("name"));
		bugsSubpanel.scrollIntoView();
		bugsSubpanel.expandSubpanel();
		bugsSubpanel.verify(1, fs, true);
		
		// Go to Cases list view
		sugar.cases.navToListView();
		sugar.cases.listView.setSearchString(testName);
		
		// Verify that Original record is not displayed in the list view.
		sugar.cases.listView.assertContains(testName, false);
		
		sugar.cases.listView.setSearchString(caseName);
		
		// Verify that Edited record is displayed in the list view.
		sugar.cases.listView.assertContains(caseName, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}