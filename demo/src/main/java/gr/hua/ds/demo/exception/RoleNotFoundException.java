package gr.hua.ds.demo.exception;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException (String roleName){
        super("Could not find role "+roleName+" in the database");
    }
}
