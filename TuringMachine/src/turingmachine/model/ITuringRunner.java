/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.model;

public interface ITuringRunner extends Runnable {

	public final static Integer PAUSE_SLEEP_MILLISECONDS = 100;

	public Boolean reset();

	public Boolean init(ITuringMachine turingMachine, String input);

	public Boolean init(ITuringMachine turingMachine, String input, Integer transitionDelay);

	public Boolean start();

	public Boolean step();

	public Boolean pause();

	public Boolean stop();

	public TuringRunnerMode getMode();

}