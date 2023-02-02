package org.crawl.utils;

import lombok.NoArgsConstructor;
import org.crawl.pojo.HtmlEntity;
import org.ejml.simple.SimpleMatrix;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Component
public class PageRank {
    /*
    q=G*q 当q不再变化时，q就是rank，即收敛特征向量
     */

    public void getPageRank(List<HtmlEntity> list,double threshold){

//        int count=20;
        SimpleMatrix q = getRandomQ(list.size());

        SimpleMatrix G=getGFromHtmlList(list);

//        G.stream().forEach(i -> {
//            i.stream().forEach(
//                j->{System.out.print(j+" ");});
//            System.out.println("\n");
//        });

//        System.out.println(G);

        while(true){
            SimpleMatrix result=G.mult(q);
            double d=getDistance(result,q);
//            System.out.println(d);
            if(d<=threshold){
//                System.out.println(result);
                for(int i=0;i<list.size();i++){
                    list.get(i).setPr(result.get(i));
                }
                break;
            }
            q=result.copy();

        }
    }

    private SimpleMatrix getRandomQ(int n) {
            Random random = new Random();
            double[] q = new double[n];
            for (int i = 0; i < n; i++) {
                q[i]=5 * random.nextDouble();
            }
            return new SimpleMatrix(n,1,true,q);
//        0.2068648767053453, 2.058981823016449, 1.1405438760105855, 2.110006136410161,
//        return new SimpleMatrix(4,1,true,new double[]{2.14335103032906, 0.4690253246490811, 0.152093449701467, 2.751926907462932});

    }
//
//    private List<Double> getFoldMatrix(List<List<Double>> g, List<Double> q) {
//        List<Double> row = new ArrayList<>();
//        for(int i=0; i<g.size(); i++) {
//            row.add(getFoldMatrixLine(g.get(i),q));
//        }
//        return row;
//    }
//
//    private Double getFoldMatrixLine(List<Double> p, List<Double> q) {
//        int n=p.size();
//        double sum=0;
//        for(int i=0; i<n; i++) {
//            sum+=p.get(i)*q.get(i);
//        }
//        return sum;
//    }

    private Double getDistance(SimpleMatrix p, SimpleMatrix q) {
        return p.minus(q).normF();
    }

    /*

     */

    private SimpleMatrix getGFromHtmlList(List<HtmlEntity> list) {
        double x=0.85;
        SimpleMatrix S=getLinkMatrix(list);
//        S.stream().forEach(i -> {
//            i.stream().forEach(
//                    j->{System.out.print(j+" ");});
//            System.out.println("\n");
//        });
        int n=list.size();

        SimpleMatrix U = new SimpleMatrix(n,n);
        U.fill(1);

        SimpleMatrix G=S.scale(x).plus(U.scale((1.0-x)*1.0/n));

        return G;
    }

//    private List<List<Double>> getGFromHtmlList(List<HtmlEntity> list) {
//        double x=0.85;
//        List<List<Double>> S=getLinkMatrix(list);
////        S.stream().forEach(i -> {
////            i.stream().forEach(
////                    j->{System.out.print(j+" ");});
////            System.out.println("\n");
////        });
//        int n=list.size();
//        List<List<Double>> U=getOneMatrix(list);
//
//        List<List<Double>> G=matrixAdd(matrixFoldStaic(x,S),matrixFoldStaic((1.0-x)*1.0/n,U));
//
//        return G;
//    }

//    private List<List<Double>> matrixAdd(List<List<Double>> s1, List<List<Double>> s2) {
//        List<List<Double>> res=new ArrayList<List<Double>>();
//        int n=s1.size();
//        for(int i=0;i<n;i++){
//            List<Double> row=new ArrayList<Double>();
//            for(int j=0;j<n;j++){
//                row.add(s1.get(i).get(j)+s2.get(i).get(j));
//            }
//            res.add(row);
//        }
//        return res;
//    }
//
//    private List<List<Double>> matrixFoldStaic(double x, List<List<Double>> s) {
//        List<List<Double>> res=new ArrayList<List<Double>>();
//        int n=s.size();
//        for(int i=0;i<n;i++){
//            Double num=x;
//            List<Double> row=new ArrayList<Double>();
//            for(int j=0;j<n;j++){
//                row.add(num*s.get(i).get(j));
//            }
//            res.add(row);
//        }
//        return res;
//    }
//
//    private List<List<Double>> getOneMatrix(List<HtmlEntity> list) {
//        List<List<Double>> S=new ArrayList<List<Double>>();
//        int n=list.size();
//        for(int i=0;i<n;i++){
//            Double num=1.0;
//            List<Double> row=new ArrayList<Double>();
//            for(int j=0;j<n;j++){
//                row.add(num);
//            }
//            S.add(row);
//        }
//        return S;
//    }
//
    private SimpleMatrix getLinkMatrix(List<HtmlEntity> list) {

        int n=list.size();
        double[][] S=new double[n][n];
        for(int i=0;i<n;i++){
            double[] row=new double[n];
            HtmlEntity h=list.get(i);
            List<String> outLinks = h.getUrls();
            int size = outLinks.size();
            for (int j=0; j<n;j++){
                HtmlEntity temp = list.get(j);
                if(i!=j && outLinks.stream().anyMatch(k->k.equals(temp.getUrl()))){
                    row[j]=1.0/Double.valueOf(size);
                }
                else{
                    row[j]=0.0;
                }
            }
            S[i]=row;
        }


        return new SimpleMatrix(S).transpose();
    }
}
