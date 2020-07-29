package Skripsi;

import java.util.ArrayList;

/**
 *
 * @author Abdurasyid
 */
public class KMeans {

    private final int k;
    private final double[][] Norm_WF_IDF;
    private ArrayList<Integer> clusterResult;
    private double[][] oldCentroid, newCentroid;

    /**
     * Deklarasi konstruktor kelas
     *
     * @param k -- jumlah klaster
     * @param centroid -- centroid awal
     * @param Norm_WF_IDF -- nilai normalisasi WF.IDF
     */
    KMeans(int k, double[][] centroid, double[][] Norm_WF_IDF) {
        this.k = k;
        this.Norm_WF_IDF = Norm_WF_IDF;
        oldCentroid = centroid;                                                 //variabel yg digunakan utk menampung centroid lama
    }

    /**
     * Method yg digunakan utk menghitung jarak seluruh objek data digunakan utk
     * evaluasi hasil klaster, dimana jarak = 1 - CosSim
     *
     * @return -- seluruh nilai Cosine Similarity antar objek data
     */
    public double[][] getDistance() {
        double[][] distance = new double[Norm_WF_IDF[0].length][Norm_WF_IDF[0].length];

        for (int i = 0; i < distance.length; i++) {
            for (int j = 0; j < distance[0].length; j++) {
                double cosineSimilarity = cosineSimilarity(Norm_WF_IDF, Norm_WF_IDF, i, j);
                distance[i][j] = (double) 1 - cosineSimilarity;                   //proses hitung Distance
                //cosSim[i][j] = (double) cosineSimilarity;
            }
        }
        return distance;
    }

    /**
     * Method yg digunakan utk mengecek apakah sudah masuk kondisi konvergen
     *
     * @param newCentroid -- centroid baru
     * @param oldCentroid -- centroid lama
     * @return -- konvergen atau tidak
     */
    private boolean isConvergen(double[][] newCentroid, double[][] oldCentroid) {
        for (int i = 0; i < newCentroid[0].length; i++) {
            for (int j = 0; j < oldCentroid[0].length; j++) {
                if (i == j) {
                    for (int k = 0; k < Norm_WF_IDF.length; k++) {
                        if (newCentroid[k][i] != oldCentroid[k][j]) {           //return false jika terdapat salah satu nilai centroid yg berbeda
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Method yg digunakan utk menghitung nilai cosine similarity
     *
     * @param X -- doc X
     * @param Y -- centroid Y
     * @param i -- index doc X
     * @param j -- index centroid Y
     * @return -- nilai cosine similarity
     */
    private double cosineSimilarity(double[][] X, double[][] Y, int i, int j) {
        double cosSim = 0;

        for (int k = 0; k < Norm_WF_IDF.length; k++) {
            cosSim = cosSim + (X[k][i] * Y[k][j]);                              //cosSim merupakan perkalian vektor X dan Y
        }
        return cosSim;
    }

    /**
     * Method yg digunakan utk mengambil posisi klaster
     *
     * @return -- hasil klaster
     */
    public ArrayList<Integer> getCluster() {
        return clusterResult;
    }

    /**
     * Method yg digunakan utk mengambil nilai centroid terbaru
     *
     * @return -- centroid terbaru
     */
    public double[][] getNewCentroid() {
        return newCentroid;
    }

    /**
     * Method yg digunakan utk mengambil nilai centroid sebelumnya
     *
     * @return -- centroid sebelumnya
     */
    public double[][] getOldCentroid() {
        return oldCentroid;
    }

    /**
     * Method yg digunakan utk menentukan posisi klaster pada doc
     */
    private void determineCluster() {
        clusterResult = new ArrayList();

        for (int i = 0; i < Norm_WF_IDF[0].length; i++) {
            int cluster = -1;
            double max = -1;
            double[][] oldCentroid = getOldCentroid();

            for (int j = 0; j < oldCentroid[0].length; j++) {
                double cosSim = cosineSimilarity(Norm_WF_IDF, oldCentroid, i, j);    //proses hitung Cosine Similarity

                if (cosSim > max) {                                             //penentuan mana cosSim yang besar
                    max = cosSim;
                    cluster = j;                                                //penempatan posisi klaster 
                }
                //System.out.print(cosSim + " ");
            }
            clusterResult.add(cluster);                                         //masukkan hasil klaster ke dalam ArrayList
            //System.out.print("Klaster " + cluster+"|");
        }
        //System.out.println();
    }

    /**
     * Method yg digunakan utk menghitung kembali centroid baru
     */
    private void calculateNewCentroid() {
        ArrayList<Integer> clusterResult = getCluster();
        double[][] oldCentroid = getOldCentroid();
        newCentroid = new double[oldCentroid.length][oldCentroid[0].length];

        for (int x = 0; x < k; x++) {
            for (int i = 0; i < Norm_WF_IDF.length; i++) {
                int count = 0;
                double sum = 0;

                for (int j = 0; j < clusterResult.size(); j++) {
                    if (clusterResult.get(j) == x) {
                        sum = sum + Norm_WF_IDF[i][j];                          //proses hitung jumlah vektor yg berada di klaster sama
                        count = count + 1;
                    }
                }
                newCentroid[i][x] = (double) (sum / count);                     //proses hitung centroid baru (rata-rata vektor di klaster sama)
            }
        }
    }

    /**
     * Method yg digunakan utk melakukan KMeans
     */
    public void doKMeans() {
        int iteration = 1;

        do {
            System.out.println("Iterasi: " + iteration);
            if (iteration > 1) {                                                //gunakan aturan ini jika sudah masuk iterasi 2 dst
                oldCentroid = getNewCentroid();
            }
            determineCluster();                                                 //proses tentukan posisi klaster utk masing-masing odc
            calculateNewCentroid();                                             //proses hitung kembali centroid baru

            iteration++;
        } while (!isConvergen(getNewCentroid(), getOldCentroid()));                       //ulangi proses ini selama kondisi konvergen belum terpenuhi
    }
}
