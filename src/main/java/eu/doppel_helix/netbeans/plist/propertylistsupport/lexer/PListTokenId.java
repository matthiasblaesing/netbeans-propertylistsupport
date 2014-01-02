package eu.doppel_helix.netbeans.plist.propertylistsupport.lexer;

import org.netbeans.api.lexer.TokenId;

/**
 * Based on ASCIIPropertyListParser.java from com.googlecode.plist.dd-plist
 * 
 * @author matthias
 */
public enum PListTokenId implements TokenId {
    WHITESPACE("WHITESPACE"),
    SINGLELINE_COMMENT("COMMENT"),
    MULTILINE_COMMENT("COMMENT"),
    ARRAY_ITEM_DELIMITER("STRUCTURE"),
    DICTIONARY_ITEM_DELIMITER("STRUCTURE"),
    ARRAY_BEGIN("STRUCTURE"),
    ARRAY_END("STRUCTURE"),
    DICTIONARY_BEGIN("STRUCTURE"),
    DICTIONARY_END("STRUCTURE"),
    EQUAL("OPERATOR"),
    STRING("STRING"),
    QUOTED_STRING("QUOTEDSTRING"),
    BOOLEAN("LITERAL"),
    INTEGER("LITERAL"),
    REAL("LITERAL"),
    DATE("LITERAL"),
    DATA("LITERAL"),
    ERROR("ERROR"),
    EOF("WHITESPACE")
    ;

    private final String primaryCategory;

    private PListTokenId(String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }
    
    @Override
    public String primaryCategory() {
        return primaryCategory;
    }

}
