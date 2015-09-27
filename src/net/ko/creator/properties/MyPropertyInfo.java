package net.ko.creator.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class MyPropertyInfo implements IAdaptable, IPropertySource{
    List<String> ids=new ArrayList<String>();
    Map<String, String> idToLabelMap=new HashMap<String, String>();
    Map<String, String> idToCategoryMap=new HashMap<String, String>();
    Map<String, String> idToValueMap=new HashMap<String, String>();

    public MyPropertyInfo(Object object) {
      String id="type";
      ids.add(id);
      idToCategoryMap.put(id, "ObjectInfo");
      idToLabelMap.put(id, "type of the current object");
      idToValueMap.put(id, object.getClass().getSimpleName());

      id="someID";
      ids.add(id);
      idToLabelMap.put(id, "some String");
      idToValueMap.put(id, ""+this.hashCode());
    }

    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class adapter) {
      if(adapter.equals(IPropertySource.class)){
        return this;
      }
      return null;
    }

    public boolean isPropertySet(Object id) {
      return false;
    }

    public Object getPropertyValue(Object id) {
      return idToValueMap.get(id);
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
      IPropertyDescriptor[] descs=new IPropertyDescriptor[ids.size()];
      for (int i=0;i<ids.size();i++) {
        final String id=ids.get(i);
        descs[i]=new PropertyDescriptor(id, idToLabelMap.get(id)){
          @Override
          public String getCategory() {
            return idToCategoryMap.get(id);
          };
        };
      }
      return descs;
    }

    public Object getEditableValue() {
      return null;
    }
    public void setPropertyValue(Object id, Object value) {}
    public void resetPropertyValue(Object id) {}
  }
