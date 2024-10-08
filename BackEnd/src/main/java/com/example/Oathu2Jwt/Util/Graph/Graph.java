package com.example.Oathu2Jwt.Util.Graph;

import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.Objects;

@Data
public class Graph {
    private ArrayList<Vertex> vertices;
    private boolean isWeighted;
    private boolean isDirected;

    public Graph(boolean inputIsWeighted, boolean inputIsDirected) {
        this.vertices = new ArrayList<Vertex>();
        this.isWeighted = inputIsWeighted;
        this.isDirected = inputIsDirected;
    }

    public Vertex addVertex(UserInfoEntity data) {
        Vertex newVertex = new Vertex(data);
        this.vertices.add(newVertex);
        return newVertex;
    }

    public void addEdge(Vertex vertex1, Vertex vertex2, Integer weight) {
        if (!this.isWeighted) {
            weight = null;
        }
        vertex1.addEdge(vertex2, weight);
        if (!this.isDirected) {
            vertex2.addEdge(vertex1, weight);
        }
    }

    public void removeEdge(Vertex vertex1, Vertex vertex2) {
        vertex1.removeEdge(vertex2);
        if (!this.isDirected) {
            vertex2.removeEdge(vertex1);
        }
    }

    public void removeVertex(Vertex vertex) {
        this.vertices.remove(vertex);
    }

    public Vertex getVertexByValue(String value) {
        for(Vertex v: this.vertices) {
            if (Objects.equals(v.getData().getAccName(), value)) {
                return v;
            }
        }

        return null;
    }
    public void print() {
        for(Vertex v: this.vertices) {
            v.print(isWeighted);
        }
    }

}
