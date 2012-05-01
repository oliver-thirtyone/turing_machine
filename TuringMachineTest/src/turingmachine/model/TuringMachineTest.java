/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.model;

import java.util.Observable;
import java.util.Observer;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import turingmachine.model.entities.Alphabet;
import turingmachine.model.entities.State;
import turingmachine.model.entities.Tape;
import turingmachine.model.entities.TapeMotion;
import turingmachine.model.entities.TapeTransition;
import turingmachine.model.entities.Transition;

public class TuringMachineTest {

	private ITuringMachine turingMachine;

	private Alphabet alphabet;

	private State initialState;
	private State finalState;

	private Tape inputTape;
	private Tape outputTape;

	private Transition zeroTransition;
	private Transition plusTransition;
	private Transition blankTransition;

	private Observable observableState;
	private Observable observableTransition;
	private Observable observableTransitionCount;

	@Mock
	private Observer stateObserverMock;
	@Mock
	private Observer transitionObserverMock;
	@Mock
	private Observer transitionCountObserverMock;
	@Mock
	private Observer inputTapeObserverMock;
	@Mock
	private Observer outputTapeObserverMock;

	@Before
	public void setUp() {
		this.alphabet = new Alphabet();
		this.alphabet.addSymbol('B', null);
		this.alphabet.addSymbol('0');
		this.alphabet.addSymbol('+');

		TuringMachine turingMachine = new TuringMachine("TuringMachineTest", this.alphabet, "initialState", "inputTape", "outputTape");

		this.initialState = new State("initialState");
		turingMachine.addState(this.initialState);
		this.finalState = new State("finalState", true);
		turingMachine.addState(this.finalState);

		this.inputTape = new Tape("inputTape");
		turingMachine.addTape(this.inputTape);
		this.outputTape = new Tape("outputTape");
		turingMachine.addTape(this.outputTape);

		this.zeroTransition = new Transition(turingMachine.getState("initialState"), turingMachine.getState("initialState"));
		this.zeroTransition.addTapeTransition(new TapeTransition(turingMachine.getTape("inputTape"), this.alphabet.getHiddenCharacter('0'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("R")));
		this.zeroTransition.addTapeTransition(new TapeTransition(turingMachine.getTape("outputTape"), this.alphabet.getHiddenCharacter('B'), this.alphabet.getHiddenCharacter('0'), TapeMotion.valueOf("R")));
		turingMachine.addTransition(this.zeroTransition);

		this.plusTransition = new Transition(turingMachine.getState("initialState"), turingMachine.getState("initialState"));
		this.plusTransition.addTapeTransition(new TapeTransition(turingMachine.getTape("inputTape"), this.alphabet.getHiddenCharacter('+'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("R")));
		this.plusTransition.addTapeTransition(new TapeTransition(turingMachine.getTape("outputTape"), this.alphabet.getHiddenCharacter('B'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("S")));
		turingMachine.addTransition(this.plusTransition);

		this.blankTransition = new Transition(turingMachine.getState("initialState"), turingMachine.getState("finalState"));
		this.blankTransition.addTapeTransition(new TapeTransition(turingMachine.getTape("inputTape"), this.alphabet.getHiddenCharacter('B'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("S")));
		this.blankTransition.addTapeTransition(new TapeTransition(turingMachine.getTape("outputTape"), this.alphabet.getHiddenCharacter('B'), this.alphabet.getHiddenCharacter('B'), TapeMotion.valueOf("S")));
		turingMachine.addTransition(this.blankTransition);

		MockitoAnnotations.initMocks(this);

		this.observableState = turingMachine.addStateOberserver(this.stateObserverMock);
		this.observableTransition = turingMachine.addTransitionOberserver(this.transitionObserverMock);
		this.observableTransitionCount = turingMachine.addTransitionCountOberserver(this.transitionCountObserverMock);

		this.inputTape.addObserver(this.inputTapeObserverMock);
		this.outputTape.addObserver(this.outputTapeObserverMock);

		this.turingMachine = turingMachine;
	}

	@Test
	public void testTuringMachine() {
		Assert.assertEquals("inputTape:" + this.alphabet.getHiddenCharacter('0') + "|outputTape:" + this.alphabet.getHiddenCharacter('B') + "", this.zeroTransition.getToken());
		Assert.assertEquals("inputTape:" + this.alphabet.getHiddenCharacter('+') + "|outputTape:" + this.alphabet.getHiddenCharacter('B') + "", this.plusTransition.getToken());
		Assert.assertEquals("inputTape:" + this.alphabet.getHiddenCharacter('B') + "|outputTape:" + this.alphabet.getHiddenCharacter('B') + "", this.blankTransition.getToken());

		// /////////////////////////////////////////////////////////////////////////////

		this.turingMachine.reset();

		Assert.assertEquals(this.initialState, this.turingMachine.getCurrentState());
		Assert.assertEquals(null, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(0), this.turingMachine.getTransitionCount());

		Mockito.verify(this.stateObserverMock).update(this.observableState, this.initialState);
		Mockito.verifyZeroInteractions(this.transitionObserverMock);
		Mockito.verify(this.transitionCountObserverMock).update(this.observableTransitionCount, new Integer(0));
		Mockito.reset(this.stateObserverMock, this.transitionObserverMock, this.transitionCountObserverMock);

		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Mockito.verifyZeroInteractions(this.inputTapeObserverMock);
		Mockito.verifyZeroInteractions(this.outputTapeObserverMock);
		Mockito.reset(this.inputTapeObserverMock, this.outputTapeObserverMock);

		// /////////////////////////////////////////////////////////////////////////////

		this.turingMachine.setInput("0+00");

		Assert.assertEquals(this.initialState, this.turingMachine.getCurrentState());
		Assert.assertEquals(null, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(0), this.turingMachine.getTransitionCount());

		Mockito.verifyZeroInteractions(this.stateObserverMock);
		Mockito.verifyZeroInteractions(this.transitionObserverMock);
		Mockito.verifyZeroInteractions(this.transitionCountObserverMock);
		Mockito.reset(this.stateObserverMock, this.transitionObserverMock, this.transitionCountObserverMock);

		Assert.assertEquals(this.alphabet.getHiddenCharacter('0'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Mockito.verify(this.inputTapeObserverMock).update(this.inputTape, null);
		Mockito.verifyZeroInteractions(this.outputTapeObserverMock);
		Mockito.reset(this.inputTapeObserverMock, this.outputTapeObserverMock);

		// /////////////////////////////////////////////////////////////////////////////

		Assert.assertTrue(this.turingMachine.transition());

		Assert.assertEquals(this.initialState, this.turingMachine.getCurrentState());
		Assert.assertEquals(this.zeroTransition, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(1), this.turingMachine.getTransitionCount());

		Mockito.verifyZeroInteractions(this.stateObserverMock);
		Mockito.verify(this.transitionObserverMock).update(this.observableTransition, this.zeroTransition);
		Mockito.verify(this.transitionCountObserverMock).update(this.observableTransitionCount, new Integer(1));
		Mockito.reset(this.stateObserverMock, this.transitionObserverMock, this.transitionCountObserverMock);

		Assert.assertEquals(this.alphabet.getHiddenCharacter('+'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Mockito.verify(this.inputTapeObserverMock).update(this.inputTape, null);
		Mockito.verify(this.outputTapeObserverMock).update(this.outputTape, null);
		Mockito.reset(this.inputTapeObserverMock, this.outputTapeObserverMock);

		// /////////////////////////////////////////////////////////////////////////////

		Assert.assertTrue(this.turingMachine.transition());

		Assert.assertEquals(this.initialState, this.turingMachine.getCurrentState());
		Assert.assertEquals(this.plusTransition, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(2), this.turingMachine.getTransitionCount());

		Mockito.verifyZeroInteractions(this.stateObserverMock);
		Mockito.verify(this.transitionObserverMock).update(this.observableTransition, this.plusTransition);
		Mockito.verify(this.transitionCountObserverMock).update(this.observableTransitionCount, new Integer(2));
		Mockito.reset(this.stateObserverMock, this.transitionObserverMock, this.transitionCountObserverMock);

		Assert.assertEquals(this.alphabet.getHiddenCharacter('0'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Mockito.verify(this.inputTapeObserverMock).update(this.inputTape, null);
		Mockito.verifyZeroInteractions(this.outputTapeObserverMock);
		Mockito.reset(this.inputTapeObserverMock, this.outputTapeObserverMock);

		// /////////////////////////////////////////////////////////////////////////////

		Assert.assertTrue(this.turingMachine.transition());

		Assert.assertEquals(this.initialState, this.turingMachine.getCurrentState());
		Assert.assertEquals(this.zeroTransition, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(3), this.turingMachine.getTransitionCount());

		Mockito.verifyZeroInteractions(this.stateObserverMock);
		Mockito.verify(this.transitionObserverMock).update(this.observableTransition, this.zeroTransition);
		Mockito.verify(this.transitionCountObserverMock).update(this.observableTransitionCount, new Integer(3));
		Mockito.reset(this.stateObserverMock, this.transitionObserverMock, this.transitionCountObserverMock);

		Assert.assertEquals(this.alphabet.getHiddenCharacter('0'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Mockito.verify(this.inputTapeObserverMock).update(this.inputTape, null);
		Mockito.verify(this.outputTapeObserverMock).update(this.outputTape, null);
		Mockito.reset(this.inputTapeObserverMock, this.outputTapeObserverMock);

		// /////////////////////////////////////////////////////////////////////////////

		Assert.assertTrue(this.turingMachine.transition());

		Assert.assertEquals(this.initialState, this.turingMachine.getCurrentState());
		Assert.assertEquals(this.zeroTransition, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(4), this.turingMachine.getTransitionCount());

		Mockito.verifyZeroInteractions(this.stateObserverMock);
		Mockito.verifyZeroInteractions(this.transitionObserverMock);
		Mockito.verify(this.transitionCountObserverMock).update(this.observableTransitionCount, new Integer(4));
		Mockito.reset(this.stateObserverMock, this.transitionObserverMock, this.transitionCountObserverMock);

		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Mockito.verify(this.inputTapeObserverMock).update(this.inputTape, null);
		Mockito.verify(this.outputTapeObserverMock).update(this.outputTape, null);
		Mockito.reset(this.inputTapeObserverMock, this.outputTapeObserverMock);

		// /////////////////////////////////////////////////////////////////////////////

		Assert.assertTrue(this.turingMachine.transition());

		Assert.assertEquals(this.finalState, this.turingMachine.getCurrentState());
		Assert.assertEquals(this.blankTransition, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(5), this.turingMachine.getTransitionCount());

		Mockito.verify(this.stateObserverMock).update(this.observableState, this.finalState);
		Mockito.verify(this.transitionObserverMock).update(this.observableTransition, this.blankTransition);
		Mockito.verify(this.transitionCountObserverMock).update(this.observableTransitionCount, new Integer(5));
		Mockito.reset(this.stateObserverMock, this.transitionObserverMock, this.transitionCountObserverMock);

		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Mockito.verifyZeroInteractions(this.inputTapeObserverMock);
		Mockito.verifyZeroInteractions(this.outputTapeObserverMock);
		Mockito.reset(this.inputTapeObserverMock, this.outputTapeObserverMock);

		// /////////////////////////////////////////////////////////////////////////////

		Assert.assertFalse(this.turingMachine.transition());

		Assert.assertEquals(this.finalState, this.turingMachine.getCurrentState());
		Assert.assertEquals(this.blankTransition, this.turingMachine.getCurrentTransition());
		Assert.assertEquals(new Integer(5), this.turingMachine.getTransitionCount());

		Mockito.verifyZeroInteractions(this.stateObserverMock);
		Mockito.verifyZeroInteractions(this.transitionObserverMock);
		Mockito.verifyZeroInteractions(this.transitionCountObserverMock);
		Mockito.reset(this.stateObserverMock, this.transitionObserverMock, this.transitionCountObserverMock);

		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.inputTape.read());
		Assert.assertEquals(this.alphabet.getHiddenCharacter('B'), this.outputTape.read());

		Mockito.verifyZeroInteractions(this.inputTapeObserverMock);
		Mockito.verifyZeroInteractions(this.outputTapeObserverMock);
		Mockito.reset(this.inputTapeObserverMock, this.outputTapeObserverMock);

		// /////////////////////////////////////////////////////////////////////////////

		Assert.assertTrue(this.turingMachine.getCurrentState().isFinal());
		Assert.assertEquals("000", this.turingMachine.getOutput());
	}
}
