

package eu.doppel_helix.netbeans.plist.propertylistsupport.parser;

import java.util.Collections;
import java.util.List;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.spi.ParserResult;
import org.netbeans.modules.parsing.api.Snapshot;

public class PListParserResult extends ParserResult {
    private final RootNode rootNode;

    public PListParserResult(RootNode rn, Snapshot _snapshot) {
        super(_snapshot);
        this.rootNode = rn;
    }

    public RootNode getRootNode() {
        return rootNode;
    }
    
    @Override
    protected void invalidate() {
        
    }

    @Override
    public List<? extends Error> getDiagnostics() {
        // @todo: Implement Error Reporting
        return Collections.EMPTY_LIST;
    }
    
}
