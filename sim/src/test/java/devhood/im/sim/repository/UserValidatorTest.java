package devhood.im.sim.repository;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import devhood.im.sim.model.User;
import devhood.im.sim.service.UserValidator;

public class UserValidatorTest {

	private UserValidator underTest;

	@Before
	public void setup() {
		underTest = new UserValidator();
	}

	@Test
	public void testIsValid() {
		String currentUser = System.getProperty("user.name");

		User user = new User();
		user.setAddress("localhost");
		user.setName(currentUser);

		boolean isvalid = underTest.isValid(user);
		Assert.assertTrue(isvalid);
	}

	@Test
	public void testIsValid_null() {
		boolean isvalid = underTest.isValid(null);
		Assert.assertFalse(isvalid);
	}

	@Test
	public void testIsValid_isolated() {
		underTest.setValidationCommand("echo username");

		User user = new User();
		user.setAddress("localhost");
		user.setName("username");

		boolean isvalid = underTest.isValid(user);
		Assert.assertTrue(isvalid);
	}

}
