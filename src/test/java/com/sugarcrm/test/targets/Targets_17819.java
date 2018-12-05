package com.sugarcrm.test.targets;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooSelect;

public class Targets_17819 extends SugarTest {
	TargetRecord myTarget;
	public void setup() throws Exception {
		sugar().login();
		myTarget = (TargetRecord)sugar().targets.api.create();
	}

	/**
	 * Verify that a specific Related Module is selected, the Module Name is the very first Tag in the Filter bar.
	 * @author Eric Yang <eyang@sugarcrm.com>
	 */
	@Test
	public void Targets_17819_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		myTarget.navToRecord();
		VoodooControl related_dropdown = new VoodooControl("span", "css", "span[data-voodoo-name='filter-module-dropdown']");
		related_dropdown.assertContains("Related", true);
		related_dropdown.assertContains("All", true);
		VoodooSelect relatedFilter = new VoodooSelect("a","css", "div.related-filter a");
		relatedFilter.set("Campaign Log");
		related_dropdown.assertContains("Campaign Log", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
