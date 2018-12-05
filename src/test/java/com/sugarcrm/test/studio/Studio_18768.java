package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_18768 extends SugarTest {
	VoodooControl createdByNameCtrl;
	VoodooControl defaultControl;
	VoodooControl descriptionControl;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the filters field metadata is updated while editing a list of fields in the search layouts in studio
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_18768_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-938
		// click on studio link  
		sugar().admin.adminTools.getControl("studio").click();
		// click on Accounts in studio panel 
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		// click on layout
		new VoodooControl("a", "css", "#layoutsBtn tr:nth-child(2) a").click();
		// click on search
		new VoodooControl("a", "css", "#searchBtn tr:nth-child(2) a").click();
		// click on search
		new VoodooControl("a", "css", "#FilterSearchBtn tr:nth-child(2) a").click();
		
		createdByNameCtrl = new VoodooControl("li", "css", "[data-name='created_by_name']");
		defaultControl = new VoodooControl("td", "id", "Default");
		// drag and drop fields from hidden to default 
		createdByNameCtrl.dragNDrop(defaultControl);
		descriptionControl = new VoodooControl("li", "css", "[data-name='description']");
		descriptionControl.dragNDrop(defaultControl);
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// navigate to account list view 
		sugar().accounts.navToListView();
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();
		// TODO: VOOD-1879
		new VoodooControl("a", "css", ".select2-choice.select2-default").click();
		// assert filed name in filter drop down 
		new VoodooControl("div", "id", "select2-drop").assertContains("Created By", true);
		new VoodooControl("div", "id", "select2-drop").assertContains("Description", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}