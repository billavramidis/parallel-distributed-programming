package lab9;

public class ClientMessage{
    private final String message;
    private final int option;

    public ClientMessage(String message, int option){
        this.message = message;
        this.option = option;
    }

    public String getMessage() {
        return message;
    }

    public int getOption() {
        return option;
    }
}