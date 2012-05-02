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
		return this.tape;
	}

	public Character getRead() {
		return this.read;
	}

	public Character getWrite() {
		return this.write;
	}

	public TapeMotion getMotion() {
		return this.motion;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((this.motion == null) ? 0 : this.motion.hashCode());
		result = 31 * result + ((this.read == null) ? 0 : this.read.hashCode());
		result = 31 * result + ((this.tape == null) ? 0 : this.tape.hashCode());
		result = 31 * result + ((this.write == null) ? 0 : this.write.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		TapeTransition other = (TapeTransition) obj;
		if (this.motion != other.motion) {
			return false;
		}
		if (this.read == null && other.read != null) {
			return false;
		}
		else if (!this.read.equals(other.read)) {
			return false;
		}
		if (this.tape == null && other.tape != null) {
			return false;
		}
		else if (!this.tape.equals(other.tape)) {
			return false;
		}
		if (this.write == null && other.write != null) {
			return false;
		}
		else if (!this.write.equals(other.write)) {
			return false;
		}
		return true;
	}

}
