package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Menu;

/**
 * Contains data and tasks associated with the Calendar module, such as field
 * data.
 * 
 * @author Alex Nisevich 
 */
public class CalendarModule extends BWCModule {
	protected static CalendarModule module;

	public static CalendarModule getInstance() throws Exception {
		if(module == null) 
			module = new CalendarModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private CalendarModule() throws Exception {
		moduleNameSingular = "Calendar";
		moduleNamePlural = "Calendar";

		// Calendar Module Menu items
		menu = new Menu(this);
		menu.addControl("scheduleMeeting", "a", "css", "[data-module='Calendar'] [data-navbar-menu-item='LNK_NEW_MEETING']");
		menu.addControl("scheduleCall", "a", "css", "[data-module='Calendar'] [data-navbar-menu-item='LNK_NEW_CALL']");
		menu.addControl("createTask", "a", "css", "[data-module='Calendar'] [data-navbar-menu-item='LNK_NEW_TASK']");
		menu.addControl("today", "a", "css", "[data-module='Calendar'] [data-navbar-menu-item='LNK_VIEW_CALENDAR']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Calendar...");
		super.init();
	}
} // CalendarModule
