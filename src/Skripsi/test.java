package Skripsi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Abdurasyid
 */
public class test {

    static String[] token;

    public static void main(String[] args) {
        ReadFile read = new ReadFile();
        Preprocessing preprocess = new Preprocessing();

        ArrayList<String> final_term = new ArrayList();
        Set<String> tmp = new HashSet<>();
        HashMap<String, String> term_join = new HashMap<>();

        Map<String, String> map = read.getDataMap();                            //test

        map.entrySet().stream().forEach((data) -> {
            String join = "";
            ArrayList<String> term = preprocess.preprocessing(data.getValue());
            for (int i = 0; i < term.size(); i++) {
                join = join + term.get(i) + " ";
                final_term.add(term.get(i));
            }
            term_join.put(data.getKey().replaceAll(".txt", ""), join);          //term_join = term pada masing2 dokumen
            term.clear();
        });

        tmp.addAll(final_term);
        final_term.clear();
        final_term.addAll(tmp);
        Collections.sort(final_term);                                           //final_term = koleksi term

        Map<String, String> data = new TreeMap<>(term_join);

        String[] labelDoc = new String[data.size()];
        int n = 0;

        for (Map.Entry<String, String> docName : data.entrySet()) {
            labelDoc[n] = docName.getKey().replaceAll("doc\\s\\(\\d+\\)-", "");
            n++;
        }

        VectorSpaceModel vsm = new VectorSpaceModel(final_term, data);

        double[][] VSM = vsm.VSM();
        double[][] Norm_WF_IDF = vsm.getNorm_WF_IDF();

        double alpha = 0.50;
        int k = 2;
        
        double[] a = {
            0.5
        };
        int[] c = {
            2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 224
        };

//PENGUJIAN IMPROVED K-MEANS (CONSOLE)
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < a.length; j++) {
                ImprovedKMeans ikm = new ImprovedKMeans(VSM, a[j], c[i]);
                ikm.improvedKMeans();

                ArrayList<Integer> clusterResult = ikm.getCluster();
                double[][] distance = ikm.getDistance();
                double[][] currentCentroid = ikm.getCurrentCentroid();

                System.out.println("\n***SilhouetteCoefficient---");
                System.out.println("Jumlah klaster: " + c[i] + " dan nilai alpha: " + a[j]);
                SilhouetteCoefficient sc = new SilhouetteCoefficient(c[i], clusterResult, distance);
                sc.doSilhouetteCoefficient();

                System.out.println("\n***Purity---");
                Purity p = new Purity(c[i], labelDoc, clusterResult);
                p.calculatePurity();
                System.out.println();
            }
        }
        
//PENGUJIAN IMPROVED K-MEANS (CONSOLE)
//        ImprovedKMeans ikm = new ImprovedKMeans(VSM, alpha, k);
//        ikm.improvedKMeans();
//
//        System.out.println("\n***SilhouetteCoefficient---");
//        System.out.println("Jumlah klaster: " + k + " dan nilai alpha: " + alpha);
//        SilhouetteCoefficient sc = new SilhouetteCoefficient(k, ikm.getCluster(), ikm.getDistance());
//        sc.doSilhouetteCoefficient();
//
//        System.out.println("\n***Purity---");
//        Purity p = new Purity(k, labelDoc, ikm.getCluster());
//        p.calculatePurity();
//        System.out.println("\n***Nilai SSE---");
//        SSE sse = new SSE(k, ikm.getCluster(), vsm.getNorm_WF_IDF(), ikm.getCurrentCentroid());
//        sse.calculateSSE();

//PENGUJIAN K-MEANS (CONSOLE)
//        double[][] centroid = new double[VSM.length][k];
//        for (int i = 0; i < centroid.length; i++) {
//            for (int j = 0; j < centroid[0].length; j++) {
//                centroid[i][j] = VSM[i][j + 1];
//            }
//        }
//
//        KMeans km = new KMeans(k, centroid, VSM);
//        km.doKMeans();
//
//        System.out.println("\n***SilhouetteCoefficient---");
//        System.out.println("Jumlah klaster: " + k);
//        SilhouetteCoefficient sc2 = new SilhouetteCoefficient(k, km.getCluster(), km.getDistance());
//        sc2.doSilhouetteCoefficient();
//
//        System.out.println("\n***Purity---");
//        Purity p2 = new Purity(k, labelDoc, km.getCluster());
//        p2.calculatePurity();

//PENGUJIAN K-MEANS (CONSOLE)
//        int random = 0;
//        for (int z = 0; z < 7; z++) {
//            for (int i = 0; i < c.length; i++) {
//                double[][] centroid = new double[VSM.length][c[i]];
//                for (int x = 0; x < centroid.length; x++) {
//                    for (int j = 0; j < centroid[0].length; j++) {
//                        centroid[x][j] = VSM[x][j + random];
//                    }
//                }
//                KMeans km = new KMeans(c[i], centroid, VSM);
//                km.doKMeans();
//
//                System.out.println("\n***SilhouetteCoefficient---");
//                System.out.println("Jumlah klaster: " + c[i]);
//                SilhouetteCoefficient sc = new SilhouetteCoefficient(c[i], km.getCluster(), km.getDistance());
//                sc.doSilhouetteCoefficient();
//
//                System.out.println("\n***Purity---");
//                Purity p = new Purity(c[i], labelDoc, km.getCluster());
//                p.calculatePurity();
//            }
//            random = random + 20;
//        }
    }
}
