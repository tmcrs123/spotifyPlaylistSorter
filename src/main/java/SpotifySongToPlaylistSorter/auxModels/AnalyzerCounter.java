package SpotifySongToPlaylistSorter.auxModels;

/**
 * Created by tiagoRodrigues on 14/05/2017.
 */
public class AnalyzerCounter {

    public String genreName;
    public int counter;

    public AnalyzerCounter(String genreName) {
        this.genreName = genreName;
        this.counter = 0;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public int getCounter() {
        return counter;
    }

    public void incrementCounter() {
        counter++;
    }

    public void incrementCounter(int increment){
        counter = counter + increment;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
