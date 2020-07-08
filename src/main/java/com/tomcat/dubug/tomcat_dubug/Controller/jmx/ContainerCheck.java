package com.tomcat.dubug.tomcat_dubug.Controller.jmx;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author linsong.chen
 * @date 2020-07-07 19:35
 */
public class ContainerCheck {

    @NotNull
    private List<Container> checkContainerList;

    public List<Container> getCheckContainerList() {
        return checkContainerList;
    }

    public void setCheckContainerList(List<Container> checkContainerList) {
        this.checkContainerList = checkContainerList;
    }
}
