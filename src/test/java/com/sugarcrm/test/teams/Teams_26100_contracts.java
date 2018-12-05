package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

public class Teams_26100_contracts extends SugarTest {
	FieldSet customData;
	BWCSubpanel notesSubpanel;
	FieldSet notesSubject = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		notesSubject.put("subject", customData.get("notesSubject"));
		sugar.contracts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
		sugar.alerts.waitForLoadingExpiration(); //Need on jenkins
	}

	/**
	 * Verify that for BWC module-Create record in subpanel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26100_contracts_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to contracts record
		sugar.contracts.navToListView();
		sugar.contracts.listView.clickRecord(1);

		// Choose a subpanel 'Notes' in detail view, and create a record having two teams.
		notesSubpanel = sugar.contracts.detailView.subpanels.get(sugar.notes.moduleNamePlural);

		// TODO: VOOD-972 create() is not implemented for BWCSubpanel
		// notesSubpanel.create(customData);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "History_createnoteorattachment_button_create_0").click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();
		sugar.notes.createDrawer.getEditField("subject").set(customData.get("notesSubject"));

		// TODO: VOOD-518
		// Add Team and save.
		new VoodooControl("button","css", "span.fld_team_name.edit button.btn.first").click();
		new VoodooSelect("div","css", ".fld_team_name.edit div:nth-child(2) div.select2-container.select2.inherit-width").set(sugar.users.getQAUser().get("userName"));
		sugar.notes.createDrawer.save();

		// Verify that record is created.
		notesSubpanel.verify(1, notesSubject, true);
		VoodooUtils.focusDefault();

		// Verify that created record has multiple teams.
		sugar.notes.navToListView();
		sugar.notes.listView.clickRecord(1);
		sugar.notes.recordView.showMore();

		// TODO: VOOD-518, VOOD-1217
		VoodooControl teamNameCtrl = new VoodooControl("span", "css", ".fld_team_name.detail");
		teamNameCtrl.assertContains(customData.get("team1"), true);
		teamNameCtrl.assertContains(sugar.users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
