package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ProcessAuthor_29394 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the a search function should work for Assign to User Dropdown in the Approval/Reject page
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29394_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Import Process definition => activity > Static Assignment, Forms > General ->  mark both checkbox for Change Assigned To User and Select New Process User
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/"+testName+".bpm");

		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Trigger the process by new account record
		sugar().accounts.create();
		sugar().processes.navToListView();
		sugar().processes.myProcessesListView.showProcess(1);
		sugar().accounts.recordView.edit();

		// Verify matched user list should be shown after enter for "Change Assigned to User"
		String qauser = sugar().users.getQAUser().get("userName");
		VoodooSelect assignedUser = (VoodooSelect)sugar().accounts.recordView.getEditField("relAssignedTo");
		assignedUser.set(qauser);
		assignedUser.assertEquals(qauser, true);
		sugar().accounts.recordView.cancel();
		sugar().accounts.recordView.openPrimaryButtonDropdown();

		// Verify matched user list should be shown after enter for "Select New Process User"
		// TODO: VOOD-1706
		new VoodooControl("a", "css", ".detail.fld_duplicate_button").click();
		VoodooUtils.waitForReady();
		VoodooSelect user = new VoodooSelect("div", "css", ".adam-window-body .adam-field .select2-container");
		user.set(qauser);

		// TODO: VOOD-1843 - Once resolved below line should use getChildElement
		new VoodooControl("span", "css", user.getHookString()+" a span.select2-chosen").assertEquals(qauser, true);
		new VoodooControl("a", "css", ".adam-panel-footer .btn-link").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}