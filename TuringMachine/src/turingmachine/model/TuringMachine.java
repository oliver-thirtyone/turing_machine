package turingmachine.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import turingmachine.model.entities.State;
import turingmachine.model.entities.Tape;
import turingmachine.model.entities.TapeMotion;
import turingmachine.model.entities.TapeTransition;
import turingmachine.model.entities.Transition;
import turingmachine.model.entities.TuringConfiguration;

public class TuringMachine extends Observable implements ITuringMachine {

	private final TuringConfiguration turingConfiguration;

	private State currentState;
	private Transition currentTransition;
	private Integer transitionCount;

	public TuringMachine(TuringConfiguration turingConfiguration) {
		this.turingConfiguration = turingConfiguration;
		this.reset();
	}

	@Override
	public synchronized void reset() {
		this.currentState = this.getConfiguration().getInitialState();
		this.currentTransition = null;
		this.transitionCount = 0;

		for (Tape tape : this.getConfiguration().getTapes()) {
			tape.reset();
		}
	}

	@Override
	public synchronized Boolean transition() {
		Boolean successful = Boolean.FALSE;
		String token = this.getToken();

		Collection<Transition> transitions = this.getConfiguration().getTransitions(this.getCurrentState());
		for (Transition transition : transitions) {
			if (transition.getToken().equals(token)) {
				System.out.println("-> " + transition.getToken() + " = " + token);
				this.setCurrentTransition(transition);

				Collection<TapeTransition> tapeTransitions = transition.getTapeTransitions();
				for (TapeTransition tapeTransition : tapeTransitions) {
					Tape tape = tapeTransition.getTape();

					// TODO: Remove paranoid checks?
					if (!this.getConfiguration().getTapes().contains(tape)) {
						throw new RuntimeException("Something went terribly wrong. This tape is not part of this Turing Machine.");
					}
					if ((tapeTransition.getRead() == null && tape.read() != null) || (tapeTransition.getRead() != null && !tapeTransition.getRead().equals(tape.read()))) {
						throw new RuntimeException("Something went terribly wrong. The token of this transition is wrong.");
					}

					tape.write(tapeTransition.getWrite());
					tape.moveCursor(tapeTransition.getMotion());
					tape.notifyObservers();
				}

				this.setCurrentState(transition.getNextState());
				this.increaseTransitionCount();
				this.notifyObservers();

				successful = Boolean.TRUE;
				break;
			}
		}

		return successful;
	}

	@Override
	public State getCurrentState() {
		return this.currentState;
	}

	public void setCurrentState(State state) {
		this.currentState = state;
		this.setChanged();
	}

	@Override
	public Transition getCurrentTransition() {
		return this.currentTransition;
	}

	public void setCurrentTransition(Transition currentTransition) {
		this.currentTransition = currentTransition;
		this.setChanged();
	}

	@Override
	public Integer getTransitionCount() {
		return this.transitionCount;
	}

	public void increaseTransitionCount() {
		this.transitionCount++;
		this.setChanged();
	}

	public TuringConfiguration getConfiguration() {
		return this.turingConfiguration;
	}

	@Override
	public void setInput(String input) {
		char[] characters = input.toCharArray();
		Tape tape = this.getConfiguration().getInputTape();

		// Add all characters to the right side of the tape
		for (int i = characters.length; i > 0; i--) {
			tape.write(characters[i - 1]);
			tape.moveCursor(TapeMotion.L);
		}

		// Select the first character
		tape.moveCursor(TapeMotion.R);
	}

	@Override
	public String getOutput() {
		StringBuilder stringBuilder = new StringBuilder();

		Tape tape = this.getConfiguration().getOutputTape();
		List<Character> characters = tape.getTape();

		for (Character character : characters) {
			if (character != null) {
				stringBuilder.append(character);
			}
		}

		return stringBuilder.toString();
	}

	private synchronized String getToken() {
		Map<String, Character> tapeReads = new LinkedHashMap<String, Character>();

		for (Tape tape : this.getConfiguration().getTapes()) {
			tapeReads.put(tape.getName(), tape.read());
		}

		return Transition.getToken(tapeReads);
	}

}
