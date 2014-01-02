
package eu.doppel_helix.netbeans.plist.propertylistsupport.lexer;

import eu.doppel_helix.netbeans.plist.propertylistsupport.parser.PListParser;
import eu.doppel_helix.netbeans.plist.propertylistsupport.structure.PListStructureScanner;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.api.StructureScanner;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;
import org.netbeans.modules.parsing.spi.Parser;

@LanguageRegistration(mimeType = PListLanguage.mimeType)
public class PListLanguage extends DefaultLanguageConfig {

    public static int indent = 2;
    public static final String mimeType = "text/x-plist";
    private final static Language<PListTokenId> language = new PListLanguageHierarchy().language();

    public static Language<PListTokenId> getLanguage() {
        return language;
    }
    
    @Override
    public Language getLexerLanguage() {
        return language;
    }

    @Override
    public String getDisplayName() {
        return "Property List";
    }

    @Override
    public Parser getParser() {
        return new PListParser();
    }

    @Override
    public StructureScanner getStructureScanner() {
        return new PListStructureScanner();
    }

    @Override
    public boolean hasStructureScanner() {
        return true;
    }


    
}
