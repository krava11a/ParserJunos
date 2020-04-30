package com.company;

public class InterfaceDescription {
//    private String id;
    private String interfaceType;
    //slot or flexible PIC concentrator
    private Integer fpc;
    //physical interface card
    private Integer pic;
    private Integer port;
    private Integer subPort;
    private String trunkName;
    private String purpose;
    private String router;
    private String number;
    private String providerOrder;
    private String localOrder;
    private String Description;
    private String ipAddress;
    private String RemoteInterface;

    public InterfaceDescription() {
        this.interfaceType = "";
        this.fpc = -1;
        this.pic = -1;
        this.port = -1;
        this.subPort = -1;
        this.trunkName = "";
        this.purpose = "";
        this.router = "";
        this.number = "";
        this.providerOrder = "";
        this.localOrder = "";
        Description = "";
        this.ipAddress = "";
        RemoteInterface = "";
    }

    public InterfaceDescription(String interfaceType, Integer fpc, Integer pic, Integer port, Integer subPort, String trunkName, String purpose, String router, String number, String providerOrder, String localOrder, String description, String ipAddress, String remoteInterface) {
        this.interfaceType = interfaceType;
        this.fpc = fpc;
        this.pic = pic;
        this.port = port;
        this.subPort = subPort;
        this.trunkName = trunkName;
        this.purpose = purpose;
        this.router = router;
        this.number = number;
        this.providerOrder = providerOrder;
        this.localOrder = localOrder;
        Description = description;
        this.ipAddress = ipAddress;
        RemoteInterface = remoteInterface;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public Integer getFpc() {
        return fpc;
    }

    public void setFpc(Integer fpc) {
        this.fpc = fpc;
    }

    public Integer getPic() {
        return pic;
    }

    public void setPic(Integer pic) {
        this.pic = pic;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getSubPort() {
        return subPort;
    }

    public void setSubPort(Integer subPort) {
        this.subPort = subPort;
    }

    public String getTrunkName() {
        return trunkName;
    }

    public void setTrunkName(String trunkName) {
        this.trunkName = trunkName;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProviderOrder() {
        return providerOrder;
    }

    public void setProviderOrder(String providerOrder) {
        this.providerOrder = providerOrder;
    }

    public String getLocalOrder() {
        return localOrder;
    }

    public void setLocalOrder(String localOrder) {
        this.localOrder = localOrder;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRemoteInterface() {
        return RemoteInterface;
    }

    public void setRemoteInterface(String remoteInterface) {
        RemoteInterface = remoteInterface;
    }

    @Override
    public String toString() {
        return   interfaceType
                + '\t' + fpc
                + '\t' + pic
                +'\t' + port
                + '\t' + subPort
                +'\t' + trunkName
                +'\t' + purpose
                +'\t' + router
                +'\t' + number
                +'\t' + providerOrder
                +'\t' + localOrder
                +'\t' + Description
                +'\t' + ipAddress
                +'\t' + RemoteInterface;

    }
}
