package engine.imp.render;

import java.util.ArrayList;
import java.util.List;

import engine.core.asset.Asset;

/**
 * Contains the data for one Animation.
 */
public class Animation extends Asset {
	private List<Frame> m_frames;

	// time since last update
	private int m_currentFrame;

	private boolean m_repeat;
	private boolean m_isFirstFrame = true;

	/**
	 * Initializes an Animation with frames.
	 * 
	 * @param frames
	 */
	public Animation(List<Frame> frames) {
		if (frames == null)
			throw new AnimationException("Frames cannot be null!");
		m_frames = frames;
		m_currentFrame = 0;
		m_repeat = false;
	}

	/**
	 * Creates a clone of an Animation.
	 * 
	 * @param animation
	 */
	public Animation(Animation animation) {
		m_frames = new ArrayList<Frame>();
		for (Frame frame : animation.getFrames()) {
			m_frames.add(new Frame(frame));
		}

		m_currentFrame = 0;
		m_repeat = animation.isRepeating();
	}

	/**
	 * Sets whether this is the "starter" frame - not used for repeats, only set once
	 * 
	 * @param first
	 */
	protected void setFirstFrame(boolean first) {
		m_isFirstFrame = first;
	}

	/**
	 * @return whether this is the "starter" frame - not used for repeats, only set once
	 */
	protected boolean isFirstFrame() {
		return m_isFirstFrame;
	}

	/**
	 * @return the current frame
	 */
	public int getCurrentFrame() {
		return m_currentFrame;
	}

	/**
	 * Sets the current frame.
	 * 
	 * @param frame
	 */
	public void setCurrentFrame(int frame) {
		if (frame < 0)
			throw new AnimationException("Current frame must be greater than or equal to zero!");
		m_currentFrame = frame;
	}

	/**
	 * @return the current Frame in the Animation
	 */
	public Frame currentFrame() {
		return m_frames.get(m_currentFrame);
	}

	/**
	 * @return whether the animation should repeat when it's finished
	 */
	public boolean isRepeating() {
		return m_repeat;
	}

	/**
	 * Sets whether the animation should repeat when it's finished
	 * 
	 * @param repeating
	 */
	public void setRepeating(boolean repeating) {
		m_repeat = repeating;
	}

	/**
	 * @return if the animation is finished play8ing
	 */
	public boolean isFinished() {
		return m_currentFrame == m_frames.size() - 1;
	}

	/**
	 * @return a modifieable List of the frames
	 */
	public List<Frame> getFrames() {
		return m_frames;
	}
}
