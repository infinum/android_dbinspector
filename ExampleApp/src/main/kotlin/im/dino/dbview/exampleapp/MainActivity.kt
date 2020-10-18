package im.dino.dbview.exampleapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import im.dino.dbinspector.DbInspector
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        show.setOnClickListener {
            DbInspector.show(this)
        }

        copy()
    }

    private fun copy() {
        content.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        var databasesDir = this.filesDir.path
        databasesDir = databasesDir.substring(0, databasesDir.lastIndexOf("/")) + "/databases"

        File(databasesDir).let {
            if (it.exists().not()) {
                it.mkdir()
            }
        }

        listOf(
            "blog.db",
            "chinook.db",
            "northwind.sqlite"
        )
            .filterNot { File(databasesDir, it).exists() }
            .forEach { filename ->
                assets.open("databases/$filename").use { inputStream ->
                    FileOutputStream(File(databasesDir, filename))
                        .use { outputStream ->
                            outputStream.write(inputStream.readBytes())
                        }
                }
            }

        progressBar.visibility = View.GONE
        content.visibility = View.VISIBLE
    }
}
