package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Quotes_28078 extends SugarTest {

	public void setup() throws Exception {
		sugar().quotes.api.create();
		sugar().login();
	}

	/**
	 * Verify Activities subpanel works correctly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Quotes_28078_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);

		FieldSet tasksFS = new FieldSet();
		tasksFS.put("subject", sugar().tasks.getDefaultData().get("subject"));

		BWCSubpanel activitySubpanel = sugar().quotes.detailView.subpanels.get(sugar().tasks.moduleNamePlural);
		activitySubpanel.subpanelAction("#formActivities");
		sugar().tasks.createDrawer.getEditField("subject").set(tasksFS.get("subject"));
		sugar().tasks.createDrawer.save();

		// Verify created Task should be shown in the Activities subpanel.
		activitySubpanel.verify(1, tasksFS, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}