package uk.ac.ebi.fgpt.urigen.action;

import org.protege.editor.owl.ui.UIHelper;
import org.protege.editor.owl.ui.action.ProtegeOWLAction;
import org.protege.editor.owl.ui.rename.RenameEntitiesPanel;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import uk.ac.ebi.fgpt.urigen.panel.FixUriEntitiesPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
/**
 * @author Simon Jupp
 * @date 17/05/2018
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class RenameEntitiesWithUrigenAction extends ProtegeOWLAction {

    public void actionPerformed(ActionEvent event) {
        FixUriEntitiesPanel panel = new FixUriEntitiesPanel(getOWLEditorKit());
        final UIHelper uiHelper = new UIHelper(getOWLEditorKit());
        if (uiHelper.showValidatingDialog("Change multiple entity URIs with Urigen", panel, panel.getFocusComponent()) == JOptionPane.OK_OPTION){
            List<OWLOntologyChange> changes = panel.getChanges();
            getOWLModelManager().applyChanges(changes);
        }
    }


    public void initialise() throws Exception {
        // do nothing
    }


    public void dispose() throws Exception {
        // do nothing
    }
}