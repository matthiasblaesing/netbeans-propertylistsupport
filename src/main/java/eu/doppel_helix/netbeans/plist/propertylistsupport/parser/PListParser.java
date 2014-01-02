

package eu.doppel_helix.netbeans.plist.propertylistsupport.parser;

import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.PListTokenId;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;

public class PListParser extends Parser {
    private PListParserResult result;
    
    @Override
    public void parse(Snapshot snpsht, Task task, SourceModificationEvent sme) throws ParseException {
        TokenSequence ts = snpsht.getTokenHierarchy().tokenSequence();
        List<InnerNode> innerNodes = new ArrayList<>();
        innerNodes.add(new RootNode());
        while(ts.moveNext()) {
            Token t = ts.token();
            LeafNode lf = new LeafNode(t, ts.offset());
            if(t.id() == PListTokenId.ARRAY_BEGIN) {
                ArrayNode an = new ArrayNode();
                an.addChildNode(lf);
                innerNodes.get(innerNodes.size() - 1).addChildNode(an);
                innerNodes.add(an);
            } else if (t.id() == PListTokenId.DICTIONARY_BEGIN) {
                DictionaryNode dn = new DictionaryNode();
                dn.addChildNode(lf);
                innerNodes.get(innerNodes.size() - 1).addChildNode(dn);
                innerNodes.add(dn);
            } else {
                InnerNode in = innerNodes.get(innerNodes.size() - 1);
                in.addChildNode(lf);
                if(t.id() == PListTokenId.DICTIONARY_END 
                        && in instanceof DictionaryNode) {
                    innerNodes.remove(innerNodes.size() - 1);
                } else if(t.id() == PListTokenId.ARRAY_END 
                        && in instanceof ArrayNode) {
                    innerNodes.remove(innerNodes.size() - 1);
                }
            }
        }
        result = new PListParserResult((RootNode) innerNodes.get(0), snpsht);
    }

    @Override
    public Result getResult(Task task) throws ParseException {
        return result;
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
    }
    
}
