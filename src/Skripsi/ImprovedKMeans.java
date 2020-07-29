package Skripsi;

import java.util.ArrayList;

/**
 *
 * @author Abdurasyid
 */
public class ImprovedKMeans {

    private final double[][] Norm_WF_IDF, euclideanDist, centroid;
    private final int[] density;
    private final double alpha;
    private final int k;
    private double MeanDist, MeanDens, alphaMeanDens;
    private ArrayList<Integer> higherDensity, indexHigherDensity, indexCentroid, clusterResult;
    private double[][] highDensData, currentCentroid;

    /**
     * Deklarasi konstruktor kelas
     *
     * @param dataInfo -- informasi dokumen (doc ke berapa)
     * @param Norm_WF_IDF -- nilai normalisasi WF.IDF
     * @param alpha -- nilai alpha dalam pemilihan centroid awal
     * @param k - jumlah klaster
     */
    ImprovedKMeans(double[][] Norm_WF_IDF, double alpha, int k) {
        this.Norm_WF_IDF = Norm_WF_IDF;
        euclideanDist = new double[Norm_WF_IDF[0].length][Norm_WF_IDF[0].length];  //variabel utk menampung jarak antar dokumen
        centroid = new double[Norm_WF_IDF.length][k];                           //variabel utk menampung centroid awal
        density = new int[Norm_WF_IDF[0].length];                               //variabel utk menampung nilai densitas dokumen
        this.alpha = alpha;
        this.k = k;
    }

    /**
     * Method yg digunakan utk mendapatkan posisi indeks dgn angka terbesar
     *
     * @param number -- data angka
     * @return -- indeks
     */
    private int getIndexMax(ArrayList<Integer> number) {
        int max = 0, index = -1;

        for (int i = 0; i < number.size(); i++) {
            if (number.get(i) > max) {
                max = number.get(i);
                index = i;
            }
        }
        return index;
    }

    /**
     * Method yg digunakan utk mengecek apakah vektor sudah digunakan sebagai
     * centroid awal
     *
     * @param data -- vektor dokumen
     * @param index -- posisi indeks
     * @return -- boolean
     */
    private boolean isCentroid(ArrayList<Integer> data, int index) {
        for (int y = 0; y < data.size(); y++) {
            if (data.get(y) == index) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method yg digunakan utk menghitung jarak data dgn menggunakan persamaan
     * Euclidean Distance
     *
     * @param X -- data X
     * @param Y -- data Y
     * @param i -- posisi index data X
     * @param j -- posisi index data Y
     * @return -- nilai Euclidean Distance
     */
    private double euclideanDistance(double[][] X, double[][] Y, int i, int j) {
        double sumXmY2 = 0;

        for (int k = 0; k < Norm_WF_IDF.length; k++) {
            sumXmY2 = sumXmY2 + Math.pow((X[k][i] - Y[k][j]), 2);
        }
        return Math.sqrt(sumXmY2);
    }

    /**
     * Method yg digunakan utk menghitung jarak antar dokumen dengan menggunakan
     * Euclidean Distance
     */
    private void setEuclideanDist() {
        for (int i = 0; i < euclideanDist.length; i++) {
            for (int j = 0; j < euclideanDist[0].length; j++) {
                euclideanDist[i][j] = euclideanDistance(Norm_WF_IDF, Norm_WF_IDF, i, j);    //proses hitung Euclidean Distance
                //euclideanDist[i][j] = (double) 1 - cosineSimilarity(Norm_WF_IDF, Norm_WF_IDF, i, j);
            }
        }
    }

    /**
     * Method yg digunakan utk memperoleh nilai Euclidan Distance
     *
     * @return -- jarak Euclidan Distance
     */
    public double[][] getEuclideanDistance() {
        return euclideanDist;
    }

    /**
     * Method yg digunakan utk menghitung Cosine Similarity
     *
     * @param X -- data X
     * @param Y -- data Y
     * @param i -- posisi index data X
     * @param j -- posisi index data Y
     * @return -- nilai Cosine Similarity
     */
    private double cosineSimilarity(double[][] X, double[][] Y, int i, int j) {
        double cosSim = 0;

        for (int k = 0; k < Norm_WF_IDF.length; k++) {
            cosSim = cosSim + (X[k][i] * Y[k][j]);                              //cosSim merupakan perkalian vektor X dan Y
        }
        return cosSim;
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
     * Method yg digunakan utk menghitung rata-rata jarak
     */
    private void setMeanDist() {
        int comb = 0;
        double sum = 0;

        for (int i = 0; i < euclideanDist.length; i++) {
            for (int j = 0; j < euclideanDist[0].length; j++) {
                if (i == j) {
                    break;
                }
                sum = sum + euclideanDist[i][j];
                comb++;
            }
        }
        MeanDist = (double) sum / comb;                                         //proses hitung rata-rata jarak
    }

    /**
     * Method yg digunakan utk memperoleh nilai rata-rata jarak
     *
     * @return -- rata-rata jarak
     */
    public double getMeanDist() {
        return MeanDist;
    }

    /**
     * Method yg digunakan utk menghitung nilai densitas dokumen
     */
    private void setDensity() {
        for (int i = 0; i < euclideanDist.length; i++) {
            int dens = 0;

            for (int j = 0; j < euclideanDist[0].length; j++) {
                if (MeanDist - euclideanDist[i][j] >= 0) {
                    dens = dens + 1;                                            //proses menentukan nilai densitas dokumen
                }
            }
            density[i] = dens;
        }
    }

    /**
     * Method yg digunakan utk memperoleh nilai densitas dokumen
     *
     * @return -- densitas
     */
    public int[] getDensity() {
        return density;
    }

    /**
     * Method yg digunakan utk menghitung rata-rata densitas
     */
    private void setMeanDens() {
        int sumDens = 0;

        for (int i = 0; i < density.length; i++) {
            sumDens = sumDens + density[i];
        }
        MeanDens = (double) sumDens / density.length;                           //proses hitung rata-rata densitas
    }

    /**
     * Method yg digunakan utk memperoleh nilai rata-rata densitas
     *
     * @return -- rata-rata densitas
     */
    public double getMeanDens() {
        return MeanDens;
    }

    /**
     * Method yg digunakan utk menentukan data dgn nilai densitas tertinggi
     */
    private void setHighDensData() {
        alphaMeanDens = alpha * MeanDens;                                       //proses hitung nilai alpha.meandens
        higherDensity = new ArrayList();
        indexHigherDensity = new ArrayList();
        int index = 0;

        for (int j = 0; j < density.length; j++) {
            if (density[j] >= alphaMeanDens) {                                  //proses menentukan apakah dokumen lebih besar dari nilai batas
                higherDensity.add(density[j]);
                indexHigherDensity.add(j);
            }
        }
        highDensData = new double[Norm_WF_IDF.length][higherDensity.size()];

        for (int j = 0; j < density.length; j++) {
            if (density[j] >= alphaMeanDens) {
                for (int k = 0; k < Norm_WF_IDF.length; k++) {
                    highDensData[k][index] = Norm_WF_IDF[k][j];                 //proses pembentukan vektor dokumen yang memiliki densitas tertinggi
                }
                index++;
            }
        }
    }

    /**
     * Method yg digunakan utk memperoleh posisi index densitas tertinggi
     *
     * @return -- index
     */
    public ArrayList<Integer> getIndexHighDensData() {
        return indexHigherDensity;
    }

    /**
     * Method yg digunakan utk memperoleh densitas yang memiliki nilai tinggi
     *
     * @return -- densitas tertinggi
     */
    public ArrayList<Integer> getHighDens() {
        return higherDensity;
    }

    /**
     * Method yg digunakan utk memperoleh data dok yg berada pada densitas
     * tertinggi
     *
     * @return -- data pada densitas tertinggi
     */
    public double[][] getHighDensData() {
        return highDensData;
    }

    /**
     * Method yg digunakan utk memperoleh nilai batas alpha x MeanDens
     *
     * @return -- alpha x MeanDens
     */
    public double getAplhaMeanDens() {
        return alphaMeanDens;
    }

    /**
     * Method yg digunakan utk menentukan centroid awal dari data dengan nilai
     * densitas tertinggi
     */
    private void setInitialCentroid() {
        indexCentroid = new ArrayList();

        for (int i = 0; i < highDensData.length; i++) {
            centroid[i][0] = highDensData[i][getIndexMax(higherDensity)];       //proses dmn densitas tertinggi digunakan sbg centroid K1
        }
        indexCentroid.add(getIndexMax(higherDensity));

        //proses menentukan centroid K2 - Kk
        for (int x = 1; x < k; x++) {
            double max = 0;
            int index = 0;

            for (int i = 0; i < x; i++) {
                for (int j = 0; j < highDensData[0].length; j++) {
                    if (!isCentroid(indexCentroid, j)) {
                        //double dist = euclideanDistance(centroid, highDensData, i, j);
                        double dist = (double) 1 - cosineSimilarity(centroid, highDensData, i, j);

                        if (dist > max) {
                            max = dist;                                         //proses menentukan jarak terjauh dari koleksi centroid 
                            index = j;
                        }
                    }
                }
            }

            for (int i = 0; i < highDensData.length; i++) {
                centroid[i][x] = highDensData[i][index];                        //proses memasukkan vektor dokumen sbg centroid awal
            }
            indexCentroid.add(index);
        }
    }

    /**
     * Method yg digunakan utk memperoleh centroid awal klaster
     *
     * @return -- centroid awal
     */
    public double[][] getInitialCentroid() {
        return centroid;
    }

    /**
     * Method yg digunakan utk memperoleh posisi index dari centroid awal
     *
     * @return -- index
     */
    public ArrayList<Integer> getIndexCentroid() {
        return indexCentroid;
    }

    /**
     * Method yg digunakan utk melakukan kmeans dgn memanggil kelas KMeans
     */
    private void doKMeans() {
        //k -- jumlah klaster
        //centroid -- centroid awal yang digunakan
        //Norm_WF_IDF -- pengelompokkan dilakukan pada hasil pembobotan kata WF.IDF 
        KMeans kmeans = new KMeans(k, centroid, Norm_WF_IDF);
        kmeans.doKMeans();
        clusterResult = kmeans.getCluster();

        currentCentroid = kmeans.getNewCentroid(); //*
    }

    /**
     * Method yg digunakan utk mendapatkan centroid terbaru
     * 
     * @return -- centroid baru
     */
    public double[][] getCurrentCentroid() {
        return currentCentroid;
    }

    /**
     * Method yg digunakan utk mengambil hasil akhir pengelompokkan
     *
     * @return -- hasil pengelompokkan
     */
    public ArrayList<Integer> getCluster() {
        return clusterResult;
    }

    /**
     * Proses Improved K-Means
     *
     * @return -- hasil pengelompokkan
     */
    public ArrayList<Integer> improvedKMeans() {
        setEuclideanDist();
        setMeanDist();
        setDensity();
        setMeanDens();
        setHighDensData();
        setInitialCentroid();
        doKMeans();

        System.out.println(higherDensity.size());

        return getCluster();
    }
}
