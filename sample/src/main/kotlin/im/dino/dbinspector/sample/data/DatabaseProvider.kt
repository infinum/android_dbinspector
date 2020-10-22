package im.dino.dbinspector.sample.data

interface DatabaseProvider {

    fun names(): List<String>

    fun copy()
}