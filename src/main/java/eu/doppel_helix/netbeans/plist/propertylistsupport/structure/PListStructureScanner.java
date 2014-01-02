package eu.doppel_helix.netbeans.plist.propertylistsupport.structure;

import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.LexerUtilities;
import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.PListTokenId;
import eu.doppel_helix.netbeans.plist.propertylistsupport.parser.ArrayNode;
import eu.doppel_helix.netbeans.plist.propertylistsupport.parser.DictionaryNode;
import eu.doppel_helix.netbeans.plist.propertylistsupport.parser.LeafNode;
import eu.doppel_helix.netbeans.plist.propertylistsupport.parser.Node;
import eu.doppel_helix.netbeans.plist.propertylistsupport.parser.PListParserResult;
import eu.doppel_helix.netbeans.plist.propertylistsupport.parser.RootNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.api.editor.fold.FoldType;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.StructureItem;
import org.netbeans.modules.csl.api.StructureScanner;
import org.netbeans.modules.csl.spi.ParserResult;

public class PListStructureScanner implements StructureScanner {

    @Override
    public List<? extends StructureItem> scan(ParserResult info) {
        RootNode rn = ((PListParserResult) info).getRootNode();
        List<StructureItem> result = new ArrayList<>();
        PListStructureItem rootStructure = new PListStructureItem(
                "Root",
                info.getSnapshot().getSource().getFileObject(),
                ElementKind.FILE,
                new OffsetRange(rn.getOffset(), rn.getEnd()));
        recusiveScanForStructureItems(rn, rootStructure);
        result.add(rootStructure);
        return result;
    }

    private void recusiveScanForStructureItems(Node n, PListStructureItem plsi) {
        if (n != null) {
            for (Node childNode : n.getChildren()) {
                if (childNode instanceof ArrayNode
                        || childNode instanceof DictionaryNode) {
                    String name = "[Array]";
                    if (childNode instanceof DictionaryNode) {
                        name = "[Dictionary]";
                    }
                    
                    String addName = getStructureName(n, childNode);
                    
                    if(addName != null) {
                        name  = addName + " " + name;
                    }
                    
                    PListStructureItem childStructure = new PListStructureItem(
                            name,
                            plsi.getElementHandle().getFileObject(),
                            ElementKind.CLASS,
                            new OffsetRange(childNode.getOffset(), childNode.getEnd()));
                    plsi.addChild(childStructure);
                    recusiveScanForStructureItems(childNode, childStructure);
                }
            }
        }
    }
    
    private String getStructureName(Node parent, Node child) {
        String result = null;
        List<Node> nodes = parent.getChildren();
        int index = 0;
        boolean found = false;
        for(; index < nodes.size(); index++) {
            if(nodes.get(index) == child) {
                found = true;
                break;
            }
        }
        if(found) {
            boolean equalRead = false;
            for(int i = index - 1; i >= 0; i--) {
                if(nodes.get(i) instanceof LeafNode) {
                    LeafNode lf = (LeafNode) nodes.get(i);
                    TokenId tid = lf.getToken().id();
                    if(equalRead) {
                        if(tid == PListTokenId.STRING) {
                            return lf.getToken().text().toString();
                        } else if(tid == PListTokenId.QUOTED_STRING) {
                            return LexerUtilities.parseQuotedString(lf.getToken().text().toString());
                        } else if ( tid != PListTokenId.WHITESPACE ) {
                            break;
                        }
                    } else {
                        if (tid == PListTokenId.EQUAL) {
                            equalRead = true;
                        } else if ( tid != PListTokenId.WHITESPACE ) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public Map<String, List<OffsetRange>> folds(ParserResult info) {
        RootNode rn = ((PListParserResult) info).getRootNode();
        Map<String, List<OffsetRange>> result = new HashMap<>();
        result.put(FoldType.COMMENT.code(), new ArrayList<OffsetRange>());
        result.put(FoldType.CODE_BLOCK.code(), new ArrayList<OffsetRange>());
        recusiveScanForFolds(rn, result);
        return result;
    }

    private void recusiveScanForFolds(Node n, Map<String, List<OffsetRange>> result) {
        if (n != null) {
            if (n instanceof LeafNode) {
                if (((LeafNode) n).getToken().id()
                        == PListTokenId.MULTILINE_COMMENT) {
                    result.get(FoldType.COMMENT.code()).add(
                            new OffsetRange(n.getOffset(), n.getEnd()));
                }
            } else if (n instanceof ArrayNode || n instanceof DictionaryNode) {
                result.get(FoldType.CODE_BLOCK.code()).add(
                        new OffsetRange(n.getOffset(), n.getEnd()));

            }
            for (Node childNode : n.getChildren()) {
                recusiveScanForFolds(childNode, result);
            }
        }
    }

    @Override
    public Configuration getConfiguration() {
        return new Configuration(true, false);
    }

}
