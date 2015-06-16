package hackathon.app.facebook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;

import java.io.InputStream;

/**
 * Created by don on 6/14/15.
 */
public class FacebookService {

    public void getUserInfo(GraphRequest.GraphJSONObjectCallback callback) {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken == null || accessToken.isExpired()) {
            throw new RuntimeException("Invalid access token");
        }

        GraphRequest request = GraphRequest.newMeRequest(accessToken, callback);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void downloadFacebookImage(ImageView imageView, String facebookId) {
        new DownloadImageTask(imageView).execute("https://graph.facebook.com/" + facebookId + "/picture");
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
            bmImage.setMinimumWidth(250);
            bmImage.setMinimumHeight(250);
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
