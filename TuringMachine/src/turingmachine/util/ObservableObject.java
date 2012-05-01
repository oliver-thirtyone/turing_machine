/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.util;

import java.util.Observable;

public class ObservableObject<O extends Object> extends Observable {

	private O object;

	public O get() {
		return this.object;
	}

	public void set(O object) {
		if (this.object == object) {
			return;
		}

		this.object = object;
		this.setChanged();
	}

	@Override
	public void notifyObservers() {
		super.notifyObservers(this.object);
	}
}
