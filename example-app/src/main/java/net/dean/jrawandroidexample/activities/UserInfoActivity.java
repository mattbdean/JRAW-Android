package net.dean.jrawandroidexample.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.models.LoggedInAccount;
import net.dean.jrawandroidexample.R;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        new AsyncTask<Void, Void, LoggedInAccount>() {
            @Override
            protected LoggedInAccount doInBackground(Void... params) {
                return AuthenticationManager.get().getRedditClient().me();
            }

            @Override
            protected void onPostExecute(LoggedInAccount data) {
                ((TextView) findViewById(R.id.user_name)).setText("Name: " + data.getFullName());
                ((TextView) findViewById(R.id.user_created)).setText("Created: " + data.getCreatedUtc());
                ((TextView) findViewById(R.id.user_link_karma)).setText("Link karma: " + data.getLinkKarma());
                ((TextView) findViewById(R.id.user_comment_karma)).setText("Comment karma: " + data.getCommentKarma());
                ((TextView) findViewById(R.id.user_has_mail)).setText("Has mail? " + (data.getInboxCount() > 0));
                ((TextView) findViewById(R.id.user_inbox_count)).setText("Inbox count: " + data.getInboxCount());
                ((TextView) findViewById(R.id.user_is_mod)).setText("Is mod? " + data.isMod());
            }
        }.execute();
    }
}
