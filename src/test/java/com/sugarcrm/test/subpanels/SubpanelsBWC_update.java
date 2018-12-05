package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

public class SubpanelsBWC_update extends SugarTest {
	BWCSubpanel targetListSubpanel;

	public void setup() throws Exception {
		sugar().campaigns.api.create();
		sugar().targetlists.api.create();
		sugar().login();

		// TODO: VOOD-444 Once resolved dependencies should be via API
		// Target list associated with Campaigns
		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		targetListSubpanel = (BWCSubpanel)sugar().campaigns.detailView.subpanels.get(sugar().targetlists.moduleNamePlural);
		targetListSubpanel.scrollIntoViewIfNeeded(false);
		VoodooUtils.focusDefault();
		targetListSubpanel.expandSubpanelActionsMenu();
		targetListSubpanel.subpanelAction("#prospect_list_campaigns_select_button");
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);

		// TODO: VOOD-1072
		new VoodooControl("input", "id", "name_advanced").set(sugar().targetlists.getDefaultData().get("targetlistName"));
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.waitForReady();
	}

	@Test
	public void SubpanelsBWC_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooUtils.focusFrame("bwc-frame");
		targetListSubpanel.scrollIntoViewIfNeeded(false);
		targetListSubpanel.getControl(String.format("viewRecordRow%02d", 1)).click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Edit the record via UI
		sugar().targetlists.recordView.edit();

		FieldSet newData = new FieldSet();
		newData.put("name", "Name edited");
		newData.put("description", "Description edited");

		sugar().targetlists.recordView.getEditField("targetlistName").set(newData.get("name"));
		sugar().targetlists.recordView.getEditField("description").set(newData.get("description"));
		sugar().targetlists.recordView.save();

		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);

		// Verify the record was edited. 
		targetListSubpanel.verify(1, newData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}