package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.records.BugRecord;

public class Bugs_18529 extends SugarTest {
	BugRecord myBug;

	// Common VoodooControl and VoodooSelect references
	VoodooControl studioLink;
	VoodooControl studioLinkBugs;
	VoodooControl layoutsButton;
	VoodooControl listviewButton;

	public void setup() throws Exception {
		
		sugar.login();
		myBug = (BugRecord) sugar.bugs.api.create();
		
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		
		// TODO VOOD-517 Create Studio Module (BWC) - will provide the
		// references to replace these explicit VoodooControls
		studioLink = new VoodooControl("a", "id", "studio");
		studioLinkBugs = new VoodooControl("a", "id", "studiolink_Bugs");
		layoutsButton = new VoodooControl("a", "css", "td#layoutsBtn a");
		listviewButton = new VoodooControl("a", "css", "td#viewBtnlistview a");

	}

	/**
	 * Verify the default List View for Bug Tracker is correct in Studio
	 * 
	 * @throws Exception
	 */
	@Test
	public void Bugs_18529_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		studioLink.click();

		studioLinkBugs.click();

		layoutsButton.click();

		listviewButton.click();

		// Verify Default panel should contain: Number, Subject, Status, Type,
		// Priority, Fixed in Release, Assigned to
		new VoodooControl("td", "css", "td#Default td#subslot0label")
				.assertContains("Number", true);
		new VoodooControl("td", "css", "td#Default td#subslot1label")
				.assertContains("Subject", true);
		new VoodooControl("td", "css", "td#Default td#subslot2label")
				.assertContains("Status", true);
		new VoodooControl("td", "css", "td#Default td#subslot3label")
				.assertContains("Type", true);
		new VoodooControl("td", "css", "td#Default td#subslot4label")
				.assertContains("Priority", true);
		new VoodooControl("td", "css", "td#Default td#subslot5label")
				.assertContains("Fixed in Release", true);
		new VoodooControl("td", "css", "td#Default td#subslot6label")
				.assertContains("Assigned to", true);

		VoodooUtils.refresh();

		// Verify the columns on the List View should match the Studio defaults
		sugar.bugs.navToListView();
		sugar.bugs.listView.verifyField(1, "name", myBug.get("name"));
		sugar.bugs.listView.verifyField(1, "status", myBug.get("status"));
		sugar.bugs.listView.verifyField(1, "type", myBug.get("type"));
		sugar.bugs.listView.verifyField(1, "priority", myBug.get("priority"));
		sugar.bugs.listView.verifyField(1, "relAssignedTo", myBug.get("relAssignedTo"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
