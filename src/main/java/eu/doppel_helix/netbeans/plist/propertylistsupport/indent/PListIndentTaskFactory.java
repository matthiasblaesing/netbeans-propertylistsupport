
package eu.doppel_helix.netbeans.plist.propertylistsupport.indent;

import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.PListLanguage;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.IndentTask;

@MimeRegistration(
        mimeType=PListLanguage.mimeType,
        service=IndentTask.Factory.class,
        position = 60)
public class PListIndentTaskFactory implements IndentTask.Factory {

    @Override
    public IndentTask createTask(Context context) {
        return new PListIndentTask(context);
    }
    
}
