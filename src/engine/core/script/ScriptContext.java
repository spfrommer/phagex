package engine.core.script;

import engine.core.Entity;
import engine.core.Game;
import engine.core.Scene;

/**
 * A struct containing all the context a Script needs to run.
 */
public class ScriptContext {
	public Game game;
	public Scene scene;
	public Entity entity;

	public ScriptContext(Game game, Scene scene, Entity entity) {
		this.game = game;
		this.scene = scene;
		this.entity = entity;
	}
}
