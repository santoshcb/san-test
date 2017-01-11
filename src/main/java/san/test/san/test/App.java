package san.test.san.test;

import javax.ws.rs.core.Response;

import com.san.service.BaseServiceConsumer;

/**
 * Hello world!
 *
 */
public class App extends BaseServiceConsumer {

	public void hit() {

		String endpoint = "https://www.google.co.in/";

		Response response = executeGET(endpoint, false);
		System.out.println(response);

	}

	public static void main(String[] args) {

		App app = new App();
		app.hit();

	}
}
