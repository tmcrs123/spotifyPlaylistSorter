package fullModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fullModel.Tests.Genres;

/**
 * Created by tiagoRodrigues on 18/04/2017.
 */
@JsonIgnoreProperties({"available_markets","disc_number","duration_ms","explicit","external_ids","external_urls","href","is_playable","linked_from","popularity","preview_url","track_number","type"})
public class Track {

    Album album;
    Artist[] artists;
    String id;
    String name;
    String uri;
    Genres genre;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Artist[] getArtists() {
        return artists;
    }

    public Artist getFirstArtist(){
        return artists[0];
    }

    public void setArtists(Artist[] artists) {
        this.artists = artists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Genres getGenre() {
        return genre;
    }

    public void setGenre(Genres genre) {
        this.genre = genre;
    }
}
