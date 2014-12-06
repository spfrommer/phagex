import random;
from engine.core import CTransform

moveX = 0
moveY = 0
timePassed = 0

def calc_moves():
	global moveX
	global moveY
	moveX = random.uniform(-0.02, 0.02)
	moveY = random.uniform(-0.02, 0.02)
	
def update(time):
	global timePassed
	if (timePassed > 1000):
		calc_moves()
		timePassed = 0
	transform.translate(moveX, moveY)
	timePassed += time
	
calc_moves()