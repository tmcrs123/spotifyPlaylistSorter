package fullModel.Tests;

/**
 * Created by tiagoRodrigues on 26/04/2017.
 */
public enum Genres {

    POP ("Pop"),
    ROCK ("Rock"),
    INSTRUMENTAL ("Instrumental"),
    ACAPPELLA ("Acappella"),
    METAL ("Metal"),
    LIVE ("Live"),
    PORTUGUESA ("Portuguesa"),
    ALEMÃ ("Alemã"),
    HIPHOP_RAP ("HipHop_Rap"),
    JAZZ ("Jazz"),
    BLUES ("Blues"),
    BOSSA_NOVA ("Bossa_Nova"),
    UNSPECIFIED ("Unspecified");


    private final String genre;

    Genres(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return genre;
    }
}
