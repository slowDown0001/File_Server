package client;

// This interface allows to switch for different commands with a lambda expression
public interface ClientCommand {
    String sendCommand (String c);
}

/*class Test {
    public static void main(String args[])
    {
        String a = "DELETE";

        // lambda expression to define the calculate method
        ClientCommand cc = (String command) -> command + " FILENAME";

        // parameter passed and return type must be
        // same as defined in the prototype
        String ans = cc.sendCommand(a);
        System.out.println(ans);
    }
}*/
