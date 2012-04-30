/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.model.entities;

public enum TapeMotion {

	L("LEFT"), R("RIGHT"), S("STAY");

	private final String motion;

	private TapeMotion(String motion) {
		this.motion = motion;
	}

	public String getMotion() {
		return motion;
	}

}
