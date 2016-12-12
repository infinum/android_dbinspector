package im.dino.dbinspector.helpers.models;

/**
 * Created by Zeljko Plesac on 07/04/15.
 */
public class TableRowModel {

    private TableRowType type;

    private String name;

    private String value;

    private String sqlAction;

    private String condition;

    public TableRowModel(String type, String name) {

        switch (type) {
            case "TEXT":
                this.type = TableRowType.TEXT;
                break;
            case "INTEGER":
                this.type = TableRowType.INTEGER;
                break;
            case "REAL":
                this.type = TableRowType.REAL;
                break;
            default:
                this.type = TableRowType.TEXT;
                break;
        }
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TableRowType getType() {
        return type;
    }

    public void setType(TableRowType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSqlAction() {
        return sqlAction;
    }

    public void setSqlAction(String sqlAction) {
        this.sqlAction = sqlAction;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
