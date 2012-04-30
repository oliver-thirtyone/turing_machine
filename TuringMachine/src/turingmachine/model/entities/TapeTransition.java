package turingmachine.model.entities;

public class TapeTransition {

	private final Tape tape;
	private final Character read;
	private final Character write;
	private final TapeMotion motion;

	public TapeTransition(Tape tape, Character read, Character write, TapeMotion motion) {
		this.tape = tape;
		this.read = read;
		this.write = write;
		this.motion = motion;
	}

	public Tape getTape() {
		return tape;
	}

	public Character getRead() {
		return read;
	}

	public Character getWrite() {
		return write;
	}

	public TapeMotion getMotion() {
		return motion;
	}

}
