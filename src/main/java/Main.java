import com.google.gson.Gson;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        final UserService userService = new UserServiceMapImpl();
        port(8080);


        get("/hello", (req, res) -> "Hello ");
        get("/hello/:name", (req,res)->{
            return "Hello, "+ req.params(":name");
        });

        //Scanner myObj = new Scanner(System.in);
        //String userName;
        //System.out.println("Enter username");
        //userName = myObj.nextLine();
        //System.out.println("Username is: " + userName);
        //get("/hello2", (req, res) -> "Hello " + userName);

        get("/users", (req, res) -> {
            res.type("application/json");

            return new Gson().toJson(userService.getUsers());
        });
        get("/users/:id", (req, res) -> {
            res.type("application/json");

            return new Gson().toJson(userService.getUser(req.params(":id")));
        });
        post("/users", (req, res) -> {
            res.type("application/json");
            User user = new Gson().fromJson(req.body(), User.class);
            userService.addUser(user);

            return new Gson()
                    .toJson(user);
        });
        put("/users/:id", (req, res) -> {
            res.type("application/json");
            User toEdit = new Gson().fromJson(req.body(), User.class);
            User editedUser = userService.editUser(toEdit);

            if (editedUser != null) {
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
                        new Gson().toJsonTree(editedUser)));
            } else {
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,
                        new Gson().toJson("User not found or error in edit")));
            }
        });
        delete("/users/:id", (req, res) -> {
            res.type("application/json");
            userService.deleteUser(req.params(":id"));
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
                        "user deleted"));
        });
        options("/users/:id", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,(userService.userExist
                            (req.params(":id"))) ? "User exists" : "User does not exists" ));
        });
    }
}