from engine.core import CTransform

def onSceneLoad():
	logger.out('Scene loaded!')
	
def update(time):
	if keyboard.isKeyPressed(keyboard.getKey("UP")) :
		transform.translate(0, 0.01);
	if keyboard.isKeyPressed(keyboard.getKey("DOWN")) :
		transform.translate(0, -0.01);
	
def exit():
	logger.out('Exiting!')