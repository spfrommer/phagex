from engine.core import CTransform

def onSceneLoad():
	logger.out('Scene loaded!')
	
def update(time):
	logger.out('Updating entity: ' + str(time))
	
def exit():
	logger.out('Exiting!');