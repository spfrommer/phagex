package engine.imp.render;

import gltools.texture.Texture2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the data for one Animation.
 */
public class Animation {
	private List<Texture2D> m_frames;

	// time since last update
	private int m_currentFrame;

	// time for a frame in milliseconds
	private float m_timePerFrame;
	private boolean m_repeat;

	private boolean m_isFirstFrame = true;

	/**
	 * Initializes an Animation with frames.
	 * 
	 * @param frames
	 */
	public Animation(List<Texture2D> frames) {
		if (frames == null)
			throw new AnimationException("Frames cannot be null!");
		m_frames = frames;
		m_currentFrame = 0;
		m_timePerFrame = 0.01f;
		m_repeat = false;
	}

	/**
	 * Creates a clone of an Animation.
	 * 
	 * @param animation
	 */
	public Animation(Animation animation) {
		m_frames = new ArrayList<Texture2D>();
		for (Texture2D m : animation.m_frames) {
			m_frames.add(m);
		}

		m_currentFrame = 0;
		m_timePerFrame = animation.getTimePerFrame();
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
	 * @return the time spent on each frame of the animation in milliseconds
	 */
	public float getTimePerFrame() {
		return m_timePerFrame;
	}

	/**
	 * Sets the time spent on each frame of the animation in milliseconds.
	 * 
	 * @param timePerFrame
	 */
	public void setTimePerFrame(float timePerFrame) {
		if (timePerFrame <= 0)
			throw new AnimationException("Time per frame must be greater than zero!");
		m_timePerFrame = timePerFrame;
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
	public List<Texture2D> getFrames() {
		return m_frames;
	}
}
