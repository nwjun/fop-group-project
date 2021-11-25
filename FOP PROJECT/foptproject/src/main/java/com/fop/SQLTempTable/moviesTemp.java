package com.fop.SQLTempTable;
 
import java.util.Properties;

public class moviesTemp {
    private Properties movies;
    
    public moviesTemp(){
        Properties prop = new Properties();
        
        prop.put("movieId", "00001"); //String
        prop.put("movieName", "Anita"); //String
        prop.put("length", "2.50"); //decimal
        prop.put("releaseDate", "2020-12-17"); //Date
        prop.put("directorCast", "{\"Cast\": \"Ape\", \"Director\": \"Lim Wei Xin\"}"); //Json
        prop.put("language", "En"); //String
        prop.put("posterId", "1"); //String
        prop.put("allShowTime", "{\"Evening1\": \"8.00p.m.\", \"Morning1\": \"8.00a.m.\", \"Morning2\": \"11.00a.m.\", \"Afternoon1\": \"3.00p.m.\"}"); //json
        prop.put("allShowDate", "{\"December\": \"17\"}"); //json
        prop.put("rottenTomato", "9.00"); //decimal
        prop.put("iMDB", "9.50"); //decimal
        prop.put("ageRestrict", "18"); //int
        
        this.movies = prop;
    }
    
    public String[] getMovies(){
        String [] get = new String [12];
        
        get[0] = movies.getProperty("movieId");
        get[1] = movies.getProperty("movieName");
        get[2] = movies.getProperty("length");
        get[3] = movies.getProperty("releaseDate");
        get[4] = movies.getProperty("directorCast");
        get[5] = movies.getProperty("language");
        get[6] = movies.getProperty("posterId");
        get[7] = movies.getProperty("allShowTime");
        get[8] = movies.getProperty("allShowDate");
        get[9] = movies.getProperty("rottenTomato");
        get[10] = movies.getProperty("iMDB");
        get[11] = movies.getProperty("ageRestrict");
        
        return get;        
    }
}

//import com.fop.SQLTempTable.moviesTemp;
//
//public class Main{
//    public static void main(String[] args) {
//        moviesTemp x = new moviesTemp();
//        for (String x1: x.getMovies()){
//            System.out.print(x1 + " ");
//        }
//    }   
//}
