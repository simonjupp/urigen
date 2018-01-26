package uk.ac.ebi.fgpt.urigen.entity;

import org.protege.editor.owl.model.OWLEditorKitHook;
import org.protege.editor.owl.model.entity.CustomOWLEntityFactory;

public class MyEditorKitHook extends OWLEditorKitHook {
    public void initialise()
            throws Exception {
        if (UrigenPreference.getUseUrigen()) {
            getEditorKit().getOWLModelManager().setOWLEntityFactory(new UrigenEntityFactory(getEditorKit().getOWLModelManager()));
        }
        else {
            getEditorKit().getOWLModelManager().setOWLEntityFactory(new CustomOWLEntityFactory(getEditorKit().getOWLModelManager()));
        }
    }

    public void dispose()
            throws Exception {
        if (UrigenPreference.getUseUrigen()) {
            getEditorKit().getOWLModelManager().setOWLEntityFactory(new UrigenEntityFactory(getEditorKit().getOWLModelManager()));
        }
        else {
            getEditorKit().getOWLModelManager().setOWLEntityFactory(new CustomOWLEntityFactory(getEditorKit().getOWLModelManager()));
        }
    }
}

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.entity.MyEditorKitHook
 * JD-Core Version:    0.6.1
 */