package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_18610 extends SugarTest {
	VoodooControl meetingsStudioCtrl,listViewCtrl,layoutCtrl,saveAndDeploy;

	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify the Studio edit of the Meeting List View
	 * @throws Exception
	 */
	@Test 
	public void Meetings_18610_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Admin tools -> studio -> Meetings  
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-542
		meetingsStudioCtrl = new VoodooControl("a", "id", "studiolink_Meetings");
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		listViewCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		saveAndDeploy = new VoodooControl("input", "css", ".list-editor #savebtn");
		meetingsStudioCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		listViewCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		// Adding "description" field from Hidden to Default List
		VoodooControl addToDefaultList = new VoodooControl("td", "css", "#editor-content #Default");
		VoodooControl descriptionFieldCtrl = new VoodooControl("li", "css", "[data-name='description']");
		descriptionFieldCtrl.dragNDrop(addToDefaultList);

		// Adding "relatedTo" Field from Default to Hidden list
		VoodooControl addtoHiddenList = new VoodooControl("td", "id", "Hidden");
		VoodooControl relatedToFieldCtrl = new VoodooControl("li", "css", "[data-name='parent_name']");
		relatedToFieldCtrl.dragNDrop(addtoHiddenList);
		saveAndDeploy.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Verify that relatedTo field is not appearing in the ListView of Meetings module
		sugar().meetings.navToListView();
		sugar().meetings.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("parent_name"))).assertExists(false);

		// Verify that Description field is appearing in the ListView of Meetings module
		// TODO: VOOD-845
		new VoodooControl("th", "css", "[data-fieldname='description']").assertExists(true);

		// Verify that Value in Description field is Read-Only in List View
		sugar().meetings.listView.editRecord(1);

		// TODO: VOOD-1489
		new VoodooControl("span", "css", ".fld_description.list").assertAttribute("class","edit" ,false);
		sugar().meetings.listView.cancelRecord(1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}