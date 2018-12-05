package com.sugarcrm.sugar;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.modules.*;
import com.sugarcrm.sugar.views.*;

import java.util.ArrayList;
import java.util.List;

/** 
 * AppModel is a model of the application under test.  It contains tasks and
 * objects representing different screens/views in the application.  Those
 * objects, in turn, contain objects representing controls on each page.
 * @author David Safar <dsafar@sugarcrm.com>
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class AppModel {
	protected static AppModel app;

	public Navbar navbar;
	public Footer footer;
	public LoginScreen loginScreen;
	public Dashboard dashboard;
	public NewUserWizard newUserWizard;
	public Alerts alerts;
	public PreviewPane previewPane;
	public GlobalSearch globalSearch;

	public Sweetspot sweetspot;

	public List<Module> moduleRegistry = new ArrayList<Module>();

	// Standard RecordModules
	public AccountsModule accounts;
	public BugsModule bugs;
	public ContactsModule contacts;
	public OpportunitiesModule opportunities;
	public RevLineItemsModule revLineItems;
	public LeadsModule leads;
	public TargetsModule targets;
	public TargetListsModule targetlists;
	public CasesModule cases;
	public TasksModule tasks;
	public NotesModule notes;
	public HomeModule home;
	public QuotedLineItemsModule quotedLineItems;
	public ProcessDefinitionsModule processDefinitions;
	public ProcessBusinessRulesModule processBusinessRules;
	public ProcessesModule processes;
	public ProcessEmailTemplatesModule processEmailTemplates;
	public KBModule knowledgeBase;
	public TagsModule tags;

	// BWC RecordModules
	public CallsModule calls;
	public ContractsModule contracts;
	public CampaignsModule campaigns;
	public DocumentsModule documents;
	public QuotesModule quotes;
	public MeetingsModule meetings;
	public UsersModule users;
	public InboundEmailModule inboundEmail;
	public ProjectsModule projects;

	// Non-standard modules
	public AdminModule admin;
	public ForecastsModule forecasts;
	public EmailsModule emails;
	public CalendarModule calendar;
	public ReportsModule reports;
	public TeamsModule teams;
	public ProductCatalogModule productCatalog;
	public ProductCategoriesModule productCategories;
	public ManufacturersModule manufacturers; 
	public ProductTypesModule productTypes;
	public StudioModule studio;

	/*
	public CurrencyModule currency;
	public EmployeesModule employee;
	public KBModule knowledgeBase;
	public RolesModule roles;
	public WorkFlowModule workflow;
	 */
	public static AppModel getInstance() {
		if (app == null) app = new AppModel();
		return app;
	}

	private AppModel() {

	}

	public void init() throws Exception {
		// App-wide views
		navbar = Navbar.getInstance();
		footer = Footer.getInstance();
		alerts = Alerts.getInstance();
		previewPane = PreviewPane.getInstance();
		globalSearch = GlobalSearch.getInstance();

		// Standard modules.
		accounts = (AccountsModule)registerModule(AccountsModule.getInstance());
		bugs = (BugsModule)registerModule(BugsModule.getInstance());
		contacts = (ContactsModule)registerModule(ContactsModule.getInstance());		
		opportunities = (OpportunitiesModule)registerModule(OpportunitiesModule.getInstance());
		revLineItems = (RevLineItemsModule)registerModule(RevLineItemsModule.getInstance());
		leads = (LeadsModule)registerModule(LeadsModule.getInstance());
		targets = (TargetsModule)registerModule(TargetsModule.getInstance());
		targetlists = (TargetListsModule)registerModule(TargetListsModule.getInstance());
		cases = (CasesModule)registerModule(CasesModule.getInstance());
		tasks = (TasksModule)registerModule(TasksModule.getInstance());
		notes = (NotesModule)registerModule(NotesModule.getInstance());
		home = (HomeModule)registerModule(HomeModule.getInstance());
		quotedLineItems = (QuotedLineItemsModule)registerModule(QuotedLineItemsModule.getInstance());
		processDefinitions = (ProcessDefinitionsModule)registerModule(ProcessDefinitionsModule.getInstance());
		processBusinessRules = (ProcessBusinessRulesModule)registerModule(ProcessBusinessRulesModule.getInstance());
		processes = (ProcessesModule)registerModule(ProcessesModule.getInstance());
		processEmailTemplates = (ProcessEmailTemplatesModule)registerModule(ProcessEmailTemplatesModule.getInstance());
		knowledgeBase = (KBModule)registerModule(KBModule.getInstance());
		tags = (TagsModule)registerModule(TagsModule.getInstance());

		// BWC modules.
		users = (UsersModule)registerModule(UsersModule.getInstance());
		contracts = (ContractsModule)registerModule(ContractsModule.getInstance());
		campaigns = (CampaignsModule)registerModule(CampaignsModule.getInstance());
		documents = (DocumentsModule)registerModule(DocumentsModule.getInstance());
		quotes = (QuotesModule)registerModule(QuotesModule.getInstance());
		meetings = (MeetingsModule)registerModule(MeetingsModule.getInstance());
		calls = (CallsModule)registerModule(CallsModule.getInstance());
		inboundEmail = (InboundEmailModule)registerModule(InboundEmailModule.getInstance());
		projects = (ProjectsModule)registerModule(ProjectsModule.getInstance());

		// Non-standard modules
		admin = (AdminModule)registerModule(AdminModule.getInstance());
		forecasts = (ForecastsModule)registerModule(ForecastsModule.getInstance());
		emails = (EmailsModule)registerModule(EmailsModule.getInstance());
		calendar = (CalendarModule)registerModule(CalendarModule.getInstance());
		reports = (ReportsModule)registerModule(ReportsModule.getInstance());
		teams = (TeamsModule)registerModule(TeamsModule.getInstance());
		productCatalog = (ProductCatalogModule)registerModule(ProductCatalogModule.getInstance());
		productCategories = (ProductCategoriesModule)registerModule(ProductCategoriesModule.getInstance());
		manufacturers = (ManufacturersModule)registerModule(ManufacturersModule.getInstance());
		productTypes = (ProductTypesModule)registerModule(ProductTypesModule.getInstance());

		// Not implemented.
		/*
		currency = CurrencyModule.getInstance();
		employee = EmployeesModule.getInstance();
		knowledgeBase = KBModule.getInstance();
		roles = RolesModule.getInstance();
		workflow = WorkFlowModule.getInstance();
		 */

		// Run initialization code that depends on other modules already being
		// constructed.
		for(Module module : moduleRegistry) {
			module.init();
		}

		loginScreen = LoginScreen.getInstance();
		newUserWizard = NewUserWizard.getInstance();
		dashboard = Dashboard.getInstance();
		sweetspot = Sweetspot.getInstance();
		studio = StudioModule.getInstance();			
	}

	public Module registerModule(Module toRegister) throws Exception {
		if(!moduleRegistry.contains(toRegister)) {
			moduleRegistry.add(toRegister);
			navbar.addMenu(toRegister, toRegister.getMenu());
		}
		return toRegister;
	}

	/**
	 * Log into SugarCRM as the admin user.
	 * TODO: Adapt this to log in as any user.
	 * @throws Exception
	 */
	public void login() throws Exception {
		loginScreen.login();
	}

	/**
	 * Log into SugarCRM as another user.
	 * 
	 * @param userData FieldSet of data to be used in login.
	 * @throws Exception
	 */
	public void login(FieldSet userData) throws Exception {
		loginScreen.login(userData);
	}

	/**
	 * Log out of SugarCRM.
	 * @throws Exception
	 */
	public void logout() throws Exception {
		navbar.selectUserAction("logout");
		loginScreen.getControl("loginUserName").waitForVisible();
	}

	/**
	 * Get current logged in user name. If name is not available, get user id.
	 * @throws Exception
	 */
	public String getCurrentUser() throws Exception {
		String userName = (String) VoodooUtils.executeJS("return App.user.attributes.full_name");
		String userId = (String) VoodooUtils.executeJS("return App.user.attributes.id");

		return (!userName.isEmpty() ? userName : (!userId.isEmpty() ? userId : "Error"));
	}

}
