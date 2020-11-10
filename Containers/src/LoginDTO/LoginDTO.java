package LoginDTO;

public class LoginDTO {
    private boolean isError;
    private String errorMessage;
    private String url;
    
    public void setError(boolean error) {
        this.isError = error;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}