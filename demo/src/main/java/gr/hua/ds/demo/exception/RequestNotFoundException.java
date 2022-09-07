package gr.hua.ds.demo.exception;

public class RequestNotFoundException extends RuntimeException{
    public RequestNotFoundException (Long id){
        super("Could not find the request with id "+id+" in the database");
    }
}
