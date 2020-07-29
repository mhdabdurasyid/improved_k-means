package Skripsi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Abdurasyid
 */
public class ReadFile {

    private final File dataFolder = new File("data/text");                      //variabel direktori dokumen txt
    private final String[] stopwordsList = new String[758];                     //variabel utk menampung daftar stopword (Bhs Indonesia)                    
    private final Set<String> rootwordList = new HashSet<>();                   //variabel utk menampung daftar kata dasar (Bhs Indonesia)
    private final HashMap<String, String> dataMap = new HashMap<>();            //variabel utk menyimpan informasi key dokumen dan value teks

    /**
     * Method yg digunakan utk memanggil teks
     *
     * @param folder -- alamat direktori penyimpanan txt file
     * @return -- txt file
     */
    private File[] getTextFile(File folder) {
        int i = 0;
        File[] collection = new File[folder.listFiles().length];

        for (File textFile : folder.listFiles()) {
            collection[i] = textFile;                                           //proses menyimpan txt file ke dalam array
            i++;
        }
        return collection;
    }

    /**
     * Method yg digunakan utk membaca isi teks (txt file)
     *
     * @param textFile -- txt file
     * @return -- String dokumen teks
     */
    private String getStringText(File textFile) {
        StringBuilder text = new StringBuilder();

        try {
            try (BufferedReader input = new BufferedReader(new FileReader(textFile))) {
                String line;
                while ((line = input.readLine()) != null) {
                    text.append(line);                                          //proses membaca isi txt file 
                    text.append(System.getProperty("line.separator"));
                }
            }
        } catch (IOException ex) {
        }
        return text.toString();
    }

    /**
     * Method yg digunakan utk setter data teks key berupa nama txt file value
     * berupa String teks
     */
    private void setDataMap() {
        File[] collection = getTextFile(dataFolder);

        for (File data : collection) {
            dataMap.put(data.getName(), getStringText(data));
        }
    }

    /**
     * Method yg digunakan utk getter data teks
     *
     * @return -- Map (key nama txt file, value String teks)
     */
    public Map<String, String> getDataMap() {
        setDataMap();
        Map<String, String> data = new TreeMap<>(dataMap);
        return data;
    }

    /**
     * Method yg digunakan utk setter daftar stopword dalam Bhs Indonesia
     */
    private void setStopwordsList() {
        String currentLine;
        int k = 0;

        try {
            FileReader stopwordsFile = new FileReader("data/stopword_list_tala.txt");
            BufferedReader line = new BufferedReader(stopwordsFile);
            while ((currentLine = line.readLine()) != null) {                   //proses membaca daftar stopword
                stopwordsList[k] = currentLine;                                 //menyimpan daftar stopword ke dalam array
                k++;
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Method yg digunakan utk getter daftar stopword dalam Bhs Indonesia
     *
     * @return -- daftar stopword
     */
    public String[] getStopwordsList() {
        setStopwordsList();
        return stopwordsList;
    }

    /**
     * Method yg digunakan utk setter daftar kata dasar Bhs Indonesia
     */
    private void setRootwordList() {
        String currentLine;

        try {
            FileReader rootwordFile = new FileReader("data/kata_dasar_indonesia.txt");
            BufferedReader line = new BufferedReader(rootwordFile);
            while ((currentLine = line.readLine()) != null) {                   //proses membaca kata dasar
                rootwordList.add(currentLine);                                  //menyimpan kata dasar ke dalam Set
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Method yg digunakan utk getter daftar kata dasar Bhs Indonesia
     *
     * @return -- daftar kata dasar
     */
    public Set<String> getRootwordList() {
        setRootwordList();
        return rootwordList;
    }
}
