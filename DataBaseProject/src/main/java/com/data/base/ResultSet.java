package com.data.base;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;

import java.util.ArrayList;

public class ResultSet{

    private ArrayList<String> columns = new ArrayList<String>(); //column Names
    private ArrayList<ObjectArray> rows = new ArrayList<ObjectArray>(); //actual rows which is converted into objects
    private ArrayList<ColumnDescription.DataType> dataTypes = new ArrayList<ColumnDescription.DataType>(); // dataTypes

    /**
     * Constructor for when we want to pass in a "true" or "false" indicating that a query was successful
     * @param bool
     */

    public ResultSet(String bool){
        rows.add(new ObjectArray());
        rows.get(0).add(bool);
    }


    /**
     * Result set constructor take in a row and properly converts it into object row
     * @param r
     * @param columnNames
     * @param dataTypes2
     */

    public ResultSet(Row r, ArrayList<String> columnNames, ArrayList<ColumnDescription.DataType> dataTypes2){ // for the functions
        dataTypes = dataTypes2;
        if(columnNames==null){
            for(String s: columnNames){
                columns.add(s);
            }
        }
        ObjectArray oa = new ObjectArray();
        for ( int i = 0 ; i < r.getRow().size(); i++){
            if(dataTypes.get(i).name().equalsIgnoreCase("NULL")){
                oa.add(null);
            }
            else if(dataTypes.get(i).name().equals("VARCHAR")){
                oa.add(r.getRow().get(i));
            }
            else if(dataTypes.get(i).name().equals("INT")){
                oa.add(Integer.parseInt(r.getRow().get(i)));
            }
            else if(dataTypes.get(i).name().equals("DECIMAL")){
                oa.add(Double.parseDouble(r.getRow().get(i)));
            }
            else if(dataTypes.get(i).name().equals("BOOLEAN")){
                oa.add(Boolean.parseBoolean(r.getRow().get(i)));
            }
        }
        rows.add(oa);
    }




    public ResultSet(ArrayList<Row> rows, ArrayList<String> cols, ArrayList<ColumnDescription.DataType> dataTypes2 ){
        this.columns = cols;
        setRows2(rows, dataTypes2);
    }

    /**
     * Mthos is used in method above. It takes the arrayList of rows and properly converts each row to objects
     * @param rows2
     * @param dataTypes2
     */

    public void setRows2(ArrayList<Row> rows2, ArrayList<ColumnDescription.DataType> dataTypes2){
        for(Row r : rows2){
            ObjectArray oa = new ObjectArray();
            for ( int i = 0 ; i < r.getRow().size(); i++){

                if(r.getRow().get(i).equalsIgnoreCase("NULL")){
                    oa.add(null);
                }
                else if(dataTypes2.get(i).name().equals("VARCHAR")){
                    oa.add(r.getRow().get(i));
                }
                else if(dataTypes2.get(i).name().equals("INT")){
                    oa.add(Integer.parseInt(r.getRow().get(i)));
                }
                else if(dataTypes2.get(i).name().equals("DECIMAL")){
                    oa.add(Double.parseDouble(r.getRow().get(i)));
                }
                else if(dataTypes2.get(i).name().equals("BOOLEAN")){
                    oa.add(Boolean.parseBoolean(r.getRow().get(i)));
                }
            }
            rows.add(oa);
        }
    }


    public ArrayList<ObjectArray> getRows() {
        return rows;
    }




    public ArrayList<String> getColumns() {
        return columns;
    }




    public void setColumns(ArrayList<String> columns1){
        this.columns = columns1;

    }


    public void setRows(ArrayList<ObjectArray> rows) {
        this.rows = rows;
    }



    public void setDataTypes(ArrayList<ColumnDescription.DataType> dataType){
        this.dataTypes = dataType;
    }


    public void printDataType(){
        for(ColumnDescription.DataType dataT: dataTypes){
            System.out.print(dataT.toString() + " ");
        }
    }


    public ArrayList<ColumnDescription.DataType> getDataTypes(){
        return dataTypes;
    }



    public void addToDataType(ColumnDescription.DataType dataT ){
        dataTypes.add(dataT);

    }


    /**
     * this method apends rows to the result set which is useful for
     * when you have a table in the result set and need to add columns for select functions
     * @param r
     * @param columnNames
     * @param dataTypes2
     */

    public void addRowsToResultSet(Row r, ArrayList<String> columnNames, ArrayList<ColumnDescription.DataType> dataTypes2){
        //adds columnNames to field columns
        //appends r to all rows in rows the field
        for(String s: columnNames){
            columns.add(s);
        }
        if (rows.size()>0 && rows.get(0).row.size()==0){
            int size = rows.size()-1;
            for (int i =0; i < size; i++){
                rows.remove(0);
            }
        }
        for(ObjectArray row : rows){
            addAll(row,r,dataTypes2);
        }
    }

    /**
     * method adds the properly converted row to the ObjectArray list
     * @param oa
     * @param r
     * @param dataTypes2
     */

    public void addAll(ObjectArray oa, Row r, ArrayList<ColumnDescription.DataType> dataTypes2){
        for(int i = 0; i < r.getRow().size(); i++){
            if(r.getRow().get(i).equalsIgnoreCase("NULL")){
                oa.add(null);
            }
            else if(dataTypes2.get(i).name().equals("VARCHAR")){
                oa.add(r.getRow().get(i));
            }
            else if(dataTypes2.get(i).name().equals("INT")){
                oa.add(Integer.parseInt(r.getRow().get(i)));
            }
            else if(dataTypes2.get(i).name().equals("DECIMAL")){
                oa.add(Double.parseDouble(r.getRow().get(i)));
            }
            else if(dataTypes2.get(i).name().equals("BOOLEAN")){
                oa.add(Boolean.parseBoolean(r.getRow().get(i)));
            }
        }
    }


    public void printResultSet(){

        System.out.println("Result Set: ");

        int i = 0;
        for (String column: columns){
            String dt ="";
            if(i != dataTypes.size()){
                 dt ="(" + dataTypes.get(i).toString() +")";
                i++;
            }
            System.out.printf("%-34s", dt +column);

        }

        System.out.println();

        for (ObjectArray r: rows) {
            for (Object cell: r.row){
                if(cell == null)System.out.printf("%-35s", "null");
                else System.out.printf("%-35s", cell.toString());
            }
            System.out.println();
        }
        System.out.println();

    }

}
