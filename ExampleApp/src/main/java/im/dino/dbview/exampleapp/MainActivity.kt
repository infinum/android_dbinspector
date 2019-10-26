package im.dino.dbview.exampleapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.doAsync
import org.threeten.bp.ZonedDateTime

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        doAsync {
            createData()
        }
    }

    @WorkerThread
    private fun createData() {
        val articleDb = ArticleDatabase.getInstance(this)!!

        val userDao = articleDb.userDao()
        val articleDao = articleDb.articleDao()

        articleDao.deleteAll()
        userDao.deleteAll()


        userDao.insert(
            User(
                firstName = "John",
                lastName = "Doe",
                email = "john@example.com",
                nickname = "admin",
                admin = true
            )
        )

        val john = userDao.getByEmail("john@example.com")

        userDao.insert(
            User(
                firstName = "Jane",
                lastName = "Doe",
                email = "jane@example.com",
                nickname = "jane"
            )
        )

        val jane = userDao.getByEmail("jane@example.com")


        (0..50).forEach { i ->
            val a1 = Article(
                title = "John's article #${i + 1}",
                text = "This is John's example article #${i + 1}",
                publishedAt = ZonedDateTime.now(),
                userId = john.id
            )

            articleDao.insert(a1)

            val a2 = Article(
                title = "Jane's article #${i + 1}",
                text = "This is Janes's example article #${i + 1}",
                publishedAt = ZonedDateTime.now(),
                userId = jane.id
            )

            articleDao.insert(a2)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }
}
