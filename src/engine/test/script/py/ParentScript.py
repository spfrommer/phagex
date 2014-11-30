logger.out('Initializing')

def onSceneLoad():
	logger.out('Scene Loaded!')
	
def update(time):
	child = entity.tree().getChild('child')
	child.scripts().callFunc("damage", 5)