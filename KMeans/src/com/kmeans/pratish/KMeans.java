package com.kmeans.pratish;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.*;
import java.util.ArrayList;
import java.util.List;




class Cluster{
	List<List<Integer>> tuples; 
	
	public Cluster() {
		// TODO Auto-generated constructor stub
		tuples = new ArrayList<List<Integer>>(); 
	}
}

public class KMeans {
	public final static int maxiter=30; 
	public final static int col=11;
	
	public static void iniCentroids(int cen[]){
		Random rand = new Random();
		
//		System.out.println("Till Here");
//		System.out.println(cen.length);
		
		for (int i=1 ; i < cen.length ; i++){
			cen[i] = rand.nextInt((10 -1 ) + 1) + 1;
		}
	}
	
	public static int checkDistance(int x[], int y[]){
		long d = 0;
		
		for (int i=1 ; i < col ; i++){
			d += Math.pow((x[i] - y[i]),2);
		}
		
		d = (int)Math.sqrt(d);
		
		return (int) d;
		
	}
	public static int minDistCentroidIndex(int x[]){
		int min = 1000000 ;
		int index=0;
		
		for (int i=0;i < x.length ; i++){
			if (x[i] < min){
				min = x[i];
				index = i;
			}
		}
		
		return index;
		
	}
	
	public static boolean checkConvergence(int prevc[][],int c[][],int k) {
		for (int i=0;i < k ; i++){
			for (int j=0 ; j < col ; j ++){
					if ( prevc[i][j] != c[i][j])
						return false;
			}
		}
		
		return true;
	}
	
	public static void KmeansCal(int k , int featureset[][], int lines) throws IOException{
		int prevc [][] =  new int[k][col];
		
		//System.out.println("Point 1");
		/*
		System.out.println();
		for (int i=0;i < lines ; i++){
			for (int j=0 ; j <9 ; j ++){
				System.out.print(featureset[i][j] + " ");
			}
			System.out.println();
		}*/
		
		int c[][] = new int [k][col ];
		
		for (int i=0 ; i < k ; i++){
			iniCentroids(c[i]);
		}
		
		System.out.println();
		
		int loopcounter = 0;
		// 
		while (true){
			Cluster cluster []= new Cluster[k];
			for (int i=0;i < k ; i++){
				cluster[i] = new Cluster();
			}
			loopcounter ++;
			
			/*
			for (int i=0;i < k ; i++){
				System.out.println("C" + i+" : ");
				for (int j=0 ; j < col ; j ++){
					System.out.print(c[i][j] + " ");
				}
				System.out.println();
			}*/
			//System.out.println("Point 2");
			int dist[] = new int [k];
			//Clustering of the dataset
			for (int i=0 ; i < lines ; i ++){
				//System.out.print("Dataset : ");
				for (int a=0 ; a < col ; a ++)
				//System.out.print(featureset[i][a] + " ");
				//System.out.println();
				
				for (int j=0; j< k ; j++){
					dist[j] = checkDistance(featureset[i], c[j]);
					//System.out.println("Dist " + i + " -> c"+ j +": " + dist[j]);
				}
				//System.out.println("Point 3");
				int cluster_index=minDistCentroidIndex(dist);
				List<Integer> tempList = new ArrayList<Integer>();
				for (int l =0 ; l < col ; l++){
					tempList.add(featureset[i][l]);
				}
				//System.out.println(cluster_index);
				cluster[cluster_index].tuples.add(tempList);
				
				//cluster.get(cluster_index).add(tempList);
				
			}
			
			// Cal the the new centroids and repeat
			
			//System.out.println("cluster length :"+ cluster.length);
			for (int i = 0 ; i < k ; i++){
				int[] cenavg = new int[col];
				System.out.println("Total number of elements in C"+i+ " : " + cluster[i].tuples.size());
				//System.out.println("C" + i +" : ");
				for (int j=0; j< cluster[i].tuples.size() ; j++){
					//System.out.println(cluster[i].tuples.get(j) + " ");
					for(int l=1 ; l < cluster[i].tuples.get(j).size(); l++)
						cenavg [l] += cluster[i].tuples.get(j).get(l);
				}
				
				//System.out.println("CenAvg : " );
				for(int l=1 ; l < cenavg.length; l++){
					cenavg[l] = cenavg[l] / cluster[i].tuples.size();
					//System.out.print(cenavg[l] + " ");
					prevc[i][l] = c [i][l];
					c[i][l] = cenavg[l];
				}
				//System.out.println();
			}
			System.out.println("%%%%%%%%%%%%%%%%%%%%%  Round  "+loopcounter+" %%%%%%%%%%%%%%%%%%%%%");
			
			if ( checkConvergence(prevc,c,k) == true){
				System.out.println("Convergence has been met");
				
				for (int i = 0; i < k; i++) {
					FileWriter fw = new FileWriter("C:\\Users\\Pratish\\Documents\\Java Files\\KMeans\\R files\\K"+i+"Cluster.txt");
					for(int j=0;j < cluster[i].tuples.size(); j++){
						for (int l=0 ; l < cluster[i].tuples.get(j).size(); l++)
							fw.write(cluster[i].tuples.get(j).get(l)+ ";");
						fw.write(System.getProperty( "line.separator" ));
					}
					fw.close();
				}
			 
				
				
				break;
			}
			
			
			
			
		}
			
		
		
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner input =  new Scanner(System.in);
		File inputFile= new File("C:\\Users\\Pratish\\Documents\\Java Files\\KMeans\\R files\\Clean_BreastCancer_Data.txt");
		Scanner readFile = new Scanner(inputFile);
		LineNumberReader readlines = new LineNumberReader(new FileReader(inputFile));
		readlines.skip(Long.MAX_VALUE);
		int lines = readlines.getLineNumber();
		int featureset[][] = new int[lines][col]; ;
		int k = 2;
		String readLine;
		
		
		
		
		System.out.print("Please enter input for no of clusters K :  ");
		k =input.nextInt();
		System.out.println("K = "+k);
		System.out.println();
		//Create a double dimension array from text file 
	
		
		for (int i=0;i < lines ; i++){ 
			readLine = readFile.nextLine();
			//System.out.println(readLine);
			
			readLine=readLine.replaceAll("\"", "");
			//System.out.println(readLine);
			String[] s = readLine.split(";");
	
			for (int j=0 ; j < col ; j ++){
				featureset[i][j] = Integer.parseInt(s[j]);
				//System.out.println();
				//System.out.print(featureset[i][j]+ " ");
			}
		}
		/*System.out.println();
		for (int i=0;i < lines ; i++){
			for (int j=0 ; j <col ; j ++){
				System.out.print(featureset[i][j] + " ");
			}
			System.out.println();
		}*/
		 
		
		KmeansCal(k,featureset,lines);
		
		
		input.close();
		readFile.close();
		readlines.close();
		
	}

}
