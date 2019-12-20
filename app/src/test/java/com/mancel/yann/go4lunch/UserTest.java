package com.mancel.yann.go4lunch;

import com.mancel.yann.go4lunch.models.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Yann MANCEL on 20/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch
 *
 * Test on {@link User}.
 */
public class UserTest {

    // FIELDS --------------------------------------------------------------------------------------

    private User mUser = new User();

    // METHODS -------------------------------------------------------------------------------------

    @Test
    public void equals_Should_Correctly_Perform() {
        assertEquals("Same address", this.mUser, this.mUser);
        assertFalse("Argument null", this.mUser.equals(null));
        assertFalse("Argument with different class", this.mUser.equals("a String class"));

        // 2 objects with the same field values (the comparison is just between uid field)
        final User userWithSameArgument = new User();

        assertEquals("2 objects with the same field values", this.mUser, userWithSameArgument);
    }

    @Test
    public void copy_Should_Correctly_Perform() {
        assertFalse("Same address", this.mUser.copy(this.mUser));
        assertFalse("Argument null", this.mUser.copy(null));
        assertFalse("Argument with different class", this.mUser.copy("a String class"));

        // Good copy
        final User userWithSameArgument = new User("ui1", "Yann", "photo");

        assertTrue("Good copy", this.mUser.copy(userWithSameArgument));
        assertEquals("Same mUid", this.mUser.getUid(), userWithSameArgument.getUid());
        assertEquals("Same mUsername", this.mUser.getUsername(), userWithSameArgument.getUsername());
        assertEquals("Same mUrlPicture", this.mUser.getUrlPicture(), userWithSameArgument.getUrlPicture());
    }
}
