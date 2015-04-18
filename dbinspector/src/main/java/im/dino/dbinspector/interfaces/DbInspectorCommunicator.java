package im.dino.dbinspector.interfaces;

/**
 * Created by Zeljko Plesac on 18/04/15.
 */
public interface DbInspectorCommunicator {

    public void recordDeleted();

    public void recordUpdated();

    public void recordInserted();
}
