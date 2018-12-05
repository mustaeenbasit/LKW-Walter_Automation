package com.sugarcrm.sugar.views;

import java.util.HashMap;

import com.sugarcrm.sugar.*;
import com.sugarcrm.sugar.modules.Module;

/**
 * Models a view in SugarCRM, which VoodooGrimoire defines as a logically discrete area of the page which contains controls the user may interact with.
 * View may be used as-is for generic views, or extended for views where custom functionality is desired (e.g. ListView, RecordView, etc.)  
 * @author David Safar <dsafar@sugarcrm.com>
 */
public class View extends VoodooControl {
	/**
	 * The module which owns the current instance of this view.
	 */
	public Module parentModule = null;
	/**
	 * A map of the controls in this view.  Keys are custom strings, user-defined names for these controls.
	 */
	HashMap<String, VoodooControl> controls;
	
	/**
	 * Initializes the view with no parent module and a default html element.  
	 * @throws Exception 
	 * @deprecated	Use View(String, String, String) instead.
	 */
	public View() throws Exception {
		controls = new HashMap<String, VoodooControl>();
	}
	
	/**
	 * Initializes the view with the specified parent module and a default html element.  
	 * @param parentModuleIn - the module that owns this view, likely passed in using the module's this variable when constructing the view.
	 * @throws Exception 
	 * @deprecated	Use View(Module, String, String, String) instead.
	 */
	public View(Module parentModuleIn) throws Exception {
		controls = new HashMap<String, VoodooControl>();
		parentModule = parentModuleIn;
	}
	
	/**
	 * Initializes the view with no parent module and the specified element.  
	 * @throws Exception 
	 */
	public View(String tagIn, String strategyNameIn, String hookStringIn) throws Exception {
		super(tagIn, strategyNameIn, hookStringIn);
		controls = new HashMap<String, VoodooControl>();
	}
	
	/**
	 * Initializes the view with the specified parent module and the specified element.  
	 * @param parentModuleIn	the module that owns this view, likely passed in using the module's this variable when constructing the view.
	 * @param tagIn	the HTML tag of the element that this view represents
	 * @param strategyNameIn	the strategy to use to identify the element that this view represents
	 * @param hookStringIn	the hook string to use to identify the element that this view represents
	 * @throws Exception 
	 */
	public View(Module parentModuleIn, String tagIn, String strategyNameIn, String hookStringIn) throws Exception {
		super(tagIn, strategyNameIn, hookStringIn);
		controls = new HashMap<String, VoodooControl>();
		parentModule = parentModuleIn;
	}
	
	/**
	 * Adds a new control to the view definition. 
	 * @param controlName - VoodooGrimoire name of the element to create, to be used as its key in the map
	 * @param tag - HTML tag name of the element
	 * @param strategy - type of hook to use to identify the element, typically ID, CSS, XPATH, NAME, CLASS
	 * @param hook - the actual string to  the strategy
	 * @throws Exception 
	 */
	public void addControl(String controlName, String tag, String strategy, String hook) throws Exception {
		controls.put(controlName, new VoodooControl(tag, strategy, hook));
	}
	
	/**
	 * Adds a new select to the view definition. 
	 * @param controlName - VoodooGrimoire name of the element to create, to be used as its key in the map
	 * @param tag - HTML tag name of the element
	 * @param strategy - type of hook to use to identify the element, typically ID, CSS, XPATH, NAME, CLASS
	 * @param hook - the actual string to  the strategy
	 * @throws Exception 
	 */
	public void addSelect(String controlName, String tag, String strategy, String hook) throws Exception {
		controls.put(controlName, new VoodooSelect(tag, strategy, hook));
	}
	
	/**
	 * Returns a reference to the specified control based on the VoodooGrimoire name used as its hash key. 
	 * @param controlName - the VoodooGrimoire name used as the hash key of the element to access 
	 * @return a VoodooControl object representing the requested control.
	 */
	public VoodooControl getControl(String controlName) throws Exception {
		VoodooControl toReturn = controls.get(controlName);  
		
		if(toReturn == null) {
			VoodooUtils.voodoo.log.severe("Control \"" + controlName + "\" not defined in this view!");
			Exception e = new ControlNotDefinedException(controlName);
			throw(e);
		}
			
		return(toReturn);
	}
	
	/**
	 * Removes the reference to the specified control based on the VoodooGrimoire name used as its hash key.
	 * @param controlName - the VoodooGrimoire name used as the hash key of the element to access 
	 * @return boolean to indicate success or failure of the operation
	 */
	public boolean removeControl(String controlName) throws Exception {
		if(controls.get(controlName) != null) {
			controls.remove(controlName);
			VoodooUtils.voodoo.log.info("Control \"" + controlName + "\" removed from this view!");
			return true;
		}
	
		VoodooUtils.voodoo.log.info("Control \"" + controlName + "\" not defined in this view!");
		return false;
	}
	
	/**
	 * Adds a new BWCRelate to the view definition. 
	 * @param controlName - VoodooGrimoire name of the element to create, to be used as its key in the map
	 * @param tag - HTML tag name of the element
	 * @param strategy - type of hook to use to identify the element, typically ID, CSS, XPATH, NAME, CLASS
	 * @param hook - the actual string to  the strategy
	 * @throws Exception
	 */
	public void addBWCRelate(String controlName, String tag, String strategy, String hook) throws Exception {
		controls.put(controlName, new VoodooBWCRelate(tag, strategy, hook));
	}

	/**
	 * Helper method to return a AppModel instance
	 * @return AppModel instance
	 */
	protected static AppModel sugar() {
		return AppModel.getInstance();
	}

	/**
	 * Helper method to return a PortalAppModel instance
	 * @return PortalAppModel instance
	 */
	protected static PortalAppModel portal() {
		return PortalAppModel.getInstance();
	}
}
