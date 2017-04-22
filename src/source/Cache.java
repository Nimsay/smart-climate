package source;



import dao.Table;

/**
 * toutes classes qui sera implémentées à partir de cette interface
 * implémentera obligatoirement les méthodes défini dans cette dérnière
 */
public interface Cache {

    boolean exists(String year, String month);
    boolean complete(String year, String month);
    Table load(String year, String month);

}
