from engine.core import CTransform

logger.out('Initializing')
logger.out(entity.fields().get(CTransform.TRANSLATION));

def onSceneLoad():
	logger.out('Scene Loaded!')
	
def update(time):
	child = entity.tree().getChild('child')
	child.scripts().callFunc('damage', 5)