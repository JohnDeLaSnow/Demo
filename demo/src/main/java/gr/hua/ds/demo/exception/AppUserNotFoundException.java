package gr.hua.ds.demo.exception;

public class AppUserNotFoundException extends RuntimeException{
    public AppUserNotFoundException (Long id){
        super("Could not find the user with id "+id+" in the database");
    }
}
