/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.model.machine;

import java.util.Observable;
import java.util.Observer;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import turingmachine.model.ITuringMachine;
import turingmachine.model.TuringMachine;
import turingmachine.model.entities.Alphabet;
import turingmachine.model.entities.State;
import turingmachine.model.entities.Tape;
import turingmachine.model.entities.TapeMotion;
import turingmachine.model.entities.TapeTransition;
import turingmachine.model.entities.Transition;
import turingmachine.model.entities.TuringConfiguration;

public class TuringMachineTest implements Observer {

	private ITuringMachine turingMachine;
	private Observable observableTuringMachine;

	private Alphabet alphabet;

	private State initialState;
	private State finalState;

	private Tape inputTape;
	private Tape outputTape;

	private Transition zeroTransition;
	private Transition plusTransition;
	private Transition blankTransition;

	private Integer updateCounter;

	@Before
	public void setUp() {
		this.alphabet = new Alphabet();
		this.alphabet.addSymbol('B', null);
		this.alphabet.addSymbol('0');
		this.alphabet.addSymbol('+');

		TuringConfiguration configuration = new TuringConfiguration("TuringMachineTest", this.alphabet, "initialState", "inputTape", "outputTape");

		this.initialState = new State("initialState");
		configuration.addState(this.initialState);
		this.finalState = new State("finalState", true);
		configuration.addState(this.finalState);

		this.inputTape = new Tape("inputTape");
		configuration.addTape(this.inputTape);
		this.outputTape = new Tape("outputTape");
		configuration.addTape(this.outputTape);

		this.zeroTransition = new Transition(configuration.getState("initialState"), configuration.getState("initialState"));
		this.zeroTransition.addTapeTransition(new TapeTransition(configuration.getTape("inputTape"), this.alphabet.getHiddenCharacter('0'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("R")));
		this.zeroTransition.addTapeTransition(new TapeTransition(configuration.getTape("outputTape"), this.alphabet.getHiddenCharacter('B'), this.alphabet.getHiddenCharacter('0'), TapeMotion.valueOf("R")));
		configuration.addTransition(this.zeroTransition);

		this.plusTransition = new Transition(configuration.getState("initialState"), configuration.getState("initialState"));
		this.plusTransition.addTapeTransition(new TapeTransition(configuration.getTape("inputTape"), this.alphabet.getHiddenCharacter('+'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("R")));
		this.plusTransition.addTapeTransition(new TapeTransition(configuration.getTape("outputTape"), this.alphabet.getHiddenCharacter('B'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("S")));
		configuration.addTransition(this.plusTransition);

		this.blankTransition = new Transition(configuration.getState("initialState"), configuration.getState("finalState"));
		this.blankTransition.addTapeTransition(new TapeTransition(configuration.getTape("inputTape"), this.alphabet.getHiddenCharacter('B'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("S")));
		this.blankTransition.addTapeTransition(new TapeTransition(configuration.getTape("outputTape"), this.alphabet.getHiddenCharacter('B'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("S")));
		configuration.addTransition(this.blankTransition);

		TuringMachine turingMachine = new TuringMachine(configuration);
		this.turingMachine = turingMachine;

		this.observableTuringMachine = turingMachine;
		this.observableTuringMachine.addObserver(this);
		this.updateCounter = 0;
	}

	@Test
	public void testTuringMachine() {
		Assert.assertEquals("inputTape:" + this.alphabet.getHiddenCharacter('0') + "|outputTape:" + this.alphabet.getHiddenCharacter('B') + "", this.zeroTransition.getToken());
		Assert.assertEquals("inputTape:" + this.alphabet.getHiddenCharacter('+') + "|outputTape:" + this.alphabet.getHiddenCharacter('B') + "", this.plusTransition.getToken());
		Assert.assertEquals("inputTape:" + this.alphabet.getHiddenCharacter('B') + "|outputTape:" + this.alphabet.getHiddenCharacter('B') + "", this.blankTransition.getToken());

		Assert.assertEquals(this.initialState, this.turingMachine.getCurrentState());
		Assert.assertEquals(null, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(0), this.turingMachine.getTransitionCount());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		this.turingMachine.setInput("0+00");

		Assert.assertEquals(this.initialState, this.turingMachine.getCurrentState());
		Assert.assertEquals(null, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(0), this.turingMachine.getTransitionCount());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('0'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Assert.assertTrue(this.turingMachine.transition());

		Assert.assertEquals(this.initialState, this.turingMachine.getCurrentState());
		Assert.assertEquals(this.zeroTransition, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(1), this.turingMachine.getTransitionCount());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('+'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Assert.assertTrue(this.turingMachine.transition());

		Assert.assertEquals(this.initialState, this.turingMachine.getCurrentState());
		Assert.assertEquals(this.plusTransition, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(2), this.turingMachine.getTransitionCount());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('0'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Assert.assertTrue(this.turingMachine.transition());

		Assert.assertEquals(this.initialState, this.turingMachine.getCurrentState());
		Assert.assertEquals(this.zeroTransition, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(3), this.turingMachine.getTransitionCount());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('0'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Assert.assertTrue(this.turingMachine.transition());

		Assert.assertEquals(this.initialState, this.turingMachine.getCurrentState());
		Assert.assertEquals(this.zeroTransition, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(4), this.turingMachine.getTransitionCount());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Assert.assertTrue(this.turingMachine.transition());

		Assert.assertEquals(this.finalState, this.turingMachine.getCurrentState());
		Assert.assertEquals(this.blankTransition, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(5), this.turingMachine.getTransitionCount());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Assert.assertFalse(this.turingMachine.transition());

		Assert.assertEquals(this.finalState, this.turingMachine.getCurrentState());
		Assert.assertEquals(this.blankTransition, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(5), this.turingMachine.getTransitionCount());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Assert.assertTrue(this.turingMachine.getCurrentState().isFinal());
		Assert.assertEquals("000", this.turingMachine.getOutput());

		Assert.assertEquals(this.updateCounter, this.turingMachine.getTransitionCount());
	}

	@Override
	public void update(Observable observable, Object argument) {
		Assert.assertEquals(this.observableTuringMachine, observable);
		this.updateCounter++;
	}
}
