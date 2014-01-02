
package eu.doppel_helix.netbeans.plist.propertylistsupport.reformat;

import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.PListLanguage;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ReformatTask;

@MimeRegistration(
        mimeType=PListLanguage.mimeType,
        service=ReformatTask.Factory.class,
        position = 80)
public class PListReformatTaskFactory implements ReformatTask.Factory {

    @Override
    public ReformatTask createTask(Context context) {
        return new PListReformatTask(context);
    }
    
}
