package com.sugarcrm.sugar.views;

public class StudioModuleRelationship extends View {
	protected static StudioModuleRelationship view;
	
	private StudioModuleRelationship() throws Exception {}
	
	public static StudioModuleRelationship getInstance() throws Exception {
		if (view == null)
			view = new StudioModuleRelationship();
		return view;
	}
}