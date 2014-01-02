package eu.doppel_helix.netbeans.plist.propertylistsupport.indent;

import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.PListLanguage;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.typinghooks.TypedTextInterceptor;

@MimeRegistration(
        mimeType=PListLanguage.mimeType,
        service=TypedTextInterceptor.Factory.class,
        position = 70)
public class PListTypedTextInterceptorFactory implements TypedTextInterceptor.Factory {

    @Override
    public TypedTextInterceptor createTypedTextInterceptor(MimePath mimePath) {
        return new PListTypedTextInterceptor();
    }
}
