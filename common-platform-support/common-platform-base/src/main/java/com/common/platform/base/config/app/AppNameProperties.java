package com.common.platform.base.config.app;

import lombok.Data;

@Data
public class AppNameProperties {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
