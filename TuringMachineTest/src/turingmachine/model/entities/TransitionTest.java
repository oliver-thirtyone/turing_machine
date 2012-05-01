package turingmachine.model.entities;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import turingmachine.model.entities.State;
import turingmachine.model.entities.Tape;
import turingmachine.model.entities.TapeMotion;
import turingmachine.model.entities.TapeTransition;
import turingmachine.model.entities.Transition;

public class TransitionTest {

	private State state1;
	private State state2;

	private Tape tape1;
	private Tape tape2;

	private TapeTransition tapeTransition1;
	private TapeTransition tapeTransition2;

	private Transition transition;

	@Before
	public void setUp() {
		this.state1 = new State("state1");
		this.state2 = new State("state2");

		this.tape1 = new Tape("tape1");
		this.tape2 = new Tape("tape2");

		this.tapeTransition1 = new TapeTransition(this.tape1, '0', null, TapeMotion.R);
		this.tapeTransition2 = new TapeTransition(this.tape2, null, '0', TapeMotion.R);

		this.transition = new Transition(this.state1, this.state2);
		this.transition.addTapeTransition(this.tapeTransition1);
		this.transition.addTapeTransition(this.tapeTransition2);
	}

	@Test
	public void testTapeTransition() {
		Assert.assertEquals(this.tape1, this.tapeTransition1.getTape());
		Assert.assertEquals(new Character('0'), this.tapeTransition1.getRead());
		Assert.assertEquals(null, this.tapeTransition1.getWrite());
		Assert.assertEquals(TapeMotion.R, this.tapeTransition1.getMotion());

		Assert.assertEquals(this.tape2, this.tapeTransition2.getTape());
		Assert.assertEquals(null, this.tapeTransition2.getRead());
		Assert.assertEquals(new Character('0'), this.tapeTransition2.getWrite());
		Assert.assertEquals(TapeMotion.R, this.tapeTransition2.getMotion());
	}

	@Test
	public void testTransition() {
		Assert.assertEquals("tape1:0|tape2:null", this.transition.getToken());
		Assert.assertEquals(this.state1, this.transition.getCurrentState());
		Assert.assertEquals(this.state2, this.transition.getNextState());

		Assert.assertTrue(this.transition.getTapeTransitions().contains(this.tapeTransition1));
		Assert.assertTrue(this.transition.getTapeTransitions().contains(this.tapeTransition2));

		this.transition.removeTapeTransition(this.tapeTransition1);
		this.transition.removeTapeTransition(this.tapeTransition2);

		Assert.assertFalse(this.transition.getTapeTransitions().contains(this.tapeTransition1));
		Assert.assertFalse(this.transition.getTapeTransitions().contains(this.tapeTransition2));

	}

}
