from engine.core import CTransform

def onSceneLoad():
	logger.out('Scene loaded!')
	
def update(time):
	if keyboard.isKeyPressed(keyboard.getKey("UP")) :
		transform.translate(0, 0.01)
	if keyboard.isKeyPressed(keyboard.getKey("DOWN")) :
		transform.translate(0, -0.01)
	if keyboard.isKeyPressed(keyboard.getKey("RIGHT")) :
		transform.translate(0.01, 0)
	if keyboard.isKeyPressed(keyboard.getKey("LEFT")) :
		transform.translate(-0.01, 0)
	if keyboard.isKeyPressed(keyboard.getKey("D")) :
		transform.rotate(-0.01);
	if keyboard.isKeyPressed(keyboard.getKey("A")) :
		transform.rotate(0.01);
	
def exit():
	logger.out('Exiting!')