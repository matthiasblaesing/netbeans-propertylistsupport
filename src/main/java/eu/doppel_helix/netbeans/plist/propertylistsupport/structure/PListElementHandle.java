
package eu.doppel_helix.netbeans.plist.propertylistsupport.structure;

import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.PListLanguage;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import org.netbeans.modules.csl.api.ElementHandle;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.csl.api.Modifier;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.spi.ParserResult;
import org.openide.filesystems.FileObject;

public class PListElementHandle implements ElementHandle {

    private final String name;
    private final FileObject fileObject;
    private final ElementKind kind;
    private final OffsetRange offsetRange;

    public PListElementHandle(String name, FileObject fileObject, ElementKind kind, OffsetRange offsetRange) {
        this.name = name;
        this.fileObject = fileObject;
        this.kind = kind;
        this.offsetRange = offsetRange;
    }
    
    @Override
    public FileObject getFileObject() {
        return fileObject;
    }

    @Override
    public String getMimeType() {
        return PListLanguage.mimeType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIn() {
        return name;
    }

    @Override
    public ElementKind getKind() {
        return kind;
    }

    @Override
    public Set<Modifier> getModifiers() {
        return Collections.EMPTY_SET;
    }

    @Override
    public boolean signatureEquals(ElementHandle handle) {
        return Objects.equals(getName(), handle.getName()) &&
                Objects.equals(getKind(), handle.getKind());
    }

    @Override
    public OffsetRange getOffsetRange(ParserResult result) {
        return offsetRange;
    }
    
}
