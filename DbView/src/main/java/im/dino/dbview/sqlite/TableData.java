package im.dino.dbview.sqlite;

/**
 * Created by dino on 25/02/14.
 */
public class TableData {

    public enum COLUMN_TYPE {
        INTEGER, REAL, TEXT, BLOB, NULL
    }

    // TODO use a cursor so the whole data is not in memory

    // TODO display in list to make it possible to view large databases

    // TODO pagination 100 by 100, next and previous buttons in the actionbar
}
