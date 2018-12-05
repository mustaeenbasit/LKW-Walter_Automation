package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooUtils;

public class StudioFooter extends View {
	protected static StudioFooter view;
	
	private StudioFooter() throws Exception {
		addControl("homeButton", "input", "css", "#footerHTML input[onclick*='home']");
		addControl("studioButton", "input", "css", "#footerHTML input[onclick*='studio']");
		addControl("moduleBuilderButton", "input", "css", "#footerHTML input[onclick*='mb']");
		addControl("portalEditorButton", "input", "css", "#footerHTML input[onclick*='sugarportal']");
		addControl("dropdownEditorButton", "input", "css", "#footerHTML input[onclick*='dropdowns']");
	}
	
	public static StudioFooter getInstance() throws Exception {
		if (view == null)
			view = new StudioFooter();
		return view;
	}
	
	/**
	 * Click the Home button in this view.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used you will be taken the the Home view of Developer Tools.<br>
	 * 
	 * @throws Exception
	 */
	public void clickHome() throws Exception {
		clickItem("homeButton");
	}
	
	/**
	 * Click the Studio button in this view.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used you will be taken the the Studio view of Developer Tools.<br>
	 * 
	 * @throws Exception
	 */
	public void clickStudio() throws Exception {
		clickItem("studioButton");
	}
	
	/**
	 * Click the Portal Editor button in this view.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used you will be taken the the Portal Editor view of Developer Tools.<br>
	 * 
	 * @throws Exception
	 */
	public void clickPortalEditor() throws Exception {
		clickItem("portalEditorButton");
	}
	
	/**
	 * Click the view Builder button in this view.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used you will be taken the the view Builder view of Developer Tools.<br>
	 * 
	 * @throws Exception
	 */
	public void clickModuleBuilder() throws Exception {
		clickItem("moduleBuilderButton");
	}			
	
	/**
	 * Click the Dropdown Editor button in this view.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used you will be taken the the Dropdown Editor view of Developer Tools.<br>
	 * 
	 * @throws Exception
	 */
	public void clickDropdownEditor() throws Exception {
		clickItem("dropdownEditorButton");
	}
	
	/**
	 * Navigate to a Studio page from the Footer menu.
	 * @param controlName must be identical to what defined in addControl.
	 * @throws Exception
	 */
	public StudioFooter clickItem(String controlName) throws Exception {
		// TODO: VOOD-2054 - Lib support needed to perform actions on pre-defined controls on a BWC view
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		getControl(controlName).click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(30000);
		return this;
	}
}
