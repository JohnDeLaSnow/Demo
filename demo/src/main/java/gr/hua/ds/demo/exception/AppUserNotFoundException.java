package gr.hua.ds.demo.exception;

public class AppUserNotFoundException extends RuntimeException{
    public AppUserNotFoundException (String username){
        super("Could not find user "+username+" in the database");
    }
}
