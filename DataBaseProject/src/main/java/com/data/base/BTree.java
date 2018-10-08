package com.data.base;


/******************************************************************************
 *  Compilation:  javac BTree.java
 *  Execution:    java BTree
 *  Dependencies: StdOut.java
 *
 *  B-tree.
 *
 *  Limitations
 *  -----------
 *   -  Assumes M is even and M >= 4
 ******************************************************************************/


import java.util.ArrayList;

/**
 *  The {@code BTree} class represents an ordered symbol table of generic
 *  key-value pairs.
 *  It supports the <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>size</em>, and <em>is-empty</em> methods.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a B-tree. It requires that
 *  the key type implements the {@code Comparable} interface and calls the
 *  {@code compareTo()} and method to compare two keys. It does not call either
 *  {@code equals()} or {@code hashCode()}.
 *  The <em>get</em>, <em>put</em>, and <em>contains</em> operations
 *  each make log<sub><em>m</em></sub>(<em>n</em>) probes in the worst case,
 *  where <em>n</em> is the number of key-value pairs
 *  and <em>m</em> is the branching factor.
 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes constant time.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/62btree">Section 6.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */


/**
 * Basic BTree structure was taken from Sedgewick, a method or two was taken from Professor Diament.
 * Everything else was my personal edit.
 * @param <Key>
 * @param <Value>
 */

public class BTree <Key extends Comparable<Key>, Value>  {
    // max children per B-tree node = M-1
    // (must be even and greater than 2)
    private static final int M = 4;

    private Node root;       // root of the B-tree
    private int height;      // height of the B-tree
    private int n;           // number of key-value pairs in the B-tree
    private boolean flag;

    private String indexName;  //Until underscore, that is the column we are working on
    private String bTreeDataType;
    private static final String sentinal = "*"; // lowest value through out. Helps for structure
    private ArrayList<Row> finalAL= new ArrayList<Row>(); // whenever we are using a where clasue that needs to append array lists


    // helper B-tree node data type
    private static final class Node {
        private int m;                             // number of children
        private Entry[] children = new Entry[M];   // the array of children

        // create a node with k children
        private Node(int k) {
            m = k; // K needs to be less than M
        }

    }

    // internal nodes: only use key and next
    // external nodes: only use key and value
    private static class Entry  { // only comparing entries! might need to override the compare to method
        private String key;
        private ArrayList<Row> val;
        private Node next;     // helper field to iterate over array entries

        public Entry(String key, ArrayList<Row> val, Node next) {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }


    }

    /**
     * Creates a btree and puts the sentinal in with a value of null
     */

    public BTree() {
        root = new Node(0);
        put(sentinal, null);

    }




    /**
     * Returns true if this symbol table is empty.
     * @return {@code true} if this symbol table is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * sets index name
     * @param indexName
     */

    public void setIndexName(String indexName){
        this.indexName= indexName;
    }


    /**
     * increases key value pair
     */
    public void increaseKVP(){
        n++;
    }

    /**
     * gets index name
     * @return
     */
    public String getIndexName(){
        return indexName;
    }

    /**
     * sets tree data Type
     * @param dataType
     */
    public void setbTreeDataType(String dataType){

        this.bTreeDataType=dataType;
    }

    public void setFlag(boolean bool){
        flag = bool;
    }

    public boolean getFlag(){
        return flag;
    }

    /**
     * Returns the root node
     * @return
     */

    public Node getRoot(){
        return root;
    }

    /**
     * Returns the height of this B-tree (for debugging).
     *
     * @return the height of this B-tree
     */

    public int getHeight(){
        return height;
    }


    /**
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */

    /**
     * this method returns an array list of rows thats associated with the key we passed in
     * @param key
     * @return
     */
    public ArrayList<Row> get(String key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        removingContentsFromAL();
        return search(root, key, height);
    }

    /**
     * this method continues from get and searches the tree for the key we are looking for
     * @param node
     * @param key
     * @param ht
     * @return
     */

    private ArrayList<Row> search(Node node, String key, int ht) {
        Entry[] children = node.children; //CHILDREN IS THE ENTRIES
        if (ht == 0) {
            for (int j = 0; j < node.m; j++) {
                if (eq(key, children[j].key)) {
                    return  children[j].val;
                }
            }
        }

        // internal node
        else {
            for (int j = 0; j < node.m; j++) {
                if (j+1 == node.m || less(key, children[j+1].key)) //if the next one is the maximum number of kids OR Its in between where I am now and the next one
                    return search(children[j].next, key, ht-1); //recursive function but one less down
            }
        }
        return null;
    }

    /**
     * comparison method so we can properly get the key we want
     * @param k1
     * @param k2
     * @return
     */
    private boolean less(String k1, String k2) {

        if (k1.equals(BTree.sentinal)) { // when key is being put, it checks other keys to see if they're greater than or eqal to. So this line is saying youre greater than me because im sentinal
            return true;
        } else if (k2.equals(BTree.sentinal)) {
            return false;
        } else if (k2.equalsIgnoreCase("null")) {
            return false;
        } else if (k1.equalsIgnoreCase("null") && k2.equalsIgnoreCase("null")) {
            return false;
        } else if (k1.equalsIgnoreCase("null")) {
            return true;
        } else if(bTreeDataType.equalsIgnoreCase("INT") || bTreeDataType.equalsIgnoreCase("DECIMAL")){
            return lessForNumbers(k1, k2);
        } else if (k1.compareToIgnoreCase(k2) < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * since we are dealing with strings, this comparison parses numbers whenever the key is a number
     *
     * @param k1
     * @param k2
     * @return
     */
    private boolean lessForNumbers(String k1, String k2){
        if(bTreeDataType.equalsIgnoreCase("INT")){
            if (Integer.parseInt(k1) < Integer.parseInt(k2)){
                return true;
            }
            else{
                return false;
            }
        }
        else {
            if (Double.parseDouble(k1) < Double.parseDouble(k2)){
                return true;
            }
            else{
                return false;
            }
        }
    }




    /**
     * This method returns an arrayList of all the values that are greater than the key we passed in
     *
     * @param key
     * @return
     */
    public ArrayList<Row> getGreaterThan(String key){
        if (key == null){
            throw new IllegalArgumentException("Argument to getGreaterThan() is null");
        }
        removingContentsFromAL();
        searchGreaterThan(root, key, height);
        return finalAL;

    }

    /**
     * This method traverses through the tree finding the keys that are greater than the key
     *
     * @param node
     * @param key
     * @param ht
     */
    private void searchGreaterThan(Node node, String key, int ht) {
        Entry[] children = node.children; //CHILDREN IS THE ENTRIES

        // external node
        if (ht == 0) {
            for (int j = 0; j < node.m; j++) {
                if (greaterThan(key, children[j].key)) {
                    addEachValueToAnotherAL(children[j].val, finalAL);
                }
            }
        }

        // internal node
        else {
            for (int j = 0; j < node.m; j++) {
                if (j+1 == node.m || greaterThan(key, children[j+1].key)){//if the next one is the maximum number of kids OR Its in between where I am now and the next one
                    searchGreaterThan(children[j].next, key, ht-1);
                }
            }
        }
    }

    /**
     * comparison method for greater than
     *
     * @param k1
     * @param k2
     * @return
     */
    private boolean greaterThan(String k1, String k2){
        if (k1.equals(BTree.sentinal)){ // when key is being put, it checks other keys to see if they're greater than or eqal to. So this line is saying youre greater than me because im sentinal
            return true;
        }
        else if(k2.equals(BTree.sentinal)){
            return false;
        }
        else if(k2.equalsIgnoreCase("null")){
            return false;
        }
        else if(k1.equalsIgnoreCase("null") && k2.equalsIgnoreCase("null")){
            return false;
        }
        else if(k1.equalsIgnoreCase("null")){
            return true;
        }
        else if(bTreeDataType.equalsIgnoreCase("INT") || bTreeDataType.equalsIgnoreCase("DECIMAL")){
           return greaterThanForNumbers(k1, k2);
        }

        else if(k1.compareToIgnoreCase(k2) <0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * comparison for numbers when dealing with greater than
     *
     * @param k1
     * @param k2
     * @return
     */

    private boolean greaterThanForNumbers(String k1, String k2){
        if(bTreeDataType.equalsIgnoreCase("INT")){
            if (Integer.parseInt(k1) < Integer.parseInt(k2)){
                return true;
            }
            else{
                return false;
            }
        }
        else {
            if (Double.parseDouble(k1) < Double.parseDouble(k2)){
                return true;
            }
            else{
                return false;
            }
        }
    }






    /**
     * This method returns an array list of all the rows which greater than or equals to the key
     *
     * @param key
     * @return
     */

    public ArrayList<Row> getGreaterThanOrEqualsTo(String key){
        if (key == null){
            throw new IllegalArgumentException("Argument to getGreaterThan() is null");
        }
        removingContentsFromAL();
        searchGreaterThanOrEquals(root, key, height);
        return finalAL;
    }

    /**
     * This method traverses through the tree finding all the keys that are greater than or equals
     * to the key
     *
     *
     * @param node
     * @param key
     * @param ht
     */

    private void searchGreaterThanOrEquals(Node node, String key, int ht){
        Entry[] children = node.children; //CHILDREN IS THE ENTRIES

        // external node
        if (ht == 0) {
            for (int j = 0; j < node.m; j++) {
                if (greaterThanOrEqualsTo(key, children[j].key)) {
                    addEachValueToAnotherAL(children[j].val, finalAL);
                }
            }
        }

        // internal node
        else {
            for (int j = 0; j < node.m; j++) {
                if (j+1 == node.m || greaterThanOrEqualsTo(key, children[j+1].key)){//if the next one is the maximum number of kids OR Its in between where I am now and the next one
                    searchGreaterThanOrEquals(children[j].next, key, ht-1);
                }
            }
        }
    }

    /**
     * greater than or equals comparison
     *
     * @param k1
     * @param k2
     * @return
     */

    private boolean greaterThanOrEqualsTo(String k1, String k2){
        if (k1.equals(BTree.sentinal)){ // when key is being put, it checks other keys to see if they're greater than or eqal to. So this line is saying youre greater than me because im sentinal
            return true;
        }
        else if(k2.equals(BTree.sentinal) && k1.equalsIgnoreCase(BTree.sentinal)){
            return true;
        }
        else if(k2.equals(BTree.sentinal)){
            return false;
        }
        else if(k2.equalsIgnoreCase("null") && k1.equalsIgnoreCase("null")){
            return true;
        }
        else if(k2.equalsIgnoreCase("null")){
            return false;
        }
        else if(k1.equalsIgnoreCase("null")){
            return true;
        }
        else if (bTreeDataType.equalsIgnoreCase("INT") | bTreeDataType.equalsIgnoreCase("DECIMAL")){
            return greaterThanOrEqualsForNumbers(k1, k2);
        }
        else if(k1.compareToIgnoreCase(k2) <0 || k1.compareToIgnoreCase(k2) == 0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * greater than or equals comparison with numbers
     *
     * @param k1
     * @param k2
     * @return
     */

    private boolean greaterThanOrEqualsForNumbers(String k1, String k2){
        if(bTreeDataType.equalsIgnoreCase("INT")){
            if (Integer.parseInt(k1) < Integer.parseInt(k2) || Integer.parseInt(k1) == Integer.parseInt(k2)){
                return true;
            }
            else{
                return false;
            }
        }
        else {
            if (Double.parseDouble(k1) < Double.parseDouble(k2) || Double.parseDouble(k1) == Double.parseDouble(k2)){
                return true;
            }
            else{
                return false;
            }
        }

    }




    /**
     * this method returns all the rows that have a key less than what we pass through
     *
     * @param key
     * @return
     */
    public ArrayList<Row> getLessThan(String key){
        if (key == null){
            throw new IllegalArgumentException("Argument to getGreaterThan() is null");
        }

        removingContentsFromAL(); // i have a field variable named FinalAL and it stores all the rows that match
        // im removing the contents before i add new rows from a new query now.

        searchLessThan(root, key, height);
        return finalAL;
    }

    /**
     * this method traverses through the tree hitting all the key values that are less than the key we passed
     *
     * @param node
     * @param key
     * @param ht
     */

    public void searchLessThan(Node node, String key, int ht){

        Entry[] children = node.children; //CHILDREN IS THE ENTRIES

        // external node
        if (ht == 0) {
            for (int j = 0; j < node.m; j++) {
                if (children[j].key.equalsIgnoreCase("*")){
                    continue;
                }
                else if (lessThan(key, children[j].key)) {
                    addEachValueToAnotherAL(children[j].val, finalAL);
                }
            }
        }

        // internal node
        else {
            for (int j = 0; j < node.m; j++) {
                if (j+1 == node.m || lessThan(key, children[j].key) || eq(key, children[j+1].key)) //if the next one is the maximum number of kids OR Its in between where I am now and the next one
                    searchLessThan(children[j].next, key, ht-1); //recursive function but one less down
            }
        }
    }

    /**
     * comparison method for less than
     * @param k1
     * @param k2
     * @return
     */
    private boolean lessThan(String k1, String k2){
        if (k1.equals(BTree.sentinal)){ // when key is being put, it checks other keys to see if they're greater than or eqal to. So this line is saying youre greater than me because im sentinal
            return false;
        }
        else if(k2.equals(BTree.sentinal)){
            return true;
        }
        else if (k1.equalsIgnoreCase("null")){
            return false;
        }
        else if(k2.equalsIgnoreCase("null")){
            return true;
        }
        else if(k1.equalsIgnoreCase("null") && k2.equalsIgnoreCase("null")){
            return false;
        }
        else if(k1.equalsIgnoreCase("null")){
            return false;
        }
        else if (bTreeDataType.equalsIgnoreCase("INT") | bTreeDataType.equalsIgnoreCase("DECIMAL")) {
            return lessThanForNumbers(k1, k2);
        }
        else if (k1.compareToIgnoreCase(k2) > 0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * comparison method for less than for numbers
     * @param k1
     * @param k2
     * @return
     */

    private boolean lessThanForNumbers(String k1, String k2){
        if(bTreeDataType.equalsIgnoreCase("INT")){
            if (Integer.parseInt(k1) > Integer.parseInt(k2)){
                return true;
            }
            else{
                return false;
            }
        }
        else {
            if (Double.parseDouble(k1) > Double.parseDouble(k2)){
                return true;
            }
            else{
                return false;
            }
        }
    }


    /**
     * returns an Array list of all the rows from the key that are less than or equals to the key we
     * are searching for
     * @param key
     * @return
     */

    public ArrayList<Row> getLessThanOrEqualsTo(String key){
        if (key == null){
            throw new IllegalArgumentException("Argument to getGreaterThan() is null");
        }

        removingContentsFromAL();
        searchLessThanOrEquals(root, key, height);
        return finalAL;
    }

    /**
     * this method traerses through the tree, hitting all the keys that are less than or equals to the
     * key we pass in
     * @param node
     * @param key
     * @param ht
     */

    private void searchLessThanOrEquals(Node node, String key, int ht){

        Entry[] children = node.children; //CHILDREN IS THE ENTRIES
        // external node
        if (ht == 0) {
            for (int j = 0; j < node.m; j++) {
                if (children[j].key.equalsIgnoreCase("*")){
                    continue;
                }
                else if (lessThanOrEquals(key, children[j].key)) {
                    addEachValueToAnotherAL(children[j].val, finalAL);
                }
            }
        }

        // internal node
        else {
            for (int j = 0; j < node.m; j++) {
                if (j+1 == node.m || lessThanOrEquals(key, children[j].key) || eq(key, children[j+1].key)) //if the next one is the maximum number of kids OR Its in between where I am now and the next one
                    searchLessThanOrEquals(children[j].next, key, ht-1); //recursive function but one less down
            }
        }

    }

    /**
     * Comparison method
     * @param k1
     * @param k2
     * @return
     */

    private boolean lessThanOrEquals(String k1, String k2){
        if (k1.equals(BTree.sentinal)){
            return false;
        }
        else if(k2.equals(BTree.sentinal)){
            return true;
        }
        else if (k1.equalsIgnoreCase("null") && k2.equalsIgnoreCase("null")){
            return true;
        }
        else if(k1.equalsIgnoreCase("null")){
            return false;
        }
        else if(k2.equalsIgnoreCase("null")){
            return true;
        }
        else if (bTreeDataType.equalsIgnoreCase("INT") || bTreeDataType.equalsIgnoreCase("DECIMAL")){
            return lessThanOrEqualsForNumbers(k1, k2);
        }
        else if (k1.compareToIgnoreCase(k2) > 0 || k1.compareToIgnoreCase(k2) == 0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * comparison method for numbers
     * @param k1
     * @param k2
     * @return
     */
    private boolean lessThanOrEqualsForNumbers(String k1, String k2){
        if(bTreeDataType.equalsIgnoreCase("INT")){
            if (Integer.parseInt(k1) > Integer.parseInt(k2) || Integer.parseInt(k1) == Integer.parseInt(k2)){
                return true;
            }
            else{
                return false;
            }
        }
        else {
            if (Double.parseDouble(k1) > Double.parseDouble(k2) || Double.parseDouble(k1) == Double.parseDouble(k2)){
                return true;
            }
            else{
                return false;
            }
        }

    }


    /**
     * this method returns an AL of rows that are NOT in the key we pass in
     *
     * @param key
     * @return
     */

    public ArrayList<Row> getNotEquals(String key){

        if (key == null){
            throw new IllegalArgumentException("Argument to getGreaterThan() is null");
        }

        removingContentsFromAL();
        searchNotEquals(root, key, height);
        return finalAL;
    }

    /**
     * this method traverses through the tree hitting every key thats NOT this key
     *
     * @param node
     * @param key
     * @param ht
     */

    private void searchNotEquals(Node node, String key, int ht){

        Entry[] children = node.children; //CHILDREN IS THE ENTRIES
        // external node
        if (ht == 0) {
            for (int j = 0; j < node.m; j++) {
                if (children[j].key.equalsIgnoreCase("*")){
                    continue;
                }
                else if (notEquals(key, children[j].key)) {
                    addEachValueToAnotherAL(children[j].val, finalAL);
                }
            }
        }

        // internal node
        else {
            for (int j = 0; j < node.m; j++) {
                     searchNotEquals(children[j].next, key, ht-1); //recursive function but one less down
            }
        }
    }

    /**
     * comparison method
     * @param k1
     * @param k2
     * @return
     */

    private boolean notEquals(String k1, String k2){
        if (k1.equalsIgnoreCase("null") && k2.equalsIgnoreCase("null")){
            return false;
        }
        else if(k1.equalsIgnoreCase("null")){
            return true;
        }
        else if (k2.equalsIgnoreCase("null")){
            return true;
        }
        if (bTreeDataType.equalsIgnoreCase("INT")){
            if (Integer.parseInt(k1) != Integer.parseInt(k2)){
                return true;
            }
            else{
                return false;
            }
        }
        else if (bTreeDataType.equalsIgnoreCase("DECIMAL")){
            if(Double.parseDouble(k1) != Double.parseDouble(k2)){
                return true;
            }
            else{
                return false;
            }
        }
        return k1.compareToIgnoreCase(k2) != 0;
    }


    /**
     * helper method to collect all the rows when traversing
     *
     * @param clauseValuesAL
     * @param finalAL
     */
    private void addEachValueToAnotherAL(ArrayList<Row> clauseValuesAL, ArrayList<Row> finalAL ){

        for (Row row: clauseValuesAL){
            finalAL.add(row);
        }
    }

    /**
     * method to reinitialize finalAL so no content was there from a previous query
     */

    private void removingContentsFromAL(){
        finalAL = new ArrayList<Row>();
    }



    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, ArrayList<Row> val) {  // key is the same as generics


        if (key == null) throw new IllegalArgumentException("argument key to put() is null");

        Entry alreadyThere = this.get(this.root, key, this.height);
        if(alreadyThere != null)
        {
            //Row row = val.get(0);
            alreadyThere.val.add(val.get(0));
            n++;
            return;
        }

        Node u = insert(root, key, val, height);
        n++;
        if (u == null){
            return;
        }

        // need to split root
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        height++;
    }


    /**
     * This method inserts a value into the proper node
     * @param h
     * @param key
     * @param val
     * @param ht
     * @return
     */

    private Node insert(Node h, String key, ArrayList<Row> val, int ht) {
        int j;
        Entry t = new Entry(key, val, null);
        // external node
        if (ht == 0) {
            for (j = 0; j < h.m; j++) {
                 if (less(key, h.children[j].key)){
                    break;
                }
            }
        }
        // internal node
        else {
            for (j = 0; j < h.m; j++) {
                if ((j+1 == h.m) || less(key, h.children[j+1].key)) {
                    Node u = insert(h.children[j++].next, key, val, ht-1);
                    if (u == null){
                        return null;
                    }
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }
        return postInsertion(t, h, j);
    }

    /**
     * after inserting we want to check if we need to call upon the split method or not
     * so our tree can be leveled correctly
     * @param t
     * @param h
     * @param j
     * @return
     */
    private Node postInsertion(Entry t, Node h, int j){
        for (int i = h.m; i > j; i--){

            h.children[i] = h.children[i-1];
        }
        h.children[j] = t;
        h.m++;
        if (h.m < M){
            return null;
        }
        else   {
            return split(h);
        }
    }

    /**
     * If an entry array gets full, we split the array and restructure the tree
     *
     * @param h
     * @return
     */
    private Node split(Node h) {
        Node t = new Node(M/2);
        h.m = M/2;
        for (int j = 0; j < M/2; j++){
            t.children[j] = h.children[M/2+j];
            h.children[M/2+j] = null;
        }

        return t;
    }


    /**
     *  this method is the over arching update method which is called in the table class
     *
     *
     * @param index
     * @param updatedKey
     * @param r
     * @param columnName
     */

    public void updateTree1(int index, String updatedKey, Row r, String columnName){
        updateTree2(index,updatedKey, r, this.root, height, columnName );
    }

    /**
     * this update method updates the tree when the column in the where clause is the same as the tree name
     * @param index
     * @param updatedKey
     * @param r
     * @param currentNode
     * @param height
     * @param columnName
     */

    //updating when the column in where is the same name as the tree
    public void updateTree2(int index, String updatedKey, Row r, Node currentNode, int height, String columnName){
        Entry[] entries = currentNode.children;
        //current node is external (i.e. height == 0)
        if (height == 0) {
            for (int j = 0; j < currentNode.m; j++) {
                if (entries[j].key.equalsIgnoreCase("*")){
                    continue;
                }
                for(Row row: entries[j].val){
                    if (row.equals(r)){
                        row.getRow().set(index, updatedKey);
                        if(columnName.equalsIgnoreCase(indexName)){
                            rearrangeValues(r, index, entries[j].key, entries[j].val);
                        }
                        break;
                    }
                }
            }
        }

        //current node is internal (height > 0)
        else {
            for (int j = 0; j < currentNode.m; j++)
            {
                updateTree2(index, updatedKey, r, entries[j].next, height -1, columnName);
            }
        }
    }

    /**
     * this is a helper method for updating the Key values in the btree
     * @param row
     * @param index
     * @param key
     * @param val
     */

    private void rearrangeValues(Row row, int index, String key , ArrayList<Row> val){
        String getKey = row.getRow().get(index);
        ArrayList<Row> rowsToAdd = new ArrayList<Row>(); // for the purpose of passing in an array list
        rowsToAdd.add(row);
        if (!getKey.equalsIgnoreCase(key)){
            put(getKey, rowsToAdd);
            val.remove(row);
        }
    }

    /**
     * this methos is the over arching delete method that is called in the table class
     * @param validRow
     */

    public void deleteTree1(Row validRow){
        deleteTree2(validRow, this.root, height);
    }


    /**
     * this method deletes the row that matches the row we want to delete which was given to us
     * from the WHERE subtable
     * @param validRow
     * @param currentNode
     * @param height
     */
    private Row deleteTree2(Row validRow, Node currentNode, int height){
        Entry[] entries = currentNode.children;
        //current node is external (i.e. height == 0)
        if (height == 0) {
            for (int j = 0; j < currentNode.m; j++) {
                if (entries[j].key.equalsIgnoreCase("*")){
                    continue;
                }
                if (entries[j].val.size() ==0){
                    continue;
                }
                for(Row row: entries[j].val){
                    if (row.getRow().equals(validRow.getRow())) {
                        entries[j].val.remove(validRow);
                        return null;
                        }
                     if (entries[j].val.size() ==0){
                         break;
                     }
                }
            }
        }
        //current node is internal (height > 0)
        else {
            for (int j = 0; j < currentNode.m; j++) {
                deleteTree2(validRow, entries[j].next,  height -1);
            }
        }
        return null;
    }

    /**
     * This method deletes ALL the proper rows leaving the keys with empty values
     */

    public void deleteAll(){
        deleteAll2(root, height);
    }


    /**
     * /**
     * This method deletes ALL the proper rows leaving the keys with empty values
     *
     * @param currentNode
     * @param height
     */

    private void deleteAll2( Node currentNode, int height){
        Entry[] entries = currentNode.children;
        //current node is external (i.e. height == 0)
        if (height == 0) {
            for (int j = 0; j < currentNode.m; j++) {
                if (entries[j].key.equalsIgnoreCase("*")){
                    continue;
                }
                if (entries[j].val.size() ==0){
                    continue;
                }
                int size = entries[j].val.size();
                for (int i = 0; i <  size; i++) {
                    entries[j].val.remove(0);
                }
                if (entries[j].val.size() ==0){
                    continue;
                }
            }
        }
        //current node is internal (height > 0)
        else {
            for (int j = 0; j < currentNode.m; j++) {
                deleteAll2(entries[j].next,  height -1);
            }
        }
    }


    /**
     * this method returns an ENTRY. Code was taken from Professor Diament's Link
     * @param currentNode
     * @param key
     * @param height
     * @return
     */
    private Entry get(Node currentNode, String key, int height) {
        Entry[] entries = currentNode.children;
        //current node is external (i.e. height == 0)
        if (height == 0) {
            for (int j = 0; j < currentNode.m; j++) {
                if(eq(key, entries[j].key)) {
                    //found desired key. Return its value
                    return entries[j];
                }
            }
            //didn't find the key
            return null;
        }
        //current node is internal (height > 0)
        else {
            for (int j = 0; j < currentNode.m; j++) {
                if (j + 1 == currentNode.m || less(key, entries[j + 1].key)) {
                    return this.get(entries[j].next, key, height - 1);
                }
            }
            return null;
        }
    }


    /**
     * Returns a string representation of this B-tree (for debugging).
     *
     * @return a string representation of this B-tree.
     */
    public String toString() {
        return toString(root, height, "") + "\n";
    }

    private String toString(Node h, int ht, String indent) {
        StringBuilder s = new StringBuilder();
        Entry[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
                s.append(indent + children[j].key + " " + children[j].val + "\n");
            }
        }
        else {
            for (int j = 0; j < h.m; j++) {
                if (j > 0) s.append(indent + "(" + children[j].key + ")\n");
                s.append(toString(children[j].next, ht-1, indent + "     "));
            }
        }
        return s.toString();
    }


    /**
     * comparison method which is used when dealing with "=" operator
     * @param k1
     * @param k2
     * @return
     */
    private boolean eq(String k1, String k2) {

        return k1.compareToIgnoreCase(k2) == 0;
    }

}



/******************************************************************************
 * Some of the methods are taken from Professor Diament's Code
 ******************************************************************************/


/******************************************************************************
 *  Copyright 2002-2018, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/


