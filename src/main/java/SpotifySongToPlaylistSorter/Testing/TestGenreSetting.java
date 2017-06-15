package SpotifySongToPlaylistSorter.Testing;

import SpotifySongToPlaylistSorter.Services.*;
import SpotifySongToPlaylistSorter.Models.Artist;
import SpotifySongToPlaylistSorter.Models.Playlist;
import SpotifySongToPlaylistSorter.Models.Track;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by tiagoRodrigues on 06/04/2017.
 */
public class TestGenreSetting {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        AuthService authService = new AuthService();
        UserService userService = new UserService();
        SongLibraryService songLibraryService = new SongLibraryService();
        PlaylistService playlistService = new PlaylistService();
        SongAnalyzerService songAnalyzerService = new SongAnalyzerService();
        ArtistService artistService = new ArtistService();


        HashMap<String, Playlist> playlistHashMap;

        authService.doAuthentication();

        userService.createUser();


        playlistHashMap = playlistService.getPlaylists(userService.getUser().getId());

        Artist artist = new Artist();
        artist.setId("0nmQIMXWTXfhgOBdNzhGOs");

        Artist[] artistArrays = new Artist[1];

        artistService.getGenresforArtist(artist);

        artistArrays[0] = artist;

        Track testTrack = new Track();

        testTrack.setName("Fado do ladrão");

        testTrack.setArtists(artistArrays);

        songAnalyzerService.trackGenreAnalyzer(testTrack);


    }
}





