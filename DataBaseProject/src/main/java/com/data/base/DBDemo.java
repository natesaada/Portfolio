package com.data.base;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLParser;
import net.sf.jsqlparser.JSQLParserException;

public class DBDemo {

    /**
     * This DBDemo class simply shows that every Query can be properly executed.
     * A result set is returned, and in most cases, the result set is printed out for convenience.
     *
     * @param args
     */

    public static void main(String[] args) {
        try {

            SQLParser parser = new SQLParser();
            DataBase db1 = new DataBase();

            //Creating table
            System.out.println("Query: CREATE TABLE YCStudent\n" +
                    "                    (BannerID int, \n" +
                    "                    SSNum int UNIQUE , \n" +
                    "                    FirstName varchar(255), \n" +
                    "                    LastName varchar(255) NOT NULL , \n" +
                    "                    GPA decimal(1,2) DEFAULT 0.00, \n" +
                    "                    CurrentStudent boolean DEFAULT true, \n" +
                    "                    PRIMARY KEY (BannerID)); ");
            ResultSet resultSet = db1.execute(" CREATE TABLE YCStudent" +
                    "(\n" +
                    "BannerID int,\n" +
                    " SSNum int UNIQUE ,\n" +
                    "FirstName varchar(255) ,\n" +
                    "LastName varchar(255) NOT NULL ,\n" +
                    "GPA decimal(1,2) DEFAULT 0.00,\n" +
                    " CurrentStudent boolean DEFAULT true,\n" +
                    "PRIMARY KEY (BannerID)\n" +
                    "); ");
            resultSet.printResultSet();
            db1.printDataBase();



            System.out.println("Query: INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('nate', 'saada' ,4.0, 800449633, 098762323);");
            ResultSet resultSet1 = db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "BannerID, SSNum) VALUES ('nate', 'saada' ,4.0, 800449633, 098762323);");

            resultSet1.printResultSet();
            db1.printDataBase();

            System.out.println("Query:  INSERT INTO YCStudent (FirstName, LastName, GPA, SSNum, BannerID) VALUES ('avi','saada',3.5, 123654888, 800999877);");
            ResultSet resultSet2 = db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('avi','saada',3.5, 123654888, 800999877);");

            resultSet2.printResultSet();
            db1.printDataBase();

            System.out.println("Query: \"INSERT INTO YCStudent (FirstName, LastName, GPA, SSNum, BannerID) VALUES ('Jack','Mizrachi',2.00, 123654338, 800123456);\" ");
            ResultSet resultSet3 = db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('Jack','Mizrachi',2.00, 123654338, 800123456);");

            resultSet3.printResultSet();
            db1.printDataBase();

            System.out.println("Query:  INSERT INTO YCStudent (FirstName, LastName, GPA, SSNum, BannerID) VALUES ('avi','kalintsky',4.00, 609457856, 800333456);");
            ResultSet resultSet4 = db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('avi','kalintsky',4.00, 609457856, 800333456);");

            resultSet4.printResultSet();
            db1.printDataBase();

            System.out.println("Query:  INSERT INTO YCStudent (FirstName, LastName, GPA, SSNum, BannerID) VALUES ('juda','gold',1.00, 999457856, 800555456);");
            ResultSet resultSet5 = db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('juda','gold',1.00, 999457856, 800555456);");

            resultSet5.printResultSet();
            db1.printDataBase();

            System.out.println("Query: INSERT INTO YCStudent (FirstName, LastName, GPA, SNum, BannerID) VALUES ('Asher','gold',3.40, 777457856, 800554456);");
            ResultSet resultSet6= db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('Asher','gold',3.40, 777457856, 800554456);");

            resultSet6.printResultSet();
            db1.printDataBase();

            System.out.println("Query: INSERT INTO YCStudent (FirstName, LastName, SSNum, BannerID) VALUES ('jake','benaya', 770057856, 800554000);");
            ResultSet resultSet7= db1.execute("INSERT INTO YCStudent (FirstName, LastName," +
                    "SSNum, BannerID) VALUES ('jake','benaya', 770057856, 800554000);");

            resultSet7.printResultSet();
            db1.printDataBase();


            System.out.println("Query: INSERT INTO YCStudent (FirstName, LastName, GPA, SSNum, BannerID) VALUES ('avi','saada', 2.33, 990057856, 800999000);");
            ResultSet resultSet8= db1.execute("INSERT INTO YCStudent (FirstName, LastName," +
                    "GPA, SSNum, BannerID) VALUES ('avi','saada', 2.33, 990057856, 800999000);");

            resultSet8.printResultSet();
            db1.printDataBase();


            System.out.println("Query: INSERT INTO YCStudent (FirstName, LastName, GPA, SSNum, BannerID) VALUES ('avi','saada', 2.33, 990057777, 800999023);");
            ResultSet resultSet9= db1.execute("INSERT INTO YCStudent (FirstName, LastName," +
                    "GPA, SSNum, BannerID) VALUES ('avi','saada', 2.33, 990057777, 800999023);");
            resultSet9.printResultSet();
            db1.printDataBase();


            System.out.println("Query: SELECT FirstName, LastName FROM YCStudent;");
            ResultSet resultSet10 = db1.execute("SELECT FirstName, LastName FROM YCStudent;");

            resultSet10.printResultSet();



            System.out.println("Query: INSERT INTO YCStudent (FirstName, LastName, GPA, SSNum, BannerID) VALUES ('david','rosenberg',3.40, 773227856, 800223456);");
            ResultSet resultSet11= db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('david','rosenberg',3.40, 773227856, 800223456);");

            resultSet11.printResultSet();
            db1.printDataBase();

            System.out.println("Query: INSERT INTO YCStudent (FirstName, LastName, GPA, SSNum, BannerID, CurrentStudent) VALUES ('','brody',4.0, 709457856, 800004456, false);");
            ResultSet resultSet12= db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID, CurrentStudent) VALUES ('','brody',4.0, 709457856, 800004456, false);");

            resultSet12.printResultSet();
            db1.printDataBase();

            System.out.println("Query: INSERT INTO YCStudent (FirstName, LastName, GPA, SSNum, BannerID) VALUES ('Zayne','solomon',2.40, 777452326, 800550056);");
            ResultSet resultSet13= db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('Zayne','solomon',2.40, 777452326, 800550056);");

            resultSet13.printResultSet();
            db1.printDataBase();



            System.out.println("*********************************************************************************************************************************");
            System.out.println("Query: SELECT DISTINCT GPA FROM YCStudent WHERE FirstName < 'jack';");
            ResultSet resultSet14 = db1.execute("SELECT DISTINCT GPA FROM YCStudent WHERE FirstName < 'jack' ORDER BY GPA ASC;");
            resultSet14.printResultSet();


            System.out.println("*********************************************************************************************************************************");
            System.out.println("Query:  ISSUE SELECT FirstName, AVG(GPA), COUNT(FirstName) FROM YCStudent;");
            ResultSet resultSet15 = db1.execute("SELECT FirstName, AVG(GPA), COUNT(FirstName) FROM YCStudent ORDER BY FirstName ASC;");
            resultSet15.printResultSet();

            System.out.println("*********************************************************************************************************************************");
            System.out.println("Query: SELECT * FROM YCStudent;");
            ResultSet resultSet16 = db1.execute("SELECT * FROM YCStudent;");
            resultSet16.printResultSet();

            System.out.println("*********************************************************************************************************************************");
            System.out.println("Query: SELECT DISTINCT GPA FROM YCStudent ORDER BY GPA ASC, FirstName DESC;;");
            ResultSet resultSet17 = db1.execute("SELECT DISTINCT GPA FROM YCStudent ORDER BY GPA ASC, FirstName DESC;");
            resultSet17.printResultSet();

            System.out.println("*********************************************************************************************************************************");
            System.out.println("Query: SELECT FirstName, LastName FROM YCStudent ORDER BY FirstName ASC, GPA DESC;");
            ResultSet resultSet18 = db1.execute("SELECT FirstName, LastName, GPA FROM YCStudent ORDER BY FirstName ASC, GPA DESC;");
            resultSet18.printResultSet();


            System.out.println("*********************************************************************************************************************************");
            System.out.println("Query: SELECT * FROM YCStudent;");
            ResultSet resultSet19 = db1.execute("SELECT * FROM YCStudent;");
            resultSet19.printResultSet();

            System.out.println("*********************************************************************************************************************************");
            System.out.println("Query: SELECT DISTINCT FirstName, LastName FROM YCStudent WHERE GPA<3.7 AND (LastName>='brody' OR CurrentStudents=true) ORDER BY FirstName ASC;");
            ResultSet resultSet20 = db1.execute("SELECT DISTINCT FirstName, LastName FROM YCStudent WHERE GPA<3.7\n" +
                    " AND (LastName>='brody' OR CurrentStudent=true) ORDER BY FirstName ASC;");
            resultSet20.printResultSet();


            System.out.println("*********************************************************************************************************************************");
            System.out.println("Query: CREATE INDEX GPA_Index on YCStudent (GPA) ");
            ResultSet resultSet21 = db1.execute("CREATE INDEX GPA_Index on YCStudent (GPA);");
            resultSet21.printResultSet();
            System.out.println("*********************************************************************************************************************************");


            System.out.println("Query:  INSERT INTO YCStudent (FirstName, LastName, GPA, SSNum, BannerID) VALUES ('juda','gold',2.4.00, 990000856, 899954566);");
            ResultSet resultSet22 = db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('juda','gold',2.4, 990000856, 899954566);");

            resultSet22.printResultSet();
            db1.printDataBase();

            System.out.println("Query: INSERT INTO YCStudent (FirstName, LastName, GPA, SNum, BannerID) VALUES ('Asher','zubi',3.40, 700057856, 800559898;");
            ResultSet resultSet23= db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('Asher','zubi',3.40, 700057856, 800559898);");

            resultSet23.printResultSet();
            db1.printDataBase();

            System.out.println("Query: INSERT INTO YCStudent (FirstName, LastName, SSNum, BannerID) VALUES ('jake','benaya', 770111856, 8005540991);");
            ResultSet resultSet24= db1.execute("INSERT INTO YCStudent (FirstName, LastName," +
                    "SSNum, BannerID) VALUES ('jake','benaya', 770111856, 800554099);");

            resultSet24.printResultSet();
            db1.printDataBase();

            System.out.println("*********************************************************************************************************************************");
            System.out.println("Query: SELECT gpa FROM YCStudent WHERE GPA < 3.0;");
            ResultSet resultSet25 = db1.execute("SELECT gpa FROM YCStudent WHERE GPA < 3.0;");
            resultSet25.printResultSet();

            System.out.println("*********************************************************************************************************************************");
            ResultSet resultSet26 = db1.execute("CREATE INDEX FirstName_Index on YCStudent (FirstName);");
            resultSet26.printResultSet();

            System.out.println("*********************************************************************************************************************************");
            ResultSet resultSet27 = db1.execute("UPDATE YCStudent SET GPA = 2.3 WHERE FirstName ='nate' OR FirstName = 'Asher';");
            resultSet27.printResultSet();

            System.out.println("***************************************************************************************");
            System.out.println("Query: SELECT * FROM YCStudent ORDER BY FirstName ASC, GPA DESC; ");
            System.out.println();
            ResultSet resultSet28 =  db1.execute("SELECT * FROM YCStudent ORDER BY FirstName ASC, GPA DESC;");
            resultSet28.printResultSet();

            System.out.println("***************************************************************************************");
            System.out.println("Query: DELETE FROM YCStudent WHERE FirstName = 'Jack' OR FirstName = 'jake';");
            System.out.println();
            ResultSet resultSet29 = db1.execute("DELETE FROM YCStudent WHERE FirstName = 'Jack' OR FirstName = 'jake';");
            resultSet29.printResultSet();


            System.out.println("***************************************************************************************");
            System.out.println("Query: DELETE FROM YCStudent WHERE GPA < 2.3 OR FirstName > 'juda'");
            ResultSet resultSet30 = db1.execute("DELETE FROM YCStudent WHERE GPA < 2.3 OR FirstName > 'juda';");
            resultSet30.printResultSet();
            db1.printDataBase();


            System.out.println("Query: SELECT FirstName, LastName, GPA, AVG(GPA), SUM(GPA), COUNT(FirstName), MIN(GPA), MAX (GPA) FROM YCStudent");
            ResultSet resultSet31 = db1.execute("SELECT FirstName, LastName, GPA, AVG(GPA), SUM(GPA), COUNT(FirstName), MIN(GPA), MAX (GPA) FROM YCStudent ORDER BY LastName ASC, GPA DESC;");
            resultSet31.printResultSet();

            System.out.println("*********************************************************************************************************************************");
            System.out.println("Query: DELETE FROM YCStudent");
            ResultSet resultSet32 = db1.execute("DELETE FROM YCStudent;");
            resultSet32.printResultSet();

            db1.printDataBase();

            System.out.println("Query:  INSERT INTO YCStudent (FirstName, LastName, GPA, SSNum, BannerID) VALUES ('avi','kalintsky',4.00, 609457856, 800333456);");
            ResultSet resultSet33 = db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('avi','kalintsky',4.00, 609457856, 800333456);");

            resultSet33.printResultSet();
            db1.printDataBase();

            System.out.println("Query:  INSERT INTO YCStudent (FirstName, LastName, GPA, SSNum, BannerID) VALUES ('juda','gold',1.00, 999457856, 800555456);");
            ResultSet resultSet34 = db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('juda','gold',1.00, 999457856, 800555456);");

            resultSet34.printResultSet();
            db1.printDataBase();


            System.out.println("Query: INSERT INTO YCStudent (FirstName, LastName, GPA, SNum, BannerID) VALUES ('Asher','gold',3.40, 777457856, 800554456);");
            ResultSet resultSet35= db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('Asher','gold',3.40, 777457856, 800554456);");
            resultSet35.printResultSet();
            db1.printDataBase();


            System.out.println("Query: INSERT INTO YCStudent (FirstName, LastName, GPA, SNum, BannerID) VALUES ('menachem','scharf',3.80, 777444456, 800553336);");
            ResultSet resultSet36= db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('menachem','scharf',3.80, 777444456, 800553336);");
            resultSet36.printResultSet();
            db1.printDataBase();


            System.out.println("Query: INSERT INTO YCStudent (FirstName, LastName, GPA, SNum, BannerID) VALUES ('rocky','abraham', 2.8, 777449936, 800429757);");
            ResultSet resultSet37= db1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                    "SSNum, BannerID) VALUES ('rocky','abraham', 2.8, 777449936, 800429757);");
            resultSet37.printResultSet();
            db1.printDataBase();



            System.out.println("Query: SELECT FirstName, LastName, GPA FROM YCStudent WHERE FirstName > 'avi'");
            ResultSet resultSet38 = db1.execute("SELECT FirstName, LastName, GPA FROM YCStudent WHERE FirstName > 'avi' ORDER BY FirstName DESC;");
            resultSet38.printResultSet();


        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

    }
}
