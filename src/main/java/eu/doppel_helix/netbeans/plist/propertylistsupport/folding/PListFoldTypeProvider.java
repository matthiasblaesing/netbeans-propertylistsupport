
package eu.doppel_helix.netbeans.plist.propertylistsupport.folding;

import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.PListLanguage;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.netbeans.api.editor.fold.FoldType;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.editor.mimelookup.MimeRegistrations;
import org.netbeans.spi.editor.fold.FoldTypeProvider;

@MimeRegistrations({
    @MimeRegistration(mimeType = PListLanguage.mimeType,
            service = FoldTypeProvider.class, 
            position = 50)
})
public class PListFoldTypeProvider implements FoldTypeProvider{
    private final static Set<FoldType> foldTypes = new HashSet<FoldType>();
    
    static {
        foldTypes.add(FoldType.COMMENT);
        foldTypes.add(FoldType.CODE_BLOCK);
    }
    
    @Override
    public Collection getValues(Class type) {
        return foldTypes;
    }

    @Override
    public boolean inheritable() {
        return false;
    }
    
}
