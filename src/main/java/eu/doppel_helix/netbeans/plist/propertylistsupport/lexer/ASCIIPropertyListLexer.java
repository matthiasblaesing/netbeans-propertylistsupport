/*
 * plist - An open source library to parse and generate property lists
 * Copyright (C) 2012 Daniel Dreibrodt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eu.doppel_helix.netbeans.plist.propertylistsupport.lexer;

import org.netbeans.api.lexer.Token;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerInput;
import org.netbeans.spi.lexer.LexerRestartInfo;
import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.PListTokenId.*;

/**
 * Parser for ASCII property lists. Supports Apple OS X/iOS and GnuStep/NeXTSTEP
 * format. This parser is based on the recursive descent paradigm, but the
 * underlying grammar is not explicitely defined.
 * <p/>
 * Resources on ASCII property list format:
 * <ul>
 * <li><a
 * href="https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/PropertyLists/OldStylePlists/OldStylePLists.html>
 * Property List Programming Guide - Old-Style ASCII Property Lists
 * </a></li>
 * <li><a
 * href="http://www.gnustep.org/resources/documentation/Developer/Base/Reference/NSPropertyList.html">
 * GnuStep - NSPropertyListSerialization class documentation
 * </a></li>
 * </ul>
 *
 * @author Daniel Dreibrodt
 */
public class ASCIIPropertyListLexer implements Lexer<PListTokenId> {

    private final LexerRestartInfo lri;
    private State state;
    private Character dataSig1 = null;
    private Character dataSig2 = null;

    public ASCIIPropertyListLexer(LexerRestartInfo<PListTokenId> lri) {
        this.lri = lri;
        this.state = (State) lri.state();
        if (this.state == null) {
            this.state = State.START;
        }
    }

    private enum State {

        START,
        WHITESPACE,
        PRE_COMMENT,
        SINGLELINE_COMMENT,
        MULTILINE_COMMENT,
        MULTILINE_COMMENT_POT_END,
        QUOTED_STRING,
        QUOTED_STRING_ESCAPE,
        STRING,
        DATA,
        ERROR
    }

    public static final char WHITESPACE_SPACE = ' ';
    public static final char WHITESPACE_TAB = '\t';
    public static final char WHITESPACE_NEWLINE = '\n';
    public static final char WHITESPACE_CARRIAGE_RETURN = '\r';

    public static final char ARRAY_BEGIN_TOKEN = '(';
    public static final char ARRAY_END_TOKEN = ')';
    public static final char ARRAY_ITEM_DELIMITER_TOKEN = ',';

    public static final char DICTIONARY_BEGIN_TOKEN = '{';
    public static final char DICTIONARY_END_TOKEN = '}';
    public static final char DICTIONARY_ASSIGN_TOKEN = '=';
    public static final char DICTIONARY_ITEM_DELIMITER_TOKEN = ';';

    public static final char QUOTEDSTRING_BEGIN_TOKEN = '"';
    public static final char QUOTEDSTRING_END_TOKEN = '"';
    public static final char QUOTEDSTRING_ESCAPE_TOKEN = '\\';

    public static final char DATA_BEGIN_TOKEN = '<';
    public static final char DATA_END_TOKEN = '>';

    public static final char DATA_GSOBJECT_BEGIN_TOKEN = '*';
    public static final char DATA_GSDATE_BEGIN_TOKEN = 'D';
    public static final char DATA_GSBOOL_BEGIN_TOKEN = 'B';
    public static final char DATA_GSBOOL_TRUE_TOKEN = 'Y';
    public static final char DATA_GSBOOL_FALSE_TOKEN = 'N';
    public static final char DATA_GSINT_BEGIN_TOKEN = 'I';
    public static final char DATA_GSREAL_BEGIN_TOKEN = 'R';

    public static final char DATE_DATE_FIELD_DELIMITER = '-';
    public static final char DATE_TIME_FIELD_DELIMITER = ':';
    public static final char DATE_GS_DATE_TIME_DELIMITER = ' ';
    public static final char DATE_APPLE_DATE_TIME_DELIMITER = 'T';
    public static final char DATE_APPLE_END_TOKEN = 'Z';

    public static final char COMMENT_BEGIN_TOKEN = '/';
    public static final char MULTILINE_COMMENT_SECOND_TOKEN = '*';
    public static final char SINGLELINE_COMMENT_SECOND_TOKEN = '/';
    public static final char MULTILINE_COMMENT_END_TOKEN = '/';

    @Override
    public Token<PListTokenId> nextToken() {
        for (int cc = lri.input().read(); cc != LexerInput.EOF; cc = lri.input().read()) {
            switch (state) {
                case START:
                    if (cc == WHITESPACE_CARRIAGE_RETURN
                            || cc == WHITESPACE_NEWLINE
                            || cc == WHITESPACE_SPACE
                            || cc == WHITESPACE_TAB) {
                        state = State.WHITESPACE;
                        break;
                    } else if (cc == COMMENT_BEGIN_TOKEN) {
                        state = State.PRE_COMMENT;
                        break;
                    } else if (cc == ARRAY_BEGIN_TOKEN) {
                        return lri.tokenFactory().createToken(PListTokenId.ARRAY_BEGIN);
                    } else if (cc == ARRAY_END_TOKEN) {
                        return lri.tokenFactory().createToken(PListTokenId.ARRAY_END);
                    } else if (cc == ARRAY_ITEM_DELIMITER_TOKEN) {
                        return lri.tokenFactory().createToken(PListTokenId.ARRAY_ITEM_DELIMITER);
                    } else if (cc == DICTIONARY_BEGIN_TOKEN) {
                        return lri.tokenFactory().createToken(PListTokenId.DICTIONARY_BEGIN);
                    } else if (cc == DICTIONARY_END_TOKEN) {
                        return lri.tokenFactory().createToken(PListTokenId.DICTIONARY_END);
                    } else if (cc == DICTIONARY_ITEM_DELIMITER_TOKEN) {
                        return lri.tokenFactory().createToken(PListTokenId.DICTIONARY_ITEM_DELIMITER);
                    } else if (cc == DICTIONARY_ASSIGN_TOKEN) {
                        return lri.tokenFactory().createToken(PListTokenId.EQUAL);
                    } else if (cc == QUOTEDSTRING_BEGIN_TOKEN) {
                        state = State.QUOTED_STRING;
                        break;
                    } else if (cc == DATA_BEGIN_TOKEN) {
                        state = State.DATA;
                        dataSig1 = null;
                        dataSig2 = null;
                        break;
                    } else {
                        state = State.STRING;
                        break;
                    }
                case WHITESPACE:
                    if (cc == WHITESPACE_CARRIAGE_RETURN
                            || cc == WHITESPACE_NEWLINE
                            || cc == WHITESPACE_SPACE
                            || cc == WHITESPACE_TAB) {
                        break;
                    } else {
                        lri.input().backup(1);
                        state = State.START;
                        return lri.tokenFactory().createToken(PListTokenId.WHITESPACE);
                    }
                case PRE_COMMENT:
                    if (cc == MULTILINE_COMMENT_SECOND_TOKEN) {
                        state = State.MULTILINE_COMMENT;
                        break;
                    } else if (cc == SINGLELINE_COMMENT_SECOND_TOKEN) {
                        state = State.SINGLELINE_COMMENT;
                        break;
                    } else {
                        state = State.ERROR;
                        break;
                    }
                case SINGLELINE_COMMENT:
                    if (cc == WHITESPACE_NEWLINE
                            || cc == WHITESPACE_CARRIAGE_RETURN) {
                        lri.input().backup(1);
                        state = State.START;
                        return lri.tokenFactory().createToken(PListTokenId.SINGLELINE_COMMENT);
                    } else {
                        break;
                    }
                case MULTILINE_COMMENT:
                    if (cc == MULTILINE_COMMENT_SECOND_TOKEN) {
                        state = State.MULTILINE_COMMENT_POT_END;
                        break;
                    } else {
                        break;
                    }
                case MULTILINE_COMMENT_POT_END:
                    if (cc == MULTILINE_COMMENT_END_TOKEN) {
                        state = State.START;
                        return lri.tokenFactory().createToken(PListTokenId.MULTILINE_COMMENT);
                    } else {
                        state = State.MULTILINE_COMMENT;
                        break;
                    }
                case QUOTED_STRING:
                    if (cc == QUOTEDSTRING_END_TOKEN) {
                        state = State.START;
                        return lri.tokenFactory().createToken(PListTokenId.QUOTED_STRING);
                    } else if (cc == QUOTEDSTRING_ESCAPE_TOKEN) {
                        state = State.QUOTED_STRING_ESCAPE;
                        break;
                    } else {
                        break;
                    }
                case QUOTED_STRING_ESCAPE:
                    state = State.QUOTED_STRING;
                    break;
                case DATA:
                    if (cc == DATA_END_TOKEN) {
                        PListTokenId type = PListTokenId.DATA;
                        if (dataSig1 != null && dataSig2 != null && dataSig1
                                == '*') {
                            switch (dataSig2) {
                                case DATA_GSBOOL_BEGIN_TOKEN:
                                    type = PListTokenId.BOOLEAN;
                                    break;
                                case DATA_GSDATE_BEGIN_TOKEN:
                                    type = PListTokenId.DATE;
                                    break;
                                case DATA_GSINT_BEGIN_TOKEN:
                                    type = PListTokenId.INTEGER;
                                    break;
                                case DATA_GSREAL_BEGIN_TOKEN:
                                    type = PListTokenId.REAL;
                                    break;
                            }
                        }
                        return lri.tokenFactory().createToken(type);
                    } else {
                        if (dataSig1 == null) {
                            dataSig1 = (char) cc;
                        } else if (dataSig2 == null) {
                            dataSig2 = (char) cc;
                        }
                        break;
                    }
                case STRING:
                    switch (cc) {
                        case WHITESPACE_SPACE:
                        case WHITESPACE_TAB:
                        case WHITESPACE_NEWLINE:
                        case WHITESPACE_CARRIAGE_RETURN:
                        case ARRAY_ITEM_DELIMITER_TOKEN:
                        case DICTIONARY_ITEM_DELIMITER_TOKEN:
                        case DICTIONARY_ASSIGN_TOKEN:
                        case ARRAY_END_TOKEN:
                            lri.input().backup(1);
                            state = State.START;
                            return lri.tokenFactory().createToken(PListTokenId.STRING);
                    }
                    break;
                case ERROR:
                    break;
            }
        }
        if (lri.input().readLength() > 0) {
            switch (state) {
                case WHITESPACE:
                    return lri.tokenFactory().createToken(PListTokenId.WHITESPACE);
                case SINGLELINE_COMMENT:
                    return lri.tokenFactory().createToken(PListTokenId.SINGLELINE_COMMENT);
                case MULTILINE_COMMENT:
                case MULTILINE_COMMENT_POT_END:
                    return lri.tokenFactory().createToken(PListTokenId.MULTILINE_COMMENT);
                case STRING:
                    return lri.tokenFactory().createToken(PListTokenId.STRING);
                case DATA:
                case ERROR:
                case PRE_COMMENT:
                case QUOTED_STRING:
                case QUOTED_STRING_ESCAPE:
                    return lri.tokenFactory().createToken(PListTokenId.ERROR);
            }
        }

        return null;
    }

    @Override
    public Object state() {
        return state;
    }

    @Override
    public void release() {
    }
}
