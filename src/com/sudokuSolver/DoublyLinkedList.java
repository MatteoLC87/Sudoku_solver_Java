package com.sudokuSolver;

class DoublyLinkedList {
    private Node head;
    private Node tail;
    private int length;

    public Node getHeadNode() {
        return head;
    }

    public void setHeadNode(Node newHeadNode) {
        head = newHeadNode;
    }

    public Node getTailNode() {
        return tail;
    }

    public void setTailNode(Node newTailNode) {
        tail = newTailNode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int newLength) {
        length = newLength;
    }
    public void append(int value, int[] position_9x9_ij) {
        Node newNode = new Node(value, position_9x9_ij);
        if (length == 0) {
            setHeadNode(newNode);
            setTailNode(newNode);
        } else {
            getTailNode().setNextNode(newNode);
            newNode.setPreviousNode(getTailNode());
            setTailNode(newNode);
        }
        setLength(getLength() + 1);
    }
}