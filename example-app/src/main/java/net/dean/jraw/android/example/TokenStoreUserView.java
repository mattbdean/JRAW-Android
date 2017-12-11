package net.dean.jraw.android.example;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import net.dean.jraw.models.OAuthData;
import net.dean.jraw.models.PersistedAuthData;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * A very simple view group that displays some information about a user and its associated
 * PersistedAuthData instance. Used by MainActivity's RecyclerView.
 */
public class TokenStoreUserView extends ConstraintLayout {
    public TokenStoreUserView(Context context) {
        super(context);
        init();
    }

    public TokenStoreUserView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TokenStoreUserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void display(String username, @NotNull PersistedAuthData data) {
        textView(R.id.username).setText(username);

        OAuthData latest = data.getLatest();
        if (latest != null) {
            // Calculate the amount of minutes in which the OAuthData will expire
            long diffMillis = data.getLatest().getExpiration().getTime() - new Date().getTime();
            long diffMinutes = TimeUnit.MINUTES.convert(diffMillis, TimeUnit.MILLISECONDS);

            // Update the TextView
            textView(R.id.expiresIn).setText(getContext().getString(R.string.access_token_status_short, diffMinutes));
        } else {
            // No OAuthData, it's expired
            textView(R.id.expiresIn).setText(R.string.access_token_expired);
        }

        // Simply set this guy to say if there's a refresh token or not
        textView(R.id.refreshTokenStatus).setText(data.getRefreshToken() == null ? R.string.no_refresh_token : R.string.refresh_token);
    }

    private TextView textView(@IdRes int id) {
        return (TextView) findViewById(id);
    }

    private void init() {
        inflate(getContext(), R.layout.view_tokenstore_row, this);
    }
}
