package org.pcp.tournament.service.matchreferences;

public class PathBuilder {
    StringBuilder builder;

    public PathBuilder() {
        builder = new StringBuilder();
    }

    public PathBuilder append(String pathElement) {
        if (builder.length()> 0) {
        builder.append("/");
        }
        builder.append(pathElement);
        return this;
    }

    public PathBuilder append(int pathElement) {
        return append(Integer.toString(pathElement));
    }

    public String toString() {
        return builder.toString();
    }
}
