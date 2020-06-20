import PasswordHandler.PasswordStorage;

public class PasswordMain {
    public static void main(String[] args) throws PasswordStorage.CannotPerformOperationException {

         String mono40 = "monografico40";
        PasswordStorage passwordMaker = new PasswordStorage();
        System.out.println(passwordMaker.createHash(mono40));

    }
}
