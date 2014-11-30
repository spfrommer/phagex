logger.print('Initializing')

def onSceneLoad():
	logger.print('Scene Loaded!')
	
def update(time):
	child = entity.tree().getChild('child')
	child.scripts().callFunc("damage", 5)