package Skripsi;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Abdurasyid
 */
public class SilhouetteCoefficient {

    private final int k;
    private double avg_sil;
    private final ArrayList<Integer> cluster;
    private final double[][] distance;
    private double[] silhouetteCoefficientDoc;

    /**
     * Deklarasi konstruktor kelas
     *
     * @param k -- variabel utk menyimpan jumlah klaster
     * @param cluster -- variabel utk menyimpan hasil klaster
     * @param distance -- variabel utk menyimpan jarak antar objek data
     */
    SilhouetteCoefficient(int k, ArrayList<Integer> cluster, double[][] distance) {
        this.k = k;
        this.cluster = cluster;
        this.distance = distance;
    }

    /**
     * Method yg digunakan utk memperoleh nilai a; dimana a merupakan rata-rata
     * jarak dok i terhadap dok lain yg berada dalam klaster sama
     *
     * @param index -- posisi dokumen
     * @return -- nilai a
     */
    private double getA(int index) {
        int count = 0;
        double sum = 0;

        for (int j = 0; j < distance[0].length; j++) {
            if (index != j && Objects.equals(cluster.get(j), cluster.get(index))) {
                sum = sum + distance[index][j];
                count++;
            }
        }
        if (count == 0 && sum == 0) {
            return 0;                                                           // return 0 jika klaster tsb hanya memiliki 1 data
        }
        return (double) sum / count;
    }

    /**
     * Method yg digunakan utk memperoleh nilai b; dimana b merupakan rata-rata
     * jarak dok i terhadap dok lain yg berada dalam klaster berbeda; hasil
     * akhir b merupakan nilai min jarak terhadap klaster lain
     *
     * @param index -- posisi dokumen
     * @return -- nilai b
     */
    private double getB(int index) {
        double B, min_B = 99;

        for (int k = 0; k < this.k; k++) {
            if (k != cluster.get(index)) {
                int count = 0;
                double sum = 0;

                for (int j = 0; j < distance[0].length; j++) {
                    if (index != j && cluster.get(j) == k) {
                        sum = sum + distance[index][j];
                        count++;
                    }
                }
                B = (double) sum / count;

                if (B < min_B) {
                    min_B = B;
                }
            }
        }
        return min_B;
    }

    /**
     * Method yg digunakan utk menghitung nilai Silhouette Coefficient
     */
    public void doSilhouetteCoefficient() {
        double sil, sum = 0;
        silhouetteCoefficientDoc = new double[cluster.size()];

        for (int i = 0; i < cluster.size(); i++) {
            double a = getA(i), b = getB(i);

            sil = (b - a) / Math.max(a, b);
            sum = sum + sil;
            silhouetteCoefficientDoc[i] = sil;

            //System.out.println("Klaster " + (cluster.get(i) + 1) + " | " + sil);
            //System.out.println("_______");
        }
        avg_sil = (double) sum / cluster.size();

        System.out.println("Rata-rata Sil: " + String.format("%.4f", avg_sil));
        System.out.println(avg_sil);
    }

    /**
     * Method yg digunakan utk mendapatkan nilai Silhouette Coefficient masing-
     * masing dokumen
     *
     * @return -- nilai Silhouette Coefficient dokumen
     */
    public double[] getSilhouetteCoefficientDoc() {
        return silhouetteCoefficientDoc;
    }

    /**
     * Method yg digunakan utk mendapatkan nilai rata-rata Silhouette
     * Coefficient
     *
     * @return -- rata-rata nilai Silhouette Coefficient
     */
    public double getAVGSilhouette() {
        return avg_sil;
    }
}
