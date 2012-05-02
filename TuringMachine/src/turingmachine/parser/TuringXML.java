/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.parser;

public enum TuringXML {

	// @formatter:off
	TURINGMACHINE("turingMachine"),
	TURINGMACHINE_NAME("name"),

	STATES("states"),
	STATES_INITIAL("initial-state-name"),
	STATE("state"),
	STATE_NAME("name"),
	STATE_FINAL("final"),

	TAPES("tapes"),
	TAPES_INPUT("input-tape-name"),
	TAPES_OUTPUT("output-tape-name"),
	TAPE("tape"),
	TAPE_NAME("name"),

	ALPHABET("alphabet"),
	BLANKSYMBOL("blank-symbol"),
	SYMBOL("symbol"),
	SYMBOL_VALUE("value"),

	TRANSITIONS("transitions"),
	TRANSITION("transition"),
	TRANSITION_CURRENTSTATE("current-state"),
	TRANSITION_NEXTSTATE("next-state"),
	TAPETRANSITION("tape-transition"),
	TAPETRANSITION_TAPE("tape"),
	TAPETRANSITION_READSYMBOL("read-symbol"),
	TAPETRANSITION_WRITESYMBOL("write-symbol"),
	TAPETRANSITION_TAPEMOTION("tape-motion");
	// @formatter:on

	private final String value;

	private TuringXML(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
