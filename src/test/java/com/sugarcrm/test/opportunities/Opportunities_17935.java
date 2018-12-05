package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
@Features(revenueLineItem = false)
public class Opportunities_17935 extends SugarTest {
	DataSource salesStageDS;

	public void setup() throws Exception {
		salesStageDS = testData.get(testName);
		sugar().login();
	}

	/**
	 * Verify Opportunities fields is displayed in Create view properly
	 * @throws Exception
	 */
	@Test
	public void Opportunities_17935_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();

		// Verify all fields on the view
		// Opp name (required)
		VoodooControl oppName = sugar().opportunities.createDrawer.getEditField("name");
		oppName.assertAttribute("class", "required", true);
		oppName.assertEquals("", true);

		// Account name (required)
		VoodooControl accountName = sugar().opportunities.createDrawer.getEditField("relAccountName");
		accountName.getChildElement("span", "css", ".select2-chosen").assertEquals(salesStageDS.get(0).get("customData"), true);

		// sales stage (dropdown)
		// TODO: VOOD-1359
		VoodooSelect salesStage = new VoodooSelect("span", "css", ".fld_sales_stage.edit");
		salesStage.getChildElement("input", "css", "input[name='sales_stage']").assertAttribute("value", salesStageDS.get(0).get("salesStage"), true);
		new VoodooControl("span", "css", ".fld_sales_stage.edit a").click();
		VoodooSelect options = new VoodooSelect("ul", "css", "#select2-drop ul");
		for (int i = 0; i < salesStageDS.size(); i++) {
			options.assertContains(salesStageDS.get(i).get("salesStage"), true);
		}
		
		// TODO: VOOD-806 - XPATH will eliminate after this story resolved
		// hack 2 close Voodooselect dropdown
		salesStage.selectWidget.getControl("searchBox").set(salesStageDS.get(0).get("salesStage"));
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + salesStageDS.get(0).get("salesStage") + "')]]").click();

		// best (editable)
		VoodooControl bestCase = sugar().opportunities.createDrawer.getEditField("bestCase");
		bestCase.assertEquals("", true);
		Assert.assertTrue("Best case is disabled.", !(bestCase.isDisabled()));

		// likely (editable)
		VoodooControl likelyCase = sugar().opportunities.createDrawer.getEditField("likelyCase");
		likelyCase.assertEquals("", true);
		Assert.assertTrue("Likely case is disabled.", !(likelyCase.isDisabled()));

		// worst (editable)
		VoodooControl worstCase = sugar().opportunities.createDrawer.getEditField("worstCase");
		worstCase.assertEquals("", true);
		Assert.assertTrue("Worst case is disabled.", !(worstCase.isDisabled()));

		// expected date (required)
		sugar().opportunities.createDrawer.getEditField("date_closed").assertAttribute("class", "required", true);

		// TODO: VOOD-1359
		// probability (disabled)
		VoodooControl probability = new VoodooControl("span", "css", ".fld_probability.edit");
		Assert.assertTrue("Probability is enabled.", probability.isDisabled());
		// TODO: VOOD-1477
		probability.getChildElement("input", "css", probability.getHookString()+" input[name=probability]").assertAttribute("value", salesStageDS.get(2).get("customData"), true);

		// Verify fields after show More
		sugar().opportunities.createDrawer.showMore();

		// Type (dropdown)
		VoodooControl type = sugar().opportunities.createDrawer.getEditField("type");
		// TODO: VOOD-1477
		type.getChildElement("span", "css", type.getHookString()+" .select2-chosen").assertEquals(salesStageDS.get(3).get("customData"), true);
		type.click();
		options.assertContains(salesStageDS.get(4).get("customData"), true);
		options.assertContains(salesStageDS.get(5).get("customData"), true);

		// hack 2 close Voodooselect dropdown
		salesStage.selectWidget.getControl("searchBox").set(salesStageDS.get(5).get("customData"));
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + salesStageDS.get(5).get("customData") + "')]]").click();

		// assignedTo (dropdown)
		sugar().opportunities.createDrawer.getEditField("relAssignedTo").getChildElement("span", "css", "span.select2-chosen").assertEquals(salesStageDS.get(6).get("customData"), true);

		// next step
		VoodooControl nextStep = sugar().opportunities.createDrawer.getEditField("nextStep");
		nextStep.assertVisible(true);
		nextStep.assertEquals("", true);

		// description (textArea)
		VoodooControl description = sugar().opportunities.createDrawer.getEditField("description");
		description.assertVisible(true);
		description.assertEquals("", true);

		// Lead source (dropdown)
		sugar().opportunities.createDrawer.getEditField("leadSource").assertAttribute("class", "select2-choice", true);

		// Campaign (dropdown)
		sugar().opportunities.createDrawer.getEditField("relCampaign").assertAttribute("class", "select2-choice", true);

		// Team (dropdown)
		sugar().opportunities.createDrawer.getEditField("relTeam").assertAttribute("class", "select2-choice", true);

		// Date created (read-only)
		// TODO: VOOD-1359
		VoodooControl dateCreated = new VoodooControl("span", "css", ".fld_date_entered_by");
		dateCreated.assertAttribute("class", "edit", false);
		dateCreated.assertEquals(salesStageDS.get(7).get("customData"), true);

		// Date modified (read-only)
		VoodooControl dateModified = new VoodooControl("span", "css", ".fld_date_modified_by");
		dateCreated.assertAttribute("class", "edit", false);
		dateModified.assertEquals(salesStageDS.get(7).get("customData"), true);

		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}