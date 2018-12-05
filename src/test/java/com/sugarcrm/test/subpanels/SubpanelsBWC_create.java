package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

public class SubpanelsBWC_create extends SugarTest {

	public void setup() throws Exception {
		sugar().campaigns.api.create();
		sugar().targetlists.api.create();
		sugar().login();
	}

	@Test
	public void SubpanelsBWC_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet targetListFS = new FieldSet();
		targetListFS.put("name", sugar().targetlists.getDefaultData().get("targetlistName"));

		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		BWCSubpanel targetListSubpanel = (BWCSubpanel)sugar().campaigns.detailView.subpanels.get(sugar().targetlists.moduleNamePlural);
		targetListSubpanel.scrollIntoViewIfNeeded(false);
		VoodooUtils.focusDefault();
		targetListSubpanel.expandSubpanelActionsMenu();
		targetListSubpanel.subpanelAction("#prospect_list_campaigns_select_button");
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);

		// TODO: VOOD-1072
		new VoodooControl("input", "id", "name_advanced").set(targetListFS.get("name"));
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.waitForReady();

		// TODO: VOOD-1708
		targetListSubpanel.verify(1, targetListFS, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}