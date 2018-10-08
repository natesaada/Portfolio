package com.data.base;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;
import net.sf.jsqlparser.JSQLParserException;

import java.util.ArrayList;


public class DataBase {

   private ArrayList<Table> db;


    public DataBase(){
        db = new ArrayList<Table>();


    }

    public ArrayList<Table> getDb() {
        return db;
    }


    /**
     * Public method where the query is passed through and sends it to the proper method
     * @param SQL
     * @return
     * @throws JSQLParserException
     */
    public ResultSet execute(String SQL) throws JSQLParserException { //Where all the query should be sent to
        SQLParser parser = new SQLParser();
        Object q = parser.parse(SQL);
        ResultSet resultSet = null;

        if(q instanceof CreateTableQuery){

           resultSet = createTable((CreateTableQuery) q);
        }

        else if (q instanceof InsertQuery){
            resultSet = insertQuery((InsertQuery) q);
        }

        else if (q instanceof SelectQuery){
            resultSet = selectQuery((SelectQuery) q);
        }

        else if(q instanceof  UpdateQuery){
            resultSet = updateQuery((UpdateQuery) q);
        }
        else if(q instanceof DeleteQuery){
            resultSet = deleteQuery((DeleteQuery) q);

        }
        else if(q instanceof CreateIndexQuery){
            resultSet = indexColumn((CreateIndexQuery) q);
        }

        return resultSet;
    }

    /**
     *  method that executes an Create Table query
     * @param sq
     * @return
     */

    private ResultSet createTable(CreateTableQuery sq){
        ResultSet rs = null;

        try{
            Table tb = new Table(sq);
            db.add(tb);

            rs = new ResultSet("true");
       }catch (Exception e){
            rs = new ResultSet("false: " + e.getMessage());
            return rs;
       }
       return rs;
    }

    /**
     *  method that executes an Insert query
     * @param sq
     * @return
     */

    private ResultSet insertQuery(InsertQuery sq){
        ResultSet rs = null;
        String tableName = sq.getTableName();

       try{
           Table t =findTable(tableName);
           rs = t.insert(sq);
       } catch (Exception e){
           rs = new ResultSet("false: " +e.getMessage());
           return rs;

       }
        return rs;
    }

    /**
     *  method that executes an Select query
     *
     * @param sq
     * @return
     */

    private ResultSet selectQuery(SelectQuery sq){
        ResultSet rs = null;
        String[] tableName = sq.getFromTableNames();

        try{

            Table t =findTable(tableName[0]); // cannot be a multi table select so this works
            rs = t.runnerSelect(sq);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     *  method that executes an UPDATE query
     * @param sq
     * @return
     */

    private ResultSet updateQuery (UpdateQuery sq){
       ResultSet rs = null;
       String tableName = sq.getTableName();

       try{
           Table t =findTable(tableName);
           t.update(sq);
           rs = new ResultSet("true");

       } catch (Exception e){
           rs = new ResultSet("false: " + e.getMessage());
           return rs;
       }
       return rs;
   }

    /**
     * method that executes an Delete query
     * @param sq
     * @return
     */

    private ResultSet deleteQuery (DeleteQuery sq){
       ResultSet rs = null;
       String tableName = sq.getTableName();

       try{
           Table t =findTable(tableName);
           t.deleteRows(sq);
           rs = new ResultSet("true");

       } catch (Exception e){
           rs = new ResultSet("false: " + e.getMessage());
           return rs;
       }
       return rs;
   }

    /**
     * method that executes an Create Index query
     * @param sq
     * @return
     */

    private ResultSet indexColumn(CreateIndexQuery sq){
       ResultSet rs = null;
       String tableName = sq.getTableName();

       try{
           Table t = findTable(tableName);
           t.createIndex(sq);
           rs = new ResultSet("true");

       } catch (Exception e){
           rs = new ResultSet("false: " + e.getMessage());
           return rs;
       }
       return rs;
   }


    /**
     * method to find the correct table which is stored in an arrayList of tables
     * @param tableName
     * @return
     */


    public Table findTable(String tableName) {
        for (Table t: db ) {
            if (t.tableName.equalsIgnoreCase(tableName)){
                return t;
            }
        }
        throw new IllegalArgumentException("table not found");
    }


    /**
     * method to print the dataBase holding table
     */

    public void printDataBase(){
        for(Table tb: db){
            System.out.println("TABLE NAME: "+ tb.tableName);
            System.out.println();
            for (ColumnDescription cd: tb.columns){
                String dt = "("+cd.getColumnType().toString()+")";
                System.out.printf("%-33s", dt + cd.getColumnName());
            }

            System.out.println();
            System.out.println();
            for(Row row: tb.getTable()){
                for (String cell: row.getRow()){
                    System.out.printf("%-35s", cell);
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("*********************************************************************************************************************************");

        }

    }
}


