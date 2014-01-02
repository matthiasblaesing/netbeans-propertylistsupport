
package eu.doppel_helix.netbeans.plist.propertylistsupport.indent;

import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.LexerUtilities;
import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.PListTokenId;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.Document;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;

public class IndentUtils {    
    public static int findCurrentIndentDepth(Document d, int offset) {
        TokenSequence<PListTokenId> ts = LexerUtilities.getTokenSequence(d, offset);
        // Move to current position in token stream
        ts.moveStart();
        List<TokenId> structure = new ArrayList<TokenId>();
        while(ts.moveNext() && ts.offset() < offset) {
            TokenId tid = ts.token().id();
            if(tid == PListTokenId.ARRAY_BEGIN) {
                structure.add(PListTokenId.ARRAY_BEGIN);
            } else if (tid == PListTokenId.DICTIONARY_BEGIN) {
                structure.add(PListTokenId.DICTIONARY_BEGIN);
            } else if (tid == PListTokenId.ARRAY_END) {
                int lastIndex = structure.size() - 1;
                if(structure.get(lastIndex) == PListTokenId.ARRAY_BEGIN) {
                    structure.remove(lastIndex);
                }
            } else if (tid == PListTokenId.DICTIONARY_END) {
                int lastIndex = structure.size() - 1;
                if(structure.get(lastIndex) == PListTokenId.DICTIONARY_BEGIN) {
                    structure.remove(lastIndex);
                }
            }
        }
        return structure.size();
    }
}
