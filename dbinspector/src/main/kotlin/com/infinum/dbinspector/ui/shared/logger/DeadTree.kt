package com.infinum.dbinspector.ui.shared.logger

import timber.log.Timber

class DeadTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) = Unit
}
