package hackathon.app.security;

import android.content.Context;
import com.facebook.AccessToken;
import hackathon.app.CurrentUserHolder;

/**
 * Created by don on 6/16/15.
 */
public class Authentication {

    public static boolean isLoggedIn(Context context) {
        return AccessToken.getCurrentAccessToken() != null && new CurrentUserHolder(context).getCurrentUserId() != -1;
    }
}
