package org.pcp.tournament.model.dto;

import org.pcp.tournament.model.Options;

public class NameAndOptions {

    private String name;

    private Options options;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the options
     */
    public Options getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(Options options) {
        this.options = options;
    }

}

