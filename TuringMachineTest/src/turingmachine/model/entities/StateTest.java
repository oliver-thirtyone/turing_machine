package turingmachine.model.entities;

import junit.framework.Assert;

import org.junit.Test;

import turingmachine.model.entities.State;

public class StateTest {

	@Test
	public void testState() {
		State state0 = new State("Q0");
		Assert.assertEquals("Q0", state0.getName());
		Assert.assertEquals(new Boolean(false), state0.isFinal());

		State state1 = new State("Q1", false);
		Assert.assertEquals("Q1", state1.getName());
		Assert.assertEquals(new Boolean(false), state1.isFinal());

		State state2 = new State("Q2", true);
		Assert.assertEquals("Q2", state2.getName());
		Assert.assertEquals(new Boolean(true), state2.isFinal());
	}

}
