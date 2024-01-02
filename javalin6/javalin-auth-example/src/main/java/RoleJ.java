import io.javalin.security.RouteRole;

public enum RoleJ implements RouteRole { ANYONE, USER_READ, USER_WRITE }
