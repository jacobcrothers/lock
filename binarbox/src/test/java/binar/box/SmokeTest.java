package binar.box;

import binar.box.rest.*;
import binar.box.rest.payment.PaypalController;
import binar.box.rest.payment.StripeController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmokeTest {

	@Autowired
	private LockController lockController;

	@Autowired
	private AuthenticationController authenticationController;

	@Autowired
	private FileController fileController;

	@Autowired
	private PanelController panelController;

	@Autowired
	private PaypalController paypalController;

	@Autowired
	private StripeController stripeController;

	@Autowired
	private UserController userController;

	@Test
	public void contextLoads() {
		assertThat(lockController).isNotNull();
		assertThat(authenticationController).isNotNull();
		assertThat(fileController).isNotNull();
		assertThat(panelController).isNotNull();
		assertThat(paypalController).isNotNull();
		assertThat(stripeController).isNotNull();
		assertThat(userController).isNotNull();
	}

}
