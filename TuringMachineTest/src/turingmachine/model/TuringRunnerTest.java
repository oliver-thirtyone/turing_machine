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

public class TuringRunnerTest {

	private ITuringRunner turingRunner;
	private Observable observableTuringRunner;

	@Mock
	private ITuringMachine turingMachineMock;

	@Mock
	private Observer turingRunnerObserverMock;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		TuringRunner turingRunner = new TuringRunner();
		turingRunner.addObserver(this.turingRunnerObserverMock);

		this.turingRunner = turingRunner;
		this.observableTuringRunner = turingRunner;

		Mockito.when(this.turingMachineMock.getName()).thenReturn("TuringMachineMock");
		Mockito.when(this.turingMachineMock.getOutput()).thenReturn("dummy output");
		Mockito.when(this.turingMachineMock.getCurrentState()).thenReturn(null);
		Mockito.when(this.turingMachineMock.getCurrentTransition()).thenReturn(null);
		Mockito.when(this.turingMachineMock.getTransitionCount()).thenReturn(null);
		Mockito.when(this.turingMachineMock.transition()).thenReturn(Boolean.FALSE);
	}

	@Test
	public void testTuringRunner() {

		Assert.assertFalse(this.turingRunner.reset());
		Assert.assertFalse(this.turingRunner.start());
		Assert.assertFalse(this.turingRunner.step());
		Assert.assertFalse(this.turingRunner.pause());
		Assert.assertFalse(this.turingRunner.stop());
		Assert.assertEquals(TuringRunnerMode.NOT_READY, this.turingRunner.getMode());

		Mockito.verifyZeroInteractions(this.turingRunnerObserverMock);
		Mockito.reset(this.turingRunnerObserverMock);

		// /////////////////////////////////////////////////////////////////////////////

		Assert.assertTrue(this.turingRunner.init(this.turingMachineMock, "dummy input"));

		Mockito.verify(this.turingRunnerObserverMock).update(this.observableTuringRunner, TuringRunnerMode.READY);
		Mockito.reset(this.turingRunnerObserverMock);

		Assert.assertFalse(this.turingRunner.init(this.turingMachineMock, "dummy input"));
		Assert.assertFalse(this.turingRunner.pause());
		Assert.assertFalse(this.turingRunner.stop());
		Assert.assertEquals(TuringRunnerMode.READY, this.turingRunner.getMode());

		// /////////////////////////////////////////////////////////////////////////////

		// TODO: further testing...

	}
}