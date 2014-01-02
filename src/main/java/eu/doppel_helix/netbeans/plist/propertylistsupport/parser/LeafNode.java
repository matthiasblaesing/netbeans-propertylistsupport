
package eu.doppel_helix.netbeans.plist.propertylistsupport.parser;

import java.util.Collections;
import java.util.List;
import org.netbeans.api.lexer.Token;

public class LeafNode extends Node {
    private final Token token;
    private final int offset;
    private final int end;

    public LeafNode(Token token, int offset) {
        this.token = token;
        this.offset = offset;
        this.end = offset + token.length();
    }
    
    @Override
    public List<Node> getChildren() {
        return Collections.EMPTY_LIST;
    }
    
    @Override
    public List<LeafNode> getLeafNodes() {
        return Collections.singletonList(this);
    }

    public int getOffset() {
        return offset;
    }

    public int getEnd() {
        return end;
    }
    
    public Token getToken() {
        return token;
    }
}
