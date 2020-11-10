package LoadXMLDTO;

public class LoadXMLDTO {
    private boolean isValid;
    private String errorMessage;

    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}