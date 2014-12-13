package engine.imp.render;

import glextra.material.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the data for one Animation.
 */
public class Animation {
	private List<Material> m_frames;

	// time since last update
	private int m_currentFrame;

	// time for a frame in second
	private float m_timePerFrame;
	private boolean m_repeat;

	/**
	 * Initializes an Animation with frames.
	 * 
	 * @param frames
	 */
	public Animation(List<Material> frames) {
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
		m_frames = new ArrayList<Material>();
		for (Material m : animation.m_frames) {
			m_frames.add(m);
		}

		m_currentFrame = 0;
		m_timePerFrame = animation.getTimePerFrame();
		m_repeat = animation.isRepeating();
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
	 * @return the time spent on each frame of the animation
	 */
	public float getTimePerFrame() {
		return m_timePerFrame;
	}

	/**
	 * Sets the time spent on each frame of the animation.
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
	public List<Material> getFrames() {
		return m_frames;
	}
}
