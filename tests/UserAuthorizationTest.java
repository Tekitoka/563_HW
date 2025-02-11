package tests;

import static org.junit.jupiter.api.Assertions.*;

import main.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAuthorizationTests {
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User("testUser", "securePass");
        user2 = new User("testUser", "wrongPass");
    }

    @Test
    void testUsernameCannotBeEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new User("", "password123"));
        assertEquals("Username cannot be empty or null", exception.getMessage());
    }

    @Test
    void testPasswordCannotBeEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new User("testUser", ""));
        assertEquals("Password cannot be empty or null", exception.getMessage());
    }

    @Test
    void testUserEquality() {
        User copyUser = new User("testUser", "securePass");
        assertTrue(user1.equals(copyUser), "Users with the same credentials should be equal");
    }

    @Test
    void testUserInequality() {
        assertFalse(user1.equals(user2), "Users with different passwords should not be equal");
    }

}
