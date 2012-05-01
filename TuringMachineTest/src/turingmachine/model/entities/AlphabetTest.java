package turingmachine.model.entities;

import org.junit.Assert;
import org.junit.Test;

import turingmachine.model.entities.Alphabet;

public class AlphabetTest {

	@Test
	public void testAlphabet() {
		Alphabet alphabet = new Alphabet();

		Assert.assertTrue(alphabet.addSymbol('B', null));
		Assert.assertTrue(alphabet.addSymbol('0'));
		Assert.assertTrue(alphabet.addSymbol('1'));

		Assert.assertFalse(alphabet.addSymbol('B', null));
		Assert.assertFalse(alphabet.addSymbol('0'));
		Assert.assertFalse(alphabet.addSymbol('1'));

		Assert.assertEquals(new Character('B'), alphabet.getVisibleCharacter(null));
		Assert.assertEquals(new Character('0'), alphabet.getVisibleCharacter('0'));
		Assert.assertEquals(new Character('1'), alphabet.getVisibleCharacter('1'));

		Assert.assertEquals(null, alphabet.getHiddenCharacter(null));
		Assert.assertEquals(new Character('0'), alphabet.getHiddenCharacter('0'));
		Assert.assertEquals(new Character('1'), alphabet.getHiddenCharacter('1'));

		Assert.assertTrue(alphabet.removeSymbol('B', null));
		Assert.assertTrue(alphabet.removeSymbol('0'));
		Assert.assertTrue(alphabet.removeSymbol('1'));

		Assert.assertFalse(alphabet.removeSymbol('B', null));
		Assert.assertFalse(alphabet.removeSymbol('0'));
		Assert.assertFalse(alphabet.removeSymbol('1'));
	}

}
