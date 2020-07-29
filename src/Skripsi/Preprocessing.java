package Skripsi;

import java.util.ArrayList;
import java.util.Arrays;
import jsastrawi.morphology.DefaultLemmatizer;
import jsastrawi.morphology.Lemmatizer;

/**
 *
 * @author Abdurasyid
 */
public class Preprocessing {

    private ArrayList<String> data;                                             //variabel utk menampung token                                
    private final ArrayList term;                                               //variabel utk menampung kata dasar

    /**
     * Deklarasi konstruktor kelas
     */
    Preprocessing() {
        term = new ArrayList();
    }

    /**
     * Method yg digunakan utk menghapus tag judul dan abstrak dari teks
     *
     * @param data -- teks
     * @return -- teks
     */
    public String removeTag(String data) {
        return data.replaceAll("<JUDUL>|</JUDUL>|<ABSTRAK>|</ABSTRAK>", "");
    }

    /**
     * Method yg digunakan utk menghapus tanda baca dan angka dari teks
     *
     * @param data -- teks
     * @return -- teks
     */
    public String removeNumberPunctuation(String data) {
        return data.replaceAll("[^a-zA-Z]", " ");                               //proses mengganti seluruh karakter non alfabet menjadi karakter spasi
    }

    /**
     * Method yg digunakan utk melakukan case folding pada teks
     *
     * @param data -- teks
     * @return -- teks
     */
    public String caseFolding(String data) {
        return data.toLowerCase();                                              //proses mengubah seluruh karakter menjadi lower
    }

    /**
     * Method yg digunakan utk melakukan tokenisasi pada teks
     *
     * @param data -- teks
     * @return -- token
     */
    public String[] tokenization(String data) {
        String[] term = data.split("\\s+");                                     //bentuk token berdasarkan karakter white space
        return term;
    }

    /**
     * Method yg digunakan utk menghapus kata dasar dari token
     *
     * @param data -- token
     * @return -- token
     */
    public ArrayList<String> removeStopwords(String[] data) {
        ReadFile read = new ReadFile();
        String[] stopwordsList = read.getStopwordsList();
        this.data = new ArrayList<>(Arrays.asList(data));

        try {
            this.data.stream().forEach((_item) -> {
                for (String stopwords : stopwordsList) {
                    if (this.data.contains(stopwords)) {                        //jika token terdapat stopword
                        this.data.remove(stopwords);                            //maka hapus dari token
                    }
                }
            });
        } catch (Exception ex) {
        }
        return this.data;
    }

    /**
     * Method yg digunakan utk melakukan stemming pada token
     *
     * @param token -- token
     * @return -- kata dasar
     */
    public ArrayList<String> stemming(ArrayList<String> token) {
        ReadFile read = new ReadFile();
        Lemmatizer lemmatizer = new DefaultLemmatizer(read.getRootwordList());  //stemming dgn bantuan library jsastrawi (alg AlgorithmNaziefAdriani)

        for (int i = 0; i < token.size(); i++) {
            term.add(lemmatizer.lemmatize(token.get(i)));
        }
        return term;
    }

    /**
     * Praproses teks
     *
     * @param data -- teks
     * @return -- kata dasar
     */
    public ArrayList<String> preprocessing(String data) {
        data = removeTag(data);
        data = removeNumberPunctuation(data);
        data = caseFolding(data);
        String[] token = tokenization(data);
        ArrayList<String> term = removeStopwords(token);

        return stemming(term);
    }
}
