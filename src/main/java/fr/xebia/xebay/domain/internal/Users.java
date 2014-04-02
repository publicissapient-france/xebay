package fr.xebia.xebay.domain.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.xebia.xebay.domain.BidException;
import fr.xebia.xebay.domain.UserNotAllowedException;
import fr.xebia.xebay.domain.PublicUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.xebia.xebay.domain.internal.AdminUser.ADMIN_ROLE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;

public class Users {
    private static final Logger log = LoggerFactory.getLogger("Users");

    private static final String CHARS_IN_KEY = "" +
            "abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "0123456789" +
            "-_";

    private final Set<User> users;
    private final Random random;

    public Users() {
        this(new HashSet<User>());
    }

    public Users(Set<User> savedUsers) {
        this.random = new Random();
        this.users = savedUsers;
        this.users.add(new AdminUser());
    }

    public Set<PublicUser> getUsers() {
        return users.stream()
                .filter(user -> !user.isInRole(ADMIN_ROLE))
                .map(user -> user.toPublicUser())
                .sorted((user1, user2) -> Double.compare(user1.getBalance() + user1.getAssets(), user2.getBalance() + user2.getAssets()))
                .collect(toSet());
    }

    public User create(String name) throws BidException {
        if (name == null || name.isEmpty()) {
            throw new BidException("can't create user without name");
        }
        if (containsName(name)) {
            throw new BidException(format("\"%s\" is already registered", name));
        }
        String key;
        do {
            StringBuilder randomKey = new StringBuilder();
            range(0, 16).forEach((i) -> randomKey.append(CHARS_IN_KEY.charAt(random.nextInt(CHARS_IN_KEY.length()))));
            key = randomKey.toString();
        } while (containsKey(key));
        User newUser = new User(key, name);
        users.add(newUser);
        saveUsers(users);
        return newUser;
    }

    public User remove(String key) throws UserNotAllowedException, BidException {
        if (AdminUser.KEY.equals(key)) {
            throw new BidException("admin can't be removed");
        }
        User user = getUser(key);
        users.remove(user);
        saveUsers(users);
        return user;
    }

    public User getUser(String key) throws UserNotAllowedException {
        return this.users.stream()
                .filter((user) -> user.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new UserNotAllowedException(format("key \"%s\" is unknown", key)));
    }

    public Set<fr.xebia.xebay.domain.User> getAdminUserSet() {

        return users.stream()
                .filter(user -> !user.isInRole(ADMIN_ROLE))
                .map(User::toUser).collect(Collectors.toSet());
    }

    private boolean containsKey(String key) {
        return users.stream().anyMatch((user) -> user.getKey().equals(key));
    }

    private boolean containsName(String name) {
        return users.stream().anyMatch((user) -> user.getName().equals(name));
    }

    public static Set<User> loadUsers(){
        Set<SerializedUser> usersInFile = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            usersInFile = mapper.readValue(new File("users.json"), new TypeReference<Set<SerializedUser>>() {
            });
        } catch (IOException e) {
            log.debug("Could not load saved users");
            return new HashSet<>();

        }
        log.info("serialized users " + usersInFile.stream().toString());
        Set<User> users = usersInFile.stream()
                .filter(user -> !AdminUser.isAdmin(user))
                .map((user) -> new User(user.getKey(), user.getName())).collect(toSet());
        log.info(" users " + users.stream().toString());
        return users;
    }

    public void saveUsers(Set<User> users){
        ObjectMapper mapper = new ObjectMapper();
        Set<SerializedUser> serializedUsers = users.stream()
                .filter(user -> ! user.isInRole(ADMIN_ROLE))
                .map((user) -> new SerializedUser(user.getKey(), user.getName())).collect(toSet());
        try {
            mapper.writeValue(new File("users.json"), serializedUsers);
        } catch (IOException e) {
            log.debug("Could not persist users");
        }

    }

}
