
package eu.doppel_helix.netbeans.plist.propertylistsupport.indent;

import javax.swing.text.BadLocationException;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.IndentTask;

public class PListIndentTask implements IndentTask {
    private final Context c;

    public PListIndentTask(Context c) {
        this.c = c;
    }
    
    @Override
    public void reindent() throws BadLocationException {
        int indentLevel = IndentUtils.findCurrentIndentDepth(
                c.document(), 
                c.startOffset());
        c.modifyIndent(c.startOffset(), indentLevel * 2);
    }

    @Override
    public ExtraLock indentLock() {
        return null;
    }
    
}
