package com.sugarcrm.test.projects;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Projects_28099 extends SugarTest {
	VoodooControl moduleCtrl, layoutCtrl, listviewBtnCtrl, historyDefault, saveBtnCtrl;

	public void setup() throws Exception {
		sugar.login();

		// Enable Projects module via Admin -> Display Modules and Subpanels
		sugar.admin.enableModuleDisplayViaJs(sugar.projects);

		// TODO: VOOD-1322
		sugar.alerts.waitForLoadingExpiration(20000);
	}

	/**
	 * Verify that Projects template displayed after changes in list view layout.
	 * @throws Exception
	 */
	@Test
	public void Projects_28099_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Get Date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String startDate = sdf.format(date);

		// Date after One Day
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.DATE, +1);
		date = c1.getTime();
		String endDate = sdf.format(date);

		// Click on Project -> Create Project Template.
		sugar.navbar.selectMenuItem(sugar.projects, "createProjectTemplate");
		VoodooUtils.focusFrame("bwc-frame");
		sugar.projects.editView.getEditField("name").set(testName);
		sugar.projects.editView.getEditField("date_estimated_start").set(startDate);
		sugar.projects.editView.getEditField("date_estimated_end").set(endDate);
		VoodooUtils.focusDefault();
		sugar.projects.editView.save();

		// Click on Project -> View Project Templates. 
		sugar.navbar.selectMenuItem(sugar.projects, "viewProjectsTemplates");
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that all the Template(s) are going to display.
		VoodooControl templatedRecord1 = new VoodooControl("a", "css", ".oddListRowS1 td:nth-child(3) a");
		templatedRecord1.assertEquals(testName, true);
		VoodooUtils.focusDefault();

		// Now Click on Admin > Studio > Projects > Layouts > Listview.
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Project");
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		listviewBtnCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		historyDefault = new VoodooControl("input", "id", "historyDefault");
		saveBtnCtrl = new VoodooControl("input", "id", "savebtn");

		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		listviewBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Add the field Date Created (date_entered) to the default column.
		VoodooControl dropCtrl = new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "#Hidden [data-name='date_entered']").dragNDrop(dropCtrl);

		// Save & Deploy
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Click on Project -> View Projects Template.
		sugar.navbar.selectMenuItem(sugar.projects, "viewProjectsTemplates");
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that all the Template(s) are going to display.
		templatedRecord1.assertEquals(testName, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}