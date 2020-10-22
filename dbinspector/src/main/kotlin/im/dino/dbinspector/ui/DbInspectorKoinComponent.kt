package im.dino.dbinspector.ui

import org.koin.core.Koin
import org.koin.core.component.KoinComponent

internal interface DbInspectorKoinComponent : KoinComponent {

    override fun getKoin(): Koin =
        Presentation.koinApplication.koin
}
