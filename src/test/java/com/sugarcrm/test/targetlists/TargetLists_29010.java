package com.sugarcrm.test.targetlists;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class TargetLists_29010 extends SugarTest{
	DataSource userNameAndTargetData;

	public void setup() throws Exception {
		userNameAndTargetData = testData.get(testName);
		DataSource targetListName = new DataSource();
		for(int i = 0; i < userNameAndTargetData.size(); i++) {
			targetListName.add(userNameAndTargetData.get(i));
		}

		// Create 3 target list
		sugar.targetlists.api.create(targetListName);

		// Login as Admin user
		sugar.login();

		// Assigned Target Lists to different users
		// TODO: VOOD-444
		for(int i = 1; i < userNameAndTargetData.size() ; i++) {
			sugar.targetlists.navToListView();
			sugar.targetlists.listView.clickRecord(i);
			sugar.targetlists.recordView.edit();
			sugar.targetlists.recordView.getEditField("assignedTo").set(userNameAndTargetData.get(i-1).get("assignedTo"));
			sugar.targetlists.recordView.save();
		}
	}

	/**
	 * Verify "user" column is sortable in the Target List module's List view
	 * 
	 * @throws Exception
	 */
	@Test
	public void TargetLists_29010_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Target List module's list view. Observe the "user" column which is available in the list view
		sugar.targetlists.navToListView();

		// Verify that the "User" column should be sortable
		sugar.targetlists.listView.sortBy("headerAssignedusername", false);
		sugar.targetlists.listView.verifyField(1, "assignedTo", userNameAndTargetData.get(1).get("assignedTo"));
		sugar.targetlists.listView.verifyField(2, "assignedTo", userNameAndTargetData.get(2).get("assignedTo"));
		sugar.targetlists.listView.verifyField(3, "assignedTo", userNameAndTargetData.get(0).get("assignedTo"));

		// Verify that the "User" column should be sortable
		sugar.targetlists.listView.sortBy("headerAssignedusername", true);
		sugar.targetlists.listView.verifyField(1, "assignedTo", userNameAndTargetData.get(0).get("assignedTo"));
		sugar.targetlists.listView.verifyField(2, "assignedTo", userNameAndTargetData.get(2).get("assignedTo"));
		sugar.targetlists.listView.verifyField(3, "assignedTo", userNameAndTargetData.get(1).get("assignedTo"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}