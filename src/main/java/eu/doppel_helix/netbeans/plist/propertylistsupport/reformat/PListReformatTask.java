
package eu.doppel_helix.netbeans.plist.propertylistsupport.reformat;

import javax.swing.text.BadLocationException;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.ReformatTask;
import static eu.doppel_helix.netbeans.plist.propertylistsupport.indent.IndentUtils.findCurrentIndentDepth;

public class PListReformatTask implements ReformatTask {

    private final Context ctx;

    public PListReformatTask(Context ctx) {
        this.ctx = ctx;
    }
    
    @Override
    public void reformat() throws BadLocationException {
        int start = ctx.startOffset();
        int end = ctx.endOffset();
        for(int lineOffset = ctx.lineStartOffset(end);
                lineOffset >= start;
                lineOffset = lineOffset > 0 ? ctx.lineStartOffset(lineOffset - 1) : -1) {
            boolean blockEnd = false;
            for(int i = lineOffset; i < ctx.document().getLength(); i++) {
                char c = ctx.document().getText(i, 1).charAt(0);
                if(c == ')' || c == '}') {
                    blockEnd = true;
                } else if ( c == '\n' || c == '\r') {
                    break;
                } else if (! Character.isWhitespace(c)) {
                    break;
                }
            }
            int indentLevel = findCurrentIndentDepth(
                    ctx.document(),
                    lineOffset);
            if(blockEnd && indentLevel > 0) {
                indentLevel--;
            }
            ctx.modifyIndent(lineOffset, indentLevel * 2);
        }
    }

    @Override
    public ExtraLock reformatLock() {
        return null;
    }
    
}
