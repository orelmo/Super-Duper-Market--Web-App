package SDMSystem;


import EngineClasses.ChatManager.ChatManager;
import EngineClasses.Users.User;
import EngineClasses.Users.UserManager;
import EngineClasses.Users.eUserType;
import Exceptions.IdAmbiguityException;
import ItemsDetailsContainer.ItemsDetailsContainer;
import MarketSystemInterface.MarketSystem;
import OrderConteiner.OrderContainer;
import StoresDetailsConteiner.StoresDetailsContainer;
import ZoneDTO.ZoneDTO;
import jaxbClasses.SuperDuperMarketDescriptor;

import javax.naming.directory.NoSuchAttributeException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class SDMSystem{
    private SDMSystemCustomers systemCustomers;
    private SDMSystemSellers systemSellers;
    private SDMZones systemZones;
    private UserManager userManager;
    private ChatManager chatManager;

    public SDMSystem() {
        this.systemCustomers = new SDMSystemCustomers();
        this.systemSellers = new SDMSystemSellers();
        this.systemZones = new SDMZones();
        this.chatManager = new ChatManager();
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void addUser(User newUser) {
        if (newUser.getUserType().equals(eUserType.Seller)) {
            this.systemSellers.add(newUser.getId(), new SDMSystemSeller(newUser));
        } else {
            this.systemCustomers.add(newUser.getId(), new SDMSystemCustomer(newUser));
        }
    }

    public SDMZones getSystemZones() {
        return systemZones;
    }

    public boolean loadFile(String fileContent, String username, StringBuilder errorMessage) {
        try {
            JAXBContext jaxbContent = JAXBContext.newInstance(SuperDuperMarketDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContent.createUnmarshaller();
            StringReader stringReader = new StringReader(fileContent);
            SuperDuperMarketDescriptor sdmDescriptor = (SuperDuperMarketDescriptor) jaxbUnmarshaller.unmarshal(stringReader);

            SDMZone newZoneToTest = new SDMZone();
            extractSDMClasses(newZoneToTest, sdmDescriptor, username);
            newZoneToTest.checkIfAllItemsHaveSeller();
            newZoneToTest.analyzeZoneDetails();

            this.systemZones.add(newZoneToTest.getName(), newZoneToTest);

            return true;

        } catch (Exception e) {
            errorMessage.append(e.getMessage());
            return false;
        }
    }

    private void extractSDMClasses(SDMZone newZoneToTest, SuperDuperMarketDescriptor sdm, String username) throws NoSuchAttributeException {
        checkIfZoneNameTaken(sdm.getSDMZone().getName());
        newZoneToTest.setName(sdm.getSDMZone().getName());
        newZoneToTest.extractSDMItems(sdm.getSDMItems());
        newZoneToTest.setOwner(this.systemSellers.get(username));
        newZoneToTest.extractSDMStores(sdm.getSDMStores());
        newZoneToTest.setSdmSystem(this);
    }

    private void checkIfZoneNameTaken(String name) {
        if (this.systemZones.isExist(name)) {
            throw new IdAmbiguityException("Zone " + name + " is already taken");
        }
    }

    public List<ZoneDTO> getZonesDTO() {
        return this.systemZones.getZoneDTOList();
    }

    public SDMSystemCustomer getCustomer(String customerUsername) {
        return this.systemCustomers.get(customerUsername);
    }

    public SDMSystemSeller getSeller(String username) {
        return this.systemSellers.get(username);
    }

    public void addChatMessage(String newMessage) {
        this.chatManager.addNewMessage(newMessage);
    }

    public List<String> getDeltaChatMessages(Integer seenMessages) {
        return this.chatManager.getDeltaMessages(seenMessages);
    }
}