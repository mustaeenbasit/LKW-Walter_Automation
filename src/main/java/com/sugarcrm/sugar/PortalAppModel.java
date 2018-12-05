package com.sugarcrm.sugar;

import java.util.ArrayList;
import java.util.List;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.modules.BugsModule;
import com.sugarcrm.sugar.modules.CasesModule;
import com.sugarcrm.sugar.modules.KBModule;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.sugar.views.Alerts;
import com.sugarcrm.sugar.views.Footer;
import com.sugarcrm.sugar.views.PortalLoginScreen;
import com.sugarcrm.sugar.views.PortalNavbar;
import com.sugarcrm.sugar.views.PortalSignUpScreen;

/** 
 * PortalAppModel is a model of the portal application under test.  It contains tasks and
 * objects representing different screens/views in the portal application.  Those
 * objects, in turn, contain objects representing controls on each page.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class PortalAppModel {
	protected static PortalAppModel portalApp;
	
	// TODO: Portal Classes to be added in later stories.
	public PortalNavbar navbar;
	public PortalLoginScreen loginScreen;
	public PortalSignUpScreen signupScreen;
//	public PortalDashboard dashboard;
	public Alerts alerts;
	public Footer footer;

	public List<Module> moduleRegistry = new ArrayList<Module>();

	// Standard RecordModules
	public BugsModule bugs;
	public CasesModule cases;
	public KBModule knowledgeBase; 
	
	public static PortalAppModel getInstance() {
		if (portalApp == null) portalApp = new PortalAppModel();
		return portalApp;
	}

	/**
	* This constructor is included to make this constructor private and not accessible
	* to other classes.
	* TODO: Portal Constructor and init() to be added later if needed.
	* @throws Exception
	*/
	private PortalAppModel() {
	
	}

	/**
	* This init method may be used for instantiating dependent module/controls etc.
	* that are required after other processes have already occured.
	* @throws Exception
	*/
	public void init() throws Exception {
		// App-wide views
		navbar = PortalNavbar.getInstance();
		
		// Standard modules.
		bugs = (BugsModule)registerModule(BugsModule.getInstance());
		cases = (CasesModule)registerModule(CasesModule.getInstance());
		knowledgeBase = (KBModule)registerModule(KBModule.getInstance());
				
		// Run initialization code that depends on other modules already being
		// constructed.
		for(Module module : moduleRegistry) {
			module.init();
		}

		alerts = Alerts.getInstance();
		navbar = PortalNavbar.getInstance();
		loginScreen = PortalLoginScreen.getInstance();
		signupScreen = PortalSignUpScreen.getInstance();
	}
	
	public Module registerModule(Module toRegister) throws Exception {
		if(!moduleRegistry.contains(toRegister)) {
			moduleRegistry.add(toRegister);
			// TODO: VOOD-1093 -- Portal Support for Module Menu
			navbar.addMenu(toRegister, toRegister.getMenu());
		}
		return toRegister;
	}
	
	/**
	 * Log into Portal as a specified user.
	 * <p>
	 * Can only be used to log into Portal.<br>
	 * User data must be for an active portal user.<br>
	 * 
	 * @param userData FieldSet of data to be used in login.
	 * @throws Exception
	 */
	public void login(FieldSet userData) throws Exception {
		loginScreen.login(userData);
	}

	/**
	 * Log out of Portal.
	 * @throws Exception
	 */
	public void logout() throws Exception {
		navbar.selectUserAction("logout");
		loginScreen.getControl("loginUserName").waitForVisible();
	}
}
