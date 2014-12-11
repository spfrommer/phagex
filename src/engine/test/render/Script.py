from engine.core import CTransform

def onSceneLoad():
	logger.out('Scene loaded!')
	
def update(time):
	transform.translate(0.001, 0.001);
	transform.rotate(0.01);
	transform.scale(0.999, 1.001);
	logger.out(mouse.getX());
	
def exit():
	logger.out('Exiting!');