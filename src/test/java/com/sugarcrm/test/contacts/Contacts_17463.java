package com.sugarcrm.test.contacts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_17463 extends SugarTest {
	public void setup() throws Exception {
		//Create a default contact name
		sugar().contacts.api.create();
		sugar().login();

		// Enabling disabled subpanels
		ArrayList<Module> disabledSubpanels = new ArrayList<Module>();
		disabledSubpanels.add(sugar().projects);
		disabledSubpanels.add(sugar().bugs);
		disabledSubpanels.add(sugar().quotedLineItems);
		disabledSubpanels.add(sugar().contracts);
		sugar().admin.enableSubpanelDisplayViaJs(disabledSubpanels);
	}

	/** Verify if the user selects Related: "All",
	 *  he gets a stacked view of all related modules in Contact's sub panel
	 *  @throws Exception
	 */
	@Test
	public void Contacts_17463_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to record view of contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Creating a Task record in task subpanel.
		StandardSubpanel taskSubpanel = sugar().contacts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskSubpanel.addRecord();
		sugar().tasks.createDrawer.getEditField("subject").set(testName);
		sugar().tasks.createDrawer.save();

		// Verify related dropdown on subpanel
		VoodooControl relatedCtrl = sugar().contacts.recordView.getControl("relatedSubpanelFilter");
		relatedCtrl.assertContains("Related", true);

		// Click on related button to show module list
		relatedCtrl.click();

		// VOOD-468
		// Verify module lists in related modules dropdown
		VoodooControl allCtrl = new VoodooControl("li", "css", "#select2-drop ul li");
		allCtrl.assertExists(true);
		VoodooControl subpanelModules = new VoodooControl("div", "id", "select2-drop");
		DataSource contactSubpanels = testData.get(testName);
		for (int i = 0; i < contactSubpanels.size(); i++) {
			subpanelModules.assertContains(contactSubpanels.get(i).get("moduleName"), true);
		}

		// Click to close the dropdown menu
		allCtrl.click();

		// Setting subpanel filter for Task record
		sugar().contacts.recordView.setSearchString(testName);

		// Verify the record still appears in Task subpanel
		taskSubpanel.getDetailField(1, "subject").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}