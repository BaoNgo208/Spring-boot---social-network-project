package com.example.Oathu2Jwt.Util.Graph;

import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Vertex {
    private UserInfoEntity data;
    private ArrayList<Edge> edges;
    public Vertex(UserInfoEntity inputData) {
        this.data = inputData;
        this.edges = new ArrayList<Edge>();
    }
    public void addEdge(Vertex endVertex,Integer weight) {
        this.edges.add(new Edge(this,endVertex,weight));
    }
    public void removeEdge(Vertex endVertex) {
        this.edges.removeIf(edge -> edge.getEnd().equals(endVertex));
    }
    public void print(boolean showWeight) {
        StringBuilder message = new StringBuilder();

        if (this.edges.size() == 0) {
            System.out.println(this.data.getId() + " -->");
            return;
        }

        for(int i = 0; i < this.edges.size(); i++) {

            if (i == 0) {
                message.append(this.edges.get(i).getStart().data.getId()).append(" -->  ");
            }

            message.append(this.edges.get(i).getEnd().data.getId());

            if (showWeight) {
                message.append(" (").append(this.edges.get(i).getWeight()).append(")");
            }

            if (i != this.edges.size() - 1) {
                message.append(", ");
            }
        }
        System.out.println(message);
    }

}
