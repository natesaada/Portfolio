package com.data.base;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnValuePair;

import java.util.ArrayList;
import java.util.Arrays;

public class Row {



    private String tableName;
    private ArrayList<String> row = new ArrayList<String>(); //pretty much the columns of the row
    private ColumnValuePair[] columnValues;


    /**
     * First constructor which takes in a tableName, CV pair Array which we retrieve from the
     * object thats returned to us by the parser, a Column Description array (which is also
     * given to us), and a primary key.
     *
     * @param tableName
     * @param values
     * @param columns
     */

    public Row (String tableName, ColumnValuePair[] values, ColumnDescription[] columns, ColumnDescription primaryKey){
        this.columnValues = values;
        this.tableName = tableName;
        this.createRow(columns, values, primaryKey);
    }



    /**
     *
     *
     * This constructor creates a Row and assigns the table name
     *
     * @param tableName
     * @param columns
     */

    public Row (String tableName, ColumnDescription[] columns){

        this.tableName = tableName;

    }

    public Row(){

    }



    /**
     * First step of the createRow method, check to see if the query is valid using the isValidQuery
     * method.
     * Secondly, we want to defaultInsert the columns so that theyre all set to their default value.
     * Thirdly, call the assignProperValues method to rearrange and check that all the values in the row are proper
     *
     * @param columns
     * @param values
     * @param primaryKey
     * @return Returns the row
     */

    private ArrayList<String> createRow(ColumnDescription[] columns, ColumnValuePair[] values, ColumnDescription primaryKey) {

        if (!isValidQuery(columns, values, primaryKey)){ // checks to see that every column given was a not null or primary key
            throw new IllegalArgumentException("Invalid query. Columns inserted must have at least the primary key column or Not Null columns");
        }
        defaultInsert(columns);

        row = assignProperValues(columns, values);
        return row;
    }

    /**
     * this method goes through every index in the row we want to create and makes sure the
     * values are valid and match each column condition.
     * @param columns
     * @param values
     * @return
     */

    private ArrayList<String> assignProperValues(ColumnDescription[] columns, ColumnValuePair[] values ){
        for (ColumnValuePair cv : values) {

            String columnName =  cv.getColumnID().getColumnName();
            ColumnDescription cd = getColumnDescription(columnName, columns);
            int index = getColumnDescriptionIndex(columns, cd);

            if (isEmptyString(cd, cv.getValue(), cv)){ // checks to see if empty string was given for a column thats allowed null values
                row.set(index, "null");
            }
            else if (valueMatchesColumnDataType(cd, cv.getValue())) {

                String value =cv.getValue();
                value = convertingNull(cd,cv,value); // if NULL was given, convert to "null"
                if (cd.getColumnType().name().equals ("DECIMAL") && !value.equals("null")){
                    value = valueForDoubleColumn(cd, cv.getValue());
                    row.set(index, value);
                    continue;
                }
                row.set(index, value);
            }
            else{
                throw new IllegalArgumentException("Invalid Query! The values did not match for column  " + cv.getColumnID().getColumnName());
                //https://stackoverflow.com/a/17514329
            }
        }
        return row;
    }


    /**
     * This method adds the extra decimal that needs to be added to fit the boundries.
     * If more numbers were added on the right side of the decimal then we only put the numbers
     * that reach the cut off point and forget about the rest of it.
     *
     * @param cd
     * @param value
     * @return
     */

    private String valueForDoubleColumn(ColumnDescription cd, String value){
        int left = value.indexOf('.'); // represents the amount of integers to the left of the decimal
        int right = value.length()-left-1;

        if (right < cd.getFractionLength()){
            int amountToAdd = cd.getFractionLength()-right;
            for (int i = 0; i < amountToAdd; i++){
                value = value + "0";
            }
            return value;
        }
        return value;
    }

    /**
     * Honestly, I just made this method becuase i didnt like to see the all caps "null"
     * this method was more for testing purposes. Though it is used in the code, I just left it
     * there even though I know i can compareIgnoreCase
     *
     * @param cd
     * @param cv
     * @param value
     * @return
     */

    private String convertingNull(ColumnDescription cd, ColumnValuePair cv, String value){

        if (value.equals("NULL") && cd.isNotNull()==false){
            value = "null";
        }
        else if (value.equals("NULL") && cd.isNotNull()==true){
            throw new IllegalArgumentException("cannot add null value to " + cv.getColumnID().getColumnName());
        }
        return value;
    }


    /**
     * this method checks to see if an empty string was passed
     *
     * @param cd
     * @param value
     * @param cv
     * @return
     */

    private boolean isEmptyString(ColumnDescription cd, String value, ColumnValuePair cv){

        if (valueMatchesColumnDataType(cd, cv.getValue()) && cv.getValue().equals("''") && !cd.isNotNull()){
            return true;
        }
        else if (valueMatchesColumnDataType(cd, cv.getValue()) && cv.getValue().equals("''") && cd.isNotNull()){
            throw new IllegalArgumentException("Cannot add an empty string to the column name: " + cv.getColumnID().getColumnName());
        }
        return false;
    }

    /**
     * this boolean method is used in the createRow method. It pretty much checks that all the values
     * match the DataType
     * @param cd
     * @param value
     * @return
     */
    private boolean valueMatchesColumnDataType(ColumnDescription cd, String value){
        if(cd.getColumnType().name().equals("DECIMAL")){  //https://stackoverflow.com/a/6667403
           return isDecimal(cd, value);
        }
        else if (cd.getColumnType().name().equals("INT")){
            return isInteger(cd, value);
         }

        else if (cd.getColumnType().name().equals("BOOLEAN")) {
            return isBoolean(value);
         }
        else if (cd.getColumnType().name().equals("VARCHAR") && value.length()<= cd.getVarCharLength() && value.charAt(0) == '\''){
             return true;
         }
        else if(cd.isNotNull() == false && value.equalsIgnoreCase("NULL")){
            return true;
        }
         else if(cd.isNotNull() == true && value.equalsIgnoreCase("NULL")){
            throw new IllegalArgumentException("cannot insert null into " + cd.getColumnName());
        }
        else{
            return false;
        }
    }

    /**
     * Part of the valueMatchesColumnDataType method. Checks to see if the value given is indeed a decimal
     * @param cd
     * @param value
     * @return
     */

    private boolean isDecimal(ColumnDescription cd, String value){
        if (cd.isNotNull()==false && value.equalsIgnoreCase("NULL")){
                return true;
        }
        try {
            double value2 = Double.parseDouble(value);
            int left = value.indexOf('.'); // represents the amount of integers to the left of the decimal
            int right = value.length()-left-1;


            if (cd.getWholeNumberLength() >= left && cd.getFractionLength()>=right){
                return true;
            }
            else{
                throw new IllegalArgumentException("the decimal you entered didnt match the constraints");
            }
        }
        catch (NumberFormatException e){
            return false;
        }
    }


    /**
     *  Part of the valueMatchesColumnDataType method
     *
     * @param value
     * @return
     */
    private boolean isInteger(ColumnDescription cd, String value){
        if (cd.isNotNull()==false && value.equalsIgnoreCase("NULL")){
            return true;
        }
        try {
            int value3 = Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }

    /**
     *  Part of the valueMatchesColumnDataType method
     * @param value
     * @return
     */

    private boolean isBoolean(String value){
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return true;
        } else {
            return false;
        }
    }




    /**
     * this method returns the column description CELL within the column description array.
     * Its returning the cell that matches the column name.
     *
     * @param columnName
     * @param columns
     * @return
     */

    private ColumnDescription getColumnDescription (String columnName, ColumnDescription[] columns) {

        for (ColumnDescription cd: columns){

            if(cd.getColumnName().equals(columnName)){
                return cd;
            }
        }
        return null;
    }

    /**
     *
     * Essentially what this method does is check to see: if say in the insert query, the query passed
     * only 2 columns for the row. What we want to do is check if the values that have a NOT NULL value was passed
     * in the query, because if it wasn't then it becomes a problem. So we iterate through the CD array and
     * when we come across a cell for which has a not null value, we jump into the CVP array and see if that
     * cell was indeed passed in the query and fulfills the requirements.
     *
     *
     * @param columns
     * @param values
     * @return this method returns a boolean. True indicates that what was passed in the query does indeed
     * fulfill the requirements
     */

    private boolean isValidQuery (ColumnDescription[] columns, ColumnValuePair[] values, ColumnDescription primaryKey ){

        boolean flag = false;
        for (int i =0; i< columns.length; i++){
            if (columns[i].isNotNull() || columns[i].getColumnName().equals(primaryKey.getColumnName())){
                for (int j =0; j< values.length; j++){
                    if (columns[i].getColumnName().equals(values[j].getColumnID().getColumnName())){
                        flag = true;
                    }
                    continue;
                }
                if (!flag){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method takes in the Column Description and iterates through it in a
     * for each loop. It checks if the value has a default value, i.e. 'NULL' or 'NOT NULL'
     * If it doesnt have a default value then fill in the row array list with null. If it does
     * indeed have a default value then fill in that slot in the row array list with that default value.
     *
     * @param columns
     */

    private void defaultInsert(ColumnDescription[] columns){

        for (ColumnDescription cd: columns) {

            if (cd.getHasDefault()){
               row.add (cd.getDefaultValue());
            }
            else if (cd.isNotNull()){
                row.add(""); // this will be replaced later by the insert method
            }
            else {
                row.add("null"); // easier to save as a string because of our @override equals method when it compares one row to another
                // the null is converted to an actual null value when its given to the result set
                //https://stackoverflow.com/questions/17485127/unique-constraint-column-can-only-contain-one-null-value            }
            }
        }

    }


    /**
     *  this method gives us the index for with the column description is in.
     * @param columns
     * @param cd
     * @return
     */

    private int getColumnDescriptionIndex(ColumnDescription[] columns, ColumnDescription cd ){

        for (int i = 0; i< columns.length; i++){
            if (columns[i].equals(cd)){
                return i;
            }
        }
        return -1;
    }

    /**
     * gets the row
     * @return
     */

    public ArrayList<String> getRow() {

        return row;

    }


    /**
     * inserts value into row
     * @param value
     */

     public void insert(String value){
        this.row.add(value);
    }



    @Override
    public String toString() {
        return "Row{" + "row=" + row + '}';
    }

    @Override
    public int hashCode() {
        int result = row.hashCode();
        result = 31 * result + Arrays.hashCode(columnValues);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        Row r = (Row) obj;
        for (int i =0; i < row.size(); i++){
            if (!this.row.get(i).equals(r.row.get(i))){
                return false;
            }
        }
        return true;
    }
}
