package turingmachine.model.entities;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Transition {

	private final State currentState;
	private final State nextState;

	private final Map<Tape, TapeTransition> tapeTransitions;

	private String token;

	public Transition(State currentState, State nextState) {
		this.currentState = currentState;
		this.nextState = nextState;

		this.tapeTransitions = new LinkedHashMap<Tape, TapeTransition>();

		this.token = "";
	}

	public String getToken() {
		return this.token;
	}

	public State getCurrentState() {
		return this.currentState;
	}

	public State getNextState() {
		return this.nextState;
	}

	public Collection<TapeTransition> getTapeTransitions() {
		return this.tapeTransitions.values();
	}

	public Boolean addTapeTransition(TapeTransition tapeTransition) {
		if (this.tapeTransitions.containsKey(tapeTransition.getTape())) {
			return Boolean.FALSE;
		}

		this.tapeTransitions.put(tapeTransition.getTape(), tapeTransition);
		this.renewToken();
		return Boolean.TRUE;
	}

	public Boolean removeTapeTransition(TapeTransition tapeTransition) {
		if (!this.tapeTransitions.containsKey(tapeTransition.getTape())) {
			return Boolean.FALSE;
		}

		this.tapeTransitions.remove(tapeTransition.getTape());
		this.renewToken();
		return Boolean.TRUE;
	}

	public static String getToken(Map<String, Character> tapeReads) {
		StringBuilder stringBuilder = new StringBuilder();

		for (String tapeName : tapeReads.keySet()) {
			stringBuilder.append(tapeName);
			stringBuilder.append(':');
			stringBuilder.append(tapeReads.get(tapeName));

			stringBuilder.append('|');
		}

		if (stringBuilder.length() > 0) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}

		return stringBuilder.toString();
	}

	private synchronized void renewToken() {
		Map<String, Character> tapeReads = new LinkedHashMap<String, Character>();

		for (TapeTransition tapeTransition : this.getTapeTransitions()) {
			tapeReads.put(tapeTransition.getTape().getName(), tapeTransition.getRead());
		}

		this.token = Transition.getToken(tapeReads);
	}
}
