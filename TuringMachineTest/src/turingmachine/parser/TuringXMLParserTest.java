/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.parser;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import turingmachine.model.TuringMachine;
import turingmachine.model.entities.Alphabet;
import turingmachine.model.entities.State;
import turingmachine.model.entities.Tape;
import turingmachine.model.entities.TapeMotion;
import turingmachine.model.entities.TapeTransition;
import turingmachine.model.entities.Transition;

public class TuringXMLParserTest {

	public static final String DATA_LOCATION = "testdata/TuringXMLParserTest/";

	private TuringMachine turingMachine;
	private TuringXMLParser parser;

	@Before
	public void setUp() {
		this.parser = new TuringXMLParser();

		Alphabet alphabet = new Alphabet();
		alphabet.addSymbol('B', null);
		alphabet.addSymbol('0');
		alphabet.addSymbol('+');

		State initialState = new State("initialState");
		State finalState = new State("finalState", true);

		Tape inputTape = new Tape("inputTape");
		Tape outputTape = new Tape("outputTape");

		Transition zeroTransition = new Transition(initialState, initialState);
		zeroTransition.addTapeTransition(new TapeTransition(inputTape, alphabet.getHiddenCharacter('0'), alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("R")));
		zeroTransition.addTapeTransition(new TapeTransition(outputTape, alphabet.getHiddenCharacter('B'), alphabet.getHiddenCharacter('0'), TapeMotion.valueOf("R")));

		Transition plusTransition = new Transition(initialState, initialState);
		plusTransition.addTapeTransition(new TapeTransition(inputTape, alphabet.getHiddenCharacter('+'), alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("R")));
		plusTransition.addTapeTransition(new TapeTransition(outputTape, alphabet.getHiddenCharacter('B'), alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("S")));

		Transition blankTransition = new Transition(initialState, initialState);
		blankTransition.addTapeTransition(new TapeTransition(inputTape, alphabet.getHiddenCharacter('B'), alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("S")));
		blankTransition.addTapeTransition(new TapeTransition(outputTape, alphabet.getHiddenCharacter('B'), alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("S")));

		this.turingMachine = new TuringMachine();
		this.turingMachine.addState(initialState);
		this.turingMachine.addState(finalState);
		this.turingMachine.addTape(inputTape);
		this.turingMachine.addTape(outputTape);
		this.turingMachine.addTransition(zeroTransition);
		this.turingMachine.addTransition(plusTransition);
		this.turingMachine.addTransition(blankTransition);

		this.turingMachine.initialize("TuringXMLParserTest", alphabet, initialState, inputTape, outputTape);
	}

	@Test
	public void testValidTuringXML() throws TuringXMLParserException {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(TuringXMLParserTest.DATA_LOCATION + "valid_turingMachine.xml");
		TuringMachine turingMachine = this.parser.parse(inputStream);

		Assert.assertEquals(this.turingMachine, turingMachine);
	}

	@Test(expected = TuringXMLParserException.class)
	public void testInvalidTuringXML1() throws TuringXMLParserException {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(TuringXMLParserTest.DATA_LOCATION + "invalid_turingMachine1.xml");
		this.parser.parse(inputStream);
	}

	@Test(expected = TuringXMLParserException.class)
	public void testInvalidTuringXML2() throws TuringXMLParserException {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(TuringXMLParserTest.DATA_LOCATION + "invalid_turingMachine2.xml");
		this.parser.parse(inputStream);
	}
}
