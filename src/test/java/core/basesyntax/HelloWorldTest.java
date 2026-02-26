package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.exception.InvalidDataException;
import core.basesyntax.model.User;
import core.basesyntax.service.RegistrationService;
import core.basesyntax.service.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {
    private static RegistrationService service = new RegistrationServiceImpl();
    private static StorageDao storageDao = new StorageDaoImpl();

    @BeforeEach
    void setUp() {
        service = new RegistrationServiceImpl();
    }

    //null block
    @Test
    public void userNull_NotOK() {

        assertThrows(InvalidDataException.class, () -> service.register(null));

    }

    @Test
    void nullLogin_notOk() {
        User user = new User();
        user.setPassword("123456");
        user.setAge(20);

        assertThrows(InvalidDataException.class,
                () -> service.register(user));
    }

    //password block
    @Test
    void nullPassword_notOk() {
        User user = new User();
        user.setLogin("s1mple");
        user.setAge(19);

        assertThrows(InvalidDataException.class,
                () -> service.register(user));
    }

    @Test
    void shortPassword_notOk() {
        User user = new User();
        user.setPassword("s1ma");
        user.setAge(19);
        user.setLogin("s1mple");

        assertThrows(InvalidDataException.class,
                () -> service.register(user));
    }

    @Test
    void edgePassword_notOk() {
        User user = new User();
        user.setPassword("111112");
        user.setAge(19);
        user.setLogin("forest");

        User actual = service.register(user);
        assertEquals(user,actual);
    }

    //age block
    @Test
    void underAge_notOk() {
        User user = new User();
        user.setPassword("111112");
        user.setAge(15);
        user.setLogin("s1mple");

        assertThrows(InvalidDataException.class,() -> service.register(user));

    }

    @Test
    void edgeAge_notOk() {
        User user = new User();
        user.setPassword("111112");
        user.setAge(18);
        user.setLogin("s1mple");
        User actual = service.register(user);
        assertEquals(user,actual);
    }

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

}
