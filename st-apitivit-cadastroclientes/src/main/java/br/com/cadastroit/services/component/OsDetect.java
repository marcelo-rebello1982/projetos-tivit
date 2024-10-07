package br.com.cadastroit.services.component;

public class OsDetect {
    
    public static String OS_NAME(){
        String os = System.getProperty("os.name");
        return os.toLowerCase();
    }
    
    public static void main(String[] args) {
        System.out.println(OsDetect.OS_NAME());
    }
}
