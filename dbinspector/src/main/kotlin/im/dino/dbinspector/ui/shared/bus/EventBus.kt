package im.dino.dbinspector.ui.shared.bus

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 * Pure Kotlin event bus implementation
 */
@ExperimentalCoroutinesApi
internal object EventBus {

    /**
     * We use a ConflatedBroadcastChannel that can send Any object but on creation sends a dummy object.
     * First dummy object is necessary because each receiver creates a new subscription.
     * Without this dummy object each would first receive an object created before subscription.
     */
    private val bus = ConflatedBroadcastChannel<Any>(object {})

    /**
     * Just sends anything through a channel.
     */
    suspend fun publish(anyObject: Any) =
        bus.send(anyObject)

    /**
     * Receiving on the same channel, converted to Flow
     * Dropping first dummy object.
     * Filtering wanted events only.
     * Mapping to these events at the end.
     */
    @FlowPreview
    inline fun <reified T> receive() =
        bus.asFlow()
            .drop(1)
            .filter { it is T }
            .map { it as T }
}
