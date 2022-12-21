package pizzeria.order.domain.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class StoreService {
    private final transient StoreRepository storeRepo;

    @Autowired
    public StoreService(StoreRepository storeRepo){
        this.storeRepo = storeRepo;
    }

    public Store addStore(Store store) throws Exception {
        if (store == null || storeRepo.existsById(store.getId())) {
            throw new StoreAlreadyExistException();
        }

        if (!verifyEmailFormat(store.getContact())) {
            throw new InvalidEmailException();
        }

        if (!verifyLocationFormat(store.getLocation())) {
            throw new InvalidLocationException();
        }

        return storeRepo.save(store);
    }

    public boolean editStore(Long id, Store store) throws Exception {
        Optional<Store> optionalStore = storeRepo.findById(id);

        if (optionalStore.isEmpty()) {
            throw new StoreDoesNotExistException();
        }

        if (!verifyEmailFormat(store.getContact())) {
            throw new InvalidEmailException();
        }

        if (!verifyLocationFormat(store.getLocation())) {
            throw new InvalidLocationException();
        }

        optionalStore.get().setContact(store.getContact());
        optionalStore.get().setLocation(store.getLocation());

        storeRepo.save(optionalStore.get());

        return true;
    }

    public boolean deleteStore(Long storeId) throws StoreDoesNotExistException {
        Optional<Store> optionalStore = storeRepo.findById(storeId);

        if (optionalStore.isEmpty()) {
            throw new StoreDoesNotExistException();
        }

        return storeRepo.deleteStoreById(storeId) != 0;
    }

    /*public boolean notifyStore(Long storeId, String notificationType, Order order) throws StoreDoesNotExistException {
        Optional<Store> optionalStore = storeRepo.findById(storeId);

        if (optionalStore.isEmpty()) {
            throw new StoreDoesNotExistException();
        }

        SendEmailRequestModel outgoing = new SendEmailRequestModel();
        outgoing.setStore(storeId);
        outgoing.setNotificationType(notificationType);
        outgoing.setOrder(order);

        RestTemplate restTemplate = new RestTemplate();

        Email response = restTemplate.postForObject("http://localhost:8082/store/send_email", outgoing, Email.class);

        return response != null;
    }*/

    /**
     * Get the email corresponding to the storeID
     * @param id ID of the store
     * @return Email of the corresponding store
     */
    public String getEmailById(Long id) {
        if (!storeRepo.existsById(id)) {
            return null;
        }
        return storeRepo.findById(id).get().getContact();
    }

    public List<Store> getAllStores() {
        return storeRepo.findAll();
    }

    private boolean verifyEmailFormat(String testEmail) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(regexPattern);
        return pattern.matcher(testEmail).matches();
    }

    private boolean verifyLocationFormat(String testLocation) {
        String regexPattern = "^(?:NL-)?(\\d{4})\\s*([A-Z]{2})$";

        Pattern pattern = Pattern.compile(regexPattern);
        return pattern.matcher(testLocation).matches();
    }

    @SuppressWarnings("PMD")
    public static class StoreDoesNotExistException extends Exception {
        @Override
        public String getMessage(){
            return "The store with the id provided does not exist";
        }
    }

    @SuppressWarnings("PMD")
    public static class StoreAlreadyExistException extends Exception {
        @Override
        public String getMessage(){
            return "There already exists a store with the same id";
        }
    }

    @SuppressWarnings("PMD")
    public static class InvalidEmailException extends Exception {
        @Override
        public String getMessage(){
            return "Invalid email format";
        }
    }

    @SuppressWarnings("PMD")
    public static class InvalidLocationException extends Exception {
        @Override
        public String getMessage(){
            return "Invalid location format";
        }
    }
}
