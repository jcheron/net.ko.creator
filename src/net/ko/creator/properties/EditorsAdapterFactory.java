package net.ko.creator.properties;

import net.ko.bean.ZTypedRegion;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.ui.views.properties.IPropertySource;

public class EditorsAdapterFactory implements IAdapterFactory {

    private static final Class[] SUPPORTED_ADAPTERS = { IPropertySource.class };

    @Override
    public Object getAdapter(Object adaptableObject, Class adapterType) {
            if  (IPropertySource.class.equals(adapterType)) {
                    return new ZoneElementProperties((ZTypedRegion) adaptableObject);
            }
            return null;
    }

    @Override
    public Class[] getAdapterList() {
            return SUPPORTED_ADAPTERS;
    }

}
