package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Studio_17184 extends SugarTest {
	DataSource studioRecords;

	public void setup() throws Exception {
		studioRecords = testData.get("Studio_17184");

		sugar().login();
	}

	/**
	 * Verify the Search layouts are present for non-BC modules
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_17184_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// The non-bwc module links are maintained in Studio_17184 CSV
		for (FieldSet studioRecord : studioRecords) {

			sugar().navbar.navToAdminTools();
			VoodooUtils.focusFrame("bwc-frame");
			// TODO VOOD-517 Create Studio Module (BWC) - will provide the
			// references to replace the explicit VoodooControls
			new VoodooControl("a", "id", "studio").click();
			new VoodooControl("a", "id", (studioRecord.get("studioLink")))
					.click();
			new VoodooControl("a", "css", "td#layoutsBtn a").click();
			new VoodooControl("a", "css", "td#searchBtn a").assertExists(true);
			VoodooUtils.focusDefault();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
