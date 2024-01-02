import io.javalin.http.Context;
import io.javalin.http.Header;
import io.javalin.http.UnauthorizedResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AuthJ {

    public static void handleAccess(Context ctx) {
        var permittedRoles = ctx.routeRoles();
        if (permittedRoles.contains(RoleJ.ANYONE)) {
            return; // anyone can access
        }
        if (userRoles(ctx).stream().anyMatch(permittedRoles::contains)) {
            return; // user has role required to access
        }
        ctx.header(Header.WWW_AUTHENTICATE, "Basic");
        throw new UnauthorizedResponse();
    }

    public static List<RoleJ> userRoles(Context ctx) {
        return Optional.ofNullable(ctx.basicAuthCredentials())
            .map(credentials -> userRolesMap.getOrDefault(new Pair(credentials.getUsername(), credentials.getPassword()), List.of()))
            .orElse(List.of());
    }

    record Pair(String a, String b) {}
    private static final Map<Pair, List<RoleJ>> userRolesMap = Map.of(
        new Pair("alice", "weak-1234"), List.of(RoleJ.USER_READ),
        new Pair("bob", "weak-123456"), List.of(RoleJ.USER_READ, RoleJ.USER_WRITE)
    );

}
