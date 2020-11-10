package OnlineUsersDTO;

import java.util.HashMap;
import java.util.Map;

public class OnlineUsersDTO {
    private boolean isSuccess;
    private Map<String, String> users = new HashMap<>();

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }
}
