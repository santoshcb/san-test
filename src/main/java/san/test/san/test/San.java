package san.test.san.test;

import com.github.javafaker.Faker;

public class San {

	public static void main(String[] args) {

		Faker faker = new Faker();

		for (int i = 0; i < 7; i++) {

			System.out.println(faker.country());

		}

	}

}
