package com.company;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserConfigJunos {
    private String filename;
    private String configLine;

    public ParserConfigJunos(String filename) {
        this.filename = filename;
        this.configLine = "";
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<InterfaceDescription> parse() throws IOException {

        Files.lines(Paths.get(filename),
                StandardCharsets.UTF_8).forEach(
                        (line)-> {configLine+=line.toString(); }
        );
        configLine = cutInterfaceInString(configLine);
        List<InterfaceDescription> interfaceDescriptionList = new ArrayList<>();
        while (configLine.length()>20) {
            interfaceDescriptionList.add(getInterfaceDescription());

        }

        return interfaceDescriptionList;
    }

    private String cutInterfaceInString(String line){
        if (line.startsWith("interfaces {")){
            line = line.substring(13).trim();
            line = line.substring(0,line.length()-1).trim();
        }else{
            return null;
        }

        return line;
    }

    private InterfaceDescription getInterfaceDescription(){
        InterfaceDescription description = new InterfaceDescription();

        //interfaceName
        String inter = configLine.substring(0, configLine.indexOf("{"));
        if (isPhysicalInterface(inter)){

            String[] desc1 = inter.split("-");
            description.setInterfaceType(desc1[0]);
            String[] desc2 = desc1[1].split("/");
            description.setFpc(Integer.parseInt(desc2[0].trim()));
            description.setPic(Integer.parseInt(desc2[1].trim()));
            if (desc2[2].contains(":")){
                String[] descP = desc2[2].split(":");
                description.setPort(Integer.parseInt(descP[0].trim()));
                description.setSubPort(Integer.parseInt(descP[1].trim()));
            }else {
                description.setPort(Integer.parseInt(desc2[2].trim()));
                description.setPort(Integer.parseInt(desc2[2].trim()));
            }
        }else{
            description.setInterfaceType(inter);
        }
        configLine = configLine.substring(configLine.indexOf("{")-1);

        //all description line
        String descAllDescriptionLine = cutAllDescriptionLine();
        description.setDescription(cutDescription(descAllDescriptionLine));
        description.setPurpose(cutPurpose(description.getDescription()));
        description.setRouter(cutRouter(description.getDescription()));
        description.setTrunkName(cutTrunkName(descAllDescriptionLine));
        description.setNumber(cutNumber(descAllDescriptionLine));
        description.setProviderOrder(cutProviderOrder(description.getDescription()));
        description.setLocalOrder(cutLocalOrder(description.getDescription()));
        description.setIpAddress(cutIpAddress(descAllDescriptionLine));
        description.setRemoteInterface(cutRemoteInterface(description.getDescription()));


//        System.out.println("add interface "+inter);
//        System.out.println("remote interface "+description.getRemoteInterface());
//        if (inter.contains("et-0/1/4:1")) {
//            String end = "";
//        }
        return description;
    }

    private String cutRemoteInterface(String line) {
        String result = "";
        Pattern patternRouter1 = Pattern.compile("[a-zA-Z0-9\\-]{3,5}\\/[0-9]{1,2}\\/[0-9]{1,2}[\\/0-9]{0,3}");
        Matcher matcher = patternRouter1.matcher(line);
        while (matcher.find()) {
            result += line.substring(matcher.start(), matcher.end())+" ";
        }

        return result.trim();
    }

    private String cutIpAddress(String line) {
        String result = "";

        if (line.contains("family inet")){
            String queryString = line.substring(line.indexOf("family inet"));
            Pattern patternRouter1 = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}.[0-9//]{1,6}");
            Matcher matcher = patternRouter1.matcher(queryString);
            while (matcher.find()) {
                result += queryString.substring(matcher.start(), matcher.end())+" ";
            }
        }
        return result.trim();
    }

    private String cutLocalOrder(String description) {
        String result="";
        if (description.toLowerCase().contains("1c")) {
            Pattern patternRouter1 = Pattern.compile("[1cC]{2}[\\-\\w]{6,9}");
            Matcher matcher = patternRouter1.matcher(description);
            while (matcher.find()) {
                result += description.substring(matcher.start(), matcher.end())+" ";
            }
        }
        if (description.toLowerCase().contains("ofm")){
            Pattern ofmPattern = Pattern.compile("[ofm]{3}[\\-\\w]{6,9}");
            Matcher matcherOFM = ofmPattern.matcher(description.toLowerCase());
            while (matcherOFM.find()) {
                result += description.substring(matcherOFM.start(), matcherOFM.end())+" ";
            }
        }
        if (description.toLowerCase().contains("opl")){
            Pattern oplPattern = Pattern.compile("[opl]{3}[\\-\\w]{6,9}");
            Matcher matcherOPL = oplPattern.matcher(description.toLowerCase());
            while (matcherOPL.find()) {
                result += description.substring(matcherOPL.start(), matcherOPL.end())+" ";
            }
        }
        if (description.toLowerCase().contains("po")){
            Pattern poPattern = Pattern.compile("[po]{2}[\\-\\w]{6,9}");
            Matcher matcherPO = poPattern.matcher(description.toLowerCase());
            while (matcherPO.find()) {
                result += description.substring(matcherPO.start(), matcherPO.end())+" ";
            }
        }

        return result.trim();
    }

    private String cutProviderOrder(String descriptionLine) {

        if (descriptionLine.contains("+SVN")){
            String order1=descriptionLine.substring(descriptionLine.indexOf("SVN"),descriptionLine.indexOf("+"));
            String dl2 = descriptionLine.substring(descriptionLine.indexOf("+")).trim();
            String order2=dl2.substring(dl2.indexOf("SVN"),dl2.indexOf("SVN")+8).trim();
            return checkEnd(order1+"+"+order2);
        }else if (descriptionLine.contains("SVN")){
            String order = descriptionLine
                    .substring(descriptionLine.indexOf("SVN"), descriptionLine.indexOf("SVN") + 8)
                    .trim();
            order = order.contains(")") ? order.replace('-', ' ').trim() : order;
            return checkEnd(order);
        }

        return "";
    }

    private String cutNumber(String description) {

        if (description.contains("#")){
            return checkNumber(checkEnd(description.substring(description.indexOf("#"),description.indexOf("#")+3).trim()));
        }else if (description.toLowerCase().contains("unbundle")){
            return "Unbundled";
        }
        return "None";
    }

    private String checkNumber(String s) {

        String test = s.substring(s.indexOf("#") + 1);
        if (test.length()<2){
            return "#0"+test;
        }

        return s;
    }

    private String cutRouter(String description) {
        Pattern patternRouter1 = Pattern.compile("[\\w\\-]{1,10}\\.[a-zA-Z]*");
        Pattern patternRouter2 = Pattern.compile("[\\w\\-]{1,10}\\.[\\w\\-]{1,10}\\.[a-zA-Z]*");
        Matcher matcher2 = patternRouter2.matcher(description);
        boolean m2 = matcher2.find();
        Matcher matcher1 = patternRouter1.matcher(description);
        boolean m1 = matcher1.find();
        if (m2){
            return description.substring(matcher2.start(),matcher2.end()).trim();
        } else if (m1) {
            return description.substring(matcher1.start(),matcher1.end()).trim();
        }


        return "Unknown";
    }

    private String cutPurpose(String description) {
        if (description.length()<3){
            return "Disabled";
        }else if (description.startsWith("[")){
            return description.substring(0,description.lastIndexOf("]")+1);
        }else if (description.toLowerCase().startsWith("reserve")){
            return "Reserve";
        }else if (description.toLowerCase().contains("test")){
            return "Test";
        }
        return "Disabled";
    }

    private String cutTrunkName(String line) {
        if (line.contains("gigether-options {")&&
                line.contains("802.3ad")&&line.contains(";")){
            return line.substring(line.indexOf("802.3ad")+8,line.lastIndexOf(";"));
        }
        return "None";

    }

    private boolean isPhysicalInterface(String interfaceName){
        if (interfaceName.contains("-")&&interfaceName.contains("/")){
            return true;
        }
        return false;
    }

    private String cutAllDescriptionLine(){
        StringBuilder description = new StringBuilder();
        Integer start = configLine.indexOf("{");
        Integer s = start;
        Boolean findEndDescription = true;
        Integer end = start;
        while (findEndDescription){
            Integer p = configLine.indexOf("}");
            description.append(configLine.substring(0, p+1));
            if (!isInterfaceDescriptionFullCut(description.toString())){
                configLine = configLine.substring(p+1);
            }else {
                configLine = configLine.substring(p+1).trim();
                findEndDescription=false;
            }
        }
        return description.toString();
    }

    private boolean isInterfaceDescriptionFullCut(String description){
        int countS=0, countP = 0;
        for (char element : description.toCharArray()){
            if (element == '{') countS++;
            if (element == '}') countP++;
        }
        if (countP==countS) return true;
        return false;
    }

    private String cutDescription(String line){
        if (line.contains("description")&&line.length()>29) {
            if (line.contains("\"")) {
                return line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\";"));
            }else{
                return line.substring(line.indexOf("description")+12, line.indexOf(";"));
            }
        }else if (line.contains("description")){
            return line.substring(line.indexOf("description")+12,line.lastIndexOf(";"));
        }
        return "";
    }

    private String checkEnd(String line){
        String end = line.substring(line.length()-1);
        if (end.contains(",")||end.contains(";")||end.contains(")")){
            return line.substring(0,line.length()-1);
        }


        return line;
    }


}
