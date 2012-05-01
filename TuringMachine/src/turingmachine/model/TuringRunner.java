/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.model;

import java.util.Observable;

public class TuringRunner extends Observable implements Runnable, ITuringRunner {

	private TuringRunnerMode mode;

	private ITuringMachine turingMachine;
	private Integer transitionDelay;

	private Thread thread;

	public TuringRunner() {
		this.mode = TuringRunnerMode.NOT_READY;
	}

	@Override
	public void run() {
		try {
			while (this.getMode() == TuringRunnerMode.RUN || this.getMode() == TuringRunnerMode.STEP || this.getMode() == TuringRunnerMode.PAUSE) {
				// Sleep in PAUSE-Mode
				if (this.getMode() == TuringRunnerMode.PAUSE) {
					Thread.sleep(ITuringRunner.PAUSE_SLEEP_MILLISECONDS);
					continue;
				}

				// Sleep between the transitions to create delay
				if (this.getTransitionDelay() != null) {
					Thread.sleep(this.getTransitionDelay());
				}

				// Transition
				if (!this.getTuringMachine().transition()) {
					this.setMode(TuringRunnerMode.STOP);
					break;
				}

				// Switch to pause if we run in STEP-Mode
				if (this.getMode() == TuringRunnerMode.STEP) {
					this.setMode(TuringRunnerMode.PAUSE);
				}
			}
		}
		catch (InterruptedException exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public synchronized Boolean reset() {
		Boolean successful = Boolean.FALSE;

		switch (this.getMode()) {
			case READY:
			case STOP:
				this.getTuringMachine().reset();
				this.setTuringMachine(null);
				this.setTransitionDelay(null);

				this.thread = null;

				this.setMode(TuringRunnerMode.NOT_READY);
				successful = Boolean.TRUE;
				break;
		}

		return successful;
	}

	@Override
	public synchronized Boolean init(ITuringMachine turingMachine, String input) {
		return this.init(turingMachine, input, null);
	}

	@Override
	public synchronized Boolean init(ITuringMachine turingMachine, String input, Integer transitionDelay) {
		Boolean successful = Boolean.FALSE;

		switch (this.getMode()) {
			case NOT_READY:
				this.setTuringMachine(turingMachine);
				this.getTuringMachine().reset();
				this.getTuringMachine().setInput(input);
				this.setTransitionDelay(transitionDelay);

				this.thread = new Thread(this, this.getTuringMachine().getName());

				this.setMode(TuringRunnerMode.READY);
				successful = Boolean.TRUE;
				break;
		}

		return successful;
	}

	@Override
	public synchronized Boolean start() {
		Boolean successful = Boolean.FALSE;

		switch (this.getMode()) {
			case READY:
				if (!this.thread.isAlive()) {
					this.thread.start();
				}
			case STEP:
			case PAUSE:
				this.setMode(TuringRunnerMode.RUN);
				successful = Boolean.TRUE;
				break;
		}

		return successful;
	}

	@Override
	public synchronized Boolean step() {
		Boolean successful = Boolean.FALSE;

		switch (this.getMode()) {
			case READY:
				if (!this.thread.isAlive()) {
					this.thread.start();
				}
			case RUN:
			case PAUSE:
				this.setMode(TuringRunnerMode.STEP);
				successful = Boolean.TRUE;
				break;
		}

		return successful;
	}

	@Override
	public synchronized Boolean pause() {
		Boolean successful = Boolean.FALSE;

		switch (this.getMode()) {
			case RUN:
			case STEP:
				this.setMode(TuringRunnerMode.PAUSE);
				successful = Boolean.TRUE;
				break;
		}

		return successful;
	}

	@Override
	public synchronized Boolean stop() {
		Boolean successful = Boolean.FALSE;

		switch (this.getMode()) {
			case RUN:
			case STEP:
			case PAUSE:
				this.setMode(TuringRunnerMode.STOP);
				successful = Boolean.TRUE;
				break;
		}

		return successful;
	}

	@Override
	public synchronized TuringRunnerMode getMode() {
		return this.mode;
	}

	private synchronized void setMode(TuringRunnerMode mode) {
		if (this.mode == mode) {
			return;
		}

		this.mode = mode;
		this.setChanged();
		this.notifyObservers(this.mode);
	}

	private synchronized ITuringMachine getTuringMachine() {
		return this.turingMachine;
	}

	private synchronized void setTuringMachine(ITuringMachine turingMachine) {
		this.turingMachine = turingMachine;
	}

	private synchronized Integer getTransitionDelay() {
		return this.transitionDelay;
	}

	private synchronized void setTransitionDelay(Integer transitionDelay) {
		this.transitionDelay = transitionDelay;
	}

}
