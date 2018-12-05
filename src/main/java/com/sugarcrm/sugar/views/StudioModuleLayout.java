package com.sugarcrm.sugar.views;

public class StudioModuleLayout extends View {
	protected static StudioModuleLayout view;
	
	public StudioRecordView recordView;
	public StudioEditView editView;
	public StudioDetailView detailView;
	public StudioListView listView;
	public StudioQuickCreateView quickCreate;
	public StudioPopupView popupView;
	public StudioSearchView searchView;
	public StudioConvertLeadView convertLead;
	
	private StudioModuleLayout() throws Exception {
		recordView = StudioRecordView.getInstance();
		editView = StudioEditView.getInstance();
		detailView = StudioDetailView.getInstance();
		listView = StudioListView.getInstance();
		quickCreate = StudioQuickCreateView.getInstance();
		popupView = StudioPopupView.getInstance();
		searchView = StudioSearchView.getInstance();
		convertLead = StudioConvertLeadView.getInstance();
		
		addControl("recordView", "a", "css", "#viewBtnrecordview tr:nth-of-type(2) a");
		addControl("editView", "a", "css", "#viewBtneditview tr:nth-of-type(2) a");
		addControl("detailView", "a", "css", "#viewBtndetailview tr:nth-of-type(2) a");
		addControl("listView", "a", "css", "#viewBtnlistview tr:nth-of-type(2) a");
		addControl("quickCreate", "a", "css", "#viewBtnquickcreate tr:nth-of-type(2) a");
		addControl("popupView", "a", "xpath", "//div[@class='bodywrapper']//div[@id='Buttons']//table/tbody/tr[contains(.,'PopupView')]/td[contains(.,'PopupView')]/a");
		addControl("search", "a", "css", "#searchBtn tr:nth-of-type(2) a");
		addControl("convertLead", "a", "css", "#layoutsBtn tr:nth-of-type(2) a");
	}
	
	public static StudioModuleLayout getInstance() throws Exception {
		if (view == null)
			view = new StudioModuleLayout();
		return view;
	}
}