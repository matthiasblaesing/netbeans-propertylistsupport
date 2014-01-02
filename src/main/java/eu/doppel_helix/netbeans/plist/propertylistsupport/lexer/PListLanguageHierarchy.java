

package eu.doppel_helix.netbeans.plist.propertylistsupport.lexer;

import java.util.Arrays;
import java.util.Collection;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;


public class PListLanguageHierarchy extends LanguageHierarchy<PListTokenId> {

    @Override
    protected Collection<PListTokenId> createTokenIds() {
        return Arrays.asList(PListTokenId.values());
    }

    @Override
    protected Lexer<PListTokenId> createLexer(LexerRestartInfo<PListTokenId> lri) {
        return new ASCIIPropertyListLexer(lri);
    }

    @Override
    protected String mimeType() {
        return PListLanguage.mimeType;
    }
    
}
