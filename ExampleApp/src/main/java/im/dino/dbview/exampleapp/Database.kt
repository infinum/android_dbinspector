package im.dino.dbview.exampleapp

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeParseException

@Entity(
    tableName = "articles",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_id")
        )
    ]
)
data class Article(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "title", index = true) var title: String,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "published_at") var publishedAt: ZonedDateTime,
    @ColumnInfo(name = "user_id") var userId: Long
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "first_name", index = true) var firstName: String,
    @ColumnInfo(name = "last_name") var lastName: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "admin") var admin: Boolean = false,
    @ColumnInfo(name = "nickname") var nickname: String? = null,
    @ColumnInfo(name = "col1") var col1: String? = null,
    @ColumnInfo(name = "col2") var col2: String? = null,
    @ColumnInfo(name = "col3") var col3: String? = null,
    @ColumnInfo(name = "col4") var col4: String? = null,
    @ColumnInfo(name = "col5") var col5: String? = null,
    @ColumnInfo(name = "col6") var col6: String? = null,
    @ColumnInfo(name = "col7") var col7: String? = null
)

@Dao
interface ArticleDao {
    @Query("SELECT * from articles")
    fun getAll(): List<Article>

    @Insert(onConflict = REPLACE)
    fun insert(article: Article)

    @Query("DELETE from articles")
    fun deleteAll()
}

@Dao
interface UserDao {
    @Query("SELECT * from users")
    fun getAll(): List<User>

    @Query("SELECT * from users WHERE email = :email")
    fun getByEmail(email: String): User

    @Insert(onConflict = REPLACE)
    fun insert(user: User)

    @Query("DELETE from users")
    fun deleteAll()
}

class RoomConverters {

    @TypeConverter
    fun fromDateTime(value: String?): ZonedDateTime? {
        if (value != null) {
            try {
                return ZonedDateTime.parse(value)
            } catch (e: DateTimeParseException) {
                e.printStackTrace()
            }
            return null
        } else {
            return null
        }
    }

    @TypeConverter
    fun toDatetime(value: ZonedDateTime?): String? {
        return value?.toString()
    }
}

@Database(entities = [User::class, Article::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun articleDao(): ArticleDao

    companion object {
        private var INSTANCE: ArticleDatabase? = null

        fun getInstance(context: Context): ArticleDatabase? {
            if (INSTANCE == null) {
                synchronized(ArticleDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ArticleDatabase::class.java, "articles.db"
                    )
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
