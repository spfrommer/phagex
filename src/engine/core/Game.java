package engine.core;

public class Game {
	private SceneManager m_scene;

	public Game() {
		m_scene = new SceneManager();
	}

	public SceneManager scenes() {
		return m_scene;
	}
}
