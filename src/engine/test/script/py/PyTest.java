package engine.test.script.py;

import commons.Resource;
import commons.ResourceFactory;
import commons.ResourceLocator.ClasspathResourceLocator;

import engine.core.Entity;
import engine.core.Game;
import engine.core.Scene;
import engine.core.script.XPython;

public class PyTest {
	public static void main(String[] args) {
		String parentCode = ResourceFactory.readString(new Resource(new ClasspathResourceLocator(),
				"engine/test/script/py/ParentScript.py"));
		String childCode = ResourceFactory.readString(new Resource(new ClasspathResourceLocator(),
				"engine/test/script/py/ChildScript.py"));

		Game game = new Game();
		Scene scene = new Scene(game);
		game.scenes().addScene(scene, "main");

		Entity test = scene.createEntity("test1", scene);
		Entity child = scene.createEntity("child", test);

		test.scripts().add(new XPython(parentCode));
		child.scripts().add(new XPython(childCode));

		game.start();
		game.update(0.166666666f);
	}
}
