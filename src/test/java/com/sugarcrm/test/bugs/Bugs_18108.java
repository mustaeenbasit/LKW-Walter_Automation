package com.sugarcrm.test.bugs;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Bugs_18108 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * Verify the fields on bug record view on Sugar Application 
	 * @throws Exception
	 */
	@Test
	public void Bugs_18108_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.selectMenuItem(sugar.bugs, "createBug");
		sugar.bugs.createDrawer.getEditField("name").assertAttribute("class", "required", true);

		// TODO: VOOD-1425
		new VoodooControl("span", "css", ".fld_bug_number.nodata").assertVisible(true);
		sugar.bugs.createDrawer.getEditField("status").assertVisible(true);
		sugar.bugs.createDrawer.getEditField("source").assertVisible(true);
		sugar.bugs.createDrawer.getEditField("resolution").assertVisible(true);
		sugar.bugs.createDrawer.getEditField("description").assertVisible(true);
		sugar.bugs.createDrawer.getEditField("priority").assertVisible(true);
		sugar.bugs.createDrawer.getEditField("type").assertVisible(true);
		sugar.bugs.createDrawer.getEditField("category").assertVisible(true);
		sugar.bugs.createDrawer.getEditField("relAssignedTo").assertVisible(true);
		sugar.bugs.recordView.showMore();

		// TODO: VOOD-436 and VOOD-1425
		assertTrue("This checkbox should NOT be checked!",(false == Boolean.parseBoolean(new VoodooControl("input", "css", ".fld_portal_viewable.edit input").getAttribute("checked"))));
		sugar.bugs.createDrawer.getEditField("found_in_release").assertVisible(true);
		sugar.bugs.createDrawer.getEditField("relTeam").assertVisible(true);
		sugar.bugs.createDrawer.getEditField("work_log").assertVisible(true);
		sugar.bugs.createDrawer.getEditField("fixed_in_release").assertVisible(true);

		// TODO: VOOD-1425
		new VoodooControl("span", "css", ".fld_date_entered_by.nodata").assertVisible(true);
		new VoodooControl("span", "css", ".fld_date_modified_by.nodata").assertVisible(true);
		sugar.bugs.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}