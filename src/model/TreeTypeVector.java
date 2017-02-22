package model;

import java.util.Properties;
import java.util.Vector;
import javafx.beans.property.SimpleStringProperty;

public class TreeTypeVector
{
    private final SimpleStringProperty treeTypeId;
    private final SimpleStringProperty description;
    private final SimpleStringProperty cost;
    private final SimpleStringProperty barcodePrefix;

    public TreeTypeVector(Vector<String> treeType)
    {
        treeTypeId =  new SimpleStringProperty(treeType.elementAt(0));
        description =  new SimpleStringProperty(treeType.elementAt(1));
        cost =  new SimpleStringProperty(treeType.elementAt(2));
        barcodePrefix =  new SimpleStringProperty(treeType.elementAt(3));
    }

    public Properties getProperty()
    {
        Properties p = new Properties();
        p.put("Id", treeTypeId.getValue());
        p.put("BarcodePrefix", description.getValue());
        p.put("Cost", cost.getValue());
        p.put("Description", barcodePrefix.getValue());
        return p;
    }

    public String gettreeTypeId() {
        return treeTypeId.get();
    }

    public void settreeTypeId(String id) {
        treeTypeId.set(id);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String descrip) {
        description.set(descrip);
    }

    public String getCost() {
        return cost.get();
    }

    public void setCost(String price) {
        cost.set(price);
    }

    public String getBarcodePrefix() {
        return barcodePrefix.get();
    }

    public void setBarcodePrefix(String bb) {
        barcodePrefix.set(bb);
    }
}