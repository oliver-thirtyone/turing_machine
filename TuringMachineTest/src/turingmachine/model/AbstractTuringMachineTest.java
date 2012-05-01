/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.model;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import turingmachine.model.entities.Alphabet;
import turingmachine.model.entities.State;
import turingmachine.model.entities.Tape;
import turingmachine.model.entities.TapeMotion;
import turingmachine.model.entities.TapeTransition;
import turingmachine.model.entities.Transition;

public class AbstractTuringMachineTest {

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
	public void testAbstractTuringMachine() {
		AbstractTuringMachine turingMachine = new TuringMachine("TuringConfigurationTest", this.alphabet, "initialState", "inputTape", "outputTape");

		Assert.assertEquals(this.alphabet, turingMachine.getAlphabet());

		Assert.assertTrue(turingMachine.addState(this.initialState));
		Assert.assertTrue(turingMachine.addState(this.finalState));
		Assert.assertFalse(turingMachine.addState(this.initialState));
		Assert.assertFalse(turingMachine.addState(this.finalState));
		Assert.assertEquals(2, turingMachine.getStates().size());
		Assert.assertTrue(turingMachine.getStates().contains(this.initialState));
		Assert.assertTrue(turingMachine.getStates().contains(this.finalState));

		Assert.assertTrue(turingMachine.addTape(this.inputTape));
		Assert.assertTrue(turingMachine.addTape(this.outputTape));
		Assert.assertFalse(turingMachine.addTape(this.inputTape));
		Assert.assertFalse(turingMachine.addTape(this.outputTape));
		Assert.assertEquals(2, turingMachine.getTapes().size());
		Assert.assertTrue(turingMachine.getTapes().contains(this.inputTape));
		Assert.assertTrue(turingMachine.getTapes().contains(this.outputTape));

		Assert.assertTrue(turingMachine.addTransition(this.zeroTransition));
		Assert.assertTrue(turingMachine.addTransition(this.plusTransition));
		Assert.assertTrue(turingMachine.addTransition(this.blankTransition));
		Assert.assertFalse(turingMachine.addTransition(this.zeroTransition));
		Assert.assertFalse(turingMachine.addTransition(this.plusTransition));
		Assert.assertFalse(turingMachine.addTransition(this.blankTransition));
		Assert.assertEquals(3, turingMachine.getTransitions(this.initialState).size());
		Assert.assertTrue(turingMachine.getTransitions(this.initialState).contains(this.zeroTransition));
		Assert.assertTrue(turingMachine.getTransitions(this.initialState).contains(this.plusTransition));
		Assert.assertTrue(turingMachine.getTransitions(this.initialState).contains(this.blankTransition));

		Assert.assertEquals(this.initialState, turingMachine.getInitialState());
		Assert.assertEquals(this.inputTape, turingMachine.getInputTape());
		Assert.assertEquals(this.outputTape, turingMachine.getOutputTape());

		Assert.assertTrue(turingMachine.removeState(this.initialState));
		Assert.assertTrue(turingMachine.removeState(this.finalState));
		Assert.assertFalse(turingMachine.removeState(this.initialState));
		Assert.assertFalse(turingMachine.removeState(this.finalState));
		Assert.assertEquals(0, turingMachine.getStates().size());
		Assert.assertFalse(turingMachine.getStates().contains(this.initialState));
		Assert.assertFalse(turingMachine.getStates().contains(this.finalState));

		Assert.assertTrue(turingMachine.removeTape(this.inputTape));
		Assert.assertTrue(turingMachine.removeTape(this.outputTape));
		Assert.assertFalse(turingMachine.removeTape(this.inputTape));
		Assert.assertFalse(turingMachine.removeTape(this.outputTape));
		Assert.assertEquals(0, turingMachine.getTapes().size());
		Assert.assertFalse(turingMachine.getTapes().contains(this.inputTape));
		Assert.assertFalse(turingMachine.getTapes().contains(this.outputTape));

		Assert.assertTrue(turingMachine.removeTransition(this.zeroTransition));
		Assert.assertTrue(turingMachine.removeTransition(this.plusTransition));
		Assert.assertTrue(turingMachine.removeTransition(this.blankTransition));
		Assert.assertFalse(turingMachine.removeTransition(this.zeroTransition));
		Assert.assertFalse(turingMachine.removeTransition(this.plusTransition));
		Assert.assertFalse(turingMachine.removeTransition(this.blankTransition));
		Assert.assertEquals(0, turingMachine.getTransitions(this.initialState).size());
		Assert.assertFalse(turingMachine.getTransitions(this.initialState).contains(this.zeroTransition));
		Assert.assertFalse(turingMachine.getTransitions(this.initialState).contains(this.plusTransition));
		Assert.assertFalse(turingMachine.getTransitions(this.initialState).contains(this.blankTransition));

	}
}
