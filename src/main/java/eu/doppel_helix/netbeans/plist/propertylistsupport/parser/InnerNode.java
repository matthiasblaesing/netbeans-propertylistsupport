package eu.doppel_helix.netbeans.plist.propertylistsupport.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InnerNode extends Node {

    private List<Node> children = new ArrayList<>();

    @Override
    public List<Node> getChildren() {
        return Collections.unmodifiableList(children);
    }
    
    public void addChildNode(Node n) {
        children.add(n);
        Collections.sort(children);
    }

    @Override
    public List<LeafNode> getLeafNodes() {
        ArrayList<LeafNode> result = new ArrayList<>();
        for (Node n : getChildren()) {
            result.addAll(n.getLeafNodes());
        }
        return result;
    }

    @Override
    public int getOffset() {
        if(children.isEmpty()) {
            throw new IllegalStateException("getOffset called without child nodes");
        }
        return children.get(0).getOffset();
    }

    @Override
    public int getEnd() {
        if(children.isEmpty()) {
            throw new IllegalStateException("getEnd called without child nodes");
        }
        return children.get(children.size() - 1).getEnd();
    }

}
