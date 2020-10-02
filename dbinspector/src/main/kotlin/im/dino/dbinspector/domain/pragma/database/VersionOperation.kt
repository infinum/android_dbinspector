package im.dino.dbinspector.domain.pragma.database

import im.dino.dbinspector.domain.shared.AbstractDatabaseOperation.Companion.PRAGMA_USER_VERSION
import im.dino.dbinspector.domain.shared.AbstractPragmaOperation

internal class VersionOperation : AbstractPragmaOperation() {

    override fun query(): String = PRAGMA_USER_VERSION
}
