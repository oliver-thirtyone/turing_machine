package turingmachine.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import turingmachine.model.entities.State;
import turingmachine.model.entities.Tape;
import turingmachine.model.entities.TapeMotion;
import turingmachine.model.entities.TapeTransition;
import turingmachine.model.entities.Transition;
import turingmachine.util.ObservableObject;

public class TuringMachine extends AbstractTuringMachine {

	private final ObservableObject<State> currentState;
	private final ObservableObject<Transition> currentTransition;
	private final ObservableObject<Integer> transitionCount;

	public TuringMachine() {
		this.currentState = new ObservableObject<State>();
		this.currentTransition = new ObservableObject<Transition>();
		this.transitionCount = new ObservableObject<Integer>();
	}

	@Override
	public synchronized Boolean doReset() {
		this.setCurrentState(this.getInitialState());
		this.setCurrentTransition(null);
		this.setTransitionCount(0);

		for (Tape tape : this.getTapes()) {
			tape.reset();
		}

		return Boolean.TRUE;
	}

	@Override
	public synchronized Boolean doTransition() {
		Boolean successful = Boolean.FALSE;
		String token = this.getToken();

		Collection<Transition> transitions = this.getTransitions(this.getCurrentState());
		for (Transition transition : transitions) {
			if (transition.getToken().equals(token)) {
				this.setCurrentTransition(transition);

				Collection<TapeTransition> tapeTransitions = transition.getTapeTransitions();
				for (TapeTransition tapeTransition : tapeTransitions) {
					Tape tape = tapeTransition.getTape();

					// TODO: Remove paranoid checks?
					if (!this.getTapes().contains(tape)) {
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

				successful = Boolean.TRUE;
				break;
			}
		}

		return successful;
	}

	@Override
	public State getCurrentState() {
		return this.currentState.get();
	}

	private void setCurrentState(State state) {
		this.currentState.set(state);
		this.currentState.notifyObservers();
	}

	@Override
	public Transition getCurrentTransition() {
		return this.currentTransition.get();
	}

	private void setCurrentTransition(Transition transition) {
		this.currentTransition.set(transition);
		this.currentTransition.notifyObservers();
	}

	@Override
	public Integer getTransitionCount() {
		return this.transitionCount.get();
	}

	private void setTransitionCount(Integer transitionCount) {
		this.transitionCount.set(transitionCount);
		this.transitionCount.notifyObservers();
	}

	private void increaseTransitionCount() {
		this.setTransitionCount(this.getTransitionCount() + 1);
	}

	@Override
	public void setInput(String input) {
		char[] characters = input.toCharArray();
		Tape tape = this.getInputTape();

		// Add all characters to the right side of the tape
		for (int i = characters.length; i > 0; i--) {
			tape.write(characters[i - 1]);
			tape.moveCursor(TapeMotion.L);
		}

		// Select the first character
		tape.moveCursor(TapeMotion.R);
		tape.notifyObservers();
	}

	@Override
	public String getOutput() {
		StringBuilder stringBuilder = new StringBuilder();

		Tape tape = this.getOutputTape();
		List<Character> characters = tape.getTape();

		for (Character character : characters) {
			if (character != null) {
				stringBuilder.append(character);
			}
		}

		return stringBuilder.toString();
	}

	public Observable addStateOberserver(Observer observer) {
		this.currentState.addObserver(observer);
		return this.currentState;
	}

	public void deleteStateOberserver(Observer observer) {
		this.currentState.deleteObserver(observer);
	}

	public Observable addTransitionOberserver(Observer observer) {
		this.currentTransition.addObserver(observer);
		return this.currentTransition;
	}

	public void deleteTransitionOberserver(Observer observer) {
		this.currentTransition.deleteObserver(observer);
	}

	public Observable addTransitionCountOberserver(Observer observer) {
		this.transitionCount.addObserver(observer);
		return this.transitionCount;
	}

	public void deleteTransitionCountOberserver(Observer observer) {
		this.transitionCount.deleteObserver(observer);
	}

	private synchronized String getToken() {
		Map<String, Character> tapeReads = new LinkedHashMap<String, Character>();

		for (Tape tape : this.getTapes()) {
			tapeReads.put(tape.getName(), tape.read());
		}

		return Transition.getToken(tapeReads);
	}

}
