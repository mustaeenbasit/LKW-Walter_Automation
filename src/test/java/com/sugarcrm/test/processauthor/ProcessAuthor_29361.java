package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29361 extends SugarTest {
	UserRecord myUser;

	public void setup() throws Exception {
		sugar().accounts.api.create();

		// Login
		sugar().login();

		// Create custom user
		myUser = (UserRecord) sugar().users.create();

		// Enable Contracts sub-panel
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);

		// Import a Process with: 
		// Target Module - BWC modules, such as Contracts (applies to New Records Only),
		// Activity > Users > Static assignment to Admin and Forms > select "Approve / Reject"
		// and check the option for "Select New Process User" for East Team. End Event
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");

		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
	}

	/**
	 * Verify Route option is available when user select one way in BWC mdoule
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29361_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet processData = testData.get(testName).get(0);

		// Create a new Contract to trigger the process
		sugar().contracts.create();

		// Go to Processes -> Show Process
		sugar().processes.navToListView();
		sugar().processes.listView.editRecord(1); // same controls for the 'Show process'

		// Define controls
		// TODO: VOOD-1706
		VoodooControl approveButton = new VoodooControl("input", "id", "ApproveBtn");
		VoodooControl rejectButton = new VoodooControl("input", "id", "RejectBtn");
		VoodooControl selectNewProcessUser = new VoodooControl("span", "css", ".adam-icon-reassing");
		VoodooSelect selectUser = new VoodooSelect("span", "id", "s2id_adhoc_user");
		VoodooControl selectType = new VoodooControl("select", "id", "adhoc_type");
		VoodooControl saveProcessUserSettings = new VoodooControl("a", "css", ".adam-button.btn-primary");
		VoodooControl cancelBtnCtrl = new VoodooControl("input", "css", "input[name='Cancel']");

		// In approve/reject form, click on the "Select new process user" icon and select "One Way" and assigned to Chris user
		VoodooUtils.focusFrame("bwc-frame");
		selectNewProcessUser.click();
		// TODO: VOOD-629
		selectUser.click();
		selectUser.selectWidget.getControl("searchForMoreLink").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().users.searchSelect.selectRecord(myUser);
		VoodooUtils.focusFrame("bwc-frame");
		selectType.set(processData.get("oneWay"));

		// Save the new process User
		saveProcessUserSettings.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Logout from the Admin user and Log in as Chris
		sugar().logout();
		myUser.login();

		// Go to Processes -> Show Process
		sugar().processes.navToListView();
		sugar().processes.listView.editRecord(1); // same controls for the 'Show process'

		// Verify the Approve/Reject buttons should shown in form
		VoodooUtils.focusFrame("bwc-frame");
		approveButton.assertVisible(true);
		rejectButton.assertVisible(true);
		cancelBtnCtrl.click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}