package fullModel;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.SignatureType;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * Created by tiagoRodrigues on 08/04/2017.
 */
public class SpotifyApi20 extends DefaultApi20 {

    protected SpotifyApi20() {
    }



    public static SpotifyApi20 instance() {
        return SpotifyApi20.InstanceHolder.INSTANCE;
    }

    public String getAccessTokenEndpoint() {
        return "https://accounts.spotify.com/api/token";
    }

    protected String getAuthorizationBaseUrl() {
        return "https://accounts.spotify.com/authorize";
    }

    public OAuth20Service createService(OAuthConfig config) {
        return new Spotify20ServiceImpl(this, config);
    }

    public SignatureType getSignatureType() {
        return SignatureType.BEARER_URI_QUERY_PARAMETER;
    }

    private static class InstanceHolder {
        private static final SpotifyApi20 INSTANCE = new SpotifyApi20();

        private InstanceHolder() {
        }
    }

}
