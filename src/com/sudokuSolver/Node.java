package com.sudokuSolver;

class Node {
    private int value; //make it final? replace the node every time the value is updated? probably not efficient
    private int[] position_9x9_ij;
    private Node next;
    private Node previous;

    public Node(int value, int[] position_9x9_ij) {
        this.value = value;
        this.position_9x9_ij = position_9x9_ij;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int newValue) {
        value = newValue;
    }

    public void incrementValue() {
        value++;
    }

    public int[] getPosition_9x9_ij() {
        return position_9x9_ij;
    }

    public void setPosition_9x9_ij(int[] newPosition_9x9_ij) {
        position_9x9_ij = newPosition_9x9_ij;
    }

    public Node getNextNode() {
        return next;
    }

    public void setNextNode(Node newNextNode) {
        next = newNextNode;
    }

    public Node getPreviousNode() {
        return previous;
    }

    public void setPreviousNode(Node newPreviousNode) {
        previous = newPreviousNode;
    }
}