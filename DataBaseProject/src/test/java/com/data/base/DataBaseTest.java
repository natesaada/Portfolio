package com.data.base;

import net.sf.jsqlparser.JSQLParserException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


/**
 * This JUNIT tests that the query and methods in my code work.
 *
 * NOTE 1: Many of the test codes in this JUNIT test MULTIPLE things at once.
 * NOTE 2: Some test methods print our the table too in order to visualize the table and understand the test
 *
 */

public class DataBaseTest {
    DataBase dataBase = new DataBase();


    /**
     * Helper method to speed the testing process
     * @throws JSQLParserException
     */
    public void createTable() throws JSQLParserException {
        dataBase.execute(" CREATE TABLE YCStudent\n" +
                "(\n" +
                "BannerID int,\n" +
                " SSNum int UNIQUE ,\n" +
                "FirstName varchar(255) ,\n" +
                "LastName varchar(255) NOT NULL ,\n" +
                "GPA decimal(1,2) DEFAULT 0.00,\n" +
                " CurrentStudent boolean DEFAULT true,\n" +
                "PRIMARY KEY (BannerID)\n" +
                "); ");
    }

    /**
     * Helper method to speed the testing process
     * @throws JSQLParserException
     */

    public void createRows() throws JSQLParserException { // note that GPA and FirstName is INDEXED upon creating the rows

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('yehuda','shams',3.5, 1, 123456888);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, " +
                "BannerID, SSNum) VALUES ('avi','saada', 2, 123654888);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('ploni','almoni',2.2, 3, 505050505);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('leal','zen', null, 4, 123456799);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('nate', 'saada' ,2.0, 5, 123451234);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('mark','cohen', 2.9, 6, 555556799);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('jacob', 'yellin' ,3.0, 7, 111151234);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('gabriel', 'yen' ,4.0, 8, 123451114);");

        dataBase.execute("CREATE INDEX GPA_Index on YCStudent (GPA); ");
        dataBase.execute("CREATE INDEX FirstName_Index on YCStudent (FirstName);");

    }

    /**
     * Helper method to speed the testing process
     * @throws JSQLParserException
     */

    public void createRows2() throws JSQLParserException {
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('yehuda','shams',3.5, 1, 123456888);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, " +
                "BannerID, SSNum) VALUES ('avi','saada', 2, 123654888);"); // can the primary key be NULL?
        // Must it always be inserted?

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('ploni','almoni',2.2, 3, 505050505);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('leal','zen', null, 4, 123456799);"); //cant make GPA equal to null

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('nate', 'saada' ,2.0, 5, 123451234);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('mark','cohen', 2.9, 6, 555556799);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('jacob', 'yellin' ,3.0, 7, 111151234);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('gabriel', 'yen' ,4.0, 8, 123451114);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('', 'ovadia' ,1.7, 18, 112345234);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('JZ', 'band' ,4.0, 19, 191981234);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('john', 'wick' ,1.8, 20, 124070714);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('calvin', 'klien' ,1.9, 21, 12240404);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('larry', 'shwab' ,3.8, 22, 09091234);");


    }

    /**
     * Helper method to speed the testing process
     * @throws JSQLParserException
     */

    public void createRowsForOrderBy() throws JSQLParserException {

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('yehuda','shams',3.5, 1, 123456888);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, " +
                "BannerID, SSNum) VALUES ('avi','saada', 2, 123654888);");


        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('ploni','almoni',2.2, 3, 505050505);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('leal','zen', null, 4, 123456799);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('nate', 'saada' ,2.0, 5, 123451234);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('mark','cohen', 2.9, 6, 555556799);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('jacob', 'yellin' ,3.0, 7, 111151234);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('gabriel', 'yen' ,3.0, 8, 123451114);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('jacob', 'yellin' ,2.0, 9, 111001234);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('gabriel', 'zowers' ,4.0, 10, 123951114);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('nate', 'yellin' ,1.0, 11, 114444234);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('nate', 'yellin' ,4.0, 12, 12121114);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('gabriel', 'zowers' ,3.0, 10, 123951114);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('nate', 'yellin' ,1.0, 11, 119910434);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('nate', 'yellin' ,4.0, 12, 121210908);");


        dataBase.execute("CREATE INDEX GPA_Index on YCStudent (GPA); ");
        dataBase.execute("CREATE INDEX FirstName_Index on YCStudent (FirstName);");
    }

    /**
     * Helper method to speed the testing process
     * @throws JSQLParserException
     */

    public void createDistinctRows() throws JSQLParserException {


        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, " +
                "SSNum, BannerID) VALUES ('avi','saada', 123654888, 4);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, " +
                "SSNum, BannerID) VALUES ('avi','saada', 123699888, 5);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, " +
                "SSNum, BannerID) VALUES ('avi','jacobi', 123654008, 6);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('gabriel', 'yen' ,4.0, 8, 123451114);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('', 'ovadia' ,1.7, 18, 112345234);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('JZ', 'band' ,4.0, 19, 191981234);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('carl', 'wick' ,1.8, 20, 124070714);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('nate', 'band' ,4.0, 21, 191981333);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('carl', 'wick' ,1.8, 22, 124070333);");

    }




    /**
     * creating a table and testing to see that the row we inserted is in fact valid and complete
     *
     * @throws JSQLParserException
     */
    @Test
    public void createTableTestAndInsertTest() throws JSQLParserException {
        createTable();
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('yehuda','shams',3.5, 1, 123456888);");


        Row row = dataBase.findTable("YCStudent").getTable().get(0); // gets the first row
        int index1 = dataBase.findTable("YCStudent").getColumnIndex("FirstName");
        int index2 = dataBase.findTable("YCStudent").getColumnIndex("LastName");
        int index3 = dataBase.findTable("YCStudent").getColumnIndex("GPA");
        int index4 = dataBase.findTable("YCStudent").getColumnIndex("BannerID");
        int index5 = dataBase.findTable("YCStudent").getColumnIndex("SSNum");

        assertTrue(dataBase.getDb().size() == 1);
        assertTrue(row.getRow().get(index1).equalsIgnoreCase("'yehuda'"));
        assertTrue(row.getRow().get(index2).equalsIgnoreCase("'shams'"));
        assertTrue(row.getRow().get(index3).equals("3.50"));
        assertTrue(row.getRow().get(index4).equals("1"));
        assertTrue(row.getRow().get(index5).equals("123456888"));
        assertTrue(dataBase.getDb().get(0).toString().equals(dataBase.findTable("YCStudent").toString()));
    }


    /**
     * testing that a row we want deleted is in fact deleted
     *
     * @throws JSQLParserException
     */

    @Test
    public void testingDelete() throws JSQLParserException {
        createTable();
        createRows();

        Row row = dataBase.findTable("YCStudent").getTable().get(0);
        dataBase.execute("DELETE FROM YCStudent WHERE BannerID=1;");

        for (Row row2 : dataBase.findTable("YCStudent").getTable()) {
            assertFalse(row2.toString().equals(row.toString())); //
        }
        assertEquals(dataBase.findTable("YCStudent").getTable().size(), 7);
    }


    /**
     * test method deletes every row from the table leaving the size equal to zero
     *
     * @throws JSQLParserException
     */
    @Test
    public void deleteEverything() throws JSQLParserException {
        createTable();
        createRows();
        dataBase.execute("DELETE FROM YCStudent;");

        assertEquals(dataBase.findTable("YCStudent").getTable().size(), 0);
    }


    /**
     * testing to see if the rows with first names greater than paul are there or not
     * we are expecting them to NOT be there.
     *
     * @throws JSQLParserException
     */

    @Test
    public void deleteMultipleRows() throws JSQLParserException {
        createTable();
        createRows();


        Row r = dataBase.findTable("YCStudent").getTable().get(0);

        dataBase.execute("DELETE FROM YCStudent WHERE FirstName > 'paul';");
        assertEquals(dataBase.findTable("YCStudent").getTable().size(), 6);
        for (Row row : dataBase.findTable("YCStudent").getTable()) {
            assertFalse(row.getRow().equals(r.getRow()));
        }
    }


    /**
     * Testing to see that a row was given the proper update to both the table and the tree
     *
     * @throws JSQLParserException
     */

    @Test
    public void updateTableAndTree() throws JSQLParserException {
        createTable();
        createRows();

        dataBase.execute("UPDATE YCStudent SET GPA=2.22 WHERE BannerID = 3");
        Row row = dataBase.findTable("YCStudent").getTable().get(2);
        int index = dataBase.findTable("YCStudent").getColumnIndex("GPA");

        assertEquals(row.getRow().get(index), "2.22");

        //Shows that the row was changed in the BannerID Tree
        ArrayList<Row> al = dataBase.findTable("YCStudent").bTreeContainer.get(0).get("3");
        assertTrue(al.get(0).getRow().get(1).equalsIgnoreCase("2.22"));

        //Shows that the targeted row was ALSO updated in the "FirstName" BTree
        ArrayList<Row> al2 = dataBase.findTable("YCStudent").bTreeContainer.get(2).get("'ploni'");
        assertTrue(al2.get(0).getRow().get(1).equalsIgnoreCase("2.22"));
    }

    @Test
    public void assertFalseUpdateRowsInTable() throws JSQLParserException {
        createTable();
        createRows2();

        ResultSet rs = dataBase.execute("UPDATE YCStudent SET BannerID=30, FirstName='super';");
        int index = dataBase.findTable("ycstudent").getColumnIndex("BannerID");

        for (int i = 0; i < dataBase.findTable("ycstudent").table.size(); i++){
            // since BannerId is the primary key, we wont be able to set every row to have the same one
            assertFalse(dataBase.findTable("ycstudent").getTable().get(i).getRow().get(index).equals(30));

        }
    }




    /**
     * testing that every row in the table has been given a gpa value of 2.22
     * @throws JSQLParserException
     */
    @Test
    public void updateEntireTable() throws JSQLParserException{

        createTable();
        createRows();

        dataBase.execute("UPDATE YCStudent SET GPA=2.22");
        int index = dataBase.findTable("YCStudent").getColumnIndex("GPA");

        for(Row row: dataBase.findTable("YCStudent").getTable()){
           assertEquals(row.getRow().get(index), "2.22");
        }
    }




    /**
     * checks to see if the proper row was selected
     * @throws JSQLParserException
     */
    @Test
    public void selectRowFromTable() throws JSQLParserException{
        createTable();

    dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
            "BannerID, SSNum) VALUES ('gabriel', 'yen' ,4.0, 8, 123451114);");


        int index = dataBase.findTable("YCStudent").getColumnIndex("BannerID");
        ResultSet rs = dataBase.execute("SELECT BannerID FROM YCStudent WHERE gpa >= 3.0 ;");

        Integer i = Integer.parseInt(dataBase.findTable("YCStudent").getTable().get(0).getRow().get(index));
         assertTrue(i.equals((rs.getRows()).get(0).row.get(0)));

    }

    /**
     * This test method checks to see that three correct rows are being returned from this complex select/where query
     * @throws JSQLParserException
     */

    @Test
    public void selectRowsWithComplicatedWhere() throws JSQLParserException{
        createTable();
        createRows2(); // bigger table

        ResultSet rs = dataBase.execute("SELECT FirstName, LastName, GPA FROM YCStudent WHERE gpa>3.0\n" +
                " AND (BannerID>10 OR LastName>'wick');");

        int index1 = dataBase.findTable("YCStudent").getColumnIndex("FirstName");
        int index2 = dataBase.findTable("YCStudent").getColumnIndex("LastName");
        int index3 = dataBase.findTable("YCStudent").getColumnIndex("GPA");

        Row r = dataBase.findTable("YCStudent").getTable().get(7);
        for (int i = 0; i < r.getRow().size(); i++){
           assertTrue(r.getRow().get(index1).equals(rs.getRows().get(0).row.get(0)));
           assertTrue(r.getRow().get(index2).equals(rs.getRows().get(0).row.get(1)));

           Double gpa = Double.parseDouble(r.getRow().get(index3));
           assertTrue(gpa.equals(rs.getRows().get(0).row.get(2)));
        }

        Row r1 = dataBase.findTable("YCStudent").getTable().get(9);
        for (int i = 0; i < r1.getRow().size(); i++){
            assertTrue(r1.getRow().get(index1).equals(rs.getRows().get(1).row.get(0)));
            assertTrue(r1.getRow().get(index2).equals(rs.getRows().get(1).row.get(1)));

            Double gpa = Double.parseDouble(r1.getRow().get(index3));
            assertTrue(gpa.equals(rs.getRows().get(1).row.get(2)));
        }

        Row r2 = dataBase.findTable("YCStudent").getTable().get(12);
        for (int i = 0; i < r2.getRow().size(); i++){
            assertTrue(r2.getRow().get(index1).equals(rs.getRows().get(2).row.get(0)));
            assertTrue(r2.getRow().get(index2).equals(rs.getRows().get(2).row.get(1)));

            Double gpa = Double.parseDouble(r2.getRow().get(index3));
            assertTrue(gpa.equals(rs.getRows().get(2).row.get(2)));
        }

    }




    /**
     * Testing to see if the first name and last name are properly selected from the subtable returned from the where
     * @throws JSQLParserException
     */
    @Test
    public void selectMultipleRows() throws JSQLParserException{
        createTable();
        createRows();

        ResultSet rs = dataBase.execute("SELECT FirstName, LastName FROM YCStudent WHERE gpa >= 3.0 OR LastName > 'wang' ;");
        int index1 = dataBase.findTable("YCStudent").getColumnIndex("FirstName");
        int index2 = dataBase.findTable("YCStudent").getColumnIndex("LastName");

        for (int i=0; i <rs.getRows().size(); i++){
           String FirstName = rs.getRows().get(i).row.get(0).toString();
            for (int j = 0; j < dataBase.findTable("YCStudent").getTable().size() ; j++) {
                if(dataBase.findTable("YCStudent").getTable().get(j).getRow().get(0).equals(FirstName)){
                    assertTrue(dataBase.findTable("YCStudent").getTable().get(j).getRow().get(0).equals(FirstName));
                }
            }
        }

        for (int i=0; i <rs.getRows().size(); i++){
            String LastName = rs.getRows().get(i).row.get(1).toString();
            for (int j = 0; j < dataBase.findTable("YCStudent").getTable().size() ; j++) {
                if(dataBase.findTable("YCStudent").getTable().get(j).getRow().get(1).equals(LastName)){
                    assertTrue(dataBase.findTable("YCStudent").getTable().get(j).getRow().get(1).equals(LastName));
                }
            }
        }

       // Printing result set to better show results
        System.out.println("**************************************************************************");
        System.out.println("Test:  selectMultipleRows: ");
        System.out.println("Query: SELECT FirstName, LastName FROM YCStudent WHERE gpa >= 3.0 OR LastName > 'wang' ;");
        System.out.println();
        rs.printResultSet();
        dataBase.findTable("YCStudent").printTable();

    }



    /**
     * this test test that we get all the values requested that are less than FirstName 'nate' and we order the values by first name ASC
     * @throws JSQLParserException
     */

    @Test
    public void testingLessThanAndOrderBy() throws JSQLParserException{
        createTable();
        createRows2();

        ResultSet rs = dataBase.execute("SELECT FirstName, LastName, AVG(GPA), MAX(GPA) FROM YCStudent WHERE FirstName < 'nate' ORDER BY FirstName ASC;");

        // printing to better visualize
        System.out.println("****************************************************************************************");
        System.out.println("Test: testingLessThan");
        System.out.println("Query:  SELECT FirstName, LastName, AVG(GPA), MAX(GPA) FROM YCStudent WHERE FirstName < 'nate' ORDER BY FirstName ASC;");
        System.out.println();
        rs.printResultSet();
        dataBase.findTable("YCStudent").printTable();



    }


    /**
     * Test shows we are receiving the correct distinct values
     * @throws JSQLParserException
     */


    @Test
    public void selectDistinct() throws JSQLParserException{
        createTable();
        createDistinctRows();

        ResultSet rs = dataBase.execute("SELECT DISTINCT FirstName FROM YCStudent;");
        assertEquals(rs.getRows().size(), 6);
        assertTrue(rs.getRows().get(1).row.get(0).equals("'gabriel'")); //shows that the second row in subtable is gabriel, not avi again
    }

    /**
     * Test method tests to see that there are only 4 rows with distinct firstName, LastName
     * @throws JSQLParserException
     */

    @Test
    public void selectDistinctWithWhere() throws JSQLParserException{
        createTable();
        createDistinctRows();

        ResultSet rs = dataBase.execute("SELECT DISTINCT FirstName, LastName FROM YCStudent WHERE FirstName < 'dave';");

        assertEquals(rs.getRows().size(), 4);

        int index1 = dataBase.findTable("YCStudent").getColumnIndex("FirstName");
        int index2 = dataBase.findTable("YCStudent").getColumnIndex("LastName");


        Row r = dataBase.findTable("YCStudent").getTable().get(0);
        for (int i = 0; i < r.getRow().size(); i++){
            assertTrue(r.getRow().get(index1).equals(rs.getRows().get(0).row.get(0)));
            assertTrue(r.getRow().get(index2).equals(rs.getRows().get(0).row.get(1)));

        }

        Row r1 = dataBase.findTable("YCStudent").getTable().get(2);
        for (int i = 0; i < r1.getRow().size(); i++){
            assertTrue(r1.getRow().get(index1).equals(rs.getRows().get(1).row.get(0)));
            assertTrue(r1.getRow().get(index2).equals(rs.getRows().get(1).row.get(1)));

        }

        Row r2 = dataBase.findTable("YCStudent").getTable().get(4);
        for (int i = 0; i < r2.getRow().size(); i++){
            String noValue ="";
            if(r2.getRow().get(index1).equalsIgnoreCase("null")){
                 noValue= null;
            }
            assertTrue(noValue==(rs.getRows().get(2).row.get(0)));
            assertTrue(r2.getRow().get(index2).equals(rs.getRows().get(2).row.get(1)));

        }

        Row r3 = dataBase.findTable("YCStudent").getTable().get(6);
        for (int i = 0; i < r3.getRow().size(); i++){
            assertTrue(r3.getRow().get(index1).equals(rs.getRows().get(3).row.get(0)));
            assertTrue(r3.getRow().get(index2).equals(rs.getRows().get(3).row.get(1)));

        }

        // printing result set and table to visually understand what is happening
        System.out.println("****************************************************************************************");
        System.out.println("Test: selectDistinctWithWhere");
        System.out.println("Query: SELECT DISTINCT FirstName, LastName FROM YCStudent WHERE FirstName < 'dave'");
        System.out.println();
        rs.printResultSet();
        dataBase.findTable("YCStudent").printTable();
    }


    /**
     * selecting distinct first names (which should only be avi and carl) and selecting the AVG gpa of that
     * subtable, as well as the max gpa of that subtable.
     * @throws JSQLParserException
     */
    @Test
    public void selectWithFunctions() throws JSQLParserException{
        createTable();
        createRows();

        ResultSet rs = dataBase.execute("SELECT FirstName, LastName, AVG(GPA), MAX(GPA) FROM YCStudent WHERE FirstName > 'carl' ORDER BY FirstName ASC;");

        assertEquals(rs.getRows().size(), 7);
        assertEquals(rs.getRows().get(0).row.get(0), "'gabriel'");
        assertEquals(rs.getRows().get(6).row.get(0), "'yehuda'");
        assertEquals(rs.getRows().get(0).row.get(2), 2.93);
        assertEquals(rs.getRows().get(0).row.get(3), 4.0);

    }


    /**
     * testing to see that the funtion method works.
     * @throws JSQLParserException
     */
    @Test
    public void selectManyFunctions() throws  JSQLParserException{

        createTable();
        createRows2();

       ResultSet rs = dataBase.execute("SELECT AVG(GPA), MIN(GPA), MAX(GPA), MIN(BannerID), MAX(FirstName), MIN(FirstName) FROM YCStudent;");

        assertEquals(rs.getRows().get(0).row.get(1), 0.0);
        assertEquals(rs.getRows().get(0).row.get(2), 4.0);
        assertEquals(rs.getRows().get(0).row.get(3), 1);
        assertEquals(rs.getRows().get(0).row.get(4), "'yehuda'");
        assertEquals(rs.getRows().get(0).row.get(5), "'avi'");

    }

    /**
     * Tests functions
     * @throws JSQLParserException
     */

    @Test
    public void testMoreFunction() throws JSQLParserException{
        createTable();
        createRows();

        ResultSet rs = dataBase.execute("SELECT AVG(GPA), COUNT(GPA) FROM ycstudent;");

        assertEquals(rs.getRows().size(), 1); //expecting 1 row of functions
        assertEquals(rs.getRows().get(0).row.get(1), 7.0); // count = 7.0, not 8.0, since one GPA value is null, we are expecting to skip over it
        assertEquals(rs.getRows().get(0).row.get(0), 2.51);

    }

    /**
     * this method tests Select with functions and with an order by.
     * All values of the first row of the subtable are expecting below
     * @throws JSQLParserException
     */

    @Test
    public void testFunctionWithOrderBy() throws JSQLParserException{
        createTable();
        createRows2();

        ResultSet rs = dataBase.execute("SELECT FirstName, COUNT(FirstName),  AVG(GPA), MIN(FirstName), MAX(FirstName) FROM ycstudent WHERE FirstName < 'nate' OR FirstName >= 'yehuda' ORDER BY FirstName DESC;");

        assertEquals(rs.getRows().size(), 11); // expecting 11 rows
        assertEquals(rs.getRows().get(0).row.get(0), "'yehuda'");
        assertEquals(rs.getRows().get(10).row.get(0), null); // there is a null value for a name and the order by is DESC
        assertEquals(rs.getRows().get(0).row.get(1), "10"); // expecting the count of first name to be 10 since there is one null
        assertEquals(rs.getRows().get(0).row.get(2), 2.66); //avg gpa
        assertEquals(rs.getRows().get(0).row.get(3), "'avi'");
        assertEquals(rs.getRows().get(0).row.get(0), "'yehuda'");


    }

    /**
     * this method tests an Order By
     * @throws JSQLParserException
     */

    @Test
    public void testOrderBy() throws JSQLParserException{

        createTable();
        createRows();

        ResultSet rs = dataBase.execute("SELECT * FROM YCStudent ORDER BY GPA ASC, LastName DESC;");

        assertEquals(dataBase.findTable("YCstudent").getTable().size(), 8);
        assertTrue(rs.getRows().get(0).row.get(1) == null); // expecting the first row to have a null GPA
        assertTrue(rs.getRows().get(7).row.get(1).equals(4.0)); // expecting last row to have gpa of 4.0
        assertTrue(rs.getRows().get(3).row.get(1).equals(2.2));

        for (int i = 0; i < rs.getRows().size() -1; i++) {
            if (rs.getRows().get(i).row.get(1) == null){
                continue;
            }
            Object d = rs.getRows().get(i).row.get(1);
            Object d2 = rs.getRows().get(i+1).row.get(1);
            assertTrue((Double) d < (Double)d2); // shows that GPA is in DESC order
        }
    }

    /**
     * This method tests a select subtable when using a WHERE and an ORDER by.
     * @throws JSQLParserException
     */

    @Test
    public void testComplicatedOrderByWithDistinct() throws JSQLParserException{
        createTable();
        createRowsForOrderBy();

        ResultSet rs = dataBase.execute("SELECT DISTINCT FirstName, LastName FROM YCStudent WHERE LastName>= 'wick' ORDER BY FirstName DESC, GPA DESC;");

        assertEquals(rs.getRows().size(), 5);
        assertEquals(rs.getRows().get(0).row.get(0), "'nate'");
        assertEquals(rs.getRows().get(4).row.get(0), "'gabriel'"); //first name descending

        assertEquals(rs.getRows().get(3).row.get(1), "'zowers'"); // since they both have the same first name, and im ordering by GPA as well,
        assertEquals(rs.getRows().get(4).row.get(1), "'yen'");    // then gabriel zowers should come before gabriel yen. If GPA was ASC then it would be the opposite

        // printing to better visualize
        System.out.println("****************************************************************************************");
        System.out.println("Test: testComplicatedOrderByWithDistinct");
        System.out.println("Query: SELECT DISTINCT FirstName, LastName FROM YCStudent WHERE LastName>= 'wick' ORDER BY FirstName DESC, GPA DESC;");
        System.out.println();
        rs.printResultSet();
        dataBase.findTable("YCStudent").printTable();

    }

    /**
     * This method tests that the btree was accessed accordingly. Flag variable indicates we accessed the Btree
     * @throws JSQLParserException
     */

    @Test
    public void testingToSeeIfAccessedBTree() throws JSQLParserException{
        createTable();
        createRows();

        ResultSet rs = dataBase.execute("SELECT FirstName, LastName, GPA FROM YcStudent WHERE GPA>=3.0 OR FirstName <'job';");
        //testing to see that the Btree was accessed

        for (BTree bt: dataBase.findTable("YCStudent").bTreeContainer ){
            if (bt.getIndexName().equalsIgnoreCase("GPA") || bt.getIndexName().equalsIgnoreCase("FirstName")){
                assertEquals(bt.getFlag(), true);
            }
            if (bt.getIndexName().equalsIgnoreCase("BannerID")){
                assertEquals(bt.getFlag(), false);
            }
        }

    }

    /**
     * This method tests that the btree was accessed accordingly. Flag variable indicates we accessed the Btree
     * @throws JSQLParserException
     */

    @Test
    public void testingBtreeForDelete() throws JSQLParserException{
        createTable();
        createRows();

        //indicates that non of the btrees were accessed
        for (BTree bt: dataBase.findTable("YCStudent").bTreeContainer ){
            assertEquals(bt.getFlag(), false);
        }

        ResultSet rs = dataBase.execute("DELETE FROM YCStudent WHERE LastName >'jacobi' ;");

        //indicates all of the btrees were accessed
        for (BTree bt: dataBase.findTable("YCStudent").bTreeContainer ){
                assertEquals(bt.getFlag(), true);
        }

        // note, yehuda shams is greater than jacobi
         assertTrue(dataBase.findTable("YCStudent").bTreeContainer.get(1).get("3.50").size()==0); // indicating that "yehuda shams was deleted from the GPA btree"
         assertTrue(dataBase.findTable("YCStudent").bTreeContainer.get(0).get("1").size()==0); // indicating yehuda shams was deleted from BannerID btree as well
    }

    /**
     * this method tests that each btree has all the rows in its proper key values
     * @throws JSQLParserException
     */

    @Test
    public void testingValuesAreInBtree() throws JSQLParserException{
        createTable();
        createRows();

        for(BTree bTree: dataBase.findTable("YCStudent").bTreeContainer){
            String columnName = bTree.getIndexName();
            int columnIndex = dataBase.findTable("YCStudent").getColumnIndex(columnName);
            for(int i = 0; i< dataBase.findTable("ycstudent").getTable().size(); i++){

                Row r = dataBase.findTable("ycstudent").getTable().get(i);
                String value = r.getRow().get(columnIndex);
                assertTrue(bTree.get(value).contains(r));
                assertEquals(bTree.size(), 9); //even tho there are 8 rows, the size is 9 because of the sentinal...

            }
        }
    }

    /**
     * this method tests that an index was indeed created, adding another btree to the container, and that the key 'shams' has 2 values in it,
     * which is exactly what we are expecting.
     * @throws JSQLParserException
     */

    @Test
    public void testingCreateIndex() throws JSQLParserException{
        createTable();
        createRows();
        ResultSet rs = dataBase.execute("CREATE INDEX LastName_Index on YCStudent (LastName);");


        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('jacob','shams',3.5, 22, 123456212);");

        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, " +
                "BannerID, SSNum) VALUES ('avi','jacobi', 23, 123222888);");


        int index = dataBase.findTable("YCSTUDENT").getColumnIndex("LastName");
        Row r = dataBase.findTable("ycstudent").getTable().get(8);

        assertEquals(dataBase.findTable("YCSTUDENT").bTreeContainer.size(), 4);
        assertEquals(dataBase.findTable("YCSTUDENT").bTreeContainer.get(3).getIndexName(), "LastName");
        assertEquals(dataBase.findTable("YCSTUDENT").bTreeContainer.get(3).size(), 11);

        assertTrue(dataBase.findTable("YCSTUDENT").bTreeContainer.get(3).get("'shams'").size()==2);
        assertTrue(dataBase.findTable("YCSTUDENT").bTreeContainer.get(3).get("'shams'").contains(r));

    }

    /**
     * Test to see that no errors occur when creating another table and that we can call upon the correct table when
     * we want to preform some sort of action.
     * @throws JSQLParserException
     */

    @Test
    public void creatingAnotherTable() throws JSQLParserException{
        createTable();

        dataBase.execute(" CREATE TABLE UCLA\n" +
                "(\n" +
                "BannerID int,\n" +
                " SSNum int UNIQUE ,\n" +
                "FirstName varchar(255) ,\n" +
                "LastName varchar(255) NOT NULL ,\n" +
                "GPA decimal(1,2) DEFAULT 0.00,\n" +
                " CurrentStudent boolean DEFAULT true,\n" +
                "PRIMARY KEY (BannerID)\n" +
                "); ");

        dataBase.execute("INSERT INTO UCLA (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('yehuda','shams',3.5, 1, 123456888);");

        dataBase.execute("INSERT INTO UCLA (FirstName, LastName, GPA," +
                "BannerID, SSNum) VALUES ('nate','saada',3.9, 2, 123456999);");



        Row row = dataBase.findTable("UCLA").getTable().get(0); // gets the first row
        int index1 = dataBase.findTable("UCLA").getColumnIndex("FirstName");
        int index2 = dataBase.findTable("UCLA").getColumnIndex("LastName");
        int index3 = dataBase.findTable("UCLA").getColumnIndex("GPA");
        int index4 = dataBase.findTable("UCLA").getColumnIndex("BannerID");

        assertTrue(dataBase.getDb().size() == 2); //There are 2 tables
        assertTrue(dataBase.findTable("UCLA").getTable().size()==2); // UCLA is of length 2
        assertTrue(row.getRow().get(index1).equalsIgnoreCase("'yehuda'"));
        assertTrue(row.getRow().get(index2).equalsIgnoreCase("'shams'"));
        assertTrue(row.getRow().get(index3).equals("3.50"));
        assertTrue(row.getRow().get(index4).equals("1"));


    }

}