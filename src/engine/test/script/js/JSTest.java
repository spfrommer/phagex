package engine.test.script.js;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JSTest {
	public static void main(String[] args) {
		/*Game game = new Game();
		Scene scene = new Scene(game);
		game.scenes().addScene(scene, "main");

		Entity test = scene.createEntity("test1asdfasdf", scene);
		// test.getCTransform().setTransform(new Transform2f(new Vector2f(1f, 0f), 0f, new Vector2f(1f, 1f)));

		// Entity test2 = scene.createEntity("test2", scene);

		SJavaScript script1 = new SJavaScript(new PhageScriptContext(game, scene, test));

		// SJavaScript script2 = new SJavaScript(new PhageScriptContext(game, scene, test2));

		script1.onSceneLoad();*/
		// script2.onSceneLoad();

		// game.start();
		// game.update(0.1666f);
		/*long lastTime = System.currentTimeMillis();
		List<ScriptEngine> engines = new ArrayList<ScriptEngine>();
		for (int i = 0; i < 1000; i++) {
			long curTime = System.currentTimeMillis();
			Logger.instance().out(curTime - lastTime);
			ScriptEngine engine = (new ScriptEngineManager()).getEngineByName("JavaScript");
			ScriptContext context = engine.getContext();
			context.setAttribute("name", "JavaScript", ScriptContext.ENGINE_SCOPE);

			engine.put("log", Logger.instance());

			engines.add(engine);
			lastTime = curTime;
		}
		Runtime runtime = Runtime.getRuntime();
		int mb = 1024 * 1024;
		Logger.instance().out("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);*/
		/*for (int i = 0; i < 1000; i++) 

			try {
				engine.eval("log.debug('Test')");
			} catch (ScriptException e) {
				e.printStackTrace();
			}
		}*/
		Context context = Context.enter();
		Scriptable scope = context.initStandardObjects();

		Object test = Context.javaToJS(new TestPrinter(), scope);
		ScriptableObject.putProperty(scope, "test", test);

		context.evaluateString(scope, "var s = test.getHelloString();", "<test>", 1, null);
		Context.exit();
	}
}