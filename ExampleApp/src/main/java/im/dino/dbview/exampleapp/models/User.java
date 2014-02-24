package im.dino.dbview.exampleapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by dino on 24/02/14.
 */
@Table(name = "users")
public class User extends Model {

    @Column(name = "first_name")
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @Column(name = "email")
    public String email;

    @Column(name = "admin")
    public boolean admin;

    @Column(name = "nickname")
    public String nickname;
}
