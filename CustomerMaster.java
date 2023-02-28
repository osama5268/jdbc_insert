package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.sql.*;
import java.util.*;

public class CustomerMaster {
//FileDetails16906
//CustomerMaster16906
    private Connection connection;
    private Statement statement;
    private HashMap<ArrayList<String>,String> rec=new HashMap<>();
    private String columns [];
    HashMap<String,String> monthMap = new HashMap <String, String> ();
    HashMap<String,String> env = new HashMap<String, String> ();

    public CustomerMaster() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
        // Class.forName("oracle.jdbc.driver.OracleDriver");
        Class.forName("com.mysql.jdbc.Driver");
        
        FileInputStream fstream = new FileInputStream(".env");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        while((strLine = br.readLine()) != null){
            String splitStr[] = strLine.split("=");
            env.put(splitStr[0].trim(), splitStr[1].trim());
        }
        // connection = DriverManager.getConnection("jdbc:oracle:thin:@" + env.get("DB_HOST") + ":" + env.get("DB_PORT") + "/" + env.get("DB_USER"), env.get("DB_USER"), env.get("DB_PASSWORD"));
        connection = DriverManager.getConnection("jdbc:mysql://"+ env.get("DB_HOST")+ ":" + env.get("DB_PORT") +"/"+ env.get("DB_NAME"), env.get("DB_USER"), env.get("DB_PASSWORD"));
        statement = connection.createStatement();
        columns = new String[]{"CUSTOMER_CODE", "CUSTOMER_NAME", "CUSTOMER_ADDRESS1", "CUSTOMER_ADDRESS2", "CUSTOMER_PIN_CODE", "EMAIL_ADDRESS", "CONTACT_NUMBER", "PRIMARY_CONTACT_PERSON", "RECORD_STATUS", "FLAG", "CREATE_DATE", "CREATED_BY", "MODIFIED_DATE", "MODIFIED_BY", "AUTHORIZED_DATE", "AUTHORIZED_BY"};
        monthMap.put("Jan", "01");
        monthMap.put("Feb", "02");
        monthMap.put("Mar", "03");
        monthMap.put("Apr", "04");
        monthMap.put("May", "05");
        monthMap.put("Jun", "06");
        monthMap.put("Jul", "07");
        monthMap.put("Aug", "08");
        monthMap.put("Sep", "09");
        monthMap.put("Oct", "10");
        monthMap.put("Nov", "11");
        monthMap.put("Dec", "12");
    }

    public boolean checkFileExt(String fileName){
        int i=fileName.lastIndexOf('.');
        if(i>0){
            String ext=fileName.substring(i+1);
            if(ext.equals("txt"))
                return true;
        }
        return false;
    }

    public boolean checkRecordValidation(ArrayList<String> customerMaster){
        String str1[]={"N","M","D","A","R"};
        String str2[]={"A","I"};
        char[] specialCharacter={'+','-', '&', '|', '!', '(', ')', '{', '}', '[', ']', '^',
                '~', '*', '?', ':','!','@','#','$','%'};

        if(!DataValidation.dataTypeValidation(customerMaster.get(4),"Numeric")){
            rec.put(customerMaster,"Data Type must be numeric");
            return false;
        }

        if(!DataValidation.dataTypeValidation(customerMaster.get(6),"Numeric")){
            rec.put(customerMaster,"Data Type must be numeric");
            return false;
        }

        if(!DataValidation.dataLength(customerMaster.get(4),6)){
            rec.put(customerMaster,"Pin code should be 6 digit");
            return false;
        }

        if(customerMaster.get(0).equals("")&&customerMaster.get(1).equals("")){
            rec.put(customerMaster,"Mandatory fields are empty");
            return false;
        }

        if(customerMaster.get(5).equals("")&&customerMaster.get(7).equals("")){
            rec.put(customerMaster,"Mandatory fields are empty");
            return false;
        }

        if(customerMaster.get(8).equals("")&&customerMaster.get(9).equals("")){
            rec.put(customerMaster,"Mandatory fields are empty");
            return false;
        }

        if(customerMaster.get(10).equals("")&&customerMaster.get(11).equals("")){
            rec.put(customerMaster,"Mandatory fields are empty");
            return false;
        }

        if(!DataValidation.validateEmail(customerMaster.get(5))){
            rec.put(customerMaster,"Entered email is not valid");
        }

        if(!DataValidation.domainValue(customerMaster.get(8),str1)){
            rec.put(customerMaster,"Status is not valid");
            return false;
        }

        if(!DataValidation.domainValue(customerMaster.get(9),str2)){
            rec.put(customerMaster,"Flag is not valid");
            return false;
        }
        return true;
    }
    private String convert_date(String str){
        String [] splitStr = str.split("/");
        return splitStr[2] + "-" + monthMap.get(splitStr[1]) + "-" + splitStr[0];
    }
    public void fileLevel(String fileName){
        try{
            FileInputStream fstream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            ArrayList list = new ArrayList();
            while ((strLine = br.readLine()) != null){
                list.add(strLine);
            }
            Iterator itr;
            for (itr = list.iterator(); itr.hasNext();){
                String str = itr.next().toString();
                String [] splitStr = str.split("~");
                if(!checkRecordValidation(new ArrayList<>(Arrays.asList(splitStr)))){
                    System.out.println("Invalid Records! Aborting!!!");
                    return;
                }
            }
            for(itr = list.iterator(); itr.hasNext();){
                String str = itr.next().toString();
                String [] splitStr = str.split("~");
                StringBuffer col = new StringBuffer();
                StringBuffer val = new StringBuffer();
                for(int i = 0; i < splitStr.length; i++){
                    if(splitStr[i] == "")
                        continue;
                    if(i == 10 || i == 12 || i == 14){
                        splitStr[i] = convert_date(splitStr[i]);
                    }
                    col.append(columns[i]);
                    if (i != splitStr.length - 1){
                        val.append("'" + splitStr[i] + "',");
                        col.append(",");
                    }
                    else
                        val.append("'" + splitStr[i] + "')");
                }
                String query = "INSERT INTO CUSTOMERMASTER(" + col.toString() + ") values (" + val.toString();
                System.out.println(query);
                int k = statement.executeUpdate(query);

            }
            fstream.close();
        } catch (Exception e) {
            try{
                statement.execute("commit");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        }
    }
    public void readLevel(String fileName){
        try{
            FileInputStream fstream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            ArrayList list = new ArrayList();
            while ((strLine = br.readLine()) != null){
                list.add(strLine);
            }
            Iterator itr;
            for(itr = list.iterator(); itr.hasNext();){
                String str = itr.next().toString();
                String [] splitStr = str.split("~");
                StringBuffer col = new StringBuffer();
                StringBuffer val = new StringBuffer();
                if(checkRecordValidation(new ArrayList<>(Arrays.asList(splitStr)))){
                    for(int i = 0; i < splitStr.length; i++){
                        if(splitStr[i] == "")
                            continue;
                        if(i == 10 || i == 12 || i == 14){
                            splitStr[i] = convert_date(splitStr[i]);
                        }
                        col.append(columns[i]);
                        if (i != splitStr.length - 1){
                            val.append("'" + splitStr[i] + "',");
                            col.append(",");
                        }
                        else
                            val.append("'" + splitStr[i] + "')");
                    }
                    String query = "INSERT INTO CUSTOMERMASTER(" + col.toString() + ") values (" + val.toString();
                    System.out.println(query);
                    int k = statement.executeUpdate(query);
                }

            }
            fstream.close();
        } catch (Exception e) {
            try{
                statement.execute("commit");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        }

    }
    public void failedValidation(){
        System.out.println("Records which are not inserted");
        Iterator i= rec.entrySet().iterator();
        while (i.hasNext()){
            Map.Entry<ArrayList<String>,String> e= (Map.Entry<ArrayList<String>,String>) i.next();
            System.out.println(e.getKey().toString()+ " Message = "+ e.getValue());
        }
    }

}