package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28739 extends SugarTest {
	VoodooControl studioCtrl, casesCtrl, subpanelCtrl, kbSubpanelCtrl, saveBtnCtrl;

	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Verify that fields are saved when drag/drop at KB sub panel in studio
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28739_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource kbHeaders = testData.get(testName);
		studioCtrl = sugar().admin.adminTools.getControl("studio");

		// Navigate to admin
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate to cases module in studio
		// TODO: VOOD-542 - need lib support for studio
		casesCtrl = new VoodooControl("a", "id", "studiolink_Cases");
		casesCtrl.click();
		VoodooUtils.waitForReady();

		// Subpanel View
		// TODO: VOOD-1511 - Support Studio Module Subpanel's Layout View
		subpanelCtrl = new VoodooControl("td", "id", "subpanelsBtn");
		subpanelCtrl.click();
		VoodooUtils.waitForReady();
		kbSubpanelCtrl = new VoodooControl("a", "css", "#Buttons tr:nth-child(2) td:nth-child(2) tr:nth-child(2) a");
		kbSubpanelCtrl.click(); 
		VoodooUtils.waitForReady();

		// Move "Publish Date" field from "Hidden" to "Default"
		new VoodooControl("li", "css", ".draggable[data-name='active_date']").dragNDrop(new VoodooControl(
				"li", "id", "topslot0"));

		// Save & Deploy
		saveBtnCtrl = new VoodooControl("input", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);

		// Assert that "Publish Date" field stays at Default pane in studio
		new VoodooControl("td", "css", "#Default [data-name='active_date'] td").assertEquals(kbHeaders.get(7).get("kbSubpanelHeader"), true);
		VoodooUtils.focusDefault();

		// Navigate to the Cases module and click the case record to open it to its record view
		sugar().navbar.navToModule(sugar().cases.moduleNamePlural);
		sugar().cases.listView.clickRecord(1);

		// Link KB records via KB subpanel in Cases record view
		StandardSubpanel kbSubpanel = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);
		kbSubpanel.scrollIntoViewIfNeeded(false);

		// TODO: RS-1029 - Non-standard class for addRecord (+ button) and link Existing record link in KB sub panel 
		// of Case record view hinders automation
		// Uncomment line #75 and remove line #76,#77 when RS-1029 gets resolved
		//kbSubpanel.clickLinkExisting();
		new VoodooControl("a", "css", "[data-subpanel-link='kbcontents'] .dropdown-toggle").click();
		new VoodooControl("a", "css", ".fld_select_button.panel-top-for-cases a").click();
		sugar().knowledgeBase.searchSelect.selectRecord(1);
		sugar().knowledgeBase.searchSelect.link();

		// TODO: VOOD-1517 - Investigate verification of Header Column Display Names
		// Assert the KB subpanel Headers
		kbSubpanel.scrollIntoViewIfNeeded(false);
		for(int i=0; i<kbHeaders.size(); i++){
			new VoodooControl("span", "css", "div[data-subpanel-link='kbcontents'] th[data-fieldname='"+ kbHeaders.get(i).get("headerCss") + "'] span").
			assertEquals(kbHeaders.get(i).get("kbSubpanelHeader"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}