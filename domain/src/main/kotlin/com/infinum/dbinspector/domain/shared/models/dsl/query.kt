package com.infinum.dbinspector.domain.shared.models.dsl

internal fun pragma(initializer: Pragma.() -> Unit): String {
    return Pragma().apply(initializer).build()
}

internal fun select(initializer: Select.() -> Unit): String {
    return Select().apply(initializer).build()
}

internal fun delete(initializer: Delete.() -> Unit): String {
    return Delete().apply(initializer).build()
}

internal fun dropView(initializer: DropView.() -> Unit): String {
    return DropView().apply(initializer).build()
}

internal fun dropTrigger(initializer: DropTrigger.() -> Unit): String {
    return DropTrigger().apply(initializer).build()
}

internal fun changes(): String {
    return Changes().build()
}
