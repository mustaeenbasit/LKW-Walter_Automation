package com.sugarcrm.test.targets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Targets_21512 extends SugarTest {

	public void setup() throws Exception {
		sugar().targets.api.create();
		FieldSet fs = new FieldSet();
		fs.put("lastName", testName);
		sugar().targets.api.create(fs);
		sugar().login();
	}

	/**
	 * TC 21512 : Verify that targets "Teams" field can be mass updated correctly
	 * @throws Exception
	 */
	@Test
	public void Targets_21512_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Opening massupdate window in list view
		sugar().targets.navToListView();
		sugar().targets.listView.getControl("selectAllCheckbox").click();
		sugar().targets.listView.openActionDropdown();
		sugar().targets.listView.massUpdate();

		// Adding one more team (qauser) to both the records in list view through massupdate
		// TODO: VOOD-1397
		String qaUserName = sugar().users.getQAUser().get("userName");
		new VoodooSelect("div", "css", ".span4.controls.filter-field").set(sugar().teams.moduleNamePlural);
		new VoodooControl("div", "css", ".first.btn").click();
		new VoodooSelect("div", "css", ".inherit-width.select2.select2-container .select2-default.select2-choice").set(qaUserName);
		new VoodooSelect("span", "css", ".fld_update_button.massupdate").click();

		// Verify the newly added team is showing in record view of first target record
		sugar().targets.listView.clickRecord(1);
		sugar().targets.recordView.showMore();
		new VoodooControl("div", "css", ".detail.fld_team_name :nth-child(2)").assertEquals(qaUserName, true);

		// Verify the newly added team is showing in record view of second target record
		sugar().targets.recordView.getControl("chevronRight").click();
		sugar().targets.recordView.showMore();
		new VoodooControl("div", "css", ".detail.fld_team_name :nth-child(2)").assertEquals(qaUserName, true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
