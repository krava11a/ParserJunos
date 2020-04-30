package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Router {
//    private String id;
    private String nameRouter;
    private String typeRouter;
    private List<InterfaceDescription> interfaces;
    private String configFileName;

    public Router() {
    }

    public Router(String configFileName) throws IOException {
        this.configFileName = configFileName;
        this.nameRouter =  configFileName.substring(configFileName.indexOf("interfaces")+10).trim();
        this.interfaces = new ParserConfigJunos(configFileName).parse();
    }

    public Router(String nameRouter, String typeRouter, List<InterfaceDescription> interfaces) {
        this.nameRouter = nameRouter;
        this.typeRouter = typeRouter;
        this.interfaces = interfaces;
    }

    public String getNameRouter() {
        return nameRouter;
    }

    public void setNameRouter(String nameRouter) {
        this.nameRouter = nameRouter;
    }

    public String getTypeRouter() {
        return typeRouter;
    }

    public void setTypeRouter(String typeRouter) {
        this.typeRouter = typeRouter;
    }

    public List<InterfaceDescription> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<InterfaceDescription> interfaces) {
        this.interfaces = interfaces;
    }

    public void writeConfigToFileTxt(){

        try (FileWriter writer = new FileWriter(this.nameRouter, false))
        {
            // запись всей строки
            this.getInterfaces().forEach((z) -> {
                try {
                    writer.write(z.toString()+System.getProperty("line.separator"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
}
