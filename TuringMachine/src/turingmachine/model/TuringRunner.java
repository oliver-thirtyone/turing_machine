/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.model;

import java.util.Observable;

import turingmachine.model.entities.Tape;
import turingmachine.model.entities.TapeMotion;
import turingmachine.model.entities.TuringRunnerMode;

public class TuringRunner extends Observable implements Runnable {

	private final TuringMachine turingMachine;
	private final Thread thread;

	private TuringRunnerMode mode;

	public TuringRunner(TuringMachine turingMachine) {
		this.turingMachine = turingMachine;
		this.thread = new Thread(this, turingMachine.getConfiguration().getName());

		this.mode = TuringRunnerMode.STOP;
	}

	public TuringRunnerMode getMode() {
		return this.mode;
	}

	public void setMode(TuringRunnerMode mode) {
		this.mode = mode;

		this.setChanged();
		this.notifyObservers(mode);
	}

	// TODO
	public Boolean start(String input) {
		if (this.getMode() != TuringRunnerMode.STOP) {
			return Boolean.FALSE;
		}

		this.turingMachine.reset();
		this.setInput(input);
		this.thread.start();

		return Boolean.TRUE;
	}

	@Override
	public void run() {
		while (this.getMode() != TuringRunnerMode.STOP) {
			switch (this.getMode()) {
				case RUN:
				case STEP:
					if (!this.turingMachine.transition()) {
						this.setMode(TuringRunnerMode.STOP);
					}
					break;
				case PAUSE:
					try {
						Thread.sleep(500);
					}
					catch (InterruptedException exception) {
						exception.printStackTrace();
					}
					break;
			}

			if (!this.turingMachine.transition()) {
				this.setMode(TuringRunnerMode.STOP);
				break;
			}
			else {
				// STEP -> PAUSE u.s.w....
			}

		}
	}

	private void setInput(String input) {
		char[] characters = input.toCharArray();
		Tape tape = this.turingMachine.getInputTape();

		// Add all characters to the right side of the tape
		for (int i = characters.length; i > 0; i--) {
			tape.write(characters[i - 1]);
			tape.moveCursor(TapeMotion.L);
		}

		// Select the first character
		tape.moveCursor(TapeMotion.R);
	}

}
