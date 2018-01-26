package SpotifySongToPlaylistSorter.Services;


import SpotifySongToPlaylistSorter.Keys;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import SpotifySongToPlaylistSorter.SpotifyApiOAuth.SpotifyApi20;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Created by tiagoRodrigues on 20/04/2017.
 */
public class AuthService {


    final String clientId = Keys.oauthKey;
    final String clientSecret = Keys.secret;
    static OAuth2AccessToken accessToken;

    public void doAuthentication() throws InterruptedException, ExecutionException, IOException {


        final OAuth20Service service = new ServiceBuilder()
                .apiKey(clientId).apiSecret(clientSecret)
                .scope("user-read-private playlist-read-private playlist-modify-private playlist-modify-public user-library-read") // replace with desired scope
                .callback("http://localhost:8080/")
                .build(SpotifyApi20.instance());
        final Scanner in = new Scanner(System.in);

//        System.out.println("=== SPOTIFY " + "'s OAuth Workflow ===\n");

        // Obtain the Authorization URL
       System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl();


//        System.out.println("Got the Authorization URL!");
//        System.out.println("Now go and authorize ScribeJava here:");



        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");


        System.out.print(">>\n");
        final String code = in.nextLine();
        System.out.println();


        // Trade the Request Token and Verfier for the Access Token
//        System.out.println("Trading the Request Token for an Access Token...");
        accessToken = service.getAccessToken(code);
        System.out.println("Got the Access Token!\n");
        System.out.println("acces token" + accessToken.getAccessToken());
        System.out.println("(if your curious it looks like this: " + accessToken + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");

    }
}
