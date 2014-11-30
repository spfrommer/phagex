package engine.core.script.test.py;

import commons.ResourceFactory;
import commons.ResourceLocator.ClasspathResourceLocator;

import engine.core.Entity;
import engine.core.Game;
import engine.core.Scene;
import engine.core.script.XPython;
import engine.core.script.XScriptContext;

public class PythonTest {
	public static void main(String[] args) {
		Game game = new Game();
		Scene scene = new Scene(game);
		game.scenes().addScene(scene, "main");

		Entity test = scene.createEntity("test1", scene);

		String code = ResourceFactory
				.readString(new ClasspathResourceLocator(), "engine/core/script/test/py/Script.py");

		XPython script1 = new XPython(code);
		script1.setContext(new XScriptContext(game, scene, test));
		script1.onSceneLoad();
	}
}
