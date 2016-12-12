package im.dino.dbview.exampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

import im.dino.dbview.exampleapp.models.Article;
import im.dino.dbview.exampleapp.models.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createData();
    }

    private void createData() {
        User admin = new User();
        admin.firstName = "John";
        admin.lastName = "Doe";
        admin.email = "admin@example.com";
        admin.nickname = "admin";
        admin.admin = true;
        admin.save();

        User jane = new User();
        jane.firstName = "Jane";
        jane.lastName = "Doe";
        jane.email = "jane@example.com";
        jane.nickname = "jane";
        jane.admin = false;
        jane.save();

        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < 50; i++) {
            Article first = new Article();
            first.title = "First post!";
            first.text = "This is the first post of the example app";
            first.publishedAt = cal;
            first.user = admin;
            first.save();

            Article second = new Article();
            second.title = "Second";
            second.text = "Second article text";
            second.publishedAt = cal;
            second.user = jane;
            second.save();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
