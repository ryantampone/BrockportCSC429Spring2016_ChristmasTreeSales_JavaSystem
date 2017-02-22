package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
		if(viewName.equals("TreeTypeView"))
			return new TreeTypeView(model);
		else if(viewName.equals("TreeTypeCollectionView"))
			return new TreeTypeCollectionView(model);
		else if (viewName.equals("TransactionView"))
			return new TransactionView(model);
		return null;
	}
	
	public static View createView(String viewName, IModel model, String key)
	{
		if(viewName.equals("ScoutView"))
			return new ScoutView(model, key);
		else if(viewName.equals("TreeView"))
			return new TreeView(model, key);
		else if (viewName.equals("TreeCollectionView"))
			return new TreeCollectionView(model, key);
		else if(viewName.equals("ScoutCollectionView"))
			return new ScoutCollectionView(model, key);
		else if(viewName.equals("SessionView"))
			return new SessionView(model, key);
//		else if(viewName.equals("TransactionView"))
//			return new TransactionView(model, key);
		else return null;
	}
}
