
package eu.doppel_helix.netbeans.plist.propertylistsupport.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import org.netbeans.modules.csl.api.ElementHandle;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.csl.api.HtmlFormatter;
import org.netbeans.modules.csl.api.Modifier;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.StructureItem;
import org.openide.filesystems.FileObject;

public class PListStructureItem implements StructureItem {

    private final PListElementHandle handle;
    private final List<PListStructureItem> children = new ArrayList<>();

    public void addChild(PListStructureItem item) {
        children.add(item);
    }
    
    public PListStructureItem(String name, FileObject fileObject, ElementKind kind, OffsetRange or) {
        this.handle = new PListElementHandle(name, fileObject, kind, or);
    }
    
    @Override
    public String getName() {
        return handle.getName();
    }

    @Override
    public String getSortText() {
        return handle.getName().toLowerCase();
    }

    @Override
    public String getHtml(HtmlFormatter formatter) {
        formatter.appendText(handle.getName());
        return formatter.getText();
    }

    @Override
    public ElementHandle getElementHandle() {
        return handle;
    }

    @Override
    public ElementKind getKind() {
        return handle.getKind();
    }

    @Override
    public Set<Modifier> getModifiers() {
        return Collections.EMPTY_SET;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public List<? extends StructureItem> getNestedItems() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public long getPosition() {
        return handle.getOffsetRange(null).getStart();
    }

    @Override
    public long getEndPosition() {
        return handle.getOffsetRange(null).getEnd();
    }

    @Override
    public ImageIcon getCustomIcon() {
        return null;
    }
    
}
