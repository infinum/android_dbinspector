package im.dino.dbview.exampleapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Calendar;

/**
 * Created by dino on 24/02/14.
 */
@Table(name = "articles")
public class Article extends Model {

    @Column(name = "title", index = true)
    public String title;

    @Column(name = "text")
    public String text;

    @Column(name = "pubblished_at")
    public Calendar publishedAt;

    @Column(name = "user")
    public User user;

}
