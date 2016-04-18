
import java.sql.*;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author NavneetSingh
 */


public class hw2 {

    public static void main(String[] args) {
         
        String connectionString = "jdbc:mysql://localhost:3306/nsbhw2";
        String username = "root";
        String password = "nsbabra";

        Connection connection;
        PreparedStatement prestatement;
        ResultSet resultset;
        String finalquery;
        
        int queryMode = 0;
        
        try {
            
            
            if(args.length <1){
                System.out.println("Invalid/No Arguments");
                return;
            }
            
           
                String queryType = args[0];  // can be window , within, neareastneighbor, fixed
                System.out.println("Query Type : "+queryType);

                try {
                    connection = DriverManager.getConnection(connectionString, username, password);      
                } catch(Exception ex)  {
                    ex.printStackTrace();
                    System.out.println("Couldn't make connection");
                    return;
                }
                    
// Query 1  
// select s.stuid, astext(s.stulocation), st_within(s.stulocation, polyfromtext(('POLYGON((100 100, 300 100, 300 300, 100 300, 100 100))'))) from student s;

            
            if(queryType.equals("window")) {
               queryMode = 1;
            }
            else if(queryType.equals("within")){
                queryMode = 2;
            }
            else if(queryType.equals("nearest-neighbor")){
                queryMode = 3;
            }
            else if(queryType.equals("fixed")){
                queryMode = 4;
            }
            else{
                System.out.println("Query Type Not Supported");
            }
            
            switch(queryMode){
                case 1:
                    try{
                        String objectType = args[1];
                    
                        System.out.println("Object Type : "+objectType);
                        
                        

                        String lowerX = args[2];
                        String lowerY = args[3];

                        String upperX = args[4];
                        String upperY = args[5];

                        System.out.println("Co-ordinates are: ("+lowerX+","+lowerY+") ; ("+upperX+","+upperY+")"); 
                        
                       
                        
                        /*
                            select s.stuid, astext(s.stulocation), 
                        st_within(s.stulocation, polyfromtext(('POLYGON((100 100, 300 100, 300 300, 100 300, 100 100))'))) 
                        from student s;
                        */
                        if(objectType.equals("student")) {
                            
                             finalquery = "select ob.stuid from student ob where st_within(ob.stulocation, polyfromtext(('POLYGON(("+lowerX+" "+lowerY+","+upperX+" "+lowerY+","+upperX+" "+upperY+","+lowerX+" "+upperY+","+lowerX+" "+lowerY+"))')))";
//                             System.out.println("Final Query :"+finalquery);
                             prestatement = connection.prepareStatement(finalquery);
                             resultset = prestatement.executeQuery();
                             while(resultset.next()) {
                                System.out.println(resultset.getString("stuid"));
                             }
                        }
                        else if(objectType.equals("building")) {
                             finalquery = "select ob.buil_id from building ob where st_within(ob.buil_location, polyfromtext(('POLYGON(("+lowerX+" "+lowerY+","+upperX+" "+lowerY+","+upperX+" "+upperY+","+lowerX+" "+upperY+","+lowerX+" "+lowerY+"))')))";
//                             System.out.println("Final Query :"+finalquery);
                             prestatement = connection.prepareStatement(finalquery);
                             resultset = prestatement.executeQuery();
                             while(resultset.next()) {
                                System.out.println(resultset.getString("buil_id"));
                             }
                        }
                        else if(objectType.equals("tramstop")) {
                             finalquery = "select ob.tramsid from tramstop ob where st_within(ob.tramstoploc, polyfromtext(('POLYGON(("+lowerX+" "+lowerY+","+upperX+" "+lowerY+","+upperX+" "+upperY+","+lowerX+" "+upperY+","+lowerX+" "+lowerY+"))')))";
//                             System.out.println("Final Query :"+finalquery);
                             prestatement = connection.prepareStatement(finalquery);
                             resultset = prestatement.executeQuery();
                             while(resultset.next()) {
                                System.out.println(resultset.getString("buil_id"));
                             }
                        }
                        else{
                            System.out.println("Wrong Object Type");
                            return;
                        }
                            
                        
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    break;
                    
                case 2:
                    try{
                        String studentId = args[1];
                        System.out.println("Student ID : "+studentId);
                        String distancearg = args[2];
                        System.out.println("Distance : "+distancearg);
                        /* Building
                        Selectb1.buil_id from building b1, student s

                         where s.stuid = 'p1' and ST_DISTANCE(b1.buil_location, s.stulocation) < '300' ;
                        */
                        
                       
                        finalquery = "select b1.buil_id from building b1, student s where s.stuid = '"+studentId+"' and ST_DISTANCE(b1.buil_location, s.stulocation) < '"+distancearg+"'";
//                        System.out.println("Final : "+finalquery);
                        prestatement = connection.prepareStatement(finalquery);
                        resultset = prestatement.executeQuery();
                        System.out.println("Buildings : ");
                             while(resultset.next()) {
                                System.out.println(resultset.getString("buil_id"));
                             }
                        System.out.println("TramStops : ");
                        
                        /*
                        Select t1.tramstid from student s, tramstop t1
where s.stuid = 'p1' and ST_DISTANCE(t1.tramstoploc, s.stulocation) < '300' ;
                        */
                        
                        finalquery = "select t1.tramstid from student s, tramstop t1 "
                                + "where s.stuid = '"+studentId+"' and ST_DISTANCE(t1.tramstoploc, s.stulocation) < '"+distancearg+"' " ;
                        
                        prestatement = connection.prepareStatement(finalquery);
                        resultset = prestatement.executeQuery();
                             while(resultset.next()) {
                                System.out.println(resultset.getString("tramstid"));
                             }
                        
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    
                    break;
                    
                case 3:
                    try{
                        
                        String objectType = args[1];
                        System.out.println("Object Type : "+objectType);
                        
                        String objectId = args[2];
                        System.out.println(" Object Id : "+objectId);
                        
                        String limit = args[3];
                        System.out.println("Limit is :"+limit);
                        
                        
                        if(objectType.equals("student"))
                        {
                             finalquery = "Select b2.stuid from student b1, student b2 "
                                    + "where b1.stuid= '"+objectId+"' and b2.stuid<>b1.stuid order by ST_distance(b1.stulocation,b2.stulocation) limit "+limit+";";
                            
                            prestatement = connection.prepareStatement(finalquery);
                            resultset = prestatement.executeQuery();
                             while(resultset.next()) {
                                System.out.println(resultset.getString("stuid"));
                             }
                            
                        }
                        else if(objectType.equals("building"))
                        {
                            finalquery = "Select b2.buil_id from building b1, building b2 "
                                    + "where b1.buil_id= '"+objectId+"' and b2.buil_id<>b1.buil_id order by ST_distance(b1.buil_location,b2.buil_location) limit "+limit+";";
                            
                            prestatement = connection.prepareStatement(finalquery);
                            resultset = prestatement.executeQuery();
                             while(resultset.next()) {
                                System.out.println(resultset.getString("buil_id"));
                             }
                        }
                        else if(objectType.equals("tramstop")){
                             finalquery = "Select b2.tramstid from tramstop b1, tramstop b2 "
                                    + "where b1.tramstid= '"+objectId+"' and b2.tramstid<>b1.tramstid order by ST_distance(b1.tramstoploc,b2.tramstoploc) limit "+limit+";";
                            
                            prestatement = connection.prepareStatement(finalquery);
                            resultset = prestatement.executeQuery();
                             while(resultset.next()) {
                                System.out.println(resultset.getString("tramstid"));
                             }
                        }
                        else
                        {
                            System.out.println("Invalid Object Type");
                            return;
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    
                    break;
                    
                case 4:
                    
                    if(args[1].equals("1")){
                         try {
                            connection = DriverManager.getConnection(connectionString, username, password);      
                        } catch(Exception ex)  {
                            ex.printStackTrace();
                            System.out.println("Couldn't make connection");
                            return;
                        }
                        
                        
                        
                        
                        //finalquery = "select s.stuid from student s, tramstop t where t.tramstid='t6ssl' and ST_DISTANCE(s.stulocation, t.tramstoploc) < t.tramradius ;";
                         
                         finalquery = "select s.stuid, b.buil_id from student s, tramstop t1, tramstop t2, building b where t1.tramstid='t2ohe' and t2.tramstid = 't6ssl' and ST_DISTANCE(s.stulocation, t1.tramstoploc) < t1.tramradius and st_distance(s.stulocation, t2.tramstoploc) <t2.tramradius; ";
                        
                         boolean found=false;
                        prestatement = connection.prepareStatement(finalquery);
                            resultset = prestatement.executeQuery();   
                            System.out.println("For Intersection of t2ohe and t6ssl: ");
                             while(resultset.next()) {
                                         System.out.println(resultset.getString("stuid"));
                             }     
                             if(!found) 
                                 System.out.println("Nothing Returned");
                    }
                    
                    else if(args[1].equals("2"))
                    {
                        Connection connection2;
                        ResultSet rs2;
                        PreparedStatement ps2;
                        
                         try {
                            connection2 = DriverManager.getConnection(connectionString, username, password);      
                        } catch(Exception ex)  {
                            ex.printStackTrace();
                            System.out.println("Couldn't make connection");
                            return;
                        }
                        
                        ArrayList<String> al = new ArrayList<>();
                        ps2 = connection2.prepareStatement("select stuid from student");
                        rs2 = ps2.executeQuery();
                        while(rs2.next()){
                            al.add(rs2.getString("stuid"));
                        }
                         
                         
                         for(int i=0;i<al.size();i++) {
                         finalquery = "Select t.tramstid from tramstop t, student s "
                                    + "where s.stuid= '"+al.get(i)+"' "
                                 + "order by ST_distance(s.stulocation, t.tramstoploc) limit 2 ;";
                            
                            prestatement = connection.prepareStatement(finalquery);
                            resultset = prestatement.executeQuery();
                            System.out.println("Student : "+al.get(i));
                             while(resultset.next()) {
                                System.out.println(resultset.getString("tramstid"));
                             }
                         }
                                
                    }
                    
                    
                    else if(args[1].equals("3"))
                    {
                        /*select count(*) from building b, tramstop t 
                        where t.tramstid = 't6ssl' and ST_DISTANCE(b.buil_location, t.tramstoploc) < '250' ; */
                        
                        try {
                            connection = DriverManager.getConnection(connectionString, username, password);      
                        } catch(Exception ex)  {
                            ex.printStackTrace();
                            System.out.println("Couldn't make connection");
                            return;
                        }
                        
                        
                        
                        finalquery = "select max(t.tramstid) as ma from building b, tramstop t "
                                + "where ST_DISTANCE(b.buil_location, t.tramstoploc) < 250" ;
                        
                        prestatement = connection.prepareStatement(finalquery);
                            resultset = prestatement.executeQuery();   
                             if(resultset.next()) {
                                 System.out.println(resultset.getString("ma"));
                             }
                    }
                    
                    
                else if(args[1].equals("4")){
                       Connection connection2;
                        ResultSet rs2;
                        PreparedStatement ps2;
                        
                         try {
                            connection2 = DriverManager.getConnection(connectionString, username, password);      
                        } catch(Exception ex)  {
                            ex.printStackTrace();
                            System.out.println("Couldn't make connection");
                            return;
                        }
                        
                        ArrayList<String> al = new ArrayList<>();
                        ps2 = connection2.prepareStatement("select buil_id from building");
                        rs2 = ps2.executeQuery();
                        while(rs2.next()){
                            al.add(rs2.getString("buil_id"));
                        }
                    
                        ArrayList<String> stual = new ArrayList<>();
                                
                        for(int i=0;i<al.size();i++) {
                         finalquery = "select s1.stuid from building b, student s1 where b.buil_id= '"+al.get(i)+"' "
                                 + "order by ST_distance(s1.stulocation, b.buil_location) limit 1";
                            
                            prestatement = connection.prepareStatement(finalquery);
                            resultset = prestatement.executeQuery();                
                             if(resultset.next()) {
                                stual.add(resultset.getString("stuid"));
                             }
                         }
                        
                        int arr[] = new int[80];
                        for(int z=0;z<79;z++){
                            arr[z]=0;
                        }
                        
                        
                        int max1=0, max2=0, max3=0, max4=0, max5=0;
                        String m1="", m2="", m3="", m4="", m5="";
                        
                        for(int j=0;j<stual.size();j++){
                            int x = Integer.parseInt(stual.get(j).substring(1));
                            arr[x] += 1;
                        }
                        
                        for(int z=0;z<arr.length;z++){
                            if(arr[z]>max1)
                            {   max1=arr[z];
                                m1 = z+"";
                            }
                            else if(arr[z]>max2)
                            {   max2=arr[z];
                                m2 = z+"";
                            }
                            else if(arr[z]>max3)
                            {   
                                max3=arr[z];
                                m3 =z+"";
                            }
                            else if(arr[z]>max4)
                            {
                                max4 = arr[z];
                                m4 = z+"";
                            }
                            else if(arr[z]>max5)
                            {
                                max5 = arr[z];
                                m5 = z+"";
                            }
                            else {}
                           }
                    
                        System.out.println("p"+m1+" "+max1);
                        System.out.println("p"+m2+" "+max2);
                        System.out.println("p"+m3+" "+max3);
                        System.out.println("p"+m4+" "+max4);
                        System.out.println("p"+m5+" "+max5);  
                }
                else if(args[1].equals("5")){
                     try {
                            connection = DriverManager.getConnection(connectionString, username, password);      
                        } catch(Exception ex)  {
                            ex.printStackTrace();
                            System.out.println("Couldn't make connection");
                            return;
                        }
                        
                        
                        
                        finalquery = "select asText(envelope(ST_UNION (polyfromtext('POLYGON((232 324,241 329,238 333,245 338,247 334,293 358,277 386,233 360,234 356,228 352,224 359,217 354,232 324))'), polyfromtext('POLYGON((309 371,325 379,333 365,349 374,342 388,357 396,346 416,331 409,324 422,307 413,313 399,298 391,309 371))')))) as ma;";
                        prestatement = connection.prepareStatement(finalquery);
                            resultset = prestatement.executeQuery();   
                             if(resultset.next()) {
                                 System.out.println(resultset.getString("ma"));
                             }
                }
                else
                        System.out.println("Not a valid fixed case");
                    
                    break;
                            
                    
                default:
                    return;    
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
 
    }
    
    
    
}
