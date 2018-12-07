#!/usr/bin/env nodejs

String.prototype.splice = function(start, length, insert) {
	return this.substring(0, start) + (insert || '') + this.substring(start + length)
};

const canReact = function(a, b) {
	return a !== b && (a.toLowerCase() === b || a === b.toLowerCase())
}

require('fs').readFile('input.txt', 'utf8', function(err, originalData) {
	if (err) throw err

	// Find the sorted possible one by trying every option
	var lengths = []
	for(var i = 0; i < 26; i++) {
		data = originalData.trim().replace(new RegExp(String.fromCharCode(65 + i), 'gi'), '')
		var pointer = 0

		while(pointer < data.length - 1) {
			// Check if we can react, and if so splice them out and back up
			if(canReact(data[pointer], data[pointer+1])) {
				data = data.splice(pointer, 2)
				pointer = Math.max(0, pointer - 1)
			} else {pointer += 1}
		}

		lengths[i] = data.length
	}

	console.error(lengths)
	lengths = lengths.sort(function(a, b) {return a - b})
	console.log(lengths[0])
});
