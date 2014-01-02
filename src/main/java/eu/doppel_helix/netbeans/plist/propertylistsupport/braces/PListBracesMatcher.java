package eu.doppel_helix.netbeans.plist.propertylistsupport.braces;

import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.LexerUtilities;
import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.PListTokenId;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.spi.editor.bracesmatching.BracesMatcher;
import org.netbeans.spi.editor.bracesmatching.MatcherContext;

public class PListBracesMatcher implements BracesMatcher {

    private final MatcherContext context;

    public PListBracesMatcher(MatcherContext context) {
        this.context = context;
    }

    @Override
    public int[] findOrigin() throws InterruptedException, BadLocationException {
        int[] ret = null;
        ((AbstractDocument) context.getDocument()).readLock();
        try {
            BaseDocument doc = (BaseDocument) context.getDocument();
            int offset = context.getSearchOffset();
            TokenSequence<? extends PListTokenId> ts = LexerUtilities.getTokenSequence(doc, offset);

            if (ts != null) {
                ts.move(offset);

                if (ts.moveNext()) {

                    Token<? extends PListTokenId> token = ts.token();

                    if (token != null) {
                        TokenId id = token.id();

                        if (id == PListTokenId.ARRAY_BEGIN 
                                || id == PListTokenId.ARRAY_END
                                || id == PListTokenId.DICTIONARY_BEGIN
                                || id == PListTokenId.DICTIONARY_END) {
                            ret = new int[]{ts.offset(), ts.offset()
                                + token.length()};
                        } else if (id == PListTokenId.QUOTED_STRING
                                || id == PListTokenId.DATA
                                || id == PListTokenId.BOOLEAN
                                || id == PListTokenId.DATE
                                || id == PListTokenId.INTEGER
                                || id == PListTokenId.REAL) {
                            int startOffset = ts.offset();
                            int endOffset = ts.offset() + token.length() - 1;
                            if (offset == startOffset) {
                                ret = new int[]{startOffset, startOffset + 1};
                            } else if (offset == endOffset) {
                                ret = new int[]{endOffset, endOffset + 1};
                            }
                        }
                    }
                }
            }

        } finally {
            ((AbstractDocument) context.getDocument()).readUnlock();
        }
        return ret;
    }

    @Override
    public int[] findMatches() throws InterruptedException, BadLocationException {
        int[] ret = null;
        ((AbstractDocument) context.getDocument()).readLock();
        try {
            BaseDocument doc = (BaseDocument) context.getDocument();
            int offset = context.getSearchOffset();
            TokenSequence<? extends PListTokenId> ts = LexerUtilities.getTokenSequence(doc, offset);

            if (ts != null) {
                ts.move(offset);

                if (ts.moveNext()) {

                    Token<? extends PListTokenId> token = ts.token();

                    if (token != null) {
                        TokenId id = token.id();

                        if (id == PListTokenId.ARRAY_BEGIN) {
                            OffsetRange r = LexerUtilities.findFwd(ts, PListTokenId.ARRAY_BEGIN.ordinal(),
                                    PListTokenId.ARRAY_END.ordinal());
                            ret = new int[]{r.getStart(), r.getEnd()};
                        } else if (id == PListTokenId.ARRAY_END) {
                            OffsetRange r = LexerUtilities.findBwd(ts, PListTokenId.ARRAY_BEGIN.ordinal(),
                                    PListTokenId.ARRAY_END.ordinal());
                            ret = new int[]{r.getStart(), r.getEnd()};
                        } else if (id == PListTokenId.DICTIONARY_BEGIN) {
                            OffsetRange r = LexerUtilities.findFwd(ts, PListTokenId.DICTIONARY_BEGIN.ordinal(),
                                    PListTokenId.DICTIONARY_END.ordinal());
                            ret = new int[]{r.getStart(), r.getEnd()};
                        } else if (id == PListTokenId.DICTIONARY_END) {
                            OffsetRange r = LexerUtilities.findBwd(ts, PListTokenId.DICTIONARY_BEGIN.ordinal(),
                                    PListTokenId.DICTIONARY_END.ordinal());
                            ret = new int[]{r.getStart(), r.getEnd()};
                        } else if (id == PListTokenId.QUOTED_STRING
                                || id == PListTokenId.DATA
                                || id == PListTokenId.BOOLEAN
                                || id == PListTokenId.DATE
                                || id == PListTokenId.INTEGER
                                || id == PListTokenId.REAL) {
                            int startOffset = ts.offset();
                            int endOffset = ts.offset() + token.length() - 1;

                            if (offset == startOffset) {
                                ret = new int[]{endOffset, endOffset + 1};
                            } else if (offset == endOffset) {
                                ret = new int[]{startOffset, startOffset + 1};
                            }
                        }
                    }
                }
            }
        } finally {
            ((AbstractDocument) context.getDocument()).readUnlock();
        }
        return ret;
    }
}
