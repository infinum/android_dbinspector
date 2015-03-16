package im.dino.dbview.exampleapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by dino on 24/02/14.
 */
@Table(name = "users")
public class User extends Model {

    @Column(name = "first_name", index = true)
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @Column(name = "email")
    public String email;

    @Column(name = "admin")
    public boolean admin;

    @Column(name = "nickname")
    public String nickname;

    @Column(name = "col1")
    public String col1;

    @Column(name = "col2")
    public String col2;

    @Column(name = "col3")
    public String col3;

    @Column(name = "col4")
    public String col4;

    @Column(name = "col5")
    public String col5;

    @Column(name = "col6")
    public String col6;

    @Column(name = "col7")
    public String col7;
}
