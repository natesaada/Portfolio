package com.data.base;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;

import java.util.ArrayList;


public class Table  {

    String tableName;
    ColumnDescription[] columns;
    ColumnDescription primaryKey;


    ArrayList<Row> table = new ArrayList<Row>();
    ArrayList<BTree> bTreeContainer = new ArrayList<BTree>();
    ArrayList<String> columnsInWhereClause = new ArrayList<String>();


    /**
     * creates a table and assigns the table name, column description
     * and the primary key.
     * Also Automatically indexes the primary key
     * @param query
     */

    public Table(CreateTableQuery query){
        tableName = query.getTableName();
        columns = query.getColumnDescriptions();
        primaryKey = query.getPrimaryKeyColumn();

        createIndex(primaryKey);


    }

    /**
     * second constructor is an empty constructor
     */
    public Table(){

    }


    /**
     * The Insert method creates a Row comprised of the data given
     * It then checks if there are any unique columns, if yes, then make sure
     * the uniqueness is valid and make sure that there wasnt any null values given to a column
     * that cant accept null.
     * Additionally, the row is added to the proper Btree
     * @param query
     * @return result set that is either true or false.
     */
    public ResultSet insert(InsertQuery query){
        ResultSet rs = null;
        try{
            Row row = new Row(tableName, query.getColumnValuePairs(), columns, primaryKey);

            if (checkUnique(row) == true && checkForNotNull2(row) == true) {  //ps. there can we ONLY ONE null value for unique
                table.add(row);
            }

            rs = new ResultSet("true");
            addingValuesToBTree(row);

        }
        catch(IllegalArgumentException e){
            rs = new ResultSet("false: " +e.getMessage());
            return rs;
        }
        return rs;
    }

    /**
     * This method adds the rows to the correct Keys to each btree
     * @param row
     */

    private void addingValuesToBTree(Row row){

        for (BTree bt: bTreeContainer){
            ArrayList<Row> rowsToPassInBTree = new ArrayList<Row>();
            int index = getColumnIndex(bt.getIndexName());
            String columnName = bt.getIndexName();

            if(columnName.equalsIgnoreCase(primaryKey.getColumnName())){
                addingPrimaryToBTree(row, bt);
                continue;

            }
            else{
                if(bt.get(row.getRow().get(index)) == null) {
                    rowsToPassInBTree.add(row);
                    bt.put((String) row.getRow().get(index), rowsToPassInBTree);
                }
                else {
                    rowsToPassInBTree.add(row);
                    bt.put(row.getRow().get(index), rowsToPassInBTree);
                }

            }
        }
    }

    /**
     * this method passes in an array list of rows in order to add to the primary key btree.
     * this method is being used in "addingValuesToBTree"
     * @param row
     * @param bt
     */

    private void addingPrimaryToBTree(Row row, BTree bt){
        ArrayList<Row> rowsToPassInBTree = new ArrayList<Row>();

        int index = getColumnIndex(bt.getIndexName());
        rowsToPassInBTree.add(row);
        bt.put(row.getRow().get(index), rowsToPassInBTree);

    }


    /**
     * this method makes sure that the row we are trying to insert doesnt have a null
     * value in a column where null isnt allowed.
     * @param r
     * @return
     */
    private boolean checkForNotNull2(Row r){
        for (int i = 0; i <columns.length ; i++) {
            if (columns[i].isNotNull()){
                int index = getColumnIndex(columns[i].getColumnName());
                if(r.getRow().get(index).equals("null")){
                    throw new IllegalArgumentException("cannot add null to " + columns[i]);
                }
            }

        }
        return true;
    }

    /**
     * this method checks that the columns which need to be unique
     * are in fact unique.
     * @param row
     * @return
     */

    private boolean checkUnique(Row row) {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].isUnique() == true || columns[i].getColumnName().equalsIgnoreCase(primaryKey.getColumnName())) {
                for (Row r : table) {
                    if(row == r){
                        continue;
                    }
                    int index = getColumnIndex(columns[i].getColumnName());
                     if (r.getRow().get(index).equalsIgnoreCase(row.getRow().get(index))) {
                        throw new IllegalArgumentException("The column " + columns[i].getColumnName()+ " for this row didnt satisfy uniquness ");
                    } else {
                        continue;
                    }
                }
            }
        }
        return true;
    }


    /**
     * this method creates an index for just the primary key. Its called when we create the table
     * since every table for sure has a primary key
     * @param primaryKey
     */

    private void createIndex(ColumnDescription primaryKey){ // for the primary key when creating table
        //note: since its primary key, we know there wont be a null value for it.

        BTree bTree = new BTree<String, ArrayList<Row>>();
        bTree.setIndexName(primaryKey.getColumnName());
        bTree.setbTreeDataType(primaryKey.getColumnType().toString());
        bTreeContainer.add(bTree);

    }

    /**
     * this method creates an index on a selected column
     * it then adds btrees to the array list of trees
     * @param sq
     */

    public void createIndex(CreateIndexQuery sq){
        int index = getColumnIndex(sq.getColumnName());

        BTree bTree = new BTree<String, ArrayList<Row>>();
        bTree.setIndexName(sq.getColumnName());
        String dataType = getColumnType(sq.getColumnName());
        bTree.setbTreeDataType(dataType);

        String data;
        for (ColumnDescription cd: columns){
            if (cd.getColumnName().equalsIgnoreCase(sq.getColumnName())){
                data = cd.getColumnType().toString();
                bTree.setbTreeDataType(data);
            }
        }

        bTreeContainer.add(bTree);
        addingToBTree(bTree, index);

    }

    /**
     * if say someone called a create index on a column to a table that already has multiple rows,
     * this method makes sure to add the rows that already exist into the btree.
     * it is called in the "Create Index" method
     *
     * @param bTree
     * @param index
     */

    private void addingToBTree(BTree bTree, int index){
        if (table.size()>=1) {
            for (Row row : table) {
                ArrayList<Row> arrayListToAdd = new ArrayList<Row>();
                if (table.size() == 1) {
                    arrayListToAdd.add(row);
                    bTree.put(row.getRow().get(index), arrayListToAdd);
                    continue;
                }
                else {
                    if(bTree.get(row.getRow().get(index)) == null){
                        arrayListToAdd.add(row);
                        bTree.put((String) row.getRow().get(index), arrayListToAdd);
                    }
                    else{
                        ArrayList<Row> arrayListToAdd3 = (ArrayList<Row>) bTree.get(row.getRow().get(index));
                        arrayListToAdd3.add(row);
                        bTree.increaseKVP();
                    }
                }
            }
        }

    }





    /**
     * this runnerSelect method is the over arching select method. The method makes sure that
     * the order of the select query was properly executed.
     * @param sq
     * @return
     */

    public ResultSet runnerSelect(SelectQuery sq){
        ColumnID[] cids = columnIndicesToBeRemoved(sq);
        ArrayList<String> colNames = colIDsToStrings(columnIndicesToBeRemoved(sq));
        ArrayList<String> newCnames = createFunctionColumnNames(sq.getFunctions());
        ResultSet rs = null;
        Row functionRow = null;
        ArrayList<Row> rows = null;
        ArrayList<Row> orderedRows = null;
        if (sq.getWhereCondition() != null) {
            rows = selectWithWhere(sq);
        }

        orderedRows = doOrderBy(rows, sq);
        rs = isStar(rows, orderedRows, sq, colNames);

        if(!isStar(sq.getSelectedColumnNames())){ // not a star
            rs = notStar(rows,orderedRows,sq,rs,cids,colNames,newCnames, functionRow);
        }
        return isDistinct(rs,sq,functionRow,newCnames,cids);
    }

    /**
     * this method returns an array list of strings which are the column IDs
     * @param IDlist
     * @return
     */

    public ArrayList<String> colIDsToStrings(ColumnID[] IDlist){
        ArrayList<String> colNames = new ArrayList<String>();
        for(int i = 0; i < IDlist.length; i++){
            colNames.add(IDlist[i].getColumnName());
        }
        return colNames;
    }

    /**
     * This method returns an array list of the columns with functions on them.
     * @param functions
     * @return
     */

    private ArrayList<String> createFunctionColumnNames(ArrayList<SelectQuery.FunctionInstance> functions) {
        ArrayList<String> newColNames = new ArrayList<String>();
        for(SelectQuery.FunctionInstance fi: functions){
            String s= fi.function+"("+fi.column.getColumnName()+")";
            newColNames.add(s);
        }
        return newColNames;
    }


    /**
     * since the parser did something a bit weird where it would share column names in the select columns
     * and in the select function, we need to make sure we arent redundant with what we are selecting. This method
     * returns the ColumnID which needs to be removed if it was duplicated
     * @param sq
     * @return
     */

    private ColumnID[] columnIndicesToBeRemoved(SelectQuery sq) {
        int flag = 0;
        ArrayList<Integer> indices = new ArrayList<Integer>();
        for(SelectQuery.FunctionInstance fi : sq.getFunctions()){
            ColumnID id = fi.column;
            for(int i = 0 ; i < sq.getSelectedColumnNames().length; i++){
                if(sq.getSelectedColumnNames()[i].equals(id)){
                    if(contains1(indices, i)){
                        continue;
                    }
                    indices.add(i);
                    flag = 1;
                    break;
                }
            }
        }
        if(flag == 0){
            return sq.getSelectedColumnNames();
        }
        return removeIndicesFromColumnID(sq.getSelectedColumnNames(), indices);
    }

    /**
     * this method carries over from the logic above
     * @param cids
     * @param indices
     * @return
     */

    private ColumnID[] removeIndicesFromColumnID(ColumnID[] cids, ArrayList<Integer> indices){
        ColumnID[] newCID = new ColumnID[cids.length - indices.size()];
        int j = 0;
        for(int i = 0 ; i < cids.length; i++){
            if(!contains1(indices, i)){
                newCID[j++] = cids[i];
            }
        }
        return newCID;
    }

    /**
     * Wasnt allowed to use  .contains() for sorting and aranging the table so I created my own
     * @param indicies
     * @param i
     * @return
     */

    private boolean contains1(ArrayList<Integer> indicies, int i){
        for (Integer integer: indicies) {
            if (integer==i){
                return true;
            }
        }
        return false;
    }

    /**
     * this method calls upon the getSpecifiedRow method which sorts the rows that was asked for by the WHERE clause
     * @param sq
     * @return
     */

    private ArrayList<Row> selectWithWhere(SelectQuery sq){

        ArrayList<Row> validRows = getSpecifiedRow(sq.getWhereCondition()); // array list of rows that meet the condition
        return validRows;
    }

    /**
     * this method carries over from the runnerSelect method. There is an order in executing the select method
     * this method has its specific order if there isnt a star in the select query.
     * @param rows
     * @param orderedRows
     * @param sq
     * @param rs
     * @param cids
     * @param colNames
     * @param newCnames
     * @param functionRow
     * @return
     */

    private ResultSet notStar(ArrayList<Row> rows, ArrayList<Row> orderedRows, SelectQuery sq, ResultSet rs, ColumnID[] cids, ArrayList<String> colNames, ArrayList<String> newCnames, Row functionRow){
        ArrayList<Row> rows2;
        if(orderedRows != null){
            orderedRows = select(cids,orderedRows);
            rs = new ResultSet(orderedRows,colNames,getDataTypes(colNames));
        }
        else if(rows != null){
            rows2 = select(cids,rows);
            rs = new ResultSet(rows2,colNames,getDataTypes(colNames));
        }
        else{
            rows2 = select(cids,table);
            rs = new ResultSet(rows2,colNames,getDataTypes(colNames));
        }
        if(sq.getFunctions().size()>=1){
            if (rows == null){
                functionRow = functionMethod(sq, table);
            }
            else {
                functionRow = functionMethod(sq, rows);
            }
            rs.addRowsToResultSet(functionRow,newCnames,getDataTypes(sq));
        }
        rs.setDataTypes(getDataTypes(colNames));
        return rs;
    }





    /**
     * this method returns an array list of Data Types. We use it to set the data types in the result set.
     * this method is being passed a SelectQuery
     * @param sq
     * @return
     */
    private ArrayList<ColumnDescription.DataType> getDataTypes(SelectQuery sq){
        ArrayList<String> colNames = new ArrayList<String>();
        for(SelectQuery.FunctionInstance fi : sq.getFunctions()){
            colNames.add(fi.column.getColumnName());
        }
        return getDataTypes(colNames);
    }


    /**
     * this method returns an arrayList of Data Types.
     * its used in the "notStar" method
     * @param columnNames
     * @return
     */

    private ArrayList<ColumnDescription.DataType> getDataTypes(ArrayList<String> columnNames){
        ArrayList<ColumnDescription.DataType> dataTypes = new ArrayList<ColumnDescription.DataType>();

        for(String selectColumn: columnNames){
            for(ColumnDescription cd: columns){
                if (selectColumn.equalsIgnoreCase(cd.getColumnName())){
                    dataTypes.add(cd.getColumnType());
                }
            }
        }
        return dataTypes;

    }

    /**
     *
     * this method carries over from the runnerSelect method. There is an order in executing the select method
     * this method has its specific order if there is a star in the select query.
     *
     * @param rows
     * @param orderedRows
     * @param sq
     * @param colNames
     * @return
     */

    private ResultSet isStar(ArrayList<Row> rows, ArrayList<Row> orderedRows, SelectQuery sq, ArrayList<String> colNames){
        ResultSet rs = null;
        if(isStar(sq.getSelectedColumnNames())){ // if it is a star
            if(orderedRows != null){
                orderedRows = select(orderedRows);
                rs = new ResultSet(orderedRows,getColumnNames(),getAllDataTypes());
            }
            else if(rows != null){
                rows = select(rows);
                rs = new ResultSet(rows,getColumnNames(),getAllDataTypes());
            }
            else{
                rs = new ResultSet(table,getColumnNames(),getAllDataTypes());
            }
            for (ColumnDescription cd: columns){
                rs.addToDataType(cd.getColumnType());
            }
        }
        return rs;
    }

    /**
     * method returns ArrayList of dataType from the column description array. Nothing is being passed in.
     * @return
     */

    private ArrayList<ColumnDescription.DataType> getAllDataTypes(){
        ArrayList<ColumnDescription.DataType> DataTypes = new ArrayList<ColumnDescription.DataType>();
        for(int i = 0; i < columns.length; i ++){
            DataTypes.add(columns[i].getColumnType());
        }
        return DataTypes;
    }


    /**
     * there are two different "select" methods. One where the query has a star and one where it doesnt.
     * this select method is called when the query HAS a star.
     * @param possibleRows
     * @return
     */
    private ArrayList<Row> select(ArrayList<Row> possibleRows){
        Table newTable = new Table();
        ArrayList<Row> subTable = new ArrayList<Row>();

        for (Row r : possibleRows ) {
            Row r2 = new Row(this.tableName, columns);
            for (int i = 0; i< columns.length; i++){
                r2.insert(r.getRow().get(i));
            }
            subTable.add(r2);
        }
        newTable.columns = columns;
        newTable.table = subTable;
        return newTable.getTable();
    }

    /**
     * there are two different "select" methods. One where the query has a star and one where it doesnt.
     * this select method is called when the query does NOT have a star.
     *
     * @param columnNames
     * @param possibleRows
     * @return
     */
    private ArrayList<Row> select(ColumnID[] columnNames, ArrayList<Row> possibleRows){
        Table newTable = new Table();
        ArrayList<Row> toReturn;
        ArrayList<Row> subTable = new ArrayList<Row>();
        ArrayList<Integer> indices = new ArrayList<Integer>();
        for (ColumnID cn: columnNames ) {
            indices.add(getColumnIndex(cn.getColumnName())); // purpose is to make a subset of column descriptions
        }
        ColumnDescription[] subTableColumnDescriptions = new ColumnDescription[columnNames.length];
        int count = 0;
        for (int i: indices ) {
            ColumnDescription cd2 = columns[i];
            subTableColumnDescriptions[count++] = cd2;
        }
        if(possibleRows !=null){
            toReturn = possibleRows;
        }
        else{
            toReturn = table;
        }
        for (Row r: toReturn ) {
            Row r2 = new Row(this.tableName, subTableColumnDescriptions);
            for (int i: indices ){
                r2.insert(r.getRow().get(i));
            }
            subTable.add(r2);
        }
        newTable.columns = subTableColumnDescriptions;
        newTable.table = subTable;
        return newTable.getTable();
    }


    /**
     * this method is executed whenever there is a where statement
     * note: since we each method is amxed out at 30 lines, the method of returning a subtable
     * is broken down into a couple of methods that follows this.
     * @param where
     * @return
     */

    private ArrayList<Row> getSpecifiedRow(Condition where) { // WHERE TRAVERSAL
        ArrayList<Row> rows = new ArrayList<Row>();
        if (where.getOperator().toString().equalsIgnoreCase("AND")) {
            ArrayList<Row> subTable1 = getSpecifiedRow((Condition) where.getLeftOperand());
            ArrayList<Row> subTable2 = getSpecifiedRow((Condition) where.getRightOperand());

            for (int i = 0; i < subTable1.size(); i++) {
                for (int j = 0; j < subTable2.size(); j++) {
                    if (subTable1.get(i).equals(subTable2.get(j))) {
                        rows.add(subTable1.get(i));
                    }
                }
            }
        } else if (where.getOperator().toString().equalsIgnoreCase("OR")) {
            ArrayList<Row> subTable1 = getSpecifiedRow((Condition) where.getLeftOperand());
            ArrayList<Row> subTable2 = getSpecifiedRow((Condition) where.getRightOperand());
            addAllRows(rows, subTable1);

            for (int i = 0; i < subTable2.size(); i++) {
                if (contains2(subTable1, subTable2.get(i))) {
                    continue;
                } else {
                    rows.add(subTable2.get(i));
                }
            }
        }
        else {
            addAllRows(rows, getSpecifiedRow2(where));
        }
        return rows;
    }

    /**
     * Continuing from the last method... this method returns an array list of rows for which
     * these rows have met the where clause condition
     * @param where
     * @return
     */

    private ArrayList<Row> getSpecifiedRow2(Condition where){
        ArrayList<Row> rows = new ArrayList<Row>();

        if (where.getOperator().toString().equals("=")){
            addAllRows(rows, equalsOp(where.getLeftOperand(), where.getRightOperand()));
        }
        else if (where.getOperator().toString().equals("<>")){
            addAllRows(rows, notEqualsOp(where.getLeftOperand(), where.getRightOperand()));
        }
        else if (where.getOperator().toString().equals("<")){
            addAllRows(rows, lessThanOp(where.getLeftOperand(), where.getRightOperand()));
        }
        else if (where.getOperator().toString().equals("<=")){
            addAllRows(rows, lessThanOrEqualsOp(where.getLeftOperand(), where.getRightOperand()));
        }

        else if (where.getOperator().toString().equals(">")){
            addAllRows(rows, greaterThanOp(where.getLeftOperand(), where.getRightOperand()));
        }
        else if (where.getOperator().toString().equals(">=")){
            addAllRows(rows,greaterThanOrEqualsOp(where.getLeftOperand(), where.getRightOperand()));
        }
        return rows;
    }


    /**
     * this simple boolean just checks if the query has a star or not
     * @param selectedColumns
     * @return
     */
    private boolean isStar(ColumnID[] selectedColumns){
        if (selectedColumns.length ==1 && selectedColumns[0].getColumnName().equals("*")){
            return true;
        }
        return false;
    }


    /**
     * this method checks for distinction and calls upon the distinct method if the query is indeed distinct
     * it returns a result set.
     * @param rs
     * @param sq
     * @param functionRow
     * @param newCnames
     * @param cids
     * @return
     */

    private ResultSet isDistinct(ResultSet rs, SelectQuery sq, Row functionRow, ArrayList<String> newCnames, ColumnID[] cids){
        ArrayList<ColumnDescription.DataType> dataTypes = new ArrayList<ColumnDescription.DataType>();
        for(ColumnDescription cd: columns){
           dataTypes.add(cd.getColumnType());
        }

        if (sq.isDistinct()){
            rs = distinctTable(rs, cids);
        }
        if (rs == null) {
            rs = new ResultSet(functionRow,newCnames,dataTypes);
        }
        return rs;
    }

    /**
     * this distinct table method removes all the duplicates for which we want distinct of
     * flag helps us keep an understand of what is duplicate and what isnt
     *
     *
     * @param resultSet
     * @param cids
     * @return
     */
    private ResultSet distinctTable(ResultSet resultSet, ColumnID[] cids){
        ArrayList<Integer> columns = getColumnIndices(resultSet, cids);
        ArrayList<Integer> removables = new ArrayList<Integer>();
        for(int i = 0; i < resultSet.getRows().size() - 1; i++){
            for(int j = i + 1; j < resultSet.getRows().size(); j++){
                int flag = 0;
                for(int k : columns){
                    if(resultSet.getRows().get(i).row.get(k) == null && resultSet.getRows().get(j).row.get(k) == null) { continue; }

                    else if(resultSet.getRows().get(i).row.get(k) == null){   flag =1;   }

                    else if(resultSet.getRows().get(j).row.get(k) == null){   flag =1;   }

                    else if(resultSet.getRows().get(i).row.get(k).toString().equals(resultSet.getRows().get(j).row.get(k).toString())){ continue; }

                    else{ flag =1; }
                }
                if(flag == 0){
                    removables.add(j);
                }
            }
        }
        int rCount = 0;
        removeDups(removables);
        for(int i : removables){
            resultSet.getRows().remove(i-rCount++);
        }
        return resultSet;
    }


    /**
     * this method is called in the method above.
     * it removes the duplicates...
     * @param ints
     */

    private void removeDups(ArrayList<Integer> ints){
        int counter = 0;
        while(true){
            try{
                if(contains(ints,ints.get(counter)) == 1){
                    ints.remove(counter);
                }
            }catch(IndexOutOfBoundsException e){
                break;
            }
            counter++;
        }
    }


    /**
     * this contains methed is use din the removeDups methos above.
     * it helps properly execute the removeDups method
     * @param ints
     * @param i
     * @return
     */
    private int contains(ArrayList<Integer> ints, int i){
        int j = -2;
        for(int k = 0 ; k < ints.size(); k++){
            if(ints.get(k) == i){
                j++;
                if(j==0){
                    j = k;
                    break;
                }
            }
        }
        if(j > 0){
            return 1;
        }
        return 0;
    }


    /**
     * This method returns an ArrayList of integers that hold the column indexes of the column Ids in the result set.
     * It is used in the "distinctTable" method
     * @param rs
     * @param cids
     * @return
     */
    private ArrayList<Integer> getColumnIndices(ResultSet rs, ColumnID[] cids) {
        ArrayList<Integer> returnIntegers = new ArrayList<Integer>();
        for(String colName : rs.getColumns()){
            for(int i = 0; i < cids.length; i++){
                if(colName.equalsIgnoreCase(cids[i].getColumnName())){
                    returnIntegers.add(i);
                }
            }
        }
        return returnIntegers;
    }

//    private Row makeRow(ObjectArray oa){
//        Row r = new Row();
//        for(Object o : oa.row){
//            r.insert((String)o);
//        }
//        return r;
//    }


    /**
     * this methos is the over arching function method. It returns a row with the proper
     * function(s) that were called on at least one column
     * @param sq
     * @param validRows
     * @return
     */
    private Row functionMethod(SelectQuery sq, ArrayList<Row> validRows){
        ArrayList<SelectQuery.FunctionInstance> functionInstanceArrayList = sq.getFunctions();
        ArrayList<String> columnNames = new ArrayList<String>();
        Row r = new Row();
        for (SelectQuery.FunctionInstance function : functionInstanceArrayList) {
            columnNames.add(function.column.getColumnName());
            if (function.function.toString().equalsIgnoreCase("AVG")){
                String average = String.valueOf(averageFunction(function, validRows));
                r.getRow().add(average);
            }
            else if(function.function.toString().equalsIgnoreCase("COUNT")){
                String count = String.valueOf(countFunction(function, validRows));
                r.getRow().add(count);
            }
            else if (function.function.toString().equalsIgnoreCase("MAX")){
                String max = maxFunction(function, validRows);
                r.getRow().add(max);
            }
            else if (function.function.toString().equalsIgnoreCase("MIN")){
                String min = minFunction(function, validRows);
                r.getRow().add(min);
            }
            else if (function.function.toString().equalsIgnoreCase("SUM")){
                String sum = String.valueOf(sumFunction(function, validRows));
                r.getRow().add(sum);
            }
        }
        return r;
    }

    /**
     * the specs of the project indicated that SUM would only be called on a column that has a number value
     * so this method works on that indication.
     * the sumFunction returns the sum of all the values from a specified column
     * @param function
     * @param validRows
     * @return
     */
    private double sumFunction(SelectQuery.FunctionInstance function,  ArrayList<Row> validRows){
       String columnName = function.column.getColumnName();
       int index = getColumnIndex(columnName);
       double sum = 0;

       for (Row r: validRows) {
           if (r.getRow().get(index).equalsIgnoreCase("null")){
               continue;
           }
           sum = Double.parseDouble(r.getRow().get(index)) + sum;
       }
       return sum;

   }

    /**
     * min function returns the min value in a column.
     * Note that since im saving values as strings, the comparison for double and integers are a bit off so id need to parse it
     * 2 separate methods were created for just that. 1 for double and 1 for integer.
     * @param function
     * @param validRows
     * @return
     */

    private String minFunction(SelectQuery.FunctionInstance function,  ArrayList<Row> validRows){
        String columnName = function.column.getColumnName();
        int index = getColumnIndex(columnName);

        String dataType = getColumnType(columnName);
        if(dataType.equalsIgnoreCase("DECIMAL") || dataType.equalsIgnoreCase("INT")){
            return minFunctionForNumber(function, validRows, dataType, index);
        }

        String min = "";
        int count = 0;
        for (Row r: validRows) {
            if (r.getRow().get(index).equalsIgnoreCase("null")){
                continue;
            }
            if (count == 0){
                min = r.getRow().get(index);
                count ++;
                continue;
            }
            if (min.compareToIgnoreCase(r.getRow().get(index)) > 0)
                min = r.getRow().get(index);
        }
        return min;
    }

    /**
     * returns the min value of the column that holds integers
     * @param function
     * @param validRows
     * @param dataType
     * @param index
     * @return
     */

    private String minFunctionForNumber(SelectQuery.FunctionInstance function,  ArrayList<Row> validRows, String dataType, int index){
        if(dataType.equalsIgnoreCase("INT")) {
            int min = 0;
            int count = 0;
            for (Row r : validRows) {
                if (r.getRow().get(index).equalsIgnoreCase("null")) {
                    continue;
                }
                if (count == 0) {
                    min = Integer.parseInt(r.getRow().get(index));
                    count++;
                    continue;
                }
                if (min > Integer.parseInt(r.getRow().get(index))) {
                    min = Integer.parseInt(r.getRow().get(index));

                }
            }
            return String.valueOf(min);
        }
        else{
            String min = minForDouble(function,  validRows, dataType, index);
            return min;
        }
    }

    /**
     * returns the min value of a column that holds double
     * @param function
     * @param validRows
     * @param dataType
     * @param index
     * @return
     */

    private String minForDouble(SelectQuery.FunctionInstance function,  ArrayList<Row> validRows, String dataType, int index){
        double min = 0;
        int count = 0;
        for (Row r : validRows) {
            if (r.getRow().get(index).equalsIgnoreCase("null")) {
                continue;
            }
            if (count == 0) {
                min = Double.parseDouble(r.getRow().get(index));
                count++;
                continue;
            }
            if (min > Double.parseDouble(r.getRow().get(index))) {
                min = Double.parseDouble(r.getRow().get(index));

            }
        }
        return String.valueOf(min);
    }


    /**
     * Max fucntion returns the Max value in a column.
     * Note that since im saving values as strings, the comparison for double and integers are a bit off so id need to parse it.
     * 2 separate methods were created for just that. 1 for double and 1 for integer.
     * @param function
     * @param validRows
     * @return
     */
    private String maxFunction(SelectQuery.FunctionInstance function,  ArrayList<Row> validRows){
        String columnName = function.column.getColumnName();
        int index = getColumnIndex(columnName);

        String dataType = getColumnType(columnName);
        if(dataType.equalsIgnoreCase("DECIMAL") || dataType.equalsIgnoreCase("INT")){
            return maxFunctionForNumber(function, validRows, dataType, index);
        }
        int count = 0;
        String max ="";
        for (Row r: validRows) {
            if (r.getRow().get(index).equalsIgnoreCase("null")){
                continue;
            }
            if (count == 0){
                max = r.getRow().get(index);
                count ++;
                continue;
            }
            if (max.compareToIgnoreCase(r.getRow().get(index)) < 0)
                max = r.getRow().get(index);
        }
        return max;
    }


    /**
     *  returns the Max value of a column that holds Integer
     * @param function
     * @param validRows
     * @param dataType
     * @param index
     * @return
     */
    private String maxFunctionForNumber(SelectQuery.FunctionInstance function,  ArrayList<Row> validRows, String dataType, int index){
        if(dataType.equalsIgnoreCase("INT")) {
            int max = 0;
            int count = 0;
            for (Row r : validRows) {
                if (r.getRow().get(index).equalsIgnoreCase("null")) {
                    continue;
                }
                if (count == 0) {
                    max = Integer.parseInt(r.getRow().get(index));
                    count++;
                    continue;
                }
                if (max < Integer.parseInt(r.getRow().get(index))) {
                    max = Integer.parseInt(r.getRow().get(index));

                }
            }
            return String.valueOf(max);
        }
        else{
            String max = maxForDouble(function,  validRows, dataType, index);
            return max;
        }

    }

    /**
     * returns the Max value of a column that holds double
     * @param function
     * @param validRows
     * @param dataType
     * @param index
     * @return
     */

    private String maxForDouble (SelectQuery.FunctionInstance function,  ArrayList<Row> validRows, String dataType, int index){

        double max = 0;
        int count = 0;
        for (Row r : validRows) {
            if (r.getRow().get(index).equalsIgnoreCase("null")) {
                continue;
            }
            if (count == 0) {
                max = Double.parseDouble(r.getRow().get(index));
                count++;
                continue;
            }
            if (max < Double.parseDouble(r.getRow().get(index))) {
                max = Double.parseDouble(r.getRow().get(index));

            }
        }
        return String.valueOf(max);
    }


    /**
     * count just returns the amount of values there are in a columns
     * doesnt count null values.
     * @param function
     * @param validRows
     * @return
     */
    private int countFunction(SelectQuery.FunctionInstance function,  ArrayList<Row> validRows){
        String columnName = function.column.getColumnName();
        int index = getColumnIndex(columnName);
        int count = 0;
        for (Row r: validRows) {
            if (!r.getRow().get(index).equalsIgnoreCase("null")){
                count++;
            }
        }
        return count;
    }

    /**
     * The project indicated that average was on numbers only.
     * This method returns the average of a column that has a number.
     * @param function
     * @param validRows
     * @return
     */
    private double averageFunction(SelectQuery.FunctionInstance function, ArrayList<Row> validRows){
        String columnName = function.column.getColumnName();
        int index = getColumnIndex(columnName);
        double count = 0;
        double total = 0;

        for (Row r: validRows) {
            if (r.getRow().get(index).equalsIgnoreCase("null")){
                continue;
            }
            total = Double.parseDouble(r.getRow().get(index)) + total;
            count++;
        }
        double average = total/count;
        average = Math.round(average * 100.0)/100.0;
        return average;

    }


    /**
     * NOTE: This method is being called in RunnerSelect.
     * This doOrderBy method is the overArching orderBy method.
     * It called the "orderByTable" method below which orders the table we pass it into the proper order.
     *
     * @param rows
     * @param sq
     * @return
     */



    private ArrayList<Row> doOrderBy(ArrayList<Row> rows, SelectQuery sq){
        ArrayList<Row> orderedRows = null;
        if(rows == null){ // if there is no WHERE clause
            if (sq.getOrderBys().length>=1){
                orderedRows = select(orderByTable(sq.getOrderBys(), table));
            }
        }else{
            if (sq.getOrderBys().length>=1){
                orderedRows = select(orderByTable(sq.getOrderBys(), rows));
            }
        }
        return orderedRows;
    }

    /**
     * This method returns a String which is the column type of the columnName we pass it in
     * @param columnName
     * @return
     */

    private String getColumnType(String columnName){
        String dataType = null;
        for (ColumnDescription cd: columns){
            if (cd.getColumnName().equalsIgnoreCase(columnName)){
                dataType =cd.getColumnType().toString();
            }
        }
        return dataType;
    }


    /**
     * This method returns an ArrayList of Rows which is the rows ordered correctly.
     * In this method we have an orderByArray which is an array where at each cell it indicates
     * if the query is asking for ASC or DESC. After each recursion, we delete the first cell so
     * we stay consistent when we look at the first cell of the array
     * @param sq
     * @param whereSubtable
     * @return
     */

    private ArrayList<Row> orderByTable(SelectQuery.OrderBy[] sq, ArrayList<Row> whereSubtable) {
        ArrayList<Row> tb;
        if (whereSubtable == null){
            tb = (ArrayList<Row>) this.table.clone();
        }
        else{
            tb = (ArrayList<Row>) whereSubtable.clone();
        }
        SelectQuery.OrderBy[] orderByArray = sq;
        int index = getColumnIndex(orderByArray[0].getColumnID().getColumnName());
        String datatype = getColumnType(orderByArray[0].getColumnID().getColumnName());
        if(orderByArray[0].isAscending()){
            tb = orderByAscending(tb, index, datatype ); // call ascending method
            if (sq.length ==0){
                return tb;
            } else {
                tb = orderByRecursionCall(index, orderByArray, tb);
                }
                return tb;
        }
        else if(orderByArray[0].isDescending()){
            tb = orderByDescending(tb, index, datatype);
            if (sq.length ==0){
                return tb;
            } else {
                tb = orderByRecursionCall(index, orderByArray, tb);
                }
            return tb;
        }
        return null;
    }

    /**
     * If there are multiple orderBys, this method is called. It minimized the order by array in order to preform the
     * task of ordering again. We preform recursion by calling the "orderByRecursionCall2" method below this method.
     *
     * We return the ordered table once the ordeByArray is of length 0
     *
     * @param index
     * @param orderByArray
     * @param tb
     * @return
     */


    private ArrayList<Row> orderByRecursionCall(int index, SelectQuery.OrderBy[] orderByArray, ArrayList<Row> tb){
        int index2 = index;
        SelectQuery.OrderBy[] orderByArray2 = new SelectQuery.OrderBy[orderByArray.length - 1];
        for (int i = 0; i < orderByArray2.length; i++) {
            orderByArray2[i] = orderByArray[i + 1];
        }
        if (orderByArray2.length==0){
            return tb;
        }
        ArrayList<Row> tb3 = new ArrayList<Row>();
        tb3 = orderByRecursionCall2(orderByArray2, tb3, tb, index2);
        return tb;
    }

    /**
     * this method preforms the recursion aspect of the orderBy function.
     * It will group the rows in proper order no mater how many functions its given. It can do this
     * because of the recursion stack trace.
     * After its finished proper grouping the multi orderBy table, it returns it.
     *
     * @param orderByArray2
     * @param tb3
     * @param tb
     * @param index2
     * @return
     */

    private ArrayList<Row> orderByRecursionCall2(SelectQuery.OrderBy[] orderByArray2,ArrayList<Row> tb3, ArrayList<Row> tb, int index2 ){
        for (int i = 0; i < tb.size() ; i++) {
            if (tb3.size() == 0) {
                tb3.add(tb.get(i));
            } else if (tb.get(i - 1).getRow().get(index2).equalsIgnoreCase(tb.get(i).getRow().get(index2))) {
                tb3.add(tb.get(i));

            } else {
                tb3.remove(0);
                tb3.add(tb.get(i));
                continue;
            }
            if(i != tb.size()-1){
                if(tb.get(i + 1).getRow().get(index2).equalsIgnoreCase(tb.get(i).getRow().get(index2))){
                    continue;
                }
            }
            if (tb3.size() > 1 ) {
                int sizeOfTb3 = tb3.size();
                ArrayList<Row> tb4 = orderByTable( orderByArray2, tb3); // recursive call
                for (int k = 0; k < tb4.size(); k++) {
                    tb.set(i - sizeOfTb3 + k + 1, tb4.get(k));
                }
                tb3 = new ArrayList<Row>();
                i--;
            }
        }
        return tb3;
    }

    /**
     * This method does orders by ascending order. Using Selection Sort here
     * Note: When we are comparing numbers, the string comparison does work properly so
     * ive created different methods depending on whether its a number or not
     *
     * https://www.youtube.com/watch?v=cqh8nQwuKNE
     * used the link above to help write ASC and DESC code
     *
     * @param tb
     * @param index
     * @param dataType
     * @return
     */

    private ArrayList<Row> orderByAscending(ArrayList<Row> tb, int index, String dataType){
        if(dataType.equalsIgnoreCase("DECIMAL")){
            tb = orderByAscendingDecimal(tb,  index);
            return tb;
        }
        else if (dataType.equalsIgnoreCase("INT")){
            tb = orderByAscendingInteger(tb, index);
            return tb;
        }
        tb = breakingDownAscending(tb, index, dataType);
        return tb;
    }

    /**
     * this method carries over from the previous method. Didnt want to make a monster method so broke it down.
     * Null values are treated accordingly
     * @param tb
     * @param index
     * @param dataType
     * @return
     */

    private ArrayList<Row> breakingDownAscending(ArrayList<Row> tb, int index, String dataType){
        for (int i = 0; i < tb.size(); i++) {
            String minValue = tb.get(i).getRow().get(index);
            int minIndex = i;
            for (int j = i; j < tb.size(); j++) {
                if (minValue.equalsIgnoreCase("null")){
                    continue;
                }
                else if (minValue.compareToIgnoreCase(tb.get(j).getRow().get(index)) > 0|| tb.get(j).getRow().get(index).equalsIgnoreCase("null") ) {
                    minValue = tb.get(j).getRow().get(index);
                    minIndex=j;
                }
                if((minValue.equalsIgnoreCase("null") && !tb.get(i).getRow().get(index).equalsIgnoreCase("Null"))){
                    Row temp = tb.get(i);
                    tb.set(i, tb.get(minIndex));
                    tb.set(minIndex, temp);
                }
                else if ( minValue.compareToIgnoreCase(tb.get(i).getRow().get(index)) <  0){
                    Row temp = tb.get(i);
                    tb.set(i, tb.get(minIndex));
                    tb.set(minIndex, temp);
                }
            }
        }
        return tb;
    }

    /**
     * Order ascending when dealing with INT
     * null values are treated accordingly
     * @param tb
     * @param index
     * @return
     */

    private ArrayList<Row> orderByAscendingInteger(ArrayList<Row> tb, int index){
        for (int i = 0; i < tb.size(); i++) {
            String minValue = tb.get(i).getRow().get(index);
            int minIndex = i;
            for (int j = i; j < tb.size(); j++) {
                if (minValue.equalsIgnoreCase("null")){
                    continue;
                }
                else if(tb.get(j).getRow().get(index).equalsIgnoreCase("null")){
                    minValue = tb.get(j).getRow().get(index);
                    minIndex=j;
                }
                else if(Integer.parseInt(minValue) > Integer.parseInt(tb.get(j).getRow().get(index)) || tb.get(j).getRow().get(index).equalsIgnoreCase("null") ){
                    minValue = tb.get(j).getRow().get(index);
                    minIndex=j;
                }
                if((minValue.equalsIgnoreCase("null") && !tb.get(i).getRow().get(index).equalsIgnoreCase("Null"))){
                    Row temp = tb.get(i);
                    tb.set(i, tb.get(minIndex));
                    tb.set(minIndex, temp);
                }
               else if (Integer.parseInt(minValue) <  Integer.parseInt(tb.get(i).getRow().get(index))){
                    Row temp = tb.get(i);
                    tb.set(i, tb.get(minIndex));
                    tb.set(minIndex, temp);
                }
            }
        }
        return tb;
    }

    /**
     * order Ascending when dealing with DECIMAL
     * Null values are treated accordingly.
     * @param tb
     * @param index
     * @return
     */

    private ArrayList<Row> orderByAscendingDecimal(ArrayList<Row> tb, int index){
        for (int i = 0; i < tb.size(); i++) {
            String minValue = tb.get(i).getRow().get(index);
            int minIndex = i;
            for (int j = i; j < tb.size(); j++) {
                if (minValue.equalsIgnoreCase("null")){
                    continue;
                }
                else if(tb.get(j).getRow().get(index).equalsIgnoreCase("null")){
                    minValue = tb.get(j).getRow().get(index);
                    minIndex=j;
                }
                else if(Double.parseDouble(minValue) > Double.parseDouble(tb.get(j).getRow().get(index)) || tb.get(j).getRow().get(index).equalsIgnoreCase("null")){
                    minValue = tb.get(j).getRow().get(index);
                    minIndex=j;
                }
                if((minValue.equalsIgnoreCase("null") && !tb.get(i).getRow().get(index).equalsIgnoreCase("Null"))){
                    Row temp = tb.get(i);
                    tb.set(i, tb.get(minIndex));
                    tb.set(minIndex, temp);
                }
                else if (Double.parseDouble(minValue) <  Double.parseDouble(tb.get(i).getRow().get(index))){
                    Row temp = tb.get(i);
                    tb.set(i, tb.get(minIndex));
                    tb.set(minIndex, temp);
                }
            }
        }
        return tb;
    }


    /**
     * Method works much like order by ascending but this time we order by Descending
     * @param tb
     * @param index
     * @param dataType
     * @return
     */

    private ArrayList<Row> orderByDescending(ArrayList<Row> tb, int index, String dataType){
        if(dataType.equalsIgnoreCase("DECIMAL")){
            tb = orderByDescendingDecimal(tb,  index);
            return tb;
        }
        else if (dataType.equalsIgnoreCase("INT")){
            tb = orderByDescendingInteger(tb, index);
            return tb;
        }
        tb = breakingDownDescending(tb, index, dataType);
        return tb;
    }

    /**
     * Method works much like order by ascending but this time we order by Descending
     * @param tb
     * @param index
     * @param dataType
     * @return
     */

    private ArrayList<Row> breakingDownDescending(ArrayList<Row> tb, int index, String dataType){
        for (int i = 0; i < tb.size(); i++) {
            String maxValue = tb.get(i).getRow().get(index);
            int maxIndex = i;
            for (int j = i; j < tb.size(); j++) {
                if(!maxValue.equalsIgnoreCase("null") && tb.get(j).getRow().get(index).equalsIgnoreCase("null")){
                    continue;
                }
                else if(maxValue.equalsIgnoreCase("null") && !tb.get(j).getRow().get(index).equalsIgnoreCase("null")){
                    maxValue = tb.get(j).getRow().get(index);
                    maxIndex=j;
                }
                else if (maxValue.compareToIgnoreCase(tb.get(j).getRow().get(index)) < 0) {
                    maxValue = tb.get(j).getRow().get(index);
                    maxIndex=j;
                }
                if(!maxValue.equalsIgnoreCase("null") && tb.get(i).getRow().get(index).equalsIgnoreCase("Null")){
                    Row temp = tb.get(i);
                    tb.set(i, tb.get(maxIndex));
                    tb.set(maxIndex, temp);
                }
                else if (maxValue.compareToIgnoreCase(tb.get(i).getRow().get(index)) > 0){
                    Row temp = tb.get(i);
                    tb.set(i, tb.get(maxIndex));
                    tb.set(maxIndex, temp);
                }
            }
        }
        return tb;
    }

    /**
     * Method works much like order by ascending but this time we order by Descending when dealing with INT
     * @param tb
     * @param index
     * @return
     */

    private ArrayList<Row> orderByDescendingInteger(ArrayList<Row> tb, int index){
        for (int i = 0; i < tb.size(); i++) {
            String maxValue = tb.get(i).getRow().get(index);
            int maxIndex = i;
            for (int j = i; j < tb.size(); j++) {
                if (!maxValue.equalsIgnoreCase("null") && tb.get(j).getRow().get(index).equalsIgnoreCase("null")){  continue;  }

                else if(maxValue.equalsIgnoreCase("null") && !tb.get(j).getRow().get(index).equalsIgnoreCase("null")){
                    maxValue = tb.get(j).getRow().get(index);
                    maxIndex=j;
                }
                else if(maxValue.equalsIgnoreCase("null") && tb.get(j).getRow().get(index).equalsIgnoreCase("null")){ continue; }

                else if(Integer.parseInt(maxValue) < Integer.parseInt(tb.get(j).getRow().get(index))){
                    maxValue = tb.get(j).getRow().get(index);
                    maxIndex=j;
                }
                if(!maxValue.equalsIgnoreCase("null") && tb.get(i).getRow().get(index).equalsIgnoreCase("Null")){
                    Row temp = tb.get(i);
                    tb.set(i, tb.get(maxIndex));
                    tb.set(maxIndex, temp);
                }
                else if(Integer.parseInt(maxValue) > Integer.parseInt(tb.get(i).getRow().get(index))){
                    Row temp = tb.get(i);
                    tb.set(i, tb.get(maxIndex));
                    tb.set(maxIndex, temp);
                }
            }
        }
        return tb;
    }

    /**
     * Method works much like order by ascending but this time we order by Descending when dealing  DECIMAL
     * @param tb
     * @param index
     * @return
     */

    private ArrayList<Row> orderByDescendingDecimal(ArrayList<Row> tb, int index){
        for (int i = 0; i < tb.size(); i++) {
            String maxValue = tb.get(i).getRow().get(index);
            int maxIndex = i;
            for (int j = i; j < tb.size(); j++) {
                if (!maxValue.equalsIgnoreCase("null") && tb.get(j).getRow().get(index).equalsIgnoreCase("null")){ continue; }

                else if(maxValue.equalsIgnoreCase("null") && !tb.get(j).getRow().get(index).equalsIgnoreCase("null")){
                    maxValue = tb.get(j).getRow().get(index);
                    maxIndex=j;
                }
                else if(maxValue.equalsIgnoreCase("null") && tb.get(j).getRow().get(index).equalsIgnoreCase("null")){ continue; }

                else if(Double.parseDouble(maxValue) < Double.parseDouble(tb.get(j).getRow().get(index)) ){
                    maxValue = tb.get(j).getRow().get(index);
                    maxIndex=j;
                }
                if(!maxValue.equalsIgnoreCase("null") && tb.get(i).getRow().get(index).equalsIgnoreCase("Null")){
                    Row temp = tb.get(i);
                    tb.set(i, tb.get(maxIndex));
                    tb.set(maxIndex, temp);
                }
                else if(Double.parseDouble(maxValue) > Double.parseDouble(tb.get(i).getRow().get(index))){
                    Row temp = tb.get(i);
                    tb.set(i, tb.get(maxIndex));
                    tb.set(maxIndex, temp);
                }
            }
        }
        return tb;
    }


    /**
     * this method is used whenever we have a WHERE clause. It is the "=" operator.
     * If our where statement has a column that is indexed, we just retrieve the rows from the btree and
     * return it. Otherwise we go through the table.
     *
     * @param leftOperand
     * @param rightOperand
     * @return
     */
    private ArrayList<Row> equalsOp(Object leftOperand, Object rightOperand){
        String leftOp = leftOperand.toString();
        String rightOp = rightOperand.toString();
        ArrayList<Row> rows = new ArrayList<Row>();
        int index = getColumnIndex(leftOp);
        rightOp = valueForDoubleColumn(columns[index], rightOp); // in case we are dealing with numbers and not the full value was given
        columnsInWhereClause.add(leftOp);
        for(BTree btree: bTreeContainer){
            if (btree.getIndexName().equalsIgnoreCase(leftOp)){
                ArrayList<Row> btreeRows = btree.get(rightOp);
                if(btreeRows==null){
                    return rows; // dont want to be returning a null array list and mess up the program
                }
                btree.setFlag(true); //for Junit purposes: indicating we've successfully accessed the  btree
                return btreeRows;
            }
        }
        for (Row row : table ) {
            if(rightOp.equalsIgnoreCase("null")){
                if(row.getRow().get(index).equalsIgnoreCase(rightOp)){
                    rows.add(row);
                }
            }
            else if(row.getRow().get(index).equalsIgnoreCase("null")){ continue; }

            else if (row.getRow().get(index).equalsIgnoreCase(rightOp)){
             rows.add(row);
            }
        }
        return rows;
    }

    /**
     * this method is used whenever we have a WHERE clause. It is the "<>" operator.
     * If our where statement has a column that is indexed, we just retrieve the rows from the btree and
     * return it. Otherwise we go through the table.
     *
     * @param leftOperand
     * @param rightOperand
     * @return
     */

    private ArrayList<Row> notEqualsOp(Object leftOperand, Object rightOperand){
        String leftOp = leftOperand.toString();
        String rightOp = rightOperand.toString();
        ArrayList<Row> rows = new ArrayList<Row>();
        int columnIndex = getColumnIndex(leftOp);
        rightOp = valueForDoubleColumn(columns[columnIndex], rightOp);

        columnsInWhereClause.add(leftOp);
        for(BTree btree: bTreeContainer){
            if (btree.getIndexName().equalsIgnoreCase(leftOp)){
                ArrayList<Row> btreeRows = btree.getNotEquals(rightOp);
                btree.setFlag(true); //for Junit purposes: indicating we've successfully accessed the  btree
                return btreeRows;
            }
        }
        for (Row row : table ) {
            if(row.getRow().get(columnIndex).equalsIgnoreCase("null") && !rightOp.equalsIgnoreCase("null")){
                continue;
            }
            if (!row.getRow().get(columnIndex).equalsIgnoreCase(rightOp)){
                rows.add(row);
            }
        }
        return rows;
    }

    /**
     * this method is used whenever we have a WHERE clause. It is the "<" operator.
     * If our where statement has a column that is indexed, we just retrieve the rows from the btree and
     * return it. Otherwise we go through the table.
     * @param leftOperand
     * @param rightOperand
     * @return
     */


    private ArrayList<Row> lessThanOp(Object leftOperand, Object rightOperand){
        String leftOp = leftOperand.toString();
        String rightOp = rightOperand.toString();
        ArrayList<Row> rows = new ArrayList<Row>();
        int index = getColumnIndex(leftOp);
        rightOp = valueForDoubleColumn(columns[index], rightOp);

        columnsInWhereClause.add(leftOp);
        for(BTree btree: bTreeContainer){
            if (btree.getIndexName().equalsIgnoreCase(leftOp)){
                ArrayList<Row> btreeRows = btree.getLessThan(rightOp);
                btree.setFlag(true); //for Junit purposes: indicating we've successfully accessed the  btree
                return btreeRows;
            }
        }

        int columnIndex = getColumnIndex(leftOp);
        String dataType = columns[columnIndex].getColumnType().name();

        rows = lessThanPossibilities(rows, leftOp, rightOp, columnIndex, dataType);
        return rows;
    }

    /**
     * This method Supports the method above. It checks the value so it can properly see if its less then.
     * Reminder: we are doing string comparison so we need to make sure comparison is done properly.
     * @param rows
     * @param leftOp
     * @param rightOp
     * @param columnIndex
     * @param dataType
     * @return
     */

    private ArrayList<Row> lessThanPossibilities(ArrayList<Row> rows, String leftOp, String rightOp, int columnIndex, String dataType ){
        for (Row row : table ) {
            if (rightOp.equalsIgnoreCase("null")){
                return rows; //nothing is less than null
            }
            if(row.getRow().get(columnIndex).equalsIgnoreCase("null")){
                rows.add(row);
                continue;
            }
            else if (dataType.equals("VARCHAR")){
                if (row.getRow().get(columnIndex).compareToIgnoreCase(rightOp) < 0) {
                    rows.add(row);
                }
            }
            else if(dataType.equals("DECIMAL")){
                double rightOp2 = Double.parseDouble(rightOp);
                double d = Double.parseDouble(row.getRow().get(columnIndex));
                if(d < rightOp2){
                    rows.add(row);
                }
            }
            else if(dataType.equals("INT")){
                int rightOp3 = Integer.parseInt(rightOp);
                int i = Integer.parseInt(row.getRow().get(columnIndex));
                if(i < rightOp3){
                    rows.add(row);
                }
            }
        }
        return rows;
    }


    /**
     * this method is used whenever we have a WHERE clause. It is the "<=" operator.
     * If our where statement has a column that is indexed, we just retrieve the rows from the btree and
     * return it. Otherwise we go through the table.
     * @param leftOperand
     * @param rightOperand
     * @return
     */

    private ArrayList<Row> lessThanOrEqualsOp(Object leftOperand, Object rightOperand){ // DOUBLE CHECK THAT THE LESS THAN OR EQUALS IS CORRECT
        String leftOp = leftOperand.toString();
        String rightOp = rightOperand.toString();
        ArrayList<Row> rows = new ArrayList<Row>();
        int columnIndex = getColumnIndex(leftOp);
        String dataType = columns[columnIndex].getColumnType().name();
        rightOp = valueForDoubleColumn(columns[columnIndex], rightOp);

        columnsInWhereClause.add(leftOp);
        for(BTree btree: bTreeContainer){
            if (btree.getIndexName().equalsIgnoreCase(leftOp)){
                ArrayList<Row> btreeRows = btree.getLessThanOrEqualsTo(rightOp);
                btree.setFlag(true); //for Junit purposes: indicating we've successfully accessed the  btree
                return btreeRows;
            }
        }
        rows= lessThanOrEqualsPossibilities(rows, leftOp, rightOp, columnIndex, dataType);
        return rows;
    }


    /**
     * This method Supports the method above. It checks the value so it can properly see if its less then.
     * Reminder: we are doing string comparison so we need to make sure comparison is done properly.
     *
     * @param rows
     * @param leftOp
     * @param rightOp
     * @param columnIndex
     * @param dataType
     * @return
     */
    private ArrayList<Row> lessThanOrEqualsPossibilities(ArrayList<Row> rows, String leftOp, String rightOp, int columnIndex, String dataType ){
        for (Row row : table ) {
            if(rightOp.equalsIgnoreCase("null")){
                if(row.getRow().get(columnIndex).equalsIgnoreCase("null")){
                    rows.add(row);
                }
            }
            else if(row.getRow().get(columnIndex).equals("null")){ continue; }

            else if (dataType.equals("VARCHAR")){
                if (row.getRow().get(columnIndex).compareToIgnoreCase(rightOp) <= 0) {
                    rows.add(row);
                }
            }
            else if(dataType.equals("DECIMAL")){
                double rightOp2 = Double.parseDouble(rightOp);
                double d = Double.parseDouble(row.getRow().get(columnIndex));
                if(d <= rightOp2){
                    rows.add(row);
                }
            }
            else if(dataType.equals("INT")){
                int rightOp3 = Integer.parseInt(rightOp);
                int i = Integer.parseInt(row.getRow().get(columnIndex));
                if(i <= rightOp3){
                    rows.add(row);
                }
            }
        }
        return rows;
    }


    /**
     *
     * this method is used whenever we have a WHERE clause. It is the ">" operator.
     * If our where statement has a column that is indexed, we just retrieve the rows from the btree and
     * return it. Otherwise we go through the table.
     *
     * @param leftOperand
     * @param rightOperand
     * @return
     */

    private ArrayList<Row> greaterThanOp(Object leftOperand, Object rightOperand){
        String leftOp = leftOperand.toString();
        String rightOp = rightOperand.toString();
        ArrayList<Row> rows = new ArrayList<Row>();
        int index = getColumnIndex(leftOp);
        rightOp = valueForDoubleColumn(columns[index], rightOp);

        columnsInWhereClause.add(leftOp);
        for(BTree btree: bTreeContainer){
            if (btree.getIndexName().equalsIgnoreCase(leftOp)){
                ArrayList<Row> btreeRows = btree.getGreaterThan(rightOp);
                btree.setFlag(true); //for Junit purposes: indicating we've successfully accessed the  btree
                return btreeRows;
            }
        }
        int columnIndex = getColumnIndex(leftOp);
        String dataType = columns[columnIndex].getColumnType().name();
        rows = greaterThanPossibilities(rows, leftOp, rightOp, columnIndex, dataType);
        return rows;
    }

    /**
     * This method Supports the method above. It checks the value so it can properly see if its less then.
     * Reminder: we are doing string comparison so we need to make sure comparison is done properly.
     *
     *
     * @param rows
     * @param leftOp
     * @param rightOp
     * @param columnIndex
     * @param dataType
     * @return
     */

    private ArrayList<Row> greaterThanPossibilities(ArrayList<Row> rows, String leftOp, String rightOp, int columnIndex, String dataType ){
        for (Row row : table ) {
            if(rightOp.equalsIgnoreCase("null")){
                if (!row.getRow().get(columnIndex).equalsIgnoreCase("null")){
                    rows.add(row);
                }
            }
            else if (row.getRow().get(columnIndex).equals("null")){ continue; }
            else if (dataType.equals("VARCHAR")){
                if (row.getRow().get(columnIndex).compareToIgnoreCase(rightOp) > 0) {
                    rows.add(row);
                }
            }
            else if(dataType.equals("DECIMAL")){
                double rightOp2 = Double.parseDouble(rightOp);
                double d = Double.parseDouble(row.getRow().get(columnIndex));
                if(d > rightOp2){
                    rows.add(row);
                }
            }
            else if(dataType.equals("INT")){
                int rightOp3 = Integer.parseInt(rightOp);
                int i = Integer.parseInt(row.getRow().get(columnIndex));
                if(i > rightOp3){
                    rows.add(row);
                }
            }
        }
        return rows;
    }


    /**
     * Returns greater than or equals
     * @param leftOperand
     * @param rightOperand
     * @return
     */

    private ArrayList<Row> greaterThanOrEqualsOp(Object leftOperand, Object rightOperand){
        String leftOp = leftOperand.toString();
        String rightOp = rightOperand.toString();
        ArrayList<Row> rows = new ArrayList<Row>();
        int columnIndex = getColumnIndex(leftOp);
        String dataType = columns[columnIndex].getColumnType().name();
        rightOp = valueForDoubleColumn(columns[columnIndex], rightOp);

        columnsInWhereClause.add(leftOp);
        for(BTree btree: bTreeContainer){
            if (btree.getIndexName().equalsIgnoreCase(leftOp)){
                ArrayList<Row> btreeRows = btree.getGreaterThanOrEqualsTo(rightOp);
                btree.setFlag(true); //for Junit purposes: indicating we've successfully accessed the  btree
                return btreeRows;
            }
        }

        rows = greaterThanOrEqualsPossibilities(rows, leftOp, rightOp, columnIndex, dataType);
        return rows;
    }

    /**
     * helps method above. Compares the proper values accordingly
     * @param rows
     * @param leftOp
     * @param rightOp
     * @param columnIndex
     * @param dataType
     * @return
     */

    private ArrayList<Row> greaterThanOrEqualsPossibilities(ArrayList<Row> rows, String leftOp, String rightOp, int columnIndex, String dataType ){
        for (Row row : table ) {

            if(rightOp.equalsIgnoreCase("null")){ // greater than or equals to null pretty much means add everything
                rows.add(row);
            }
            else if(row.getRow().get(columnIndex).equals("null")){ continue; }

            else if (dataType.equals("VARCHAR")){
                if (row.getRow().get(columnIndex).compareToIgnoreCase(rightOp) >= 0) { // NOT SO SURE
                    rows.add(row);
                }
            }
            else if(dataType.equals("DECIMAL")){
                double rightOp2 = Double.parseDouble(rightOp);
                double d = Double.parseDouble(row.getRow().get(columnIndex));
                if(d >= rightOp2){
                    rows.add(row);
                }
            }
            else if(dataType.equals("INT")){
                int rightOp3 = Integer.parseInt(rightOp);
                int i = Integer.parseInt(row.getRow().get(columnIndex));
                if(i >= rightOp3){
                    rows.add(row);
                }
            }
        }
        return rows;
    }


    /**
     * this method deletes rows from the table.
     * Note: when deleting a row from the table, we need to delete that row from all Btrees as well!
     * @param sq
     */


    public void deleteRows( DeleteQuery sq){
        if(sq.getWhereCondition() != null ){
            ArrayList<Row> validRows = getSpecifiedRow(sq.getWhereCondition());

            for(BTree bt: bTreeContainer){
                for(Row r: validRows){
                    bt.deleteTree1(r);
                    bt.setFlag(true); //for Junit purposes: indicating we've successfully accessed the  btree
                }
            }
            for(int i = 0; i < validRows.size(); i++){
                for(int j = 0; j < table.size(); j++){
                    if (validRows.get(i).equals(table.get(j))){
                        table.remove(j);
                    }
                }
            }
        }
        else {
            for(BTree bt: bTreeContainer){
                bt.deleteAll();
                bt.setFlag(true); //for Junit purposes: indicating we've successfully accessed the  btree
            }
            int loops = table.size();
            for (int i =0; i < loops; i++) {
                table.remove(0);
            }
        }
    }


    /**
     * this method updates a row. Its the over arching update method.
     * the method calls upon other methods below to properly check that the value given is
     * valid, and if need be, unique.
     *
     *
     *
     *
     * @param sq
     */

    public void update(UpdateQuery sq ){
        ArrayList<Row> validRows = table;
        if (sq.getWhereCondition() != null) {
            validRows = getSpecifiedRow(sq.getWhereCondition());
        }
        for (int i = 0; i < validRows.size(); i++){
            for (ColumnValuePair columnValuePair :sq.getColumnValuePairs()) {

                int index = getColumnIndex(columnValuePair.getColumnID().getColumnName());
                String value = columnValuePair.getValue();

                value =checkUpdateRequirements(columnValuePair, index, value);
                removeInvalidRow(index, value, validRows, columnValuePair);

                for(BTree bt: bTreeContainer){
                    String columnName = columnValuePair.getColumnID().getColumnName();
                    bt.updateTree1(index, value, validRows.get(i), columnName);
                    bt.setFlag(true); //indicating we've successfully accessed the  btree
                }

            }
        }

    }

    /**
     * This method is used above and pretty much checks for that the column we want to update stays consistent
     * with its uniqueness (obv this applies to a column that is a primary key and is unique)
     * This method is similar to checkUnique but this is a more specific methos testing columns. Other method tests Rows.
     * @param index
     * @param value
     * @param validRows
     * @param columnValuePair
     */

    public void removeInvalidRow(int index, String value, ArrayList<Row> validRows, ColumnValuePair columnValuePair ){

        for(ColumnDescription cd: columns){
            if((cd.isUnique() || cd.getColumnName().equalsIgnoreCase(primaryKey.getColumnName())) && cd.getColumnName().equalsIgnoreCase(columnValuePair.getColumnID().getColumnName())){
                for (int i = 0; i< validRows.size(); i++){
                    if (validRows.get(i).getRow().get(index).equalsIgnoreCase(value)){
                        throw new IllegalArgumentException("Couldnt Update " + columnValuePair.getColumnID().getColumnName() + " didnt satisfy uniqueness " );
                    }
                }
            }
        }
    }

    /**
     * This method takes the value given to us and kind of refactors it into its "proper form"
     *
     * @param cv
     * @param index
     * @param value
     * @return
     */

    private String checkUpdateRequirements(ColumnValuePair cv, int index, String value){

        if(varcharUpdate(cv, index,value) == false){
            throw new IllegalArgumentException("not a valid input to update column " + cv.getColumnID().getColumnName());
        }
        value = decimalUpdate(cv,index,value);
        value = integerUpdate(cv, index, value);
        value = booleanUpdate(cv, index, value);

        if(value.equalsIgnoreCase("null")){
            value = "null";
        }

        if(!checkForNotNull(cv, value)){
            throw new IllegalArgumentException("Cant input null value!");
        }
        return value;
    }

    /**
     * checks to see if the value given for a boolean was indeed a boolean value
     * @param cv
     * @param index
     * @param value
     * @return
     */

    private String booleanUpdate(ColumnValuePair cv, int index, String value){
        if((columns[index].getColumnType().name().equals("BOOLEAN") && !isBoolean(columns[index], value))){
            throw new IllegalArgumentException("Didnt properly fit the requirements for the boolean dataType");
        }
        return value;
    }

    /**
     * used in the method above. Uses the same logic as above
     * @param cd
     * @param value
     * @return
     */

    private boolean isBoolean(ColumnDescription cd, String value){

        if (cd.isNotNull() == false && value.equalsIgnoreCase("NULL")){
            return true;
        }

        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks to see if an integer was actually given for an INT column
     * @param cv
     * @param index
     * @param value
     * @return
     */

    private String integerUpdate(ColumnValuePair cv, int index, String value){
        if((columns[index].getColumnType().name().equals("INT") && !isInteger(columns[index], value))){
            throw new IllegalArgumentException("Didnt properly fit the requirements for the INTEGER dataType");
        }
        return value;
    }

    /**
     * Uses logic explained above. If indeed an INT column then attempt to parse.
     * @param cd
     * @param value
     * @return
     */
    private boolean isInteger(ColumnDescription cd, String value){ //using for update
        if (cd.isNotNull() == false && value.equalsIgnoreCase("NULL")){
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
     * Checks to see a string was given for a VARCHAR column
     * @param cv
     * @param index
     * @param value
     * @return
     */

    private boolean varcharUpdate(ColumnValuePair cv, int index, String value){

        if (columns[index].getColumnType().name().equals("VARCHAR") && value.charAt(0) != '\''){
            if ( columns[index].isNotNull() == false && value.equalsIgnoreCase("NULL")){
                return true;
            }
            else{
                return false;
            }
        }
        else if (columns[index].getColumnType().name().equalsIgnoreCase("VARCHAR") &&value.length()> columns[index].getVarCharLength()){
            return false;
        }
        return true;
    }

    /**
     * This method checks that a null value wasnt given for a not null column.
     * @param cv
     * @param value
     * @return
     */

    private boolean checkForNotNull(ColumnValuePair cv, String value){
        for (ColumnDescription cd: columns){
            if(cd.isNotNull() && cv.getColumnID().getColumnName().equals(cd.getColumnName())){
                if(value.equalsIgnoreCase("NULL")){
                    return false;
                }
            }
            if (cv.getColumnID().getColumnName().equals(primaryKey.getColumnName())){
                if(value.equalsIgnoreCase("NULL")){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks to see if an decimal was actually given for an DECIMAL column
     *
     * @param cv
     * @param index
     * @param value
     * @return
     */

    private String decimalUpdate(ColumnValuePair cv, int index, String value){

        if((columns[index].getColumnType().name().equals("DECIMAL") && !isDecimal(columns[index], value))){
            throw new IllegalArgumentException("Didnt properly fit the requirements for the decimal dataType");
        }
        else if((columns[index].getColumnType().name().equals("DECIMAL"))){
            value = valueForDoubleColumn(columns[index], value);
        }
        return value;
    }

    /**
     * Method continues from the logic above...
     * @param cd
     * @param value
     * @return
     */

    private boolean isDecimal(ColumnDescription cd, String value){ //using for Update
        if (cd.isNotNull()==false && value.equals("NULL")){
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
     * this method takes a decimal and pretty much fills in the values. i.e. if 3.0 was passed, its returned as 3.00
     * and keeps it like that in the table/btree
     * @param cd
     * @param value
     * @return
     */
    private String valueForDoubleColumn(ColumnDescription cd, String value){
        if(value.equals("NULL")){
            return value;
        }
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
     * returns an array list of columnNames from the column Description array
     * @return
     */

    public ArrayList<String> getColumnNames(){
        ArrayList<String> colNames = new ArrayList<String>();
        for(int i = 0; i<columns.length; i ++){
            colNames.add(columns[i].getColumnName());
        }
        return colNames;
    }

    /**
     * gets an index of a column
     * @param columnName
     * @return
     */

    public int getColumnIndex(String columnName){

        for (int i =0; i < columns.length; i++){
            if (columns[i].getColumnName().equalsIgnoreCase(columnName)){
                return i;
            }
        }
        return -1;

    }

    /**
     * couldnt use an "addALL" method since the specs in the project didnt allow it, so I created my own
     * @param row
     * @param row2
     */
    private void addAllRows (ArrayList<Row> row, ArrayList<Row> row2){
        for (Row rows: row2 ) {
            row.add(rows);
        }
    }

    /**
     * couldnt use an ".contains()" method since the specs in the project didnt allow it, so I created my own
     *
     * @param subtable1
     * @param row2
     * @return
     */

    private boolean contains2(ArrayList<Row> subtable1, Row row2){
        for(Row row: subtable1){
            if (row.equals(row2)){
                return true;
            }
        }
        return false;
    }

    /**
     * returns table
     * @return
     */

    public ArrayList<Row> getTable() {
        return table;
    }

    /**
     * prints table
     */

    public void printTable(){
       // System.out.println("**************************************************************************************");
        System.out.println("Table Name: " + this.tableName);
        System.out.println(" ");

        for (ColumnDescription c : this.columns) {
            System.out.printf("%-36s", c.getColumnName() + "(" + c.getColumnType().toString() + ')');
        }
        System.out.println();
        int i = 0;

        for (Row row : this.table) {
            for (String index : row.getRow()) {
                System.out.printf("%-38s", index.toString());
                continue;
            }
            System.out.println();
        }
        System.out.println("***************************************************************************************");


    }

    @Override
    public String toString() {
        return "Table{" + "table=" + table + '}';
    }
}
