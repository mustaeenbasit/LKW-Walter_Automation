package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StudioBreadCrumb;
import com.sugarcrm.sugar.views.StudioFooter;
import com.sugarcrm.sugar.views.StudioModuleField;
import com.sugarcrm.sugar.views.StudioModuleLabel;
import com.sugarcrm.sugar.views.StudioModuleLayout;
import com.sugarcrm.sugar.views.StudioModuleMobileLayout;
import com.sugarcrm.sugar.views.StudioModuleRelationship;
import com.sugarcrm.sugar.views.StudioModuleSubpanel;
import com.sugarcrm.sugar.views.StudioTreeView;
import com.sugarcrm.sugar.views.StudioResetView;
import com.sugarcrm.sugar.views.View;

/**
 * Contains tasks and data associated with the Studio module.
 * 
 * @author Amarendra Sinha <asinha@sugarcrm.com>
 */
public class StudioModule extends Module {
	// TODO: VOOD-2054 - Lib support needed to perform actions on pre-defined controls on a BWC view
	//       StudioModule will also extend BwcView
	protected static StudioModule module;
	
	public StudioBreadCrumb breadCrumb;
	public StudioTreeView treeView;
	public StudioResetView resetView;
	public StudioFooter footer;
	
	public StudioModuleLabel labels;
	public StudioModuleField fields;
	public StudioModuleRelationship relationships;
	public StudioModuleLayout layouts;
	public StudioModuleSubpanel subpanels;
	public StudioModuleMobileLayout mobileLayouts;	
	
	public View studioModule = new View();
	
	public static StudioModule getInstance() throws Exception {
		if (module == null)
			module = new StudioModule();
		return module;
	}
	
	private StudioModule() throws Exception {
		moduleNameSingular = "Studio";
		moduleNamePlural = "Studio";
		
		breadCrumb = StudioBreadCrumb.getInstance();
		treeView = StudioTreeView.getInstance();
		resetView = StudioResetView.getInstance();
		footer = StudioFooter.getInstance();
		
		labels = StudioModuleLabel.getInstance();
		fields = StudioModuleField.getInstance();
		relationships = StudioModuleRelationship.getInstance();
		layouts = StudioModuleLayout.getInstance();
		subpanels = StudioModuleSubpanel.getInstance();
		mobileLayouts = StudioModuleMobileLayout.getInstance();
		
		// Studio Module Menu Items
		studioModule.addControl("labels", "a", "css", "#labelsBtn tr:nth-of-type(2) a");
		studioModule.addControl("fields", "a", "css", "#fieldsBtn tr:nth-of-type(2) a");
		studioModule.addControl("relationships", "a", "css", "#relationshipsBtn tr:nth-of-type(2) a");
		studioModule.addControl("layouts", "a", "css", "#layoutsBtn tr:nth-of-type(2) a");
		studioModule.addControl("subpanels", "a", "css", "#subpanelsBtn tr:nth-of-type(2) a");
		studioModule.addControl("mobileLayouts", "a", "css", "#wirelesslayoutsBtn tr:nth-of-type(2) a");
		studioModule.addControl("resetModule", "input", "id", "exportBtn");
	}
	
	/**
	 * Navigate to a module in Studio Module menu page
	 * Leaves you on the module menu page
	 * 
	 * @param module Module
	 * @return StudioModule instance to enable chaining of methods wherever suitable
	 * @throws Exception
	 */
	public StudioModule navToStudioModule(Module module) throws Exception {
		sugar().admin.navToAdminPanelLink("studio");
		clickStudioModule(module.moduleNamePlural);
		return this;
	}
	
	/**
	 * Navigate to a module in Studio Module menu page.
	 * You should be on the main Studio page to call this method. Leaves you on the module menu page. 
	 * 
	 * @param moduleName plural module name
	 * @return StudioModule instance to enable chaining of methods wherever suitable
	 * @throws Exception
	 */
	public StudioModule clickStudioModule(String moduleName) throws Exception {
		String linkID;
		
		// TODO: VOOD-828 - Differentiate display name from Sugar internal name in all modules.
		switch (moduleName) {
			case "Projects":
				linkID = "studiolink_Project";
				break;
			case "Project Tasks":
				linkID = "studiolink_ProjectTask";
				break;
			default:
				linkID = "studiolink_" + moduleName;
		}
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", linkID).click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		return this;
	}
	
	/**
	 * A generic method to navigate to any item in Studio. Item name should NOT be a module name.
	 * Should already be on the page where this menu item is available.
	 * 
	 * @param controlName VoodooControl type must be identical to what defined in addControl.
	 * @return StudioModule instance to enable chaining of methods wherever suitable
	 * @throws Exception
	 */
	public StudioModule clickItemOnPage(VoodooControl controlName) throws Exception {
		// TODO: VOOD-2054 - Lib support needed to perform actions on pre-defined controls on a BWC view
		VoodooUtils.focusFrame("bwc-frame");
		controlName.click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(30000);
		return this;
	}
	
	/**
	 * Click a Save type item which may take some time to execute. getControl() will fetch control from the respective class.
	 * Mostly used in recordView, editView, detailView, quickCreateView
	 * 
	 * @param controlName VoodooControl type must be identical to what defined in addControl.
	 * @return StudioModule instance to enable chaining of methods wherever suitable
	 * @throws Exception
	 */
	public StudioModule clickItem(VoodooControl controlName) throws Exception {
		// TODO: VOOD-2054 - Lib support needed to perform actions on pre-defined controls on a BWC view
		VoodooUtils.focusFrame("bwc-frame");
		controlName.click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(30000);
		return this;
	}
	
	/**
	 * Set value of a VoodooControl to the given value. getControl() will fetch control from the respective class.
	 * Mostly used in recordView, editView, detailView, quickCreateView
	 *  
	 * @param controlName VoodooControl type must be identical to what defined in addControl.
	 * @param toSet String type must be identical to what defined in addControl.
	 * @return StudioModule instance to enable chaining of methods wherever suitable
	 * @throws Exception
	 */
	public StudioModule setItem(VoodooControl controlName, String toSet) throws Exception {
		// TODO: VOOD-2054 - Lib support needed to perform actions on pre-defined controls on a BWC view
		VoodooUtils.focusFrame("bwc-frame");
		controlName.set(toSet);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		return this;
	}
	
	/**
	 * A generic method to move fields from one place to another.
	 * 
	 * @param itemName must be the VoodooControl of the enclosing item
	 * @param toCol must be the VoodooControl control name of the column
	 * @return StudioModule instance to enable chaining of methods wherever suitable
	 * @throws Exception
	 */
	public StudioModule moveItem(VoodooControl itemName, VoodooControl toCol) throws Exception {
		// TODO: VOOD-2054 - Lib support needed to perform actions on pre-defined controls on a BWC view
		VoodooUtils.focusFrame("bwc-frame");
		itemName.dragNDrop(toCol);
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		return this;
	}
	
	/**
	 * A generic method to move fields from one column to another Via dragNDropViaJS
	 * To be used mainly in ListView type views
	 * 
	 * @param itemNameLI must be the li VoodooControl of the enclosing item
	 * @param toColUL must be the VoodooControl ul control name of the column
	 * @return StudioModule instance to enable chaining of methods wherever suitable
	 * @throws Exception
	 */
	public StudioModule moveItemViaJs(VoodooControl itemNameLi, VoodooControl toColUl) throws Exception {
		// TODO: VOOD-2054 - Lib support needed to perform actions on pre-defined controls on a BWC view
		VoodooUtils.focusFrame("bwc-frame");
		itemNameLi.dragNDropViaJS(toColUl);
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		return this;
	}
	
	/**
	 * Resets a Studio module to its default state.
	 * Should already be in the Module Studio page. Returns to Studio main page after completion.
	 * Should be the last method in the chain. No further method chaining is allowed after this method.
	 * 
	 * @throws Exception
	 */
	public void resetModule() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		studioModule.getControl("resetModule").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		resetView.reset();
		footer.clickStudio();
	}

	/**
	 * Resets a Studio module's specific components to its default state.
	 * Should already be in the Module Studio page. Returns to Studio main page after completion.
	 * Should be the last method in the chain. No further method chaining is allowed after this method.
	 * 
	 * @param FieldSet options contains enable/disable key/value options for ALL the checkboxes on the reset view
	 * @throws Exception
	 */
	public void resetModule(FieldSet options) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		studioModule.getControl("resetModule").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		resetView.reset(options);
		footer.clickStudio();
	}
} // StudioModule