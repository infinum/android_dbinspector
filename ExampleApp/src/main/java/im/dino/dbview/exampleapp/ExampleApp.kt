package im.dino.dbview.exampleapp

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

/**
 * Created by dino on 02/06/15.
 */
class ExampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
