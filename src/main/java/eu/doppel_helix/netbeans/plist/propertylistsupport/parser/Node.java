
package eu.doppel_helix.netbeans.plist.propertylistsupport.parser;

import java.util.List;

public abstract class Node implements Comparable<Node> {
    abstract public List<Node> getChildren();
    abstract public List<LeafNode> getLeafNodes();
    abstract public int getOffset();
    abstract public int getEnd();
    
    @Override
    public int compareTo(Node o) {
        return this.getOffset() - o.getOffset();
    }
}
