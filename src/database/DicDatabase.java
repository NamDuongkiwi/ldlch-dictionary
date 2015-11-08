package database;

/**
 * Created by cuong on 10/20/2015.
 */
public class DicDatabase {
    //default number for DicAbstract in app (E_V, V_E, Recent words)
    public final static int E_V = 1;
    public final static int V_E = 2;
    public final static int RECENT_WORDS = 3;

    private DicAbstract dicData[] = new DicAbstract[3];

    public DicDatabase() {
        loadDatabase();
    }

    private void loadDatabase() {

        System.out.println("Loading dictionaries\' databases:");

        // load data of English - Vietnamese Dictionary
        dicData[0] = new Dictionary();
        dicData[0].setPath("./data/E_V.zip");
        dicData[0].setName("E_V");
        dicData[0].readData();

        // load data of Vietnamese - English Dictionary
        dicData[1] = new Dictionary();
        dicData[1].setPath("./data/V_E.zip");
        dicData[1].setName("V_E");
        dicData[1].readData();

        // load data of recent words file
        dicData[2] = new RecentWords();
        dicData[2].setPath("./data/recentWords.txt");
        dicData[2].setName("RECENT_WORDS");
        dicData[2].setData(dicData[0].getData()); // data for recent words is from E_V
        dicData[2].readData();
    }

    public DicAbstract getDictionary(int i) {
        return dicData[i];
    }

//    public static void main (String args[]) {
//        DicDatabase dicDatabase = new DicDatabase();
//    }
}
