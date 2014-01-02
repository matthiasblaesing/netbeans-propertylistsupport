/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.doppel_helix.netbeans.plist.propertylistsupport.braces;

import eu.doppel_helix.netbeans.plist.propertylistsupport.lexer.PListLanguage;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.editor.mimelookup.MimeRegistrations;
import org.netbeans.spi.editor.bracesmatching.BracesMatcherFactory;
import org.netbeans.spi.editor.bracesmatching.MatcherContext;

@MimeRegistrations({
    @MimeRegistration(mimeType = PListLanguage.mimeType,
            service = BracesMatcherFactory.class, position = 50)
})
public class PListBracesMatcherFactory implements BracesMatcherFactory {

    @Override
    public PListBracesMatcher createMatcher(MatcherContext context) {
        return new PListBracesMatcher(context);
    }

}
