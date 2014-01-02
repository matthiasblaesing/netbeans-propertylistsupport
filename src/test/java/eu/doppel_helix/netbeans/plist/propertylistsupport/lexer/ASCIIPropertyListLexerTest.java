/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.doppel_helix.netbeans.plist.propertylistsupport.lexer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import static junit.framework.Assert.assertEquals;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.PartType;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;

/**
 *
 * @author matthias
 */
public class ASCIIPropertyListLexerTest {

    public ASCIIPropertyListLexerTest() {
    }

//    @org.junit.Test
//    public void testLexerBase() throws  IOException, BadLocationException {
//        FileReader fr = new FileReader("/home/matthias/dbspec.plist");
//        StringWriter sw = new StringWriter();
//        int c;
//        while((c = fr.read()) != -1){
//            sw.write((char) c);
//        }
//        System.out.println(dumpTokens(getTokenSequence(sw.toString())));
//    }

    private static TokenSequence<PListTokenId> getTokenSequence(String input) throws BadLocationException {
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, input, null);
        Language plistlang = new PListLanguage().getLexerLanguage();
        doc.putProperty(Language.class, plistlang);
        TokenHierarchy<?> hi = TokenHierarchy.get(doc);
        doc.readLock();
        TokenSequence<PListTokenId> seq = hi.tokenSequence(plistlang);
        doc.readUnlock();
        seq.moveStart();
        return seq;
    }

    private static CharSequence dumpTokens(TokenSequence<?> seq) {
        seq.moveStart();
        StringBuilder builder = new StringBuilder();
        Token<?> token = null;
        while (seq.moveNext()) {
            if (token != null) {
                builder.append('\n');
            }
            token = seq.token();
            builder.append(token.id());
            PartType part = token.partType();
            if (part != PartType.COMPLETE) {
                builder.append(' ');
                builder.append(token.partType());
            }
            builder.append(' ');
            builder.append('\'');
            builder.append(token.text());
            builder.append('\'');
        }
        return builder;
    }

    private static void assertTokens(TokenSequence<PListTokenId> seq, PListTokenId... ids) {
        if (ids == null) {
            ids = new PListTokenId[0];
        }
        assertEquals("Wrong token count.", ids.length, seq.tokenCount());
        seq.moveNext();
        for (PListTokenId id : ids) {
            assertEquals("Wrong token ID at index " + seq.index(), id, seq.token().id());
            seq.moveNext();
        }
    }
}
