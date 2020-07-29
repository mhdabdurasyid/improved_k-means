package Skripsi;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Abdurasyid
 */
public class VectorSpaceModel {

    private final ArrayList<String> term;                                       //variabel utk menampung kata dasar hasil praproses
    private final Map<String, String> term_doc;                                 //variabel utk menampung teks
    private final double[][] vector, weightTF, WF_IDF, Norm_WF_IDF;
    private final double[] IDF;                                                 //variabel utk menampung nilai IDF
    private final String[] docName;                                             //variabel utk menampung nama dokumen

    /**
     * Deklarasi konstruktor kelas
     *
     * @param term -- kata dasar
     * @param term_doc -- dokumen teks
     */
    VectorSpaceModel(ArrayList<String> term, Map<String, String> term_doc) {
        this.term = term;
        this.term_doc = term_doc;
        vector = new double[term.size()][term_doc.size()];                      //variabel utk menampung nilai TF
        weightTF = new double[term.size()][term_doc.size()];                    //variabel utk menampung nilai bobot TF
        IDF = new double[term.size()];
        WF_IDF = new double[term.size()][term_doc.size()];                      //variabel utk menampung nilai WF.IDF      
        Norm_WF_IDF = new double[term.size()][term_doc.size()];                 //variabel utk menampung nilai normalisasi WF.IDF 
        docName = new String[term_doc.size()];
    }

    /**
     * Method yg digunakan utk memperoleh nama dokumen
     *
     * @return -- nama dokumen
     */
    public String[] getDocName() {
        int i = 0;

        for (Map.Entry<String, String> data : term_doc.entrySet()) {
            docName[i] = data.getKey();                                         //dapatkan informasi nama dokumen
            i++;
        }
        return docName;
    }

    /**
     * Method yg digunakan utk menghitung nilai TF
     */
    private void setTF() {
        for (int i = 0; i < term.size(); i++) {
            int count = 0;

            for (Map.Entry<String, String> data : term_doc.entrySet()) {
                double tf = 0;

                String[] token = data.getValue().split("\\s");
                for (String t : token) {
                    if (term.get(i).equals(t)) {
                        tf = tf + 1;                                            //proses hitung frekuensi kemunculan kata pd dokumen
                    }
                }
                vector[i][count] = tf;
                count = count + 1;
            }
        }
    }

    /**
     * Method yg digunakan utk memperoleh nilai TF
     *
     * @return -- nilai TF
     */
    public double[][] getTF() {
        return vector;
    }

    /**
     * Method yg digunakan utk menghitung nilai bobot TF
     */
    private void setWeightTF() {
        double[][] vector = getTF();

        for (int i = 0; i < vector.length; i++) {
            for (int j = 0; j < vector[0].length; j++) {
                if (vector[i][j] > 0) {
                    weightTF[i][j] = 1 + Math.log10(vector[i][j]);              //proses hitung bobot tf -> 1 + log TF
                }
            }
        }
    }

    /**
     * Method yg digunakan utk memperoleh nilai bobot TF
     *
     * @return -- nilai bobot TF
     */
    public double[][] getWeightTF() {
        return weightTF;
    }

    /**
     * Method yg digunakan utk menghitung nilai IDF
     */
    private void setIDF() {
        double[][] vector = getTF();
        Map<String, String> term_doc = this.term_doc;

        for (int i = 0; i < vector.length; i++) {
            int count = 0, df = 0;

            for (Map.Entry<String, String> data : term_doc.entrySet()) {
                String[] token = data.getValue().split("\\s");
                for (String term : token) {
                    if (this.term.get(i).equals(term)) {
                        df = df + 1;                                            //proses hitung df
                        break;
                    }
                }
                count = count + 1;
            }
            IDF[i] = Math.log10((double) term_doc.size() / df);                 //proses hitung IDF -> log N/df
        }
    }

    /**
     * Method yg digunakan utk memperoleh nilai IDF
     *
     * @return -- nilai IDF
     */
    public double[] getIDF() {
        return IDF;
    }

    /**
     * Method yg digunakan utk menghitung nilai WF.IDF
     */
    private void setWF_IDF() {
        double[][] weightTF = getWeightTF();

        for (int i = 0; i < weightTF.length; i++) {
            for (int j = 0; j < weightTF[0].length; j++) {
                WF_IDF[i][j] = weightTF[i][j] * IDF[i];                         //proses perkalian WF dan IDF
            }
        }
    }

    /**
     * Method yg digunakan utk memperoleh nilai WF.IDF
     *
     * @return -- nilai WF.IDF
     */
    public double[][] getWF_IDF() {
        return WF_IDF;
    }

    /**
     * Method yg digunakan utk menghitung nilai normalisasi WF.IDF
     */
    private void setNorm_WF_IDF() {
        double[][] WF_IDF = getWF_IDF();
        double[] sp = new double[WF_IDF[0].length];

        for (int i = 0; i < WF_IDF[0].length; i++) {
            double sumproduct = 0;

            for (double[] wf_idf : WF_IDF) {
                sumproduct = sumproduct + Math.pow(wf_idf[i], 2); //sumproduct vektor
            }
            sp[i] = sumproduct;
        }

        for (int i = 0; i < WF_IDF.length; i++) {
            for (int j = 0; j < WF_IDF[0].length; j++) {
                Norm_WF_IDF[i][j] = WF_IDF[i][j] / Math.sqrt(sp[j]);            //proses normaliasi WF.IDF
            }
        }
    }

    /**
     * Method yg digunakan utk memperoleh nilai normalisasi WF.IDF
     *
     * @return -- nilai normalisasi WF.IDF
     */
    public double[][] getNorm_WF_IDF() {
        return Norm_WF_IDF;
    }

    /**
     * Proses Vector Space Model
     *
     * @return -- pembobotan WF.IDF
     */
    public double[][] VSM() {
        setTF();
        setWeightTF();
        setIDF();
        setWF_IDF();
        setNorm_WF_IDF();

        return getNorm_WF_IDF();
    }
}
