package im.dino.dbview.exampleapp;

import com.activeandroid.query.Delete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;

import im.dino.dbview.activities.DbViewActivity;
import im.dino.dbview.exampleapp.models.Article;
import im.dino.dbview.exampleapp.models.User;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createData();

        View showDbButton = findViewById(R.id.button_show_example_database);

        showDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, DbViewActivity.class));
            }
        });
    }

    private void createData() {

        new Delete().from(Article.class).execute();
        new Delete().from(User.class).execute();

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
