package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25847 extends SugarTest {
	VoodooControl accountsSubPanelCtrl;
	VoodooControl relationshipCtrl, renameModuleCtrl, cancelRenameModuleCtrl;
	DataSource customData = new DataSource();
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify "Change Module Name" button work on studio labels  
	 * @throws Exception
	 */
	@Test
	public void Studio_25847_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		customData = testData.get(testName);
		relationshipCtrl = new VoodooControl("td", "id", "labelsBtn");
		renameModuleCtrl = new VoodooControl("input", "id", "renameModBtn");
		cancelRenameModuleCtrl = new VoodooControl("input", "id", "renameCancelBttn");

		sugar().navbar.navToAdminTools();
		for(int i = 0; i < customData.size(); i++){
			VoodooUtils.focusFrame("bwc-frame");
			// TODO: VOOD-938
			sugar().admin.adminTools.getControl("studio").click(); // studio
			VoodooUtils.waitForReady();
			new VoodooControl("a", "id", customData.get(i).get("moduleName")).click();
			relationshipCtrl.click();
			renameModuleCtrl.click();
			VoodooUtils.waitForReady();
			VoodooUtils.focusDefault();
			VoodooUtils.focusFrame("bwc-frame");
			// Assert check module titlemoduleTitle
			new VoodooControl("h2", "css", ".moduleTitle h2").assertContains(customData.get(i).get("pageHeading"), true);
			
			// Click on cancel button			
			cancelRenameModuleCtrl.click();
			VoodooUtils.focusDefault();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}