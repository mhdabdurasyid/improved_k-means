package Skripsi;

import java.util.ArrayList;

/**
 *
 * @author Abdurasyid
 */
public class Purity {

    private final ArrayList<Integer> cluster;
    private final String[] label = {"RPL", "KC", "JKI", "MG", "RB", "SC", "JKT", "SI"};
    private final String[] labelDoc;
    private final int k;
    private int[][] countLabel;
    private double purity;

    /**
     * Deklarasi konstruktor kelas
     *
     * @param k -- jumlah klaster
     * @param labelDoc -- label masing-masing dokumen
     * @param cluster -- hasil klaster
     */
    Purity(int k, String[] labelDoc, ArrayList<Integer> cluster) {
        this.k = k;
        this.labelDoc = labelDoc;
        this.cluster = cluster;
    }

    /**
     * Method yg digunakan utk menghitung jumlah label yg terdapat pada masing-
     * masing klaster
     */
    private void setCountLabel() {
        countLabel = new int[k][label.length];

        for (int i = 0; i < label.length; i++) {
            for (int j = 0; j < k; j++) {
                int count = 0;

                for (int k = 0; k < cluster.size(); k++) {
                    if (cluster.get(k) == j) {
                        if (label[i].equals(labelDoc[k])) {
                            count++;
                        }
                    }
                }
                countLabel[j][i] = count;
            }
        }
    }

    /**
     * Method yg digunakan utk memperoleh label klaster
     *
     * @return -- array label klaster
     */
    public String[] getLabel() {
        return label;
    }

    /**
     * Method yg digunakan utk memperoleh jumlah label pada masing-masing
     * klaster
     *
     * @return -- array jumlah label pd masing-masing klaster
     */
    public int[][] getCountLabel() {
        return countLabel;
    }

    /**
     * Method yg digunakan utk menghitung label mana yg dominan pada tiap
     * klaster
     *
     * @param countLabel -- array jumlah label yg ada pd tiap klaster
     * @return -- array jumlah label yg domninan
     */
    private int[] getMaxCountLabel(int[][] countLabel) {
        int[] maxCountLabel = new int[k];

        for (int i = 0; i < countLabel.length; i++) {
            int max = 0;

            for (int j = 0; j < countLabel[0].length; j++) {
                if (countLabel[i][j] > max) {
                    max = countLabel[i][j];
                }
            }
            maxCountLabel[i] = max;
        }
        return maxCountLabel;
    }

    /**
     * Method yg digunakan utk menghitung nilai purity
     */
    public void calculatePurity() {
        int sum = 0;
        int[] maxCountLabel;

        setCountLabel();
        maxCountLabel = getMaxCountLabel(getCountLabel());

        for (int i = 0; i < maxCountLabel.length; i++) {
            sum = sum + maxCountLabel[i];
        }
        purity = (double) sum / cluster.size();

        System.out.println("Jumlah: " + sum);
        System.out.println("Nilai Purity: " + purity);
    }

    /**
     * Method yg digunakan utk memperoleh nilai purity
     *
     * @return -- nilai purity
     */
    public double getPurity() {
        return purity;
    }
}
