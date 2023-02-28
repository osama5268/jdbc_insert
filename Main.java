package org.example;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
       // SystemParameter systemParameter=new SystemParameter();
       // ResultSet resultSet = systemParameter.systemPara();
       // while (resultSet.next()){
       //     // CustomerMaster customerMaster=new CustomerMaster();
       //     // if(customerMaster.checkFileExt(resultSet.getString(2))){
       //     //     File file=new File(resultSet.getString(1)+resultSet.getString(2));
       //     //     if(resultSet.getString(3).equals("F")){
       //     //         customerMaster.fileLevel(file);
       //     //     } else if (resultSet.getString(3).equals("R")) {
       //     //         customerMaster.readLevel(file);
       //     //         customerMaster.failedValidation();
       //     //     }
       //     //     else {
       //     //         System.out.println(resultSet.getString(2)+" Has invalid extension");
       //     //     }
       //     // }
       // }
       //  MyFile myFile=new MyFile();
        // myFile.writeInFile();
      CustomerMaster customerMaster=new CustomerMaster();
      //assuming filename filelevel will come in arguments
      if(customerMaster.checkFileExt(args[0])){
         if(args[1].equals("F"))
            customerMaster.fileLevel(args[0]);
         else if(args[1].equals("R"))
            customerMaster.readLevel(args[0]);
      }
    }
}