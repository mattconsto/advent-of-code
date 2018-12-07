#!/usr/bin/env python3

import datetime, operator, re, sys

lines = map(lambda s: s.strip(), sorted(list(open('input.txt', 'r'))))
current = -1
guards = {}

parser = re.compile(r'^\[([^\]]+)\] (.*)$')

# Parse input into a sensible structure
for line in lines:
	# This is overly complex
	time, message = parser.match(line).groups()
	time = datetime.datetime.strptime(time, '%Y-%m-%d %H:%M')

	if message == 'falls asleep':
		guards[current]['events'].append({'type':'sleep','time':time})
	elif message == 'wakes up':
		guards[current]['events'].append({'type':'wake','time':time})
	else:
		begin = re.match(r'^Guard #(\d+) begins shift$', message)
		if begin:
			current = int(begin.group(1))
			if not current in guards:
				guards[current] = {'id':current,'events':[],'breakdown':{}}
			guards[current]['events'].append({'type':'start','time':time})

# Calculate how sleepy each guard is
for id, guard in guards.items():
	start = None
	for event in guard['events']:
		if event['type'] == 'sleep':
			start = event['time']
		elif start:
			for m in range(int(start.timestamp()/60), int(event['time'].timestamp()/60)):
				if not (m%60) in guard['breakdown']: guard['breakdown'][m%60] = 0
				guard['breakdown'][m%60] += 1

			start = None

	if len(guard['breakdown'].items()) > 0:
		guard['max'] = max(guard['breakdown'].items(), key=operator.itemgetter(1))[0]
	else:
		guard['max'] = 0

# Find the sleepiest guard
sleepiest = max(guards.values(), key=lambda g: g['max'])

sys.stderr.write('The sleepiest guard is ' + str(sleepiest['id']) + "\n")
print(sleepiest['id'] * sleepiest['max'])
