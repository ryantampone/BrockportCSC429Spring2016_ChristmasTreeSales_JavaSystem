package userinterface;

import java.util.Vector;
import java.util.Properties;
import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class TreeTableModel
{
	private final SimpleStringProperty Barcode;
	private final SimpleStringProperty TreeType;
	private final SimpleStringProperty Notes;
	private final SimpleStringProperty Status;
	private final SimpleStringProperty DateStatusUpdated;

	//----------------------------------------------------------------------------
	public TreeTableModel(Vector<String> TreeData)
	{
		Barcode = new SimpleStringProperty(TreeData.elementAt(0));
		TreeType = new SimpleStringProperty(TreeData.elementAt(1));
		Notes = new SimpleStringProperty(TreeData.elementAt(2));
		Status = new SimpleStringProperty(TreeData.elementAt(3));
		DateStatusUpdated = new SimpleStringProperty(TreeData.elementAt(4));
	}

	public Properties getProperty()
    {
        Properties p = new Properties();
        p.put("Barcode", Barcode.getValue());
        p.put("TreeType", TreeType.getValue());
        p.put("Notes", Notes.getValue());
        p.put("Status", Status.getValue());
		p.put("DateStatusUpdated", DateStatusUpdated.getValue());
        return p;
    }
	
	public String getBarcode() {
		return Barcode.get();
	}

	public String getTreeType() {
		return TreeType.get();
	}
	
	public void setBarcode(String bar) {
		Barcode.set(bar);
	}

	public void setTreeType(String tt) {
		TreeType.set(tt);
	}
	
	public String getNotes() {
		return Notes.get();
	}
	
	public void setNotes(String notes) {
		Notes.set(notes);
	}


	public String getStatus() {
		return Status.get();
	}
	
	public void setStatus(String stat) {
		Status.set(stat);
	}

	public String getDateStatusUpdated() {
		return DateStatusUpdated.get();
	}
	
	public void setDateStatusUpdated(String dsu) {
		DateStatusUpdated.set(dsu);
	}

	
}