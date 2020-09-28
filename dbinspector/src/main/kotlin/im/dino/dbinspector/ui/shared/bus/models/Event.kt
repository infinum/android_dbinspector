package im.dino.dbinspector.ui.shared.bus.models

internal sealed class Event {

    class RefreshDatabases

    class RefreshViews

    class RefreshTriggers
}