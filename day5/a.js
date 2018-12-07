#!/usr/bin/env nodejs

String.prototype.splice = function(start, length, insert) {
	return this.substring(0, start) + (insert || '') + this.substring(start + length)
};

const canReact = function(a, b) {
	return a !== b && (a.toLowerCase() === b || a === b.toLowerCase())
}

require('fs').readFile('input.txt', 'utf8', function(err, data) {
	if (err) throw err

	data = data.trim() // Input ends with a newline
	var pointer = 0

	while(pointer < data.length - 1) {
		// Check if we can react, and if so splice them out and back up
		if(canReact(data[pointer], data[pointer+1])) {
			data = data.splice(pointer, 2)
			pointer = Math.max(0, pointer - 1)
		} else {pointer += 1}
	}

	console.error(data)
	console.log(data.length)
});
