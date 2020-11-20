package markassistancesystem.view;

/**
 * IView provides a generic interface for the display of browsable records
 * @author Ziheng Cong
 * @param <T> the type for the record that is to be displayed
 */
public interface IView<T> {
    void displayRecord( T r);
    void displayMessage( String m );
    void setBrowsing( boolean b );
    void displayError( String e );
}

