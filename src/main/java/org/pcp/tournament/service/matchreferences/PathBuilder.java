package org.pcp.tournament.service.matchreferences;

public class PathBuilder {
    StringBuilder builder;

    public PathBuilder() {
        builder = new StringBuilder();        
    } 

    public PathBuilder append(String pathElement) {
        builder.append("/")
        .append(pathElement);
        return this;
    }

    public PathBuilder append(int pathElement) {
        return append(Integer.toString(pathElement));
    }
}
