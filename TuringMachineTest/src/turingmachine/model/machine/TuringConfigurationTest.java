/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.model.machine;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import turingmachine.model.entities.Alphabet;
import turingmachine.model.entities.State;
import turingmachine.model.entities.Tape;
import turingmachine.model.entities.TapeMotion;
import turingmachine.model.entities.TapeTransition;
import turingmachine.model.entities.Transition;
import turingmachine.model.entities.TuringConfiguration;

public class TuringConfigurationTest {

	private Alphabet alphabet;

	private State initialState;
	private State finalState;

	private Tape inputTape;
	private Tape outputTape;

	private Transition zeroTransition;
	private Transition plusTransition;
	private Transition blankTransition;

	@Before
	public void setUp() {
		this.alphabet = new Alphabet();
		this.alphabet.addSymbol('B', null);
		this.alphabet.addSymbol('0');
		this.alphabet.addSymbol('+');

		this.initialState = new State("initialState");
		this.finalState = new State("finalState", true);

		this.inputTape = new Tape("inputTape");
		this.outputTape = new Tape("outputTape");

		this.zeroTransition = new Transition(this.initialState, this.initialState);
		this.zeroTransition.addTapeTransition(new TapeTransition(this.inputTape, this.alphabet.getHiddenCharacter('0'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("R")));
		this.zeroTransition.addTapeTransition(new TapeTransition(this.outputTape, this.alphabet.getHiddenCharacter('B'), this.alphabet.getHiddenCharacter('0'), TapeMotion.valueOf("R")));

		this.plusTransition = new Transition(this.initialState, this.initialState);
		this.plusTransition.addTapeTransition(new TapeTransition(this.inputTape, this.alphabet.getHiddenCharacter('+'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("R")));
		this.plusTransition.addTapeTransition(new TapeTransition(this.outputTape, this.alphabet.getHiddenCharacter('B'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("S")));

		this.blankTransition = new Transition(this.initialState, this.initialState);
		this.blankTransition.addTapeTransition(new TapeTransition(this.inputTape, this.alphabet.getHiddenCharacter('B'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("S")));
		this.blankTransition.addTapeTransition(new TapeTransition(this.outputTape, this.alphabet.getHiddenCharacter('B'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("S")));
	}

	@Test
	public void testTuringConfiguration() {
		TuringConfiguration configuration = new TuringConfiguration("TuringConfigurationTest", this.alphabet, "initialState", "inputTape", "outputTape");

		Assert.assertEquals(this.alphabet, configuration.getAlphabet());

		Assert.assertTrue(configuration.addState(this.initialState));
		Assert.assertTrue(configuration.addState(this.finalState));
		Assert.assertFalse(configuration.addState(this.initialState));
		Assert.assertFalse(configuration.addState(this.finalState));
		Assert.assertEquals(2, configuration.getStates().size());
		Assert.assertTrue(configuration.getStates().contains(this.initialState));
		Assert.assertTrue(configuration.getStates().contains(this.finalState));

		Assert.assertTrue(configuration.addTape(this.inputTape));
		Assert.assertTrue(configuration.addTape(this.outputTape));
		Assert.assertFalse(configuration.addTape(this.inputTape));
		Assert.assertFalse(configuration.addTape(this.outputTape));
		Assert.assertEquals(2, configuration.getTapes().size());
		Assert.assertTrue(configuration.getTapes().contains(this.inputTape));
		Assert.assertTrue(configuration.getTapes().contains(this.outputTape));

		Assert.assertTrue(configuration.addTransition(this.zeroTransition));
		Assert.assertTrue(configuration.addTransition(this.plusTransition));
		Assert.assertTrue(configuration.addTransition(this.blankTransition));
		Assert.assertFalse(configuration.addTransition(this.zeroTransition));
		Assert.assertFalse(configuration.addTransition(this.plusTransition));
		Assert.assertFalse(configuration.addTransition(this.blankTransition));
		Assert.assertEquals(3, configuration.getTransitions(this.initialState).size());
		Assert.assertTrue(configuration.getTransitions(this.initialState).contains(this.zeroTransition));
		Assert.assertTrue(configuration.getTransitions(this.initialState).contains(this.plusTransition));
		Assert.assertTrue(configuration.getTransitions(this.initialState).contains(this.blankTransition));

		Assert.assertEquals(this.initialState, configuration.getInitialState());
		Assert.assertEquals(this.inputTape, configuration.getInputTape());
		Assert.assertEquals(this.outputTape, configuration.getOutputTape());

		Assert.assertTrue(configuration.removeState(this.initialState));
		Assert.assertTrue(configuration.removeState(this.finalState));
		Assert.assertFalse(configuration.removeState(this.initialState));
		Assert.assertFalse(configuration.removeState(this.finalState));
		Assert.assertEquals(0, configuration.getStates().size());
		Assert.assertFalse(configuration.getStates().contains(this.initialState));
		Assert.assertFalse(configuration.getStates().contains(this.finalState));

		Assert.assertTrue(configuration.removeTape(this.inputTape));
		Assert.assertTrue(configuration.removeTape(this.outputTape));
		Assert.assertFalse(configuration.removeTape(this.inputTape));
		Assert.assertFalse(configuration.removeTape(this.outputTape));
		Assert.assertEquals(0, configuration.getTapes().size());
		Assert.assertFalse(configuration.getTapes().contains(this.inputTape));
		Assert.assertFalse(configuration.getTapes().contains(this.outputTape));

		Assert.assertTrue(configuration.removeTransition(this.zeroTransition));
		Assert.assertTrue(configuration.removeTransition(this.plusTransition));
		Assert.assertTrue(configuration.removeTransition(this.blankTransition));
		Assert.assertFalse(configuration.removeTransition(this.zeroTransition));
		Assert.assertFalse(configuration.removeTransition(this.plusTransition));
		Assert.assertFalse(configuration.removeTransition(this.blankTransition));
		Assert.assertEquals(0, configuration.getTransitions(this.initialState).size());
		Assert.assertFalse(configuration.getTransitions(this.initialState).contains(this.zeroTransition));
		Assert.assertFalse(configuration.getTransitions(this.initialState).contains(this.plusTransition));
		Assert.assertFalse(configuration.getTransitions(this.initialState).contains(this.blankTransition));

	}
}
