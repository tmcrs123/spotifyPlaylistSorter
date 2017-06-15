package SpotifySongToPlaylistSorter.Models;

/**
 * Created by tiagoRodrigues on 26/04/2017.
 */
public enum Genres {

    POP ("Pop"),
    ROCK ("Rock"),
    INSTRUMENTAL ("Instrumental"),
    ACAPPELLA ("A cappella"),
    METAL ("Metal"),
    LIVE ("Live"),
    PORTUGUESE ("Portuguese"),
    GERMAN ("German"),
    HIPHOP ("Hip Hop"),
    JAZZ ("Jazz"),
    BLUES ("Blues"),
    UNSPECIFIED ("Unspecified"),
    ACOUSTIC ("Acoustic"),
    SOUNDTRACK("Soundtrack"),
    REGGAE("Reggae"),
    CLASSICAL("Classical"),
    DANCE("Dance"),
    COUNTRY("Country");


    private final String genre;

    Genres(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return genre;
    }
}
