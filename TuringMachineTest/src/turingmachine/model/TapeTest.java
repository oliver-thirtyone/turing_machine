/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.model;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import turingmachine.model.entities.Tape;
import turingmachine.model.entities.TapeMotion;

public class TapeTest {

	private Tape tape;

	@Before
	public void setUp() {
		this.tape = new Tape("T0");
	}

	@Test
	public void testABC() {
		this.initTape("ABC");

		Assert.assertEquals(new Character('A'), this.tape.read());
		this.tape.write(null);
		Assert.assertEquals(null, this.tape.read());

		this.tape.moveCursor(TapeMotion.R);
		Assert.assertEquals(new Character('B'), this.tape.read());
		this.tape.write(null);

		this.tape.moveCursor(TapeMotion.L);
		Assert.assertEquals(null, this.tape.read());

		this.tape.moveCursor(TapeMotion.R);
		Assert.assertEquals(null, this.tape.read());

		this.tape.moveCursor(TapeMotion.R);
		Assert.assertEquals(new Character('C'), this.tape.read());

		this.tape.moveCursor(TapeMotion.R);
		Assert.assertEquals(null, this.tape.read());

		this.tape.moveCursor(TapeMotion.L);
		Assert.assertEquals(new Character('C'), this.tape.read());

		this.tape.write('X');
		this.tape.moveCursor(TapeMotion.S);
		Assert.assertEquals(new Character('X'), this.tape.read());
	}

	@Test
	public void test1234567890() {
		String tape = "0123456789";
		this.initTape(tape);

		for (int i = 0; i < tape.length(); i++) {
			Assert.assertEquals(new Character(tape.charAt(i)), this.tape.read());
			this.tape.moveCursor(TapeMotion.R);
		}

		Assert.assertEquals(null, this.tape.read());
		this.tape.moveCursor(TapeMotion.L);

		for (int i = tape.length(); i > 0; i--) {
			Assert.assertEquals(new Character(tape.charAt(i - 1)), this.tape.read());
			this.tape.moveCursor(TapeMotion.L);
		}
	}

	private void initTape(String input) {
		char[] characters = input.toCharArray();

		// Add all characters to the right side of the tape
		for (int i = characters.length; i > 0; i--) {
			this.tape.write(characters[i - 1]);
			this.tape.moveCursor(TapeMotion.L);
		}

		// Select the first character
		this.tape.moveCursor(TapeMotion.R);
	}
}
