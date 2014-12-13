package engine.imp.render;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import engine.core.Component;
import engine.core.ComponentBuilder;
import engine.core.exceptions.ComponentException;

/**
 * Containes the animation data for an Entity.
 */
public class CAnimation implements Component {
	public static final String NAME = "animation";
	public static final String ANIMATIONS = "animations";
	public static final String[] IDENTIFIERS = new String[] { ANIMATIONS };

	private Map<String, Animation> m_animations = new HashMap<String, Animation>();
	private Animation m_current;
	private String m_currentName;
	private float m_timeElapsed;

	/**
	 * Makes an empty animation component.
	 */
	public CAnimation() {

	}

	/**
	 * @return the elapsed time
	 */
	protected float getTimeElapsed() {
		return m_timeElapsed;
	}

	/**
	 * Sets the elapsed time.
	 * 
	 * @param time
	 */
	protected void setTimeElapsed(float time) {
		m_timeElapsed = time;
	}

	/**
	 * Adds an Animation.
	 * 
	 * @param name
	 * @param animation
	 */
	public void addAnimation(String name, Animation animation) {
		if (name == null)
			throw new AnimationException("Cannot add animation with null name!");
		if (animation == null)
			throw new AnimationException("Cannot add null Animation!");
		if (m_animations.containsKey(name))
			throw new AnimationException("Animation already exists for: " + name);

		m_animations.put(name, animation);
	}

	/**
	 * @param name
	 * @return the Animation corresponding to the name
	 */
	public Animation getAnimation(String name) {
		if (name == null)
			throw new AnimationException("Cannot get animation with null name!");
		if (!m_animations.containsKey(name))
			throw new AnimationException("Animation does not exist for: " + name);

		return m_animations.get(name);
	}

	/**
	 * @return the current Animation
	 */
	protected Animation getCurrentAnimation() {
		return m_current;
	}

	/**
	 * Sets the current Animation.
	 * 
	 * @param name
	 */
	public void playAnimation(String name) {
		m_current = getAnimation(name);
		m_current.setCurrentFrame(0);
		m_currentName = name;
		m_timeElapsed = 0;
	}

	/**
	 * @return all the Animation names.
	 */
	public Set<String> allNames() {
		return m_animations.keySet();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String[] getIdentifiers() {
		return IDENTIFIERS;
	}

	@Override
	public Object getData(String identifier) {
		if (identifier == null)
			throw new ComponentException("Cannot get data for null identifier!");

		if (identifier.equals(ANIMATIONS))
			return m_animations;

		throw new ComponentException("No such identifier!");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setData(String identifier, Object data) {
		if (identifier == null)
			throw new ComponentException("Cannot set data for null identifier!");
		if (data == null)
			throw new ComponentException("Cannot set null data for identifier: " + identifier);

		if (identifier.equals(ANIMATIONS)) {
			m_animations = (Map<String, Animation>) data;
		} else {
			throw new ComponentException("No data for identifier: " + identifier);
		}
	}

	@Override
	public ComponentBuilder<CAnimation> getBuilder() {
		ComponentBuilder<CAnimation> builder = new ComponentBuilder<CAnimation>() {
			@Override
			public CAnimation build() {
				CAnimation animation = new CAnimation();
				Set<String> key = m_animations.keySet();
				for (String s : key) {
					animation.addAnimation(s, new Animation(getAnimation(s)));
				}
				animation.playAnimation(m_currentName);
				return animation;
			}

			@Override
			public String getName() {
				return NAME;
			}
		};
		return builder;
	}
}
