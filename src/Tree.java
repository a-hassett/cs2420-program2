// ******************ERRORS********************************
// Throws UnderflowException as appropriate

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

class UnderflowException extends RuntimeException {
    /**
     * Construct this exception object.
     *
     * @param message the error message.
     */
    public UnderflowException(String message) {
        super(message);
    }
}

public class Tree<E extends Comparable<? super E>> {
    public BinaryNode<E> root;  // Root of tree
    private String treeName;     // Name of tree

    /**
     * Create an empty tree
     * @param label Name of tree
     */
    public Tree(String label) {
        treeName = label;
        root = null;
    }

    /**
     * Create non ordered tree from list in preorder
     * @param arr   List of elements
     * @param label Name of tree
     */
    public Tree(E[] arr, String label, boolean ordered) {
        treeName = label;
        if (ordered) {
            root = null;
            for (int i = 0; i < arr.length; i++) {
                bstInsert(arr[i]);
            }
        } else root = buildUnordered(arr, 0, arr.length - 1);
    }

    /**
     * Build a NON BST tree by preorder
     * @param arr nodes to be added
     * @return new tree
     */
    private BinaryNode<E> buildUnordered(E[] arr, int low, int high) {
        if (low > high) return null;
        int mid = (low + high) / 2;
        BinaryNode<E> curr = new BinaryNode<>(arr[mid], null, null);
        curr.left = buildUnordered(arr, low, mid - 1);
        curr.right = buildUnordered(arr, mid + 1, high);
        return curr;
    }
    /**
     * Create BST from Array
     * @param arr   List of elements to be added
     * @param label Name of  tree
     */
    public Tree(E[] arr, String label) {
        root = null;
        treeName = label;
        for (int i = 0; i < arr.length; i++) {
            bstInsert(arr[i]);
        }
    }

    /**
     * Change name of tree
     * @param name new name of tree
     */
    public void changeName(String name) {
        this.treeName = name;
    }

    /**
     * Task 1
     * Complexity is O(n) because we reach each of the n nodes 1 time
     * @return a string displaying the tree contents as a tree with one node per line
     */
    public String toString() {
        if (root == null)
            return (treeName + " Empty tree\n");
        else
            //return treeName + "\n" + toString2(root);
            return treeName+"\n" + toString(root,"");
    }

    /**
     * Internal recursive function that performs opposite of in-order traversal
     * (right, node, left)
     * Only appends to final string when working with current node
     * @param t current working node
     * @param indent How far out to put the node's values when we print them to represent depth
     * @return just the nodes printed with indents and in opposite order of in-order traversal
     */
    private String toString(BinaryNode<E> t, String indent) {
        StringBuilder sb = new StringBuilder();
        if(t.right != null){
            sb.append(toString(t.right, indent + "  "));
        }
        sb.append(indent + t.toString() + "\n");
        if (t.left != null){
            sb.append(toString(t.left, indent + "  "));
        }
        return sb.toString();
    }

    /**
     * @return string displaying the tree contents as a single line
     */
    public String toString2() {
        if (root == null)
            return treeName + " Empty tree";
        else
            return treeName + " " + toString2(root);
    }

    /**
     * Internal method to return a string of items in the tree in order
     * @param t Our current working node
     * @return The values found in the nodes lined up through the opposite of in-order traversal
     */
    private String toString2(BinaryNode<E> t) {
        if (t == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(toString2(t.left));
        sb.append(t.element.toString() + " ");
        sb.append(toString2(t.right));
        return sb.toString();
    }

    /**
     * Task 2
     * Complexity is O(n) because we run through each of the n nodes and flip them
     * Reverse left and right children recursively
     * Method with no parameters can be called by user to use on an entire tree
     */
    public void flip() {
        flip(root);
    }

    /**
     * Internal method that does all the flipping
     * Cannot be called by user
     * Uses three nodes to switch two of them (parent.left and parent.right)
     * @param parent the node we're currently working with
     */
    private void flip(BinaryNode<E> parent){
        if(parent == null){
            return;
        }
        BinaryNode<E> hold = parent.right;
        parent.right = parent.left;
        parent.left = hold;
        flip(parent.left);
        flip(parent.right);
    }

    /**
     * Task 3
     * Complexity is 2n because we go through the tree once to find the height
     * ...then again to find a corresponding node
     * Find the height of the tree
     * Find a node that is that many moves away from the root and return it
     */
    public E deepestNode() {
        return deepestNode(root, height());
    }

    /**
     * Go through the nodes and get the deepest one that is not null
     * @param parent the current working node
     * @param recurseCount how to keep track of what level of the tree I am in
     * @return the deepest level node
     */
    private E deepestNode(BinaryNode<E> parent, int recurseCount){
        if(parent == null){
            return null;
        } else if(recurseCount == 0) {
            return parent.element;
        } else if(deepestNode(parent.left, recurseCount - 1) == null){
            return deepestNode(parent.right, recurseCount - 1);
        } else{
            return deepestNode(parent.left, recurseCount - 1);
        }
    }

    /**
     * Get the height of the tree so we can find the deepest node
     * @return the integer of the height
     */
    public int height(){
        return height(root);
    }

    /**
     * Return the height of the tallest branch of the root
     * @param node current working node
     * @return the height of the tallest branch
     */
    public int height(BinaryNode<E> node){
        if(node == null){
            return -1;
        }
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    /**
     * Task 4
     * Complexity is O(n) because we run through each of the n nodes either 0 or 1 times, depending on the level
     * Counts number of nodes in specified level
     * @param level Level in tree, root is zero
     * @return count of number of nodes at specified level
     */
    public int nodesInLevel(int level) {
        return nodesInLevel(level, 0, root);
    }

    /**
     * Internal method adds up all the nodes at the level
     * @param level The level we need to get to so we can count the nodes there
     * @param recursion How many levels down we've gone by using recursion
     * @param node The current node we're working with
     * @return number of nodes at the desired level of the branches of the node
     */
    private int nodesInLevel(int level, int recursion, BinaryNode<E> node){
        if((level == recursion) && (node != null)){
            return 1;
        } else if(node == null){
            return 0;
        } else{
            return nodesInLevel(level, recursion + 1, node.left) + nodesInLevel(level, recursion + 1, node.right);
        }
    }

    /**
     * Task 5
     * Complexity is O(n) since we move through every path, but we never repeat since we're building off the last
     * Print all paths from root to leaves
     */
    public void printAllPaths() {
        printAllPaths(root, root.toString());
    }

    /**
     * Doesn't return anything; just prints the paths as leaf node is found
     * Moving left to right
     * @param parent Current working node
     * @param chain String listing the nodes we've picked up so far
     */
    private void printAllPaths(BinaryNode<E> parent, String chain){
        if((parent.left == null) && (parent.right == null)){
            System.out.println(chain);
            return;
        }
        if(parent.left != null){
            printAllPaths(parent.left, chain + " " + parent.left.toString());
        }
        if(parent.right != null){
            printAllPaths(parent.right, chain + " " + parent.right.toString());
        }
    }

    /**
     * Task 6
     * Complexity is O(n) because we check every node connected to a path
     * Remove all paths from tree that sum to less than given value
     * @param sum: minimum path sum allowed in final tree
     */
    public void pruneK(Integer sum) {
        if(!pruneK(sum, 0, root)){
            root = null;
        }
    }

    /**
     * Check if the paths have the "sum"
     * If not, delete the irrelevant nodes
     * @param sum: minimum path sum allowed in final tree
     * @param currentSum The sum we currently have as we add up the nodes in the current path
     * @param node Current working node
     * @return
     */
    private boolean pruneK(Integer sum, Integer currentSum, BinaryNode<E> node){
        if(node == null){
            return false;
        }

        currentSum += Integer.parseInt(node.toString());
        if((currentSum > sum) || (currentSum.equals(sum))){
            return true;
        } else if(pruneK(sum, currentSum, node.left) && pruneK(sum, currentSum, node.right)) {
            return true;
        } else if((!pruneK(sum, currentSum, node.left)) && (!pruneK(sum, currentSum, node.right))){
            postOrderDeletion(node);
            node = null;
            return false;
        } else{
            return true;
        }
    }

    /**
     * Does the post-order deletion of a binary search tree
     * Does not delete the root node, but we do that in public pruneK()
     * @param node
     */
    public void postOrderDeletion(BinaryNode<E> node){
        if(node != null){
            postOrderDeletion(node.left);
            node.left = null;
            postOrderDeletion(node.right);
            node.right = null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Task 7
     * Complexity is O(n) and possibly log n at best
     * Find the least common ancestor of two nodes
     * @param a first node
     * @param b second node
     * @return String representation of ancestor
     */
    public String lca(E a, E b) {
        if(a == b){
            return a.toString();
        }

        if((!contains(a)) || (!contains(b))){
            return "none";
        }

        String ancestor = lca(root, a, b);

        if (ancestor == null){
            return "none";
        } else{
            return ancestor;
        }
    }

    /**
     * Returns the string of either the left or right lca
     * Idk how to pick which one
     * @param t node
     * @param a lower bound
     * @param b upper bound
     * @return string of the lca node
     */
    private String lca(BinaryNode<E> t, E a, E b) {
        if (t == null){
            return null;
        }

        String right;
        String left;

        if(t.right != null){
            System.out.println(t.right);
            if(contains(a, t.right) && contains(b, t.right)){
                right = lca(t.right, a, b);
            } else{
                right = t.toString();
            }
        } else{
            right = t.toString();
        }
        if(t.left != null){
            System.out.println(t.left);
            if(contains(a, t.left) && contains(b, t.left)){
                left = lca(t.left, a, b);
            } else{
                left = t.toString();
            }
        } else{
            left = t.toString();
        }

        // if one node is the parent of the other, return the child
        if (left == null) {
            return right;
        } else{
            return left;
        }
    }

    /**
     * Determines if item is in tree
     * @param item the item to search for.
     * @return true if found.
     */
    public boolean contains(E item) {
        return contains(item, root);
    }

    /**
     * Internal method to find an item in a subtree.
     * This routine runs in O(log n) as there is only one recursive call that is executed and the work
     * associated with a single call is independent of the size of the tree: a=1, b=2, k=0
     *
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     * @return node containing the matched item.
     */
    private boolean contains(E x, BinaryNode<E> t) {
        if (t == null)
            return false;

        if(x == t.element){
            return true;
        } else if(contains(x, t.left) || contains(x, t.right)){
            return true;
        } else{
            return false;
        }
    }

    /**
     * Task 8
     * Complexity is O(n) because we go through every node a couple of times
     * Balance the tree
     * Collect all the nodes and make a new tree that is structured and balanced
     */
    public void balanceTree() {
        ArrayList<E> treeList = new ArrayList<>();
        balanceTree(root, treeList);
        Collections.sort(treeList);
        ArrayList<E> balancedTree = new ArrayList<>();
        balancedShuffle(treeList, balancedTree, 0, treeList.size());

        String[] balanceTree = new String[balancedTree.size()];
        for(int i = 0; i < balancedTree.size(); i++){
            balanceTree[i] = balancedTree.get(i).toString();
        }
        root = buildUnordered((E[]) balanceTree, 0, balancedTree.size() - 1);
    }

    /**
     * Recursively head through the whole tree and add all the nodes to an ArrayList
     * @param node current working node
     * @param treeList all the nodes in the tree
     */
    private void balanceTree(BinaryNode<E> node, ArrayList<E> treeList){
        if(node == null){
            return;
        } else{
            treeList.add(node.element);
            balanceTree(node.right, treeList);
            balanceTree(node.left, treeList);
        }
    }

    /**
     * Organize the items in the arraylist
     * @param list original list of nodes
     * @param balanced new organize list of nodes
     * @param low lower bound
     * @param high upper bound
     */
    private void balancedShuffle(ArrayList<E> list, ArrayList<E> balanced, int low, int high){
        if(low == high){
            return;
        }
        int mid = (low + high) / 2;
        balanced.add(list.get(mid));
        balancedShuffle(list, balanced, low, mid);
        balancedShuffle(list, balanced, mid + 1, high);
    }

    /**
     * Task 9
     * Complexity is O(n) because I run through every
     * In a BST, keep only nodes between range a and b
     * @param a lowest value
     * @param b highest value
     */
    public void keepRange(E a, E b) {
        if(root.element.compareTo(a) < 0){
            root.left = null;
            keepRange(a, b, root.right);
            root = root.right;
        } else if(root.element.compareTo(b) > 0){
            root.right = null;
            keepRange(a, b, root.left);
            root = root.left;
        } else{
            keepRange(a, b, root);
        }
    }

    /**
     * Gets rid of the left or right children of nodes that are out of range
     * @param a lower bound
     * @param b upper bound
     * @param node current working node
     */
    private void keepRange(E a, E b, BinaryNode<E> node){
        // if a.compareTo(b) returns neg, a < b
        // if a.compareTo(b) returns 0, a == b
        // if a.compareTo(b) returns pos, a > b
        if(node == null){
            return;
        }
        if(node.element.compareTo(a) < 0){
            node.left = null;
            keepRange(a, b, node.right);
            node = null;
        } else if(node.element.compareTo(b) > 0){
            node.right = null;
            keepRange(a, b, node.left);
            node = null;
        } else{
            keepRange(a, b, node.left);
            keepRange(a, b, node.right);
        }
    }

    /**
     * Task 10
     * Complexity is O(n^2) because we check whether each node has a tree for children
     * To check this, we traverse the whole tree again
     * Counts all non-null binary search trees embedded in tree
     * @return Count of embedded binary search trees
     */
    public Integer countBST() {
        if (root == null){
            return 0;
        }
        if(ifBST(root)){
            return countBST(root);
        }
        return countBST(root) - 1;
    }

    /**
     * If the node is null, there will be no attached tree
     * If the left and the right of the node are both BSTs, then it must be a BST. Proceed to traverse the tree
     * @param node current working node
     * @return total number of BSTs found
     */
    private Integer countBST(BinaryNode<E> node){
        if(node == null){
            return 0;
        }
        if((ifBST(node.left) && ifBST(node.right))){
            return 1 + countBST(node.left) + countBST(node.right);
        } else{
            return countBST(node.left) + countBST(node.right);
        }
    }

    /**
     * Returns whether a node's children create a binary search tree
     * Leaf nodes are BST's, and any other correctly ordered tree stemming from a node is a BST
     * @param node current working node
     * @return true or false depending on whether we find a tree or not
     */
    private boolean ifBST(BinaryNode<E> node){
        if(node == null){
            return true;
        }
        if((node.left == null) && (node.right == null)){
            return true;
        } else if((node.left != null) && (node.right != null)){
            if((!(node.left.element.compareTo(node.element) > 0)) && (!(node.right.element.compareTo(node.element) < 0))){
                return (ifBST(node.left) && ifBST(node.right));
            }
        } else if((node.left != null)){
            if((!(node.left.element.compareTo(node.element) > 0))){
                return ifBST(node.left);
            }
        } else{
            if((!(node.right.element.compareTo(node.element) < 0))){
                return ifBST(node.right);
            }
        }
        return false;
    }

    /**
     * Insert into a bst tree; duplicates are allowed
     * @param x the item to insert.
     */
    public void bstInsert(E x) {
        root = bstInsert(x, root);
    }

    /**
     * Internal method to insert into a subtree.
     * In tree is balanced, this routine runs in O(log n)
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<E> bstInsert(E x, BinaryNode<E> t) {
        if (t == null)
            return new BinaryNode<E>(x, null, null);
        int compareResult = x.compareTo(t.element);
        if (compareResult < 0) {
            t.left = bstInsert(x, t.left);
        } else {
            t.right = bstInsert(x, t.right);
        }
        return t;
    }

    /**
     * Task - Bonus
     * Build tree given inOrder and preOrder traversals.  Each value is unique
     * @param inOrder  List of tree nodes in inorder
     * @param preOrder List of tree nodes in preorder
     */
    public void buildTreeTraversals(E[] inOrder, E[] preOrder) {
        root = null;
    }

    // Basic node stored in unbalanced binary  trees
    private static class BinaryNode<E> {
        E element;            // The data in the node
        BinaryNode<E> left;   // Left child
        BinaryNode<E> right;  // Right child

        // Constructors
        BinaryNode(E theElement) {
            this(theElement, null, null);
        }

        BinaryNode(E theElement, BinaryNode<E> lt, BinaryNode<E> rt) {
            element = theElement;
            left = lt;
            right = rt;
        }

        // toString for BinaryNode
        public String toString() {
            StringBuilder sb = new StringBuilder();
            //sb.append("Node:");
            sb.append(element);
            return sb.toString();
        }

    }

}


