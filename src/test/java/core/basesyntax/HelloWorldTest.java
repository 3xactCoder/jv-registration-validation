package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.exception.InvalidDataException;
import core.basesyntax.model.User;
import core.basesyntax.service.RegistrationService;
import core.basesyntax.service.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {
    private RegistrationService service = new RegistrationServiceImpl();
    private StorageDao storageDao = new StorageDaoImpl();

    @BeforeEach
    void setUp() {
        Storage.people.clear();
        service = new RegistrationServiceImpl();
    }

    //null block
    @Test
    public void register_userNull_NotOK() {

        assertThrows(InvalidDataException.class, () -> service.register(null));

    }

    @Test
    void register_nullLogin_notOk() {
        User user = new User();
        user.setPassword("123456");
        user.setAge(20);

        assertThrows(InvalidDataException.class,
                () -> service.register(user));
    }

    //password block
    @Test
    void register_nullPassword_notOk() {
        User user = new User();
        user.setLogin("s1mple");
        user.setAge(19);

        assertThrows(InvalidDataException.class,
                () -> service.register(user));
    }

    @Test
    void register_shortPassword_notOk() {
        User user = new User();
        user.setPassword("s1ma");
        user.setAge(19);
        user.setLogin("s1mple");

        assertThrows(InvalidDataException.class,
                () -> service.register(user));
    }

    @Test
    void register_edgePassword_notOk() {
        User user = new User();
        user.setPassword("111112");
        user.setAge(19);
        user.setLogin("forest");

        User actual = service.register(user);
        assertEquals(user,actual);
    }

    //age block
    @Test
    void register_underAge_notOk() {
        User user = new User();
        user.setPassword("111112");
        user.setAge(15);
        user.setLogin("s1mple");

        assertThrows(InvalidDataException.class,() -> service.register(user));

    }

    @Test
    void register_edgeAge_notOk() {
        User user = new User();
        user.setPassword("111112");
        user.setAge(18);
        user.setLogin("s1mple");
        User actual = service.register(user);
        assertEquals(user,actual);
    }

    @Test
    void register_negativeAge_notOk() {
        User user = new User();
        user.setPassword("111112");
        user.setAge(-11);
        user.setLogin("s1mple");

        assertThrows(InvalidDataException.class,() -> service.register(user));
    }


    //other things
    @Test
    void register_duplicateLogin_notOk() {

        User existing = new User();
        existing.setLogin("frozen");
        existing.setPassword("123456");
        existing.setAge(20);

        storageDao.add(existing);

        User newUser = new User();
        newUser.setLogin("frozen");
        newUser.setPassword("abcdef");
        newUser.setAge(25);

        assertThrows(InvalidDataException.class,
                () -> service.register(newUser));
    }

    @Test
    void register_successfulPlacement_Ok() {
        User user = new User();
        user.setLogin("frozen");
        user.setPassword("123456");
        user.setAge(20);

        User registered = service.register(user);
        assertEquals(user, registered);
        assertEquals(user, new StorageDaoImpl().get("frozen"));
    }

}
