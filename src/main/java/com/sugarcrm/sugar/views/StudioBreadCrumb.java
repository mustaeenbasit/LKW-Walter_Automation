package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;

public class StudioBreadCrumb extends View {
	protected static StudioBreadCrumb view;
	
	private StudioBreadCrumb() throws Exception {}
	
	public static StudioBreadCrumb getInstance() throws Exception {
		if (view == null)
			view = new StudioBreadCrumb();
		return view;
	}
	
	/**
	 * Navigate to a view in Studio via Bread Crumb menu.
	 * Should already be on some page in Studio and wish to go back to view main menu.
	 * @param viewName plural view name
	 * @return StudioBreadCrumb instance to enable chaining of methods wherever suitable
	 * @throws Exception
	 */
	public StudioBreadCrumb clickModuleInBreadCrumb(Module module) throws Exception {
		String linkText;

		// TODO: VOOD-828 - Differentiate display name from Sugar internal name in all modules.
		switch (module.moduleNamePlural) {
			case "KBContents":
				linkText = "Knowledge Base";
				break;
			case "ProductTemplates":
				linkText = "Product Catalog";
				break;
			case "Project Tasks":
				linkText = "Project Tasks";
				break;
			case "Products":
				linkText = "Quoted Line Items";
				break;
			case "RevenueLineItems":
				linkText = "Revenue Line Items";
				break;
			case "Prospects":
				linkText = "Targets";
				break;
			case "Project":
				linkText = "Projects";
				break;
			default:
				linkText = module.moduleNamePlural;
		}
		clickItemInBreadCrumb(linkText);
		return this;
	}
	
	/**
	 * Navigate to Studio via Bread Crumb menu.
	 * @return StudioBreadCrumb instance to enable chaining of methods wherever suitable
	 * @throws Exception
	 */
	public StudioBreadCrumb clickStudioInBreadCrumb() throws Exception {
		clickItemInBreadCrumb(sugar().studio.moduleNamePlural);
		return this;
	}
	
	/**
	 * Navigate to a, item in Studio via Bread Crumb menu.
	 * Should already be on some page in Studio and wish to go back to view main menu.
	 * @param viewName plural view name
	 * @return StudioBreadCrumb instance to enable chaining of methods wherever suitable
	 * @throws Exception
	 */
	public StudioBreadCrumb clickItemInBreadCrumb(String itemName) throws Exception {
		// TODO: VOOD-2054 - Lib support needed to perform actions on pre-defined controls on a BWC view
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "xpath", "//a[@class='crumbLink'][contains(.,'"+itemName+"')]").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(30000);
		return this;
	}
}