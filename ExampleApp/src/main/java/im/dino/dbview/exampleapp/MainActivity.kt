package im.dino.dbview.exampleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import im.dino.dbinspector.DbInspector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        show.setOnClickListener {
            DbInspector.launch(this)
        }
    }
}
