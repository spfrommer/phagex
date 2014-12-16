from engine.core import CTransform

def onSceneLoad():
	logger.out("Scene loaded!")
	
def update(time):
	if keyboard.isKeyPressed(keyboard.getKey("UP")) :
		transform.translate(0, 0.001 * time)
	if keyboard.isKeyPressed(keyboard.getKey("DOWN")) :
		transform.translate(0, -0.001 * time)
	if keyboard.isKeyPressed(keyboard.getKey("RIGHT")) :
		transform.translate(0.001 * time, 0)
	if keyboard.isKeyPressed(keyboard.getKey("LEFT")) :
		transform.translate(-0.001 * time, 0)
	if keyboard.isKeyPressed(keyboard.getKey("D")) :
		transform.rotate(-0.001 * time)
	if keyboard.isKeyPressed(keyboard.getKey("A")) :
		transform.rotate(0.001 * time)
	if keyboard.isKeyPressed(keyboard.getKey("H")) :
		render.setVisible(False)
	if keyboard.isKeyPressed(keyboard.getKey("V")) :
		render.setVisible(True)
	
def exit():
	logger.out("Exiting!")