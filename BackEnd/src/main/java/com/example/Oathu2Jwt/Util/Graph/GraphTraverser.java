package com.example.Oathu2Jwt.Util.Graph;

import com.example.Oathu2Jwt.Util.Graph.Edge;
import com.example.Oathu2Jwt.Util.Graph.Vertex;

import java.util.ArrayList;
import com.example.Oathu2Jwt.Util.Graph.Queue;

public class GraphTraverser {
    public static void depthFirstTraversal(Vertex start, ArrayList<Vertex> visistedVertices){
        System.out.println(start.getData().getAccName());
        for(Edge e : start.getEdges()) {
              Vertex neighbor = e.getEnd();
              if(!visistedVertices.contains(neighbor)) {
                  visistedVertices.add(neighbor);
                  GraphTraverser.depthFirstTraversal(neighbor,visistedVertices);
              }
        }
    }


    public static void breadthFirstSearch(Vertex start, ArrayList<Vertex> visitedVertices) {
        Queue visitQueue = new Queue();
        visitQueue.enqueue(start);
        while (!visitQueue.isEmpty()) {
            Vertex current = visitQueue.dequeue();
            System.out.println(current.getData());

            for (Edge e: current.getEdges()) {
                Vertex neighbor = e.getEnd();
                if (!visitedVertices.contains(neighbor)) {
                    visitedVertices.add(neighbor);
                    visitQueue.enqueue(neighbor);
                }
            }
        }
    }
}
