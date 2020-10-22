package im.dino.dbinspector.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import im.dino.dbinspector.DbInspector
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        show.setOnClickListener {
            DbInspector.show(this)
        }

        viewModel.copy()
    }
}
