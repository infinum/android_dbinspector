package im.dino.dbinspector.ui.shared.bus

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
internal object EventBus {

    private val bus = ConflatedBroadcastChannel<Any>(object {})

    suspend fun send(o: Any) =
        bus.send(o)

    @FlowPreview
    inline fun <reified T> on() =
        bus.asFlow()
            .drop(1)
            .filter { it is T }
            .map { it as T }
}