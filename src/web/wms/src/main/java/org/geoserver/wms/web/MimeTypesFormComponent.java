/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.extensions.markup.html.form.palette.component.Recorder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.geoserver.web.wicket.SimpleChoiceRenderer;

public class MimeTypesFormComponent extends FormComponentPanel {

    /**
     * the palette
     */
    protected Palette<String> palette;
    protected AjaxCheckBox allMimeTypesCheckBox;

    /**
     * list of behaviors to add, staged before the palette recorder component is created
     */
    List<IBehavior> toAdd = new ArrayList<IBehavior>();

    public MimeTypesFormComponent(String id, IModel<List<String>> model,
            IModel<Collection<String>> choicesModel) {
        super(id, new Model<String>());


        add(palette = new Palette<String>("palette", model, choicesModel,
                new SimpleChoiceRenderer(), 10, false) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Recorder<String> newRecorderComponent() {
                Recorder<String> rec = super.newRecorderComponent();

                // add any behaviors that need to be added
                rec.add(toAdd.toArray(new IBehavior[toAdd.size()]));
                toAdd.clear();
                return rec;
            }

            
             // Override otherwise the header is not i18n'ized            
            @Override
            public Component newSelectedHeader(final String componentId) {
                return new Label(componentId, new ResourceModel(getSelectedHeaderPropertyKey()));
            }

            // Override otherwise the header is not i18n'ized
            @Override
            public Component newAvailableHeader(final String componentId) {
                return new Label(componentId, new ResourceModel(getAvaliableHeaderPropertyKey()));
            }
        });
        palette.setOutputMarkupId(true);
    }


    /**
     * @return the default key, subclasses may override, if "Selected" is not illustrative enough
     */
    protected String getSelectedHeaderPropertyKey() {
        return "MimeTypesFormComponent.selectedHeader";
    }

    /**
     * @return the default key, subclasses may override, if "Available" is not illustrative enough
     */
    protected String getAvaliableHeaderPropertyKey() {
        return "MimeTypesFormComponent.availableHeader";
    }

    @Override
    public Component add(IBehavior... behaviors) {
        if (palette.getRecorderComponent() == null) {
            // stage for them for later
            toAdd.addAll(Arrays.asList(behaviors));
        } else {
            // add them now
            palette.getRecorderComponent().add(behaviors);
        }
        return this;
    }

    public Palette<String> getPalette() {
        return palette;
    }

    public IModel<List<String>> getPaletteModel() {
        return (IModel<List<String>>) palette.getDefaultModel();
    }

    @Override
    public void updateModel() {
        super.updateModel();
        palette.getRecorderComponent().updateModel();
    }
}