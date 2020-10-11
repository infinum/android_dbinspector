package im.dino.dbinspector.data.models.local

class QueryException : IllegalStateException() {

    override val message: String?
        get() = "Cannot perform a query using a closed database connection."
}