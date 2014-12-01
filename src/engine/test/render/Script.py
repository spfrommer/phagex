from engine.core import CTransform

def onSceneLoad():
	logger.out('Scene loaded!')
	
def update(time):
	entity.getCTransform().translate(0.001, 0.001);
	entity.getCTransform().rotate(0.01);
	entity.getCTransform().scale(0.999, 1.001);
	
def exit():
	logger.out('Exiting!');