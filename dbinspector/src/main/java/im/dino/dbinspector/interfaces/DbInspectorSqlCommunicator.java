package im.dino.dbinspector.interfaces;

/**
 * Created by Zeljko Plesac on 18/04/15.
 */
public interface DbInspectorSqlCommunicator {

    void recordDeleted();

    void recordUpdated();

    void recordInserted();
}
