from engine.core import CTransform

def onSceneLoad():
	logger.out("Scene loaded!")

lastChange = 0
scale = 0.995

def update(time):
	global lastChange
	global scale
	lastChange += time
	if (lastChange >= 1000) :
		lastChange = 0
		if (scale == 0.995) :
			scale = 1.005
		else :
			scale = 0.995
	
	transform.scale(scale, scale)
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