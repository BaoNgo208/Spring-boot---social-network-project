package com.example.Oathu2Jwt.Util.Graph;

import com.example.Oathu2Jwt.Util.Graph.Vertex;

public class Node {

    public Vertex data;
    private Node next;

    public Node(Vertex data) {
        this.data = data;
        this.next = null;
    }

    public void setNextNode(Node node) {
        this.next = node;
    }

    public Node getNextNode() {
        return this.next;
    }

}