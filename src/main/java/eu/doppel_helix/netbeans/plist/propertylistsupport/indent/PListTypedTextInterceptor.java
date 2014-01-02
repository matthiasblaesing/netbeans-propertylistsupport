
package eu.doppel_helix.netbeans.plist.propertylistsupport.indent;

import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.PListLanguage;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.spi.editor.typinghooks.TypedTextInterceptor;
import static org.netbeans.modules.editor.indent.api.IndentUtils.lineStartOffset;
import static org.netbeans.modules.editor.indent.api.IndentUtils.createIndentString;

public class PListTypedTextInterceptor implements TypedTextInterceptor {

    @Override
    public boolean beforeInsert(Context cntxt) throws BadLocationException {
        char c = cntxt.getText().charAt(0);
        if(c == ')' || c == '}') {
            AbstractDocument d = (AbstractDocument) cntxt.getDocument();

            int offset = cntxt.getOffset();
            if(offset < PListLanguage.indent) {
                return false;
            }
            
            
            int lineStartOffset = lineStartOffset(d, offset);

            d.readLock();
            int indentLevel = IndentUtils.findCurrentIndentDepth(
                    d,
                    lineStartOffset);
            d.readUnlock();

            if(documentSectionOnlyWhiteSpace(d, lineStartOffset, offset)) {
                String s;
                if(indentLevel > 1) {
                    s = createIndentString(d, (indentLevel - 1) * 2);
                } else {
                    s = "";
                }
                d.insertString(lineStartOffset, s, null);
                d.remove(lineStartOffset + s.length(), offset - lineStartOffset);
            }
        }
        return false;
    }

    private boolean documentSectionOnlyWhiteSpace(Document d, int startOffset, int offset) { 
        try {
            String currentLookback = d.getText(startOffset, offset - startOffset);
            for(int i = currentLookback.length() - 1; i >= 0; i-- ) {
                char c = currentLookback.charAt(i);
                if(! Character.isWhitespace(c)) {
                    return false;
                }
            }
            return true;
        } catch (BadLocationException ex) {
            return false;
        }
    }
    
    @Override
    public void insert(MutableContext mc) throws BadLocationException {
    }

    @Override
    public void afterInsert(Context cntxt) throws BadLocationException {
    }

    @Override
    public void cancelled(Context cntxt) {
    }
    
}
