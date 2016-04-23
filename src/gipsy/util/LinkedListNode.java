package gipsy.util;

public class LinkedListNode {

  public LinkedListNode previous;
  public LinkedListNode next;
  public Object object;

  /**
   * This class maintains a timestamp when an object was first added to
   * cache, which records the caculation time of the object.
   */
  public long timestamp = 0;

  /**
   * Constructs a new linked list node.
   *
   * @param object Object - the object that the node represents.
   * @param next LinkedListNode - a reference to the next LinkedListNode in the list.
   * @param previous LinkedListNode - a reference to the previous LinkedListNode in the list.
   */
  public LinkedListNode(Object object, LinkedListNode next,
                        LinkedListNode previous) {
    this.object = object;
    this.next = next;
    this.previous = previous;
  }

  /**
   * Removes this node from the linked list that it is a part of.
   */
  public void remove() {
    previous.next = next;
    next.previous = previous;
  }

  /**
   * Returns a String representation of the linked list node by calling the
   * toString method of the node's object.
   *
   * @return a String representation of the LinkedListNode.
   */
  public String toString() {
    return object.toString();
  }
}
