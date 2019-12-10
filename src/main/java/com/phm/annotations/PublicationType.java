    
package com.phm.annotations;

/**
 *
 * @author phm
 */
public enum PublicationType {
    Journal("Journal"), 
    Book("Book"),
    Manual("Manual"),
    Dissertation("Dissertation"),
    Misc("Misc"), 
    Proceedings("Proceedings"), 
    Techreport("Techreport"),
    Unpublished("Unpublished");
    
    private final String label;
    
    private PublicationType(String lbl) {
        label = lbl;
    }
    @Override
    public String toString () {
        return label;
    }
}