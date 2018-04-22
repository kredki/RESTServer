package oceny;

public class XmlError {
    private String type;
    private String message;

    public XmlError(String type, String message){
        this.type = type;
        this.message = message;
    }

    public String getType(){
        return this.type;
    }

    public String getMessage(){
        return this.message;
    }
}
