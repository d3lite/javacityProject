/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cityproject;

/**
 *
 * @author David
 */
public class LinkedList {

    public LinkedList.Node head = null; //head of the linked list
    public LinkedList.Node first = null; //first node of the linked list

    //null constructor
    public LinkedList() {
    }//end constrcut

    /**
     * ***************************************
     */
    public LinkedList(City c) {
        first = new LinkedList.Node(c, null);
        head = first;
    }//end LinkedL

    /**
     * ***************************************
     */
    public void addFirst(City c) {
        LinkedList.Node newFirst = new LinkedList.Node(c, head);
        head = newFirst;
    }//end AddFirst

    /**
     * ***************************************
     */
    public LinkedList.Node getHead() {
        return this.head;
    }//end getHead        

    /**
     * ***************************************
     */
    public String toString() {
        String info = ""; //set string to null
        StringBuffer s = new StringBuffer(info); //create stringbuffer 
        LinkedList.Node pointer = this.getHead(); //set pointer node to be the list's head            

        while (pointer != null) //append each city's name to the string until there aren't any more cities
        {
            info = pointer.data.getName();
            if (pointer.pointer == null) {
                s.append(info + ".");
            }//end if                
            else {
                s.append(info + ", ");
            }//end else
            pointer = pointer.pointer;
        }//end while                     
        return s.toString();
    }//end toString

    class Node
        {
            public City data; //holds City data            
            public LinkedList.Node pointer; //holds node data
            
            public Node()
            {
              this.data = null;
              this.pointer = null;
            }//end Node
            /******************************************/
            
            public Node(City d, LinkedList.Node n) 
            {
              this.data = d;
              this.pointer = n;
            }//end Node
            /******************************************/            
            public String toString() 
            {
              String info = data + ", {" + pointer + "}";
              return info;
            }//end toString
            /******************************************/
        } //End Node  
}
