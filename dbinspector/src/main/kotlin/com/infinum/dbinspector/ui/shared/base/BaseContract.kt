package com.infinum.dbinspector.ui.shared.base

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.infinum.dbinspector.ui.Presentation

internal abstract class BaseContract<T : BaseContract.Input> : ActivityResultContract<T, Boolean>() {

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
        if (resultCode == Activity.RESULT_OK) {
            intent?.getBooleanExtra(
                Presentation.Constants.Keys.SHOULD_REFRESH,
                false
            ) ?: false
        } else {
            false
        }

    interface Input
}
